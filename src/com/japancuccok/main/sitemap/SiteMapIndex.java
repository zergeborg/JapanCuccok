package com.japancuccok.main.sitemap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

public abstract class SiteMapIndex extends WebPage implements Observer {

    private static final long serialVersionUID = 3370062839733642361L;

    private static final String PARAM_SITEMAP_OFFSET = "offset";
    private static final String PARAM_SITEMAP_SOURCEINDEX = "sourceindex";

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n";
    private static final String FOOTER = "</sitemapindex>";
    private static final int MAX_BYTES_SITEMAP = 10485760; //10 megabyte
    private static final int MAX_ENTRIES_PER_SITEMAP = 50000;
    private static final SimpleDateFormat STRIPPED_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private String domain;

    public SiteMapIndex(final PageParameters parameters) {
        super(parameters);
        if (parameters.getAllNamed().size() > 0) {
            final Integer index = parameters.get(PARAM_SITEMAP_OFFSET).toInteger();
            final Integer sourceIndex = parameters.get(PARAM_SITEMAP_SOURCEINDEX).toInteger();

            setResponsePage(new SiteMap() {

                private static final long serialVersionUID = -5573549948041300351L;

                @Override
                protected SiteMapFeed getFeed() {
                    final SiteMapFeed feed = new SiteMapFeed(new IOffsetSiteMapEntryIterable.SiteMapIterable() {
                        public IOffsetSiteMapEntryIterable.SiteMapIterator iterator() {
                            return getDataSources()[sourceIndex].getIterator(index);
                        }
                    });
                    feed.addObserver(SiteMapIndex.this);
                    return feed;
                }
            });
        }
    }

    public String getDomain() {
        if (domain == null) {
            final Request rawRequest = RequestCycle.get().getRequest();
            if (!(rawRequest instanceof WebRequest)) {
                throw new WicketRuntimeException("sitemap.xml generation is only possible for http requests");
            }
            WebRequest wr = (WebRequest) rawRequest;
            domain = "http://" + ((HttpServletRequest)wr.getContainerRequest()).getHeader("host");
        }
        return domain;
    }

    public void update(Observable o, Object arg) {
        //todo feedback loop to adjust block sizes
        if (o instanceof SiteMapFeed) {
            final SiteMapFeed siteMapFeed = (SiteMapFeed) o;
            if ((siteMapFeed.getEntriesWritten() > MAX_ENTRIES_PER_SITEMAP) ||
                    (siteMapFeed.getBytesWritten() > MAX_BYTES_SITEMAP)) {
                throw new IllegalStateException("please adjust block sizes for this sitemap.");
            }
        }
    }

    @Override
    public Markup getAssociatedMarkup()
    {
        return null;
    }

    @Override
    public MarkupType getMarkupType() {
        return new MarkupType("xml", MarkupType.XML_MIME);
    }

    @Override
    public void renderPage() {
        PrintWriter w = new PrintWriter(getResponse().getOutputStream());
        try {
            w.write(HEADER);
            int sourceNumber = 0;
            for (IOffsetSiteMapEntryIterable dataBlock : getDataSources()) {
                for (int i = 0; i < dataBlock.getUpperLimitNumblocks(); i++) {
                    w.append("<sitemap>\n<loc>");
                    final PageParameters params = new PageParameters();
                    //TODO
                    params.add(PARAM_SITEMAP_SOURCEINDEX, String.valueOf(sourceNumber));
                    params.add(PARAM_SITEMAP_OFFSET, String.valueOf(i * dataBlock.getElementsPerSiteMap()));
                    String url =
                            RequestCycle.get().getUrlRenderer().renderFullUrl(
                                    Url.parse(urlFor(getClass(),params).toString()));
                    w.append(StringEscapeUtils.escapeXml(url));
                    w.append("</loc>\n<lastmod>");
                    w.append(STRIPPED_DAY_FORMAT.format(dataBlock.changedDate()));
                    w.append("</lastmod>\n</sitemap>\n");
                }
                sourceNumber++;
            }
            w.write(FOOTER);
            w.flush();
        } finally {
            w.close();
        }
    }

    public abstract IOffsetSiteMapEntryIterable[] getDataSources();
}
