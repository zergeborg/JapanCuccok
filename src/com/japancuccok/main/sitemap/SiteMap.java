package com.japancuccok.main.sitemap;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.02.01.
 * Time: 5:39
 */
public abstract class SiteMap extends WebPage {

    private static final long serialVersionUID = 5160105438342599012L;

    @Override
    public MarkupType getMarkupType() {
        return new MarkupType("xml", MarkupType.XML_MIME);
    }

    @Override
    protected void onRender() {
        PrintWriter writer = new PrintWriter(getResponse().getOutputStream());
        try {
            getFeed().writeFeed(writer);
            writer.flush();
        } catch (IOException e) {
            throw new WicketRuntimeException("unable to construct sitemap.xml for request: " + ((HttpServletRequest)((WebRequest) getRequest()).getContainerRequest()).getRemoteAddr(), e);
        } finally {
            writer.close();
        }
    }

    protected abstract SiteMapFeed getFeed();

}
