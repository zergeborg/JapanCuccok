package com.japancuccok.common.wicket.component;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.validation.IValidator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.29.
 * Time: 14:17
 */
public class UrlTextField extends TextField {

    private static final long serialVersionUID = -838495542809119009L;

    public UrlTextField(String id) {
        super(id, URL.class);
    }

    public UrlTextField(String id, IModel object) {
        super(id, object, URL.class);
    }

    public UrlTextField(String id, IModel object, IValidator urlValidator) {
        super(id, object, URL.class);
        add(urlValidator);
    }

    @Override
    public final IConverter getConverter(Class type) {
        return new IConverter() {

            private static final long serialVersionUID = -2907725021165893203L;

            public Object convertToObject(String value, Locale locale) {
                try {
                    return new URL(value.toString());
                } catch (MalformedURLException e) {
                    throw new ConversionException("'" + value
                            + "' is not a valid URL");
                }
            }
            public String convertToString(Object value, Locale locale) {
                return value != null ? value.toString() : null;
            }
        };
    }
}