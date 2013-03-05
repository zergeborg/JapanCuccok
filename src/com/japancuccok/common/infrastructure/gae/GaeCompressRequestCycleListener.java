package com.japancuccok.common.infrastructure.gae;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductMetaData;
import com.japancuccok.common.infrastructure.tools.CompressUtil;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import org.apache.wicket.Session;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.session.ISessionStore;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.10.
 * Time: 23:27
 */
public class GaeCompressRequestCycleListener extends AbstractRequestCycleListener {

    private static final long serialVersionUID = 2476101962605216223L;

    transient private static final Logger logger = Logger.getLogger(GaeCompressRequestCycleListener.class.getName());

    /**
     * Should make sure to call it in a thread safe manner
     */
    private final ISessionStore delegate;

    private final Object lockObject = new Object();

    public GaeCompressRequestCycleListener(ISessionStore delegate) {
        this.delegate = delegate;
    }

    @Override
    public void onBeginRequest(RequestCycle cycle) {
        JapanCuccokSession japanCuccokSession = (JapanCuccokSession)Session.get();
        Cart cart = japanCuccokSession.getCart();
        if(cart.getId() != null) {
            loadProducts(cart);
        }
    }

    private void loadProducts(Cart cart) {
        for(Long productId : cart.getProductIds()) {
            Product product = productDao.find(Key.create(Product.class, productId));
            product = productDao.load(product,
            		(Class<Product>[])new Class<?>[]{
                            Product.WithBinaryImage.class,
                            Product.WithUrlImage.class,
                            BinaryImage.WithBinaryImageData.class,
                            UrlImage.WithUrlImageData.class});
            cart.get(product.getId()).setProduct(product);
        }
    }

    @Override
    public void onEndRequest(RequestCycle cycle) {
        super.onEndRequest(cycle);

        if (cycle != null)
        {
            List<String> attributeList;
            synchronized (lockObject) {
                attributeList = delegate.getAttributeNames(cycle.getRequest());
            }
            for (String name : attributeList) {
                Serializable value;
                synchronized (lockObject) {
                    value = delegate.getAttribute(cycle.getRequest(), name);
                }

                if (!(value instanceof Session)) {
                    if (!(value instanceof byte[])) {

                        byte[] bytes = CompressUtil.compress(value);
                        synchronized (lockObject) {
                            delegate.setAttribute(cycle.getRequest(),name,bytes);
                        }
                    } else {
                        byte[] bytes = (byte[]) value;
                        synchronized (lockObject) {
                            delegate.setAttribute(cycle.getRequest(),name,bytes);
                        }
                    }
                } else {
                    Session session = (Session) value;
                    long sessionSize = session.getSizeInBytes();
                    logger.info("The actual size of the session ["+session.getId()+"] at the end of the " +
                            "request is: ["
                            + sessionSize + " bytes] [" + Math.round(sessionSize/1024) + " KB]");
                }
            }
        }
    }

}
