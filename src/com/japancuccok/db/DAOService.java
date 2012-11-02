package com.japancuccok.db;

import com.japancuccok.common.domain.cart.Cart;
import com.japancuccok.common.domain.contact.Contact;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.28.
 * Time: 23:25
 */
public class DAOService implements ServletContextListener, Serializable {

    private static final long serialVersionUID = -3933191450026150500L;
    public static GenericGaeDAO<Product> productDao;
    public static GenericGaeDAO<IImage> imageDao;
    public static GenericGaeDAO<BaseImage> baseImageDao;
    public static GenericGaeDAO<BinaryImage> binaryImageDao;
    public static GenericGaeDAO<UrlImage> urlImageDao;
    public static GenericGaeDAO<BaseImageData> baseImageDataDao;
    public static GenericGaeDAO<BinaryImageData> binaryImageDataDao;
    public static GenericGaeDAO<UrlImageData> urlImageDataDao;
    public static GenericGaeDAO<ImageOptions> imageOptionsDao;
    public static GenericGaeDAO<Contact> contactDao;
    public static GenericGaeDAO<Cart> cartDao;
    public static GenericGaeDAO<ChunkFile> chunkFileDAO;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        final GenericGaeDAO<Product> productDao = (GenericGaeDAO<Product>) GenericGaeDAOFactory.getInstance(Product.class);
        final GenericGaeDAO<BaseImage> baseImageDao = (GenericGaeDAO<BaseImage>) GenericGaeDAOFactory.getInstance(BaseImage.class);
        final GenericGaeDAO<BinaryImage> binaryImageDao = (GenericGaeDAO<BinaryImage>) GenericGaeDAOFactory.getInstance(BinaryImage.class);
        final GenericGaeDAO<UrlImage> urlImageDao = (GenericGaeDAO<UrlImage>) GenericGaeDAOFactory.getInstance(UrlImage.class);
        final GenericGaeDAO<BaseImageData> baseImageDataDao = (GenericGaeDAO<BaseImageData>) GenericGaeDAOFactory.getInstance(BaseImageData.class);
        final GenericGaeDAO<BinaryImageData> binaryImageDataDao = (GenericGaeDAO<BinaryImageData>) GenericGaeDAOFactory.getInstance(BinaryImageData.class);
        final GenericGaeDAO<UrlImageData> urlImageDataDao = (GenericGaeDAO<UrlImageData>) GenericGaeDAOFactory.getInstance(UrlImageData.class);
        final GenericGaeDAO<ImageOptions> imageOptionsDao = (GenericGaeDAO<ImageOptions>) GenericGaeDAOFactory.getInstance(ImageOptions.class);
        final GenericGaeDAO<Contact> contactDao = (GenericGaeDAO<Contact>) GenericGaeDAOFactory.getInstance(Contact.class);
        final GenericGaeDAO<Cart> cartDao = (GenericGaeDAO<Cart>) GenericGaeDAOFactory.getInstance(Cart.class);
        final GenericGaeDAO<ChunkFile> chunkFileDAO = (GenericGaeDAO<ChunkFile>) GenericGaeDAOFactory.getInstance(ChunkFile.class);
        DAOService.productDao = productDao;
        DAOService.baseImageDao = baseImageDao;
        DAOService.binaryImageDao = binaryImageDao;
        DAOService.urlImageDao = urlImageDao;
        DAOService.baseImageDataDao = baseImageDataDao;
        DAOService.binaryImageDataDao = binaryImageDataDao;
        DAOService.urlImageDataDao = urlImageDataDao;
        DAOService.imageOptionsDao = imageOptionsDao;
        DAOService.contactDao = contactDao;
        DAOService.cartDao = cartDao;
        DAOService.chunkFileDAO = chunkFileDAO;
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
