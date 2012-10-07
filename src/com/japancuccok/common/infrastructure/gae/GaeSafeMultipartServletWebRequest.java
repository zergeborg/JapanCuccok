package com.japancuccok.common.infrastructure.gae;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.IMultipartWebRequest;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaeSafeMultipartServletWebRequest extends MultipartServletWebRequest implements IMultipartWebRequest {
    /** Map of file items. */
    private final Map<String, List<FileItem>> files = new HashMap<String, List<FileItem>>();

    /** Map of parameters. */
    private final Map<String, String[]> parameters = new HashMap<String, String[]>();

    /** The posted (either POST or GET) parameters stored as a map */
    private final IRequestParameters requestParameters;

    /**
     * total bytes uploaded (downloaded from server's pov) so far. used for upload notifications
     */
    private int bytesUploaded;

    /** content length cache, used for upload notifications */
    private int totalBytes;

    /** upload identifier */
    private String upload;


    /**
     * Constructor
     *
     * @param maxSize
     *            the maximum size allowed for this request
     * @param request
     *            the servlet request
     * @param filterPrefix
     *            contentPath + filterPath, used to extract the actual {@link Url}
     * @param upload
     *            upload identifier for {@link org.apache.wicket.protocol.http.servlet.UploadInfo}
     */
    public GaeSafeMultipartServletWebRequest(HttpServletRequest request, String filterPrefix, 
                                             Bytes maxSize, String upload) throws
            FileUploadException {
        this(request, filterPrefix, maxSize, new GaeFileItemFactory(GaeFileItemFactory
                .DEFAULT_SIZE_THRESHOLD), upload);
    }
    
    /**
     * Constructor
     * 
     * @param maxSize
     *            the maximum size allowed for this request
     * @param request
     *            the servlet request
     * @param filterPrefix
     *            contentPath + filterPath, used to extract the actual {@link Url}
     * @param factory
     *            the file item factory that should be used
     * @param upload
     *            upload identifier for {@link org.apache.wicket.protocol.http.servlet.UploadInfo}
     */
    public GaeSafeMultipartServletWebRequest(HttpServletRequest request, String filterPrefix, 
                                             Bytes maxSize, FileItemFactory factory,
                                             String upload) throws FileUploadException {
        super(request, filterPrefix);

        Args.notNull(maxSize, "maxSize");
        Args.notNull(upload, "upload");
        this.upload = upload;

        if (maxSize == null)
        {
            throw new IllegalArgumentException("argument maxSize must be not null");
        }

        // Check that request is multipart
        final boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
        {
            throw new IllegalStateException("ServletRequest does not contain multipart content");
        }

        // Configure the factory here, if desired.
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        // The encoding that will be used to decode the string parameters
        // It should NOT be null at this point, but it may be
        // if the older Servlet API 2.2 is used
        String encoding = request.getCharacterEncoding();

        // set encoding specifically when we found it
        if (encoding != null)
        {
            fileUpload.setHeaderEncoding(encoding);
        }

        fileUpload.setFileSizeMax(maxSize.bytes());
        fileUpload.setSizeMax(maxSize.bytes());

        final List<FileItem> items;

        if (wantUploadProgressUpdates())
        {
            ServletRequestContext ctx = new ServletRequestContext(request)
            {
                public InputStream getInputStream() throws IOException
                {
                    return new CountingInputStream(super.getInputStream());
                }
            };
            totalBytes = request.getContentLength();

            onUploadStarted(totalBytes);
            items = fileUpload.parseRequest(ctx);
            onUploadCompleted();

        }
        else
        {
            items = fileUpload.parseRequest(request);
        }

        // Loop through items
        for (FileItem item : items)
        {
            // If item is a form field
            if (item.isFormField())
            {
                // Set parameter value
                final String value;
                if (encoding != null)
                {
                    try
                    {
                        value = item.getString(encoding);
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        throw new WicketRuntimeException(e);
                    }
                }
                else
                {
                    value = item.getString();
                }

                addParameter(item.getFieldName(), value);
            }
            else
            {
                List<FileItem> fileItems = files.get(item.getFieldName());
                if (fileItems == null)
                {
                    fileItems = new ArrayList<FileItem>();
                    files.put(item.getFieldName(), fileItems);
                }
                // Add to file list
                fileItems.add(item);
            }
        }
        requestParameters = new GaeMultipartPostParameters(getParameterMap());
    }

    @Override
    public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, String upload)
            throws FileUploadException
    {
        return this;
    }

    @Override
    public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, String upload,
                                                             FileItemFactory factory) throws FileUploadException
    {
        return this;
    }

    /**
     * Adds a parameter to the parameters value map
     * 
     * @param name
     *            parameter name
     * @param value
     *            parameter value
     */
    private void addParameter(final String name, final String value)
    {
        final String[] currVal = parameters.get(name);

        String[] newVal = null;

        if (currVal != null)
        {
            newVal = new String[currVal.length + 1];
            System.arraycopy(currVal, 0, newVal, 0, currVal.length);
            newVal[currVal.length] = value;
        }
        else
        {
            newVal = new String[] { value };

        }

        parameters.put(name, newVal);
    }

    /**
     * @return Returns the files.
     */
    public Map<String, List<FileItem>> getFiles()
    {
        return files;
    }

    /**
     * Gets the file that was uploaded using the given field name.
     * 
     * @param fieldName
     *            the field name that was used for the upload
     * @return the upload with the given field name
     */
    public List<FileItem> getFile(final String fieldName)
    {
        return files.get(fieldName);
    }

    /**
     * @see org.apache.wicket.request.Request#getRequestParameters()
     */
    public String getParameter(final String key)
    {
        String[] val = parameters.get(key);
        return (val == null) ? null : val[0];
    }

    /**
     * @see org.apache.wicket.request.Request#getRequestParameters()
     */
    public Map<String, String[]> getParameterMap()
    {
        return parameters;
    }

    /**
     * @see org.apache.wicket.request.Request#getRequestParameters()
     */
    public String[] getParameters(final String key)
    {
        return parameters.get(key);
    }

    @Override
    public IRequestParameters getPostParameters()
    {
        return requestParameters;
    }

    /**
     * Subclasses that want to receive upload notifications should return true
     * 
     * @return true if upload status update event should be invoked
     */
    protected boolean wantUploadProgressUpdates()
    {
        return false;
    }

    /**
     * Upload start callback
     * 
     * @param totalBytes
     */
    protected void onUploadStarted(int totalBytes)
    {

    }

    /**
     * Upload status update callback
     * 
     * @param bytesUploaded
     * @param total
     */
    protected void onUploadUpdate(int bytesUploaded, int total)
    {

    }

    /**
     * Upload completed callback
     */
    protected void onUploadCompleted()
    {

    }

    /**
     * An {@link InputStream} that updates total number of bytes read
     * 
     * @author Igor Vaynberg (ivaynberg)
     */
    private class CountingInputStream extends InputStream
    {

        private final InputStream in;

        /**
         * Constructs a new CountingInputStream.
         * 
         * @param in
         *            InputStream to delegate to
         */
        public CountingInputStream(InputStream in)
        {
            this.in = in;
        }

        /**
         * @see java.io.InputStream#read()
         */
        public int read() throws IOException
        {
            int read = in.read();
            bytesUploaded += (read < 0) ? 0 : 1;
            onUploadUpdate(bytesUploaded, totalBytes);
            return read;
        }

        /**
         * @see java.io.InputStream#read(byte[])
         */
        public int read(byte[] b) throws IOException
        {
            int read = in.read(b);
            bytesUploaded += (read < 0) ? 0 : read;
            onUploadUpdate(bytesUploaded, totalBytes);
            return read;
        }

        /**
         * @see java.io.InputStream#read(byte[], int, int)
         */
        public int read(byte[] b, int off, int len) throws IOException
        {
            int read = in.read(b, off, len);
            bytesUploaded += (read < 0) ? 0 : read;
            onUploadUpdate(bytesUploaded, totalBytes);
            return read;
        }

    }

}
