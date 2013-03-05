package com.japancuccok.common.pattern;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BaseImage;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.12.
 * Time: 23:49
 */
public class ImageLoadStrategy<T extends IImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = -8659440642144344850L;
    protected final Map<String, Object> conditions = new HashMap<String, Object>();

    public ImageLoadStrategy() {
        super();
    }

    public ImageLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

    @Override
    public List<T> load() {
        List<Product> products = null;
        if(categoryType == null) {
            products = productDao.load(
            		(Class<Product>[])new Class<?>[]{
                                    Product.WithBinaryImage.class,
                                    Product.WithUrlImage.class,
                                    BaseImage.WithImageOptions.class,
                                    BinaryImage.WithBinaryImageData.class,
                                    UrlImage.WithUrlImageData.class});
        } else {
            products = productDao.load(getConditions(),
            		(Class<Product>[])new Class<?>[]{Product.WithBinaryImage.class,
                                                  Product.WithUrlImage.class,
                                                  BaseImage.WithImageOptions.class,
                                                  BinaryImage.WithBinaryImageData.class,
                                                  UrlImage.WithUrlImageData.class});
        }
        List<T> imageList = loadByCategoryType(products, categoryType);
        return imageList;
    }

    protected Map<String, Object> getConditions() {
        conditions.put("category", categoryType);
        return conditions;
    }

    protected List<T> loadByCategoryType(List<Product> products, CategoryType categoryType) {
        List<T> imageList = new ArrayList<T>();
        for(Product product : products) {
            List<BinaryImage> productImageList = product.getBinaryImageList();
            if(productImageList != null) {
                for(BinaryImage image : productImageList) {
                    Key<Product> productKey = Key.create(Product.class, product.getId());
                    // TODO: Check whether productKey or image is null or not
                    // Maybe the API request was too large error is the root cause
                    if(image != null && productKey != null) {
                        image.setKey(Ref.create(productKey));
                        imageList.add((T) image);
                    }
                }
            }
            List<UrlImage> urlImageList = product.getUrlImageList();
            if(urlImageList != null) {
                for(UrlImage image : urlImageList) {
                    Key<Product> productKey = Key.create(Product.class, product.getId());
                    // TODO: Check whether productKey or image is null or not
                    // Maybe the API request was too large error is the root cause
                    if(image != null && productKey != null) {
                        image.setKey(Ref.create(productKey));
                        imageList.add((T) image);
                    }
                }
            }
        }
        return imageList;
    }
}
