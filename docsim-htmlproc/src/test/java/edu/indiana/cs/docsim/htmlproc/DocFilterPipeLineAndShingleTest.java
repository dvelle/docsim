package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.Shingle;
import edu.indiana.cs.docsim.ShingleSet;
import edu.indiana.cs.docsim.ShingleTextParser;
import edu.indiana.cs.docsim.ShingleUnit;
import edu.indiana.cs.docsim.ShingleUnitBag;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DocFilterPipeLineAndShingleTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DocFilterPipeLineAndShingleTest.class.getName());

    private DocFilterPipeLineDefault docFilterPipeLine;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocFilterPipeLineAndShingleTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocFilterPipeLineAndShingleTest.class);
    }

    @Override
    public void setUp() {
    }

    public void testPipeLineAndShingleLocal() throws Exception {
        String filename = "res://augmented_page.html";
        // First apply pipe line of filters
        String result = filter(filename);
        assertNotNull(result);

        ShingleTextParser shingleTxtParser = new ShingleTextParser();

        // build shingle set
        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        int shinglesize = 5;
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        int size = shingleset.sizeUnique();
        sb.append("\nShingle size: " + shinglesize);
        sb.append("\nSize of shingle set: " + size);
        logger.info(sb.toString());
    }

    public String filter(String src) throws Exception {
        final DomTagListener listenerHeading = new DomTagListenerHeadingTags();
        final DomTagListener listenerTitle = new DomTagListenerTitleTag();
        final DomTagListener listenerEmph = new DomTagListenerEmphTags();

        final DomTagListenerRegistry listenerReg = new DomTagListenerRegistry();
        final HtmlTagListenerFilter listenerFilter = new HtmlTagListenerFilter(listenerReg);

        listenerReg.register(listenerHeading);
        listenerReg.register(listenerEmph);
        listenerReg.register(listenerTitle);

        try {
            docFilterPipeLine = new DocFilterPipeLineDefault() {
                protected void beforeInit() {
                    try {
                        pipeline.add(listenerFilter);
                    } catch(Exception ex) {
                    }
                }
            };
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
            throw ex;
        }


        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        String result = null;
        if (src.startsWith("res://")) {
            String filename = src;
            result = docFilterPipeLine.filter(filename, charset);
            sb.append("\nGet data from file \"" + filename + "\" result:\n" + result);
        } else {
            try {
                URL url = new URL(src);
                result = docFilterPipeLine.filter(url);
                sb.append("\nGet data from url \"" + url + "\" result:\n" + result);
            } catch (Exception e) {
                String filename = src;
                result = docFilterPipeLine.filter(filename, charset);
                sb.append("\nGet data from file \"" + filename + "\" result:\n" + result);
            }
        }


        Object dataobj = listenerHeading.getData();
        String output = "Heading listener:\n" + seralizeList(dataobj);
        sb.append("\n" + output);

        dataobj = listenerTitle.getData();
        output = "Title listener:\n" + seralizeList(dataobj);
        sb.append("\n" + output);

        dataobj = listenerEmph.getData();
        output = "Emph listener:\n" + seralizeList(dataobj);
        sb.append("\n" + output);

        logger.info(sb.toString());
        return result;
    }

    public void testPipeLineAndShingleRemote2() throws Exception {
        ShingleTextParser shingleTxtParser = new ShingleTextParser();

        String strUrl1 = "http://cs.indiana.edu";
        String strUrl2 = "http://www.informatics.indiana.edu/";
        String result1 = filter(strUrl1);
        String result2 = filter(strUrl2);

        // build shingle set
        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        int shinglesize = 5;
        sb.append("\nShingle size: " + shinglesize);

        ShingleSet shingleset1 = shingleTxtParser.parseText(result1, shinglesize, charset);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result2, shinglesize, charset);
        ShingleSet union = shingleset1.union(shingleset2);
        ShingleSet intersect = shingleset1.intersect(shingleset2);

        int size1 = shingleset1.sizeUnique();
        int size2 = shingleset2.sizeUnique();
        int sizeunion = union.sizeUnique();
        int sizeintersect = intersect.sizeUnique();

        sb.append("\nGet data from URL(1) " + strUrl1);
        sb.append("\nSize of shingle set: " + size1);
        sb.append("\nGet data from URL(2) " + strUrl2);
        sb.append("\nSize of shingle set: " + size2);
        sb.append("\nunion size:" + sizeunion +
                ";intersect size:" + sizeintersect);

        logger.info(sb.toString());
    }

    public void testPipeLineAndShingleRemote() throws Exception {
        // String strUrl = "http://cs.indiana.edu";
        String strUrl = "http://www.informatics.indiana.edu/";
        String result = filter(strUrl);
        ShingleTextParser shingleTxtParser = new ShingleTextParser();

        // build shingle set
        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        int shinglesize = 5;
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        int size = shingleset.sizeUnique();
        sb.append("\nShingle size: " + shinglesize);
        sb.append("\nSize of shingle set: " + size);
        logger.info(sb.toString());
    }

    private String seralizeList(Object dataobj) {
        StringBuilder sb = new StringBuilder();
        if (dataobj instanceof List) {
            List<String> datals = (List<String>)dataobj;
            for (Iterator<String> it = datals.iterator(); it.hasNext();) {
                sb.append(it.next() + "\n");
            }
        } else {
            logger.severe("A list is expected!!!");
            return "";
        }
        return sb.toString();
    }
}

