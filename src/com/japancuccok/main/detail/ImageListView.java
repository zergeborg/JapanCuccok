package com.japancuccok.main.detail;

import com.japancuccok.common.domain.image.IImage;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.05.
 * Time: 22:44
 */
public class ImageListView<T extends IImage> extends ListView<T> {

    private static final long serialVersionUID = 4773356342888606898L;

    public ImageListView(String id, IModel<? extends List<? extends T>> model) {
        super(id, model);
    }

    @Override
    protected void populateItem(ListItem<T> listItem) {
        setVisible(true);
        setOutputMarkupId(true);
        setReuseItems(true);
        Panel imgPanel = new ImagePanel("imagePanel", listItem.getIndex(), listItem.getModel());
        imgPanel.setVisible(true);
        imgPanel.setOutputMarkupId(true);
        listItem.add(imgPanel);
    };

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        super.onComponentTag(tag);
    }

}