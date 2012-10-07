package com.japancuccok.admin.dashboard.base;

import com.googlecode.objectify.Key;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.ImageOptions;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;
import org.apache.wicket.Component;

import static com.japancuccok.db.DAOService.imageOptionsDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 15:01
 */
public class ImagePersister<T extends IImage, R extends Product> extends AbstractPersister<T, R> {

    protected ImagePersister(AbstractPersister successor, Component component, ProductUploadModel uploadModel) {
        super(successor, component, uploadModel);
    }

    protected ImageOptions storeImageOption(ImageOptions imageOption) {
        checkEntityExists(imageOption);
        Key<ImageOptions> result;
        result = imageOptionsDao.put(imageOption);
        return imageOption;
    }

    /**
     * Check whether the file already exists, and if so, try to delete it.
     *
     * @param entity the data handleMessage entity to check (e.g. product, image, etc...)
     */
    protected <T> void checkEntityExists(T entity) {
        GenericGaeDAO<T> dao = (GenericGaeDAO<T>) GenericGaeDAOFactory.getInstance(entity
                .getClass());
        // Try to delete the file
        try {
            dao.delete(entity);
        } catch (IllegalArgumentException e) {
            // Delete was unsuccessful, since the entity is missing
        }
    }

    protected void info(String s) {
        if(component != null) {
            component.info(s);
        }
    }

}
