package edu.indiana.cs.docsim.htmlproc;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import edu.indiana.cs.docsim.ShingleSet;
import edu.indiana.cs.docsim.Shingle;
import edu.indiana.cs.docsim.ShingleUnit;
import edu.indiana.cs.docsim.ShingleUnitBag;
import edu.indiana.cs.docsim.ShingleTextParser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.net.URL;

public class DocShingleBuilderTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DocShingleBuilderTest.class.getName());

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;

    private ShingleTextParser        shingleTxtParser;
    private DocFilterPipeLineDefault docFilterPipeLine;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocShingleBuilderTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocShingleBuilderTest.class);
    }

    @Override
    public void setUp() {
        shingleTxtParser = new ShingleTextParser();
        try {
            docFilterPipeLine = new DocFilterPipeLineDefault();
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
        }
    }

    public void testShingleTextFromFile() throws Exception {
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        String result = docFilterPipeLine.filter(filename, charset);
        StringBuilder sb = new StringBuilder();
        sb.append("Get data from file '" + filename + "'");
        sb.append("\nResult:\n" + result);

        int shinglesize = 5;
        sb.append("\nFeed the text to shingle builder. shingle size:" + shinglesize);

        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result, shinglesize, charset);

        ShingleSet union = shingleset.union(shingleset2);
        ShingleSet intersect = shingleset.intersect(shingleset2);

        int size1 = union.sizeUnique();
        int size2 = intersect.sizeUnique();
        sb.append("\nCalculate union and intersect of two shingle set " +
                "built from the same text");
        sb.append("\nunion size:" + size1 + ";intersect size:" + size2);

        logger.info(sb.toString());

        assertEquals(size1, size2);
    }

    public void testShingleTextFromURL() throws Exception {
        String strUrl1 = "http://cs.indiana.edu";
        String strUrl2 = "http://www.informatics.indiana.edu/";
        URL url1 = new URL(strUrl1);
        URL url2 = new URL(strUrl2);

        String charset = "UTF-8";

        int shinglesize = 5;
        StringBuilder sb = new StringBuilder();

        String result = docFilterPipeLine.filter(url1);
        sb.append("\nGet data from url(1) \"" + strUrl1 + "\"\n After filtering, result:\n" + result);
        assertNotNull(result);
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);

        result = docFilterPipeLine.filter(url2);
        sb.append("\nGet data from url(2) \"" + strUrl2 + "\"\n After filtering, result is:\n" + result);
        assertNotNull(result);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result, shinglesize, charset);

        ShingleSet union = shingleset.union(shingleset2);
        ShingleSet intersect = shingleset.intersect(shingleset2);

        int size1 = union.sizeUnique();
        int size2 = intersect.sizeUnique();
        sb.append("\nunion size:" + size1 + ";intersect size:" + size2);
        logger.info(sb.toString());
    }
}
