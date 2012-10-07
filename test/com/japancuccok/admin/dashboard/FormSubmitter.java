package com.japancuccok.admin.dashboard;

import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import java.net.URISyntaxException;

public abstract class FormSubmitter {

    public abstract void setFile(FormTester formTester) throws URISyntaxException;

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

}