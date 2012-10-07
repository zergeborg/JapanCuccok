package com.japancuccok.common.domain.image;

import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.Property;
import com.googlecode.objectify.impl.translate.*;

import java.lang.reflect.Type;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.24.
 * Time: 23:07
 */
public class BinaryImageDataTranslatorFactory extends ValueTranslatorFactory<BinaryImageData, Long> {

    public BinaryImageDataTranslatorFactory() {
        super(BinaryImageData.class);
    }

    @Override
    protected ValueTranslator<BinaryImageData, Long> createSafe(Path path, Property property, Type type, CreateContext ctx) {
        return new ValueTranslator<BinaryImageData, Long>(path, Long.class) {
            @Override
            protected BinaryImageData loadValue(Long value, LoadContext ctx) {
                return new BinaryImageData();
            }

            @Override
            protected Long saveValue(BinaryImageData value, SaveContext ctx) {
                return value.getId();
            }
        };
    }
}
