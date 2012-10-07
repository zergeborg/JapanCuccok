package com.japancuccok.common.domain.image;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.model.LoadableDetachableModel;

import static com.japancuccok.db.DAOService.baseImageDataDao;
import static com.japancuccok.db.DAOService.productDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.07.
 * Time: 22:14
 */
public class ImageDataModel<T extends IImageData> extends LoadableDetachableModel<T> {

    private static final long serialVersionUID = 5686845006457190450L;

    private Long imageDataId;

    public ImageDataModel(Long imageDataId) {
        this.imageDataId = imageDataId;
    }

    @Override
    protected T load() {
        Key<BaseImageData> imageDataKeyKey = Key.create(BaseImageData.class, imageDataId);
        return (T) baseImageDataDao.get(imageDataKeyKey).get(imageDataKeyKey);
    }
}
