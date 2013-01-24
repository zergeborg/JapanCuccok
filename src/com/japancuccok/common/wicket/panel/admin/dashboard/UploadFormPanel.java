package com.japancuccok.common.wicket.panel.admin.dashboard;

import com.japancuccok.admin.dashboard.base.ProductUploadModel;
import com.japancuccok.common.domain.image.ImageOptions;
import com.japancuccok.common.events.ImageUploadItemDelete;
import com.japancuccok.common.wicket.component.BlockUIDecorator;
import com.japancuccok.common.wicket.component.UrlTextField;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.02.
 * Time: 22:07
 */
public class UploadFormPanel extends Panel {

    private static final long serialVersionUID = 7354151268630583877L;
    private List<IModel> modelList = new ArrayList<IModel>();
    private ImageUploadPanel imageUploadPanel;
    private ImageURLPanel imageURLPanel;

    public UploadFormPanel(final String id, String currentUploadNr,
                           CompoundPropertyModel<ProductUploadModel> model) {
        super(id, model);
        initDefaultModel();
        initImageUrlPanel(currentUploadNr, model);
        initImageUploadPanel(currentUploadNr, model);
        bindModels(currentUploadNr, model);
        addUploadPanel(currentUploadNr);
    }

    private void initImageUploadPanel(String currentUploadNr, CompoundPropertyModel<ProductUploadModel> model) {
        imageUploadPanel = new ImageUploadPanel("newImageUpload",currentUploadNr, model);
        imageUploadPanel.setOutputMarkupId(true);
        imageUploadPanel.setOutputMarkupPlaceholderTag(true);
        imageUploadPanel.setVersioned(true);
        imageUploadPanel.setVisible(false);
        imageUploadPanel.setMarkupId(imageUploadPanel.getMarkupId() + currentUploadNr);
    }

    private void initImageUrlPanel(String currentUploadNr, CompoundPropertyModel<ProductUploadModel> model) {
        imageURLPanel = new ImageURLPanel("newImageLink",currentUploadNr, model);
        imageURLPanel.setOutputMarkupId(true);
        imageURLPanel.setOutputMarkupPlaceholderTag(true);
        imageURLPanel.setVersioned(true);
        imageURLPanel.setMarkupId(imageURLPanel.getMarkupId() + currentUploadNr);
    }

    private void bindModels(String currentUploadNr, CompoundPropertyModel<ProductUploadModel> model) {
        IModel newProductDashboardModel = model.bind("imageOptions." + currentUploadNr + ".productDashboard");
        modelList.add(newProductDashboardModel);
        IModel newProductGeneralPageModel = model.bind("imageOptions."+currentUploadNr+".productGeneralPage");
        modelList.add(newProductGeneralPageModel);
    }

    private void addUploadPanel(String currentUploadNr) {
        WebMarkupContainer uploadPanel = new WebMarkupContainer("productUploadPanel", new Model());
        uploadPanel.add(getDeleteLink(currentUploadNr));
        uploadPanel.add(new CheckBox("newProductDashboard", modelList.get(0)));
        uploadPanel.add(new CheckBox("newProductGeneralPage", modelList.get(1)));
        uploadPanel.add(getSelectorGroup(currentUploadNr));
        uploadPanel.add(imageURLPanel);
        uploadPanel.add(imageUploadPanel);
        uploadPanel.setVisible(true);
        uploadPanel.setOutputMarkupPlaceholderTag(true);
        add(uploadPanel);
    }

    private void initDefaultModel() {
        ProductUploadModel defaultModelObject = (ProductUploadModel) getDefaultModelObject();
        defaultModelObject.getImageOptions().add(new ImageOptions(0, 0, null, false, false));
    }

    private RadioGroup getSelectorGroup(String currentUploadNr) {
        RadioGroup imgTypeSelector = new RadioGroup("selector");
        imgTypeSelector.setOutputMarkupId(true);
        IModel urlModel = new Model(new StringResourceModel("urlImageType", this, null));
        Radio radio1 = new Radio("1", urlModel);
        radio1.setMarkupId(radio1.getMarkupId()+currentUploadNr);
        imgTypeSelector.add(initAjaxBehavior(radio1));
        IModel fileUploadModel = new Model(new StringResourceModel("datastoreImageType", this, null));
        Radio radio2 = new Radio("2", fileUploadModel);
        radio2.setMarkupId(radio2.getMarkupId()+currentUploadNr);
        imgTypeSelector.add(initAjaxBehavior(radio2));
        imgTypeSelector.setModel(urlModel);
        return imgTypeSelector;
    }

    private Component getDeleteLink(String currentUploadNr) {
        IndicatingAjaxFallbackLink deleteLink = new IndicatingAjaxFallbackLink("deleteLink", getDefaultModel()) {

            private static final long serialVersionUID = -8054686106943046581L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                send(getPage(), Broadcast.BREADTH, new ImageUploadItemDelete(target, UploadFormPanel.this));
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator()
            {
                return new BlockUIDecorator();
            }

        };
        deleteLink.setOutputMarkupId(true);
        deleteLink.setVersioned(true);
        deleteLink.setMarkupId("deleteLink" + currentUploadNr);
        return deleteLink;
    }
    
    private Radio initAjaxBehavior(final Radio radio) {
        radio.add(new AjaxEventBehavior("onchange") {
            private static final long serialVersionUID = 1661986416753740195L;

            @Override
            protected void onEvent(AjaxRequestTarget target) {
                imageUploadPanel.setVisible(!imageUploadPanel.isVisible());
                imageURLPanel.setVisible(!imageURLPanel.isVisible());
                target.add(imageUploadPanel);
                target.add(imageURLPanel);
            }
        });
        return radio;
    }

    public FileUploadField getImageUpload() {
        return imageUploadPanel.getImageUpload();
    }

    public UrlTextField getUrls() {
        return imageURLPanel.getUrls();
    }

}
