package com.japancuccok.common.domain.image;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.japancuccok.common.wicket.resource.MyDynamicImageResource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * @author uudashr
 *
 */
public class DatastoreImage extends Image implements Serializable {

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

    public DatastoreImage(String id, ImageOptions options) {
        super(id);
        this.options = options;
    }

    @Override
    protected IResource getImageResource()
    {
        return new MyDynamicImageResource();
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

}