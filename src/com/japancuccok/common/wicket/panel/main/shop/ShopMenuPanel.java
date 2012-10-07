package com.japancuccok.common.wicket.panel.main.shop;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.cart.CartModel;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.main.cart.CartListPage;
import com.japancuccok.common.wicket.component.CartLabel;
import com.japancuccok.common.wicket.panel.main.base.BaseMenuPanel;
import com.japancuccok.common.wicket.template.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.18.
 * Time: 23:21
 */
public class ShopMenuPanel extends BaseMenuPanel {

    private static final long serialVersionUID = -8767205758780234071L;

    public ShopMenuPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(getCartLabel());
    }

    @Override
    public Link getCartLink() {
        return (Link) new BookmarkablePageLink("cart", CartListPage.class).setBody(new StringResourceModel("cart", findParent(BasePage.class), null));
    }

    public Label getCartLabel() {
        Label cartSumLabel = new CartLabel("sumTotal", new PropertyModel(new CartModel(), "total"));
        return cartSumLabel;
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }


}
