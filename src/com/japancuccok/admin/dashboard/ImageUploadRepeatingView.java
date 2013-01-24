package com.japancuccok.admin.dashboard;

import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 18:55
 */
public class ImageUploadRepeatingView extends RepeatingView {

    private static final long serialVersionUID = 2959046740045153062L;
    private NewImageAjaxButton imageAjaxButton;

    public ImageUploadRepeatingView(String componentId, NewImageAjaxButton imageAjaxButton) {
        super(componentId);
        this.imageAjaxButton = imageAjaxButton;
    }

    public NewImageAjaxButton getImageAjaxButton() {
        return imageAjaxButton;
    }

    public void setImageAjaxButton(NewImageAjaxButton imageAjaxButton) {
        this.imageAjaxButton = imageAjaxButton;
    }
}
