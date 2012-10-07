package com.japancuccok.main.tshirt;

import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.pattern.GeneralPageImageLoadStrategy;
import com.japancuccok.common.wicket.component.CustomPagingNavigator;
import com.japancuccok.common.wicket.component.ProductListView;
import com.japancuccok.common.wicket.template.ShopBasePage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.08.
 * Time: 14:19
 */
public class Tshirt extends ShopBasePage {

    private static final long serialVersionUID = -180583447175381539L;

    public Tshirt() {
        ProductListView productListView = new ProductListView("productRow", new GeneralPageImageLoadStrategy(CategoryType.TSHIRT));
        CustomPagingNavigator upperNavigator = new CustomPagingNavigator("upperNavigator", productListView);
        CustomPagingNavigator lowerNavigator = new CustomPagingNavigator("lowerNavigator", productListView);
        add(upperNavigator);
        add(productListView);
        add(lowerNavigator);
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Nézd meg póló választékunkat!");
    }

}
