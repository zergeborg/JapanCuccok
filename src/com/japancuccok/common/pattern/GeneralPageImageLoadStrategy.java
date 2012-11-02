package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.IImage;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.01.
 * Time: 22:12
 */
public class GeneralPageImageLoadStrategy<T extends IImage> extends ImageLoadStrategy<T> {
    
    private static final long serialVersionUID = 5585531801724921810L;

    public GeneralPageImageLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

}
