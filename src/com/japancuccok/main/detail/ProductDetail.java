package com.japancuccok.main.detail;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductMetaData;
import com.japancuccok.common.domain.product.ProductModel;
import com.japancuccok.common.events.CartUpdate;
import com.japancuccok.common.events.SizeChoose;
import com.japancuccok.common.pattern.ProductDetailImageLoadStrategy;
import com.japancuccok.common.wicket.session.JapanCuccokSession;
import com.japancuccok.common.wicket.template.ShopBasePage;
import com.japancuccok.main.JapanCuccok;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;
import java.util.logging.Logger;

import static com.japancuccok.common.infrastructure.tools.ImageResourceUtil.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.08.
 * Time: 16:36
 */
public class ProductDetail extends ShopBasePage {

    private static final long serialVersionUID = 7400716818559396662L;

    transient private static final Logger logger = Logger.getLogger(ProductDetail.class.getName());
    private Long productId;
    private Product.SIZE productSize;

    public ProductDetail(final PageParameters parameters) {
        productId = parameters.get("id").toLongObject();
    }

    public JapanCuccokSession getJapanSession() {
        return (JapanCuccokSession) getSession();
    }

    public Cart getCart() {
        return getJapanSession().getCart();
    }

    public List<Product> getProducts() {
        return JapanCuccok.get().getProducts();
    }

    private FeedbackPanel feedbackPanel;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ProductModel productToShow = getProductModel();

        feedbackPanel = new FeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        feedbackPanel.setOutputMarkupPlaceholderTag(true);
        add(feedbackPanel);
        add(getSelectedImage(productToShow));
        add(getImagePanel(productToShow));
        add(getInfoPanel(productToShow));
        add(getSubmitButton(productToShow));
    }

    private ProductModel getProductModel() {
        return new ProductModel(productId, false);
    }

    private Component getSelectedImage(ProductModel productToShow) {
        LoadableDetachableModel imageListModel
                = new ProductDetailImageLoadStrategy<IImage>(productToShow);
        List<IImage> imageList = (List<IImage>) imageListModel.getObject();
        ExternalLink imageLink = new ExternalLink("selectedImageAnchor", new Model(getUrl(imageList.get(0), this)));
        imageLink.add(getImage(imageList.get(0), "selectedImage", this));
        return imageLink;
    }

    private Component getImagePanel(ProductModel productToShow) {

        ImageListView imgRepeatingView = new ImageListView<IImage>("imgRepeatingView", new ProductDetailImageLoadStrategy<IImage>(productToShow));
        final WebMarkupContainer dummyRepeaterContainer = new WebMarkupContainer("dummyRepeaterContainer");
        dummyRepeaterContainer.add(imgRepeatingView);
        dummyRepeaterContainer.setVisible(true);
        dummyRepeaterContainer.setOutputMarkupId(true);

        return dummyRepeaterContainer;
    }

    private Panel getInfoPanel(ProductModel productToShow) {
        Panel infoPanel = new InfoPanel("infoPanel", feedbackPanel, productToShow);
        return infoPanel;
    }

    private Component getSubmitButton(ProductModel productToShow) {
        AjaxFallbackLink submitLink = new AjaxFallbackLink<Product>("putIntoCart", productToShow) {

            private static final long serialVersionUID = 5212167386862636225L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                Product productToShow = (Product) getInnermostModel().getObject();
                target.add(feedbackPanel);
                if(productSize == null) {
                    error(getString("noSizeChosen"));
                    return;
                }
                Cart cart = getCart();
                List<Product> productList = cart.getProducts();
                for(Product product : productList) {
                    if(product != null) {
                        logger.info(product.toString());
                    }
                }
                List<Long> productIdList = cart.getProductIds();
                if(!productIdList.contains(productToShow.getId())) {
                    ProductMetaData productMetaData = new ProductMetaData(productToShow);
                    productMetaData.setChosenSize(productSize);
                    cart.add(productMetaData);
                    info(String.format(getString("productSuccessfullyInCart"), productToShow.getName()));
                    send(getSession(), Broadcast.DEPTH, new CartUpdate(target, productToShow.getId()));
                } else {
                    StringResourceModel model = new StringResourceModel("productAlreadyInCart",this,null);
                    error(model.getString());
                }
            }

        };
        submitLink.setOutputMarkupId(true);
        return submitLink;
    }
    
    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Termék részletei");
    }

    @Override
    public void onEvent(IEvent<?> event){
        super.onEvent(event);

        // check if this is a size choose event and if so repaint self
        if (event.getPayload() instanceof SizeChoose)
        {
            SizeChoose update = (SizeChoose)event.getPayload();
            this.productSize = update.getChosenSize();
        }
    }

}
