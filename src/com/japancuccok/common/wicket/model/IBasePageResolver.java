package com.japancuccok.common.wicket.model;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.26.
 * Time: 14:57
 */
public interface IBasePageResolver {

    public Label getHeaderTitle();
    public Image getContentBackground();
    public Panel getMenuPanel();
    public Panel getCopyrightPanel();
    public Panel getFooterContentPanel();
    public Panel getFooterScriptPanel();
    public Panel getBannerPanel();
}
