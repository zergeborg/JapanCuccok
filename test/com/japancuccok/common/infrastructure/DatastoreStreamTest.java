package com.japancuccok.common.infrastructure;

import com.googlecode.objectify.Key;
import com.japancuccok.base.TestServiceProvider;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreInputStream;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreOutputStream;
import com.japancuccok.db.DAOService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.24.
 * Time: 22:44
 */
public class DatastoreStreamTest {

    private class MyDatastoreoutputStream extends DatastoreOutputStream {

        public MyDatastoreoutputStream(int completeFileSize) {
            super(completeFileSize);
        }

        @Override
        public List<Key<ChunkFile>> pop() {
            return super.pop();
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
        TestServiceProvider.end();
    }

    @Test
    public void testOutputSingleByte() throws Exception {
        //given the image has a single byte
        byte[] singleByteArray = new byte[]{0};
        MyDatastoreoutputStream dos =
                new MyDatastoreoutputStream(1);

        //when we write this single byte to the stream
        dos.write(singleByteArray);
        dos.close();

        //then the stream does write a single ChunkFile to the datastore
        assertEquals(1, DAOService.chunkFileDAO.list().size());
    }

    @Test
    public void testOutputByteLengthAboveBufferSize() throws Exception {
        //given the byte array length is above the buffer size
        int chunkSize = DatastoreOutputStream.DATA_CHUNK_SIZE;
        byte[] b = new byte[chunkSize+1];
        new Random().nextBytes(b);
        MyDatastoreoutputStream dos =
                new MyDatastoreoutputStream(chunkSize+1);

        //when we write this byte array to the stream
        dos.write(b);
        dos.close();

        //then the stream writes two ChunkFiles to the datastore
        assertEquals(2, DAOService.chunkFileDAO.list().size());
    }

    @Test
    public void testOutputInputSingleByte() throws Exception {
        //given the image has a single byte
        byte[] singleByteArray = new byte[]{(byte)98765};
        MyDatastoreoutputStream dos =
                new MyDatastoreoutputStream(1);

        //when we write this single byte to the stream
        dos.write(singleByteArray);
        dos.close();

        List<Key<ChunkFile>> chunkFileKeys = dos.pop();
        List<ChunkFile> chunkFiles =
                new ArrayList<ChunkFile> (DAOService.chunkFileDAO.getAll(chunkFileKeys).values());

        //and we read the same single byte from another stream
        byte[] resultBytes = new byte[1];
        DatastoreInputStream dis = new DatastoreInputStream(chunkFiles);
        dis.read(resultBytes);
        dis.close();

        //then the stream does write a single ChunkFile to the datastore
        assertEquals(1, DAOService.chunkFileDAO.list().size());
        //and we can read the same single value that we stored
        int difference = getDifferemce(singleByteArray, resultBytes);
        assertTrue(Arrays.equals(singleByteArray, resultBytes));
    }

    @Test
    public void testOutputInputLengthAboveBufferSize() throws Exception {
        //given the image has a single byte
        int chunkSize = DatastoreOutputStream.DATA_CHUNK_SIZE;
        int fileSize = chunkSize+1;
        byte[] bytes = new byte[fileSize];
        new Random().nextBytes(bytes);
        MyDatastoreoutputStream dos =
                new MyDatastoreoutputStream(fileSize);

        //when we write this single byte to the stream
        dos.write(bytes);
        dos.close();

        List<Key<ChunkFile>> chunkFileKeys = dos.pop();
        List<ChunkFile> chunkFiles =
                new ArrayList<ChunkFile> (DAOService.chunkFileDAO.getAll(chunkFileKeys).values());

        //and we read the same single byte from another stream
        byte[] resultBytes = new byte[fileSize];
        DatastoreInputStream dis = new DatastoreInputStream(chunkFiles);
        dis.read(resultBytes);
        dis.close();

        //then the stream does write two ChunkFiles to the datastore
        assertEquals(2, DAOService.chunkFileDAO.list().size());
        //and we can read the same single value that we stored
        int index = getDifferemce(bytes, resultBytes);
        assertTrue(Arrays.equals(bytes, resultBytes));
    }

    @Test
    public void testOutputInputRealImage() throws Exception {
        //given we have a real image in byte format
        File image =
                new File(
                        DatastoreStreamTest.class.getResource(
                                "japan-nature-wallpaper2.png").toURI());
        RandomAccessFile f = new RandomAccessFile(image, "r");
        byte[] imageBytes = new byte[(int)f.length()];
        f.read(imageBytes);
        int fileSize = (int) image.length();

        MyDatastoreoutputStream dos =
                new MyDatastoreoutputStream(fileSize);

        //when we write this single byte to the stream
        dos.write(imageBytes);
        dos.close();

        List<Key<ChunkFile>> chunkFileKeys = dos.pop();
        List<ChunkFile> chunkFiles =
                new ArrayList<ChunkFile> (DAOService.chunkFileDAO.getAll(chunkFileKeys).values());

        //and we read the same single byte from another stream
        byte[] resultBytes = new byte[fileSize];
        DatastoreInputStream dis = new DatastoreInputStream(chunkFiles);
        dis.read(resultBytes);
        dis.close();

        //then the stream does write three ChunkFiles to the datastore
        assertEquals(3, DAOService.chunkFileDAO.list().size());
        //and we can read the same single value that we stored
        int index = getDifferemce(imageBytes, resultBytes);
        assertTrue(Arrays.equals(imageBytes, resultBytes));
    }

    private int getDifferemce(byte[] bytes, byte[] resultBytes) {
        // if both null, they are the same
        if ((bytes == null) && (resultBytes == null)) return -1;

        // if either are null (they both are not), they are different
        if ((bytes == null) || (resultBytes == null)) return -1;

        // if the lengths are different, they are different
        if (bytes.length != resultBytes.length) return -1;

        // now we know neither are null, so compare, item for item (order counts)
        for (int i = 0; i < bytes.length; i++)
        {
            if (bytes[i] != resultBytes[i]) return i;
        }

        // they are NOT different!
        return -1;
    }
}
