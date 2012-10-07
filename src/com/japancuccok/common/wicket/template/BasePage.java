package com.japancuccok.common.wicket.template;

import com.japancuccok.common.wicket.model.IBasePageResolver;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.04.11.
 * Time: 23:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasePage extends WebPage implements IBasePageResolver {

    private static final long serialVersionUID = -6102135804168596234L;

    public BasePage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(getHeaderTitle());
//        add(getContentBackground());
        add(getMenuPanel());
        add(getBannerPanel());
        add(getFooterContentPanel());
        add(getFooterScriptPanel());
        add(getCopyrightPanel());
    }

    @Override
    public Panel getFooterContentPanel() {
        return new EmptyPanel("footerContentPanel");
    }

}
