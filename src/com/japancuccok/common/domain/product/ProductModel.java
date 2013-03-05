package com.japancuccok.common.domain.product;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.UrlImage;
import org.apache.wicket.model.LoadableDetachableModel;

import static com.japancuccok.db.DAOService.productDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.06.
 * Time: 13:54
 */
public class ProductModel extends LoadableDetachableModel<Product> {

    private static final long serialVersionUID = 1219506579970687035L;
    
    private Long productId;
    private final boolean loadByGroupClass;

    public ProductModel(Long productId, boolean loadByGroupClass) {
        this.productId = productId;
        this.loadByGroupClass = loadByGroupClass;
    }

    @Override
    protected Product load() {
        if(loadByGroupClass) {
            Key<Product> productKey = Key.create(Product.class, productId);
            Product productToFullyLoad = productDao.get(productKey).get(productKey);
            return productDao.load(productToFullyLoad,
            		(Class<Product>[])new Class<?>[]{
                            Product.WithBinaryImage.class,
                            Product.WithUrlImage.class,
                            BinaryImage.WithBinaryImageData.class,
                            UrlImage.WithUrlImageData.class});
        } else {
            Key<Product> productKey = Key.create(Product.class, productId);
            return productDao.get(productKey).get(productKey);
        }
    }

}
