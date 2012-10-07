package com.japancuccok.common.infrastructure.gae;

import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileItemFactory;
import org.apache.wicket.util.upload.FileUploadException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author uudashr
 * 
 */
public class GaeSafeServletWebRequest extends ServletWebRequest {

    public GaeSafeServletWebRequest(HttpServletRequest httpServletRequest, String filterPrefix) {
        super(httpServletRequest, filterPrefix);
        FileCleaner.reapFiles(2);
    }

    @Override
    public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, String upload,
                                                             FileItemFactory factory) throws FileUploadException
    {
        return new GaeSafeMultipartServletWebRequest(getContainerRequest(), getFilterPrefix(),
                maxSize, factory, upload);
    }

    @Override
    public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, String upload)
            throws FileUploadException
    {
        return new GaeSafeMultipartServletWebRequest(getContainerRequest(), getFilterPrefix(), maxSize,
                upload);
    }
}
