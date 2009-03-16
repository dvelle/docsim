package edu.indiana.cs.docsim;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 *
 * @author
 * @version
 */
public class WordShingleTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(WordShingleTest.class.getName());
    private String simpleText =
        "Adds a \"See Also\" heading with a link or text entry that points "+
        "to reference. A doc comment may contain any number of @see tags,"+
        "which are all grouped under the same heading. The @see tag has "+
        "three variations; the third form below is the most common." +
        "This tag is valid in any doc comment: overview, package," +
        "class, interface, constructor, method or field. For inserting an " +
        "in-line link within a sentence to a package, class or member.";

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;
    private WordShingle wordshingle;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public WordShingleTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(WordShingleTest.class);
    }

    @Override
    public void setUp() {
        wordshingle = new WordShingle();
    }

    public void testSimpleText() throws Exception {
        logger.info("Testing by feeding simple text");
        StringBuilder sb = new StringBuilder();
        TextTokenizerWord tokenizer =
            new TextTokenizerWord(simpleText, textEncoding);
        int pos = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            // logger.info("token:" + token);
            wordshingle.addShingleUnit(token, pos++);
        }
        dumpWordShingle(wordshingle);
    }
    public void testWordShingle() throws Exception {
        logger.info("Testing WordShingle by feeding simple text");
        StringBuilder sb = new StringBuilder();
        TextTokenizerWord tokenizer = new TextTokenizerWord(simpleText, textEncoding);
        int pos = 0;
        List<String> list = Lists.newArrayList();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            list.add(token);
        }
        wordshingle.buildShingle(list.toArray(new String[0]));
        dumpWordShingle(wordshingle);
    }

    private void dumpWordShingle(WordShingle shingle) {
        ShingleUnitBag bag = shingle.getSuMgr();
        List<ShingleUnit> shingleunits = bag.getUniqueShingleUnits();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < shingleunits.size() ; ++i) {
            ShingleUnit su = shingleunits.get(i);
            int[] pos = bag.getPos(su);
            sb.append("\n" + su.value() + serIntArray(pos));
        }
        logger.info("Data in shingle:");
        logger.info(sb.toString());
    }

    private String serIntArray(int[] array){
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            return "";
        } else {
            for (int i = 0 ; i < array.length ; ++i) {
                sb.append("-" + array[i]);
            }
        }
        return sb.toString();
    }
}
