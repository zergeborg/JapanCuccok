package com.japancuccok.admin.dashboard;

import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.30.
 * Time: 20:21
 */
public class UploadedProductListViewTest extends ProductUploadFormTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        testOnSubmit_threeBinaries();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testFirstImageIconShowsUp() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the first image
        clickOnImageSelector(0);

        //then the camera icon shows up
    }

    @Test
    public void testSecondImageIconShowsUp() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the second image
        clickOnImageSelector(1);

        //then the camera icon shows up
    }

    @Test
    public void testThirdImageIconShowsUp() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the third image
        clickOnImageSelector(2);

        //then the camera icon shows up
    }

    @Test
    public void testFirstImageDisplaysProperly() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the first image
        clickOnImageSelector(0);
        //and clicks the camera icon
        clickIcon();

        //then the image displays properly
    }

    @Test
    public void testSecondImageDisplaysProperly() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the first image
        clickOnImageSelector(1);
        //and clicks the camera icon
        clickIcon();

        //then the image displays properly
    }

    @Test
    public void testThirdImageDisplaysProperly() throws Exception {
        //given the user successfully uploaded three images

        //when the user clicks the image selector dropdown
        //and selects the first image
        clickOnImageSelector(2);
        //and clicks the camera icon
        clickIcon();

        //then the image displays properly
    }

    private void clickOnImageSelector(int index) {
        FormTester formTester = tester.newFormTester("uploadedProductForm");
        formTester.select("uploadedProductContainer:"+
                                  "productList:" +
                                  0 +
                                  ":imageList", index);
        tester.executeAjaxEvent("uploadedProductForm:" +
                                        "uploadedProductContainer:" +
                                        "productList:" +
                                        0 +
                                        ":imageList", "onchange");
        tester.assertComponentOnAjaxResponse("uploadedProductForm:"+
                                                     "uploadedProductContainer:"+
                                                     "productList:" +
                                                     0 +
                                                     ":showLink");
    }

    private void clickIcon() {
        tester.clickLink("uploadedProductForm:"+
                                 "uploadedProductContainer:"+
                                 "productList:" +
                                 0 +
                                 ":showLink");
    }

}
