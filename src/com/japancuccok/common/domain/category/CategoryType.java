package com.japancuccok.common.domain.category;

import com.japancuccok.common.wicket.template.ShopBasePage;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.15.
 * Time: 20:20
 */
public enum CategoryType {

    TSHIRT(com.japancuccok.main.tshirt.Tshirt.class, "tshirt"),
    STUFF(com.japancuccok.main.stuff.Stuff.class, "stuff"),
    ACCESSORIES(com.japancuccok.main.accessories.Accessories.class, "accessories"),
    CONTACT(com.japancuccok.main.contact.Contact.class, "contact");

    private Class<? extends ShopBasePage> categoryClass;
    private String name;

    CategoryType(Class<? extends ShopBasePage> categoryClass, String name) {
        this.categoryClass = categoryClass;
        this.name = name;
    }

    public Class<? extends ShopBasePage> getCategoryClass() {
        return categoryClass;
    }

    public String getName() {
        return name;
    }
}
