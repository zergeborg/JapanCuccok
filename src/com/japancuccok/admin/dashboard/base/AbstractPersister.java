package com.japancuccok.admin.dashboard.base;

import com.japancuccok.db.GenericGaeDAO;
import com.japancuccok.db.GenericGaeDAOFactory;
import org.apache.wicket.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 16:08
 */
public class AbstractPersister<T, R> implements IEventHandler {

    protected IEventHandler successor;
    protected PersistEventHandlerPayload payload;

    protected AbstractPersister(IEventHandler successor, IEventHandlerPayload payload) {
        if(!(payload instanceof PersistEventHandlerPayload)) {
            throw new IllegalArgumentException("Payload must be instance of PersistEventHandlerPayload");
        }
        this.successor = successor;
        this.payload = (PersistEventHandlerPayload) payload;
    }

    protected AbstractPersister(IEventHandler successor) {
        if(successor.getPayload() == null) {
            throw new IllegalArgumentException("Some parameters are missing. Object is in invalid state.");
        }
        this.successor = successor;
        this.payload = (PersistEventHandlerPayload) successor.getPayload();
    }

    @Override
    public PersistEventHandlerPayload getPayload() {
        return payload;
    }

    @Override
    public IEvent handleEvent(IEvent event) {
        IPersistanceEvent<List<T>, R> newPersistanceEvent = null;
        if(event instanceof IPersistanceEvent) {
            if(successor != null) {
                newPersistanceEvent = (IPersistanceEvent<List<T>, R>) successor.handleEvent(event);
            } else {
                newPersistanceEvent = (IPersistanceEvent<List<T>, R>) event;
            }
        }
        return newPersistanceEvent;
    }

    /**
     * Check whether the file already exists, and if so, try to delete it.
     *
     * @param entity the data handleMessage entity to check (e.g. product, image, etc...)
     */
    protected <T> void checkEntityExists(T entity) {
        GenericGaeDAO<T> dao = (GenericGaeDAO<T>) GenericGaeDAOFactory.getInstance(entity.getClass());
        // Try to delete the file
        try {
            dao.delete(entity);
        } catch (IllegalArgumentException e) {
            // Delete was unsuccessful, since the entity is missing
        }
    }

    protected void info(String s) {
        if(payload instanceof PersistEventHandlerPayload) {
            PersistEventHandlerPayload persistPayload = (PersistEventHandlerPayload)payload;
            persistPayload.getComponent().info(s);
        }
    }

}
