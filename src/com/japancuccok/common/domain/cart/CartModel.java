package com.japancuccok.common.domain.cart;

import com.japancuccok.common.wicket.session.JapanCuccokSession;
import org.apache.wicket.Session;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.09.16.
 * Time: 22:05
 */
public class CartModel extends LoadableDetachableModel<Cart> {

    private static final long serialVersionUID = 2430438034556128578L;

    @Override
    protected Cart load() {
        return getSession().getCart();
    }

    public JapanCuccokSession getSession() {
        return (JapanCuccokSession) Session.get();
    }
}
