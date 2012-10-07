package com.japancuccok.common.infrastructure.gae;

import com.japancuccok.main.JapanCuccok;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.IProvider;

/**
* Created with IntelliJ IDEA.
* User: Nagy Gergely
* Date: 2012.07.15.
* Time: 23:20
*/
public class GaeWebSessionStoreProvider implements IProvider<ISessionStore>
{
    private final ISessionStore sessionStore;

    public GaeWebSessionStoreProvider(ISessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public ISessionStore get()
    {
        sessionStore.registerUnboundListener(JapanCuccok.get());
        return sessionStore;
    }
}
