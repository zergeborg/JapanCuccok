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

        public TestOutputStream(int length) {
            super(length);
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
    public void testVaildWrite_BufferIsEmpty() throws Exception {
        //given
        // The user provides valid entity
        Entity gaeEntity = new Entity("Valami","valami");
        TypedDatastoreOutputStream outStream = new TestOutputStream(1);

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
