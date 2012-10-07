package com.japancuccok.common.wicket.panel.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ProductUploadModel;
import com.japancuccok.common.wicket.component.UrlTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.28.
 * Time: 17:26
 */
public class ImageURLPanel extends Panel {

    private static final long serialVersionUID = 3639755907205073274L;

    private UrlTextField urlField;

    public ImageURLPanel(String id, String currentUploadNr,
                            CompoundPropertyModel<ProductUploadModel> model) {
        super(id, model);
        initImageUpload(currentUploadNr, model);
    }

    private void initImageUpload(String currentUploadNr, CompoundPropertyModel<ProductUploadModel> model) {
        IModel newFileUrlModel = model.bind("urls."+currentUploadNr);
        urlField = new UrlTextField("newImageUrl", newFileUrlModel){

            private static final long serialVersionUID = -4478694973139953040L;

            public boolean isRequired() {
                return isVisible();
            }
        };
        add(urlField);
    }

    public UrlTextField getUrls() {
        return urlField;
    }
}
