package com.japancuccok.admin.products;

import com.japancuccok.common.domain.image.BinaryImage;
import com.japancuccok.common.events.ImageItemDelete;
import com.japancuccok.common.pattern.BinaryImageLoadStrategy;
import com.japancuccok.common.wicket.panel.admin.products.DatastoreImagePanel;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 20:10
 */
public class DataStoreImagePanelRepeater extends RefreshingView<BinaryImage> {

    private static final long serialVersionUID = 7219541336013731848L;

    public DataStoreImagePanelRepeater(String id) {
        super(id);
    }

    public DataStoreImagePanelRepeater(String id, LoadableDetachableModel<?> model) {
        super(id, model);
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        // check if this is a item delete update event and if so repaint self
        if (event.getPayload() instanceof ImageItemDelete)
        {
            ImageItemDelete update = (ImageItemDelete)event.getPayload();
            remove(update.getImagePanel());
            update.getTarget().add(this.getParent());
        }
    }

    @Override
    protected Iterator<IModel<BinaryImage>> getItemModels() {
        BinaryImageLoadStrategy imageModel = (BinaryImageLoadStrategy) getDefaultModel();
        List<BinaryImage> imageList = imageModel.load();
        List<IModel<BinaryImage>> models = new ArrayList<IModel<BinaryImage>>();
        for(BinaryImage binaryImage : imageList) {
            models.add(new Model<BinaryImage>(binaryImage));
        }
        return models.iterator();
    }

    @Override
    protected void populateItem(Item<BinaryImage> item) {
        item.setRenderBodyOnly(true);
        BinaryImage image = item.getModelObject();
        add(new DatastoreImagePanel("imagePanel"+item.getIndex(), image));
    }
}
