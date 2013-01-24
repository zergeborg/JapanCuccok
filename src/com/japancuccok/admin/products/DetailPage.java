package com.japancuccok.admin.products;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.pattern.BinaryImageLoadStrategy;
import com.japancuccok.common.wicket.panel.admin.products.InfoPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopFooterScriptPanel;
import com.japancuccok.common.wicket.template.AdminBasePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.03.
 * Time: 20:05
 */
public class DetailPage extends AdminBasePage {

    private static final long serialVersionUID = 8217908652605382099L;
    private Product productToEdit;
    private FeedbackPanel feedbackPanel;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        productToEdit = getProduct();

        feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);

        Form<?>            editProductForm = new Form<Object>("editProductForm");
        WebMarkupContainer imagePanel      = getImagePanel();
        Panel              infoPanel       = getInfoPanel();
        editProductForm.add(imagePanel);
        editProductForm.add(infoPanel);
        add(editProductForm);
    }

    private Product getProduct() {
        PageParameters pageParameters = getPageParameters();
        Long productId = pageParameters.get("id").toLongObject();
        Key<Product> productKey = Key.create(Product.class, productId);
        return productDao.get(productKey).get(productKey);
    }

    private WebMarkupContainer getImagePanel() {

        RepeatingView imgRepeatingView =
                new DataStoreImagePanelRepeater("imgRepeatingView", new BinaryImageLoadStrategy(productToEdit));
        imgRepeatingView.setRenderBodyOnly(true);
        final WebMarkupContainer dummyRepeaterContainer = new WebMarkupContainer("dummyRepeaterContainer");
        dummyRepeaterContainer.add(imgRepeatingView);
        dummyRepeaterContainer.setVisible(true);
        dummyRepeaterContainer.setOutputMarkupId(true);

        return dummyRepeaterContainer;
    }

    private Panel getInfoPanel() {
        Panel infoPanel = new InfoPanel("infoPanel", productToEdit, feedbackPanel);
        return infoPanel;
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Itt szerkesztheted a termékeket");
    }

    @Override
    public Panel getFooterScriptPanel() {
        //We need the same length like in the shop
        return new ShopFooterScriptPanel("footerScriptPanel");
    }
}
