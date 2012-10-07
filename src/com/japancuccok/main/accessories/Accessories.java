package com.japancuccok.main.accessories;

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
 * Time: 15:26
 */
public class Accessories extends ShopBasePage {

    private static final long serialVersionUID = -3499335302424838911L;

    public Accessories() {
        ProductListView productListView = new ProductListView("productRow", new GeneralPageImageLoadStrategy(CategoryType.ACCESSORIES));
        CustomPagingNavigator upperNavigator = new CustomPagingNavigator("upperNavigator", productListView);
        CustomPagingNavigator lowerNavigator = new CustomPagingNavigator("lowerNavigator", productListView);
        add(upperNavigator);
        add(productListView);
        add(lowerNavigator);
    }

    @Override
    public Label getHeaderTitle() {
        return new Label("headerTitle", "Nézd meg kiegészítőink választékát!");
    }

}
