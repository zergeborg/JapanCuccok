package com.japancuccok.common.wicket.panel.main.base;

import com.japancuccok.main.contact.Contact;
import com.japancuccok.main.stuff.Stuff;
import com.japancuccok.main.accessories.Accessories;
import com.japancuccok.main.tshirt.Tshirt;
import com.japancuccok.common.wicket.template.BasePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.26.
 * Time: 16:09
 */
public abstract class BaseMenuPanel extends Panel {

    private static final long serialVersionUID = -3133622189745558285L;

    public BaseMenuPanel(String id) {
        super(id);
    }
    
    public abstract Link getCartLink();

    @Override
    protected void onInitialize() {
        super.onInitialize();
        initializeLinks();
    }

    protected void initializeLinks() {
        add(new BookmarkablePageLink("tshirt", Tshirt.class).setBody(new StringResourceModel("tshirt", findParent(BasePage.class), null)));
        add(new BookmarkablePageLink("accessories", Accessories.class).setBody(new StringResourceModel("accessories", findParent(BasePage.class), null)));
        add(new BookmarkablePageLink("stuff", Stuff.class).setBody(new StringResourceModel("stuff", findParent(BasePage.class), null)));
        add(new BookmarkablePageLink("contact", Contact.class).setBody(new StringResourceModel("contact", findParent(BasePage.class), null)));
        add(getCartLink());
    }
}
