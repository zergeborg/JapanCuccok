package com.japancuccok.main.cart;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.cart.CartModel;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductModel;
import com.japancuccok.common.events.CartItemDelete;
import com.japancuccok.common.events.CartUpdate;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.main.JapanCuccok;
import com.japancuccok.main.order.AddressPage;
import com.japancuccok.common.wicket.component.CartLabel;
import com.japancuccok.common.wicket.panel.main.cart.CartListFooterScriptPanel;
import com.japancuccok.common.wicket.template.ShopBasePage;
import org.apache.wicket.Component;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.13.
 * Time: 22:02
 */
public class CartListPage extends ShopBasePage {

    private static final long serialVersionUID = 6271020764787786212L;

    private int sumTotal;
    private FeedbackPanel feedbackPanel;

    public CartListPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        sumTotal = getCart().getTotal();
        CartLabel cartSumLabel = new CartLabel("cartSum", new PropertyModel(new CartModel(), "total"));
        cartSumLabel.setOutputMarkupId(true);
        add(cartSumLabel);
        WebMarkupContainer orderPanel = getOrderPanel();
        add(orderPanel);
        add(getSubmitLink());
    }

    private WebMarkupContainer getOrderPanel() {
        RepeatingView orderItemRepeatingView = new RepeatingView("orderItemRepeatingView") {

            private static final long serialVersionUID = 4902769900581056402L;

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);

                // check if this is a item delete update event and if so repaint self
                if (event.getPayload() instanceof CartItemDelete)
                {
                    CartItemDelete update = (CartItemDelete)event.getPayload();
                    remove(update.getProductCheckoutPanel());
                    update.getTarget().add(this.getParent());
                }
            }
        };
        orderItemRepeatingView.setOutputMarkupId(true);
        int i = 0;
        for(Product product : getCart().getProducts()) {
            if(product != null) {
                Panel productCheckoutPanel = new ProductCheckoutPanel("productCheckoutPanel",
                        new ProductModel(product.getId(), true), i);
                orderItemRepeatingView.add(productCheckoutPanel);
                i++;
            }
        }
        final WebMarkupContainer orderItemRepeatContainer = new WebMarkupContainer("orderItemRepeatContainer");
        orderItemRepeatContainer.add(orderItemRepeatingView);
        orderItemRepeatContainer.setVisible(true);
        orderItemRepeatContainer.setOutputMarkupId(true);

        return orderItemRepeatContainer;
    }

    private Component getSubmitLink() {
        Link submitLink = new Link("sendMail") {
            
            private static final long serialVersionUID = 5072965733664407209L;
            private List<Long> productIdsToOrder = new ArrayList<Long>();

            @Override
            protected void onInitialize() {
                super.onInitialize();

                productIdsToOrder.addAll(getCart().getProductIds());
            }

            @Override
            public void onClick() {
                if(productIdsToOrder.size() > 0) {
                    setResponsePage(new AddressPage(productIdsToOrder));
                } else {
                    warn(getString("noProductInTheCart"));
                }
            }

            @Override
            public void onEvent(IEvent<?> event) {
                super.onEvent(event);

                if (event.getPayload() instanceof CartUpdate)
                {
                    CartUpdate update = (CartUpdate)event.getPayload();
                    if(update.getProductId() != null) {
                        update.getTarget().add(this);
                        productIdsToOrder.add(update.getProductId());
                    }
                }
                //
                // check if this is a item delete update event and if so repaint self
                if (event.getPayload() instanceof CartItemDelete)
                {
                    CartItemDelete update = (CartItemDelete)event.getPayload();
                    update.getTarget().add(this);
                    productIdsToOrder.remove(update.getProductCheckoutPanel().getProductToOrder().getId());
                }
            }

        };
        submitLink.setOutputMarkupId(true);
        return submitLink;
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }

    public List<Product> getProducts() {
        return JapanCuccok.get().getProducts();
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Kérjük nézd át a megrendelt darabokat!");
    }

    @Override
    public Panel getFooterScriptPanel() {
        return new CartListFooterScriptPanel("footerScriptPanel");
    }

}
