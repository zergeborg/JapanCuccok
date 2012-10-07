package com.japancuccok.common.domain.image;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.IResource;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.09.25.
 * Time: 22:05
 */
public class ResourceProvider extends WebComponent implements IResourceProvider {

    private static final long serialVersionUID = 7508227718261761098L;

    private IResourceProvider component;

    public ResourceProvider(IResourceProvider component) {
        super(component.getId(), null);
        this.component = component;
    }

    public ResourceProvider(String id) {
        super(id);
    }

    public ResourceProvider(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    public IResource getImageResource() {
        return component.getImageResource();
    }
}
