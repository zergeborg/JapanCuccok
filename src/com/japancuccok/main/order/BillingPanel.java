package com.japancuccok.main.order;

import com.japancuccok.common.domain.order.BillingData;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 16:31
 */
public class BillingPanel extends Panel {

    private static final long serialVersionUID = -2592773794559469931L;
    
    private final BillingData billingData = new BillingData();
    
    private TextField name;
    private TextField phoneNumber;
    private TextField settlement;
    private TextField street;
    private TextField nameOfEnterprise;
    private TextField taxNumber;

    public BillingPanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new TextField("name", new PropertyModel(this, "billingData.name")).setRequired(true));
        add(new TextField("phoneNumber", new PropertyModel(this, "billingData.phoneNumber")).setRequired(true));
        add(new TextField("settlement", new PropertyModel(this, "billingData.settlement")).setRequired(true));
        add(new TextField("street", new PropertyModel(this, "billingData.street")).setRequired(true));
        add(new TextField("nameOfEnterprise", new PropertyModel(this, "billingData.nameOfEnterprise")).setRequired(true));
        add(new TextField("taxNumber", new PropertyModel(this, "billingData.taxNumber")).setRequired(true));
    }

    public void validate() {
        if(!isEmpty()) {
            ComponentHierarchyIterator components = visitChildren(FormComponent.class);
            while(components.hasNext()) {
                FormComponent child = (FormComponent) components.next();
                child.validate();
            }
        }
    }

    public void update() {
        if(!isEmpty()) {
            ComponentHierarchyIterator components = visitChildren(FormComponent.class);
            while(components.hasNext()) {
                FormComponent child = (FormComponent) components.next();
                child.updateModel();
            }
        }
    }

    public boolean isValid() {
        ComponentHierarchyIterator components = visitChildren(FormComponent.class);
        boolean valid = true;
        while(components.hasNext()) {
            FormComponent child = (FormComponent) components.next();
            valid = valid && child.isValid();
        }
        return valid;
    }

    private boolean isEmpty() {
        ComponentHierarchyIterator components = visitChildren(FormComponent.class);
        boolean empty = true;
        while(components.hasNext()) {
            FormComponent child = (FormComponent) components.next();
            empty = empty && (child.getRawInput() == null
                                || child.getRawInput().equals(""));
        }
        return empty;
    }

}
