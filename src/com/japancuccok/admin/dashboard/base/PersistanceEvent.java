package com.japancuccok.admin.dashboard.base;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.09.
 * Time: 22:10
 */
public class PersistanceEvent<T, R> implements IPersistanceEvent<T, R> {
    
    private transient T wrappedObject;
    private IPersistanceResult<R> persistanceResult;

    public PersistanceEvent(T wrappedObject) {
        this.wrappedObject = wrappedObject;
    }

    @Override
    public T getPayload() {
        return wrappedObject;
    }

    @Override
    public void put(IPersistanceResult<R> persistanceResult) {
        this.persistanceResult = persistanceResult;
    }

    @Override
    public R getResult() {
        return persistanceResult.getPayload();
    }
}
