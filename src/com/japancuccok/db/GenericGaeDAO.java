package com.japancuccok.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.ReadPolicy;
import com.googlecode.objectify.*;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.SimpleQuery;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreInputStream;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 10:25
 */
public final class GenericGaeDAO<T> implements GenericGaeDAOIf<T> {

    transient private static final Logger logger = Logger.getLogger(GenericGaeDAO.class.getName());
    private final Objectify objectify;
    private final Class<T> clazz;
    private final BinaryGaeDAO<T> binaryDao;

    public GenericGaeDAO(Class<T> clazz, boolean cacheNeeded) {
        this.clazz = clazz;
        this.objectify = ObjectifyService.begin().consistency(ReadPolicy.Consistency.EVENTUAL).cache(cacheNeeded);
        this.binaryDao = new BinaryGaeDAO<T>(clazz, objectify);
    }

    @Override
    public Key<T> put(T object) {
        synchronized (this) {
            return objectify.save().entity(object).now();
        }
    }

    @Override
    public Map<Key<T>, T> put(Iterable<T> objects) {
        synchronized (this) {
            return objectify.save().entities(objects).now();
        }
    }

    @Override
    public Map<Key<T>, T> put(T... objects) {
        synchronized (this) {
            return objectify.save().entities(objects).now();
        }
    }

    @Override
    public Map<Key<T>, T> get(T... objects) {
        synchronized (this) {
            return objectify.load().entities(Arrays.asList(objects));
        }
    }

    @Override
    public T delete(T object) {
        synchronized (this) {
            return (T) objectify.delete().entity(object).now();
        }
    }

    @Override
    public void delete(T... objects) {
        synchronized (this) {
            objectify.delete().entities(objects).now();
        }
    }

    @Override
    public Map<Key<T>, T> get(Key<T>... objects) {
        synchronized (this) {
            return objectify.load().keys(objects);
        }
    }

    @Override
    public void delete(Key<T>... objects) {
        synchronized (this) {
            objectify.delete().keys(objects).now();
        }
    }

    @Override
    public Collection<Key<T>> putAll(Collection<T> objects) {
        Result<Map<Key<T>, T>> resultMap;
        synchronized (this) {
            resultMap = objectify.save().entities(objects);
        }
        return resultMap.now().keySet();
    }

    @Override
    public Map<Key<T>, T> getAll(Collection<T> objects) {
        synchronized (this) {
            return objectify.load().values(objects);
        }
    }

    @Override
    public <E> List<T> getChilds(Key<E> key) {
        SimpleQuery<Object> query;
        synchronized (this) {
            query = objectify.load().ancestor(key);
        }
        return (List<T>) query.list();
    }

    @Override
    public <E> List<T> load(Class<E>... loadGroupClazz) {
        synchronized (this) {
            return objectify.load().group(loadGroupClazz).type(clazz).list();
        }
    }

    @Override
    public <E> T load(T object, Class<E>... loadGroupClazz) {
        synchronized (this) {
            Ref<T> storedValue = objectify.load().group(loadGroupClazz).value(object);
            return storedValue.get();
        }
    }

    @Override
    public T load(T object) {
        synchronized (this) {
            Ref<T> storedValue = objectify.load().type(clazz).filterKey(object).first();
            return storedValue.get();
        }
    }

    @Override
    public byte[] loadBinary(IBinaryProvider IBinaryProvider) {
        synchronized (this) {
            return binaryDao.loadDirectlyFromDatastore(IBinaryProvider);
        }
    }

    @Override
    public void saveBinary(IBinaryProvider IBinaryProvider) {
        synchronized (this) {
            // TODO: this whole process should be in a single (or two) transactions
            // TODO: in a single transaction first the datastore
            // First we put the image metadata into the DS
            Key<T> rawKey = put((T) IBinaryProvider);
            // So later the binary dao doesn't need to construct a key
            binaryDao.saveDirectlyIntoDatastore(IBinaryProvider, rawKey);
        }
    }

    @Override
    public void deleteBinary(IBinaryProvider IBinaryProvider) {
        synchronized (this) {
            // First we get the key of the image metadata from DS
            Key<?> entityKey = Key.create(clazz, IBinaryProvider.getId());
            DatastoreService cachingDatastoreService = CachingDatastoreServiceFactory.getDatastoreService();
            Entity entity = null;
            try {
                // Note that we are using caching DS here
                // Otherwise this call would result null
                entity = cachingDatastoreService.get(entityKey.getRaw());
            } catch (EntityNotFoundException e) {
                logger.severe("Delete failed");
                logger.severe("No entity was found with the following ID ["+ IBinaryProvider.getId()+"]");
                logger.severe(toString(e));
                return;
            }
            DatastoreInputStream dsInputStream = null;
            try {
                dsInputStream = new DatastoreInputStream(Key.create(entity.getKey()));
                Iterator<ChunkFile> iterator = dsInputStream.getEntities();
                GenericGaeDAO<ChunkFile> chunkFileDAO = (GenericGaeDAO<ChunkFile>)GenericGaeDAOFactory.getInstance(ChunkFile.class);
                while (iterator.hasNext()) {
                    ChunkFile chunkFile = iterator.next();
                    chunkFileDAO.delete(chunkFile);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            delete((T) IBinaryProvider);
        }
    }

    @Override
    public void deleteAll(Collection<T> objects) {
        synchronized (this) {
            objectify.delete().entities(objects).now();
        }
    }

    @Override
    public List<T> list() {
        Query<T> query;
        synchronized (this) {
            query = objectify.load().type(clazz);
        }
        return query.list();
    }

    @Override
    public T find(T object) {
        synchronized (this) {
            return (T) objectify.load().value(object).safeGet();
        }
    }

    @Override
    public T find(Key<T> object) {
        synchronized (this) {
            return objectify.load().key(object).safeGet();
        }
    }

    private String toString(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
