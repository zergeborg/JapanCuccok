package com.japancuccok.admin.products;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.wicket.panel.admin.products.DatastoreImagePanel;
import com.japancuccok.common.wicket.panel.admin.products.InfoPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopFooterScriptPanel;
import com.japancuccok.common.wicket.template.AdminBasePage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.List;

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
    private List<? extends IImage> imageListToEdit;
    private FeedbackPanel feedbackPanel;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        productToEdit = getProduct();
        imageListToEdit = getImageList();

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

    private List<? extends IImage> getImageList() {
        productToEdit = productDao.load(productToEdit,
                new Class<?>[]{Product.WithBinaryImage.class, Product.WithUrlImage.class});
        List<IImage> images = new ArrayList<IImage>();
        if(productToEdit.getBinaryImageList() != null) {
            images.addAll(productToEdit.getBinaryImageList());
        }
        if(productToEdit.getUrlImageList() != null) {
            images.addAll(productToEdit.getUrlImageList());
        }
        return images;
    }

    private WebMarkupContainer getImagePanel() {

        RepeatingView imgRepeatingView = new RepeatingView("imgRepeatingView");
        int i = 0;
        for(IImage image : imageListToEdit) {
            Panel imgPanel = null;
            if(image instanceof BinaryImage) {
                imgPanel = new DatastoreImagePanel("imagePanel"+i, (BinaryImage) image);
                imgRepeatingView.add(imgPanel);
                i++;
            }
        }
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
