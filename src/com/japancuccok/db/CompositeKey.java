package com.japancuccok.db;

import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 0:51
 */
public class CompositeKey<T> {

    private static final long serialVersionUID = -3542554614958857474L;

    private List<Key<?>> keyList = new ArrayList<Key<?>>();

    public CompositeKey() {
    }

    public CompositeKey(List<Key<?>> keyList) {
        this.keyList = keyList;
    }

    public void add(Key<?> key) {
        keyList.add(key);
    }

    public void remove(Key<?> key) {
        keyList.remove(key);
    }

    public void addAll(Collection<Key<?>> keys) {
        keyList.addAll(keys);
    }

    public void removeAll(Collection<Key<?>> keys) {
        keyList.removeAll(keys);
    }
    
    public List<Key<?>> getList() {
        return keyList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeKey)) return false;
        if (!super.equals(o)) return false;

        CompositeKey that = (CompositeKey) o;

        if (!keyList.equals(that.keyList)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + keyList.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CompositeKey{" +
                "keyList=" + keyList +
                '}';
    }
}
