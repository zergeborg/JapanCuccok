package com.japancuccok.main.dashboard;

import com.japancuccok.common.pattern.DashboardImageLoadStrategy;
import com.japancuccok.common.wicket.panel.main.base.BaseMenuPanel;
import com.japancuccok.common.wicket.panel.main.dashboard.DashboardBannerPanel;
import com.japancuccok.common.wicket.panel.main.dashboard.DashboardFooterScriptPanel;
import com.japancuccok.common.wicket.template.BasePage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.05.26.
 * Time: 13:44
 */
public class Dashboard extends BasePage {

    private static final long serialVersionUID = 1338162580971321018L;

    public Dashboard() {
        SliderListView sliderListView = new SliderListView("imageSlider", new DashboardImageLoadStrategy());
        sliderListView.setRenderBodyOnly(true);
        add(sliderListView);
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Lapozd végig választékunkat!");
    }
    
    @Override
    public org.apache.wicket.markup.html.image.Image getContentBackground() {
        org.apache.wicket.markup.html.image.Image contentBackground = new org.apache.wicket
                .markup.html.image.Image("contentBackground","");
        contentBackground.add(new AttributeModifier("src","/img/contentBackground.png"));
        return contentBackground;
    }

    @Override
    public Panel getMenuPanel() {
        return new BaseMenuPanel("menuPanel"){

            private static final long serialVersionUID = -153635075724852575L;

            @Override
            public Link getCartLink() {
                return new Link("cart", new Model("")) {
                    private static final long serialVersionUID = 7781311910710557609L;

                    @Override
                    public void onClick() {
                    }
                };
            }
        };
    }

    @Override
    public Panel getCopyrightPanel() {
//        return new DashboardCopyrightPanel("copyrightPanel");
        return new EmptyPanel("copyrightPanel");
    }

    @Override
    public Panel getFooterContentPanel() {
//        return new DashboardFooterContentPanel("footerContentPanel");
        return new EmptyPanel("footerContentPanel");
    }

    @Override
    public Panel getFooterScriptPanel() {
        return new DashboardFooterScriptPanel("footerScriptPanel");
    }

    @Override
    public Panel getBannerPanel() {
        return new DashboardBannerPanel("bannerPanel");
    }
    
}
