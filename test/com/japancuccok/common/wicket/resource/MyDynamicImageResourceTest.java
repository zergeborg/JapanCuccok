package com.japancuccok.common.wicket.resource;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.util.string.StringValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.02.01.
 * Time: 2:56
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(StringValue.class)
public class MyDynamicImageResourceTest {

    /**
     * This is the case when a robot hits the page and "imageFileNameWithExtension" cannot be
     * parsed from the named parameters in the URL. Note that NFE is ugly, but it should be
     * prevented instead of catching it. Look at
     * {@link com.japancuccok.main.JapanCuccok#mountResources}
     * and {@link com.japancuccok.main.JapanCuccok#newWebResponse} how the images are mounted and
     * JSESSIONID appended URLS are prevented
     * @throws Exception
     */
    @Test(expected=NumberFormatException.class)
    public void testGetImageDataFromScantyParameters() throws Exception {
        //given There is a request for an image resource, an the resource was successfully created
        MyDynamicImageResource imageResource = new MyDynamicImageResource();

        //when I call the getImageData() method with scanty parameters
        IResource.Attributes attributes = mock(IResource.Attributes.class);
        PageParameters parameters = mock(PageParameters.class);
        StringValue stringValue = mock(StringValue.class);
        when(attributes.getParameters()).thenReturn(parameters);
        when(parameters.get("imageFileNameWithExtension")).thenReturn(stringValue);
        when(stringValue.toString()).thenReturn(".png");
        imageResource.getImageData(attributes);

        //then The method throws NumberFormatException
    }
}
