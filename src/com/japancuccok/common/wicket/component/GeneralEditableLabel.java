package com.japancuccok.common.wicket.component;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.03.
 * Time: 22:40
 */
public class GeneralEditableLabel<T> extends AjaxEditableLabel {

    private static final long serialVersionUID = -415660217135059100L;

    private FeedbackPanel feedbackPanel;
    private T modelObject;

    public GeneralEditableLabel(String id, FeedbackPanel feedbackPanel, IModel<T> model) {
        super(id, model);
        feedbackPanel.setOutputMarkupId(true);
        this.feedbackPanel = feedbackPanel;
    }

    @Override
    protected void onError(AjaxRequestTarget target) {
        super.onError(target);
        target.add(feedbackPanel);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target) {
        super.onSubmit(target);
        target.add(feedbackPanel);
        modelObject = (T) getDefaultModelObject();
    }
}