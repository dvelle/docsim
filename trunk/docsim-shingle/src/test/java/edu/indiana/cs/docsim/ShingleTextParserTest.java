package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Iterator;

public class ShingleTextParserTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(ShingleTextParserTest.class.getName());
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

    private ShingleTextParser shingleTxtParser;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ShingleTextParserTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ShingleTextParserTest.class);
    }

    @Override
    public void setUp() {
        shingleTxtParser = new ShingleTextParser();
    }

    public void testSimpleText() throws Exception {
        int shinglesize = 10;
        String dumpStr;
        logger.info("Testing by feeding simple text");
        StringBuilder sb = new StringBuilder();
        ShingleSet shingleset = shingleTxtParser.parseText(simpleText,
                shinglesize, textEncoding);
        dumpStr = dumpeShingleSet(shingleset);
        logger.info(dumpStr);

        shinglesize = 5;
        shingleset = shingleTxtParser.parseText(dupText, shinglesize, textEncoding);
        dumpStr = dumpeShingleSet(shingleset);
        logger.info(dumpStr);
    }

    public void testShingleTextFromFile() throws Exception {
        int shinglesize = 10;
        String textFile = "res://shingle.sample.1";
        ShingleSet shingleset = shingleTxtParser.parseFile(textFile,
                shinglesize, textEncoding);
        String dumpStr = dumpeShingleSet(shingleset);
        logger.info(dumpStr);
    }

    private String dumpeShingleSet(ShingleSet shingleset) {
        // List<Shingle> shingles = shingleset.getShingleList();
        List<Shingle> shingles = shingleset.getUniqueShingleList();
        StringBuilder sb = new StringBuilder();
        for (Iterator<Shingle> it = shingles.iterator() ; it.hasNext();) {
            Shingle shingle = it.next();
            sb.append("\nshingle:\n");
            sb.append("\t" + dumpShingle(shingle));
            int[] pos = shingleset.getPos(shingle);
            sb.append("\n\tpos:" + serIntArray(pos));
        }
        return sb.toString();
    }
    private String dumpShingle(Shingle shingle) {
        ShingleUnitBag sub = shingle.getSuMgr();
        List<ShingleUnit> sulist = sub.getUniqueShingleUnits();
        Iterator<ShingleUnit> it = sulist.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ShingleUnit su = it.next();
            int[] pos = sub.getPos(su);
            sb.append(su.value());
            sb.append(":" + serIntArray(pos) + "; ");
        }
        return sb.toString();
    }
    private String serIntArray(int[] array){
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            return "";
        } else {
            for (int i = 0 ; i < array.length ; ++i) {
                if (i == array.length-1) {
                    sb.append(array[i]);
                } else {
                    sb.append(array[i]+",");
                }
            }
        }
        return sb.toString();
    }
}
