package com.japancuccok.common.domain.category;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.28.
 * Time: 14:57
 */
public class CategoryModel<T extends Link> extends LoadableDetachableModel<List<T>> {

    private static final long serialVersionUID = 7148123439486246753L;
    transient private static final Logger logger = Logger.getLogger(CategoryModel.class.getName());

    private Component component;

    public CategoryModel(Component component) {
        this.component = component;
    }

    @Override
    protected List<T> load() {
        List<T> linkList = new ArrayList<T>();
        for(CategoryType category : CategoryType.values()) {
            T link = (T) new BookmarkablePageLink("menuItemAnchor", category.getCategoryClass());
            link.setOutputMarkupId(true);
            Component menuItemImg =
                    new Image("menuItemImg",
                              new ContextRelativeResource(
                                    component.getString(category.getName() + ".icon.filepath")));
            link.add(menuItemImg);
            Component menuItemLi =
                    new WebMarkupContainer("menuItemLi",
                            new Model<Serializable>(component.getString(category.getName() + ".title"))){
                        private static final long serialVersionUID = 7892425745689228696L;

                        /**
                         * {@inheritDoc}
                         */
                        @Override
                        public void onComponentTagBody(final MarkupStream markupStream, final ComponentTag openTag)
                        {
                            logger.info("Replacing menuItemLi component tag body");
                            replaceComponentTagBody(
                                    markupStream,
                                    openTag,
                                    getDefaultModelObjectAsString());
                        }

                    };
            menuItemLi.setOutputMarkupId(true);
            menuItemLi.setMarkupId(component.getString(category.getName() +".htmlMarkup.id"));
            menuItemLi.setEscapeModelStrings(false);
            link.add(menuItemLi);
            linkList.add(link);
        }
        return linkList;
    }
}
