package com.japancuccok.common.domain.image;

import org.apache.wicket.request.resource.IResource;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.09.25.
 * Time: 22:04
 */
public interface IResourceProvider {
    
    public String getId();

    /**
     * @return Resource returned from subclass
     */
    public IResource getImageResource();

}
