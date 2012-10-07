package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:36
 */
public class Drink implements CategoryIf, Serializable {

    private static final long serialVersionUID = 3239805252404256573L;

    private final String DRINK = "DRINK";
    private final String DESCRIPTION = "Italok";

    private String name;
    private String description;

    public Drink() {
        this.name = DRINK;
        this.description = DESCRIPTION;
    }

    public Drink(String name, String description) {
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

        Drink drink = (Drink) o;

        if (!description.equals(drink.description)) return false;
        if (!name.equals(drink.name)) return false;

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
        return "Drink{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
