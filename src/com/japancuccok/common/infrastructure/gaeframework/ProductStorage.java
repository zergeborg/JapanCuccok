package com.japancuccok.common.infrastructure.gaeframework;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 1:32
 */
public class ProductStorage extends FileStorage {

    private ProductStorage() {
        super();
    }
    
    @Override
    protected String getEntityType() {
        return "Product";
    }
}
