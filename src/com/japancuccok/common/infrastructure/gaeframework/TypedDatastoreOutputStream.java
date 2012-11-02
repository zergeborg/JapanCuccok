package com.japancuccok.common.infrastructure.gaeframework;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 20:08
 */
public class TypedDatastoreOutputStream<T> extends DatastoreOutputStream<T> {

    public TypedDatastoreOutputStream(int length) {
        super(length);
    }

    /**
     * This is the counterpart of the flushTheCompleted() method. This method serves as a proxy which
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

        if (buffer.hasRemaining()) {
            buffer.put((byte)b);
        } else {
            flushTheCompleted();
            write(b);
        }
    }

    @Override
    public List<com.googlecode.objectify.Key<ChunkFile>> pop() {
        return super.pop();
    }

}
