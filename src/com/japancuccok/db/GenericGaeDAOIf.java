package com.japancuccok.db;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 10:26
 */
public interface GenericGaeDAOIf<T> {

    /**
     * Saves a singly entity to the datastore in a single asynchronous batch operation.
     *
     * @param object The object to be put
     * @return The saved entity's key
     */
    public Key<T> put(T object);

    /**
     * Saves multiple entities to the datastore in a single asynchronous batch operation.
     *
     * @param objects The objects to be put
     * @return The collection of the saved entities' keys
     */
    Map<Key<T>, T> put(Iterable<T> objects);

    /**
     * Saves multiple entities to the datastore in a single asynchronous batch operation.
     *
     *
     * @param objects The objects to be put
     * @return The collection of the saved entities' keys
     */
    public Map<Key<T>, T> put(T... objects);

    /**
     * This method will return all the objects with the given keys in a single round
     *
     * @param objects The key(s) of the desired object(s)
     */
    public Map<Key<T>, T> get(T... objects);

    /**
     * Deletes the specified entity.
     *
     * @param object The entity to be deleted
     * @return The deleted object with initialized ID and other fields
     */
    public T delete(T object);

    /**
     * Deletes the specified entiti(es).
     *
     * @param objects The entiti(es) to be deleted
     */
    public void delete(T... objects);

    /**
     * This method will return all the objects with the given keys in a single round
     *
     * @param objects The key(s) of the desired object(s)
     */
    public Map<Key<T>, T> get(Key<T>... objects);

    /**
     * Deletes the specified entiti(es).
     *
     * @param objects The entiti(es) to be deleted
     */
    public void delete(Key<T>... objects);

   /**
     * Saves asll the entities to the datastore in a single parallel batch operation.
     *
     * @param objects The objects to be put
     */
    public Collection<Key<T>> putAll(Collection<T> objects);

    /**
     * This method will return all the objects with the given keys in a single round
     *
     * @param objects The key(s) of the desired object(s)
     */
    public Map<Key<T>, T> getAll(Collection<T> objects);

    /**
     * Deletes all the specified entiti(es).
     *
     * @param objects The entiti(es) to be deleted
     */
    public void deleteAll(Collection<T> objects);

    /**
     * Returns all the partial entities of the actual type
     *
     */
    public List<T> list();

    /**
     * Searches for the given entity by value
     *
     */
    public T find(T object);

    /**
     * Searches for the given entity by key
     *
     */
   public T find(Key<T> object);


    /**
     * Searches entities of the given class (clazzz) with the given parent key
     *
     * @param key The parent key
     * @return The list of entities of type T
     */
    public <E> List<T> getChilds(Key<E> key);

    /**
     * Returns all the full entities belonging to the given load group
     *
     * @param loadGroupClazz The load group(s) to be used during loading
     * @return The list of fully initialized objects
     */
    public <E> List<T> load(Class<E>... loadGroupClazz);

    /**
     * Returns a fully loaded entity of the given object belonging to the given load group
     *
     * @param object The given object that should be fully loaded
     * @param loadGroupClazz The load group(s) to be used during loading
     * @return The list of fully initialized objects
     */
    public <E> T load(T object, Class<E>... loadGroupClazz);

    /**
     * Returns fully loaded entity of the given entity type
     *
     * @param object The actual entity id
     * @return The entity to be found
     */
    public T load(T object);

    /**
     *
     * Loads the given object
     *
     * @param IBinaryProvider
     * @return
     */
    public byte[] loadBinary(IBinaryProvider IBinaryProvider);

    /**
     *
     * Stores the given object and returns the Objectify keys of the stored elements
     *
     * @param blob
     */
    public List<Key<ChunkFile>> saveBinary(Blob blob);

    /**
     *
     * Deletes the given object
     *
     * @param IBinaryProvider
     */
    public void deleteBinary(IBinaryProvider IBinaryProvider);
}
