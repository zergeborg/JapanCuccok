package com.japancuccok.admin.contact;

import com.japancuccok.common.domain.contact.ContactModel;
import com.japancuccok.common.wicket.panel.main.shop.ShopFooterScriptPanel;
import com.japancuccok.common.wicket.template.AdminBasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;

import static com.japancuccok.db.DAOService.contactDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.15.
 * Time: 22:01
 */
public class Contact extends AdminBasePage {

    private static final long serialVersionUID = -6435113468770087594L;

    public Contact() {
        // Create feedback panels
        FeedbackPanel feedback = new FeedbackPanel("feedback");

        // Add feedback to the page itself
        add(feedback);

        IModel<String> model
                = new CompoundPropertyModel<String> (new ContactModel());
        TextArea<String> contactDetails
                = new TextArea<String>("contactDetails", model);
        Form contactForm = new Form("contactForm", model) {

            private static final long serialVersionUID = 7679286449172629658L;

            @Override
            protected void onSubmit() {
                com.japancuccok.common.domain.contact.Contact contact
                        = ((ContactModel)((CompoundPropertyModel) getDefaultModel())
                            .getChainedModel()).getActualContact();
                if(contact != null) {
                    contact.setContactDetails((String) getModelObject());
                    contactDao.put(contact);
                } else {
                    contact = new com.japancuccok.common.domain.contact.Contact((String) getModelObject());
                    contactDao.put(contact);
                }
            }
        };
        contactForm.add(contactDetails);
        contactForm.add(new Button("submitContact", new StringResourceModel("submitContact", this, null)));
        add(contactForm);
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Itt szerkesztheted a kapcsolattartás adatait");
    }

    @Override
    public Panel getFooterScriptPanel() {
        //We need the same length like in the shop
        return new ShopFooterScriptPanel("footerScriptPanel");
    }
}
