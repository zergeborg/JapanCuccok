package com.japancuccok.common.wicket.upload;

import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.ParameterParser;

import java.io.*;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 11:50
 */
public class InMemoryFileItem implements FileItem {

    // ----------------------------------------------------- Manifest constants


    /**
     * Default content charset to be used when no explicit charset parameter is provided by the
     * sender. Media subtypes of the "text" type are defined to have a default charset value of
     * "ISO-8859-1" when received via HTTP.
     */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";


    // ----------------------------------------------------------- Data members


    /**
     * Counter used in unique identifier generation.
     */
    private static int counter = 0;
    private static final long serialVersionUID = 4976086874569865367L;


    /**
     * The name of the form field as provided by the browser.
     */
    private String fieldName;


    /**
     * The content type passed by the browser, or <code>null</code> if not defined.
     */
    private final String contentType;


    /**
     * Whether or not this item is a simple form field.
     */
    private boolean isFormField;


    /**
     * The original filename in the user's filesystem.
     */
    private final String fileName;


    /**
     * The threshold above which uploads will be stored on disk.
     */
    private final int sizeThreshold;


    /**
     * Cached contents of the file.
     */
    private byte[] cachedContent;


    /**
     * Output stream for this item.
     */
    private ByteArrayOutputStream baos;


    // ----------------------------------------------------------- Constructors


    /**
     * Constructs a new <code>DiskFileItem</code> instance.
     *
     * @param fieldName     The name of the form field.
     * @param contentType   The content type passed by the browser or <code>null</code> if
     *                      not specified.
     * @param isFormField   Whether or not this item is a plain form field,
     *                      as opposed to a file upload.
     * @param fileName      The original filename in the user's filesystem,
     *                      or <code>null</code> if not
     *                      specified.
     * @param sizeThreshold The threshold, in bytes, below which items will be retained in
     *                      memory and above
     *                      which they will be stored directly to GAE datastore..
     */
    public InMemoryFileItem(String fieldName, String contentType, boolean isFormField, String fileName,
                       int sizeThreshold) {
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.isFormField = isFormField;
        this.fileName = fileName;
        this.sizeThreshold = sizeThreshold;
    }


    // ------------------------------- Methods from javax.activation.DataSource


    /**
     * Returns an {@link java.io.InputStream InputStream} that can be used to retrieve the
     * contents
     * of the file directly from GAE datastore.
     *
     * @return An {@link java.io.InputStream InputStream} that can be used to retrieve the contents
     *         of the file directly from GAE datastore.
     * @throws IOException if an error occurs.
     */
    public InputStream getInputStream() throws IOException {
        //We will always store the uploaded files in memory
		if (cachedContent == null)
		{
			cachedContent = baos.toByteArray();
		}
        return new ByteArrayInputStream(cachedContent);
    }


    /**
     * Returns the content type passed by the agent or <code>null</code> if not defined.
     *
     * @return The content type passed by the agent or <code>null</code> if not defined.
     */
    public String getContentType() {
        return contentType;
    }


    /**
     * Returns the content charset passed by the agent or <code>null</code> if not defined.
     *
     * @return The content charset passed by the agent or <code>null</code> if not defined.
     */
    public String getCharSet() {
        ParameterParser parser = new ParameterParser();
        parser.setLowerCaseNames(true);
        // Parameter parser can handle null input
        Map<String, String> params = parser.parse(getContentType(), ';');
        return (String) params.get("charset");
    }


    /**
     * Returns the original filename in the client's filesystem.
     *
     * @return The original filename in the client's filesystem.
     */
    public String getName() {
        return fileName;
    }


    // ------------------------------------------------------- FileItem methods


    /**
     * Provides a hint as to whether or not the file contents will be read from memory.
     *
     * @return <code>true</code> if the file contents will be read from memory; <code>false</code>
     *         otherwise.
     */
    public boolean isInMemory() {
        return true;
    }


    /**
     * Returns the size of the file.
     *
     * @return The size of the file, in bytes.
     */
    public long getSize() {
		if (cachedContent != null)
		{
            return cachedContent.length;
        }
		else
		{
            // TODO: ez most az aktuális buffer tartalmát adja vissza, ami nem feltétlen egyezik meg
            // TODO: a teljes memóriában tárolt fájl hosszával
            return baos.toByteArray().length;
		}
    }


    /**
     * Returns the contents of the file as an array of bytes. If the contents of the file were not
     * yet cached in memory, they will be loaded from GAE datastore and cached.
     *
     * @return The contents of the file as an array of bytes.
     */
    public byte[] get() {
        if (cachedContent == null)
        {
            cachedContent = baos.toByteArray();
        }
        return cachedContent;
    }


    /**
     * Returns the contents of the file as a String, using the specified encoding. This
     * method uses
     * {@link #get()} to retrieve the contents of the file.
     *
     * @param charset The charset to use.
     * @return The contents of the file, as a string.
     * @throws UnsupportedEncodingException if the requested character encoding is not available.
     */
    public String getString(final String charset) throws UnsupportedEncodingException {
        return new String(get(), charset);
    }


    /**
     * Returns the contents of the file as a String, using the default character encoding. This
     * method uses {@link #get()} to retrieve the contents of the file.
     *
     * @return The contents of the file, as a string.
     * @todo Consider making this method throw UnsupportedEncodingException.
     */
    public String getString() {
        byte[] rawdata = get();
        String charset = getCharSet();
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        try {
            return new String(rawdata, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(rawdata);
        }
    }


    /**
     * This method is dummy and doesn't do anything!
     *
     * @param file The <code>File</code> into which the uploaded item should be stored.
     * @throws Exception if an error occurs.
     */
    public void write(File file) throws java.io.IOException {
        // DONOTHING
    }


    /**
     * Deletes the underlying data for a file item, including deleting any stored data in GAE
     * datastore. Although this data will be deleted automatically when the <code>FileItem</code>
     * instance is garbage collected, this method can be used to ensure that this is done at an
     * earlier time, thus preserving system resources.
     */
    public void delete() {
        cachedContent = null;
    }


    /**
     * Returns the name of the field in the multipart form corresponding to this file item.
     *
     * @return The name of the form field.
     * @see #setFieldName(java.lang.String)
     */
    public String getFieldName() {
        return fieldName;
    }


    /**
     * Sets the field name used to reference this file item.
     *
     * @param fieldName The name of the form field.
     * @see #getFieldName()
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    /**
     * Determines whether or not a <code>FileItem</code> instance represents a simple form
     * field.
     *
     * @return <code>true</code> if the instance represents a simple form field;
     *         <code>false</code> if it represents an uploaded file.
     * @see #setFormField(boolean)
     */
    public boolean isFormField() {
        return isFormField;
    }


    /**
     * Specifies whether or not a <code>FileItem</code> instance represents a simple form
     * field.
     *
     * @param state <code>true</code> if the instance represents a simple form field;
     *              <code>false</code> if it represents an uploaded file.
     * @see #isFormField()
     */
    public void setFormField(boolean state) {
        isFormField = state;
    }


    /**
     * Returns an {@link java.io.OutputStream OutputStream} that can be used for storing the
     * contents of the file directly to GAE datastore.
     *
     * @return An {@link java.io.OutputStream OutputStream} that can be used for storing the
     *         contensts of the file directly to GAE datastore.
     * @throws IOException if an error occurs.
     */
    public OutputStream getOutputStream() throws IOException {
        if (baos == null) {
            String tempFileName = getTempFileName();
            // We will always store the uploaded stuff in memory
            baos = new ByteArrayOutputStream();
        }

        return baos;
    }


    // --------------------------------------------------------- Public methods


    // ------------------------------------------------------ Protected methods


    /**
     * Removes the file contents from the temporary storage.
     */
    protected void finalize() {
    }


    /**
     * Creates and returns a {@link java.io.File File} representing a uniquely named temporary file
     * in the configured repository path. The lifetime of the file is tied to the lifetime of the
     * <code>FileItem</code> instance; the file will be deleted when the instance is garbage
     * collected.
     *
     * @return The {@link java.io.File File} to be used for temporary storage.
     */
    protected String getTempFileName() {
        return "upload_" + getUniqueId() + ".tmp";
    }


    // -------------------------------------------------------- Private methods


    /**
     * Returns an identifier that is unique within the class loader used to load this class, but
     * does not have random-like apearance.
     *
     * @return A String with the non-random looking instance identifier.
     */
    private static String getUniqueId() {
        int current;
        synchronized (InMemoryFileItem.class) {
            current = counter++;
        }
        String id = Integer.toString(current);

        // If you manage to get more than 100 million of ids, you'll
        // start getting ids longer than 8 characters.
        if (current < 100000000) {
            id = ("00000000" + id).substring(id.length());
        }
        return id;
    }

    @Override
    public String toString() {
        return "InMemoryFileItem{" +
                "fieldName='" + fieldName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", isFormField=" + isFormField +
                ", fileName='" + fileName + '\'' +
                ", sizeThreshold=" + sizeThreshold +
                '}';
    }
}
