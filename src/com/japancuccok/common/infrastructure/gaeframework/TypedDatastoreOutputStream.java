package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Entity;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 20:08
 */
public class TypedDatastoreOutputStream<T> extends DatastoreOutputStream<T> {

    public TypedDatastoreOutputStream(String fileName, Entity gaeEntity) {
        super(fileName, gaeEntity);
    }

    public TypedDatastoreOutputStream(String fileName) {
        super(fileName);
    }

    /**
     * This is the counterpart of the flushCompleted() method. This method serves as a proxy which
     * will eventually call the OutputStream#write(byte[]) method
     *
     * @param b The data to be written
     * @throws IOException
     */
    public void writeBytes(byte b[]) throws IOException {
        write(b);
    }

    @Override
    public void write(int b) throws IOException {
        checkClosed();
        if (getGaeEntity() == null) {
            throw new IllegalStateException("The GAE entity is missing... You should have " +
                    "provided it in the constructor.");
        }

        if (buffer.hasRemaining()) {
            buffer.put((byte)b);
            totalSize++;
        } else {
            /**
             * This is a dirty hack. The sole purpose of this method is calling its super method and saving
             * the keys produced during the last write operation. The saved keys will be used later during
             * calling the writeAndGetKeys() method
             * @return The collection of datastore keys
             * @see com.japancuccok.common.infrastructure.gaeframework.TypedDatastoreOutputStream#writeAndGetKeys(byte[])
             */
            flushCompleted();
        }
    }

}
