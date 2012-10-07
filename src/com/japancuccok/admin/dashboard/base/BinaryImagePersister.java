package com.japancuccok.admin.dashboard.base;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.common.domain.image.ImageOptions;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.Component;
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

    public BinaryImagePersister(AbstractPersister successor, Component component, ProductUploadModel uploadModel) {
        super(successor, component, uploadModel);
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
        List<ImageOptions> imageOptions = uploadModel.getImageOptions();
        List<List<FileUpload>> uploads = uploadModel.getUploads();
        for (int i = 0; i<uploads.size();i++)
        {
            List<FileUpload> uploadList = uploads.get(i);
            ImageOptions imageOption = imageOptions.get(i);
            if(uploadList != null) {
                FileUpload upload = uploadList.get(0);
                // We received a map, but this will sure to be single
                BinaryImageData imageData = storeBinaryImageData(upload);
                ImageOptions imageOpt = storeImageOption(new ImageOptions(imageOption));
                BinaryImage image = storeBinaryImage(upload, imageData, imageOpt);
                images.add(image);
            }
        }
    }

    private BinaryImageData storeBinaryImageData(FileUpload upload) {
        BinaryImageData imageData = new BinaryImageData(new Blob(upload.getBytes()), null);
        checkEntityExists(imageData);
        baseImageDataDao.saveBinary(imageData);
        info("Saved image data: " + imageData.getId());
        return imageData;
    }

    private BinaryImage storeBinaryImage(FileUpload upload, BinaryImageData singleImageData,
                                         ImageOptions imageOption) {
        BinaryImage image = new BinaryImage(
                upload.getClientFileName(), uploadModel.getNewProductCategory(),
                upload.getContentType(),
                upload.getContentType(), uploadModel.getNewProductDescription(),
                imageOption, singleImageData);
        checkEntityExists(image);
        Key<BinaryImage> result;
        result = binaryImageDao.put(image);
        singleImageData.setKey(Ref.create(result));
        info("Saved image: " + image.getName());

        return image;
    }

}
