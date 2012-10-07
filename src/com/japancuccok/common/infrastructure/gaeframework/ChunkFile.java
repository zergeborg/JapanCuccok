package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import com.japancuccok.common.domain.image.BinaryImageData;

import java.io.Serializable;

/**
 * This class represents a limited sized binary data chunk,
 * which might be the part of a larger binary data (e.g. images)
 *
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.21.
 * Time: 21:25
 */
@Entity
@Cache
public class ChunkFile implements Serializable {

    private static final long serialVersionUID = 8635461503296085569L;

    @Id String id;
    public Blob data;
    public int length;
    @Parent Key<BinaryImageData> imageDataKey;

    public ChunkFile() {
    }

    public ChunkFile(String id, Blob data, int length, Key<BinaryImageData> imageDataKey) {
        this.id = id;
        this.data = data;
        this.length = length;
        this.imageDataKey = imageDataKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkFile)) return false;

        ChunkFile chunkFile = (ChunkFile) o;

        if (id != null ? !id.equals(chunkFile.id) : chunkFile.id != null) return false;
        if (imageDataKey != null ? !imageDataKey.equals(chunkFile.imageDataKey) : chunkFile
                .imageDataKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (imageDataKey != null ? imageDataKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ChunkFile{" +
                "id='" + id + '\'' +
                ", length=" + length +
                ", imageDataKey=" + imageDataKey +
                '}';
    }
}
