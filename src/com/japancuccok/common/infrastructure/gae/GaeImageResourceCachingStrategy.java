package com.japancuccok.common.infrastructure.gae;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.caching.ResourceUrl;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.13.
 * Time: 21:27
 */
public class GaeImageResourceCachingStrategy implements IResourceCachingStrategy {
    @Override
    public void decorateUrl(ResourceUrl url, IStaticCacheableResource resource) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void undecorateUrl(ResourceUrl url) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void decorateResponse(AbstractResource.ResourceResponse response, IStaticCacheableResource resource) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
