package com.japancuccok.db;

import com.google.appengine.api.datastore.Blob;

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

}
