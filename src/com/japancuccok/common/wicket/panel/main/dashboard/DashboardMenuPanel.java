package com.japancuccok.common.wicket.panel.main.dashboard;

import com.japancuccok.main.shop.Shop;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.26.
 * Time: 14:11
 */
public class DashboardMenuPanel extends Panel {

    private static final long serialVersionUID = 9131121706394102129L;

    public DashboardMenuPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new BookmarkablePageLink("shop", Shop.class).setBody(new StringResourceModel("shop", getPage(), null)));
        add(new BookmarkablePageLink("about", Shop.class).setBody(new StringResourceModel("about", getPage(), null)));
        add(new BookmarkablePageLink("contact", Shop.class).setBody(new StringResourceModel("contact", getPage(), null)));
        add(new BookmarkablePageLink("event", Shop.class).setBody(new StringResourceModel("event", getPage(), null)));
        add(new BookmarkablePageLink("links", Shop.class).setBody(new StringResourceModel("links", getPage(), null)));
        add(new BookmarkablePageLink("product", Shop.class).setBody(new StringResourceModel("product", getPage(), null)));
    }
}
