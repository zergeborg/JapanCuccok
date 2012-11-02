package com.japancuccok.db;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cache.CachingDatastoreServiceFactory;
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
        TypedDatastoreOutputStream typedStream = new TypedDatastoreOutputStream(blob.getBytes().length);
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
        return getBytes(object);
    }

    byte[] getBytes(IBinaryProvider binaryProvider) {
        DatastoreInputStream typedStream = null;
        typedStream = new DatastoreInputStream(binaryProvider.getChunkFileKeys());
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
