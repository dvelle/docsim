package edu.indiana.cs.docsim.data;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Iterator;

public class PageRetrievalTest extends TestCase {
    private static Logger logger =
        Logger.getLogger(PageRetrievalTest.class.getName());

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageRetrievalTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PageRetrievalTest.class);
    }

    @Override
    public void setUp() { }

    public void testDummy() {
    }

    /**
     * According to stop word list, this test may succeed or fail.
     *
     * @throws Exception
     */
    public void notestWithLoadingDefaultData() throws Exception {
        String[] options = new String[] {
            // "--output-directory=./pages"
            "-d", "pages"
        };
        PageRetrieval.main(options);
    }
}

