package com.japancuccok.admin.dashboard.base;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.09.
 * Time: 22:14
 */
public class PersistanceResult<T> implements IPersistanceResult<T> {
    
    private transient T wrappedResult;

    public PersistanceResult(T wrappedResult) {
        this.wrappedResult = wrappedResult;
    }

    @Override
    public T getPayload() {
        return wrappedResult;
    }
}
