package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.19.
 * Time: 00:10
 */
public class Accessories implements CategoryIf, Serializable {

    private static final long serialVersionUID = 990587462339867824L;

    private final String ACCESSORIES = "ACCESSORIES";
    private final String DESCRIPTION = "Kiegészítők";

    private String name;
    private String description;

    public Accessories() {
        this.name = ACCESSORIES;
        this.description = DESCRIPTION;
    }

    public Accessories(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;

        Accessories that = (Accessories) o;

        if (!description.equals(that.description)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Accessories{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
