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

public class DomTagListenerTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DomTagListenerTest.class.getName());

    private DomTagListenerRegistry listenerReg = new DomTagListenerRegistry();
    private HtmlTagListenerFilter  listenerFilter = new HtmlTagListenerFilter(listenerReg);
    private DocFilterPipeLineDefault filterPipeLine;

    {
        try {
            filterPipeLine = new DocFilterPipeLineDefault(){
                protected void init() throws Exception {
                    pipeline.add(listenerFilter);
                }
            };
        } catch(Exception ex) {
            logger.severe("Cannot create DocFilterPipeLineDefault " + ex);
        }
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DomTagListenerTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DomTagListenerTest.class);
    }

    @Override
    public void setUp() {
        try {
            listenerReg.clear();
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
        }
    }

    public void testEmphTagListeners() throws Exception {
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        DomTagListener listenerEmph = new DomTagListenerEmphTags();
        listenerReg.register(listenerEmph);
        filterPipeLine.filter(filename, charset);
        Object dataobj = listenerEmph.getData();
        String output = "emph listener:\n" + seralizeList(dataobj);
        logger.info(output);
    }

    public void testHeadingTagListeners() throws Exception {
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        DomTagListener listenerHeading = new DomTagListenerHeadingTags();
        listenerReg.register(listenerHeading);
        filterPipeLine.filter(filename, charset);
        Object dataobj = listenerHeading.getData();
        String output = "heading listener:\n" + seralizeList(dataobj);
        logger.info(output);
    }

    public void testTitleTagListeners() throws Exception {
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        DomTagListener listenerTitle = new DomTagListenerTitleTag();
        listenerReg.register(listenerTitle);
        filterPipeLine.filter(filename, charset);
        Object dataobj = listenerTitle.getData();
        String output = "Title listener:\n" + seralizeList(dataobj);
        logger.info(output);
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

