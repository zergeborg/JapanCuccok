package com.japancuccok.main.dashboard;

import com.japancuccok.common.wicket.template.BasePage;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.support.membermodification.MemberMatcher.*;
import static org.powermock.api.support.membermodification.MemberModifier.stub;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.04.12.
 * Time: 22:05
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest( { WebPage.class, Label.class, BookmarkablePageLink.class, MarkupContainer.class })
public class DashboardTest {

    @Test
    public void testNewPage() throws Exception {
        suppress(defaultConstructorIn(WebPage.class));
        suppress(constructor(Label.class, String.class, String.class));
        stub(method(MarkupContainer.class, "children_indexOf", Component.class)).toReturn(-1);
        suppress(method(MarkupContainer.class, "addedComponent", Component.class));
        BasePage dashboard = new Dashboard();
        assert(dashboard.size() != 0);
    }
}
