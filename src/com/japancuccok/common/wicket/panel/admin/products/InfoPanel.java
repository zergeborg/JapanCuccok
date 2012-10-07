package com.japancuccok.common.wicket.panel.admin.products;

import com.japancuccok.common.domain.product.Product;
import com.japancuccok.common.wicket.component.GeneralEditableLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.03.
 * Time: 23:07
 */
public class InfoPanel extends Panel {

    private static final long serialVersionUID = -901786171160542238L;
    private final FeedbackPanel feedbackPanel;

    public InfoPanel(String id, Product productToEdit,FeedbackPanel feedbackPanel) {
        super(id, new CompoundPropertyModel<Product>(productToEdit));
        this.feedbackPanel = feedbackPanel;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new GeneralEditableLabel("name", feedbackPanel, null));
        add(new Label("category"));
        add(new GeneralEditableLabel("price", feedbackPanel, null));
        add(new GeneralEditableLabel("description", feedbackPanel, null));
    }
}
