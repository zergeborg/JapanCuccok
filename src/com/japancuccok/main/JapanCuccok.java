package com.japancuccok.main;

import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.infrastructure.gae.GaeCompressRequestCycleListener;
import com.japancuccok.common.infrastructure.gae.GaeMaintenanceAwareSessionStore;
import com.japancuccok.common.infrastructure.gae.GaeWebSessionStoreProvider;
import com.japancuccok.common.pattern.ProductLoadStrategy;
import com.japancuccok.common.wicket.component.DummyPackageResourceGuard;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.main.accessories.Accessories;
import com.japancuccok.main.cart.CartListPage;
import com.japancuccok.main.contact.Contact;
import com.japancuccok.main.dashboard.Dashboard;
import com.japancuccok.main.done.Goodbye;
import com.japancuccok.main.order.AddressPage;
import com.japancuccok.main.shop.Shop;
import com.japancuccok.main.stuff.Stuff;
import com.japancuccok.main.tshirt.Tshirt;
import org.apache.wicket.Application;
import org.apache.wicket.DefaultPageManagerProvider;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.pageStore.IDataStore;
import org.apache.wicket.pageStore.memory.HttpSessionDataStore;
import org.apache.wicket.pageStore.memory.PageNumberEvictionStrategy;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.session.HttpSessionStore;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.util.time.Duration;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.04.11.
 * Time: 21:46
 * To change this template use File | Settings | File Templates.
 */
public class JapanCuccok extends WebApplication {

    private final ISessionStore sessionStore = new GaeMaintenanceAwareSessionStore(new HttpSessionStore());

    transient private static final Logger logger = Logger.getLogger(JapanCuccok.class.getName());
    private final boolean COMPRESS_WHITESPACE = true;
    private final boolean STORE_ASYNCHRONOUS = true;
    private final int PAGE_INSTANCE_NUMBER = 7;

    @Override
    public Class<? extends Page> getHomePage() {
        return Dashboard.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        logger.info("Creating a new session for the following request: ["+request.getUrl()+"]");
        return new JapanCuccokSession(request);
    }

    @Override
    public void sessionUnbound(final String sessionId) {
        IRequestLogger logger = getRequestLogger();
        if (logger != null)
        {
            for(IRequestLogger.SessionData sessionData : logger.getLiveSessions()) {
                if(sessionData.getSessionId().equals(sessionId)) {
                    ((JapanCuccokSession)WebSession.get()).invalidateNow();
                }
            }
        }
        super.sessionUnbound(sessionId);
    }

    @Override
    public void init() {
        initSettings();
        mountResources();
        mountPages();
        initSessionInfrastructure();
        initResourceGuard();
    }

    private void mountResources() {
//        mountResource("/images/${name}", new ImageResourceReference());
    }

    public static JapanCuccok get() {
        return (JapanCuccok) Application.get();
    }

    public List<Product> getProducts() {
        return new ProductLoadStrategy().load();
    }

    private void initResourceGuard() {
        getResourceSettings().setPackageResourceGuard(new DummyPackageResourceGuard());
    }

    private void initSessionInfrastructure() {
        setPageManagerProvider(new DefaultPageManagerProvider(this) {
            protected IDataStore newDataStore() {
                return new HttpSessionDataStore(getPageManagerContext(),
                        new PageNumberEvictionStrategy(20));
            }
        });
        setSessionStoreProvider(new GaeWebSessionStoreProvider(sessionStore));
        getRequestCycleListeners().add(new GaeCompressRequestCycleListener(sessionStore));
    }

    private void initSettings() {
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy
                                                            .REDIRECT_TO_RENDER);
        getMarkupSettings().setCompressWhitespace(COMPRESS_WHITESPACE);
        getStoreSettings().setAsynchronous(STORE_ASYNCHRONOUS);
        getStoreSettings().setInmemoryCacheSize(PAGE_INSTANCE_NUMBER);
        getResourceSettings().setDefaultCacheDuration(Duration.ONE_WEEK);
    }

    private void mountPages() {
        mountPage("/", Dashboard.class);
        mountPage("/cartlist", CartListPage.class);
        mountPage("/shop", Shop.class);
        mountPage("/tshirt", Tshirt.class);
        mountPage("/accessories", Accessories.class);
        mountPage("/stuff", Stuff.class);
        mountPage("/personaldata", AddressPage.class);
        mountPage("/goodbye", Goodbye.class);
        mountPage("/contact", Contact.class);
    }

}
