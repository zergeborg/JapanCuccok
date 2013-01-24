package com.japancuccok.common.wicket.component;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.01.23.
 * Time: 4:50
 */
public class BlockUIDecorator implements IAjaxCallDecorator {

    private static final long serialVersionUID = -6157242720791005789L;

    @Override
    public CharSequence decorateScript(Component component, CharSequence script) {
        return "$.blockUI({message: 'A kérés folyamatban, Kérem várjon...'," +
                          "css: { \n" +
                       "            border: 'none', \n" +
                       "            padding: '15px', \n" +
                       "            backgroundColor: '#000', \n" +
                       "            '-webkit-border-radius': '10px', \n" +
                       "            '-moz-border-radius': '10px', \n" +
                       "            opacity: .5, \n" +
                       "            color: '#fff' \n" +
                       "        } });" + script;
    }

    @Override
    public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
        return "$.unblockUI();" + script;
    }

    @Override
    public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
        return "$.unblockUI();" + script;
    }
}
