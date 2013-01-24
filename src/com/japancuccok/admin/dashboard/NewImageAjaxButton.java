package com.japancuccok.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ProductUploadModel;
import com.japancuccok.common.wicket.component.BlockUIDecorator;
import com.japancuccok.common.wicket.panel.admin.dashboard.UploadFormPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.21.
 * Time: 23:04
 */
public class NewImageAjaxButton extends AjaxButton {

    private static final long serialVersionUID = -4027163959759610997L;

    private RepeatingView uploadRepeatingView;
    private CompoundPropertyModel<ProductUploadModel> uploadPanelModel;
    private WebMarkupContainer dummyRepeaterContainer;
    public static final int MAX_NUMBER_OF_UPLOADS = 5;
    private int currentNumberOfUploads = 0;

    public NewImageAjaxButton(String id, IModel<String> model, RepeatingView uploadRepeatingView, CompoundPropertyModel<ProductUploadModel> uploadPanelModel, WebMarkupContainer dummyRepeaterContainer) {
        super(id, model);
        this.uploadRepeatingView = uploadRepeatingView;
        this.uploadPanelModel = uploadPanelModel;
        this.dummyRepeaterContainer = dummyRepeaterContainer;
        setVisibilityAllowed(true);
        setOutputMarkupPlaceholderTag(true);
    }

    public void increase() {
        if(currentNumberOfUploads < MAX_NUMBER_OF_UPLOADS) {
            currentNumberOfUploads++;
        }
    }

    public void decrease() {
        if(currentNumberOfUploads > 0) {
            currentNumberOfUploads--;
        }
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
        if(target != null) {
            final Panel uploadPanel = createUploadPanel(uploadRepeatingView.newChildId(), uploadPanelModel, target);
            if(uploadPanel != null) {
                // We add the upload panel to the repeater, but...
                uploadRepeatingView.add(uploadPanel);
                // ... we update its' parent because Wicket keeps telling that...
                // ... "RepeatingView has been added to the target. This component is...
                // ... a repeater and cannot be repainted via ajax directly."
                target.add(dummyRepeaterContainer);
            }
        }
    }

    @Override
    protected IAjaxCallDecorator getAjaxCallDecorator()
    {
        return new BlockUIDecorator();
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form<?> form) {
        System.out.println("Error in newImageDiv");
    }

    @Override
    protected void onConfigure() {
        setVisible(currentNumberOfUploads < MAX_NUMBER_OF_UPLOADS);
    }

    private Panel createUploadPanel(String childId, CompoundPropertyModel<ProductUploadModel> model, AjaxRequestTarget target) {
        Panel uploadPanel = null;
        uploadPanel = new UploadFormPanel(childId, String.valueOf(currentNumberOfUploads), model);
        currentNumberOfUploads++;
        if(currentNumberOfUploads < MAX_NUMBER_OF_UPLOADS) {
            return uploadPanel;
        }
        setVisible(false);
        target.add(this);
        error(getString("maxUploadLimitExceeded"));
        return uploadPanel;
    }

}
