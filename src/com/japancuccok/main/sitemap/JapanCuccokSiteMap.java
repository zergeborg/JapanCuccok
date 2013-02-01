package com.japancuccok.main.sitemap;

import com.japancuccok.main.JapanCuccok;
import org.apache.wicket.Session;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Gergely Nagy
 * Date: 2013.02.01.
 * Time: 6:33
 */
public class JapanCuccokSiteMap extends SiteMapIndex {

    private static final long serialVersionUID = 4203192158844206656L;

    private static final int ELEMENTS_PER_BLOCK = 1000;

    Session sess;

    public JapanCuccokSiteMap(final PageParameters parameters) {
        super(parameters);
    }

    public JapanCuccok getJapancuccok() {
        return ((JapanCuccok)getApplication());
    }

    @Override
    public IOffsetSiteMapEntryIterable[] getDataSources() {
        return new IOffsetSiteMapEntryIterable[]{new IOffsetSiteMapEntryIterable() {
            public SiteMapIterator getIterator(final int startIndex) {

                //todo begin db transaction

                return new SiteMapIterator() {

                    int numcalled;
                    Iterator<Map.Entry<String, Class>> mountedPages = getJapancuccok().getMountedPages();

                    public boolean hasNext() {
                        return
                                numcalled <= JapanCuccokSiteMap.ELEMENTS_PER_BLOCK &&
                                        mountedPages.hasNext();
                    }

                    public ISiteMapEntry next() {
                        PageParameters pageParameters = new PageParameters();
                        pageParameters.add("number", numcalled + startIndex);
                        numcalled++;
                        String url =
                                RequestCycle.get().getUrlRenderer().renderFullUrl(
                                     Url.parse(
                                          urlFor(
                                                mountedPages.next().getValue(), pageParameters).toString()));
                        return new BasicSiteMapEntry(url);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException("not possible here..");
                    }

                    public void close() {
                        //todo end db transaction
                        //todo close iterator if instanceof HibernateIterator
                    }
                };
            }

            public int getUpperLimitNumblocks() {
                //todo count number of elements from db
                return (int) Math.ceil(10000 / JapanCuccokSiteMap.ELEMENTS_PER_BLOCK);
            }

            public int getElementsPerSiteMap() {
                return JapanCuccokSiteMap.ELEMENTS_PER_BLOCK;
            }

            public Date changedDate() {
                return new Date(); //todo query db for last change date
            }
        }};
    }
}
