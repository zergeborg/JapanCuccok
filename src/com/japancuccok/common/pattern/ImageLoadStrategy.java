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
import java.util.Iterator;
import java.util.List;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.12.
 * Time: 23:49
 */
public class ImageLoadStrategy<T extends IImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = -8659440642144344850L;

    public ImageLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

    public ImageLoadStrategy(boolean all) {
        super(all);
    }

    @Override
    public List<T> load() {
        //TODO: #1 load the products from data store
        //TODO: #2 load the images from blob store
        List<Product> products = productDao.load(
                new Class<?>[]{
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BaseImage.WithImageOptions.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class});
        List<T> imageList = new ArrayList<T>();
        Iterator productIterator = products.iterator();
        while(productIterator.hasNext()){
            Product product = (Product) productIterator.next();
            if(all) {
                for(CategoryType cType : CategoryType.values()) {
                    if(!loadByCategoryType(imageList, product, cType)) {
                        //TODO: A warning should be logged here
                    }
                }
            } else {
                if(!loadByCategoryType(imageList, product, categoryType)){
                    //TODO: A warning should be logged here
                }
            }
        }
        return imageList;
    }

    protected boolean loadByCategoryType(List<T> imageList, Product product,
                                         CategoryType categoryType) {
        boolean loaded = false;
        if((product != null) && (product.getCategory().equals(categoryType)))
        {
            List<BinaryImage> productImageList = product.getBinaryImageList();
            if(productImageList != null) {
                for(BinaryImage image : productImageList) {
                    Key<Product> productKey = Key.create(Product.class, product.getId());
                    // TODO: Check whether productKey or image is null or not
                    // Maybe the API request was too large error is the root cause
                    if(image != null && productKey != null) {
                        image.setKey(Ref.create(productKey));
                        imageList.add((T) image);
                        loaded = true;
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
                        loaded = true;
                    }
                }
            }
        }
        return loaded;
    }
}
