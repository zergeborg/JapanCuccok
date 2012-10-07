package com.japancuccok.common.events;

import com.japancuccok.main.cart.ProductCheckoutPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.07.
 * Time: 23:30
 */
public class CartItemDelete implements Serializable {

    private static final long serialVersionUID = 2038994775558606444L;
    private final AjaxRequestTarget target;
    private transient ProductCheckoutPanel productCheckoutPanel;

    /**
     * Constructor
     *
     * @param target
     * @param productCheckoutPanel
     */
    public CartItemDelete(AjaxRequestTarget target, ProductCheckoutPanel productCheckoutPanel)
    {
        this.target = target;
        this.productCheckoutPanel = productCheckoutPanel;
    }

    /** @return ajax request target */
    public AjaxRequestTarget getTarget()
    {
        return target;
    }

    public ProductCheckoutPanel getProductCheckoutPanel() {
        return productCheckoutPanel;
    }
}
