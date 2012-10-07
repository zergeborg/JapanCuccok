package com.japancuccok.common.domain.order;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 17:18
 */
public class BillingData implements Serializable {

    private static final long serialVersionUID = -5443258771297583119L;

    private String name;
    private String phoneNumber;
    private String settlement;
    private String street;
    private String nameOfEnterprise;
    private String taxNumber;

    public BillingData() {
    }

    public String getName() {
        return name;
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

    public String getNameOfEnterprise() {
        return nameOfEnterprise;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillingData)) return false;

        BillingData that = (BillingData) o;

        if (!name.equals(that.name)) return false;
        if (!nameOfEnterprise.equals(that.nameOfEnterprise)) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (!settlement.equals(that.settlement)) return false;
        if (!street.equals(that.street)) return false;
        if (!taxNumber.equals(that.taxNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + settlement.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + nameOfEnterprise.hashCode();
        result = 31 * result + taxNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BillingData{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", settlement='" + settlement + '\'' +
                ", street='" + street + '\'' +
                ", nameOfEnterprise='" + nameOfEnterprise + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                '}';
    }
}
