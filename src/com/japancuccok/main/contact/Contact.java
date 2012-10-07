package com.japancuccok.main.contact;

import com.japancuccok.common.domain.contact.ContactModel;
import com.japancuccok.common.wicket.template.ShopBasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.15.
 * Time: 23:57
 */
public class Contact extends ShopBasePage {

    private static final long serialVersionUID = -3433826103021350268L;

    public Contact() {
        super();
        IModel<String> model
                = new CompoundPropertyModel<String>(new ContactModel());
        add(new MultiLineLabel("contactDetails", model));
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Elérhetőségeink a következők");
    }
}
