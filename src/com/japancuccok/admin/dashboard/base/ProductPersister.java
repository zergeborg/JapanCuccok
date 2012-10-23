package com.japancuccok.admin.dashboard.base;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.UrlImage;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.Component;

import java.util.ArrayList;
import java.util.List;

import static com.japancuccok.db.DAOService.productDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 16:06
 */
public class ProductPersister<T extends IImage, R extends Product> extends AbstractPersister<T, R> {

    public ProductPersister(IEventHandler successor, IEventHandlerPayload payload) {
        super(successor, payload);
    }

    public ProductPersister(IEventHandler successor) {
        super(successor);
    }

    @Override
    public IEvent handleEvent(IEvent event) {
        IPersistanceEvent<List<T>, R> newPersistanceEvent = (IPersistanceEvent<List<T>, R>) super.handleEvent(event);
        R result = storeProduct(newPersistanceEvent);
        newPersistanceEvent.put(new PersistanceResult<R>(result));
        return newPersistanceEvent;
    }

    protected R storeProduct(IPersistanceEvent<List<T>, R> persistanceEvent) {
        List<?> imageList = persistanceEvent.getPayload();
        List<BinaryImage> binaryImages = getBinaryImages(imageList);
        List<UrlImage> urlImages = getUrlImages(imageList);
        Product product = new Product(getPayload().getUploadModel().getNewProductName(),
                getPayload().getUploadModel().getNewProductCategory(),
                getPayload().getUploadModel().getNewProductDescription(),
                getPayload().getUploadModel().getNewProductPrice(),
                binaryImages, urlImages);

        //Check new file, delete if it already existed
        checkEntityExists(product);
        Key<Product> result;
        try {
            result = productDao.put(product);
            info("Saved product: " + product.getName());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to write file", e);
        }
        return (R) product;
    }

    private List<UrlImage> getUrlImages(List<?> imageList) {
        return (List<UrlImage>) getImagesByClass(imageList, UrlImage.class);
    }

    private List<BinaryImage> getBinaryImages(List<?> imageList) {
        return (List<BinaryImage>) getImagesByClass(imageList, BinaryImage.class);
    }

    private List<?> getImagesByClass(List<?> imageList, Class<?> classToGet) {
        List<IImage> images = new ArrayList();
        for(Object image : imageList) {
            boolean equal = image.getClass().equals(classToGet);
            if(equal) {
                images.add((IImage) image);
            }
        }
        return images;
    }

}
