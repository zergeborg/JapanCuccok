package com.japancuccok.common.events;

import com.japancuccok.common.wicket.panel.admin.dashboard.UploadFormPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 18:11
 */
public class ImageUploadItemDelete implements Serializable {

    private static final long serialVersionUID = 1162216142453429964L;
    private final AjaxRequestTarget target;
    private transient UploadFormPanel uploadFormPanel;

    /**
     * Constructor
     *
     * @param target
     * @param uploadFormPanel
     */
    public ImageUploadItemDelete(AjaxRequestTarget target, UploadFormPanel uploadFormPanel)
    {
        this.target = target;
        this.uploadFormPanel = uploadFormPanel;
    }

    /** @return ajax request target */
    public AjaxRequestTarget getTarget()
    {
        return target;
    }

    public UploadFormPanel getUploadFormPanel() {
        return uploadFormPanel;
    }

}
