package com.japancuccok.common.domain.contact;

import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

import static com.japancuccok.db.DAOService.contactDao;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.16.
 * Time: 0:28
 */
public class ContactModel extends LoadableDetachableModel<String> {

    private static final long serialVersionUID = 3838448313605792229L;

    private Contact actualContact;

    @Override
    protected String load() {
        List<Contact> contacts = contactDao.list();
        if(contacts != null && contacts.size() > 0) {
            actualContact = contacts.get(0);
            return actualContact.getContactDetails();
        }
        return null;
    }

    public Contact getActualContact() {
        return actualContact;
    }
}

