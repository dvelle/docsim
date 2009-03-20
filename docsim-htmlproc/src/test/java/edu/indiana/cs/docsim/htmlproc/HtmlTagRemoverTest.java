package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HtmlTagRemoverTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(HtmlTagRemoverTest.class.getName());

    private HtmlTagRemover tagRemover;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public HtmlTagRemoverTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(HtmlTagRemoverTest.class);
    }

    @Override
    public void setUp() {
        tagRemover = new HtmlTagRemover();
    }

    public void testTagRemoverFromFile() throws Exception{
        String filename = "augmented_page.html";
        String result = tagRemover.tagRemoveFromFile(filename);
        logger.info("result:\n" + result);
    }
    public void testTagRemoverFromURL() throws Exception{
        String urlstr = "http://cs.indiana.edu";
        URL url = new URL(urlstr);
        String result = tagRemover.tagRemoveFromURL(url);
        logger.info("result:\n" + result);
    }
}

