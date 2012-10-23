package com.japancuccok.db;

import com.google.appengine.api.datastore.*;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.impl.ConcreteEntityMetadata;
import com.japancuccok.base.TestServiceProvider;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreOutputStream;
import com.japancuccok.common.domain.image.BaseImageData;
import com.japancuccok.common.domain.image.BinaryImageData;
import com.japancuccok.common.domain.image.BinaryImage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.appengine.api.datastore.FetchOptions.Builder.*;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 22:35
 */
public class GenericDAOTest {

    private DatastoreService dsService = DatastoreServiceFactory.getDatastoreService();
    private List<Entity> entities;

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
    public void testBinaryAvailablePut_sizeBelowChunkSize() throws Exception {
        //given
        // The user provides an object which is the type of IBinaryProvider
        BinaryImageData imageData = new BinaryImageData(
                Ref.create(Key.<BinaryImage>create(BinaryImage.class,"Valamni")));

        //when
        // The user saves it using the the DAO
        GenericGaeDAO<BaseImageData> dao = new GenericGaeDAO<BaseImageData>(BaseImageData.class, true);
        dao.saveBinary(new Blob(new byte[]{0}));

        //then
        // The DAO tries to put it directly (instead of objectify) into the datastore
        // as a byte array stream and it returns the keys of the saved items
        // and a SINGLE image data plus a SINGLE data chunk will be stored as a result
        assertEquals(1, dao.list().size());
    }

    @Test
    public void testBinaryAvailablePut_sizeOverChunkSize() throws Exception {
        //given
        // We have a binary data which is over the GAE size limit
        int chunkSize = DatastoreOutputStream.DATA_CHUNK_SIZE;
        byte[] b = new byte[chunkSize+1];
        new Random().nextBytes(b);
        BinaryImageData imageData = new BinaryImageData(
                Ref.create(Key.<BinaryImage>create(BinaryImage.class,"Valamni")));

        //when
        // The user saves it using the the DAO
        GenericGaeDAO<BaseImageData> dao = new GenericGaeDAO<BaseImageData>(BaseImageData.class, true);
        dao.saveBinary(new Blob(new byte[]{0}));

        //then
        // The DAO puts it directly into the datastore as a byte array stream
        // and a SINGLE image data plus MULTIPLE data chunks will be stored as a result
        assertEquals(1, dao.list().size());
        GenericGaeDAO<ChunkFile> chunkFileDAO = (GenericGaeDAO<ChunkFile>) GenericGaeDAOFactory.getInstance(ChunkFile.class);
        assertEquals(2, chunkFileDAO.list().size());
    }

    @Test
    public void testImageDataDelete() throws Exception {
        //given
        // We have an image to be uploaded
        int chunkSize = DatastoreOutputStream.DATA_CHUNK_SIZE;
        byte[] b = new byte[chunkSize+1];
        new Random().nextBytes(b);
        BinaryImageData imageData = new BinaryImageData(
                Ref.create(Key.<BinaryImage>create(BinaryImage.class,"Valamni")));

        //when
        // The user saves it using the the DAO
        GenericGaeDAO<BinaryImageData> dao = new GenericGaeDAO<BinaryImageData>(BinaryImageData.class, true);
        dao.saveBinary(new Blob(new byte[]{0}));
        // And the user also deletes it
        dao.deleteBinary(imageData);
        dao.delete(imageData);

        //then
        // Neither chunks should remain
        PreparedQuery pQuery = load("ChunkFile");
        FetchOptions fetchOptions = withLimit(1);
        entities = pQuery.asList(fetchOptions);
        assertEquals(0, entities.size());
        // Nor image data
        assertEquals(0, dao.list().size());
    }

    private Entity getEntity(Object object) {
        ConcreteEntityMetadata entityMetadata = new ConcreteEntityMetadata(new ObjectifyFactory()
                , object.getClass());
        Entity entity = entityMetadata.getKeyMetadata().initEntity(object);
        return entity;
    }

    private PreparedQuery load(String kind) throws FileNotFoundException {
        Query query = new Query(kind);
        query.addSort("index");
        return dsService.prepare(query);
    }
}
