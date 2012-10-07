package com.japancuccok.common.domain.contact;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.08.15.
 * Time: 22:33
 */
@Entity
@Cache
public class Contact implements Serializable {

    private static final long serialVersionUID = -4349896703904190789L;

    @Id
    Long id;
    
    private String contactDetails;

    public Contact() {
    }

    public Contact(String contactDetails) {
        if(contactDetails == null || contactDetails.equals("")) {
            throw new IllegalArgumentException("[contactDetails] must not be neither null nor empty");
        }
        this.contactDetails = contactDetails;
    }

    public Long getId() {
        return id;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (!contactDetails.equals(contact.contactDetails)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return contactDetails.hashCode();
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", contactDetails='" + contactDetails + '\'' +
                '}';
    }
}
