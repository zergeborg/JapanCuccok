package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BaseImage;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;

import java.util.Iterator;
import java.util.List;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.13.
 * Time: 0:37
 */
public class DashboardImageLoadStrategy<T extends BaseImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = 7978663892006327991L;

    public DashboardImageLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

    public DashboardImageLoadStrategy(boolean all) {
        super(all);
    }

    @Override
    public List<T> load() {
        List<BaseImage> images = baseImageDao.load(
                new Class<?>[] {
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class,
                        BaseImage.WithImageOptions.class});
        Iterator imageIterator = images.iterator();
        while(imageIterator.hasNext()){
            IImage image = (IImage) imageIterator.next();
            if(!image.getImageOptions().isProductDashboard())  {
                imageIterator.remove();
            }
        }
        return (List<T>) images;
    }
}
