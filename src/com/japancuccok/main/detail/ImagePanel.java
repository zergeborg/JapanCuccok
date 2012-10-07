package com.japancuccok.main.detail;

import com.japancuccok.common.domain.image.IImage;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.08.
 * Time: 22:09
 */
public class ImagePanel<T extends IImage> extends Panel {

    private static final long serialVersionUID = -4211098065980067151L;
    private int index;

    public ImagePanel(String id, int index, IModel<T> imageModel) {
        super(id, imageModel);
        this.index = index;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        setMarkupId(getId()+index);
        final IImage image = (IImage) getDefaultModelObject();
        WebComponent dsImage = image.asWicketImage("smallImage");
        add(dsImage);
    }
}
