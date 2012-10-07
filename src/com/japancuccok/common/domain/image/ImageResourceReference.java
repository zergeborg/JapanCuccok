package com.japancuccok.common.domain.image;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.09.25.
 * Time: 23:08
 */
public class ImageResourceReference extends ResourceReference {

    private static final long serialVersionUID = -8007029817130894200L;
    private final IResource iResource;

    public ImageResourceReference(IResource iResource) {
        super(ImageResourceReference.class, "images");
        this.iResource = iResource;
    }

    @Override
    public IResource getResource() {
        return iResource;
    }
}
