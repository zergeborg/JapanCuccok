package com.japancuccok.db;

import com.googlecode.objectify.ObjectifyService;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 10:29
 */
public final class GenericGaeDAOFactory {

    private static HashMap<Class<?>, GenericGaeDAOIf<?>> genericDaoMap = null;

    private GenericGaeDAOFactory() {
    }

    public static GenericGaeDAOIf<?> getInstance(Class<?> clazz) {
        synchronized (GenericGaeDAOFactory.class) {
            if (genericDaoMap == null) {
                genericDaoMap = new HashMap<Class<?>, GenericGaeDAOIf<?>>();
            }
            if (!genericDaoMap.containsKey(clazz)) {
                genericDaoMap.put(clazz, new GenericGaeDAO(clazz, true));
                ObjectifyService.register(clazz);
            }
        }
        return (GenericGaeDAOIf<?>) genericDaoMap.get(clazz);
    }
}
