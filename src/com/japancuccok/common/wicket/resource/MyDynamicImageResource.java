package com.japancuccok.common.wicket.resource;

import com.google.appengine.api.images.ImagesServiceFactory;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.common.domain.image.IImageData;
import com.japancuccok.common.domain.image.ImageDataModel;
import com.japancuccok.common.domain.image.ImageResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.time.TimeOfDay;

import java.util.logging.Logger;

/**
* Created by IntelliJ IDEA.
* User: Gergő
* Date: 2012.12.24.
* Time: 1:33
* To change this template use File | Settings | File Templates.
*/
public class MyDynamicImageResource extends DynamicImageResource {

    private static final long serialVersionUID = -2812349562312499405L;
    transient private static final Logger logger = Logger.getLogger(MyDynamicImageResource.class.getName());

    private byte[] getImageData(Long imageDataId) {
        logger.fine("Trying to return image data of type [ " + getFormat() + " ]");
        try {
            ImageDataModel imageModel = new ImageDataModel<IImageData>(imageDataId);
            BinaryImageData imageData = (BinaryImageData)imageModel.load();
            byte[] data = imageData.getBytes();

            com.google.appengine.api.images.Image oldImage = ImagesServiceFactory.makeImage(data);

            // TODO: this is not really needed
//                Transform resize = ImagesServiceFactory.makeResize(options.getWidth(), options.getHeight(), true);
//                com.google.appengine.api.images.Image newImage = imagesService.applyTransform
//                        (resize, oldImage, ImagesService.OutputEncoding.PNG);
//                newWidth = newImage.getWidth();
//                newHeight = newImage.getHeight();
//
//                newFileSize = newImage.getImageData().length;
            data = oldImage.getImageData();
            return data;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new WicketRuntimeException(e);
        } finally {
            // ...
        }
    }

    @Override
    protected byte[] getImageData(Attributes attributes) {
        PageParameters parameters = attributes.getParameters();
        Long imageDataId = Long.parseLong(parameters.get("imageFileNameWithExtension").toString().replaceAll("\\.png", ""));
        return getImageData(imageDataId);
    }

    @Override
    protected void setResponseHeaders(final ResourceResponse data,
                                      final Attributes attributes) {
        Response response = attributes.getResponse();
        if (response instanceof WebResponse)
        {
            WebResponse webResponse = (WebResponse)response;

            webResponse.setContentType("image/"+getFormat());
            webResponse.setHeader("Pragma", "max-age=3600, must-revalidate");
            webResponse.setHeader("Cache-Control", "max-age=3600, must-revalidate");
            webResponse.setDateHeader("Expires",
                    Time.valueOf(TimeOfDay.now()));
        }
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof MyDynamicImageResource;
    }

}
