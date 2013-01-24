package com.japancuccok.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ImageDropdownRenderer;
import com.japancuccok.admin.dashboard.base.ImageUpdateHelper;
import com.japancuccok.admin.dashboard.base.ImageUpdater;
import com.japancuccok.admin.products.DetailPage;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.db.IBinaryProvider;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 0:27
 */
public class UploadedProductListView extends ListView<Product> {

    private static final long serialVersionUID = -7909204154561013682L;

    transient private static final Logger logger = Logger.getLogger(UploadedProductListView.class.getName());
    
    transient private IImage selectedImage;

    /**
     * Construct.
     *
     * @param name
     *            Component name
     * @param products
     *            The product list model
     */
    public UploadedProductListView(String name, final IModel<List<Product>> products)
    {
        super(name, products);
        setReuseItems(true);
    }

    /**
     * @see ListView#populateItem(ListItem)
     */
    @Override
    protected void populateItem(ListItem<Product> listItem)
    {
        Panel showSpan = ImageUpdateHelper.getShowSpan(listItem.getIndex());
        Product product = listItem.getModelObject();
        Link<Void> showLink = getShowLink(listItem.getIndex());
        Component wicketImage =
                ImageUpdateHelper.getWicketImage("image", selectedImage, product, this);
        IModel<List<? extends IImage>> imageChoices = getImageChoices(product);
        CompoundPropertyModel<Product> productModel = new CompoundPropertyModel<Product>(product);

        setOutputMarkupId(true);

        wicketImage.add(new AttributeModifier("style", "max-width: 100%; max-height: 100%;"));
        showSpan.add(wicketImage);

        listItem.setModel(productModel);
        listItem.add(new Label("name"));
        listItem.add(new Label("price"));
        listItem.add(new Label("category"));
        listItem.add(new Label("description"));
        listItem.add(showLink);
        listItem.add(showSpan);
        listItem.add(getDeleteLink(product));
        listItem.add(getEditLink(product));

        DropDownChoice<IImage> imageDropdown =
                getImageDropdown(showSpan, showLink, imageChoices, listItem);
        listItem.add(imageDropdown);
        listItem.setVersioned(true);
    }

    private IModel<List<? extends IImage>> getImageChoices(final Product product) {
        IModel<List<? extends IImage>> categoryChoices = new AbstractReadOnlyModel<List<? extends
                IImage>>()
        {
            private static final long serialVersionUID = 5927054028721218235L;

            @Override
            public List<? extends IImage> getObject()
            {
                List<IImage> images = new ArrayList<IImage>();
                if(product.getBinaryImageList() != null) {
                    images.addAll(product.getBinaryImageList());
                }
                if(product.getUrlImageList() != null) {
                    images.addAll(product.getUrlImageList());
                }
                return images;
            }

        };

        return categoryChoices;
    }

    private Link<Void> getShowLink(int index) {
        Link<Void> showLink =  new Link<Void>("showLink")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick() {
                logger.fine("The user clicked show");
            }
        };
        showLink.setOutputMarkupId(true);
        showLink.setVisible(false);
        showLink.setOutputMarkupPlaceholderTag(true);
        showLink.setVersioned(true);
        showLink.setMarkupId("showLink" + index);
        showLink.add(new AttributeModifier("onclick", "showPopup(showSpan" + index + ");return false;"));
        return showLink;
    }

    private Link<Void> getDeleteLink(final Product finalProduct) {
        Link<Void> deleteLink = new Link<Void>("delete")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick()
            {
                logger.fine("The user clicked delete");
                //cleanAll();
                delete(finalProduct);
                info("Deleted " + finalProduct.getName());
            }
        };
        deleteLink.setOutputMarkupId(true);
        return deleteLink;
    }

    private Link<DetailPage> getEditLink(Product product) {
        PageParameters parameters = new PageParameters();
        parameters.add("id", product.getId());
        BookmarkablePageLink<DetailPage> editLink = new BookmarkablePageLink<DetailPage>("edit",
                DetailPage.class, parameters);
        editLink.setOutputMarkupId(true);
        return editLink;
    }

    private DropDownChoice<IImage> getImageDropdown(Panel showSpan,
                                                    Link<Void> showLink,
                                                    IModel<List<? extends IImage>> imageChoices,
                                                    ListItem<Product> listItem) {
        final DropDownChoice<IImage> categories = new DropDownChoice<IImage>("imageList",new PropertyModel<IImage>(this, "selectedImage"),imageChoices,new ImageDropdownRenderer());
        categories.add(new ImageUpdater("onchange", categories, showSpan, showLink, listItem));
        categories.clearInput();
        categories.setOutputMarkupId(true);
        categories.setNullValid(true);
        return categories;
    }

    private void delete(Product finalProduct) {
        List<BinaryImage> binaryImages = finalProduct.getBinaryImageList();
        if(binaryImages != null) {
            for(BinaryImage image : binaryImages) {
                baseImageDataDao.deleteBinary((IBinaryProvider) image.getImageData());
                imageOptionsDao.delete(image.getImageOptions());
                binaryImageDao.delete(image);
            }
        }
        List<UrlImage> urlImages = finalProduct.getUrlImageList();
        if(urlImages != null) {
            for(UrlImage image : urlImages) {
                urlImageDataDao.delete((UrlImageData) image.getImageData());
                imageOptionsDao.delete(image.getImageOptions());
                urlImageDao.delete(image);
            }
        }
        logger.fine("The binary data of " + finalProduct.getName() + " deleted");
        logger.fine("The image data of " + finalProduct.getName() + " deleted");
        logger.fine("The image of " + finalProduct.getName() + " deleted");
        productDao.delete(finalProduct);
        logger.fine("The product of " + finalProduct.getName() + " deleted");
    }

    private void cleanAll() {
        List<Product> productList = productDao.load(Product.WithBinaryImage.class);
        List<BinaryImage> imageList = binaryImageDao.load(Product.WithBinaryImage.class);
        List<BaseImageData> imageDataList = baseImageDataDao.load(Product.WithBinaryImage.class);
        for(IImageData imageData : imageDataList) {
            baseImageDataDao.deleteBinary((IBinaryProvider) imageData);
        }
        binaryImageDao.deleteAll(imageList);
        productDao.deleteAll(productList);
        baseImageDataDao.deleteAll(imageDataList);
    }

}
