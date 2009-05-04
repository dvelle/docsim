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
import java.io.StringWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    // Seperator of additional results
    private static String Additional_Result_Sep = ";";

    public static String doubleFormat = "0.000000";
    public static DecimalFormat doubleFormatter = new DecimalFormat(doubleFormat);

    public void format(DocSimLatticeStatistics statistics, String fileName)
      throws IOException {
        BufferedWriter bw =
            new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

        format(statistics, bw);
        bw.close();
    }

    public String format(DocSimLatticeStatistics statistics)
      throws IOException {
        StringWriter sw = new StringWriter();
        format(statistics, sw);
        sw.close();
        return sw.getBuffer().toString();
    }

    /**
     * The original statistical data stored in parameter
     * <code>statistics</code> is not altered.
     *
     * @param statistics
     * @param writer
     */
    public void format(DocSimLatticeStatistics statistics, Writer writer)
      throws IOException {
        String result;
        StringBuilder sb = new StringBuilder();

        // result = formatStatLs(statistics.getRawStatistics());
        List<DocSimPairStatistics> originalStat = statistics.getRawStatistics();
        List<DocSimPairStatistics> backup = new ArrayList<DocSimPairStatistics>();

        backup.addAll(originalStat);
        int[] dims = {4, 3, 1, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(backup, dims));
        writer.write("\n[4 3 1 2]\n", 0, "\n[4 3 1 2]\n".length());
        writer.write(result, 0, result.length());
        writer.flush();

        backup.clear();
        backup.addAll(originalStat);
        int[] dims2 = {4, 1, 3, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(backup, dims2));
        writer.write("\n[4 1 3 2]\n", 0, "\n[4 1 3 2]\n".length());
        writer.write(result, 0, result.length());
        writer.flush();

        backup.clear();
        backup.addAll(originalStat);
        int[] dims3 = {3, 1, 4, 2};
        result = formatStatLs(
                DocSimLatticeStatistics.sort(backup, dims3));
        writer.write("\n[3 1 4 2]\n", 0, "\n[3 1 4 2]\n".length());
        writer.write(result, 0, result.length());
        writer.flush();
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
            sb.append("additional:" + serializeAddSim(stat));
            sb.append("\n");
            sb.append("class:" + (stat.getPositive()?"yes":"no"));
            sb.append("\n\n");
        }

        return sb.toString();
    }

    public String serializeAddSim(DocSimPairStatistics stat) {
        List<Double> additionalSimilarity = stat.getAdditionalSimilarity();

        StringBuilder sb = new StringBuilder();
        for (Iterator<Double> it = additionalSimilarity.iterator();
             it.hasNext();) {
            double sim = it.next();
            sb.append(doubleFormatter.format(sim));
            if (it.hasNext())
                sb.append(Additional_Result_Sep);
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
        List<DocSimPairStatistics> backup = new ArrayList<DocSimPairStatistics>();
        backup.addAll(statistics.getRawStatistics());

        List<DocSimPairStatistics> result = DocSimLatticeStatistics.sort(backup, dims);
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
            }
            DataEntry dentry =
                new DataEntry( oldsize, ac1/count, ac3/count, ac2/count, ac4/count);
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
        if (init)
            allStat.add(stat);
        is.close();
        return allStat;
    }


    static class DTreeArffEntry {
        public boolean clazz;
        public String docSrc1;
        public String docSrc2;

        public List<Pair> shinglingResults = new ArrayList<Pair>();

        public void addShinglingResult(int shinglesize,
                double result, ShingleAlgorithm shingleAlg) {
            if (shingleAlg != null)
                shinglingResults.add(new Pair(shinglesize, result, shingleAlg));
        }

        public void addAll(List<Pair> additionalResults) {
            this.shinglingResults.addAll(additionalResults);
        }

        public String serialize4DTree() {
            StringBuilder sb = new StringBuilder();
            for (Iterator<Pair> it = shinglingResults.iterator();
                    it.hasNext(); ) {
                Pair pair = it.next();
                sb.append(doubleFormatter.format(pair.result));
                if (it.hasNext()) {
                    sb.append(",");
                }
                // for (int i = 0 ; i < pair.additionalResults.size(); ++i) {
                //     double result = pair.additionalResults.get(i);
                //     sb.append(doubleFormatter.format(result));
                //     if ( i < pair.additionalResults.size() - 1 )
                //         sb.append(", ");
                // }
            }
            sb.append("," + (clazz?"yes":"no"));
            return sb.toString();
        }
        public void sortShinglingResults() {
            Collections.sort(shinglingResults, new Comparator<Pair>() {
                public int compare(Pair pair1, Pair pair2) {
                    int sa1, sa2;
                    switch (pair1.shingleAlg) {
                        case Original:
                            sa1 = 1;
                            break;
                        case IgnoreOrder:
                            sa1 = 2;
                            break;
                        default:
                            sa1 = 3;
                    }
                    switch (pair2.shingleAlg) {
                        case Original:
                            sa2 = 1;
                            break;
                        case IgnoreOrder:
                            sa2 = 2;
                            break;
                        default:
                            sa2 = 3;
                    }
                    if (sa1 == sa2 ) {
                        if (pair1.shinglesize == pair2.shinglesize) {
                            return 0;
                        } else {
                            return pair1.shinglesize < pair2.shinglesize ? -1 : 1;
                        }
                    } else {
                        return sa1<sa2?-1:1;
                    }
                }
                public boolean equals(Object object) {
                    return this == object;
                }
            });
        }
    }

    static class Pair {
        public int shinglesize = -1;
        public double result = 0.0;
        public ShingleAlgorithm shingleAlg = null;
        // public List<Double> additionalResults = new ArrayList<Double>();

        // public void addAdditionalResult(double result) {
        //     additionalResults.add(result);
        // }

        public Pair(int shinglesize, double result,
                ShingleAlgorithm shingleAlg) {
                // List<Double> additionalResults) {
            this.shinglesize = shinglesize;
            this.result = result;
            this.shingleAlg = shingleAlg;
            // this.additionalResults.clear();
            // this.additionalResults.addAll(additionalResults);
        }
    }

    public static void toArffFile(DocSimLatticeStatistics statistics,
            String baseFileName) throws Exception {
        StringBuilder originalSb = new StringBuilder();
        StringBuilder ignoreOrderSb = new StringBuilder();
        StringBuilder mixSb = new StringBuilder();
        toArffFormatString(statistics, originalSb, ignoreOrderSb, mixSb);
        FileUtils.writeStringToFile(new File(baseFileName+".original.arff"),
                originalSb.toString(),"UTF-8");
        FileUtils.writeStringToFile(new File(baseFileName+".ignoreorder.arff"),
                ignoreOrderSb.toString(), "UTF-8");
        FileUtils.writeStringToFile(new File(baseFileName+".mix.arff"),
                mixSb.toString(), "UTF-8");
    }

    /**
     * Convert data to arff format which is used by Weka project.
     * See http://weka.wiki.sourceforge.net/ARFF for details.
     *
     * @return
     */
    public static void toArffFormatString(DocSimLatticeStatistics statistics,
            StringBuilder originalSb, StringBuilder ignoreOrderSb,
            StringBuilder mixSb) {

        List<DocSimPairStatistics> statLs = statistics.getRawStatistics();
        List<DocSimPairStatistics> backup = new ArrayList<DocSimPairStatistics>();
        backup.addAll(statLs);

        Set<Integer> shingleSizes = new HashSet<Integer>();

        // Separate original shingling alg and ignore-order shingling alg
        List<DTreeArffEntry> originalEntries = new ArrayList<DTreeArffEntry>();
        List<DTreeArffEntry> ignoreOrderEntries = new ArrayList<DTreeArffEntry>();
        while (true) {
            Iterator<DocSimPairStatistics> it= backup.iterator();
            if (!it.hasNext())
                break;

            DTreeArffEntry entry = new DTreeArffEntry();
            DocSimPairStatistics stat = it.next();
            it.remove();
            String docSrc1 = stat.getDocSrc1();
            String docSrc2 = stat.getDocSrc2();

            shingleSizes.add(stat.getShingleSize());
            entry.addShinglingResult(
                    stat.getShingleSize(),
                    stat.getDocShinglingRatioUnique(),
                    stat.getShingleAlg());

            //================= for LSA ==================
            List<Pair> additionalResults = new ArrayList<Pair>();
            Iterator<Double> itdouble = stat.getAdditionalSimilarity().iterator();
            while (itdouble.hasNext()) {
                Double v = itdouble.next();
                additionalResults.add(new Pair(-1, v, ShingleAlgorithm.LSA));
            }
            //================= for LSA end ==================

            for (Iterator<DocSimPairStatistics> it2 = backup.iterator();
                 it2.hasNext(); ) {
                DocSimPairStatistics alt = it2.next();
                if (docSrc1.equals(alt.getDocSrc1()) &&
                    docSrc2.equals(alt.getDocSrc2()) &&
                    stat.getPositive() == alt.getPositive() &&
                    stat.getShingleAlg() == alt.getShingleAlg() ) {
                    entry.addShinglingResult(
                            alt.getShingleSize(),
                            alt.getDocShinglingRatioUnique(),
                            alt.getShingleAlg());

                    it2.remove();
                    shingleSizes.add(alt.getShingleSize());
                }
            }

            entry.sortShinglingResults();
            entry.clazz = stat.getPositive();
            entry.docSrc1 = docSrc1;
            entry.docSrc2 = docSrc2;

            entry.addAll(additionalResults);
            if (stat.getShingleAlg() == ShingleAlgorithm.Original) {
                originalEntries.add(entry);
            }
            else if (stat.getShingleAlg() == ShingleAlgorithm.IgnoreOrder) {
                ignoreOrderEntries.add(entry);
            }
        }


        // Mix original shingling alg and ignore-order shingling alg
        backup.clear();
        backup.addAll(statLs);
        List<DTreeArffEntry> mixEntries = new ArrayList<DTreeArffEntry>();

        int nAddResults = 0;
        while (true) {
            Iterator<DocSimPairStatistics> it= backup.iterator();
            if (!it.hasNext())
                break;

            DTreeArffEntry entry = new DTreeArffEntry();
            DocSimPairStatistics stat = it.next();
            it.remove();
            String docSrc1 = stat.getDocSrc1();
            String docSrc2 = stat.getDocSrc2();

            entry.addShinglingResult(stat.getShingleSize(),
                    stat.getDocShinglingRatioUnique(),
                    stat.getShingleAlg());

            //================= for LSA ==================
            List<Pair> additionalResults = new ArrayList<Pair>();
            Iterator<Double> itdouble = stat.getAdditionalSimilarity().iterator();
            while (itdouble.hasNext()) {
                Double v = itdouble.next();
                additionalResults.add(new Pair(-1, v, ShingleAlgorithm.LSA));
                nAddResults = additionalResults.size();
            }
            //================= for LSA end ==================

            for (Iterator<DocSimPairStatistics> it2 = backup.iterator();
                 it2.hasNext(); ) {
                DocSimPairStatistics alt = it2.next();
                if (docSrc1.equals(alt.getDocSrc1()) &&
                    docSrc2.equals(alt.getDocSrc2()) &&
                    stat.getPositive() == alt.getPositive()) {
                    entry.addShinglingResult(
                            alt.getShingleSize(),
                            alt.getDocShinglingRatioUnique(),
                            alt.getShingleAlg());
                    it2.remove();
                }
            }

            entry.addAll(additionalResults);
            entry.sortShinglingResults();
            entry.clazz = stat.getPositive();
            entry.docSrc1 = docSrc1;
            entry.docSrc2 = docSrc2;
            mixEntries.add(entry);
        }


        List<Integer> shingleSizeLs = new ArrayList<Integer>();
        shingleSizeLs.addAll(shingleSizes);
        Collections.sort(shingleSizeLs, new Comparator<Integer>() {
            public int compare(Integer int1, Integer int2) {
                if (int1 == int2) return 0;
                return int1 < int2 ? -1 : 1;
            }
            public boolean equals(Object object) {
                return this == object;
            }
        });

        // Following output should be consistent to the sort implementation of
        // class DTreeArffEntry
        originalSb.append("@relation docsim-original-shingling");
        originalSb.append("\n\n");
        for (Iterator<Integer> it = shingleSizeLs.iterator(); it.hasNext(); ) {
            originalSb.append("@attribute shinglesize" + it.next() + " real\n");
        }
        for (int i = 0 ; i < nAddResults ; ++i) {
            originalSb.append("@attribute additionalSimilarity_" + (i+1) + " real\n");
        }
        originalSb.append("@attribute similar {yes, no}\n");
        originalSb.append("\n@data\n");
        originalSb.append(dTreeData2String(originalEntries));

        ignoreOrderSb.append("@relation docsim-ignore-order-shingling");
        ignoreOrderSb.append("\n\n");
        for (Iterator<Integer> it = shingleSizeLs.iterator(); it.hasNext(); ) {
            ignoreOrderSb.append("@attribute shinglesize" + it.next() + " real\n");
        }
        for (int i = 0 ; i < nAddResults ; ++i) {
            ignoreOrderSb.append("@attribute additionalSimilarity_" + (i+1) + " real\n");
        }
        ignoreOrderSb.append("@attribute similar {yes, no}\n");
        ignoreOrderSb.append("\n@data\n");
        ignoreOrderSb.append(dTreeData2String(ignoreOrderEntries));

        mixSb.append("@relation docsim-mix-shingling");
        mixSb.append("\n\n");
        for (Iterator<Integer> it = shingleSizeLs.iterator(); it.hasNext(); ) {
            mixSb.append("@attribute original-shinglesize" + it.next() + " real\n");
        }
        for (Iterator<Integer> it = shingleSizeLs.iterator(); it.hasNext(); ) {
            mixSb.append("@attribute ignore-order-shinglesize" + it.next() + " real\n");
        }
        for (int i = 0 ; i < nAddResults ; ++i) {
            mixSb.append("@attribute additionalSimilarity_" + (i+1) + " real\n");
        }
        mixSb.append("@attribute similar {yes, no}\n");
        mixSb.append("\n@data\n");
        mixSb.append(dTreeData2String(mixEntries));
    }

    private static String dTreeData2String(List<DTreeArffEntry> dtreeEntries) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<DTreeArffEntry> it = dtreeEntries.iterator();
             it.hasNext(); ){
            DTreeArffEntry entry = it.next();
            sb.append(entry.serialize4DTree() + "\n");
        }
        return sb.toString();
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
        } else if (key.equalsIgnoreCase("additional")) {
            String additionalResults = value.trim();
            if (additionalResults.length() > 0) {
                String[] results = value.split(Additional_Result_Sep);
                for ( int i = 0 ; i < results.length ; ++i ) {
                    stat.addAdditionalSimilarity(Double.valueOf(results[i]));
                }
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

