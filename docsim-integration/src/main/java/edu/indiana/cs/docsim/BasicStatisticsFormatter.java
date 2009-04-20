package edu.indiana.cs.docsim;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import edu.indiana.cs.docsim.data.util.ResourceLoader;

/**
 * basic formatter.
 *
 * @author
 * @version
 */
public class BasicStatisticsFormatter {

    public static Logger logger =
        Logger.getLogger(BasicStatisticsFormatter.class.getName());

    public void format(DocSimLatticeStatistics statistics, String fileName)
      throws IOException {
        BufferedWriter bw =
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

        String result;
        StringBuilder sb = new StringBuilder();

        result = formatStatLs(statistics.getRawStatistics());

        int[] dims = {4, 3, 1, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(statistics.getRawStatistics(), dims));
        bw.write("\n[4 3 1 2]\n", 0, "\n[4 3 1 2]\n".length());
        bw.write(result, 0, result.length());
        bw.flush();

        int[] dims2 = {4, 1, 3, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(statistics.getRawStatistics(),
                    dims2));
        bw.write("\n[4 1 3 2]\n", 0, "\n[4 1 3 2]\n".length());
        bw.write(result, 0, result.length());
        bw.flush();

        int[] dims3 = {3, 1, 4, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(statistics.getRawStatistics(),
                    dims3));
        bw.write("\n[3 1 4 2]\n", 0, "\n[3 1 4 2]\n".length());
        bw.write(result, 0, result.length());
        bw.flush();

        bw.close();
    }

    public String format(DocSimLatticeStatistics statistics) {
        String result;
        StringBuilder sb = new StringBuilder();

        result = formatStatLs(statistics.getRawStatistics());

        int[] dims = {4, 3, 1, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(statistics.getRawStatistics(), dims));
        sb.append("[4 3 1 2]\n" +result);

        int[] dims2 = {4, 1, 3, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(statistics.getRawStatistics(),
                    dims2));
        sb.append("\n[4 1 3 2]\n" +result);
        // result = formatSortBySize(statistics);
        return sb.toString();
    }

    private String formatSortBySize(DocSimLatticeStatistics statistics) {
        List<DocSimPairStatistics> statLs;
        StringBuilder sb = new StringBuilder();
        String result;

        statLs = statistics.retrieveByShingleAlg(ShingleAlgorithm.Original);
        result = formatStatLs(statLs);
        sb.append("[original]\n" + result);

        statLs = statistics.retrieveByShingleAlg(ShingleAlgorithm.IgnoreOrder);
        result = formatStatLs(statLs);
        sb.append("[ignoreorder]\n" + result);

        return sb.toString();
    }

    public String formatStatLs(List<DocSimPairStatistics> statLs) {
        if (statLs == null)
            return "";
        StringBuilder sb = new StringBuilder();
        Iterator<DocSimPairStatistics> it;
        for (it = statLs.iterator(); it.hasNext(); ) {
            DocSimPairStatistics stat = it.next();
            sb.append("shinglesize:" + stat.getShingleSize());
            ShingleAlgorithm shingleAlg = stat.getShingleAlg();;
            if (shingleAlg == ShingleAlgorithm.Original) {
                sb.append("\n");
                sb.append("shingle algorithm:" + ShingleAlgorithm.original);
            }else if (shingleAlg == ShingleAlgorithm.IgnoreOrder) {
                sb.append("\n");
                sb.append("shingle algorithm:" + ShingleAlgorithm.ignoreOrder);
            } else {
            }
            sb.append("\n");
            sb.append("doc 1:" + stat.getDocSrc1());
            sb.append("\n");
            sb.append("doc 2:" + stat.getDocSrc2());
            sb.append("\n");
            sb.append("shingle set size 1:" + stat.getDocShingleSetSizeUnique1());
            sb.append("\n");
            sb.append("shingle set size 2:" + stat.getDocShingleSetSizeUnique2());
            sb.append("\n");
            sb.append("shingle union set size:" + stat.getDocShingleSetUnionSizeUnique());
            sb.append("\n");
            sb.append("shingle intersect set size:" + stat.getDocShingleSetIntersectSizeUnique());
            sb.append("\n");
            sb.append("ratio:" + stat.getDocShinglingRatioUnique());
            sb.append("\n");
            sb.append("class:" + (stat.getPositive()?"yes":"no"));
            // logger.info("ratio:" + stat.getDocShinglingRatioUnique());
            sb.append("\n\n");
        }

        return sb.toString();
    }

    public static void avgCalcBySize2File(DocSimLatticeStatistics statistics,
            String fileName) throws Exception {
        String result = avgCalcBySize(statistics);
        FileUtils.writeStringToFile(new File(fileName), result, "UTF-8");
    }

    public static String avgCalcBySize(DocSimLatticeStatistics statistics) {
        int[] dims = {1, 4, 3, 2};
        List<DocSimPairStatistics> result =
            DocSimLatticeStatistics.sort(statistics.getRawStatistics(), dims);
        double ac1, ac2, ac3, ac4;
        int oldsize = -1;

        List<DataEntry> dentries = new ArrayList<DataEntry>();
        int i = 0, count;
        while(i < result.size()) {
            ac1 = ac2 = ac3 = ac4 = 0;
            oldsize = -1;
            count = 0;
            for ( ; i < result.size() ; ++i) {
                ++count;
                DocSimPairStatistics stat = result.get(i);
                if (oldsize == -1) {
                    oldsize = stat.getShingleSize();
                } else if (oldsize != stat.getShingleSize()) {
                    break;
                }

                if (stat.getShingleAlg() == ShingleAlgorithm.Original &&
                    stat.getPositive()) {
                    ac1 += stat.getDocShinglingRatioUnique();
                } else if (stat.getShingleAlg() == ShingleAlgorithm.IgnoreOrder &&
                           stat.getPositive()) {
                    ac2 += stat.getDocShinglingRatioUnique();
                } else if (stat.getShingleAlg() == ShingleAlgorithm.Original &&
                    !stat.getPositive()) {
                    ac3 += stat.getDocShinglingRatioUnique();
                } else if (stat.getShingleAlg() == ShingleAlgorithm.IgnoreOrder &&
                    !stat.getPositive()) {
                    ac4 += stat.getDocShinglingRatioUnique();
                }
                // logger.info("ratio:" + stat.getDocShinglingRatioUnique());
            }
            DataEntry dentry =
                new DataEntry( oldsize, ac1/count, ac3/count, ac2/count, ac4/count);
            // logger.info("count is:"+count + ";" + ac1 +";"+ac2+";"+ac3+";"+ac4);
            dentries.add(dentry);
        }
        return formatStatBySize(dentries);
    }

    public static String formatStatBySize(List<DataEntry> dentries) {
        StringBuilder sb = new StringBuilder();
        sb.append("size\toriginal and positive\toriginal and negative\t" +
                "ignoreOrder and positive\tignoreOrder and negative\n");
        for (Iterator<DataEntry> it = dentries.iterator();
             it.hasNext();) {
            DataEntry dentry = it.next();
            sb.append(dentry.shingleSize + "\t" +
                    dentry.originalAndPositive + "\t" +
                    dentry.originalAndNegative + "\t" +
                    dentry.ignoreOrderAndPositive + "\t" +
                    dentry.ignoreOrderAndNegative + "\n");
        }
        return sb.toString();
    }

    // Sample:
    //
    // shinglesize:1
    // shingle algorithm:original
    // doc 1:http://news.yahoo.com/s/nm/20090415/tc_nm/us_intel_13
    // doc 2:http://www.ciol.com/Global-News/News-Reports/Intel-says-PC-market-hit-bottom-but-shares-slide/15409118409/0/
    // shingle set size 1:359
    // shingle set size 2:10
    // shingle union set size:366
    // shingle intersect set size:3
    // ratio:0.00819672131147541
    // class:yes
    public static DocSimLatticeStatistics getStatFromFile(String statFileName)
      throws Exception {
        InputStream is = ResourceLoader.open(statFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        DocSimLatticeStatistics allStat = new DocSimLatticeStatistics();
        String line = null;
        DocSimPairStatistics stat = null;
        boolean init = false;
        boolean first = true;
        int count = 0;
        while(true) {
            if (line == null) {
                stat = new DocSimPairStatistics();
                init = false;
            }

            line = br.readLine();
            if (line != null) {
                if (line.matches("\\[. . . .\\].{0,}+")) {
                    if (first)
                        first = false;
                    else
                        break;
                } else {
                    line = line.trim();
                    if (line.length() == 0) {
                        line = null;
                        if (init)
                            allStat.add(stat);
                        continue;
                    } else {
                        setAttr(stat, line);
                        init = true;
                    }
                }
            } else {
                break;
            }
            count++;
        }
        logger.info("count:" + count);
        if (init)
            allStat.add(stat);
        is.close();
        return allStat;
    }

    private static void setAttr(DocSimPairStatistics stat, String line)
      throws Exception {
        int idx = line.indexOf(":");
        if (idx == -1) return;
        String key = line.substring(0, idx).trim();
        String value = line.substring(idx+1).trim();
        if (key.equalsIgnoreCase("shinglesize")) {
            int shinglesize = Integer.valueOf(value);
            stat.setShingleSize(shinglesize);
        } else if (key.equalsIgnoreCase("shingle algorithm")) {
            ShingleAlgorithm shingleAlg = ShingleAlgorithm.getValue(value);
            if (shingleAlg == null) {
                throw new Exception("shingle algorithm is not valid:'" +
                        value + "'");
            }
            stat.setShingleAlg(shingleAlg);
        } else if (key.equalsIgnoreCase("doc 1")) {
            String doc1 = value;
            stat.setDocSrc1(doc1);
        } else if (key.equalsIgnoreCase("doc 2")) {
            String doc2 = value;
            stat.setDocSrc2(doc2);
        } else if (key.equalsIgnoreCase("shingle set size 1")) {
            int size1 = Integer.valueOf(value);
            stat.setDocShingleSetSizeUnique1(size1);
        } else if (key.equalsIgnoreCase("shingle set size 2")) {
            int size2 = Integer.valueOf(value);
            stat.setDocShingleSetSizeUnique2(size2);
        } else if (key.equalsIgnoreCase("shingle union set size")) {
            int unionsize = Integer.valueOf(value);
            stat.setDocShingleSetUnionSizeUnique(unionsize);
        } else if (key.equalsIgnoreCase("shingle intersect set size")) {
            int intersectsize = Integer.valueOf(value);
            stat.setDocShingleSetIntersectSizeUnique(intersectsize);
        } else if (key.equalsIgnoreCase("class")) {
            String clazz = value;
            if (clazz.equalsIgnoreCase("yes")) {
                stat.setPositive(true);
            } else {
                stat.setPositive(false);
            }
        } else {
        }
    }
}

class DataEntry {
    public int shingleSize;
    public double originalAndPositive;
    public double originalAndNegative;
    public double ignoreOrderAndPositive;
    public double ignoreOrderAndNegative;
    public DataEntry(int shingleSize, double originalAndPositive,
            double originalAndNegative, double ignoreOrderAndPositive,
            double ignoreOrderAndNegative) {
        this.shingleSize = shingleSize;
        this.originalAndNegative = originalAndNegative;
        this.originalAndPositive = originalAndPositive;
        this.ignoreOrderAndNegative = ignoreOrderAndNegative;
        this.ignoreOrderAndPositive = ignoreOrderAndPositive;
    }
}

