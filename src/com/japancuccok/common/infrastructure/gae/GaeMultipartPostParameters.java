package com.japancuccok.common.infrastructure.gae;

import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.16.
 * Time: 23:20
 */
public class GaeMultipartPostParameters implements IRequestParameters {
    
    private final Map<String, List<StringValue>> rawParameters;
    
    public GaeMultipartPostParameters(Map<String, String[]> rawParameters) {
        this.rawParameters = convertToStringValueMap(rawParameters);
    }

    private Map<String, List<StringValue>> convertToStringValueMap(Map<String,
            String[]> rawParameters) {
        Map<String, List<StringValue>> result = new HashMap<String, List<StringValue>>();
        for(String key : rawParameters.keySet()) {
            List<StringValue> stringValueList = new ArrayList<StringValue>();
            List<String> stringList = Arrays.asList(rawParameters.get(key));
            for(String param : stringList) {
                stringValueList.add(StringValue.valueOf(param));
            }
            result.put(key,stringValueList);
        }
        return result;
    }

    @Override
    public Set<String> getParameterNames() {
        return this.rawParameters.keySet();
    }

    @Override
    public StringValue getParameterValue(String name) {
        List<StringValue> values = rawParameters.get(name);
        if (values == null || values.isEmpty())
        {
            return StringValue.valueOf((String)null);
        }
        else
        {
            return values.iterator().next();
        }    }

    @Override
    public List<StringValue> getParameterValues(String name) {
        List<StringValue> values = rawParameters.get(name);
        if (values != null)
        {
            values = Collections.unmodifiableList(values);
        }
        return values;    }
}
