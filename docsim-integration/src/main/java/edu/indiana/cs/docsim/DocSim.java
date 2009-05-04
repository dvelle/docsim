package edu.indiana.cs.docsim;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.Shingle;
import edu.indiana.cs.docsim.ShingleSet;
import edu.indiana.cs.docsim.ShingleTextParser;
import edu.indiana.cs.docsim.ShingleTextParserNoOrder;
import edu.indiana.cs.docsim.ShingleUnit;
import edu.indiana.cs.docsim.ShingleUnitBag;
import edu.indiana.cs.docsim.data.PageRepository;
import edu.indiana.cs.docsim.data.PageRepositoryShuffle;
import edu.indiana.cs.docsim.data.QueryEntry;
import edu.indiana.cs.docsim.data.SearchResultEntry;
import edu.indiana.cs.docsim.data.SearchResultEntry;
import edu.indiana.cs.docsim.htmlproc.DocFilterPipeLineDefault;
import edu.indiana.cs.docsim.htmlproc.DomTagListener;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerEmphTags;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerHeadingTags;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerRegistry;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerTitleTag;
import edu.indiana.cs.docsim.htmlproc.HtmlTagListenerFilter;
import edu.indiana.cs.docsim.htmlproc.HtmlTagRemoverFilter;
import edu.indiana.cs.docsim.htmlproc.stem.PaiceStemmerWrapper;
import edu.indiana.cs.docsim.htmlproc.stopword.PossessiveFilter;
import edu.indiana.cs.docsim.htmlproc.stopword.StopWordFilter;
import edu.indiana.cs.docsim.htmlproc.util.HttpUtil;


/**
 * Main class.
 *
 * @author
 * @version
 */
public class DocSim {

    private static Logger logger = Logger.getLogger(DocSim.class.getName());

    private static final int Combo_FirstTwo = 1;
    private static final int Combo_AllPairs = 2;

    private static LSASim lsaSim = new LSASim();

    public String preprocess(String src, DocSimTrainingConfig trainingConfig, boolean isRealData)
      throws Exception {

        final Config config = trainingConfig.getConfig();

        final DomTagListenerRegistry listenerReg = new DomTagListenerRegistry();
        final HtmlTagListenerFilter listenerFilter = new HtmlTagListenerFilter(listenerReg);

        if (config.includeFilter("default") || config.includeFilter("heading")) {
            final DomTagListener listenerHeading = new DomTagListenerHeadingTags();
            listenerReg.register(listenerHeading);
        }

        if (config.includeFilter("default") || config.includeFilter("title")) {
            final DomTagListener listenerTitle = new DomTagListenerTitleTag();
            listenerReg.register(listenerTitle);
        }

        if (config.includeFilter("default") || config.includeFilter("emph")) {
            final DomTagListener listenerEmph = new DomTagListenerEmphTags();
            listenerReg.register(listenerEmph);
        }

        DocFilterPipeLineDefault docFilterPipeLine = null;

        try {
            docFilterPipeLine = new DocFilterPipeLineDefault() {
                // protected void beforeInit() {
                @Override
                protected void init() {
                    try {
                        pipeline.add(listenerFilter);
                        // pipeline.add(new HtmlTagRemoverFilter());
                        // pipeline.add(new PossessiveFilter());
                        // pipeline.add(new StopWordFilter(true));
                        // pipeline.add(new PaiceStemmerWrapper());

                        if (config.getTagRemoval().equalsIgnoreCase("default")) {
                            pipeline.add(new HtmlTagRemoverFilter());
                        }
                        if (config.getPossessive().equalsIgnoreCase("default")) {
                            pipeline.add(new PossessiveFilter());
                        }
                        if (config.getStopword().equalsIgnoreCase("default")) {
                            pipeline.add(new StopWordFilter(true));
                        }
                        if (config.getStemmer().equalsIgnoreCase("default")) {
                            pipeline.add(new PaiceStemmerWrapper());
                        }
                    } catch(Exception ex) {
                        System.out.println(ex);
                        ex.printStackTrace();
                    }
                }
            };
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
            throw ex;
        }


        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        String result = null;
        if (!isRealData) {
            if (src.startsWith("res://")) {
                String filename = src;
                result = docFilterPipeLine.filter(filename, charset);
                // sb.append("\nGet data from file \"" + filename + "\" result:\n" + result);
            } else {
                try {
                    URL url = new URL(src);
                    result = docFilterPipeLine.filter(url);
                    // sb.append("\nGet data from url \"" + url + "\" result:\n" + result);
                } catch (MalformedURLException e) {
                    String filename = src;
                    result = docFilterPipeLine.filter(new File(filename), charset);
                    sb.append("\nGet data from file \"" + filename + "\" result:\n" + result);
                }
            }
        } else {
            result = docFilterPipeLine.filter(src);
        }


        // Object dataobj = listenerHeading.getData();
        // String output = "Heading listener:\n" + seralizeList(dataobj);
        // sb.append("\n" + output);

        // dataobj = listenerTitle.getData();
        // output = "Title listener:\n" + seralizeList(dataobj);
        // sb.append("\n" + output);

        // dataobj = listenerEmph.getData();
        // output = "Emph listener:\n" + seralizeList(dataobj);
        // sb.append("\n" + output);

        // logger.info("after preprocess:\n" + sb.toString());
        return result;
    }

    private String dataSumFile;

    public void applyShinglingOutFile(URL url1, URL url2,
            String  trainingConfigFile, String outputFile) throws Exception {
        String output = applyShinglingOutString(url1, url2, trainingConfigFile);
        FileUtils.writeStringToFile(new File(outputFile), output);
        // DocSimLatticeStatistics statistics = applyShingling(url1, url2, trainingConfigFile);
        // BasicStatisticsFormatter formatter = new BasicStatisticsFormatter();
        // formatter.format(statistics, outputFile);
    }

    public String applyShinglingOutString(URL url1, URL url2,
            String  trainingConfigFile) throws Exception {
        DocSimLatticeStatistics statistics = applyShingling(url1, url2, trainingConfigFile);

        BasicStatisticsFormatter formatter = new BasicStatisticsFormatter();
        String readable = formatter.format(statistics);

        StringBuilder originalSb = new StringBuilder();
        StringBuilder ignoreOrderSb = new StringBuilder();
        StringBuilder mixSb = new StringBuilder();
        BasicStatisticsFormatter.toArffFormatString(
                statistics, originalSb, ignoreOrderSb, mixSb);

        StringBuilder resultSb = new StringBuilder();
        resultSb.append(readable);
        resultSb.append("\n\n original shingling:\n\t" + originalSb);
        resultSb.append("\n\n ignore-order shingling:\n\t" + ignoreOrderSb);
        resultSb.append("\n\n mix:\n\t" + mixSb);

        return resultSb.toString();
    }

    public DocSimLatticeStatistics applyShingling(URL url1, URL url2,
            String  trainingConfigFile) throws Exception {
        DocSimTrainingConfig trainingConfig =
            new DocSimTrainingConfig(false, trainingConfigFile, false);
        return applyShingling(url1, url2, trainingConfig);
    }

    public DocSimLatticeStatistics applyShingling(URL url1, URL url2,
            DocSimTrainingConfig trainingConfig)
      throws Exception {

        boolean isRealData = false;
        DocSimLatticeStatistics allStat = new DocSimLatticeStatistics();

        Config config = trainingConfig.getConfig();
        int shingleSizeStart = config.getSizeStart();
        int shingleSizeStep  = config.getSizeStep();
        int shingleSizeStop  = config.getSizeStop();

        DocSimPairStatistics stat = null;
        for (int shinglesize = shingleSizeStart;
             shinglesize <= shingleSizeStop;
             shinglesize += shingleSizeStep){

             logger.info("shingle size :" + shinglesize);
             stat = applyShingleOriginalRemote(url1.toString(),
                     url2.toString(), shinglesize, trainingConfig,
                     isRealData);
             allStat.add(stat);

             stat = applyShingleNoOrderRemote(url1.toString(),
                     url2.toString(), shinglesize, trainingConfig,
                     isRealData);
             allStat.add(stat);

        }
        return allStat;
    }

    /**
     * Apply shingling algorithm and options are specified in
     * trainingConfigFile.
     *
     * @param trainingConfigFile
     *      pass null to use default configuration file.
     */
    public void applyShingling (String trainingConfigFile, String outputFileName)
      throws IOException, InvalidConfigFormatException,
      UnsupportedShinglingAlgorithmException, Exception {
        DocSimTrainingConfig trainingConfig =
            new DocSimTrainingConfig(false, trainingConfigFile);
        dataSumFile = trainingConfig.getDataFileName();
        DocSimLatticeStatistics statistics = applyShingling(trainingConfig);
        BasicStatisticsFormatter formatter = new BasicStatisticsFormatter();
        if (outputFileName == null)
            outputFileName = "results.txt";
        formatter.format(statistics, outputFileName);
    }

    private DocSimLatticeStatistics applyShingling(DocSimTrainingConfig trainingConfig)
      throws UnsupportedShinglingAlgorithmException, Exception{
        Config config = trainingConfig.getConfig();

        // calculate results using positive data points
        PageRepository pageRepo = trainingConfig.getPageRepo();
        DocSimLatticeStatistics stat1 = applyShingling(pageRepo,
                trainingConfig, true, Combo_AllPairs);

        // shuffle the original data set to get negative data points.
        // calculate results using generated negative data points
        PageRepositoryShuffle.shuffle(pageRepo);
        DocSimLatticeStatistics stat2 = applyShingling(pageRepo,
                trainingConfig, false, Combo_AllPairs);

        // results from both positive data set and negative data set are
        // merged
        return stat1.merge(stat2);
    }

    /**
     * @param pageRepo
     *      page repository. It is the data set used.
     * @param trainingConfig
     *      Configuration of the processing.
     * @param isPositive
     *      whether the data points in data set are positive or negative.
     * @param combo
     *        1: just select first two docs in each entry.
     *        2: select all pairs of docs in each entry.
     * @return
     *      results of running shingling algorithm.
     */
    private DocSimLatticeStatistics applyShingling(
            PageRepository pageRepo,
            DocSimTrainingConfig trainingConfig,
            boolean isPositive,
            int combo) throws UnsupportedShinglingAlgorithmException, Exception{

        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();

        DocSimLatticeStatistics allStat = new DocSimLatticeStatistics();

        for (Iterator<QueryEntry> it = queryEntries.iterator();
             it.hasNext(); ) {
            QueryEntry entry = it.next();
            String query = entry.getQuery();
            int resultCount = entry.getResultCount();
            List<SearchResultEntry> results = entry.getResults();
            resultCount = resultCount > results.size() ? results.size() : resultCount;
            if (resultCount < 2 ) { //not enough docs in this query entry
                continue;
            }

            SearchResultEntry result1 = null;
            SearchResultEntry result2 = null;

            // Two strategies are supported here
            //   1) pick the first two data points in a query result set.
            //   2) pick all pairs of data points in a query result set.
            //      So, assume the number of data points is N, the number of
            //      generated pairs would be N*(N-1)/2.
            switch (combo) {
                case Combo_FirstTwo:
                    result1 = results.get(0);
                    result2 = results.get(1);
                    applyShingling(result1, result2, trainingConfig,
                            isPositive, allStat);
                    break;
                case Combo_AllPairs:
                    for (int i = 0; i < resultCount-1; ++i) {
                        for (int j = i+1 ; j < resultCount; ++j) {
                            result1 = results.get(i);;
                            result2 = results.get(j);
                            applyShingling(result1, result2, trainingConfig,
                                    isPositive, allStat);
                        }
                    }
            }
        }
        return allStat;
    }

    /**
     *
     *
     * @param result1
     *      one search result entry. (basically one web page).
     * @param result2
     *      one search result entry. (basically one web page).
     * @param trainingConfig
     * @param isPositive
     *      whether the two data points are positive or negative.
     * @param allStat
     *      where the results would be written.
     */
    private void applyShingling( SearchResultEntry result1,
            SearchResultEntry result2,
            DocSimTrainingConfig trainingConfig,
            boolean isPositive,
            DocSimLatticeStatistics allStat) {

        Config config = trainingConfig.getConfig();
        int shingleSizeStart = config.getSizeStart();
        int shingleSizeStep  = config.getSizeStep();
        int shingleSizeStop  = config.getSizeStop();

        logger.info("\n\turl 1:" + result1.getUrl() +
                "\n\turl 2:" + result2.getUrl());

        //tell whether variables content1 and content2 contain url/path of
        //the data file where the data is stored, or the real data
        boolean isRealData = true;
        String content1 = null;
        String content2 = null;
        String src1 = result1.getUrl();
        String src2 = result2.getUrl();

        // Get content of the data points
        // Currently, two sources are supported:
        //  remote http URL and local file.
        if (src1.toLowerCase().startsWith("http:")) {
            try {
                content1 = HttpUtil.fetchPageHTTP(new URL(src1));
                content2 = HttpUtil.fetchPageHTTP(new URL(src2));
            } catch(Exception ex) {
                logger.severe(ex.toString());
                return;
            }
        } else {
            try {
                String path = "pages";
                if (dataSumFile != null)
                    path =FilenameUtils.getFullPath(dataSumFile); //with ending '/'
                content1 = FileUtils.readFileToString(new File(path + src1), "UTF-8") ;
                content2 = FileUtils.readFileToString(new File(path + src2), "UTF-8") ;
            } catch(Exception ex) {
                logger.severe(ex.toString());
                return;
            }
        }

        // ==========   for LSA   ==============
        double lsascore = lsaCalc(content1, content2);
        double lsascorecos = lsaCosCalc(content1, content2);
        if (Double.isNaN(lsascorecos))
            lsascorecos = 1.0;
        // logger.info("cos score: " + lsascorecos);
        // ==========   for LSA end  ==============

        // apply shingling algorithm using different shingle size
        for (int shinglesize = shingleSizeStart;
             shinglesize <= shingleSizeStop;
             shinglesize += shingleSizeStep){

            try {
                List<ShingleAlgorithm> shingleAlgs = config.getShingleAlgLs();
                for (Iterator<ShingleAlgorithm> it2 = shingleAlgs.iterator();
                     it2.hasNext() ; ){
                    ShingleAlgorithm shingleAlg = it2.next();
                    DocSimPairStatistics statistics =
                        applyShingle(content1, content2, shinglesize,
                                shingleAlg, trainingConfig, isRealData);
                    if (isRealData) {
                        statistics.setDocSrc1(result1.getUrl());
                        statistics.setDocSrc2(result2.getUrl());
                    }
                    if (statistics != null) {
                        statistics.setPositive(isPositive);

                        // ==========   for LSA   ==============
                        statistics.addAdditionalSimilarity(lsascore);
                        statistics.addAdditionalSimilarity(lsascorecos);
                        // ==========   for LSA end ==============

                        allStat.add(statistics);
                    }
                }
            } catch(Exception ex) {
                logger.warning("Exception was thrown during processing " +
                        "this pair of documents: \n" +
                        "\t" + src1 + "\n\t" + src2);
            }
        }

        /*
        if (lsaStat != null) {
            lsaStat.setPositive(isPositive);
            lsaStat.setDocSrc1(result1.getUrl());
            lsaStat.setDocSrc2(result2.getUrl());
            // TODO
            lsaStat.setShingleAlg(TODO);
            allStat.add(lsaStat);
        }
        */
    }

    public double lsaCalc(String content1, String content2) {
        // DocSimPairStatistics stat = new DocSimPairStatistics();
        // double lsascore = 10.0;
        double lsascore = lsaSim.LSAsimilarity(content1, content2);
        return lsascore;
    }

    public double lsaCosCalc(String content1, String content2) {
        // DocSimPairStatistics stat = new DocSimPairStatistics();
        // double lsascore = 10.0;
        double lsascore = lsaSim.COSsimilarity(content1, content2);
        return lsascore;
    }

    /**
     *
     *
     * @param dataPoint1
     *      one data point. It can be location of the data, or the real data.
     *      It is indicated by parameter <code>isRealData</code>
     * @param dataPoint2
     *      one data point. It can be location of the data, or the real data.
     *      It is indicated by parameter <code>isRealData</code>
     * @param shinglesize
     * @param shingleAlg
     *      Shingle algorithm to be used
     * @param trainingConfig
     * @param isRealData
     * @return
     * @throws UnsupportedShinglingAlgorithmException
     * @throws Exception
     */
    public DocSimPairStatistics applyShingle (
            String dataPoint1,
            String dataPoint2,
            int shinglesize,
            ShingleAlgorithm shingleAlg,
            DocSimTrainingConfig trainingConfig,
            boolean isRealData) throws UnsupportedShinglingAlgorithmException, Exception{

        if (shingleAlg == ShingleAlgorithm.IgnoreOrder) {
            return applyShingleNoOrderRemote(dataPoint1, dataPoint2,
                    shinglesize, trainingConfig, isRealData);
        } else if (shingleAlg == ShingleAlgorithm.Original) {
            return applyShingleOriginalRemote(dataPoint1, dataPoint2,
                    shinglesize, trainingConfig, isRealData);
        } else if (shingleAlg == ShingleAlgorithm.None) {
            return null;
        } else {
            throw new UnsupportedShinglingAlgorithmException("shingling algotirhtm " +
                    shingleAlg.toString() + " is not supported");
        }
    }

    /**
     *
     *
     * @param strUrl1
     * @param strUrl2
     * @param shinglesize
     * @return
     * @throws Exception
     */
    public DocSimPairStatistics applyShingleNoOrderRemote
        (String strUrl1,
         String strUrl2,
         int shinglesize,
         DocSimTrainingConfig trainingConfig,
         boolean isRealData)
        throws Exception {

        DocSimPairStatistics statistics = new DocSimPairStatistics();
        ShingleTextParserNoOrder shingleTxtParser = new ShingleTextParserNoOrder();

        // String strUrl1 = "http://cs.indiana.edu";
        // String strUrl2 = "http://www.informatics.indiana.edu/";
        String result1 = preprocess(strUrl1, trainingConfig, isRealData).trim();
        String result2 = preprocess(strUrl2, trainingConfig, isRealData).trim();

        if (result1 == null || result1.length() == 0 ||
            result2 == null || result2.length() == 0) {
            return null;
        }

        String charset = "UTF-8";

        ShingleSet shingleset1 = shingleTxtParser.parseText(result1, shinglesize, charset);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result2, shinglesize, charset);
        ShingleSet union = shingleset1.union(shingleset2);
        ShingleSet intersect = shingleset1.intersect(shingleset2);

        int size1 = shingleset1.sizeUnique();
        int size2 = shingleset2.sizeUnique();
        int sizeunion = union.sizeUnique();
        int sizeintersect = intersect.sizeUnique();

        if (!isRealData) {
             statistics.setDocSrc1(strUrl1);
             statistics.setDocSrc2(strUrl2);
        }
        statistics.setShingleSize(shinglesize);
        statistics.setDocShingleSetSizeUnique1(size1);
        statistics.setDocShingleSetSizeUnique2(size2);
        statistics.setDocShingleSetUnionSizeUnique(sizeunion);
        statistics.setDocShingleSetIntersectSizeUnique(sizeintersect);
        statistics.setShingleAlg(ShingleAlgorithm.IgnoreOrder);

        return statistics;
    }

    public DocSimPairStatistics applyShingleOriginalRemote (
            String strUrl1, String strUrl2, int shinglesize,
            DocSimTrainingConfig trainingConfig, boolean isRealData)
      throws Exception {
        DocSimPairStatistics statistics = new DocSimPairStatistics();
        ShingleTextParser shingleTxtParser = new ShingleTextParser();

        // String strUrl1 = "http://cs.indiana.edu";
        // String strUrl2 = "http://www.informatics.indiana.edu/";
        String result1 = preprocess(strUrl1, trainingConfig, isRealData);
        String result2 = preprocess(strUrl2, trainingConfig, isRealData);

        if (result1 == null || result1.length() == 0 ||
            result2 == null || result2.length() == 0) {
            return null;
        }

        String charset = "UTF-8";

        ShingleSet shingleset1 = shingleTxtParser.parseText(result1, shinglesize, charset);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result2, shinglesize, charset);
        ShingleSet union = shingleset1.union(shingleset2);
        ShingleSet intersect = shingleset1.intersect(shingleset2);

        int size1 = shingleset1.sizeUnique();
        int size2 = shingleset2.sizeUnique();
        int sizeunion = union.sizeUnique();
        int sizeintersect = intersect.sizeUnique();

        if (!isRealData) {
             statistics.setDocSrc1(strUrl1);
             statistics.setDocSrc2(strUrl2);
        }
        statistics.setShingleSize(shinglesize);
        statistics.setDocShingleSetSizeUnique1(size1);
        statistics.setDocShingleSetSizeUnique2(size2);
        statistics.setDocShingleSetUnionSizeUnique(sizeunion);
        statistics.setDocShingleSetIntersectSizeUnique(sizeintersect);
        statistics.setShingleAlg(ShingleAlgorithm.Original);

        return statistics;
    }
}

