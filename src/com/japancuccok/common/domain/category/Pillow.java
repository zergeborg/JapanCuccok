package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:14
 */
public class Pillow implements CategoryIf, Serializable {

    private static final long serialVersionUID = 2270684216362121746L;

    private final String PILLOW = "PILLOW";
    private final String DESCRIPTION = "Párnák";

    private String name;
    private String description;

    public Pillow() {
        this.name = PILLOW;
        this.description = DESCRIPTION;
    }

    public Pillow(String name, String description) {
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

        Pillow that = (Pillow) o;

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
