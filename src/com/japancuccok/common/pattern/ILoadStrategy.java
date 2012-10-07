package com.japancuccok.common.pattern;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.12.
 * Time: 23:42
 */
public interface ILoadStrategy<T> {

    public List<T> load();
    public void detach();

}
