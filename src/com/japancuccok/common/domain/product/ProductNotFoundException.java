package com.japancuccok.common.domain.product;

import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 1:36
 */
public class ProductNotFoundException extends FileNotFoundException {

    private static final long serialVersionUID = -3880693870974028084L;

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String s) {
        super(s);
    }
}
