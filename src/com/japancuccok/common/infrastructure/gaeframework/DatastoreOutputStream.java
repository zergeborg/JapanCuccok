package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;
import org.apache.wicket.util.lang.Bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author uudashr
 *
 */
public class DatastoreOutputStream<T> extends OutputStream {
    public static final int DATA_CHUNK_SIZE = (int)(Bytes.megabytes(1).bytes() - Bytes.kilobytes(512).bytes());
    private GenericGaeDAO<ChunkFile> chunkFileDAO = (GenericGaeDAO<ChunkFile>) GenericGaeDAOFactory.getInstance(ChunkFile.class);
    private DatastoreService dataStoreService = CachingDatastoreServiceFactory.getDatastoreService();
    private boolean closed = false;
    protected ByteBuffer buffer = ByteBuffer.allocate(DATA_CHUNK_SIZE);
    private int chunkIndex = 0;
    int totalSize = 0;
    private final String fileName;
    private Entity gaeEntity;

    protected DatastoreOutputStream(String fileName, Entity gaeEntity) {
        this.fileName = fileName;
        if(gaeEntity == null) {
            throw new IllegalArgumentException("GAE entity must not be null!");
        }
        this.gaeEntity = gaeEntity;
    }

    public DatastoreOutputStream(String fileName) {
        this.fileName = fileName;
    }

    protected String getFileName() {
        return fileName;
    }

    protected DatastoreService getDataStoreService() {
        return dataStoreService;
    }

    protected Entity getGaeEntity() {
        return gaeEntity;
    }

    @Override
    public void write(int b) throws IOException {
        checkClosed();
        if (getGaeEntity() == null) {
            gaeEntity = new Entity("File", fileName);
            gaeEntity.setProperty("name", fileName);
            dataStoreService.put(gaeEntity);
        }

        if (buffer.hasRemaining()) {
            buffer.put((byte)b);
            totalSize++;
        } else {
            flushCompleted();
        }
    }

    protected void flushCompleted() {
        if (getGaeEntity() != null) {
            int length = buffer.position();
            byte[] data = new byte[length]; buffer.flip(); buffer.get(data);

            com.googlecode.objectify.Key<?> objectifyKey =
                    com.googlecode.objectify.Key.create(getGaeEntity().getKey());
            ChunkFile chunkFile = new ChunkFile("cf"+chunkIndex, new Blob(data),length, (com.googlecode.objectify.Key<BinaryImageData>) objectifyKey);
            chunkFileDAO.put(chunkFile);
            
            buffer.clear();
            
            // update size
            getGaeEntity().setProperty("size", new Long(totalSize));
            chunkIndex++;
        } else {
        }
    }
    
    protected void checkClosed() throws IOException {
        if (closed) {
            throw new IOException("Resource already closed");
        }
    }
    
    @Override
    public void close() throws IOException {
        checkClosed();
        if (buffer.hasRemaining()) {
            flushCompleted();
        }
        closed = true;
        buffer = null;
    }
    
    @Override
    protected void finalize() throws Throwable {
        buffer = null;
    }
}
