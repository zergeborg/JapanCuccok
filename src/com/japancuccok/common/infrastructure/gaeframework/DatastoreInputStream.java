package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.chunkFileDAO;

/**
 * @author uudashr
 *
 */
public class DatastoreInputStream extends InputStream {

    private transient static final Logger logger =
            Logger.getLogger(DatastoreInputStream.class.getName());

    private final DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();
    private final MemcacheService mcService = MemcacheServiceFactory.getMemcacheService();
    
    private boolean closed;
    private final Iterator<ChunkFile> chunkFiles;
    private ByteBuffer buffer;
    private String fileName;
    private int counter = 0;

    public DatastoreInputStream(List<ChunkFile> chunkFileList) {
       Collections.sort(chunkFileList);
       this.chunkFiles = chunkFileList.iterator();
       logger.info("Loading chunkFiles based on chunk file keys");
    }

    public DatastoreInputStream(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        logger.info("Loading chunkFiles based on the following file name: " + fileName);
        com.google.appengine.api.datastore.Key fileKey = KeyFactory.createKey("File", fileName);
        checkFileExists(fileKey);
        List<ChunkFile> chunkFileList = load(Key.create(fileKey));
        Collections.sort(chunkFileList);
        this.chunkFiles = chunkFileList.iterator();
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
            if (chunkFiles.hasNext()) {
                ChunkFile chunkFile = chunkFiles.next();
                logger.info("Current ChunkFile is " + chunkFile);
                Blob blobData = chunkFile.data;
                byte[] blobBytes = blobData.getBytes();
                buffer = ByteBuffer.wrap(blobBytes);
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
