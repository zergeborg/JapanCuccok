package com.japancuccok.admin.dashboard.base;

import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.wicket.panel.admin.dashboard.StyledPopupJSPanel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;

import static com.japancuccok.common.infrastructure.tools.ImageResourceUtil.getImage;

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
     *
     * @param selectedImage
     * @param product The product which has embedded image list
     * @return The first element of the product's image list
     */
    public static Component getWicketImage(String name,
                                           IImage selectedImage,
                                           Product product,
                                           Component component) {
        IImage image = (selectedImage != null)
                ? selectedImage : (product.getBinaryImageList() != null)
                ? product.getBinaryImageList().get(0) : (product.getUrlImageList() != null)
                ? product.getUrlImageList().get(0) : null;
        Component wicketImage = getImage(image, name, component);
        return wicketImage;
    }


}
