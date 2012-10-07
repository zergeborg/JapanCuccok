package com.japancuccok.common.wicket.component;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.08.
 * Time: 10:34
 */
public class CustomPagingNavigator extends PagingNavigator {

    private static final long serialVersionUID = 319038887463552470L;

    public CustomPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
        setOutputMarkupId(true);
        setMarkupId(id);
    }

    public CustomPagingNavigator(final String id, final IPageable pageable,
                                 final IPagingLabelProvider labelProvider)
    {
        super(id,pageable,labelProvider);
        setOutputMarkupId(true);
        setMarkupId(id);
    }
}
