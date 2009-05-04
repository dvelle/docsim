package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DocFilterPipeLineDefaultTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DocFilterPipeLineDefaultTest.class.getName());

    private DocFilterPipeLineDefault docFilterPipeLine;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocFilterPipeLineDefaultTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocFilterPipeLineDefaultTest.class);
    }

    @Override
    public void setUp() {
        try {
            docFilterPipeLine = new DocFilterPipeLineDefault();
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
        }
    }

    public void testPipeLineLocal() throws Exception{
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        String result = docFilterPipeLine.filter(filename, charset);
        logger.info("Get data from file \"" + filename + "\" result:\n" + result);
    }

    public void testPipeLineRemote() throws Exception{
        // String strUrl = "http://cs.indiana.edu";
        String strUrl = "http://www.informatics.indiana.edu/";
        String charset = "UTF-8";
        URL url = new URL(strUrl);
        String result = docFilterPipeLine.filter(url);
        logger.info("Get page from " + strUrl + ".\n Filtering result:\n" + result);
    }
}

