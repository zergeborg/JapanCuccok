package com.japancuccok.common.pattern;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.BaseImage;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.13.
 * Time: 0:37
 */
public class DashboardImageLoadStrategy<T extends BaseImage> extends AbstractLoadStrategy<T> {

    private static final long serialVersionUID = 7978663892006327991L;

    public DashboardImageLoadStrategy() {
        super();
    }

    public DashboardImageLoadStrategy(CategoryType categoryType) {
        super(categoryType);
    }

    @Override
    public List<T> load() {
        Map<String, Object> conditions = new HashMap();
        conditions.put("imageOptions.productDashboard",true);
        List<BaseImage> images =
                baseImageDao.load(
                conditions,
                (Class<BaseImage>[]) new Class<?>[] {
                        Product.WithBinaryImage.class,
                        Product.WithUrlImage.class,
                        BinaryImage.WithBinaryImageData.class,
                        UrlImage.WithUrlImageData.class,
                        BaseImage.WithImageOptions.class});
        return (List<T>) images;
    }
}
