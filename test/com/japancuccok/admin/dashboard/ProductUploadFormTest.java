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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
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
    protected final String FILE_MISSING_ERROR_MSG = "Nem adtál meg URL-t és képet sem töltöttél fel";
    protected final String IMAGE_DATA_SAVED = "Saved image data: ";
    protected final String IMAGE_SAVED = "Saved image: motos_suzuki_022.jpg";
    protected final String IMAGE_URL_SAVED = "Saved image: http://google.hu";
    protected final String PRODUCT_SAVED = "Saved product: Test Name";

    private DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();
    private List<Entity> entities;

    protected class ProductData {

        protected String  PRODUCT_NAME = "Test Name";
        protected String  PRODUCT_DESCRIPTION = "Test Description";
        protected Integer PRODUCT_CATEGORY = 1;
        protected String  PRODUCT_PRICE = "1200";
    }

    protected class TestProduct extends Product {
        private static final long serialVersionUID = -6390819295763761722L;

        public TestProduct(String name, CategoryType categoryType, String description, int price,
                           List<BinaryImage> binaryImageList, List<UrlImage> urlImageList) {
            super(name,categoryType,description,price,binaryImageList, urlImageList);
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
    }

    @After
    public void tearDown() throws Exception {
        for(IImageData imageData : baseImageDataDao.list()) {
            System.out.println("Cleaning up " + imageData.toString());
            if(imageData instanceof IBinaryProvider) {
                IBinaryProvider binaryImageData = (IBinaryProvider) imageData;
                baseImageDataDao.deleteBinary(binaryImageData);
            } else {
                baseImageDataDao.delete((BaseImageData) imageData);
            }
        }
        for(IImageData imageData : urlImageDataDao.list()) {
            System.out.println("Cleaning up " + imageData.toString());
            urlImageDataDao.delete((UrlImageData) imageData);
        }
        System.out.println("Cleaning up image options");
        imageOptionsDao.deleteAll(imageOptionsDao.list());
        System.out.println("Cleaning up binary images");
        binaryImageDao.deleteAll(binaryImageDao.list());
        System.out.println("Cleaning up URL images");
        urlImageDao.deleteAll(urlImageDao.list());
        System.out.println("Cleaning up products");
        productDao.deleteAll(productDao.list());
        TestServiceProvider.end();
    }

    @Test
    public void testOnSubmit_Init() throws Exception {
        //given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        tester.startPage(Dashboard.class);

        //when no action happens
        FormTester formTester = tester.newFormTester("newProductForm");

        //then no error messages appear
        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Test
    public void testOnSubmit_oneBinaryOneUrl() throws Exception {
        // given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        tester.startPage(Dashboard.class);

        // when clicks the "New image!" button twice
        showNewImagePanel(tester);
        showNewImagePanel(tester);
        // and clicks the Datastore radio button once
        clickOnDatastoreRadioBtn(tester, 1);
        final FormSubmitter formSubmitter = new FormSubmitter() {
            @Override
            public void setFile(FormTester formTester) throws URISyntaxException {
                // and he uploads a binary file
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 1 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_022.jpg"),
                        "multipart/form-data");
            }
            @Override
            public void setUrl(FormTester formTester) {
                // and he provides a URL
                formTester.setValue(
                        "dummyRepeaterContainer:uploadRepeatingView:2:productUploadPanel" +
                                ":newImageLink:newImageUrl",
                        "http://google.hu");
            }
        };
        // and submits a filled-in form with valid data
        formSubmitter.submit(tester, new ProductData());

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));

        //then
        // then info messages appear about successful saving
        tester.assertInfoMessages(IMAGE_DATA_SAVED+"1", IMAGE_SAVED, IMAGE_DATA_SAVED+"4", IMAGE_URL_SAVED, PRODUCT_SAVED);
        // and we will have a new productList
        assertTrue(tester.getTagsByWicketId("productList").size() == 1);
    }

    @Test
    public void testOnSubmit_EmptyForm() throws Exception {
        //given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        tester.startPage(Dashboard.class);

        //when submits the empty form
        FormTester formTester = tester.newFormTester("newProductForm");
        formTester.submit("newProductUpload");

        //then error messages appear
        tester.assertErrorMessages(NAME_ERROR_MSG, CATEGORY_ERROR_MSG, PRICE_ERROR_MSG,
                FILE_MISSING_ERROR_MSG);
    }

    @Test
    public void testOnSubmit_AllDataValid() throws Exception {
        // given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        Page dashboardPage = tester.startPage(Dashboard.class);

        // when clicks the "New image!" button
        showNewImagePanel(tester);
        // and clicks the Datastore radio button
        clickOnDatastoreRadioBtn(tester, 1);
        // and submits a filled-in form with valid data
        // tester.dumpPage();
        final FormSubmitter formSubmitter = new FormSubmitter() {
            @Override
            public void setFile(FormTester formTester) throws URISyntaxException {
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 1 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_022.jpg"),
                        "multipart/form-data");
            }

            @Override
            public void setUrl(FormTester formTester) {
            }
        };
        formSubmitter.submit(tester, new ProductData());

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));
        // then info messages appear about successful saving
        tester.assertInfoMessages(IMAGE_DATA_SAVED+"1", IMAGE_SAVED, PRODUCT_SAVED);
        // and we will have a new productList
        assertTrue(tester.getTagsByWicketId("productList").size() == 1);
    }

    private void clickOnDatastoreRadioBtn(WicketTester tester, int index) {
        tester.executeAjaxEvent("newProductForm:dummyRepeaterContainer:uploadRepeatingView:" + index +
                ":productUploadPanel:selector:2", "onchange");
        tester.assertComponentOnAjaxResponse("newProductForm:dummyRepeaterContainer:uploadRepeatingView:"
                + index + ":productUploadPanel:newImageUpload");
    }

    @Test
    public void testTwoConsecutiveUploads() throws Exception {
        List<BinaryImage> dummyBinaryImageList = new ArrayList<BinaryImage>();
        dummyBinaryImageList.add(new BinaryImage());
        List<UrlImage> dummyUrlImageList = new ArrayList<UrlImage>();
        dummyUrlImageList.add(new UrlImage());
        TestProduct product1 = new TestProduct("Test Name",CategoryType.STUFF,"Test Description",
                1200,dummyBinaryImageList,dummyUrlImageList);
        product1.setId(new Long(4));
        TestProduct product2 = new TestProduct("Test Name 2",CategoryType.ACCESSORIES,
                "Test Description 2",1200,dummyBinaryImageList,dummyUrlImageList);
        product2.setId(new Long(8));
        List<Product> productList = new ArrayList<Product>();
        productList.add(product1);
        productList.add(product2);
        // given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        Page dashboardPage = tester.startPage(Dashboard.class);

        // when clicks the "New image!" button
        showNewImagePanel(tester);
        // and clicks the Datastore radio button
        clickOnDatastoreRadioBtn(tester, 1);
        // and submits a filled-in form with valid data twice
        ProductData productData = new ProductData();
        final FormSubmitter formSubmitter = new FormSubmitter() {
            @Override
            public void setFile(FormTester formTester) throws URISyntaxException {
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 1 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_022.jpg"),
                        "multipart/form-data");
            }

            @Override
            public void setUrl(FormTester formTester) {
            }
        };
        formSubmitter.submit(tester, productData);
        tester.assertInfoMessages(IMAGE_DATA_SAVED+"1", IMAGE_SAVED, PRODUCT_SAVED);

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));

        productData.PRODUCT_NAME = "Test Name 2";
        productData.PRODUCT_DESCRIPTION = "Test Description 2";
        productData.PRODUCT_CATEGORY = 2;
        formSubmitter.submit(tester, productData);

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));

        // then we get two productLists
        assertTrue(tester.getTagsByWicketId("productList").size() == 2);
        tester.assertListView("productList", productList);
    }

    @Test
    public void testDelete() throws Exception {
        // given the user opens the page
        WicketTester tester = new WicketTester(new JapanCuccokAdmin());
        tester.getSession().invalidateNow();
        Page dashboardPage = tester.startPage(Dashboard.class);
        // which is initially empty
        assertTrue(tester.getTagsByWicketId("productList").size() == 0);
        assertDelete();
        // when he clicks the "New image!" button 5 times
        // and clicks the Datastore radio button  5 times
        for(int i = 0; i < 5; i++) {
            showNewImagePanel(tester);
            clickOnDatastoreRadioBtn(tester, i+1);
        }
        // and he uploads a product with 5 images
        final FormSubmitter formSubmitter = new FormSubmitter() {
            @Override
            public void setFile(FormTester formTester) throws URISyntaxException {
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 1 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_022.jpg"),
                        "multipart/form-data");
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 2 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_028.jpg"),
                        "multipart/form-data");
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 3 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_032.jpg"),
                        "multipart/form-data");
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 4 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_039.jpg"),
                        "multipart/form-data");
                formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + 5 +
                        ":productUploadPanel:newImageUpload:uploads",
                        getFile("motos_suzuki_041.jpg"),
                        "multipart/form-data");
            }

            @Override
            public void setUrl(FormTester formTester) {
            }
        };
        ProductData productData = new ProductData();
        formSubmitter.submit(tester, productData);

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));

        //when
        // he clicks on the delete icon
        tester.clickLink("productList:0:delete");

        System.out.println(tester.getMessages(FeedbackMessage.SUCCESS));
        System.out.println(tester.getMessages(FeedbackMessage.INFO));
        System.out.println(tester.getMessages(FeedbackMessage.ERROR));

        //then
        assertDelete();

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

    private PreparedQuery load(String kind) throws FileNotFoundException {
        Query query = new Query(kind);
        query.addSort("index");
        return dsService.prepare(query);
    }

    private void showNewImagePanel(WicketTester tester) {
        tester.executeAjaxEvent("newProductForm:newImageDiv", "onclick");
        tester.assertComponentOnAjaxResponse("newProductForm:dummyRepeaterContainer");
    }

    private org.apache.wicket.util.file.File getFile(String fileName) throws URISyntaxException {
        File imageFile = new File(this.getClass().getResource(fileName).toURI());
        return new org.apache.wicket.util.file.File(imageFile);
    }
}
