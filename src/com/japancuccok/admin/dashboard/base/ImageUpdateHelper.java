package com.japancuccok.admin.dashboard.base;

import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.wicket.panel.admin.dashboard.StyledPopupJSPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.27.
 * Time: 20:09
 */
public class ImageUpdateHelper {

    public static Panel getShowSpan(int index) {
        Panel showSpan = new StyledPopupJSPanel("showSpan");
        showSpan.setOutputMarkupPlaceholderTag(true);
        showSpan.setOutputMarkupId(true);
        showSpan.setMarkupId("showSpan" + index);
        return showSpan;
    }

    /**
     * Returns the first element of the embedded image list of the provided product
     *
     *
     * @param selectedImage
     * @param product The product which has embedded image list
     * @return The first element of the product's image list
     */
    public static WebComponent getWicketImage(IImage selectedImage, Product product) {
        IImage image = (selectedImage != null)
                ? selectedImage : (product.getBinaryImageList() != null)
                ? product.getBinaryImageList().get(0) : (product.getUrlImageList() != null)
                ? product.getUrlImageList().get(0) : null;
        WebComponent wicketImage = image.asWicketImage("image");
        wicketImage.add(new AttributeModifier("style", "max-width: 100%; max-height: 100%;"));
        return wicketImage;
    }

}
