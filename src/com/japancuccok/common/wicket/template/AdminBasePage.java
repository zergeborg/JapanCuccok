package com.japancuccok.common.wicket.template;

import com.japancuccok.common.wicket.model.IAdminBasePageResolver;
import com.japancuccok.common.wicket.panel.admin.base.AdminMenuPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.11.
 * Time: 22:13
 */
public abstract class AdminBasePage extends WebPage implements IAdminBasePageResolver {

    private static final long serialVersionUID = 461769904486216926L;

    public AdminBasePage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("pageTitle", "Szia Admin!"));
        add(getHeaderTitle());
        add(getMenuPanel());
        add(getFooterScriptPanel());
    }

    @Override
    public Panel getMenuPanel() {
        return new AdminMenuPanel("menuPanel");
    }

}
