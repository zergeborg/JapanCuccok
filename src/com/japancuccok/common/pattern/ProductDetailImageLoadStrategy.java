package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.domain.product.ProductModel;

import java.util.ArrayList;
import java.util.List;

import static com.japancuccok.db.DAOService.productDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.02.
 * Time: 23:47
 */
public class ProductDetailImageLoadStrategy<T extends IImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = -8517896100564563352L;
    private transient final Product productToShow;
    private transient final ProductModel productToShowModel;

    public ProductDetailImageLoadStrategy(Product productToShow) {
        super(true);
        this.productToShowModel = null;
        this.productToShow = productToShow;
    }

    public ProductDetailImageLoadStrategy(ProductModel productToShowModel) {
        super(true);
        this.productToShow = null;
        this.productToShowModel = productToShowModel;
    }

    @Override
    public List<T> load() {
        List<IImage> images = new ArrayList<IImage>();
        if(productToShowModel != null) {
            images = loadByModel();
        } else if (productToShow != null) {
            images = loadByObject();
        }
        return (List<T>) images;
    }

    private List<IImage> loadByModel() {
        Product loadedProduct = productDao.load(productToShowModel.getObject(),
                new Class<?>[]{
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class});
        List<IImage> images = new ArrayList<IImage>();
        if(loadedProduct.getBinaryImageList() != null) {
            images.addAll(loadedProduct.getBinaryImageList());
        }
        if(loadedProduct.getUrlImageList() != null) {
            images.addAll(loadedProduct.getUrlImageList());
        }
        return images;
    }

    private List<IImage> loadByObject() {
        Product loadedProduct = productDao.load(productToShow,
                new Class<?>[]{
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class});
        List<IImage> images = new ArrayList<IImage>();
        if(loadedProduct.getBinaryImageList() != null) {
            images.addAll(loadedProduct.getBinaryImageList());
        }
        if(loadedProduct.getUrlImageList() != null) {
            images.addAll(loadedProduct.getUrlImageList());
        }
        return images;
    }
}
