package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import com.googlecode.objectify.impl.ConcreteEntityMetadata;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;
import org.apache.wicket.util.lang.Bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author uudashr
 *
 */
public class DatastoreOutputStream<T> extends OutputStream {

    public static final int DATA_CHUNK_SIZE =
            (int)(Bytes.megabytes(1).bytes() - Bytes.kilobytes(512).bytes());

    protected ByteBuffer buffer =
            ByteBuffer.allocate(DATA_CHUNK_SIZE);

    int totalSize = 0;

    private static long chunkIndex = 1;
    private static LinkedList<com.googlecode.objectify.Key<ChunkFile>> chunkIndexes =
            new LinkedList<com.googlecode.objectify.Key<ChunkFile>>();
    private transient static final Logger logger =
            Logger.getLogger(DatastoreOutputStream.class.getName());
    private GenericGaeDAO<ChunkFile> chunkFileDAO =
            (GenericGaeDAO<ChunkFile>) GenericGaeDAOFactory.getInstance(ChunkFile.class);
    private DatastoreService dataStoreService =
            CachingDatastoreServiceFactory.getDatastoreService();
    private boolean closed = false;
    private Entity gaeEntity;

    public DatastoreOutputStream() {
        Entity entity;

        ConcreteEntityMetadata entityMetadata =
                new ConcreteEntityMetadata(new ObjectifyFactory(),
                        ChunkFile.class);

        synchronized (DatastoreOutputStream.class) {
            entity = new Entity(entityMetadata.getKeyMetadata().getKind(),
                    chunkIndex);
        }

        this.gaeEntity = entity;
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

        if (buffer.hasRemaining()) {
            buffer.put((byte)b);
            totalSize++;
        } else {
            flushTheCompleted();
        }
    }

    protected void flushTheCompleted() {
        logger.info("Buffer is full. Flushing...");

        int length = buffer.position();
        byte[] data = new byte[length]; buffer.flip(); buffer.get(data);

        com.googlecode.objectify.Key<?> objectifyKey =
                com.googlecode.objectify.Key.create(getGaeEntity().getKey());
        ChunkFile chunkFile =
                new ChunkFile("cf"+chunkIndex,
                        new Blob(data),length, (com.googlecode.objectify.Key<BinaryImageData>) objectifyKey);
        logger.info(chunkFile+" created");
        com.googlecode.objectify.Key<ChunkFile> chunkFileKey =
                chunkFileDAO.put(chunkFile);
        logger.info(chunkFile+" is about to save asynchronously");

        buffer.clear();

        // update size
        getGaeEntity().setProperty("size", new Long(totalSize));
        chunkIndex++;
        chunkIndexes.addFirst(chunkFileKey);
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
            flushTheCompleted();
        }
        closed = true;
        buffer = null;
    }
    
    @Override
    protected void finalize() throws Throwable {
        buffer = null;
    }

    /**
     * Returns all the latest available chunk file indexes on the stack and clears
     * the collection.
     *
     * @return
     */
    protected List<com.googlecode.objectify.Key<ChunkFile>> pop() {
        List<com.googlecode.objectify.Key<ChunkFile>> list =
                new ArrayList<Key<ChunkFile>>(chunkIndexes);
        chunkIndexes.clear();
        return list;
    }
}
