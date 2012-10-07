package com.japancuccok.admin.dashboard.base;

import com.japancuccok.common.domain.image.IImage;
import com.japancuccok.common.domain.product.Product;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.27.
 * Time: 20:05
 */
public class ImageUpdater extends AjaxFormComponentUpdatingBehavior {

    private static final long serialVersionUID = 8041198261942168783L;
    private DropDownChoice<IImage> categories;
    private Panel showSpan;
    private Link<Void> showLink;
    private ListItem<Product> listItem;

    /**
     * Construct.
     *
     * @param event event to trigger this behavior
     * @param categories
     * @param showSpan
     * @param showLink
     * @param listItem
     */
    public ImageUpdater(String event, DropDownChoice<IImage> categories, Panel showSpan, Link<Void> showLink, ListItem<Product> listItem) {
        super(event);
        this.categories = categories;
        this.showSpan = showSpan;
        this.showLink = showLink;
        this.listItem = listItem;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        if(target != null) {
            DropDownChoice categoryDropDown = (DropDownChoice)getFormComponent();
            if(categoryDropDown.getConvertedInput() == null) {
                showLink.setVisible(false);
            } else {
                showLink.setVisible(true);
                IImage selectedImage = (IImage) categories.getDefaultModelObject();
                WebComponent wicketImage = ImageUpdateHelper.getWicketImage(selectedImage, null);
                showSpan.addOrReplace(wicketImage);
                target.add(showSpan);
            }
            target.add(showLink);
            target.add(categories);
            String javaScript = new StringBuilder()
                    .append("$(function(){")
                    .append("$('."+showLink.getMarkupId()+"').styledpopup();")
                    .append("});").toString();
            target.appendJavaScript(javaScript);
        }
    }
}
