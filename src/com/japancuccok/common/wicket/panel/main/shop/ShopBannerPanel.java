package com.japancuccok.common.wicket.panel.main.shop;

import com.japancuccok.common.wicket.component.StatelessExternalLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.14.
 * Time: 21:49
 */
public class ShopBannerPanel extends Panel {

    private static final long serialVersionUID = -5021621350445743306L;

    public ShopBannerPanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new StatelessExternalLink("facebook", "https://facebook.com/"));
        add(new StatelessExternalLink("twitter", "https://twitter.com/"));
        add(new StatelessExternalLink("tumblr", "https://www.tumblr.com/"));
        add(new StatelessExternalLink("deviantart", "http://www.deviantart.com/"));
        add(new StatelessExternalLink("flickr", "http://www.flickr.com/"));
    }

    @Override
    protected boolean getStatelessHint()
    {
        return true;
    }

}
