package com.japancuccok.admin;

import com.japancuccok.admin.dashboard.Dashboard;
import com.japancuccok.common.infrastructure.gae.GaeSafeServletWebRequest;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.lang.Bytes;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.11.
 * Time: 22:12
 */
public class JapanCuccokAdmin extends WebApplication {

    transient private static final Logger log = Logger.getLogger(JapanCuccokAdmin.class.getName());

    /**
     * Constructor.
     */
    public JapanCuccokAdmin()
    {
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return Dashboard.class;
    }

    @Override
    protected void init()
    {
        super.init();

        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
        getApplicationSettings().setDefaultMaximumUploadSize(Bytes.bytes(1024*1024*10-1024));
        mountPage("/", Dashboard.class);
    }

    @Override
    protected WebRequest newWebRequest(final HttpServletRequest servletRequest, 
                                       final String filterPath) {
        return new GaeSafeServletWebRequest(servletRequest, filterPath);
    }

}
