package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 *
 * @author
 * @version
 */
public class ShingleTokenizerWordTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(ShingleTokenizerWordTest.class.getName());
    private String simpleText =
        "Adds a \"See Also\" heading with a link or text entry that points "+
        "to reference. A doc comment may contain any number of @see tags,"+
        "which are all grouped under the same heading. The @see tag has "+
        "three variations; the third form below is the most common." +
        "This tag is valid in any doc comment: overview, package," +
        "class, interface, constructor, method or field. For inserting an " +
        "in-line link within a sentence to a package, class or member.";

    private String textLiteral;
    private String textFileName;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ShingleTokenizerWordTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ShingleTokenizerWordTest.class);
    }

    @Override
    public void setUp(){
    }

    public void testSimpleText() throws Exception{
        logger.info("Testing by feeding simple text");
        // textLiteral = "hello   \t[ world";
        StringBuilder sb = new StringBuilder();
        TextTokenizerWord tokenizer = new
            TextTokenizerWord(simpleText);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            sb.append(" " + token);
            // logger.info("Token:" + token);
        }
        logger.info(sb.toString());
    }
}
