package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:38
 */
public class Gadget implements CategoryIf, Serializable {

    private static final long serialVersionUID = -9008567918402121576L;

    private final String GADGET = "GADGET";
    private final String DESCRIPTION = "Bigyóka";

    private String name;
    private String description;

    public Gadget() {
        this.name = GADGET;
        this.description = DESCRIPTION;
    }

    public Gadget(String name, String description) {
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

        Gadget gadget = (Gadget) o;

        if (!description.equals(gadget.description)) return false;
        if (!name.equals(gadget.name)) return false;

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
        return "Gadget{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
