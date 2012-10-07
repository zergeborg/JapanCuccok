package com.japancuccok.common.wicket.component;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.events.CartUpdate;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 23:40
 */
public class CartLabel extends Label {

    private static final long serialVersionUID = -6688960191149791905L;

    transient private static final Logger logger = Logger.getLogger(CartLabel.class.getName());

    public CartLabel(String id, IModel model) {
        super(id, model);
        setOutputMarkupId(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
    {
        logger.info("Replacing CartLabel component tag body");
        replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString() + " HUF");
    }

    @Override
    public void onEvent(IEvent<?> event) {
        logger.info("A new event arrived [ Type: "+ event.getType() +
                ", Payload type: " + event.getPayload().getClass().getSimpleName() +
                ", Payload: " + event.getPayload() + " ]");
        super.onEvent(event);

        if (event.getPayload() instanceof CartUpdate)
        {
            CartUpdate update = (CartUpdate)event.getPayload();
            update.getTarget().add(this);
        }
    }
}