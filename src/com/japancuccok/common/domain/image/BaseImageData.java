package com.japancuccok.common.domain.image;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;
import com.japancuccok.db.IKeyProvider;
import com.japancuccok.db.INameProvider;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.24.
 * Time: 19:47
 */
@Entity
@Cache
public class BaseImageData<T extends IImage> implements INameProvider, IKeyProvider<T>,
        IImageData,
        Serializable {

    private static final long serialVersionUID = 8822622792678700876L;

    @Id
    Long id;

    @Parent
    @Load
    Ref<T> imageRef;

    public BaseImageData() {
    }

    public BaseImageData(Ref<T> imageRef) {
        this.imageRef = imageRef;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return imageRef.getKey().getName();
    }

    @Override
    public void setKey(Ref<T> key) {
        this.imageRef = key;
    }

    @Override
    public Ref<T> getKey() {
        return imageRef;
    }

    @Override
    public Collection<Ref<T>> getKeys() {
        return null;
    }

    @Override
    public String toString() {
        return "BaseImageData";
    }
}
