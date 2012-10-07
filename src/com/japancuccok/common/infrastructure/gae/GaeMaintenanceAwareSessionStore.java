package com.japancuccok.common.infrastructure.gae;

import com.google.appengine.api.capabilities.CapabilitiesService;
import com.google.appengine.api.capabilities.CapabilitiesServiceFactory;
import com.google.appengine.api.capabilities.Capability;
import com.google.appengine.api.capabilities.CapabilityStatus;
import com.japancuccok.common.infrastructure.tools.CompressUtil;
import org.apache.wicket.Session;
import org.apache.wicket.request.Request;
import org.apache.wicket.session.ISessionStore;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.10.
 * Time: 9:01
 */
public class GaeMaintenanceAwareSessionStore implements ISessionStore {

    private static final long serialVersionUID = -6171879819893682322L;

    transient private static final Logger logger = Logger.getLogger(GaeMaintenanceAwareSessionStore.class.getName());

    public final ISessionStore delegate;

    public GaeMaintenanceAwareSessionStore(ISessionStore delegate) {
        this.delegate = delegate;
    }

    private void recalculateApplicationStatus() {


        long before = System.currentTimeMillis();
        CapabilitiesService service =  CapabilitiesServiceFactory.getCapabilitiesService();
        CapabilityStatus status = service.getStatus(Capability.DATASTORE_WRITE).getStatus();

        if (status == CapabilityStatus.DISABLED) {
            System.out.println("DATASTORE IS DISABLED; CANT CREATE SESSION");
//            ApplicationStatus.automaticMaintenanceMode =true;
        }
        else {
//            if (ApplicationStatus.automaticMaintenanceMode) {
//                System.out.println("MAINTENANCE MODE ENDS!");
//                ApplicationStatus.automaticMaintenanceMode =false;
//            }
        }
        System.out.println("checking capabilities took "+(System.currentTimeMillis()-before));
    }
    
    @Override
    public Serializable getAttribute(Request request, String name) {
        Object attribute = delegate.getAttribute(request, name);
        if (attribute==null)
            return null;

        if (attribute instanceof byte[]) {
            byte[] bytes= (byte[]) attribute;
            Object value2 = CompressUtil.uncompress(bytes);
            delegate.setAttribute(request, name, (Serializable)value2);
            return (Serializable)value2;
        }
        else {
            return (Serializable)attribute;
        }
    }

    @Override
    public List<String> getAttributeNames(Request request) {
        return delegate.getAttributeNames(request);
    }

    @Override
    public void setAttribute(Request request, String name, Serializable value) {
        delegate.setAttribute(request,name,value);
    }

    @Override
    public void removeAttribute(Request request, String name) {
        delegate.removeAttribute(request,name);
    }

    @Override
    public void invalidate(Request request) {
        delegate.invalidate(request);
    }

    @Override
    public String getSessionId(Request request, boolean create) {
        return delegate.getSessionId(request,create);
    }

    @Override
    public Session lookup(Request request) {
        logger.info("Looking up a session for the following request: ["+request.getClientUrl()+"]");
        Session session = delegate.lookup(request);
        logger.info("Looked-up session is: ["+((session == null) ? null : session.getId())+"]");
        return session;
    }

    @Override
    public void bind(Request request, Session newSession) {
        logger.info("Binding the following session: ["+newSession.getId()+"]");
        delegate.bind(request,newSession);
    }

    @Override
    public void flushSession(Request request, Session session) {
        delegate.flushSession(request, session);
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

    @Override
    public void registerUnboundListener(UnboundListener listener) {
        delegate.registerUnboundListener(listener);
    }

    @Override
    public void unregisterUnboundListener(UnboundListener listener) {
        delegate.unregisterUnboundListener(listener);
    }

    @Override
    public Set<UnboundListener> getUnboundListener() {
        return delegate.getUnboundListener();
    }
}
