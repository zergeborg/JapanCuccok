package com.japancuccok.common.domain.order;

import com.japancuccok.main.order.PersonalDataPanel;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 16:39
 */
public class PersonalData implements Serializable {
    
    private static final long serialVersionUID = -6164469785915484361L;

    private String name;
    private String emailAddress;
    private String phoneNumber;
    private String settlement;
    private String street;
    private String comment;

    public PersonalData() {
    }

    public String getName() {
        return name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSettlement() {
        return settlement;
    }

    public String getStreet() {
        return street;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalData)) return false;

        PersonalData that = (PersonalData) o;

        if (!emailAddress.equals(that.emailAddress)) return false;
        if (!name.equals(that.name)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!settlement.equals(that.settlement)) return false;
        if (!street.equals(that.street)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + emailAddress.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + settlement.hashCode();
        result = 31 * result + street.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PersonalData{" +
                "name='" + name + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", settlement='" + settlement + '\'' +
                ", street='" + street + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
