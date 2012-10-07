package com.japancuccok.common.domain.image;

import com.japancuccok.common.domain.product.Product;
import com.japancuccok.db.IKeyProvider;
import com.japancuccok.db.Identifiable;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.request.resource.IResource;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:03
 */
public interface IImage extends IImageInfo, IKeyProvider<Product>, Identifiable {

    /**
     * Returns a Wicket component which is constructed based on the underlying <code>IImage</code>
     * instance. The constructed instance will generate output markup ID by default and it is set
     * visible.
     *
     * @param imageComponentName The desired name of the newly created component
     * @return A new {@link Image} instance
     */
    public WebComponent asWicketImage(String imageComponentName);

    public IResource asResource(String imageComponentName);

}
