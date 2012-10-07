package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:11
 */
public class Tshirt implements CategoryIf, Serializable {

    private static final long serialVersionUID = 990587462339867824L;

    private final String TSHIRT = "TSHIRT";
    private final String DESCRIPTION = "Pólók";

    private String name;
    private String description;

    public Tshirt() {
        this.name = TSHIRT;
        this.description = DESCRIPTION;
    }

    public Tshirt(String name, String description) {
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

        Tshirt that = (Tshirt) o;

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
        return "Tshirt{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
