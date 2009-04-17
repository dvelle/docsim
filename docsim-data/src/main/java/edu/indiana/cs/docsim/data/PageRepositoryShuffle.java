package edu.indiana.cs.docsim.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class PageRepositoryShuffle {
    public static void shuffle(PageRepository pageRepo) {
        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();
        int nQEntries = queryEntries.size();
        Iterator<QueryEntry> it = queryEntries.iterator();
        Random generator = new Random();
        while (it.hasNext()) {
            QueryEntry qentry = it.next();
            List<SearchResultEntry> results = qentry.getResults();
            List<SearchResultEntry> newresults = new ArrayList<SearchResultEntry>();
            for (int i = 0 ; i < results.size() ; ++i) {

                int idx = generator.nextInt(nQEntries);
                QueryEntry qentrytmp = queryEntries.get(idx);

                int nResults = qentrytmp.getResults().size();
                int idx2 = generator.nextInt(nResults);

                SearchResultEntry srentry = qentrytmp.getResults().get(idx2);
                newresults.add(srentry);
            }
            qentry.setResults(newresults);
        }
    }
}

