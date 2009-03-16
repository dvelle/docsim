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
        logger.info("Testing by feeding simple text");
        StringBuilder sb = new StringBuilder();
        ShingleSet shingleset = shingleTxtParser.parseText(simpleText,
                shinglesize, textEncoding);
        dumpeShingleSet(shingleset);
        logger.info(sb.toString());

        shinglesize = 5;
        shingleset = shingleTxtParser.parseText(dupText, shinglesize, textEncoding);
        dumpeShingleSet(shingleset);
        logger.info(sb.toString());
    }

    private void dumpeShingleSet(ShingleSet shingleset) {
        // List<Shingle> shingles = shingleset.getShingleList();
        List<Shingle> shingles = shingleset.getUniqueShingleList();
        for (Iterator<Shingle> it = shingles.iterator() ; it.hasNext();) {
            Shingle shingle = it.next();
            logger.info("shingle:");
            dumpShingle(shingle);
            int[] pos = shingleset.getPos(shingle);
            logger.info("pos:" + serIntArray(pos));
        }
    }
    private void dumpShingle(Shingle shingle) {
        ShingleUnitBag sub = shingle.getSuMgr();
        List<ShingleUnit> sulist = sub.getUniqueShingleUnits();
        Iterator<ShingleUnit> it = sulist.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ShingleUnit su = it.next();
            int[] pos = sub.getPos(su);
            sb.append(su.value());
            sb.append(":" + serIntArray(pos));
        }
        logger.info(sb.toString());
    }
    private String serIntArray(int[] array){
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            return "";
        } else {
            for (int i = 0 ; i < array.length ; ++i) {
                sb.append(array[i]+"-");
            }
        }
        return sb.toString();
    }
}
