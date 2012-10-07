package com.japancuccok.common.domain.category;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 22:28
 */
public class Food implements CategoryIf, Serializable {

    private static final long serialVersionUID = 217664597693372060L;

    private final String FOOD = "FOOD";
    private final String DESCRIPTION = "Ételek";

    private String name;
    private String description;

    public Food() {
        this.name = FOOD;
        this.description = DESCRIPTION;
    }

    public Food(String name, String description) {
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

        Food food = (Food) o;

        if (!description.equals(food.description)) return false;
        if (!name.equals(food.name)) return false;

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
        return "Food{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
