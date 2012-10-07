package com.japancuccok.common.events;

import com.japancuccok.common.domain.product.Product;
import com.japancuccok.main.cart.ProductCheckoutPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.08.
 * Time: 1:10
 */
public class SizeChoose implements Serializable {

    private static final long serialVersionUID = -2405427073906924735L;
    private final AjaxRequestTarget target;
    private final AjaxFallbackLink selectedSizeLink;
    private final Product.SIZE chosenSize;

    /**
     * Constructor
     *
     * @param target
     * @param chosenSize
     * @param selectedSizeLink
     */
    public SizeChoose(AjaxRequestTarget target, Product.SIZE chosenSize, AjaxFallbackLink selectedSizeLink)
    {
        this.target = target;
        this.chosenSize = chosenSize;
        this.selectedSizeLink = selectedSizeLink;
    }

    /** @return ajax request target */
    public AjaxRequestTarget getTarget()
    {
        return target;
    }

    public Product.SIZE getChosenSize() {
        return chosenSize;
    }

    public AjaxFallbackLink getSelectedSizeLink() {
        return selectedSizeLink;
    }

    @Override
    public String toString() {
        return "SizeChoose{" +
                "target=" + target +
                ", selectedSizeLink=" + selectedSizeLink +
                ", chosenSize=" + chosenSize +
                '}';
    }
}
