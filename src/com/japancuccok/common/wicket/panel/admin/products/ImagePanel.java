package com.japancuccok.common.wicket.panel.admin.products;

import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.DatastoreImage;
import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.image.ResourceProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.03.
 * Time: 23:05
 */
public abstract class ImagePanel<T extends IImage> extends Panel {

    private static final long serialVersionUID = -8496766777326419564L;
    private final T image;

    protected ImagePanel(String id, T image) {
        super(id, new CompoundPropertyModel<T>(image));
        this.image = image;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        DatastoreImage wicketImage = (DatastoreImage)((BinaryImage)getDefaultModelObject()).asWicketImage("editedImage");
        add(wicketImage);
        IModel dashboardModel = ((CompoundPropertyModel)getDefaultModel()).bind("imageOptions.productDashboard");
        IModel generalPageModel = ((CompoundPropertyModel)getDefaultModel()).bind("imageOptions.productGeneralPage");
        add(new CheckBox("dashboard", dashboardModel));
        add(new CheckBox("productGeneralPage", generalPageModel));
        add(new Label("name"));
        add(getOptionsPanel());
    }

    protected abstract Panel getOptionsPanel();

}
