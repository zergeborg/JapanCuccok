package com.japancuccok.db;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import com.googlecode.objectify.impl.ConcreteEntityMetadata;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreInputStream;
import com.japancuccok.common.infrastructure.gaeframework.TypedDatastoreOutputStream;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.util.io.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.20.
 * Time: 20:44
 */
public class BinaryGaeDAO<T> {

    private final Objectify objectify;
    private final Class<T> clazz;
    final DatastoreService dsService;

    public BinaryGaeDAO(Class<T> clazz, Objectify objectify) {
        this.clazz = clazz;
        this.objectify = objectify;
        this.dsService = CachingDatastoreServiceFactory.getDatastoreService();
    }

    List<Key<ChunkFile>> saveDirectlyIntoDatastore(Blob blob) {
        TypedDatastoreOutputStream typedStream = new TypedDatastoreOutputStream();
        try {
            typedStream.writeBytes(blob.getBytes());
        } catch (IOException e) {
            throw new WicketRuntimeException("Could not save the given object [" + blob + " ]", e);
        } finally {
            try {
                typedStream.close();
            } catch (IOException e) {
                //
            }
        }
        return typedStream.pop();
    }

    byte[] loadDirectlyFromDatastore(IBinaryProvider object) {
        ConcreteEntityMetadata entityMetadata = new ConcreteEntityMetadata(new ObjectifyFactory()
                , object.getClass());
        Entity entity = entityMetadata.getKeyMetadata().initEntity(object);
        return getBytes(entity.getKey());
    }

//    void deleteDirectlyFromDatastore(Entity entity) {
//        DatastoreInputStream dsInputStream = null;
//        try {
//            dsInputStream = new DatastoreInputStream(entity.getKey());
//            Iterator<ChunkFile> iterator = dsInputStream.getEntities();
//            while(iterator.hasNext()) {
//                Entity nextEntity = iterator.next();
//                dsService.delete(nextEntity.getKey());
//            }
//            dsService.delete(entity.getKey());
//        } catch (FileNotFoundException e) {
//            throw new WicketRuntimeException("Could not delete the given object " +
//                    "with the following key: [ " + entity.getKey().getName() + " ]", e);
//        } finally {
//            if(dsInputStream != null) {
//                try {
//                    dsInputStream.close();
//                } catch (IOException e) {
//                    //
//                }
//            }
//        }
//    }

    byte[] getBytes(com.google.appengine.api.datastore.Key entityKey) {
        DatastoreInputStream typedStream = null;
        try {
            typedStream = new DatastoreInputStream(Key.create(entityKey));
        } catch (FileNotFoundException e) {
            throw new WicketRuntimeException("Could not load the given object " +
                    "with the following key: [ " + entityKey.getName() + " ]", e);
        }
        return getImageData(typedStream);
    }

    private byte[] getImageData(DatastoreInputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int b = 0;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            byte[] data = out.toByteArray();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new WicketRuntimeException("Could not load the given object ", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new WicketRuntimeException("Could not load the given object ", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }


}
