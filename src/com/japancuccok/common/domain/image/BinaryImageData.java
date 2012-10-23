package com.japancuccok.common.domain.image;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import com.japancuccok.db.IBinaryProvider;

import java.io.Serializable;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.17.
 * Time: 17:52
 */
@EntitySubclass
@Cache
public class BinaryImageData extends BaseImageData<BinaryImage> implements IBinaryProvider<IImage>,
        Serializable {

    private static final long serialVersionUID = 4740699823432691723L;
    private static Long staticId = 0l;

    public static class WithBlob {}

    public BinaryImageData() {
    }

    public BinaryImageData(Ref<BinaryImage> imageKey) {
        super(imageKey);
        synchronized (BinaryImageData.class) {
            staticId++;
        }
    }

    @Override
    public Blob getRawData() {
        return null;
    }

    @Override
    public void setBytes(byte[] bytes) {
        // Intentionally left blank
    }

    @Override
    public byte[] getBytes() {
        return null;
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
