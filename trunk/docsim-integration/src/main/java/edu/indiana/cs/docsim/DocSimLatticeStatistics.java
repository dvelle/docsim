package edu.indiana.cs.docsim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    private static Logger logger =
        Logger.getLogger(DocSimLatticeStatistics.class.getName());

    private List<DocSimPairStatistics> rawStatistics =
        new ArrayList<DocSimPairStatistics>();

    public DocSimLatticeStatistics merge (DocSimLatticeStatistics stat) {
        List<DocSimPairStatistics> rawStatistics =
            new ArrayList<DocSimPairStatistics>();
        rawStatistics.addAll(this.getRawStatistics());
        rawStatistics.addAll(stat.getRawStatistics());
        DocSimLatticeStatistics resultstat = new DocSimLatticeStatistics();
        resultstat.setRawStatistics(rawStatistics);
        return resultstat;
    }

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

    /**
     *
     * @param statistics
     * @param dims
     * @return
     */
    public static List<DocSimPairStatistics> sort (
            List<DocSimPairStatistics> statisticsParam, int[] dims) {

        BasicStatisticsFormatter formatter = new BasicStatisticsFormatter();
        List<DocSimPairStatistics> statistics = new ArrayList<DocSimPairStatistics>();
        statistics.addAll(statisticsParam);

        int dim1 = dims[0];
        int dim2 = dims[1];
        int dim3 = dims[2];
        int dim4 = dims[3];
        List<Range> resultRangeIdx0 = new ArrayList<Range>();
        resultRangeIdx0.add(new Range(0, statistics.size()));

        List<Range> resultRangeIdx1 = new ArrayList<Range>();
        statistics = sort(statistics, resultRangeIdx0, dim1, resultRangeIdx1);
        // logger.info("after 1:\n" + formatter.formatStatLs(statistics));

        List<Range> resultRangeIdx2 = new ArrayList<Range>();
        statistics = sort(statistics, resultRangeIdx1, dim2, resultRangeIdx2);
        // logger.info("after 2:\n" + formatter.formatStatLs(statistics));

        List<Range> resultRangeIdx3 = new ArrayList<Range>();
        statistics = sort(statistics, resultRangeIdx2, dim3, resultRangeIdx3);
        // logger.info("after 3:\n" + formatter.formatStatLs(statistics));

        List<Range> resultRangeIdx4 = new ArrayList<Range>();
        statistics = sort(statistics, resultRangeIdx3, dim4, resultRangeIdx4);
        // logger.info("after 4:\n" + formatter.formatStatLs(statistics));
        return statistics;
    }

    public static List<DocSimPairStatistics> sort (
            List<DocSimPairStatistics> statistics,
            List<Range> rangeIdx,
            int dim,
            List<Range> resultRangeIdx) {

        List<DocSimPairStatistics> results =
            new ArrayList<DocSimPairStatistics>();

        int size = statistics.size();
        int offset = 0, noffset = 0;
        for (Iterator<Range> it = rangeIdx.iterator();
             it.hasNext(); ) {
            List<DocSimPairStatistics> list =
                new ArrayList<DocSimPairStatistics>();
            List<Range> rangeTmp = new ArrayList<Range>();
            Range range = it.next();
            int start = range.getStart();
            int stop = range.getStop();
            for (int i = start; i < stop && i < size; ++i) {
                DocSimPairStatistics obj = statistics.get(i);
                list.add(obj);
                ++ noffset;
            }
            results.addAll(sort(list, dim, rangeTmp));
            resultRangeIdx.addAll(addOffset2Ranges(rangeTmp, offset));
            offset = noffset;
        }
        return results;
    }

    private static List<Range> addOffset2Ranges(List<Range> rangeIdx, int offset) {
        for (Iterator<Range> it = rangeIdx.iterator();
             it.hasNext(); ) {
            Range range = it.next();
            range.setStart(range.getStart() + offset);
            range.setStop(range.getStop() + offset);
        }
        return rangeIdx;
    }

    /**
     * dim:
     *  1: size
     *  2: performance
     *  3: shingle algorithm
     *  4: class
     */
    public static List<DocSimPairStatistics> sort(
            List<DocSimPairStatistics> statistics,
            int dim,
            List<Range> resultRangeIdx) {
        switch (dim) {
            case 1:
                return sortBySize(statistics, resultRangeIdx);
            case 2:
                return sortByPerformance(statistics, resultRangeIdx);
            case 3:
                return sortByShingleAlg(statistics, resultRangeIdx);
            case 4:
                return sortByClass(statistics, resultRangeIdx);
            default:
                logger.warning("unrecognized dimension");
                return null;
        }
    }

    public static List<DocSimPairStatistics> sortBySize
      (List<DocSimPairStatistics> statistics, List<Range> resultRangeIdx) {
        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
                int shinglesize1 = stat1.getShingleSize();
                int shinglesize2 = stat2.getShingleSize();
                if (shinglesize1 != shinglesize2) {
                    return shinglesize1 < shinglesize2 ? -1 : 1;
                } else {
                    return 0;
                }
            }
            public boolean equals(Object obj) {
                return this == obj;
            }
        });
        int start = -1, stop = -1, oldvalue = -1, idx = -1;
        for (Iterator<DocSimPairStatistics> it = statistics.iterator();
             it.hasNext(); ){
            ++idx;
            DocSimPairStatistics stat = it.next();
            int size = stat.getShingleSize();
            if (start == -1) {
                start = 0;
                oldvalue = size;
                continue;
            }
            if (size != oldvalue) {
                stop = idx; //exclusive
                resultRangeIdx.add(new Range(start, stop));
                oldvalue = size;
                start = idx;
            }
        }
        if (start != -1)
            resultRangeIdx.add(new Range(start, idx+1));
        return statistics;
    }

    public static List<DocSimPairStatistics> sortByClass
      (List<DocSimPairStatistics> statistics, List<Range> resultRangeIdx) {
        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
                boolean positive1 = stat1.getPositive();
                boolean positive2 = stat2.getPositive();
                if (positive1 != positive2) {
                    return positive1 ? -1 : 1;
                } else {
                    return 0;
                }
            }
            public boolean equals(Object obj) {
                return this == obj;
            }
        });
        int start = -1, stop = -1, idx = -1;
        boolean oldvalue = false;;
        for (Iterator<DocSimPairStatistics> it = statistics.iterator();
             it.hasNext(); ){
            ++idx;
            DocSimPairStatistics stat = it.next();
            boolean positive = stat.getPositive();
            if (start == -1) {
                start = 0;
                oldvalue = positive;
                continue;
            }
            if (positive != oldvalue) {
                stop = idx; //exclusive
                resultRangeIdx.add(new Range(start, stop));
                oldvalue = positive;
                start = idx;
            }
        }
        if (start != -1)
            resultRangeIdx.add(new Range(start, idx+1));
        return statistics;
    }

    public static List<DocSimPairStatistics> sortByShingleAlg
      (List<DocSimPairStatistics> statistics, List<Range> resultRangeIdx) {
        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
                ShingleAlgorithm shingleAlg1 = stat1.getShingleAlg();
                ShingleAlgorithm shingleAlg2 = stat2.getShingleAlg();
                int n1, n2;
                switch (shingleAlg1) {
                    case None:
                        n1 = 1;
                        break;
                    case Original:
                        n1 = 2;
                        break;
                    case IgnoreOrder:
                        n1 = 3;
                        break;
                    case Weighted:
                        n1 = 4;
                        break;
                    default:
                        n1 = 5;
                }
                switch (shingleAlg2) {
                    case None:
                        n2 = 1;
                        break;
                    case Original:
                        n2 = 2;
                        break;
                    case IgnoreOrder:
                        n2 = 3;
                        break;
                    case Weighted:
                        n2 = 4;
                        break;
                    default:
                        n2 = 5;
                }
                if (shingleAlg1 != shingleAlg2) {
                    return n1 < n2 ? -1 : 1;
                } else {
                    return 0;
                }
            }
            public boolean equals(Object obj) {
                return this == obj;
            }
        });
        int start = -1, stop = -1, idx = -1;
        ShingleAlgorithm oldvalue = null;
        for (Iterator<DocSimPairStatistics> it = statistics.iterator();
             it.hasNext(); ){
            ++idx;
            DocSimPairStatistics stat = it.next();
            ShingleAlgorithm shingleAlg = stat.getShingleAlg();
            if (start == -1) {
                start = 0;
                oldvalue = shingleAlg;
                continue;
            }
            if (shingleAlg != oldvalue) {
                stop = idx; //exclusive
                resultRangeIdx.add(new Range(start, stop));
                oldvalue = shingleAlg;
                start = idx;
            }
        }
        if (start != -1)
            resultRangeIdx.add(new Range(start, idx+1));
        return statistics;
    }

    public static List<DocSimPairStatistics> sortByPerformance
      (List<DocSimPairStatistics> statistics, List<Range> resultRangeIdx) {
        Collections.sort(statistics, new Comparator<DocSimPairStatistics> () {
            public int compare(DocSimPairStatistics stat1, DocSimPairStatistics stat2) {
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
        int start = -1, stop = -1, idx = -1;
        Double oldvalue = null;
        for (Iterator<DocSimPairStatistics> it = statistics.iterator();
             it.hasNext(); ){
            ++idx;
            DocSimPairStatistics stat = it.next();
            Double perf = stat.getDocShinglingRatioUnique();
            if (start == -1) {
                start = 0;
                oldvalue = perf;
                continue;
            }
            if (perf != oldvalue) {
                stop = idx; //exclusive
                resultRangeIdx.add(new Range(start, stop));
                oldvalue = perf;
                start = idx;
            }
        }
        if (start != -1)
            resultRangeIdx.add(new Range(start, idx+1));
        return statistics;
    }
}

class RangePicker {
    public static List<List<?>> getRanges (List list, List<Range> rangeIdx) {
        int size = list.size();
        List<List<?>> lists = new ArrayList();
        for (Iterator<Range> it = rangeIdx.iterator();
             it.hasNext(); ) {
            List sublist = new ArrayList();
            Range range = it.next();
            int start = range.getStart();
            int stop = range.getStop();
            for (int i = start; i < stop && i < size; ++i) {
                Object obj = list.get(i);
                sublist.add(obj);
            }
            lists.add(sublist);
        }
        return lists;
    }
}

class Range {
    private int stop;
    private int start;

    public Range(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    /**
     * get the value of start
     * @return the value of start
     */
    public int getStart(){
        return this.start;
    }
    /**
     * set a new value to start
     * @param start the new value to be used
     */
    public void setStart(int start) {
        this.start=start;
    }
    /**
     * get the value of stop
     * @return the value of stop
     */
    public int getStop(){
        return this.stop;
    }
    /**
     * set a new value to stop
     * @param stop the new value to be used
     */
    public void setStop(int stop) {
        this.stop=stop;
    }
}

