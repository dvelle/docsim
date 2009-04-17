package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DocSimTest extends TestCase {

    private static Logger logger =
        Logger.getLogger(DocSimTest.class.getName());

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocSimTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocSimTest.class);
    }
    public void testWholeProcess() throws Exception {
        DocSim docsim = new DocSim();
        docsim.applyShingling(null);
    }
}

