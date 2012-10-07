package com.japancuccok.common.domain.image;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Load;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.wicket.component.StaticImage;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.IResource;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.27.
 * Time: 23:14
 */
@EntitySubclass
@Cache
public class UrlImage extends BaseImage implements Serializable {

    private static final long serialVersionUID = 4951497829438091896L;

    public static class WithUrlImageData {}

    @Load({WithUrlImageData.class})
    UrlImageData imageData;

    private static Long staticId = 0l;

    public UrlImage() {
    }

    public UrlImage(UrlImage other) {
        this.id = other.id;
        this.name = other.name;
        this.category = other.category;
        this.fileType = other.fileType;
        this.mimeType = other.mimeType;
        this.description = other.description;
        this.imageOptions = other.imageOptions;
        this.imageData = other.imageData;
    }

    public UrlImage(ImageOptions imageOptions, UrlImageData imageData,
                    String name, CategoryType category, String fileType,
                    String mimeType, String description) {
        if((name == null) || (category == null) || (fileType == null) || (imageOptions == null)) {
            throw new IllegalArgumentException("The following fields must not be null: [name, " +
                    "fileType, category, imageOptions]");
        }
        synchronized (UrlImage.class) {
            staticId++;
        }
        this.imageOptions = imageOptions;
        this.imageData = imageData;
        this.name = name;
        this.category = category;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.description = description;
    }

    @Override
    public IImageData getImageData() {
        return imageData;
    }

    @Override
    public WebComponent asWicketImage(String imageComponentName) {
        return getStaticImage(imageComponentName);
    }

    private StaticImage getStaticImage(String imageComponentName) {
        return new StaticImage(imageComponentName, new ImageDataModel(imageData.getId()));
    }

    @Override
    public IResource asResource(String imageComponentName) {
        return getStaticImage(imageComponentName).getImageResource();
    }

    @Override
    public String toString() {
        return "UrlImage{" +
                "id='" + id + '\'' +
                ", productKey=" + getProductKey() +
                ", imageData=" + imageData +
                ", name='" + getName() + '\'' +
                ", category=" + category +
                ", fileType='" + getFileType() + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", description='" + getDescription() + '\'' +
                ", imageOptions='" + getImageOptions() + '\'' +
                '}';
    }
}
