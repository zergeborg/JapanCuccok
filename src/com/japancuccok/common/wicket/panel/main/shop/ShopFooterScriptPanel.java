package com.japancuccok.common.wicket.panel.main.shop;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.27.
 * Time: 10:55
 */
public class ShopFooterScriptPanel extends Panel {

    private static final long serialVersionUID = 1256067210247711514L;

    public ShopFooterScriptPanel(String id) {
        super(id);
    }

    @Override
    protected boolean getStatelessHint()
    {
        return true;
    }

}
