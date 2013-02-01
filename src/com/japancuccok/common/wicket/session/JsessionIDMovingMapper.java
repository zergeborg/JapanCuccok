package com.japancuccok.common.wicket.session;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The point of the class is that we don't want the JSESSIONID to be the first parameter among the
 * GET parameters, because it will make the mounted resource mechanism defined in {@link 
 * com.japancuccok.main.JapanCuccok#mountResources} malfunctioning.
 * 
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.02.01.
 * Time: 4:41
 */
public class JsessionIDMovingMapper  implements IRequestMapper {

    private static final Logger log = LoggerFactory.getLogger(JsessionIDMovingMapper.class);

    private final IRequestMapper wrappedMapper;

    /**
     * Construct.
     *
     * @param wrappedMapper
     *            the non-crypted request mapper
     */
    public JsessionIDMovingMapper(final IRequestMapper wrappedMapper)
    {
        this.wrappedMapper = Args.notNull(wrappedMapper, "wrappedMapper");
    }

    @Override
    public IRequestHandler mapRequest(Request request) {
        Url url = moveJsessionID(request, request.getUrl());

        if (url == null)
        {
            return null;
        }

        return wrappedMapper.mapRequest(request.cloneWithUrl(url));
    }

    @Override
    public int getCompatibilityScore(Request request) {
        return 0;
    }

    @Override
    public Url mapHandler(IRequestHandler requestHandler) {
        // We don't have to care about the URL in the response, because the point of moving
        // JSESSSIONID is taking care that the ID remains at the end of the URL permanently
        return wrappedMapper.mapHandler(requestHandler);
    }

    private Url moveJsessionID(Request request, Url originalUrl) {
        if (originalUrl.getQueryParameters().isEmpty())
        {
            return originalUrl;
        }

        List<Url.QueryParameter> queryParameters = originalUrl.getQueryParameters();
        if (queryParameters.size() < 1)
        {
            return null;
        }

        Url url = new Url(request.getCharset());
        try
        {
            Url.QueryParameter queryParameter = queryParameters.get(0);
            if (Strings.isEmpty(queryParameter.toString(request.getCharset())))
            {
                return null;
            }

            Url.QueryParameter jsessionId = null;
            for(Url.QueryParameter queryParam : queryParameters) {
                if(queryParam.toString(request.getCharset()).toLowerCase().contains("jsessionid")) {
                    jsessionId = queryParam;
                    continue;
                }
                url.getQueryParameters().add(queryParam);
            }
            if(jsessionId != null) {
                url.getQueryParameters().add(jsessionId);
            }
            url.getSegments().addAll(originalUrl.getSegments());

        } catch (Exception e) {
            log.error("Error during moving JSESSIONID to the end of the URL", e);
            url = null;
        }

        return url;
    }
}
