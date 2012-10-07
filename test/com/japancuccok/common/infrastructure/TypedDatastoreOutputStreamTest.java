package com.japancuccok.common.infrastructure;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.japancuccok.base.TestServiceProvider;
import com.japancuccok.common.infrastructure.gaeframework.TypedDatastoreOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 20:18
 */
public class TypedDatastoreOutputStreamTest {

    DatastoreService dataStoreServiceMock = mock(DatastoreService.class);

    private class TestOutputStream extends TypedDatastoreOutputStream {

        public TestOutputStream(String fileName) {
            super(fileName);
        }

        public TestOutputStream(String fileName, Entity gaeEntity) {
            super(fileName, gaeEntity);
        }

        @Override
        protected DatastoreService getDataStoreService() {
            return dataStoreServiceMock;
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

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() throws Exception {
        //given
        // The user provides null argument
        Entity gaeEntity = null;
        TypedDatastoreOutputStream outStream = new TypedDatastoreOutputStream("valami", gaeEntity);

        //when
        //then
        // The constructor throws IllegalArgumentException
    }

    @Test
    public void testVaildWrite_BufferIsEmpty() throws Exception {
        //given
        // The user provides valid entity
        Entity gaeEntity = new Entity("Valami","valami");
        TypedDatastoreOutputStream outStream = new TestOutputStream("valami", gaeEntity);

        //when
        // The user calls the method
        outStream.write(0);

        //then
        // Nothing happens :)
    }

    @Test
    public void testConstructorWithEntity() throws Exception {
        //given

        //when

        //then
    }
}
