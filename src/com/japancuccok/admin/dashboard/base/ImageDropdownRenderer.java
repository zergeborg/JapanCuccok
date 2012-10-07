package com.japancuccok.admin.dashboard.base;

import com.japancuccok.common.domain.image.IImage;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.01.
 * Time: 17:43
 */
public class ImageDropdownRenderer implements IChoiceRenderer<IImage>{

    private static final long serialVersionUID = 1331356073453676739L;

    @Override
    public Object getDisplayValue(IImage image) {
        return image.getName();
    }

    @Override
    public String getIdValue(IImage image, int index) {
        return image.getId().toString();
    }
}
