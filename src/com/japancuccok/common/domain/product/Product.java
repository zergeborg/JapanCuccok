package com.japancuccok.common.domain.product;

import com.googlecode.objectify.annotation.*;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.UrlImage;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 20:58
 */
@Entity
@Cache
public class Product implements Serializable, ProductIf {

    private static final long serialVersionUID = 8853146639806522420L;

    public static class WithBinaryImage {}
    public static class WithUrlImage {}

    public enum SIZE {S,M,L,XL,XXL;};
    @Id Long id;

    @Load(WithBinaryImage.class)
    List<BinaryImage> binaryImageList;

    @Load(WithUrlImage.class)
    List<UrlImage> urlImageList;

    private static Long staticId = 0l;
    @Index private String name;
    @Index private CategoryType category;
    private String description;
    private int price;

    public Product() {
    }

    public Product(String name, CategoryType category, String description,
                   int price, List<BinaryImage> binaryImageList,
                   List<UrlImage> urlImageList) {
        if(name == null ||category == null || price == 0 ||
                binaryImageList == null || urlImageList == null ||
                (binaryImageList.size() == 0 && urlImageList.size() == 0)) {
            throw new IllegalArgumentException("The following parameters are mandatory: [ "+
                    "name, category, price, (binaryImageList or urlImageList) ]");
        }
        synchronized (Product.class) {
            staticId++;
        }
        this.name = name;
        this.category = category;
        this.price = price;
        this.binaryImageList = binaryImageList;
        this.urlImageList = urlImageList;
        this.description = description;
    }
    
    protected void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
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
    public String getDescription() {
        return description;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public List<BinaryImage> getBinaryImageList() {
        return binaryImageList;
    }

    public List<UrlImage> getUrlImageList() {
        return urlImageList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || !(o instanceof Product)) return false;

        Product product = (Product) o;

        if (!category.equals(product.category)) return false;
        if (!id.equals(product.id)) return false;
        if (!name.equals(product.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
