package com.japancuccok.common.events;

import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 23:31
 */
public class CartUpdate implements Serializable {

    private static final long serialVersionUID = 5720365557980962293L;
    private final AjaxRequestTarget target;
    private Long productId;

    /**
     * Constructor
     *
     * @param target
     * @param productId
     */
    public CartUpdate(AjaxRequestTarget target, Long productId)
    {
        this.target = target;
        this.productId = productId;
    }

    /** @return ajax request target */
    public AjaxRequestTarget getTarget()
    {
        return target;
    }

    public Long getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "CartUpdate{" +
                "target=" + target +
                ", productId=" + productId +
                '}';
    }
}
