package com.japancuccok.common.domain.image;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EntitySubclass;

import java.io.Serializable;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.27.
 * Time: 23:15
 */
@EntitySubclass
@Cache
public class UrlImageData extends BaseImageData<UrlImage> implements Serializable {

    private static final long serialVersionUID = -8558374893499451659L;
    
    private URL imageURL;

    public UrlImageData() {
        super();
    }

    public UrlImageData(Ref<UrlImage> imageKey, URL imageURL) {
        super(imageKey);
        this.imageURL = imageURL;
    }

    public URL getImageURL() {
        return imageURL;
    }
}
