package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import java.util.Map;

import com.google.common.collect.Maps;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 *
 * @author
 * @version
 */
public class TextTokenizerWordTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(TextTokenizerWordTest.class.getName());

    private String simpleText =
        "Adds a \"See Also\" heading with a link or text entry that points "+
        "to reference. A doc comment may contain any number of @see tags,";

    // This variable contains a list of words appearing in variable
    // simpleText. So if variable simpleText is changed, this variable must be
    // changed accordingly.
    private String[] simpleTextTokens = {
        "Adds", "a", "See", "Also", "heading", "with", "a", "link", "or",
        "text", "entry", "that", "points", "to", "reference", "A", "doc",
        "comment", "may", "contain", "any", "number", "of", "see", "tags"
    };

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TextTokenizerWordTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TextTokenizerWordTest.class);
    }

    @Override
    public void setUp(){
    }


    /**
     *  If the tokenizer maintains the token order, this test function should
     *  be used.
     *
     * @throws Exception
     */
    public void testSimpleTextInOrder() throws Exception{
        logger.info("Testing TextTokenizer by feeding simple text");
        StringBuilder sb = new StringBuilder();
        TextTokenizerWord tokenizer = new
            TextTokenizerWord(simpleText, textEncoding);
        int index = 0;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            assertEquals(token, simpleTextTokens[index++]);
        }
    }

    /**
     *  If the tokenizer does NOT maintain the token order, this test function
     *  should be used.
     *
     * @throws Exception
     */
    public void testSimpleTextOutOfOrder() throws Exception{
        Map<String, Integer> tokenmap = Maps.newTreeMap();
        for (int i = 0 ; i < simpleTextTokens.length ; ++i) {
            String token = simpleTextTokens[i];
            int count = 1;
            if (tokenmap.containsKey(token)) {
                count = tokenmap.get(token);
                count += 1;
            }
            tokenmap.put(token, count);
        }

        logger.info("Testing TextTokenizer by feeding simple text");
        StringBuilder sb = new StringBuilder();
        TextTokenizerWord tokenizer = new
            TextTokenizerWord(simpleText, textEncoding);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            assertTrue(tokenmap.containsKey(token));
            int count = tokenmap.get(token);
            tokenmap.put(token, count-1);
        }

        for (Map.Entry<String, Integer> entry : tokenmap.entrySet() ) {
            assertEquals(0, (int)entry.getValue());
        }
    }
}
