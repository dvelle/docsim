package edu.indiana.cs.docsim.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PageRepositoryShuffle {

    /**
     * This shuffling algorithm has pitfall that the same search result can be
     * selected multiple times.
     *
     * @param pageRepo
     */
    public static void shuffleBad(PageRepository pageRepo) {
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

    public static void shuffle(PageRepository pageRepo) {
        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();
        List<QueryEntry> normQueryEntries = new ArrayList<QueryEntry>();
        List<Integer> nResultLs = new ArrayList<Integer>();

        for (Iterator<QueryEntry> it = queryEntries.iterator(); it.hasNext();) {
            QueryEntry qentry = it.next();
            List<SearchResultEntry> results = qentry.getResults();
            if (results.size() > 0) {
                nResultLs.add(results.size());
                normQueryEntries.add(qentry);
            }
        }
        int nQEntries = normQueryEntries.size();

        // logger.info("nq:" + nQEntries);

        Random generator = new Random();
        List<QueryEntry> newQueryEntries = new ArrayList<QueryEntry>();
        for (int i = 0 ; i <nQEntries; ++i) {
            int size = nResultLs.get(i);
            if (size > nQEntries) continue;

            QueryEntry qentry = new QueryEntry();

            Set<Integer> selected = new HashSet<Integer>();
            int idx;
            for (int j = 0 ; j < size; ++j) {
                while (true) {
                    idx = generator.nextInt(nQEntries);
                    if (!selected.contains(idx)) {
                        selected.add(idx);
                        break;
                    }
                }
            }

            for (Iterator<Integer> it = selected.iterator(); it.hasNext();) {
                int nidx = it.next();
                QueryEntry qe = normQueryEntries.get(nidx);
                List<SearchResultEntry> tmpQEntries = qe.getResults();
                if (tmpQEntries.size() > 0) {
                    SearchResultEntry tmpSRE = tmpQEntries.remove(0);
                    qentry.addSearchResultEntry(tmpSRE);
                }
            }
            if (qentry.getResultCount() > 0)
                newQueryEntries.add(qentry);
        }
        pageRepo.setQueryEntries(newQueryEntries);
    }
}

