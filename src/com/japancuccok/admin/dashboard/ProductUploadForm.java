package com.japancuccok.admin.dashboard;

import com.japancuccok.admin.dashboard.base.*;
import com.japancuccok.common.domain.category.CategoryType;
import com.japancuccok.common.domain.image.*;
import com.japancuccok.common.wicket.component.UrlTextField;
import com.japancuccok.common.wicket.panel.admin.dashboard.UploadFormPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.12.
 * Time: 23:44
 */
public class ProductUploadForm extends Form<ProductUploadModel> {

    private static final long serialVersionUID = 1369884133215778766L;
    private RepeatingView uploadRepeatingView;

    public ProductUploadForm(final String name, final CompoundPropertyModel<ProductUploadModel> model) {
        super(name, model);

        setOutputMarkupId(true);
        setMultiPart(true);

        add(new TextField<String>("newProductName").setRequired(true));
        add(new TextArea<String>("newProductDescription"));
        ChoiceRenderer renderer = new ChoiceRenderer("name", "categoryClass");
        DropDownChoice<CategoryType> categories = new DropDownChoice<CategoryType>
                ("newProductCategory", Arrays.asList(CategoryType.values()), renderer);
        categories.setRequired(true).setModel(new
                PropertyModel<CategoryType>(getDefaultModelObject(), "newProductCategory"));
        add(categories);
        add(new TextField<String>("newProductPrice").setRequired(true).add(
                new RangeValidator<Integer>(0, Integer.MAX_VALUE)));

        add(new Button("newProductUpload", new StringResourceModel("finished", this, null)));

        uploadRepeatingView = new RepeatingView("uploadRepeatingView");
        final WebMarkupContainer dummyRepeaterContainer = new WebMarkupContainer("dummyRepeaterContainer");
        dummyRepeaterContainer.add(uploadRepeatingView);
        dummyRepeaterContainer.setVisible(true);
        dummyRepeaterContainer.setOutputMarkupId(true);
        add(dummyRepeaterContainer);

        AjaxButton newImageButton = createAjaxButton(model, dummyRepeaterContainer);
        newImageButton.setOutputMarkupId(true);
        newImageButton.setDefaultFormProcessing(false);
        add(newImageButton);

    }

    private AjaxButton createAjaxButton(CompoundPropertyModel<ProductUploadModel> uploadPanelModel,
                                        WebMarkupContainer dummyRepeaterContainer) {
        IModel<String> buttonModel = new StringResourceModel("newImage",this,null);
        return new NewImageAjaxButton("newImageDiv", buttonModel, uploadRepeatingView, uploadPanelModel, dummyRepeaterContainer);
    }

    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    protected void onSubmit() {
        ProductUploadModel uploadModel = ((ProductUploadModel) getDefaultModelObject());

        List<IImage> images = new ArrayList<IImage>();
        IEventHandlerPayload payload = new PersistEventHandlerPayload(this, uploadModel);
        IEventHandler binaryImagePersister =
                new BinaryImagePersister(null, payload);
        IEventHandler urlImagePersister =
                new UrlImagePersister(binaryImagePersister);
        IEventHandler productPersister =
                new ProductPersister(urlImagePersister);
        IEvent productStoreEvent = productPersister.handleEvent(new PersistanceEvent(images));
    }

    @Override
    protected void onError() {
        System.out.println("Error in ProductUploadForm!");
    }

    @Override
    protected void onValidate() {
        boolean imageFound = false;
        boolean urlFound = false;
        for(Component component : uploadRepeatingView) {
            UploadFormPanel formPanel = (UploadFormPanel)component;
            List<FileUpload> uploads = formPanel.getImageUpload().getFileUploads();
            if(uploads != null && uploads.size() > 0) {
                imageFound = true;
            }
            UrlTextField urlTextField = formPanel.getUrls();
            if(urlTextField.getConvertedInput() != null && !urlTextField.getConvertedInput().equals("")) {
                urlFound = true;
            }
        }
        if(!imageFound && !urlFound) {
            error(getString("uploadAndUrlIsMissing"));
        }
    }

}
