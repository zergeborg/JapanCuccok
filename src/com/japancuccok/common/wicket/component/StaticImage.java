package com.japancuccok.common.wicket.component;

import com.japancuccok.common.domain.image.ResourceProvider;
import com.japancuccok.common.domain.image.UrlImageData;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.string.StringValue;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.31.
 * Time: 23:32
 */
public class StaticImage<T extends UrlImageData> extends ResourceProvider {

    private static final long serialVersionUID = -8332968259318875679L;

    private class ImageResource extends DynamicImageResource {

        private static final long serialVersionUID = 2534948066743214080L;

        @Override
        protected byte[] getImageData(Attributes attributes) {

            PageParameters parameters = attributes.getParameters();
            StringValue name = parameters.get("name");

            byte[] imageBytes = null;

            if (name.isEmpty() == false) {
                imageBytes = getImageAsBytes(name.toString());
            }
            return imageBytes;
        }

        private byte[] getImageAsBytes(String label) {
            // We don't have any data. The whole StaticImage class is a simple empty shell acting
            // like a real image
            return new byte[0];
        }

        @Override
        public boolean equals(Object that) {
            return that instanceof StaticImage.ImageResource;
        }
    }

    public StaticImage(String id, IModel<T> model) {
        super(id, model);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        checkComponentTag(tag, "img");
        UrlImageData imageData = (UrlImageData) getDefaultModelObject();
        tag.put("src", imageData.getImageURL().toExternalForm());
    }

    @Override
    public IResource getImageResource() {
        return new ImageResource();
    }
}
