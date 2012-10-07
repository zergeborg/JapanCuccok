package com.japancuccok.common.domain.image;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.model.LoadableDetachableModel;

import static com.japancuccok.db.DAOService.baseImageDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.08.
 * Time: 13:17
 */
public class ImageModel extends LoadableDetachableModel<IImage> {
    
    private static final long serialVersionUID = 1024450333935329629L;
    private Long imageId;

    public ImageModel(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    protected IImage load() {
        Key<BaseImage> productKey = Key.create(BaseImage.class, imageId);
        return baseImageDao.get(productKey).get(productKey);
    }
}
