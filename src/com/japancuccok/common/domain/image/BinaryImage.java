package com.japancuccok.common.domain.image;

import com.googlecode.objectify.annotation.*;
import com.japancuccok.common.domain.category.CategoryType;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.resource.IResource;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 20:58
 */
@EntitySubclass(index=true)
@Cache
public class BinaryImage extends BaseImage implements Serializable {

    private static final long serialVersionUID = -7269686191560842505L;

    public static class WithBinaryImageData {}

    @Load({WithBinaryImageData.class})
    BinaryImageData imageData;

    private static Long staticId = 0l;

    public BinaryImage() {
    }

    public BinaryImage(BinaryImage other) {
        this.id = other.id;
        this.name = other.name;
        this.category = other.category;
        this.fileType = other.fileType;
        this.mimeType = other.mimeType;
        this.description = other.description;
        this.imageOptions = other.imageOptions;
        this.imageData = other.imageData;
    }

    public BinaryImage(String name, CategoryType category, String fileType,
                       String mimeType, String description,
                       ImageOptions imageOptions, BinaryImageData imageData) {
        if((name == null) || (category == null) || (fileType == null) || (imageOptions == null)) {
            throw new IllegalArgumentException("The following fields must not be null: [name, " +
                    "fileType, category, imageOptions]");
        }
        synchronized (BinaryImage.class) {
            staticId++;
        }
        this.name = name;
        this.category = category;
        this.fileType = fileType;
        this.mimeType = mimeType;
        this.description = description;
        this.imageOptions = imageOptions;
        this.imageData = imageData;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public IImageData getImageData() {
        return imageData;
    }

    @Override
    public WebComponent asWicketImage(String imageComponentId) {
        DatastoreImage selectedImage = getDatastoreImage(imageComponentId);
        return selectedImage;
    }

    private DatastoreImage getDatastoreImage(String imageComponentId) {
        ImageOptions imageOptions = getImageOptions();
        DatastoreImage selectedImage = new DatastoreImage(imageComponentId, imageOptions);
        selectedImage.setOutputMarkupId(true);
        selectedImage.setVisible(true);
        return selectedImage;
    }

    @Override
    public IResource asResource(String imageComponentId) {
        return getDatastoreImage(imageComponentId).getImageResource();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || !(o instanceof BinaryImage)) return false;

        BinaryImage image = (BinaryImage) o;

        if (category != image.category) return false;
        if (fileType != null ? !fileType.equals(image.fileType) : image.fileType != null) return false;
        if (!id.equals(image.id)) return false;
        if (mimeType != null ? !mimeType.equals(image.mimeType) : image.mimeType != null) return false;
        if (!name.equals(image.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + fileType.hashCode();
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BinaryImage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", productKey=" + productKey +
                ", fileType='" + fileType + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", description='" + description + '\'' +
                ", imageOptions='" + imageOptions + '\'' +
                '}';
    }
}
