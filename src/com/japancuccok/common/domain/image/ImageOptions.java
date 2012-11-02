package com.japancuccok.common.domain.image;

import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.IfTrue;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.21.
 * Time: 8:57
 */
@Entity
@Embed
@Cache
public class ImageOptions implements Serializable {

    private static final long serialVersionUID = -2735311378771495558L;

    @Id Long id;
    private int width;
    private int height;
    private String name;
    @Index(IfTrue.class)
    boolean productGeneralPage;
    @Index(IfTrue.class)
    boolean productDashboard;

    public ImageOptions() {
    }

    public ImageOptions(ImageOptions other) {
        this.width = other.width;
        this.height = other.height;
        this.name = other.name;
        this.productDashboard = other.productDashboard;
        this.productGeneralPage = other.productGeneralPage;
    }

    public ImageOptions(int width, int height, String name, boolean productDashboard,
                        boolean productGeneralPage) {
        this.width = width;
        this.height = height;
        this.name = name;
        this.productDashboard = productDashboard;
        this.productGeneralPage = productGeneralPage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public boolean isProductGeneralPage() {
        return productGeneralPage;
    }

    public boolean isProductDashboard() {
        return productDashboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageOptions)) return false;

        ImageOptions that = (ImageOptions) o;

        if (height != that.height) return false;
        if (productDashboard != that.productDashboard) return false;
        if (productGeneralPage != that.productGeneralPage) return false;
        if (width != that.width) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (productGeneralPage ? 1 : 0);
        result = 31 * result + (productDashboard ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ImageOptions{" +
                "width=" + width +
                ", height=" + height +
                ", name='" + name + '\'' +
                ", productGeneralPage=" + productGeneralPage +
                ", productDashboard=" + productDashboard +
                '}';
    }
}
