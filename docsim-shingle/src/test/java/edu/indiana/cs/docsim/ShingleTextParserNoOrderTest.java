package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Iterator;

import static edu.indiana.cs.docsim.TestUtil.dumpShingleSet;
import static edu.indiana.cs.docsim.TestUtil.dumpShingle;

public class ShingleTextParserNoOrderTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(ShingleTextParserNoOrderTest.class.getName());
    private String simpleText =
        "Adds a \"See Also\" heading with a link or text entry that points "+
        "to reference. A doc comment may contain any number of @see tags,"+
        "which are all grouped under the same heading. The @see tag has "+
        "three variations; the third form below is the most common." +
        "This tag is valid in any doc comment: overview, package," +
        "class, interface, constructor, method or field. For inserting an " +
        "in-line link within a sentence to a package, class or member.";

    private String dupText =
        "hello world hello world hello world word hello hello world";

    private String moreDupText =
        "hello world hello world hello world hello world hello world";

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;

    private ShingleTextParserNoOrder shingleTxtParser;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ShingleTextParserNoOrderTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ShingleTextParserNoOrderTest.class);
    }

    @Override
    public void setUp() {
        shingleTxtParser = new ShingleTextParserNoOrder();
    }

    public void testSimpleText() throws Exception {
        int shinglesize = 10;
        String dumpStr;
        StringBuilder sb = new StringBuilder();
        ShingleSet shingleset = shingleTxtParser.parseText(simpleText,
                shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);
        sb.append("- Text:\n" + simpleText);
        sb.append("\n- shingle size:" + shinglesize);
        sb.append("\n- Shingle set:\n" + dumpStr);

        shinglesize = 5;
        shingleset = shingleTxtParser.parseText(dupText, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);
        sb.append("\n- Text:\n" + dupText);
        sb.append("\n- shingle size:" + shinglesize);
        sb.append("\n- Shingle set:\n" + dumpStr);
        logger.info(sb.toString());
    }

    public void testShingleTextFromFile() throws Exception {
        int shinglesize = 10;
        String textFile = "res://shingle.sample.1";
        ShingleSet shingleset = shingleTxtParser.parseFile(textFile,
                shinglesize, textEncoding);
        String dumpStr = dumpShingleSet(shingleset);

        StringBuilder sb = new StringBuilder();
        sb.append("- File:" + textFile);
        sb.append("\n- shingle size:" + shinglesize);
        sb.append("\n- Shingle set:\n" + dumpStr);
        logger.info(sb.toString());
    }
}

