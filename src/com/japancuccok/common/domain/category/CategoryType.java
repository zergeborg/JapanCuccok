package com.japancuccok.common.domain.category;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.15.
 * Time: 20:20
 */
public enum CategoryType {
    TSHIRT(Tshirt.class, "Pólók"),
    STUFF(Stuff.class, "Cuccok"),
    ACCESSORIES(Accessories.class, "Kiegészítők");
    /*CLOTHES(Clothes.class, "Ruhák"),
    DRINK(Drink.class, "Italok"),
    FOOD(Food.class, "Ételek"),
    GADGET(Gadget.class, "Bigyóka"),
    PILLOW(Pillow.class, "Párnák");*/

    private Class<? extends CategoryIf> categoryClass;
    private String name;

    CategoryType(Class<? extends CategoryIf> categoryClass, String name) {
        this.categoryClass = categoryClass;
        this.name = name;
    }
}
