package com.japancuccok.common.wicket.model;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.15.
 * Time: 18:45
 */
public interface IAdminBasePageResolver {

    public Label getHeaderTitle();
    public Panel getMenuPanel();
    public Panel getFooterScriptPanel();
}
