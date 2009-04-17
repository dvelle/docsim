package edu.indiana.cs.docsim;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.Shingle;
import edu.indiana.cs.docsim.ShingleSet;
import edu.indiana.cs.docsim.ShingleTextParserNoOrder;
import edu.indiana.cs.docsim.ShingleUnit;
import edu.indiana.cs.docsim.ShingleUnitBag;

import edu.indiana.cs.docsim.data.PageRepository;
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
import edu.indiana.cs.docsim.htmlproc.stopword.PossessiveFilter;
import edu.indiana.cs.docsim.htmlproc.stopword.StopWordFilter;
import edu.indiana.cs.docsim.htmlproc.stem.PaiceStemmerWrapper;
import java.net.MalformedURLException;


/**
 * Main class.
 *
 * @author
 * @version
 */
public class DocSim {

    private static Logger logger = Logger.getLogger(DocSim.class.getName());

    // private DocFilterPipeLineDefault docFilterPipeLine;

    /*
    public void testPipeLineAndShingleLocal() throws Exception {
        String filename = "res://augmented_page.html";
        // First apply pipe line of filters
        String result = filter(filename);
        assertNotNull(result);

        ShingleTextParserNoOrder shingleTxtParser = new ShingleTextParserNoOrder();

        // build shingle set
        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        int shinglesize = 5;
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        int size = shingleset.sizeUnique();
        sb.append("\nShingle size: " + shinglesize);
        sb.append("\nSize of shingle set: " + size);
        logger.info(sb.toString());
    }*/

    public String preprocess(String src, DocSimTrainingConfig trainingConfig) throws Exception {

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
                result = docFilterPipeLine.filter(filename, charset);
                sb.append("\nGet data from file \"" + filename + "\" result:\n" + result);
            }
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

        logger.info("after preprocess:\n" + sb.toString());
        return result;
    }

    public void applyShingling (String trainingConfigFile)
      throws IOException, InvalidConfigFormatException,
        UnsupportedShinglingAlgorithmException, Exception {

        DocSimTrainingConfig trainingConfig =
            new DocSimTrainingConfig(true, null);
        DocSimLatticeStatistics statistics = applyShingling(trainingConfig);
        BasicStatisticsFormatter formatter = new BasicStatisticsFormatter();
        String result = formatter.format(statistics);
        logger.info("--------   result  ---------\n" +
                    result +
                    "-------    result end  ---------");
    }

    private DocSimLatticeStatistics applyShingling(DocSimTrainingConfig trainingConfig)
      throws UnsupportedShinglingAlgorithmException, Exception{
        Config config = trainingConfig.getConfig();
        PageRepository pageRepo = trainingConfig.getPageRepo();
        List<QueryEntry> queryEntries = pageRepo.getQueryEntries();
        ShingleAlgorithm shingleAlg = config.getShingleAlg();

        int shingleSizeStart = config.getSizeStart();
        int shingleSizeStep  = config.getSizeStep();
        int shingleSizeStop  = config.getSizeStop();

        DocSimLatticeStatistics allStat = new DocSimLatticeStatistics();

        for (Iterator<QueryEntry> it = queryEntries.iterator();
             it.hasNext(); ) {
            QueryEntry entry = it.next();
            String query = entry.getQuery();
            int resultCount = entry.getResultCount();
            List<SearchResultEntry> results = entry.getResults();
            if (resultCount < 2 ) { //not enough docs in this query entry
                continue;
            }
            SearchResultEntry result1 = results.get(0);
            SearchResultEntry result2 = results.get(1);
            for (int shinglesize = shingleSizeStart;
                 shinglesize <= shingleSizeStop;
                 shinglesize += shingleSizeStep){

                try {
                    DocSimPairStatistics statistics =
                        applyShingle(result1.getUrl(), result2.getUrl(), shinglesize, trainingConfig);

                    allStat.add(statistics);
                } catch(Exception ex) {
                    logger.warning("Exception was thrown during processing " +
                            " this pair of documents: \n" +
                            "\t" + result1.getUrl() +
                            "\n\t" + result2.getUrl());
                }
            }
        }
        return allStat;
    }

    public DocSimPairStatistics applyShingle (
        String strUrl1, String strUrl2, int shinglesize, DocSimTrainingConfig trainingConfig)
        throws UnsupportedShinglingAlgorithmException, Exception{

        ShingleAlgorithm shingleAlg = trainingConfig.getShingleAlg();

        if (shingleAlg == ShingleAlgorithm.IgnoreOrder) {
            return applyShingleNoOrderRemote(strUrl1, strUrl2, shinglesize, trainingConfig);
        } else if (shingleAlg == ShingleAlgorithm.Original) {
            return applyShingleOriginalRemote(strUrl1, strUrl2, shinglesize, trainingConfig);
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
        (String strUrl1, String strUrl2, int shinglesize, DocSimTrainingConfig trainingConfig)
        throws Exception {

        DocSimPairStatistics statistics = new DocSimPairStatistics();
        ShingleTextParserNoOrder shingleTxtParser = new ShingleTextParserNoOrder();

        // String strUrl1 = "http://cs.indiana.edu";
        // String strUrl2 = "http://www.informatics.indiana.edu/";
        String result1 = preprocess(strUrl1, trainingConfig);
        String result2 = preprocess(strUrl2, trainingConfig);

        String charset = "UTF-8";

        ShingleSet shingleset1 = shingleTxtParser.parseText(result1, shinglesize, charset);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result2, shinglesize, charset);
        ShingleSet union = shingleset1.union(shingleset2);
        ShingleSet intersect = shingleset1.intersect(shingleset2);

        int size1 = shingleset1.sizeUnique();
        int size2 = shingleset2.sizeUnique();
        int sizeunion = union.sizeUnique();
        int sizeintersect = intersect.sizeUnique();

        statistics.setDocSrc1(strUrl1);
        statistics.setDocSrc2(strUrl2);
        statistics.setShingleSize(shinglesize);
        statistics.setDocShingleSetSizeUnique1(size1);
        statistics.setDocShingleSetSizeUnique2(size2);
        statistics.setDocShingleSetUnionSizeUnique(sizeunion);
        statistics.setDocShingleSetIntersectSizeUnique(sizeintersect);
        statistics.setShingleAlg(ShingleAlgorithm.IgnoreOrder);

        return statistics;
    }

    public DocSimPairStatistics applyShingleOriginalRemote (
            String strUrl1, String strUrl2, int shinglesize, DocSimTrainingConfig trainingConfig)
      throws Exception {
        return null;
    }

    /*
    public void testPipeLineAndShingleRemote() throws Exception {
        // String strUrl = "http://cs.indiana.edu";
        String strUrl = "http://www.informatics.indiana.edu/";
        String result = filter(strUrl);
        ShingleTextParserNoOrder shingleTxtParser = new ShingleTextParserNoOrder();

        // build shingle set
        StringBuilder sb = new StringBuilder();
        String charset = "UTF-8";
        int shinglesize = 5;
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        int size = shingleset.sizeUnique();
        sb.append("\nShingle size: " + shinglesize);
        sb.append("\nSize of shingle set: " + size);
        logger.info(sb.toString());
    }
    */

    private String seralizeList(Object dataobj) {
        StringBuilder sb = new StringBuilder();
        if (dataobj instanceof List) {
            List<String> datals = (List<String>)dataobj;
            for (Iterator<String> it = datals.iterator(); it.hasNext();) {
                sb.append(it.next() + "\n");
            }
        } else {
            logger.severe("A list is expected!!!");
            return "";
        }
        return sb.toString();
    }
}

