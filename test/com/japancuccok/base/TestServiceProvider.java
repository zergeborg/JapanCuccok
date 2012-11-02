package com.japancuccok.base;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.06.18.
 * Time: 20:43
 */
public class TestServiceProvider {

    private static final LocalDatastoreServiceTestConfig testConfig = new
            LocalDatastoreServiceTestConfig();
    private static final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(testConfig)
            .setEnvIsAdmin(true).setEnvIsLoggedIn(true);

    public static void begin() throws IOException, SAXException {
        testHelper.setEnvAppId("fogettijapancuccok");
        testHelper.setEnvAuthDomain("gmail.com");
        testHelper.setUp();
    }

    public static void setUpServlet() throws IOException, SAXException {
        final File webXml = new File("JapanCuccok\\web\\WEB-INF\\web.xml");
        ServletRunner sr = null;
        sr = new ServletRunner(webXml);
        ServletUnitClient client = sr.newClient();
    }

    public static void end() {
        testHelper.tearDown();
    }
}
