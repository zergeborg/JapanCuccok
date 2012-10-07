package com.japancuccok.main.dashboard;

import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.db.DAOService;
import com.japancuccok.db.GenericGaeDAO;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.24.
 * Time: 1:19
 */
public class SliderListView<T extends BaseImage> extends ListView<T> {

    private static final long serialVersionUID = 5235676500079567095L;

    transient private static final Logger logger = Logger.getLogger(SliderListView.class.getName());

    public SliderListView(String id) {
        super(id);
    }

    public SliderListView(String id, IModel<? extends List<T>> iModel) {
        super(id, iModel);
    }

    @Override
    protected void populateItem(ListItem<T> listItem) {
        setOutputMarkupId(true);

        IImage image = listItem.getModelObject();
        listItem.add(getImageLink(image));
    }

    private Link<Void> getImageLink(IImage image) {
        PageParameters parameters = new PageParameters();
//        Product product = getProduct(image);
//        parameters.add("id", product.getId());
//        BookmarkablePageLink imageLink = new BookmarkablePageLink<BaseImage>("imageAnchor", ProductDetail.class, parameters);
        BookmarkablePageLink imageLink = new BookmarkablePageLink<BaseImage>("imageAnchor", getPage().getClass());
        imageLink.setOutputMarkupId(true);
        imageLink.add(getImageWrapper(image));
        imageLink.add(getLabel(image));
        return imageLink;
    }

    private Product getProduct(IImage image) {
        GenericGaeDAO<Product> productDAO = DAOService.productDao;
        return productDAO.find(image.getKey().getKey());
    }


    private WebComponent getImage(IImage image) {
        WebComponent imageTag = image.asWicketImage("imageTag");
        String hidden = "visibility:hidden;";
        String maxW = "max-width:100%;";
        String maxH = "max-height:90%;";
        imageTag.add(new AttributeModifier("style", hidden+maxW+maxH));
        return imageTag;
    }

    private String getUrl(IImage image) {
        ResourceReference imagesResourceReference = new ImageResourceReference(image.asResource("imageTag"));
        PageParameters imageParameters = new PageParameters();

        String url = null;
        if(image.getImageData() instanceof BinaryImageData) {
            String name = image.getImageOptions().getName();
            imageParameters.set("name", name);
            CharSequence urlForImage = getRequestCycle().urlFor(imagesResourceReference, imageParameters);
            url = urlForImage.toString();
        }
        if(image.getImageData() instanceof UrlImageData) {
            url = ((UrlImageData) image.getImageData()).getImageURL().toExternalForm();
        }
        return url;
    }

    private WebMarkupContainer getImageWrapper(IImage image) {
        String imageUrl = getUrl(image);
        WebMarkupContainer imageWrapper = new WebMarkupContainer("imageWrapper");
        imageWrapper.add(getImage(image));
        String bg = "background:url('"+imageUrl+"') no-repeat;";
        String bgSize = "background-size: cover;";
        String maxH = "max-height:100%;";
        imageWrapper.add(new AttributeModifier("style",
                new Model<String>(bg+bgSize)));
        return imageWrapper;
    }

    private Label getLabel(IImage image) {
        return new Label("imageLabel", new PropertyModel<Object> (new ImageModel(image.getId()), "description"));
    }

}
