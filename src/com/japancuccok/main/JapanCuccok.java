package com.japancuccok.main;

import com.japancuccok.common.domain.image.ImageResourceReference;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.infrastructure.gae.GaeCompressRequestCycleListener;
import com.japancuccok.common.infrastructure.gae.GaeMaintenanceAwareSessionStore;
import com.japancuccok.common.infrastructure.gae.GaeWebSessionStoreProvider;
import com.japancuccok.common.pattern.ProductLoadStrategy;
import com.japancuccok.common.wicket.component.DummyPackageResourceGuard;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.common.wicket.session.JsessionIDMovingMapper;
import com.japancuccok.main.accessories.Accessories;
import com.japancuccok.main.cart.CartListPage;
import com.japancuccok.main.contact.Contact;
import com.japancuccok.main.dashboard.Dashboard;
import com.japancuccok.main.detail.ProductDetail;
import com.japancuccok.main.done.Goodbye;
import com.japancuccok.main.order.AddressPage;
import com.japancuccok.main.shop.Shop;
import com.japancuccok.main.sitemap.JapanCuccokSiteMap;
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
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.session.HttpSessionStore;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.util.time.Duration;

import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private final String CHARACTER_ENCODING_UTF8 = "UTF-8";
    private final boolean COMPRESS_WHITESPACE = true;
    private final boolean STORE_ASYNCHRONOUS = true;
    private final int PAGE_INSTANCE_NUMBER = 7;
    private static final String[] botAgents = {
                  "googlebot", "msnbot", "slurp", "jeeves",
                  "appie", "architext", "jeeves", "bjaaland", "ferret", "gulliver",
                  "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret",
                  "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina", "slurp",
                  "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver",
                  "jennybot", "mercator", "netcraft", "msnbot", "petersnews",
                  "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch",
                  "zyborg", "wisenutbot", "robot", "crawl", "spider"
                  };
    private static final Map<String, Class> mountedPages = new Hashtable<String, Class>();

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

    public static boolean isAgent(final String agent) {
        if (agent != null) {
            final String lowerAgent = agent.toLowerCase();
            for (final String bot : botAgents) {
                if (lowerAgent.indexOf(bot) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    public static JapanCuccok get() {
        return (JapanCuccok) Application.get();
    }

    public List<Product> getProducts() {
        return new ProductLoadStrategy().load();
    }

    public Iterator<Map.Entry<String, Class>> getMountedPages() {
        return mountedPages.entrySet().iterator();
    }

    protected WebResponse newWebResponse(final WebRequest webRequest, final HttpServletResponse httpServletResponse){
        return new ServletWebResponse((ServletWebRequest)webRequest, httpServletResponse) {

            @Override
            public String encodeURL(CharSequence url) {
                return isRobot(webRequest) ? url.toString() : super.encodeURL(url);
            }

            @Override
            public String encodeRedirectURL(CharSequence url) {
                return isRobot(webRequest) ? url.toString() : super.encodeRedirectURL(url);
            }

            private boolean isRobot(WebRequest request) {
                final String agent = webRequest.getHeader("User-Agent");
                return isAgent(agent);
            }
        };
    }

    private void mountResources() {
        mountResource("/images/${imageFileNameWithExtension}", new ImageResourceReference());
    }

    private void initResourceGuard() {
        getResourceSettings().setPackageResourceGuard(new DummyPackageResourceGuard());
    }

    private void initSessionInfrastructure() {
        setRootRequestMapper(new JsessionIDMovingMapper(getRootRequestMapper()));
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
        getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.RenderStrategy.REDIRECT_TO_RENDER);
        getRequestCycleSettings().setResponseRequestEncoding(CHARACTER_ENCODING_UTF8);
        getMarkupSettings().setDefaultMarkupEncoding(CHARACTER_ENCODING_UTF8);
        getMarkupSettings().setCompressWhitespace(COMPRESS_WHITESPACE);
        getStoreSettings().setAsynchronous(STORE_ASYNCHRONOUS);
        getStoreSettings().setInmemoryCacheSize(PAGE_INSTANCE_NUMBER);
        getResourceSettings().setDefaultCacheDuration(Duration.ONE_WEEK);
    }

    private void mountPages() {
        mountPage("/dashboard", Dashboard.class);
        mountedPages.put("/dashboard", Dashboard.class);
        mountPage("/cartlist", CartListPage.class);
        mountedPages.put("/cartlist", CartListPage.class);
        mountPage("/shop", Shop.class);
        mountedPages.put("/shop", Shop.class);
        mountPage("/tshirt", Tshirt.class);
        mountedPages.put("/tshirt", Tshirt.class);
        mountPage("/accessories", Accessories.class);
        mountedPages.put("/accessories", Accessories.class);
        mountPage("/stuff", Stuff.class);
        mountedPages.put("/stuff", Stuff.class);
        mountPage("/personaldata", AddressPage.class);
        mountedPages.put("/personaldata", AddressPage.class);
        mountPage("/goodbye", Goodbye.class);
        mountedPages.put("/goodbye", Goodbye.class);
        mountPage("/contact", Contact.class);
        mountedPages.put("/contact", Contact.class);
        mountPage("/productdetail", ProductDetail.class);
        mountedPages.put("/productdetail", ProductDetail.class);
        mountPage("/sitemap.xml", JapanCuccokSiteMap.class);
    }

}
