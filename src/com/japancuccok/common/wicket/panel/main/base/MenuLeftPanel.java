package com.japancuccok.common.wicket.panel.main.base;

import com.japancuccok.common.domain.category.CategoryModel;
import com.japancuccok.common.wicket.template.BasePage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.28.
 * Time: 18:00
 */
public class MenuLeftPanel extends Panel {

    private static final long serialVersionUID = 6301017923210299464L;

    public MenuLeftPanel(String id) {
        super(id);
    }

    public MenuLeftPanel(String id, IModel<?> model) {
        super(id, model);
    }
    @Override
    protected void onInitialize() {
        super.onInitialize();
        initializeLinks();
    }

    protected void initializeLinks() {
        MenuItemRepeater menuItemRepeater = new MenuItemRepeater("menuItemRepeater", new CategoryModel(findParent(BasePage.class)));
        menuItemRepeater.setRenderBodyOnly(true);
        add(menuItemRepeater);
    }

}
