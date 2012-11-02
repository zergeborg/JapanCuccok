package com.japancuccok.common.wicket.panel.main.base;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.26.
 * Time: 16:09
 */
public abstract class BaseMenuPanel extends Panel {

    private static final long serialVersionUID = -3133622189745558285L;

    public BaseMenuPanel(String id) {
        super(id);
        setRenderBodyOnly(true);
    }
    
    public abstract Link getCartLink();

    @Override
    protected void onInitialize() {
        super.onInitialize();
        initializeLinks();
    }

    protected void initializeLinks() {
        Panel menuLeftPanel = new MenuLeftPanel("menuLeftPanel");
        add(menuLeftPanel);
        add(getCartLink());
    }
}
