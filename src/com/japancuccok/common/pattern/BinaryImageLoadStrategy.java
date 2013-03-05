package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BaseImage;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.product.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 20:13
 */
public class BinaryImageLoadStrategy<T extends BinaryImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = 2099622144447399155L;

    private Product productToEdit;

    public BinaryImageLoadStrategy(Product productToEdit) {
        this.productToEdit = productToEdit;
    }

    public BinaryImageLoadStrategy(CategoryType categoryType, Product productToEdit) {
        super(categoryType);
        this.productToEdit = productToEdit;
    }

    @Override
    public List<T> load() {
        productToEdit = productDao.load(productToEdit,
        		(Class<Product>[]) new Class<?>[]{Product.WithBinaryImage.class});
        List<T> images = new ArrayList<T>();
        if(productToEdit.getBinaryImageList() != null) {
            images.addAll((Collection<? extends T>) productToEdit.getBinaryImageList());
        }
        return images;
    }

}
