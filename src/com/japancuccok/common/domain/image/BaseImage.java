package com.japancuccok.common.domain.image;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.IResource;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.26.
 * Time: 20:43
 */
@Entity
@Cache
public class BaseImage implements IImage, Serializable {

    private static final long serialVersionUID = -1455087740482925457L;

    public static class WithImageOptions{}

    @Id
    Long id;

    @Index
    @Embed
    @Load({WithImageOptions.class})
    ImageOptions imageOptions;

    @Parent
    @Load
    Ref<Product> productKey;
    protected String name;
    protected CategoryType category;
    protected String fileType;
    protected String mimeType;

    protected String description;

    @Override
    public WebComponent asWicketImage(String imageComponentName) {
        throw new RuntimeException(("You can't call this operation on base class, use a subclass instead"));
    }

    @Override
    public IResource asResource(String imageComponentName) {
        throw new RuntimeException(("You can't call this operation on base class, use a subclass instead"));
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public IImageData getImageData() {
        throw new RuntimeException(("You can't call this operation on base class, use a subclass instead"));
    }

    @Override
    public ImageOptions getImageOptions() {
        return imageOptions;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CategoryType getCategory() {
        return category;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public Ref<Product> getProductKey() {
        return productKey;
    }

    @Override
    public void setKey(Ref<Product> key) {
        this.productKey = key;
    }

    @Override
    public Ref<Product> getKey() {
        return getProductKey();
    }

    @Override
    public Collection<Ref<Product>> getKeys() {
        return null;
    }
}