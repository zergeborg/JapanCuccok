package com.japancuccok.common.wicket.panel.admin.base;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.japancuccok.admin.contact.Contact;
import com.japancuccok.admin.dashboard.Dashboard;
import com.japancuccok.common.wicket.template.AdminBasePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.15.
 * Time: 19:01
 */
public class AdminMenuPanel extends Panel {

    private static final long serialVersionUID = -2315120101155217893L;
    
    private class UrlModel extends LoadableDetachableModel<String> {

        private static final long serialVersionUID = -1753377594003026749L;

        @Override
        protected String load() {
            UserService userService = UserServiceFactory.getUserService();
            return userService.createLogoutURL(getRequestUrl());
        }
    }

    public AdminMenuPanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new BookmarkablePageLink("dashboard", Dashboard.class).setBody(new
                StringResourceModel("dashboard", findParent(AdminBasePage.class), null)));
        add(new BookmarkablePageLink("contact", Contact.class).setBody(new
                StringResourceModel("contact", findParent(AdminBasePage.class), null)));
        add(new ExternalLink("logOut", new UrlModel(), new StringResourceModel("logOut",
                findParent(AdminBasePage.class), null)));
    }

    private String getRequestUrl(){
        // this is a wicket-specific request interface
        final Request request = getRequest();
        if(request instanceof WebRequest){
            final WebRequest wr = (WebRequest) request;
            // but this is the real thing
            final Object containerRequest = wr.getContainerRequest();
            if(containerRequest instanceof HttpServletRequest){
                final HttpServletRequest hsr = (HttpServletRequest)containerRequest;
                String reqUrl = hsr.getRequestURL().toString();
                final String queryString = hsr.getQueryString();
                if(queryString != null){
                    reqUrl += "?" + queryString;
                }
                return reqUrl;
            }
            return null;
        }
        return null;

    }

}
