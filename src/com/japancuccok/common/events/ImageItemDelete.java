package com.japancuccok.common.events;

import com.japancuccok.common.wicket.panel.admin.products.DatastoreImagePanel;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 23:12
 */
public class ImageItemDelete implements Serializable {

    private static final long serialVersionUID = 3648777196527791712L;
    private final AjaxRequestTarget target;
    private final DatastoreImagePanel imagePanel;

    public ImageItemDelete(AjaxRequestTarget target, DatastoreImagePanel imagePanel) {
        this.target = target;
        this.imagePanel = imagePanel;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }

    public DatastoreImagePanel getImagePanel() {
        return imagePanel;
    }

    @Override
    public String toString() {
        return "ImageItemDelete{" +
                       "target=" + target +
                       ", imagePanel=" + imagePanel +
                       '}';
    }
}
