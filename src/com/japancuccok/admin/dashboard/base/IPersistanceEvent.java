package com.japancuccok.admin.dashboard.base;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.09.
 * Time: 20:47
 */
public interface IPersistanceEvent<T, R> extends IEvent {

    public T getPayload();
    public void put(IPersistanceResult<R> result);
    public R getResult();
}
