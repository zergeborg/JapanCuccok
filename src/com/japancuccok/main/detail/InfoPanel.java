package com.japancuccok.main.detail;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.events.SizeChoose;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.08.
 * Time: 22:19
 */
public class InfoPanel extends Panel {

    private static final long serialVersionUID = -279474338189185070L;

    private List<AjaxFallbackLink> linkList = new ArrayList<AjaxFallbackLink>();
    private class InfoPanelLink extends AjaxFallbackLink<Product> {

        private static final long serialVersionUID = -8795938693384846931L;

        private Product.SIZE productSize;
        private final FeedbackPanel feedbackPanel;

        private InfoPanelLink(String id, IModel<Product> iModel, Product.SIZE productSize,
                              FeedbackPanel feedbackPanel) {
            super(id, iModel);
            this.productSize = productSize;
            this.feedbackPanel = feedbackPanel;
        }

        @Override
            public void onClick(AjaxRequestTarget target) {
            if (target != null) {
                target.add(feedbackPanel);
                info(getString("sizeChosen", new Model<Serializable>(this)));
                send(getPage(), Broadcast.DEPTH, new SizeChoose(target, productSize, this));
            } else {
                error("No ajax!");
            }
        }

    }

    public InfoPanel(String id, FeedbackPanel feedbackPanel, IModel<Product> model) {
        super(id, model);
        setOutputMarkupId(true);
        add(new Label("selectedProductName"
                , new PropertyModel<Product>(getDefaultModel(), "name")));
        add(new Label("selectedProductPrice"
                , new PropertyModel<Product>(getDefaultModel(), "price")));
        add(new Label("description"
                , new PropertyModel<Object>(getDefaultModel(), "description")));
        InfoPanelLink sLink = new InfoPanelLink("S", (IModel<Product>) getDefaultModel(),
                Product.SIZE.S, feedbackPanel);
        add(sLink);
        InfoPanelLink mLink = new InfoPanelLink("M", (IModel<Product>) getDefaultModel(),
                Product.SIZE.M, feedbackPanel);
        add(mLink);
        InfoPanelLink lLink = new InfoPanelLink("L", (IModel<Product>) getDefaultModel(),
                Product.SIZE.L, feedbackPanel);
        add(lLink);
        InfoPanelLink xlLink = new InfoPanelLink("XL", (IModel<Product>) getDefaultModel(),
                Product.SIZE.XL, feedbackPanel);
        add(xlLink);
        InfoPanelLink xxlLink = new InfoPanelLink("XXL", (IModel<Product>) getDefaultModel(),
                Product.SIZE.XXL, feedbackPanel);
        add(xxlLink);
        linkList.add(sLink);
        linkList.add(mLink);
        linkList.add(lLink);
        linkList.add(xlLink);
        linkList.add(xxlLink);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        // check if this is a item delete update event and if so repaint self
        if (event.getPayload() instanceof SizeChoose)
        {
            SizeChoose update = (SizeChoose)event.getPayload();
            changeSelected(update.getTarget(), update.getSelectedSizeLink(), getLinkList());
            update.getTarget().add(this);
        }
    }

    private void changeSelected(AjaxRequestTarget target, AjaxFallbackLink selected, List
            <AjaxFallbackLink> linkList) {
        for(AjaxFallbackLink link : linkList) {
            if(link.equals(selected)) {
                link.add(new AttributeModifier("class", "selected"));
            } else {
                link.add(new AttributeModifier("class", ""));
            }
            target.add(link);
        }
    }

    public List<AjaxFallbackLink> getLinkList() {
        return linkList;
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }

}
