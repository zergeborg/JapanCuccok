package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
import org.apache.wicket.util.lang.Bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.chunkFileDAO;

/**
 * @author uudashr
 *
 */
public class DatastoreOutputStream<T> extends OutputStream {

    public static final int DATA_CHUNK_SIZE =
            (int)(Bytes.megabytes(1).bytes() - Bytes.kilobytes(512).bytes());

    protected ByteBuffer buffer =
            ByteBuffer.allocate(DATA_CHUNK_SIZE);

    int completeFileSize = 0;

    private static LinkedList<com.googlecode.objectify.Key<ChunkFile>> chunkFileKeys =
            new LinkedList<com.googlecode.objectify.Key<ChunkFile>>();
    private transient static final Logger logger =
            Logger.getLogger(DatastoreOutputStream.class.getName());
    private DatastoreService dataStoreService =
            CachingDatastoreServiceFactory.getDatastoreService();
    private boolean closed = false;

    public DatastoreOutputStream(int completeFileSize) {
        this.completeFileSize = completeFileSize;
    }

    @Override
    public void write(int b) throws IOException {
        checkClosed();

        if (buffer.hasRemaining()) {
            buffer.put((byte)b);
        } else {
            flushTheCompleted();
            write(b);
        }
    }

    protected void flushTheCompleted() {
        logger.info("Buffer is full. Flushing...");

        int length = buffer.position();
        byte[] data = new byte[length]; buffer.flip(); buffer.get(data);

        ChunkFile chunkFile =
                new ChunkFile(new Blob(data), length, completeFileSize);
        logger.info(chunkFile+" created");
        com.googlecode.objectify.Key<ChunkFile> chunkFileKey =
                chunkFileDAO.put(chunkFile);
        logger.info(chunkFile+" is about to save asynchronously");

        buffer.clear();

        synchronized (DatastoreOutputStream.class) {
            chunkFileKeys.add(chunkFileKey);
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
        List<com.googlecode.objectify.Key<ChunkFile>> list;
        synchronized (DatastoreOutputStream.class) {
            list = new ArrayList<Key<ChunkFile>>(chunkFileKeys);
            chunkFileKeys.clear();
        }
        return list;
    }
}
