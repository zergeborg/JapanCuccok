package com.japancuccok.admin.dashboard.base;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.ImageOptions;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 23:55
 */
public class ProductUploadModel implements IClusterable {

    private static final long serialVersionUID = 6748641364849805680L;

    private String newProductName;
    private String newProductDescription;
    private CategoryType newProductCategory;
    private Integer newProductPrice;
    private final List<ImageOptions> imageOptions = new ArrayList<ImageOptions>();
    private final List<List<FileUpload>> uploads = new ArrayList<List<FileUpload>>();
    private final List<URL> urls = new ArrayList<URL>();

    public ProductUploadModel() {
    }

    public ProductUploadModel(Product product) {
        this();
        this.newProductName = product.getName();
        this.newProductDescription = product.getDescription();
        this.newProductCategory = product.getCategory();
        this.newProductPrice = product.getPrice();
        for(IImage image : product.getBinaryImageList()) {
            imageOptions.add(image.getImageOptions());
        }
    }

    public ProductUploadModel(String newProductName, String newProductDescription, 
                              CategoryType newProductCategory, Integer newProductPrice) {
        this();
        this.newProductName = newProductName;
        this.newProductDescription = newProductDescription;
        this.newProductCategory = newProductCategory;
        this.newProductPrice = newProductPrice;
    }

    public String getNewProductName() {
        return newProductName;
    }

    public void setNewProductName(String newProductName) {
        this.newProductName = newProductName;
    }

    public String getNewProductDescription() {
        return newProductDescription;
    }

    public void setNewProductDescription(String newProductDescription) {
        this.newProductDescription = newProductDescription;
    }

    public CategoryType getNewProductCategory() {
        return newProductCategory;
    }

    public void setNewProductCategory(CategoryType newProductCategory) {
        this.newProductCategory = newProductCategory;
    }

    public Integer getNewProductPrice() {
        return newProductPrice;
    }

    public void setNewProductPrice(Integer newProductPrice) {
        this.newProductPrice = newProductPrice;
    }

    public List<List<FileUpload>> getUploads() {
        return uploads;
    }

    public List<ImageOptions> getImageOptions() {
        return imageOptions;
    }

    public List<URL> getUrls() {
        return urls;
    }
}
