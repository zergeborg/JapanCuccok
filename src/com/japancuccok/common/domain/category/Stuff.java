package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.19.
 * Time: 00:08
 */
public class Stuff implements CategoryIf, Serializable {

    private static final long serialVersionUID = 990587462339867824L;

    private final String STUFF = "STUFF";
    private final String DESCRIPTION = "Cuccok";

    private String name;
    private String description;

    public Stuff() {
        this.name = STUFF;
        this.description = DESCRIPTION;
    }

    public Stuff(String name, String description) {
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

        Stuff that = (Stuff) o;

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
        return "Stuff{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
