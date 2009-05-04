package edu.indiana.cs.docsim.htmlproc.stopword;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class StopWordFilterTest extends TestCase {
    private static Logger logger =
        Logger.getLogger(StopWordFilterTest.class.getName());

    private StopWordFilter filter;
    {
        try {
            filter = new StopWordFilter(false);
        } catch(Exception ex) {
        }
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public StopWordFilterTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(StopWordFilterTest.class);
    }

    @Override
    public void setUp() {
        // this is necessary. or else the stop words in different tests would accumulate
        filter.clear();
    }

    /**
     * Don't load default stop word file in this test.
     *
     * @throws Exception
     */
    public void testWithoutLoadingSWFile() throws Exception {
        filter = new StopWordFilter(false); //don't use default stop word file
        filter.addStopWord("hello");
        filter.addStopWord("world");
        String text = "hello world this is gerald";
        String correctResult = "this is gerald";
        String result = filter.filter(text);
        assertEquals(correctResult, result);
    }

    /**
     * According to stop word list, this test may succeed or fail.
     *
     * @throws Exception
     */
    public void testWithLoadingSWFile() throws Exception {
        filter = new StopWordFilter(true); //don't use default stop word file
        String text = "hello world this is gerald";
        String correctResult = "hello world gerald";
        String result = filter.filter(text);
        assertEquals(correctResult, result);
    }
}

