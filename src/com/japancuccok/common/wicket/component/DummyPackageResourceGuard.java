package com.japancuccok.common.wicket.component;

import org.apache.wicket.markup.html.SecurePackageResourceGuard;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.01.
 * Time: 0:36
 */
public class DummyPackageResourceGuard extends SecurePackageResourceGuard {

    public DummyPackageResourceGuard() {
        super(new SimpleCache(100));
    }
}
