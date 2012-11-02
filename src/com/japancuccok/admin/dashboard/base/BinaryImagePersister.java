package com.japancuccok.admin.dashboard.base;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.common.domain.image.ImageOptions;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import java.util.List;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 15:05
 */
public class BinaryImagePersister extends ImagePersister<BinaryImage, Product> {

    public BinaryImagePersister(IEventHandler successor, IEventHandlerPayload payload) {
        super(successor, payload);
    }

    public BinaryImagePersister(IEventHandler successor) {
        super(successor);
    }

    @Override
    public IEvent handleEvent(IEvent event) {
        IPersistanceEvent<List<BinaryImage>, Product> newPersistanceEvent =
                (IPersistanceEvent<List<BinaryImage>, Product>) super.handleEvent(event);
        handleFileUploads(newPersistanceEvent);
        return newPersistanceEvent;
    }

    private void handleFileUploads(IPersistanceEvent<List<BinaryImage>, Product> persistanceEvent) {
        List images = persistanceEvent.getPayload();
        List<ImageOptions> imageOptions = getPayload().getUploadModel().getImageOptions();
        List<List<FileUpload>> uploads = getPayload().getUploadModel().getUploads();
        for (int i = 0; i<uploads.size();i++)
        {
            List<FileUpload> uploadList = uploads.get(i);
            ImageOptions imageOption = imageOptions.get(i);
            if(uploadList != null) {
                FileUpload upload = uploadList.get(0);
                BinaryImageData imageData = storeBinaryImageData(upload);
                ImageOptions imageOpt = storeImageOption(new ImageOptions(imageOption));
                BinaryImage image = storeBinaryImage(upload, imageData, imageOpt);
                images.add(image);
            }
        }
    }

    private BinaryImageData storeBinaryImageData(FileUpload upload) {
        List<Key<ChunkFile>> keyList =
                baseImageDataDao.saveBinary(new Blob(upload.getBytes()));
        BinaryImageData imageData = new BinaryImageData(null, keyList);
        checkEntityExists(imageData);
        baseImageDataDao.put(imageData);
        info("Saved image data: " + imageData.getId());
        return imageData;
    }

    private BinaryImage storeBinaryImage(FileUpload upload, BinaryImageData singleImageData,
                                         ImageOptions imageOption) {
        BinaryImage image = new BinaryImage(
                upload.getClientFileName(),
                getPayload().getUploadModel().getNewProductCategory(),
                upload.getContentType(),
                upload.getContentType(),
                getPayload().getUploadModel().getNewProductDescription(),
                imageOption, singleImageData);
        checkEntityExists(image);
        Key<BinaryImage> result;
        result = binaryImageDao.put(image);
        singleImageData.setKey(Ref.create(result));
        info("Saved image: " + image.getName());

        return image;
    }

}
