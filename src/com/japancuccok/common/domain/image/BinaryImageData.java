package com.japancuccok.common.domain.image;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.OnLoad;
import com.japancuccok.common.infrastructure.gaeframework.ChunkFile;
import com.japancuccok.common.infrastructure.gaeframework.DatastoreInputStream;
import com.japancuccok.db.IBinaryProvider;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 17:52
 */
@EntitySubclass
@Cache
public class BinaryImageData extends BaseImageData<BinaryImage> implements
        IBinaryProvider<IImage>, Serializable {

    private transient static final Logger logger =
            Logger.getLogger(BinaryImageData.class.getName());

    private static final long serialVersionUID = 4740699823432691723L;

    private List<Key<ChunkFile>> chunkFileKeys;
    private byte[] binaryContent;

    public BinaryImageData() {
    }

    public BinaryImageData(Ref<BinaryImage> imageKey, List<Key<ChunkFile>> chunkFileKeys) {
        super(imageKey);
        this.chunkFileKeys = chunkFileKeys;
    }

    @OnLoad
    public void loadBytes() {
        DatastoreInputStream fis = null;

        try
        {
            Collection<ChunkFile> chunkFiles = chunkFileDAO.getAll(chunkFileKeys).values();
            if(!chunkFiles.isEmpty()) {
                int fileSizeInBytes =
                        (int)chunkFiles.iterator().next().getFileSizeInBytes();
                logger.info("Getting file (file size: "+fileSizeInBytes+") as binary content");
                byte[] tempBytes = new byte[fileSizeInBytes];
                fis = new DatastoreInputStream(new ArrayList<ChunkFile>(chunkFiles));
                fis.read(tempBytes);
                binaryContent = Arrays.copyOf(tempBytes, tempBytes.length);
            }
        }
        catch (IOException e)
        {
            logger.log(Level.SEVERE, "Something nasty and unexpected happened", e);
            binaryContent = null;
        } finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
        }
    }

    @Override
    public Blob getRawData() {
        return new Blob(binaryContent);
    }

    @Override
    public void setBytes(byte[] bytes) {
        // Intentionally left blank
    }

    @Override
    public byte[] getBytes() {
        return binaryContent;
    }

    public List<Key<ChunkFile>> getChunkFileKeys() {
        return chunkFileKeys;
    }

    public void setChunkFileKeys(List<Key<ChunkFile>> chunkFileKeys) {
        this.chunkFileKeys = chunkFileKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || !(o instanceof BinaryImageData)) return false;

        BinaryImageData imageData = (BinaryImageData) o;

        if (!id.equals(imageData.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BinaryImageData{" +
                "id=" + id +
                '}';
    }
}
