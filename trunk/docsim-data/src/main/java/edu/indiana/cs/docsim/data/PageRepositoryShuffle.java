package edu.indiana.cs.docsim.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PageRepositoryShuffle {

    public static void shuffle(PageRepository pageRepo) {
        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();
        int nQEntries = queryEntries.size();
        Iterator<QueryEntry> it = queryEntries.iterator();
        Random generator = new Random();
        Set<Integer> set = new HashSet<Integer>();
        int curQuery = 0;
        while (it.hasNext()) {
            QueryEntry qentry = it.next();
            List<SearchResultEntry> results = qentry.getResults();
            List<SearchResultEntry> newresults = new ArrayList<SearchResultEntry>();
            int resultsize = results.size();
            int newsize = resultsize > nQEntries ? nQEntries : resultsize;

            set.add(curQuery);
            int nResults = 0;
            for (int i = 1 ; i < newsize; ++i) {

                int idx;
                while (true) {
                    idx = generator.nextInt(nQEntries);
                    if (set.contains(idx)) {
                        continue;
                    } else {
                        set.add(idx);
                        break;
                    }
                }
                QueryEntry qentrytmp = queryEntries.get(idx);

                nResults = qentrytmp.getResults().size();
                if (nResults > 0) {
                    int idx2 = generator.nextInt(nResults);
                    SearchResultEntry srentry = qentrytmp.getResults().get(idx2);
                    newresults.add(srentry);
                    ++nResults;
                } else {
                    continue;
                }
            }
            qentry.setResults(newresults);
            qentry.setResultCount(nResults);
            set.clear();
            ++curQuery;
        }
    }
}

