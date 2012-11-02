package com.japancuccok.db;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 19:45
 */
public interface IBinaryProvider<T> extends Identifiable {

    public void setBytes(byte[] bytes);

    public byte[] getBytes();

    Blob getRawData();

    public List<Key<ChunkFile>> getChunkFileKeys();

}
