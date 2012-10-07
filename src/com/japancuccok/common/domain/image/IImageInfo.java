package com.japancuccok.common.domain.image;

import com.japancuccok.common.domain.category.CategoryType;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 18:05
 */
public interface IImageInfo {

    Long getId();

    String getName();

    CategoryType getCategory();

    String getFileType();

    String getMimeType();

    String getDescription();

    ImageOptions getImageOptions();

    IImageData getImageData();
}
