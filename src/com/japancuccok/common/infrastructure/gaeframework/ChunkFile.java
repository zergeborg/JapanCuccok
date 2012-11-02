package com.japancuccok.common.infrastructure.gaeframework;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

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
public class ChunkFile implements Serializable, Comparable {

    private static final long serialVersionUID = 8635461503296085569L;

    @Id
    Long id;
    public Blob data;
    public int length;
    public long fileSizeInBytes;

    public ChunkFile() {
    }

    public ChunkFile(Blob data, int length, long fileSizeInBytes) {
        this.data = data;
        this.length = length;
        this.fileSizeInBytes = fileSizeInBytes;
    }

    public long getFileSizeInBytes() {
        return fileSizeInBytes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkFile)) return false;

        ChunkFile chunkFile = (ChunkFile) o;

        if (id != null ? !id.equals(chunkFile.id) : chunkFile.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "ChunkFile{" +
                "id='" + id + '\'' +
                ", length=" + length +
                '}';
    }

    @Override
    public int compareTo(Object object) {
        ChunkFile chunkFile = (ChunkFile) object;
        return id.compareTo(chunkFile.id);
    }
}
