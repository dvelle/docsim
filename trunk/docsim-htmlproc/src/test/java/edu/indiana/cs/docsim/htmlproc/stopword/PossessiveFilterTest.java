package edu.indiana.cs.docsim.htmlproc.stopword;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PossessiveFilterTest extends TestCase {
    private static Logger logger =
        Logger.getLogger(PossessiveFilterTest.class.getName());

    private PossessiveFilter filter = new PossessiveFilter();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PossessiveFilterTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PossessiveFilterTest.class);
    }

    @Override
    public void setUp() { }

    public void testShortText() throws Exception {
        String text = "this is gerald's code";
        String correctResult = "this is gerald code";
        String result = filter.filter(text);
        assertEquals(correctResult, result);
    }

    public void testShortText2() throws Exception {
        String text = "this is gerald'ss code";
        String correctResult = "this is gerald'ss code";
        String result = filter.filter(text);
        assertEquals(correctResult, result);
    }
}

