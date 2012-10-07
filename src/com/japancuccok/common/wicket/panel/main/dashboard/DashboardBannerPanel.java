package com.japancuccok.common.wicket.panel.main.dashboard;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.14.
 * Time: 23:02
 */
public class DashboardBannerPanel extends Panel {

    private static final long serialVersionUID = -944049712037270474L;

    public DashboardBannerPanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new ExternalLink("facebook", "https://facebook.com/"));
        add(new ExternalLink("twitter", "https://twitter.com/"));
        add(new ExternalLink("tumblr", "https://www.tumblr.com/"));
        add(new ExternalLink("deviantart", "http://www.deviantart.com/"));
        add(new ExternalLink("flickr", "http://www.flickr.com/"));
    }

}
