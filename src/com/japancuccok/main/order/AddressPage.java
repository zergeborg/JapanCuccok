package com.japancuccok.main.order;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.order.PersonalData;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductMetaData;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.main.done.Goodbye;
import com.japancuccok.common.wicket.template.ShopBasePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 14:55
 */
public class AddressPage extends ShopBasePage {
    
    private static final long serialVersionUID = 703343916418680451L;
    transient private static final Logger logger = Logger.getLogger(AddressPage.class.getName());

    private FeedbackPanel feedbackPanel;

    public AddressPage() {
    }

    public AddressPage(List<Long> productsToOrder) {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(getForm());
        add(getCartSumLabel());
    }

    private Component getForm() {
        Form addressForm = new Form<Void>("addressForm");
        BillingPanel billingPanel = getBillingDataPanel();
        PersonalDataPanel personalDataPanel = getPersonalDataPanel();
        addressForm.add(personalDataPanel);
        addressForm.add(billingPanel);
        addressForm.add(getSubmitLink(billingPanel, personalDataPanel));
        return addressForm;
    }

    private Label getCartSumLabel() {
        PropertyModel sumTotalProp = new PropertyModel(getCart(), "total");
        Label cartSumLabel = new Label("cartSum", sumTotalProp) {

            private static final long serialVersionUID = 3566509569955572435L;

            /**
             * {@inheritDoc}
             */
            @Override
            public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
            {
                replaceComponentTagBody(markupStream, openTag, getDefaultModelObjectAsString() + " HUF");
            }
        };
        cartSumLabel.setOutputMarkupId(true);
        return cartSumLabel;
    }

    private BillingPanel getBillingDataPanel() {
        return new BillingPanel("billingDataPanel");
    }

    private PersonalDataPanel getPersonalDataPanel() {
        return new PersonalDataPanel("personalDataPanel");
    }

    private Component getSubmitLink(final BillingPanel billingPanel,
                                    final PersonalDataPanel personalDataPanel) {
        SubmitLink submitLink = new SubmitLink("sendMail"){
            private static final long serialVersionUID = 3309754304400009964L;

            @Override
            public void onSubmit() {
                // because we overrode the form processing we need to handle validation/processing on the components ourselves
                personalDataPanel.validate();
                billingPanel.validate();
                if(!billingPanel.isValid()
                        || !personalDataPanel.isValid()){
                    // didn't validate so we stop processing (validation errors will be displayed provided we are using a FeedbackPanel or similar)
                    return;
                }
                personalDataPanel.update();
                billingPanel.update();
                sendConfirmationMail(personalDataPanel.getPersonalData());
                sendNotificationMail(personalDataPanel.getPersonalData());
                getSession().dirty();
                getSession().invalidate();
                getSession().detach();
                setResponsePage(Goodbye.class);
                getSession().clear();
            }
        };
        submitLink.setDefaultFormProcessing(false);
        return submitLink;
    }

    private void sendConfirmationMail(PersonalData personalData) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        StringBuilder stringBuilder = new StringBuilder();
        String msgForeword = getString("msgForeword");
        stringBuilder.append(msgForeword);
        String cartContent = getCartContent();
        stringBuilder.append(cartContent);
        String ending = getString("msgEnding");
        stringBuilder.append(ending);
        String contact = getString("msgContact");
        stringBuilder.append(contact);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("deliverydaemon@japancuccok.com",
                    "japancuccok.com - Automatic Mail Delivery"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(personalData.getEmailAddress(), personalData.getName()));
            msg.setSubject(getString("msgSubject"), "UTF-8");
            msg.setText(stringBuilder.toString(), "UTF-8");
            Transport.send(msg);

        } catch (AddressException e) {
            error(e.getLocalizedMessage());
        } catch (SendFailedException e) {
            error(e.getLocalizedMessage());
        } catch (MessagingException e) {
            error(e.getLocalizedMessage());
        } catch (UnsupportedEncodingException e) {
            error(e.getLocalizedMessage());
        }
        logger.info("Message sent to recipient!");
    }

    private void sendNotificationMail(PersonalData personalData) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        StringBuilder stringBuilder = new StringBuilder();
        String foreWord = getString("msgNotification");
        stringBuilder.append(foreWord);
        String personalDetails = getPersonalDetails(personalData);
        stringBuilder.append(personalDetails);
        String cartContentForeword = getString("msgCartContent");
        stringBuilder.append(cartContentForeword);
        String cartContent = getCartContent();
        stringBuilder.append(cartContent);

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("deliverydaemon@japancuccok.com",
                    "japancuccok.com - Automatic Mail Delivery"));
            msg.addRecipients(Message.RecipientType.TO,
                    new Address[] {
                            new InternetAddress("fogetti@gmail.com"),
                            new InternetAddress("info@japancuccok.com")});
            msg.setSubject(getString("msgSubject"), "UTF-8");
            msg.setText(stringBuilder.toString(), "UTF-8");
            Transport.send(msg);

        } catch (AddressException e) {
            error(e.getLocalizedMessage());
        } catch (SendFailedException e) {
            error(e.getLocalizedMessage());
        } catch (MessagingException e) {
            error(e.getLocalizedMessage());
        } catch (UnsupportedEncodingException e) {
            error(e.getLocalizedMessage());
        }
        logger.info("Message sent to admin!");
    }

    private String getPersonalDetails(PersonalData personalData) {
        StringBuilder stringBuilder = new StringBuilder();
        String name = personalData.getName();
        stringBuilder.append(name+"\n");
        String emailAddress = personalData.getEmailAddress();
        stringBuilder.append(emailAddress+"\n");
        String phoneNumber = personalData.getPhoneNumber();
        stringBuilder.append(phoneNumber+"\n");
        String settlement = personalData.getSettlement();
        stringBuilder.append(settlement+"\n");
        String street = personalData.getStreet();
        stringBuilder.append(street+"\n");
        String comment = personalData.getComment();
        stringBuilder.append((comment == null ? "" : comment)+"\n\n");
        return stringBuilder.toString();
    }

    private String getCartContent() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        for(Product product : getCart().getProducts()) {
            ProductMetaData productMetaData = getCart().get(product.getId());
            String nextLine = "\n" + i + ". " + product.getName() + ",\t"
                    + productMetaData.getChosenSize() + " méretben,\t"
                    + productMetaData.getChosenAmount() + " db,\t"
                    + product.getPrice() + " HUF\n";
            stringBuilder.append(nextLine);
            String sumTotal = "  Összesen: " + product.getPrice()*productMetaData.getChosenAmount() + " HUF\n\n";
            stringBuilder.append(sumTotal);
            i++;
        }
        return stringBuilder.toString();
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", new Model<Serializable>("Kérjük add meg az adataidat!"));
    }
}
