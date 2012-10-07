package com.japancuccok.common.wicket.component;

import org.apache.wicket.markup.html.link.StatelessLink;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.19.
 * Time: 15:50
 */
public class DummyStatelessLink<T> extends StatelessLink<T> {

    private static final long serialVersionUID = -3034261850723839138L;

    /**
     * Construct.
     *
     * @param id
     */
    public DummyStatelessLink(String id, Class<T> clazz) {
        super(id);
    }

    @Override
    public void onClick() {
    }
}
