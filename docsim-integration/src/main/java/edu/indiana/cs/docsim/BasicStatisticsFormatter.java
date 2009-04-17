package edu.indiana.cs.docsim;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * basic formatter.
 *
 * @author
 * @version
 */
public class BasicStatisticsFormatter {

    public static Logger logger =
        Logger.getLogger(BasicStatisticsFormatter.class.getName());

    public String format(DocSimLatticeStatistics statistics) {
        String result;
        result = formatSortBySize(statistics);
        return result;
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

    private String formatStatLs(List<DocSimPairStatistics> statLs) {
        if (statLs == null)
            return "";
        StringBuilder sb = new StringBuilder();
        Iterator<DocSimPairStatistics> it;
        for (it = statLs.iterator(); it.hasNext(); ) {
            DocSimPairStatistics stat = it.next();
            sb.append("shinglesize:" + stat.getShingleSize());
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
            logger.info("ratio:" + stat.getDocShinglingRatioUnique());
            sb.append("\n\n");
        }

        return sb.toString();
    }
}

