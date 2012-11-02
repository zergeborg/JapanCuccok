package com.japancuccok.common.wicket.panel.main.base;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.28.
 * Time: 15:37
 */
public class MenuItemRepeater<T extends Link> extends ListView<T> {

    private static final long serialVersionUID = 857430870054661170L;

    public MenuItemRepeater(String id) {
        super(id);
    }

    public MenuItemRepeater(String id, IModel<? extends List<T>> iModel) {
        super(id, iModel);
    }

    public MenuItemRepeater(String id, List<T> list) {
        super(id, list);
    }

    @Override
    protected void populateItem(ListItem<T> listItem) {
        setOutputMarkupId(true);

        Link menuItem = listItem.getModelObject();
        listItem.add(menuItem);
    }
}
