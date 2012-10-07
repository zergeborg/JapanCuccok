package com.japancuccok.common.wicket.panel.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ProductUploadModel;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.28.
 * Time: 17:16
 */
public class ImageUploadPanel extends Panel {

    private static final long serialVersionUID = -7257671182071168991L;

    private FileUploadField imageUpload;

    public ImageUploadPanel(String id, String currentUploadNr,
                            CompoundPropertyModel<ProductUploadModel> model) {
        super(id, model);
        initImageUpload(currentUploadNr, model);
    }

    private void initImageUpload(String currentUploadNr, CompoundPropertyModel<ProductUploadModel> model) {
        IModel newFileUploadModel = model.bind("uploads."+currentUploadNr);
        imageUpload = new FileUploadField("uploads", newFileUploadModel);
        imageUpload.setOutputMarkupId(true);
        add(imageUpload);
    }

    public FileUploadField getImageUpload() {
        return imageUpload;
    }

}
