package com.japancuccok.main.done;

import com.japancuccok.common.wicket.component.DummyStatelessLink;
import com.japancuccok.common.wicket.panel.main.base.BaseMenuPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopMenuPanel;
import com.japancuccok.common.wicket.template.BasePage;
import com.japancuccok.common.wicket.template.ShopBasePage;
import com.japancuccok.main.accessories.Accessories;
import com.japancuccok.main.contact.Contact;
import com.japancuccok.main.stuff.Stuff;
import com.japancuccok.main.tshirt.Tshirt;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.14.
 * Time: 23:38
 */
public class Goodbye extends ShopBasePage {

    private static final long serialVersionUID = -509125521223907161L;

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Köszönjük megrendelésed!") {

            private static final long serialVersionUID = 7240259934437999328L;

            @Override
            protected boolean getStatelessHint()
            {
                return true;
            }

        };
    }

    @Override
    public Panel getMenuPanel() {
        return new ShopMenuPanel("menuPanel"){

            private static final long serialVersionUID = -153635075724852575L;

            @Override
            protected void initializeLinks() {
                add(new DummyStatelessLink("tshirt", Tshirt.class).setBody(new StringResourceModel("tshirt", findParent(BasePage.class), null)));
                add(new DummyStatelessLink("accessories", Accessories.class).setBody(new StringResourceModel("accessories", findParent(BasePage.class), null)));
                add(new DummyStatelessLink("stuff", Stuff.class).setBody(new StringResourceModel("stuff", findParent(BasePage.class), null)));
                add(new DummyStatelessLink("contact", Contact.class).setBody(new StringResourceModel("contact", findParent(BasePage.class), null)));
                add(getCartLink());
            }

            @Override
            public Link getCartLink() {
                return new DummyStatelessLink("cart", Class.class);
            }
            
            @Override
            public Label getCartLabel() {
                return new Label("sumTotal") {

                    private static final long serialVersionUID = 3874378973117236421L;

                    @Override
                    protected boolean getStatelessHint()
                    {
                        return true;
                    }

                };
            }
        };
    }

}
