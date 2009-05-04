package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Iterator;

public class DocFilterPipeLineSuiteTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DocFilterPipeLineSuiteTest.class.getName());

    private DocFilterPipeLineDefault docFilterPipeLine;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocFilterPipeLineSuiteTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocFilterPipeLineSuiteTest.class);
    }

    @Override
    public void setUp() {
    }

    public void testPipeLineLocal() throws Exception {
        String filename = "res://augmented_page.html";
        filter(filename);
    }

    public void filter(String src) throws Exception{
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
                        //TODO: Add Exception handler here
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
        String result = "";
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
    }

    public void testPipeLineRemote() throws Exception{
        // String strUrl = "http://cs.indiana.edu";
        String strUrl = "http://www.informatics.indiana.edu/";
        filter(strUrl);
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

