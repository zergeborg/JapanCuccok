package com.japancuccok.main.dashboard;

import org.apache.wicket.model.PropertyModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.10.18.
 * Time: 0:22
 */
public class DescriptionPropertyModel extends PropertyModel<String> {
    
    private static final long serialVersionUID = -4515800892702725151L;
    
    public static final String SPAN_OPEN_TAG_BEGIN = "<span class=\"";
    public static final String SPAN_OPEN_TAG_END = "\">";
    public static final String SPAN_CLOSE_TAG = "</span>";
    public static final String FIRST_LETTER_CSS_CLASS = "letter";

    /**
     * Construct with a wrapped (IModel) or unwrapped (non-IModel) object and a property expression
     * that works on the given model.
     *
     * @param modelObject The model object, which may or may not implement IModel
     * @param expression  Property expression for property access
     */
    public DescriptionPropertyModel(Object modelObject, String expression) {
        super(modelObject, expression);
    }
    
    @Override
    public String getObject() {
        String description = super.getObject();
        if(description != null && !description.equals("")) {
            description = description.trim();
            String firstLetter = description.substring(0,1);
            String theRestOfTheText = description.substring(1,description.length());
            firstLetter = firstLetter.toUpperCase();
//            description = getNewDescription(firstLetter, theRestOfTheText);
        }
        return description;
    }

    private String getNewDescription(String firstLetter, String theRestOfTheText) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(SPAN_OPEN_TAG_BEGIN);
        strBuilder.append(FIRST_LETTER_CSS_CLASS);
        strBuilder.append(" ");
        strBuilder.append(firstLetter);
        strBuilder.append(SPAN_OPEN_TAG_END);
        strBuilder.append(firstLetter);
        strBuilder.append(SPAN_CLOSE_TAG);
        strBuilder.append(theRestOfTheText);
        return strBuilder.toString();
    }

}
