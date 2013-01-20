package com.japancuccok.common.infrastructure.tools;

import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.ImageResourceReference;
import com.japancuccok.common.domain.image.UrlImageData;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.27.
 * Time: 19:31
 */
public class ImageResourceUtil {

    public static Component getImage(IImage image, String wicketImageName, Component component) {
        String imageUrl = getUrl(image, component);
        WebComponent wicketImage = new Image(wicketImageName, new ContextRelativeResource("./img/grey.gif"));
        wicketImage.add(new AttributeModifier("style", ""));
        wicketImage.add(new AttributeModifier("class", "lazy"));
        wicketImage.add(new AttributeModifier("data-original", new Model<String>(imageUrl)));
        return wicketImage;
    }

    public static String getUrl(IImage image, Component component) {
        ResourceReference imagesResourceReference =
                WebApplication.get().
                        getResourceReferenceRegistry().
                        getResourceReference(ImageResourceReference.class, "images", null, null, null, true, false);
        PageParameters imageParameters = new PageParameters();

        String url = null;
        if(image.getImageData() instanceof BinaryImageData) {
            Long imageDataId = image.getImageData().getId();
            imageParameters.set("imageFileNameWithExtension", imageDataId + ".png");
            CharSequence urlForImage =
                    component.getRequestCycle().urlFor(imagesResourceReference, imageParameters);
            url = urlForImage.toString();
        }
        if(image.getImageData() instanceof UrlImageData) {
            url = ((UrlImageData) image.getImageData()).getImageURL().toExternalForm();
        }
        return url;
    }


}
