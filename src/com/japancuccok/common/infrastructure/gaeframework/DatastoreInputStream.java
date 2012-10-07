package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.List;

/**
 * @author uudashr
 *
 */
public class DatastoreInputStream extends InputStream {

    private final GenericGaeDAO<ChunkFile> chunkFileDAO = (GenericGaeDAO<ChunkFile>)GenericGaeDAOFactory.getInstance(ChunkFile.class);
    private final DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();
    private final MemcacheService mcService = MemcacheServiceFactory.getMemcacheService();
    
    private boolean closed;
    private final Iterator<ChunkFile> entities;
    private ByteBuffer buffer;
    private String fileName;
    private int counter = 0;

    public DatastoreInputStream(Key fileKey) throws FileNotFoundException {
        List<ChunkFile> chunkFiles = load(fileKey);
        entities = chunkFiles.iterator();
    }

    public DatastoreInputStream(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        com.google.appengine.api.datastore.Key fileKey = KeyFactory.createKey("File", fileName);
        checkFileExists(fileKey);
        List<ChunkFile> chunkFiles = load(Key.create(fileKey));
        entities = chunkFiles.iterator();
    }

    public Iterator<ChunkFile> getEntities() {
        return entities;
    }

    private List<ChunkFile> load(Key objectifyKey) throws FileNotFoundException {
        List<ChunkFile> chunkFiles = chunkFileDAO.getChilds(objectifyKey);
        Iterator<ChunkFile> chunkFileIterator = chunkFiles.iterator();
        while(chunkFileIterator.hasNext()) {
            Object object = chunkFileIterator.next();
            if(!(object instanceof ChunkFile)) {
                chunkFileIterator.remove();
            }
        }
        return chunkFiles;
    }

    private void checkFileExists(com.google.appengine.api.datastore.Key fileKey) throws FileNotFoundException {
        try {
            // make sure file is exists
            dsService.get(fileKey);
        } catch (EntityNotFoundException e) {
            // we might have put it in the memcache instead of the datastore
            Entity entity = (Entity) mcService.get(fileKey);
            if(entity == null) {
                throw new FileNotFoundException("Cannot find file with key '" + fileKey + "': " + e.getMessage());
            }

        }
    }

    @Override
    public int read() throws IOException {
        checkClosed();
        if (buffer == null || !buffer.hasRemaining()) {
            if (entities.hasNext()) {
                ChunkFile chunkFile = entities.next();
                Blob blobData = chunkFile.data;
                buffer = ByteBuffer.wrap(blobData.getBytes());
                counter++;
            } else {
                return -1;
            }
        }
        
        // TODO uudashr: the might me empty data
        int b = 0xff & buffer.get();
        if (b < 0) {
            // logger.debug("Got " + b);
        }
        return b;
    }
    
    private void checkClosed() throws IOException {
        if (closed) {
            throw new IOException("Resource already closed");
        }
    }
    
    @Override
    public void close() throws IOException {
        checkClosed();
        closed = true;
        buffer = null;
    }
    
    @Override
    protected void finalize() throws Throwable {
        buffer = null;
    }
    
    @Override
    public String toString() {
        return "DatastoreInputStream for " + fileName;
    }
}
