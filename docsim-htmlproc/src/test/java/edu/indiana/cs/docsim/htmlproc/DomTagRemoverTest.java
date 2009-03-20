package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DomTagRemoverTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DomTagRemoverTest.class.getName());

    private DomTagRemover tagRemover;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DomTagRemoverTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DomTagRemoverTest.class);
    }

    @Override
    public void setUp() {
        tagRemover = new DomTagRemover();
    }

    public void testTagRemover() throws Exception{
        String xmlStr =
            "<?xml version=\"1.0\"?>\n" +
            "<catalog>\n" +
            "<book id=\"bk101\">\n" +
            "<author>Gambardella, Matthew</author>\n" +
            "<title>XML Developer's Guide</title>\n" +
            "<genre>Computer</genre>\n" +
            "<price>44.95</price>\n" +
            "<publish_date>2000-10-01</publish_date>\n" +
            "<description>An in-depth look at creating applications\n" +
            "with XML.</description>\n" +
            "</book>\n" +
            "</catalog>";

        String resultCorrect1 = "Gambardella, Matthew XML Developer's " +
            "Guide Computer 44.95 2000-10-01 An in-depth look at creating " +
            "applications with XML.";

        String resultCorrect2 = "Gambardella, Matthew\n" +
            "XML Developer's Guide\n" +
            "Computer\n" +
            "44.95\n" +
            "2000-10-01\n" +
            "An in-depth look at creating applications\n" +
            "with XML.";

        String result;
        tagRemover.setSupressWhiteSpaces(true);
        result = tagRemover.tagRemove(xmlStr);
        // logger.info("\"" + result + "\"");
        // logger.info("\"" + resultCorrect1 + "\"");
        assertEquals(result, resultCorrect1);

        tagRemover.setSupressWhiteSpaces(false);
        result = tagRemover.tagRemove(xmlStr);
        assertEquals(result, resultCorrect2);
        // logger.info(result);
        // logger.info(resultCorrect2);
    }

    public void testXMLFile() throws Exception{
        String filename = "sample.xml";
        String encoding = "UTF-8";
        InputStream is = getInputStreamFromFile(filename);
        String xmlStr = IOUtils.toString(is, encoding);
        String result = tagRemover.tagRemove(xmlStr);
        logger.info("result:\n" + result);
    }

    private InputStream getInputStreamFromFile(String fileName)
      throws Exception{
        ClassLoader cl = DomTagRemoverTest.class.getClassLoader();
        return cl.getResourceAsStream(fileName.trim());
    }
}
