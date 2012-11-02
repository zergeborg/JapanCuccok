package com.japancuccok.admin.dashboard;

import com.google.appengine.api.datastore.*;
import com.japancuccok.admin.JapanCuccokAdmin;
import com.japancuccok.base.TestServiceProvider;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.domain.product.Product;
import com.japancuccok.db.IBinaryProvider;
import org.apache.wicket.Page;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.*;
import static com.japancuccok.db.DAOService.*;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.09.
 * Time: 19:01
 */
public class ProductUploadFormTest {

    protected final String NAME_ERROR_MSG = "A(z) 'Név' mező kitöltése kötelező.";
    protected final String CATEGORY_ERROR_MSG = "A(z) 'Kategória' mező kitöltése kötelező.";
    protected final String PRICE_ERROR_MSG = "A(z) 'Ár' mező kitöltése kötelező.";
    protected final String FILE_MISSING_ERROR_MSG =
            "Nem adtál meg URL-t és képet sem töltöttél fel";
    protected final String IMAGE_DATA_SAVED = "Saved image data: ";
    protected final String IMAGE_SAVED = "Saved image: %s";
    protected final String IMAGE_URL_SAVED = "Saved image: http://google.hu";
    protected final String PRODUCT_SAVED = "Saved product: Test Name";

    protected DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();
    protected List<Entity> entities;
    protected WicketTester tester;

    protected class ProductData {

        protected String PRODUCT_NAME = "Test Name";
        protected String PRODUCT_DESCRIPTION = "Test Description";
        protected Integer PRODUCT_CATEGORY = 1;
        protected String PRODUCT_PRICE = "1200";
    }

    protected class TestProduct extends Product {

        private static final long serialVersionUID = -6390819295763761722L;

        public TestProduct(String name, CategoryType categoryType, String description, int price,
                           List<BinaryImage> binaryImageList, List<UrlImage> urlImageList) {
            super(name, categoryType, description, price, binaryImageList, urlImageList);
        }

        @Override
        protected void setId(Long id) {
            super.setId(id);
        }
    }

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
        tester = openDashboard();
    }

    @After
    public void tearDown() throws Exception {
        deleteBaseImageData();
        deleteUrlImageData();
        deleteTheRest();
        TestServiceProvider.end();
    }

    @Test
    public void testOnSubmit_Init() throws Exception {
        //given the user opens the page in setUp

        //when no action happens
        FormTester formTester = tester.newFormTester("newProductForm");

        //then no error messages appear
        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Test
    public void testOnSubmit_oneBinaryOneUrl() throws Exception {
        String imageFileName = "motos_suzuki_022.jpg";
        String[] fileNames = new String[]{imageFileName};

        //given the user opens the page in setUp

        // when clicks the "New image!" button twice
        showNewImagePanel();
        showNewImagePanel();
        // and clicks the Datastore radio button once
        clickOnDatastoreRadioBtn(1);

        final FormSubmitter formSubmitter = new FormSubmitter(fileNames) {
            @Override
            public void setUrl(FormTester formTester) {
                // and he provides a URL
                formTester.setValue("dummyRepeaterContainer:uploadRepeatingView:2" +
                                            ":productUploadPanel" + ":newImageLink:newImageUrl",
                                    "http://google.hu");
            }
        };
        // and submits a filled-in form with valid data
        formSubmitter.submit(tester, new ProductData());

        printFeedback(tester);

        //then
        // then info messages appear about successful saving
        tester.assertInfoMessages(IMAGE_DATA_SAVED + "1", String.format(IMAGE_SAVED,
                                                                        imageFileName),
                                  IMAGE_DATA_SAVED + "4", IMAGE_URL_SAVED, PRODUCT_SAVED);
        // and we will have a new productList
        assertTrue(tester.getTagsByWicketId("productList").size() == 1);
    }

    @Test
    public void testOnSubmit_threeBinaries() throws Exception {
        String imageFileName1 =
                "Japan_Nature_1920x1440_HD_Wallpapers_Pack_2-16" +
                        ".jpg_Japanese_Garden_Royal_Roads_University_British_Columbia.png";
        String imageFileName2 =
                "japanese_maple_wallpaper_plants_nature_wallpaper_1280_960_1174.png";
        String imageFileName3 = "japan-nature-wallpaper1.png";
        String[] fileNames = new String[]{imageFileName1, imageFileName2, imageFileName3};

        //given the user opens the page in setUp

        // when clicks the "New image!" button three times
        showNewImagePanel();
        showNewImagePanel();
        showNewImagePanel();
        // and clicks the Datastore radio button three times
        clickOnDatastoreRadioBtn(1);
        clickOnDatastoreRadioBtn(2);
        clickOnDatastoreRadioBtn(3);
        final FormSubmitter formSubmitter = new FormSubmitter(fileNames) {
            @Override
            public void setUrl(FormTester formTester) {
                // intentionally left blank
            }
        };
        // and submits a filled-in form with valid data
        formSubmitter.submit(tester, new ProductData());

        printFeedback(tester);

        // then info messages appear about successful saving
        tester.assertInfoMessages(IMAGE_DATA_SAVED + "1", String.format(IMAGE_SAVED,
                                                                        imageFileName1),
                                  IMAGE_DATA_SAVED + "4", String.format(IMAGE_SAVED,
                                                                        imageFileName2),
                                  IMAGE_DATA_SAVED + "7", String.format(IMAGE_SAVED,
                                                                        imageFileName3),
                                  PRODUCT_SAVED);
        // and we will have a new productList
        assertTrue(tester.getTagsByWicketId("productList").size() == 1);
    }

    @Test
    public void testOnSubmit_EmptyForm() throws Exception {
        //given the user opens the page in setUp

        //when submits the empty form
        FormTester formTester = tester.newFormTester("newProductForm");
        formTester.submit("newProductUpload");

        //then error messages appear
        tester.assertErrorMessages(NAME_ERROR_MSG, CATEGORY_ERROR_MSG, PRICE_ERROR_MSG,
                                   FILE_MISSING_ERROR_MSG);
    }

    @Test
    public void testOnSubmit_AllDataValid() throws Exception {
        String imageFileName = "motos_suzuki_022.jpg";
        String[] fileNames = new String[]{imageFileName};

        //given the user opens the page in setUp

        // when clicks the "New image!" button
        showNewImagePanel();
        // and clicks the Datastore radio button
        clickOnDatastoreRadioBtn(1);
        // and submits a filled-in form with valid data
        // tester.dumpPage();
        final FormSubmitter formSubmitter = new FormSubmitter(fileNames) {
            @Override
            public void setUrl(FormTester formTester) {
                // intentionally left blank
            }
        };
        formSubmitter.submit(tester, new ProductData());

        printFeedback(tester);
        // then info messages appear about successful saving
        tester.assertInfoMessages(IMAGE_DATA_SAVED + "1", String.format(IMAGE_SAVED,
                                                                        imageFileName),
                                  PRODUCT_SAVED);
        // and we will have a new productList
        assertTrue(tester.getTagsByWicketId("productList").size() == 1);
    }

    @Test
    public void testTwoConsecutiveUploads() throws Exception {
        String imageFileName = "motos_suzuki_022.jpg";
        String[] fileNames = new String[]{imageFileName};
        TestProduct product1 = null;
        TestProduct product2 = null;
        ProductData productData = new ProductData();
        List<Product> productList = new ArrayList<Product>();
        List<UrlImage> dummyUrlImageList = new ArrayList<UrlImage>();
        List<BinaryImage> dummyBinaryImageList = new ArrayList<BinaryImage>();

        dummyUrlImageList.add(new UrlImage());
        dummyBinaryImageList.add(new BinaryImage());

        product1 = new TestProduct("Test Name", CategoryType.STUFF, "Test Description", 1200,
                                   dummyBinaryImageList, dummyUrlImageList);

        product2 = new TestProduct("Test Name 2", CategoryType.ACCESSORIES, "Test Description 2",
                                   1200, dummyBinaryImageList, dummyUrlImageList);

        product1.setId(new Long(4));
        product2.setId(new Long(8));
        productList.add(product1);
        productList.add(product2);

        // given the user opens the page in setUp

        // when clicks the "New image!" button
        showNewImagePanel();

        // and clicks the Datastore radio button
        clickOnDatastoreRadioBtn(1);

        // and submits a filled-in form with valid data twice
        final FormSubmitter formSubmitter = new FormSubmitter(fileNames) {
            @Override
            public void setUrl(FormTester formTester) {
                // intentionally left blank
            }
        };
        formSubmitter.submit(tester, productData);
        tester.assertInfoMessages(IMAGE_DATA_SAVED + "1", String.format(IMAGE_SAVED,
                                                                        imageFileName),
                                  PRODUCT_SAVED);

        printFeedback(tester);

        productData.PRODUCT_NAME = "Test Name 2";
        productData.PRODUCT_DESCRIPTION = "Test Description 2";
        productData.PRODUCT_CATEGORY = 2;
        formSubmitter.submit(tester, productData);

        printFeedback(tester);

        // then we get two productLists
        assertTrue(tester.getTagsByWicketId("productList").size() == 2);
        tester.assertListView("uploadedProductForm:uploadedProductContainer:productList",productList);
    }

    @Test
    public void testDelete() throws Exception {
        String filename1 = "motos_suzuki_022.jpg";
        String filename2 = "motos_suzuki_028.jpg";
        String filename3 = "motos_suzuki_032.jpg";
        String filename4 = "motos_suzuki_039.jpg";
        String filename5 = "motos_suzuki_041.jpg";
        String[] fileNames = new String[]{filename1, filename2, filename3, filename4, filename5};
        final FormSubmitter formSubmitter = new FormSubmitter(fileNames) {
            @Override
            public void setUrl(FormTester formTester) {
                // intentionally left blank
            }
        };

        // given the user opens the page
        tester.getSession().invalidateNow();
        Page dashboardPage = tester.startPage(Dashboard.class);

        // which is initially empty
        assertTrue(tester.getTagsByWicketId("productList").size() == 0);
        assertDelete();

        // when he clicks the "New image!" button 5 times
        // and clicks the Datastore radio button  5 times
        for (int i = 0; i < 5; i++) {
            showNewImagePanel();
            clickOnDatastoreRadioBtn(i + 1);
        }

        // and he uploads a product with 5 images
        ProductData productData = new ProductData();
        formSubmitter.submit(tester, productData);

        printFeedback(tester);

        //when
        // he clicks on the delete icon
        tester.clickLink("uploadedProductForm:uploadedProductContainer:productList:0:delete");

        printFeedback(tester);

        //then
        assertDelete();

    }

    private WicketTester openDashboard() {
        //given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        tester.startPage(Dashboard.class);
        return tester;
    }

    private void clickOnDatastoreRadioBtn(int index) {
        tester.executeAjaxEvent("newProductForm:dummyRepeaterContainer:uploadRepeatingView:" +
                                        index +
                                        ":productUploadPanel:selector:2", "onchange");
        tester.assertComponentOnAjaxResponse
                       ("newProductForm:dummyRepeaterContainer:uploadRepeatingView:" + index +
                                ":productUploadPanel:newImageUpload");
    }

    private void deleteUrlImageData() {
        for (IImageData imageData : urlImageDataDao.list()) {
            System.out.println("Cleaning up " + imageData.toString());
            urlImageDataDao.delete((UrlImageData) imageData);
        }
    }

    private PreparedQuery load(String kind) throws FileNotFoundException {
        Query query = new Query(kind);

        query.addSort("index");
        return dsService.prepare(query);
    }

    private void showNewImagePanel() {
        tester.executeAjaxEvent("newProductForm:newImageDiv", "onclick");
        tester.assertComponentOnAjaxResponse("newProductForm:dummyRepeaterContainer");
    }

    private void deleteBaseImageData() {
        for (IImageData imageData : baseImageDataDao.list()) {
            System.out.println("Cleaning up " + imageData.toString());
            if (imageData instanceof IBinaryProvider) {
                IBinaryProvider binaryImageData = (IBinaryProvider) imageData;
                baseImageDataDao.deleteBinary(binaryImageData);
            }
            else {
                baseImageDataDao.delete((BaseImageData) imageData);
            }
        }
    }

    private void deleteTheRest() {
        System.out.println("Cleaning up image options");
        imageOptionsDao.deleteAll(imageOptionsDao.list());
        System.out.println("Cleaning up binary images");
        binaryImageDao.deleteAll(binaryImageDao.list());
        System.out.println("Cleaning up URL images");
        urlImageDao.deleteAll(urlImageDao.list());
        System.out.println("Cleaning up products");
        productDao.deleteAll(productDao.list());
    }

    private void assertDelete() throws FileNotFoundException {
        // The image data chunks are deleted first
        PreparedQuery pQuery = load("ChunkFile");
        FetchOptions fetchOptions = withLimit(1);

        entities = pQuery.asList(fetchOptions);
        assertEquals(0, entities.size());
        // Then the image data items are deleted also
        assertEquals(0, baseImageDataDao.list().size());
        // Together with image options
        assertEquals(0, imageOptionsDao.list().size());
        // Then the images are deleted also
        assertEquals(0, binaryImageDao.list().size());
        // And finally the product is deleted as well
        assertEquals(0, productDao.list().size());
    }

    private void printFeedback(WicketTester tester) {
        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));
    }
}
