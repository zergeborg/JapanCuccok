package com.japancuccok.common.wicket.panel.admin.products;

import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.domain.image.DatastoreImage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.26.
 * Time: 22:28
 */
public class ImageOptionsPanel<T extends BinaryImage> extends Panel {

    private static final long serialVersionUID = -4256002646174518697L;

    public ImageOptionsPanel(String id, IModel<?> imageModel) {
        super(id, imageModel);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        DatastoreImage wicketImage = (DatastoreImage) ((BinaryImage)getDefaultModelObject()).asWicketImage("editedImage");
        add(new Label("imgRes", new Model<Serializable>(wicketImage.getOldWidth()+" x "+wicketImage.getOldHeight())));
        add(new Label("imgSize", new Model<Serializable>(wicketImage.getOldFileSize()+" Byte")));
    }
}
