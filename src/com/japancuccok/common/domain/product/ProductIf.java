package com.japancuccok.common.domain.product;

import com.japancuccok.common.domain.category.CategoryType;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:01
 */
public interface ProductIf {
    Long getId();

    String getName();

    CategoryType getCategory();

    String getDescription();

    int getPrice();

}
