package com.japancuccok.common.wicket.template;

import com.japancuccok.common.wicket.panel.main.shop.ShopBannerPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopCopyrightPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopFooterScriptPanel;
import com.japancuccok.common.wicket.panel.main.shop.ShopMenuPanel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.14.
 * Time: 22:43
 */
public abstract class ShopBasePage extends BasePage {

    private static final long serialVersionUID = 7328220589692626464L;

    public abstract Label getHeaderTitle();

    @Override
    public Panel getMenuPanel() {
        return new ShopMenuPanel("menuPanel");
    }

    @Override
    public Panel getCopyrightPanel() {
        return new ShopCopyrightPanel("copyrightPanel");
    }

    @Override
    public Panel getFooterScriptPanel() {
        return new ShopFooterScriptPanel("footerScriptPanel");
    }

    @Override
    public Panel getBannerPanel() {
        return new ShopBannerPanel("bannerPanel");
    }

    @Override
    public Image getContentBackground() {
        Image contentBackground = new Image("contentBackground","") {

            private static final long serialVersionUID = 7765851797556940855L;

            @Override
            protected boolean getStatelessHint()
            {
                return true;
            }

        };
        contentBackground.add(new AttributeModifier("src","/img/productBackground.png"));
        return contentBackground;
    }

}
