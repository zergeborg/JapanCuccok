package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import org.apache.wicket.model.LoadableDetachableModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.12.
 * Time: 23:54
 */
public abstract class AbstractLoadStrategy<T> extends LoadableDetachableModel<List<T>> implements Serializable {

    private static final long serialVersionUID = 3957867693741442293L;

    protected final boolean all;
    protected final CategoryType categoryType;

    protected AbstractLoadStrategy(CategoryType categoryType) {
        this.categoryType = categoryType;
        this.all = false;
    }

    protected AbstractLoadStrategy(boolean all) {
        this.all = all;
        this.categoryType = null;
    }

    @Override
    public abstract List<T> load();

}
