package com.japancuccok.admin;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.SaveException;
import com.japancuccok.base.TestServiceProvider;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;
import com.japancuccok.db.IBinaryProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 12:12
 */
public class JapanCuccokAdminTest {

    private static final String TEST_IMAGE_NAME = "Test Name";
    private static final String TEST_IMAGE_DESCRIPTION = "Test Description";
    private static final CategoryType TEST_IMAGE_CATEGORY = CategoryType.TSHIRT;
    private static final String TEST_IMAGE_URL_STRING = "http://google.com";
    private static URL testUrl;
    static {
        try {
             testUrl = new URL(TEST_IMAGE_URL_STRING);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static final LocalDatastoreServiceTestConfig testConfig = new
            LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(testConfig)
            .setEnvIsAdmin(true).setEnvIsLoggedIn(true);
    private static CountDownLatch startGate = new CountDownLatch(1);
    private static CountDownLatch endGate = new CountDownLatch(2);
    private static Object lockObject = new Object();
    private static final Blob testBlob = new Blob(new byte[]{0, 1, 2, 3, 4, 5, 6, 7});
    private static List<Key<ChunkFile>> testKeyList;
    private static BinaryImageData testBinaryImageData = new BinaryImageData(null, testKeyList);
    private static ImageOptions testImageOptions = new ImageOptions(10,10,TEST_IMAGE_NAME,true,true);
    private static BinaryImage testBinaryImage = new BinaryImage(TEST_IMAGE_NAME,TEST_IMAGE_CATEGORY,"image/jpg",
            "image/jpg",TEST_IMAGE_DESCRIPTION,testImageOptions, testBinaryImageData);
    private List<BinaryImage> binaryImageList = new ArrayList<BinaryImage>();
    private static UrlImageData testUrlImageData = new UrlImageData(null, testUrl);
    private static UrlImage testUrlImage = new UrlImage(testImageOptions,testUrlImageData,TEST_IMAGE_NAME,
            TEST_IMAGE_CATEGORY,"N/A","N/A",TEST_IMAGE_DESCRIPTION);
    private List<UrlImage> urlImageList = new ArrayList<UrlImage>();

    static {
        try {
            TestServiceProvider.setUpServlet();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() throws Exception {
        TestServiceProvider.begin();
        binaryImageList.add(testBinaryImage);
        urlImageList.add(testUrlImage);
    }

    @After
    public void tearDown() throws Exception {
        TestServiceProvider.end();
    }

    @Test(expected = SaveException.class)
    public void testProductDAOPut_errorIfImageNotSaved() throws Exception {
        //given
        // DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have a product, but we don't save the image before
        Product product = new Product("dummyProductName", CategoryType.TSHIRT,
                "dummyProductDescrption", 1000, binaryImageList, urlImageList);

        //when
        // I put the product into the datastore
        productDao.put(product);

        //then
        // we get a huge Exception
        fail("Test should never ever get here. An Exception should have been thrown yet.");
    }

    @Test
    public void testProductDAOPut() throws Exception {
        //given
        // DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have a product
        Product product = new Product("dummyProductName", CategoryType.TSHIRT,
                "dummyProductDescrption", 1000, binaryImageList, urlImageList);

        //when we save the product
        saveProduct(product);

        //then
        // I will have a binary and a URL image data
        assertEquals(2, baseImageDataDao.list().size());
        IBinaryProvider imageData = (IBinaryProvider) baseImageDataDao.list().get(0);
        // Containing a simple blob with byte array data
        assertTrue(imageData.getRawData().equals(testBlob));
        // And I can retrieve and delete the product
        assert productDao.list() != null;
        productDao.delete(product);
    }

    @Test
    public void testProductDAODelete() throws Exception {
        //given the DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have a product
        Product product = new Product("dummyProductName", CategoryType.TSHIRT,
                "dummyProductDescrption", 1000, binaryImageList, urlImageList);

        //when we save the product
        saveProduct(product);

        // and we delete the product later on
        deleteProduct(product);

        //then
        List<Product> products = productDao.list();
        assert products != null;
        // we have no products left in the datastore
        assertTrue(products.isEmpty());
        // and we have neither binary images...
        List<BinaryImage> binaryImages = binaryImageDao.list();
        assert binaryImages != null;
        assertTrue(binaryImages.isEmpty());
        // ...nor URL images...
        List<UrlImage> urlImages = urlImageDao.list();
        assert urlImages != null;
        assertTrue(urlImages.isEmpty());
        // ...and base images left
        List<BaseImage> baseImages = baseImageDao.list();
        assert baseImages != null;
        assertTrue(baseImages.isEmpty());
    }

    private void deleteProduct(Product product) {
        for(BinaryImage image : product.getBinaryImageList()) {
            baseImageDataDao.deleteBinary((IBinaryProvider) image.getImageData());
        }
        for(UrlImage image : product.getUrlImageList()) {
            urlImageDataDao.delete((UrlImageData) image.getImageData());
        }
        System.out.println("The binary data of " + product.getName() + " deleted");
        for(IImage image : product.getBinaryImageList()) {
            imageOptionsDao.delete(image.getImageOptions());
        }
        for(BinaryImage image : product.getBinaryImageList()) {
            binaryImageDao.delete(image);
        }
        for(UrlImage image : product.getUrlImageList()) {
            urlImageDao.delete(image);
        }
        System.out.println("The image data of " + product.getName() + " deleted");
        productDao.delete(product);
        System.out.println("The image of " + product.getName() + " deleted");
    }

    private void saveProduct(Product product) {
        // I put a binary image data into the datastore first
        testKeyList = baseImageDataDao.saveBinary(testBlob);
        testBinaryImageData.setChunkFileKeys(testKeyList);
        baseImageDataDao.put(testBinaryImageData);
        // then I put a URL image data into the datastore
        urlImageDataDao.put(testUrlImageData);
        // then I put the image options into the datastore
        imageOptionsDao.put(testImageOptions);
        // then I put the binary image into the datastore
        binaryImageDao.put(testBinaryImage);
        // and I put the URL image also into the datastore
        urlImageDao.put(testUrlImage);
        // and I put the product into the datastore
        checkEntityExists(product);
        productDao.put(product);
    }

    @Test(expected = NotFoundException.class)
    public void testImageDAOPut() throws Exception {
        //given
        // DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have image data
        BinaryImageData imageData = new BinaryImageData(null, null);
        // And we have an image
        BinaryImage image = new BinaryImage("dummImageName",CategoryType.ACCESSORIES,"image/jpg",
                "image/jpg",
                "dummImageDescription",testImageOptions,imageData);

        //when
        IImage originalImage = new BinaryImage((BinaryImage) image);
        // I put the image data into the datastore first
        baseImageDataDao.put(imageData);
        // then I put the image into the datastore
        Key<BinaryImage> key = binaryImageDao.put(image);

        //then
        // I can retrieve and delete it
        BinaryImage imageFromDS = binaryImageDao.find(key);
        assert binaryImageList != null;
        assertNotSame(image.getId(), imageFromDS);
        BinaryImage deletedImage = binaryImageDao.delete(image);
        assertNull(deletedImage);
        imageFromDS = binaryImageDao.find(key);
        fail("The find method should have thrown NotFoundException!");
    }

    @Test
    public void testImageOptionsDAOPut() throws Exception {
        //given
        // DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have an image
        ImageOptions imageOptions = new ImageOptions(640, 480, "dummyImageName",true,true);

        //when
        // I put the image into the datastore
//        binaryImageDao.put(image);

        //then
        // I can retrieve and delete it
        assert binaryImageDao.list() != null;
//        binaryImageDao.delete(image);
    }

    @Test
    public void testConcurrentDAOPut() throws Exception {
        //given
        // DAO is instantiated
        JapanCuccokAdmin adminWebapp = new JapanCuccokAdmin();
        // And we have two products
        Product product1 = new Product("dummyProductName1", CategoryType.TSHIRT,
                "dummyProductDescrption1", 1000, binaryImageList, urlImageList);
        Product product2 = new Product("dummyProductName2", CategoryType.ACCESSORIES,
                "dummyProductDescrption2", 2000, binaryImageList, urlImageList);

        //when
        // I put the products into the datastore in the same time
        Thread daoThread1 = new Thread(new DAOPutThread(product1));
        Thread daoThread2 = new Thread(new DAOPutThread(product2));
        daoThread1.start();
        daoThread2.start();
        startGate.countDown();
        endGate.await();

        //then
        // I can retrieve and delete them
        assert productDao.list() != null;
        productDao.delete(product1);
        productDao.delete(product2);
    }

    private static void concurrentPut(Product product) {
        System.out.println("Thread " + Thread.currentThread().getId() + " starts a risky " +
                "operation");
        synchronized (lockObject) {
            System.out.println("Thread " + Thread.currentThread().getId() + " acquired the " +
                    "monitor");
            while (true) {
                try {
                    startGate.await();
                    try {
                        baseImageDataDao.put(testBinaryImageData);
                        imageOptionsDao.put(testImageOptions);
                        binaryImageDao.put(testBinaryImage);
                        productDao.put(product);
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
        }
        System.out.println("Thread " + Thread.currentThread().getId() + " released the monitor");
        System.out.println("Thread " + Thread.currentThread().getId() + " ending");

    }

    private static class DAOPutThread implements Runnable {

        private final Product product;

        private DAOPutThread(Product product) {
            this.product = product;
        }

        @Override
        public void run() {
            try {
                testHelper.setUp();
                startGate.await();
                JapanCuccokAdminTest.concurrentPut(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check whether the file already exists, and if so, try to delete it.
     *
     * @param entity the data store entity to check (e.g. product, image, etc...)
     */
    protected <T> void checkEntityExists(T entity) {
        GenericGaeDAO<T> dao = (GenericGaeDAO<T>) GenericGaeDAOFactory.getInstance(entity.getClass());
        // Try to delete the file
        try {
            dao.delete(entity);
        } catch (IllegalArgumentException e) {
            // Delete was unsuccessful, since the entity is missing
        }
    }

}
