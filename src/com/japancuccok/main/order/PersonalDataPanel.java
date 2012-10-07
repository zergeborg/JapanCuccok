package com.japancuccok.main.order;

import com.japancuccok.common.domain.order.PersonalData;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.22.
 * Time: 16:30
 */
public class PersonalDataPanel extends Panel {

    private static final long serialVersionUID = 9039866785140716340L;
    
    private final PersonalData personalData = new PersonalData();

    public PersonalDataPanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        add(new TextField("name", new PropertyModel(this, "personalData.name")).setRequired(true));
        add(new TextField("emailAddress", new PropertyModel(this, "personalData.emailAddress")).setRequired(true));
        add(new TextField("phoneNumber", new PropertyModel(this, "personalData.phoneNumber")).setRequired(true));
        add(new TextField("settlement", new PropertyModel(this, "personalData.settlement")).setRequired(true));
        add(new TextField("street", new PropertyModel(this, "personalData.street")).setRequired(true));
        add(new TextArea("comment", new PropertyModel(this, "personalData.comment")));
    }

    public void validate() {
        ComponentHierarchyIterator components = visitChildren(FormComponent.class);
        while(components.hasNext()) {
            FormComponent child = (FormComponent) components.next();
            child.validate();
        }
    }

    public void update() {
        ComponentHierarchyIterator components = visitChildren(FormComponent.class);
        while(components.hasNext()) {
            FormComponent child = (FormComponent) components.next();
            child.updateModel();
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

    public PersonalData getPersonalData() {
        return personalData;
    }
}
