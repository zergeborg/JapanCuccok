package com.japancuccok.common.domain.image;

import com.japancuccok.common.wicket.resource.MyDynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.09.25.
 * Time: 23:08
 */
public class ImageResourceReference extends ResourceReference {

    private static final long serialVersionUID = -8007029817130894200L;

    public ImageResourceReference() {
        super(ImageResourceReference.class, "images");
    }

    @Override
    public IResource getResource() {
        return new MyDynamicImageResource();
    }

}
