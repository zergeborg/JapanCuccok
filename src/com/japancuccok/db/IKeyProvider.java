package com.japancuccok.db;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 23:28
 */
public interface IKeyProvider<T> {
    
    public void setKey(Ref<T> key);

    public Ref<T> getKey();

    public Collection<Ref<T>> getKeys();

}
