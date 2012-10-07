package com.japancuccok.common.wicket.panel.main.shop;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.27.
 * Time: 11:21
 */
public class ShopCopyrightPanel extends Panel {

    private static final long serialVersionUID = 5981487451071080679L;

    public ShopCopyrightPanel(String id) {
        super(id);
    }

    @Override
    protected boolean getStatelessHint()
    {
        return true;
    }

}
