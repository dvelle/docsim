package edu.indiana.cs.docsim;

import java.util.List;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.indiana.cs.docsim.TestUtil.dumpWordShingle;

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

    public void testAddSUOnebyOne() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("- Text:\n" + simpleText);
        TextTokenizerWord tokenizer =
            new TextTokenizerWord(simpleText, textEncoding);
        int pos = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            wordshingle.addShingleUnit(token, pos++);
        }
        sb.append("\n- Word Shingle:\n" + dumpWordShingle(wordshingle));
        logger.info(sb.toString());
    }

    public void testAddSUListOnce() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("- Text:\n" + simpleText);
        TextTokenizerWord tokenizer = new TextTokenizerWord(simpleText, textEncoding);
        int pos = 0;
        List<String> list = Lists.newArrayList();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            list.add(token);
        }
        wordshingle.buildShingle(list.toArray(new String[0]));
        sb.append("\n- Word Shingle:\n" + dumpWordShingle(wordshingle));
        logger.info(sb.toString());
    }
}

