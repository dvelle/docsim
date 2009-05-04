package edu.indiana.cs.docsim.data;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.List;
import java.util.Iterator;

public class PageRepositoryTest extends TestCase {
    private static Logger logger =
        Logger.getLogger(PageRepositoryTest.class.getName());

    private PageRepository pageRepo;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageRepositoryTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(PageRepositoryTest.class);
    }

    @Override
    public void setUp() { }

    /**
     * According to stop word list, this test may succeed or fail.
     *
     * @throws Exception
     */
    public void testWithLoadingDefaultData() throws Exception {
        pageRepo = new PageRepository(true); //don't use default stop word file
        String result = dump(pageRepo);
        logger.info(result);
    }
    private String dump(PageRepository pageRepo) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();
        for (Iterator<QueryEntry> it = queryEntries.iterator(); it.hasNext(); ) {
            QueryEntry entry = it.next();
            sb.append("\n\n" + entry.getQuery());
            int count = entry.getResultCount();
            sb.append("; " + count);
            List<SearchResultEntry> results = entry.getResults();
            for (int i = 0 ; i < count ; ++i) {
                SearchResultEntry result = results.get(i);
                sb.append("\n" + result.getTitle());
                sb.append("\n" + result.getUrl());
            }
        }
        return sb.toString();
    }

    public void testWithLoadingDefaultDataShuffle() throws Exception {
        pageRepo = new PageRepository(true); //don't use default stop word file
        PageRepositoryShuffle.shuffle(pageRepo);
        String str = dump(pageRepo);
        logger.info(str);
    }
}

