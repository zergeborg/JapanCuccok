package com.japancuccok.common.wicket.panel.admin.products;

import com.japancuccok.common.domain.image.BaseImage;
import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.events.ImageItemDelete;
import com.japancuccok.common.wicket.component.BlockUIDecorator;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.panel.Panel;

import static com.japancuccok.db.DAOService.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.26.
 * Time: 22:33
 */
public class DatastoreImagePanel<T extends BinaryImage> extends ImagePanel<T> {

    private static final long serialVersionUID = -4018188384625829441L;

    public DatastoreImagePanel(String id, T image) {
        super(id, image);
        setOutputMarkupPlaceholderTag(true);
    }

    @Override
    protected Panel getOptionsPanel() {
        return new ImageOptionsPanel("optionsPanel", getDefaultModel());
    }

    @Override
    protected Component getDeleteLink() {
        IndicatingAjaxFallbackLink deleteLink = new IndicatingAjaxFallbackLink("deleteLink", getDefaultModel()) {

            private static final long serialVersionUID = -8054686106943046581L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                baseImageDao.delete((BaseImage) getImage());
                send(getPage(), Broadcast.BREADTH, new ImageItemDelete(target, DatastoreImagePanel.this));
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator()
            {
                return new BlockUIDecorator();
            }

        };
        deleteLink.setOutputMarkupId(true);
        deleteLink.setOutputMarkupPlaceholderTag(true);
        deleteLink.setVersioned(true);
        return deleteLink;
    }
}
