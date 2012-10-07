package com.japancuccok.common.wicket.component;

import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.19.
 * Time: 16:17
 */
public class StatelessExternalLink extends ExternalLink {

    private static final long serialVersionUID = 7936755438420398293L;

    public StatelessExternalLink(String id, String href) {
        super(id, href);
    }

    @Override
    protected boolean getStatelessHint() {
        return true;
    }
}
