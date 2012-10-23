package com.japancuccok.admin.dashboard.base;

import org.apache.wicket.Component;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.22.
 * Time: 15:52
 */
public class PersistEventHandlerPayload implements IEventHandlerPayload, Serializable {

    private static final long serialVersionUID = 462370817415378518L;

    public Component component;
    public ProductUploadModel uploadModel;

    public PersistEventHandlerPayload(Component component, ProductUploadModel uploadModel) {
        if(component == null || uploadModel == null) {
            throw new IllegalArgumentException("Some parameters are missing. Object is in invalid state.");
        }
        this.component = component;
        this.uploadModel = uploadModel;
    }

    public Component getComponent() {
        return component;
    }

    public ProductUploadModel getUploadModel() {
        return uploadModel;
    }
}
