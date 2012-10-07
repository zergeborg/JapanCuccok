package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.model.LoadableDetachableModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 16:12
 */
public class OrderedItemLoadStrategy extends LoadableDetachableModel<List<Long>> implements Serializable {

    private static final long serialVersionUID = -2975583912333257763L;

    public OrderedItemLoadStrategy() {
    }

    @Override
    public List<Long> load() {
        return new ArrayList<Long>();
    }

}
