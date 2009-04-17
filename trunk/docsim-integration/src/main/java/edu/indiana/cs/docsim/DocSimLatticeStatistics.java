package edu.indiana.cs.docsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Accumulate all statistical data of some number of runs of shingling
 * algorithm.
 * User can retrieve statistical data of different dimensions. Also user can
 * choose how to sort the statistical data.
 *
 * For example:
 *  retrieve statistical data according to shingle size, shingling algorithm
 *  ...
 *
 * Note: the retrieved results are backed by the statistical data stored by
 * this class. So usually you should not alter the returned results.
 *
 * @author
 * @version
 */
public class DocSimLatticeStatistics {

    private List<DocSimPairStatistics> rawStatistics =
        new ArrayList<DocSimPairStatistics>();

    public void add (DocSimPairStatistics pairStat) {
        rawStatistics.add(pairStat);
    }

    /**
     * get the value of rawStatistics
     * @return the value of rawStatistics
     */
    public List<DocSimPairStatistics> getRawStatistics(){
        return this.rawStatistics;
    }
    /**
     * set a new value to rawStatistics
     * @param rawStatistics the new value to be used
     */
    public void setRawStatistics(List<DocSimPairStatistics> rawStatistics) {
        this.rawStatistics=rawStatistics;
    }
    /**
     *
     *
     * @param shinglesize
     * @return
     */
    public List<DocSimPairStatistics> retrieveByShingleSize (int shinglesize) {
        List<DocSimPairStatistics> results = new ArrayList<DocSimPairStatistics>();
        Iterator<DocSimPairStatistics> it;
        for (it = rawStatistics.iterator(); it.hasNext(); ) {
            DocSimPairStatistics stat = it.next();
            if (stat.getShingleSize() == shinglesize) {
                results.add(stat);
            }
        }
        return results;
    }

    public List<DocSimPairStatistics> retrieveByShingleAlg (ShingleAlgorithm shingleAlg) {
        List<DocSimPairStatistics> results = new ArrayList<DocSimPairStatistics>();
        if (shingleAlg == null)
            return results;
        Iterator<DocSimPairStatistics> it;
        for (it = rawStatistics.iterator(); it.hasNext(); ) {
            DocSimPairStatistics stat = it.next();
            if (stat.getShingleAlg() == shingleAlg) {
                results.add(stat);
            }
        }
        return results;
    }


    /**
     * Sort by shingle size and performance.
     *
     * @param statistics
     * @return
     */
    public static List<DocSimPairStatistics> sortBySizePerformance
      (List<DocSimPairStatistics> statistics) {
        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
                int shinglesize1 = stat1.getShingleSize();
                int shinglesize2 = stat2.getShingleSize();
                if (shinglesize1 != shinglesize2) {
                    return shinglesize1 < shinglesize2 ? -1 : 1;
                }
                Double perf1 = stat1.getDocShinglingRatioUnique();
                Double perf2 = stat2.getDocShinglingRatioUnique();
                if (perf2 == Double.POSITIVE_INFINITY || perf2 == Double.NaN) {
                    return -1;
                }
                if (perf1 == Double.POSITIVE_INFINITY || perf1 == Double.NaN) {
                    return 1;
                }
                return perf1==perf2 ? 0 : (perf1 < perf2 ? -1 : 1);
            }
            public boolean equals(Object obj) {
                return this == obj;
            }
        });
        return statistics;
    }

    public List<DocSimPairStatistics> sortBySizePerformance() {
        List<DocSimPairStatistics> copy = new ArrayList<DocSimPairStatistics>();
        copy.addAll(this.getRawStatistics());
        return sortBySizePerformance(copy);
    }

    public static List<DocSimPairStatistics> reverse(
            List<DocSimPairStatistics> statistics) {
        Collections.reverse(statistics);
        return statistics;
    }

    public static List<DocSimPairStatistics> sortByPerformance (
            List<DocSimPairStatistics> statistics) {

        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
                // DocSimPairStatistics stat1 = (DocSimPairStatistics)o1;
                // DocSimPairStatistics stat2 = (DocSimPairStatistics)o2;
                Double perf1 = stat1.getDocShinglingRatioUnique();
                Double perf2 = stat2.getDocShinglingRatioUnique();
                if (perf2 == Double.POSITIVE_INFINITY || perf2 == Double.NaN) {
                    return -1;
                }
                if (perf1 == Double.POSITIVE_INFINITY || perf1 == Double.NaN) {
                    return 1;
                }
                return perf1==perf2 ? 0 : (perf1 < perf2 ? -1 : 1);
            }
            public boolean equals(Object obj) {
                return this == obj;
            }
        });
        return statistics;
    }

    public List<DocSimPairStatistics> sortByPerformance () {
        List<DocSimPairStatistics> copy = new ArrayList<DocSimPairStatistics>();
        copy.addAll(this.getRawStatistics());
        return sortByPerformance(copy);
    }
}

