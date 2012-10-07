package com.japancuccok.common.wicket.panel.admin.products;

import com.japancuccok.common.domain.image.BinaryImage;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.26.
 * Time: 22:33
 */
public class DatastoreImagePanel<T extends BinaryImage> extends ImagePanel<T> {

    private static final long serialVersionUID = -4018188384625829441L;

    public DatastoreImagePanel(String id, T image) {
        super(id, image);
    }

    @Override
    protected Panel getOptionsPanel() {
        return new ImageOptionsPanel("optionsPanel", getDefaultModel());
    }
}
