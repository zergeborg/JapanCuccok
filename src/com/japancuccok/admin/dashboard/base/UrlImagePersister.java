package com.japancuccok.admin.dashboard.base;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.Component;

import java.net.URL;
import java.util.List;

import static com.japancuccok.db.DAOService.urlImageDao;
import static com.japancuccok.db.DAOService.urlImageDataDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 15:08
 */
public class UrlImagePersister extends ImagePersister<UrlImage, Product> {

    public UrlImagePersister(IEventHandler successor, IEventHandlerPayload payload) {
        super(successor, payload);
    }

    public UrlImagePersister(IEventHandler successor) {
        super(successor);
    }

    @Override
    public IEvent handleEvent(IEvent event) {
        IPersistanceEvent<List<UrlImage>, Product> newPersistanceEvent =
                (IPersistanceEvent<List<UrlImage>, Product>) super.handleEvent(event);
        handleUrls(newPersistanceEvent);
        return newPersistanceEvent;
    }

    protected void handleUrls(IPersistanceEvent<List<UrlImage>, Product> persistanceEvent) {
        List images = persistanceEvent.getPayload();
        List<ImageOptions> imageOptions = getPayload().getUploadModel().getImageOptions();
        List<URL> urls = getPayload().getUploadModel().getUrls();
        for (int i = 0; i<urls.size();i++)
        {
            URL imageUrl = urls.get(i);
            ImageOptions imageOption = imageOptions.get(i);
            if(imageUrl != null) {
                UrlImageData imageData = storeUrlImageData(imageUrl);
                ImageOptions imageOpt = storeImageOption(new ImageOptions(imageOption));
                UrlImage image = storeUrlImage(imageData, imageOpt);
                images.add(image);
            }
        }
    }

    private UrlImage storeUrlImage(UrlImageData imageData, ImageOptions imageOpt) {
        UrlImage image = new UrlImage(imageOpt, imageData, imageData.getImageURL().toString(),
                getPayload().getUploadModel().getNewProductCategory(),
                "",
                "",
                getPayload().getUploadModel().getNewProductDescription());
        checkEntityExists(image);
        Key<UrlImage> result;
        result = urlImageDao.put(image);
        imageData.setKey(Ref.create(result));
        info("Saved image: " + image.getName());

        return image;
    }

    private UrlImageData storeUrlImageData(URL fileUrl) {
        UrlImageData imageData = new UrlImageData(null,fileUrl);
        checkEntityExists(imageData);
        urlImageDataDao.put(imageData);
        info("Saved image data: " + imageData.getId());
        return imageData;
    }

}
