package com.japancuccok.common.wicket.session;

import org.apache.wicket.protocol.http.WebSession;
import com.japancuccok.common.domain.cart.Cart;
import org.apache.wicket.request.Request;

import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 20:57
 */
public class JapanCuccokSession extends WebSession {

    private static final long serialVersionUID = 979234607554027989L;

    transient private static final Logger logger = Logger.getLogger(JapanCuccokSession.class.getName());

    static {
        logger.info("Starting static initialization of JapanCuccokSession...");
    }

    private final Cart cart;
    private final boolean initialized;

    /**
     * Constructor. Note that {@link org.apache.wicket.request.cycle.RequestCycle} is not
     * available until this constructor returns.
     *
     * @param request The current request
     */
    public JapanCuccokSession(Request request) {
        super(request);
        logger.info("Starting JapanCuccokSession construction...");
        this.initialized = true;
        this.cart = new Cart();
        synchronized (this) {
            cartDao.put(cart);
        }
    }

    public synchronized Cart getCart() {
        return cart;
    }

    @Override
    public final void detach() {
        logger.info("Detaching the following session: ["+getId()+"]");
        // A dirty hack: Session.detach() is called even before the constructor was called
        if(initialized) {
            // Save the cart into DB
            synchronized (this) {
                cart.detach();
            }
        }
        super.detach();
    }

    public final void invalidateNow() {
        super.invalidateNow();
        synchronized (this) {
            cartDao.delete(cart);
        }
    }
}
