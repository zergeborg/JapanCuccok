package com.japancuccok.common.domain.image;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreInputStream;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.time.TimeOfDay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author uudashr
 *
 */
public class DatastoreImage extends NonCachingImage implements Serializable, IResourceProvider {

    private static final long serialVersionUID = -6103333163734189303L;
    transient private static final Logger logger = Logger.getLogger(DatastoreImage.class.getName());

    private ImageOptions options;
    private int oldWidth;
    private int oldHeight;
    private int newWidth;
    private int newHeight;
    private int newFileSize;
    private int oldFileSize;
    transient private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();

    public DatastoreImage(String id, IModel<IImageData> iModel, ImageOptions options) {
        super(id, iModel);
        this.options = options;
    }

    /**
     * @see org.apache.wicket.markup.html.image.Image#getImageResource()
     */
    @Override
    public IResource getImageResource() {
        return new MyDynamicImageResource(options);
    }

    public int getOldWidth() {
        return this.oldWidth;
    }

    public int getOldHeight() {
        return this.oldHeight;
    }

    public int getOldFileSize() {
        return this.oldFileSize;
    }

    public int getNewHeight() {
        return this.newHeight;
    }

    public int getNewWidth() {
        return this.newWidth;
    }

    public int getNewFileSize() {
        return this.newFileSize;
    }

    private class MyDynamicImageResource extends DynamicImageResource {

        private static final long serialVersionUID = -2812349562312499405L;

        private final ImageOptions options;

        public MyDynamicImageResource(ImageOptions options) {
            this.options = options;
            DatastoreImage.this.oldWidth = -1;
            DatastoreImage.this.oldHeight = -1;
            DatastoreImage.this.newWidth = -1;
            DatastoreImage.this.newHeight = -1;
            DatastoreImage.this.newFileSize = -1;
            DatastoreImage.this.oldFileSize = -1;
        }

        private byte[] getImageData() {
            logger.fine("Trying to return image data [ " + getId() + " ] of type [ " +
                    "" + getFormat() + " ]");
            try {
                if(getDefaultModelObject() instanceof BinaryImageData) {
                    BinaryImageData imageData = (BinaryImageData)getDefaultModelObject();
                    byte[] data = imageData.getBytes();

                    com.google.appengine.api.images.Image oldImage = ImagesServiceFactory.makeImage(data);
                    oldWidth = oldImage.getWidth();
                    oldHeight = oldImage.getHeight();
                    oldFileSize = oldImage.getImageData().length;

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
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new WicketRuntimeException(e);
            } finally {
                // ...
            }
            return new byte[] {};
        }

        @Override
        protected byte[] getImageData(Attributes attributes) {
            return getImageData();
        }

        @Override
        protected void setResponseHeaders(final ResourceResponse data,
                                          final Attributes attributes) {
            Response response = attributes.getResponse();
            if (response instanceof WebResponse)
            {
                WebResponse webResponse = (WebResponse)response;

                webResponse.setHeader("Pragma", "max-age=3600, must-revalidate");
                webResponse.setHeader("Cache-Control", "max-age=3600, must-revalidate");
                webResponse.setDateHeader("Expires",
                        Time.valueOf(TimeOfDay.now()));
            }
        }

    }
}