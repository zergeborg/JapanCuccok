package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:22
 */
public class Clothes implements CategoryIf, Serializable {

    private static final long serialVersionUID = -1311004091149571715L;
    
    private final String CLOTHES = "CLOTHES";
    private final String DESCRIPTION = "Ruhák";
    
    private String name;
    private String description;

    public Clothes() {
        this.name = CLOTHES;
        this.description = DESCRIPTION;
    }

    public Clothes(String name, String description) {
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

        Clothes that = (Clothes) o;

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
