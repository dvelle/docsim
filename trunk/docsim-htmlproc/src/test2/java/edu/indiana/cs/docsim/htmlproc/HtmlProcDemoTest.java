package edu.indiana.cs.docsim.htmlproc;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HtmlProcDemoTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(HtmlProcDemoTest.class.getName());
    private HtmlProcDemo procdemo;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HtmlProcDemoTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(HtmlProcDemoTest.class);
    }

    @Override
    public void setUp() {
        procdemo = new HtmlProcDemo();
    }

    public void testDemoMain() {
        procdemo.demoMain();
    }
}
