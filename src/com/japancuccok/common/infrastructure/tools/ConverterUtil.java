package com.japancuccok.common.infrastructure.tools;

import com.googlecode.objectify.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.20.
 * Time: 0:37
 */
public class ConverterUtil {
    
    public static <T> T getSingleEntity(Map<Key<T>, T> map) {
        T result = null;
        if(!map.isEmpty()) {
            List<Key<T>> keys = new ArrayList<Key<T>>();
            for(Key<T> imageKey : map.keySet()) {
                keys.add(imageKey);
            }
            if(keys.isEmpty()) return null;
            result = map.get(keys.get(0));
        }
        return result;
    }

    public static <T> Key<T> findEntityKey(Map<Key<T>, T> map, T entity) {
        Key<T> result = null;
        if(map.containsValue(entity)) {
            for(Map.Entry<Key<T>, T> entry : map.entrySet()) {
                if(entry.getValue().equals(entity)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

}
