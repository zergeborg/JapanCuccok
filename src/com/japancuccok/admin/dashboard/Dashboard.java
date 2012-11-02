package com.japancuccok.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ProductUploadModel;
import com.japancuccok.common.pattern.ProductLoadStrategy;
import com.japancuccok.common.wicket.panel.main.shop.ShopFooterScriptPanel;
import com.japancuccok.common.wicket.template.AdminBasePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.11.
 * Time: 22:32
 */
public class Dashboard extends AdminBasePage {

    private static final long serialVersionUID = 5511625049455834004L;

    /** Reference to listview for easy access. */
    private final UploadedProductListView uploadedProductListView;

    public Dashboard() {
        // Create feedback panels
        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");

        // Add uploadFeedback to the page itself
        add(uploadFeedback);

        add(new Label("newProductTitle", new StringResourceModel("newProductTitle", this, null)));
        CompoundPropertyModel<ProductUploadModel> model = new CompoundPropertyModel<ProductUploadModel>(new ProductUploadModel());
        final Form<?> newProductForm = new ProductUploadForm("newProductForm", model);
        add(newProductForm);

        Form<?> uploadedProductForm = new Form<Object>("uploadedProductForm");
        WebMarkupContainer uploadedProductContainer = new WebMarkupContainer("uploadedProductContainer");
        uploadedProductListView = new UploadedProductListView("productList", new ProductLoadStrategy());
        uploadedProductContainer.add(uploadedProductListView);
        uploadedProductForm.add(uploadedProductContainer);
        add(uploadedProductForm);

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
