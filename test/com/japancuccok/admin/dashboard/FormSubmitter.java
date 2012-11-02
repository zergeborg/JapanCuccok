package com.japancuccok.admin.dashboard;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import java.io.File;
import java.net.URISyntaxException;

public abstract class FormSubmitter {

    protected String[] fileNames;

    protected FormSubmitter(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public void setFile(FormTester formTester) throws URISyntaxException {
        int i = 1;
        for(String fileName : fileNames) {
            formTester.setFile("dummyRepeaterContainer:uploadRepeatingView:" + i +
                    ":productUploadPanel:newImageUpload:uploads",
                    getFile(fileName),
                    "multipart/form-data");
            i++;
        }
    }

    public abstract void setUrl(FormTester formTester);

    public void submit(WicketTester tester, ProductUploadFormTest.ProductData productData) throws URISyntaxException {
        FormTester formTester = tester.newFormTester("newProductForm");
        formTester.setValue("newProductName", productData.PRODUCT_NAME);
        formTester.setValue("newProductDescription", productData.PRODUCT_DESCRIPTION);
        formTester.select("newProductCategory", productData.PRODUCT_CATEGORY);
        formTester.setValue("newProductPrice", productData.PRODUCT_PRICE);
        setFile(formTester);
        setUrl(formTester);
        formTester.submit("newProductUpload");
    }

    private org.apache.wicket.util.file.File getFile(String fileName) throws URISyntaxException {
        File imageFile = new File(this.getClass().getResource(fileName).toURI());
        return new org.apache.wicket.util.file.File(imageFile);
    }
}