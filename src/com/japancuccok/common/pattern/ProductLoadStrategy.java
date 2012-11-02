package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;

import java.util.List;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.13.
 * Time: 0:20
 */
public class ProductLoadStrategy extends AbstractLoadStrategy<Product> {

    private static final long serialVersionUID = -5702607335911651771L;

    public ProductLoadStrategy() {
        super();
    }

    public ProductLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

    @Override
    public List<Product> load() {
        List<Product> binaryProducts = productDao.load(
                new Class<?>[]{
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class});
        return binaryProducts;
    }

}
