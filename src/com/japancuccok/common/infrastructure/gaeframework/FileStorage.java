package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author uudashr
 *
 */
public class FileStorage<T> {
    private String entityType = "File";

    private static final FileStorage INSTANCE = new FileStorage();
    
    public static FileStorage instance() {
        return INSTANCE;
    }
    
    FileStorage() {}
    
    private DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();

    protected String getEntityType() {
        return entityType;
    }

    public void write(InputStream in, String name) throws IOException {
        Entity fileEntity = new Entity(getEntityType(), name);
        fileEntity.setProperty("name", name);
        Key fileKey = dsService.put(fileEntity);
        long size = storeByChunks(in, fileKey);
        fileEntity.setProperty("size", size);
        dsService.put(fileEntity);
    }
    
    public long size(String name) throws FileNotFoundException {
        try {
            Entity entity = dsService.get(KeyFactory.createKey(getEntityType(), name));
            return (Long)entity.getProperty("size");
        } catch (EntityNotFoundException e) {
            throw new FileNotFoundException("Cannot find file with name '" + name + "': " + e.getMessage());
        }
    }

    public List<T> list(Class<T> clazz) {
        List<T> entities = new ArrayList<T>();
        Objectify ofy = ObjectifyService.begin();
        com.googlecode.objectify.cmd.Query<T> q = ofy.load().type(clazz);
        for (T item: q) {
            System.out.println(item.toString());
            entities.add(item);
        }
        return entities;
    }

    public List<Entity> list() {
        Query query = new Query(getEntityType());
        query.addSort("name");
        PreparedQuery pQuery = dsService.prepare(query);
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity e : pQuery.asIterable()) {
            entities.add(e);
        }
        return entities;
    }
    
    public InputStream getFileInputStream(String fileName) throws FileNotFoundException {
        return new DatastoreInputStream(fileName);
    }
    
    public void delete(Key fileKey) {
        Query query = new Query("ChunkFile", fileKey);
        query.setKeysOnly();
        query.addSort("index");
        PreparedQuery pQuery = dsService.prepare(query);
        for (Entity e : pQuery.asIterable()) {
            dsService.delete(e.getKey());
        }
        dsService.delete(fileKey);
    }
    
    public void delete(String name) {
        Key fileKey = KeyFactory.createKey(getEntityType(), name);
        delete(fileKey);
    }
    
    private long storeByChunks(InputStream in, Key fileKey) throws IOException {
        byte[] buff = new byte[DatastoreOutputStream.DATA_CHUNK_SIZE];
        
        int totalReaded = 0;
        int index = 0;
        long totalSize = 0;
        while ((totalReaded = in.read(buff)) != -1) {
            Entity chunkEntity = new Entity("ChunkFile", "cf" + index, fileKey);
            chunkEntity.setProperty("index", index);
            chunkEntity.setUnindexedProperty("data", new Blob(buff.clone()));
            chunkEntity.setUnindexedProperty("length", totalReaded);
            dsService.put(chunkEntity);
            index++;
            totalSize += totalReaded;
        }
        return totalSize;
    }
    
}
