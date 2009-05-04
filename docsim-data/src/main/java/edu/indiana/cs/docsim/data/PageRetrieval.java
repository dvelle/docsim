package edu.indiana.cs.docsim.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.FilenameUtils;

import edu.indiana.cs.docsim.htmlproc.DocFilterPipeLineDefault;
import edu.indiana.cs.docsim.htmlproc.DomTagListener;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerEmphTags;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerHeadingTags;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerRegistry;
import edu.indiana.cs.docsim.htmlproc.DomTagListenerTitleTag;
import edu.indiana.cs.docsim.htmlproc.HtmlTagListenerFilter;
import edu.indiana.cs.docsim.htmlproc.HtmlTagRemoverFilter;
import edu.indiana.cs.docsim.htmlproc.LowerCaseFilter;
import edu.indiana.cs.docsim.htmlproc.stem.PaiceStemmerWrapper;
import edu.indiana.cs.docsim.htmlproc.stopword.PossessiveFilter;
import edu.indiana.cs.docsim.htmlproc.stopword.StopWordFilter;

public class PageRetrieval {
    private PageRepository pageRepo;
    private String outputDir;
    private String outputXmlFile;
    private static Logger logger =
        Logger.getLogger(PageRetrieval.class.getName());

    // public PageRetrieval () throws IOException {
    //     pageRepo = new PageRepository(true);
    // }
    // public PageRetrieval(String configFile) throws IOException {
    //     pageRepo = new PageRepository(configFile);
    // }

    public PageRetrieval(String configFile, String outputDir, String outputXmlFile)
      throws IOException, Exception {
        if (configFile == null) {
            pageRepo = new PageRepository(true);
        } else {
            pageRepo = new PageRepository(configFile);
        }
        if (outputDir.endsWith("/"))
            this.outputDir = outputDir;
        else
            this.outputDir = outputDir + "/";
        this.outputXmlFile = outputXmlFile;
    }

    public void retrieve() {
        Config config = buildDefaultConfig();
        StringBuilder lsaData = new StringBuilder();    //for Jiayi's LSA prog
        StringBuilder integrateData = new StringBuilder();//for shingling algorithms
        String result = serialize2XML(config, lsaData, integrateData);
        write2File(result, outputDir + outputXmlFile);
        String baseName = FilenameUtils.removeExtension(outputXmlFile);
        write2File(lsaData.toString(), outputDir + baseName + ".lsa");
        write2File(integrateData.toString(), outputDir + baseName + ".shingle");
    }

    public String serialize2XML(Config config,
            StringBuilder sb1,
            StringBuilder sb2) {

        if (pageRepo == null)
            return "";
        List<QueryEntry> qEntries = pageRepo.getQueryEntries();
        Iterator<QueryEntry> it = qEntries.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(xmlDecl);
        sb.append("\n\n<Docset>\n");
        while (it.hasNext()) {
            sb.append("\t<Result>\n");
            QueryEntry entry = it.next();
            logger.info("qeury:" + entry.getQuery());
            sb.append("\t\t<Querystring>" + entry.getQuery() + "</Querystring>\n");
            // sb.append("\t\t<Doc>\n");
            List<SearchResultEntry> results = entry.getResults();
            Iterator<SearchResultEntry> it2 = results.iterator();
            StringBuilder buf2 = new StringBuilder();
            int count = 0;
            while (it2.hasNext()) {
                StringBuilder buf = new StringBuilder();
                buf.append("\t\t<Doc>\n");
                SearchResultEntry srentry = it2.next();
                buf.append("\t\t\t<Url>\n");
                String strUrl = srentry.getUrl();
                String strTitle = srentry.getTitle();
                String baseName = (++cursor) + ".html";
                String fileName = outputDir + baseName;
                File file = new File(fileName);
                logger.info("\nurl:"+ strUrl +
                        "\nabs path:" + file.getAbsolutePath());
                fileName = file.getAbsolutePath();
                if (!preprocessAndWrite2File(strUrl, fileName, config)) {
                    --cursor;
                    continue;
                }

                ++count;
                // String content;
                // try {
                //     content = preprocess(strUrl, config);
                //     if (content.trim().length() == 0)
                //         continue;
                // } catch(Exception ex) {
                //     continue;
                // }
                buf.append("\t\t\t" + strUrl + "\n");
                buf.append("\t\t\t</Url>\n");
                buf.append("\t\t\t<Data>\n");
                buf.append("\t\t\t" + baseName + "\n");
                // buf.append("\t\t\t" + content + "\n");
                buf.append("\t\t\t</Data>\n");
                buf.append("\t\t</Doc>\n");
                sb.append(buf);

                sb1.append("Url:" + strUrl);
                sb1.append("\nDoc:" + baseName + "\n\n");

                buf2.append("\nTitle:" + strTitle);
                buf2.append("\nUrl:" + baseName);
            }
            sb2.append("Query:" + entry.getQuery());
            sb2.append("\nTotalResult:" + count);
            sb2.append(buf2);
            sb2.append("\n\n");
            // sb.append("\t</Docs>\n");
            sb.append("\t</Result>\n\n");
        }
        sb.append("\n</Docset>");
        return sb.toString();
    }

    public boolean preprocessAndWrite2File(String content, String fileName, Config config) {
        try {
            content = preprocess(content, config);
            return write2File(content, fileName);
        } catch(Exception ex) {
            logger.warning("Error during processing of the html and writing it to a locla file\n" + ex);
        }
        return false;
    }

    public boolean write2File(String content, String fileName) {
        try {
            if (content != null && content.trim().length() != 0) {
                FileUtils.writeStringToFile(new File(fileName), content, "UTF-8");
                return true;
            }
        } catch(Exception ex) {
            logger.warning("error during writing data to file '" + fileName + "'\n" + ex);
        }
        return false;
    }

    /*
    private String fetchPageHTTP (URL url) {
        String page = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url.toString());
        method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                logger.severe("Method failed: " + method.getStatusLine());
            }

            // page = method.getResponseBodyAsString();
            InputStream is = method.getResponseBodyAsStream();
            page = IOUtils.toString(is, "UTF-8");

        } catch (HttpException e) {
            logger.severe("Fatal protocol violation: " + e);
        } catch (IOException e) {
            logger.severe("Fatal transport error: " + e);
        } finally {
            method.releaseConnection();
        }
        return page;
    }
    */

    private static String xmlDecl = "<? xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    private static int cursor = 0;

    public static void main (String []args) throws Exception {
        Options options = new Options();
        options.addOption("d", "output-directory", true,
                "output directory. By default, it is current directory.");
        options.addOption("o", "output-file", true,
                "output xml file. By default it is output.xml");
        options.addOption("c", "config", true,
                "Specify the configuration file. By default, it is searchResult.txt");
        options.addOption("h", "help", false, "display help document");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        String outputDir = ".";
        String configFileName = null;
        String outputXmlFile = "output.xml";
        if (cmd.hasOption("c")) {
            configFileName = cmd.getOptionValue("c");
        }
        if (cmd.hasOption("d")) {
            outputDir = cmd.getOptionValue("d");
        }
        if (cmd.hasOption("o")) {
            outputXmlFile = cmd.getOptionValue("o");
        }
        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "page retrieval", options );
            return;
        }
        PageRetrieval pageRetrieval =
            new PageRetrieval(configFileName, outputDir, outputXmlFile);
        pageRetrieval.retrieve();
    }

    private Config buildDefaultConfig() {
        Config config = new Config();
        // config.setDataFile(dataFile);

        // This option does not matter
        // because we don't apply shingling algorithm
        config.setShingleAlgRaw("none");
        // config.setDataFile(configFileName);

        config.setTagRemoval("default");
        config.setPossessive("default");
        config.setStopword("default");
        config.setStemmer("none");
        config.setFilters("");

        // config.setPossessive("none");
        // config.setStopword("none");
        config.setStemmer("none");
        config.setFilters("");
        return config;
    }

    public String preprocess(String src, Config paramConfig) throws Exception {

        final Config config = paramConfig;

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
                @Override
                protected void init() {
                    try {
                        pipeline.add(new LowerCaseFilter());

                        pipeline.add(listenerFilter);

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
                result = docFilterPipeLine.filter(new File(filename), charset);
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

        // logger.info("after preprocess:\n" + sb.toString());
        return result;
    }
}


class Config {
    private static Logger logger =
        Logger.getLogger(Config.class.getName());

    private static String keyDataFile           = "datafile";
    private static String keyShingle            = "shingle-method";
    private static String keyShingleSizeRange   = "size-range";
    private static String keyStopWord           = "stopword";
    private static String keyStemmer            = "stemmer";
    private static String keyPossessive         = "possessive";
    private static String keyTagRemoval         = "tag-removal";
    private static String keyFilters            = "filters";
    private static String filterSep             = ",";
    private static String sizeRangeSep          = ",";

    private String dataFile;
    private String shingleAlgRaw;
    private ShingleAlgorithm shingleAlg;
    private String stopword;
    private String stemmer;
    private String filters;
    private String possessive;
    private String tagRemoval;
    private List<String> filterLs = new ArrayList<String>();

    private int sizeStart;
    private int sizeStep;
    private int sizeStop;
    /**
     * get the value of tagRemoval
     * @return the value of tagRemoval
     */
    public String getTagRemoval(){
        return this.tagRemoval;
    }
    /**
     * set a new value to tagRemoval
     * @param tagRemoval the new value to be used
     */
    public void setTagRemoval(String tagRemoval) {
        this.tagRemoval=tagRemoval;
    }

    /**
     * get the value of sizeStart
     * @return the value of sizeStart
     */
    public int getSizeStart(){
        return this.sizeStart;
    }
    /**
     * set a new value to sizeStart
     * @param sizeStart the new value to be used
     */
    public void setSizeStart(int sizeStart) {
        this.sizeStart=sizeStart;
    }
    /**
     * get the value of sizeStep
     * @return the value of sizeStep
     */
    public int getSizeStep(){
        return this.sizeStep;
    }
    /**
     * set a new value to sizeStep
     * @param sizeStep the new value to be used
     */
    public void setSizeStep(int sizeStep) {
        this.sizeStep=sizeStep;
    }
    /**
     * get the value of sizeStop
     * @return the value of sizeStop
     */
    public int getSizeStop(){
        return this.sizeStop;
    }
    /**
     * set a new value to sizeStop
     * @param sizeStop the new value to be used
     */
    public void setSizeStop(int sizeStop) {
        this.sizeStop=sizeStop;
    }

    /**
     * get the value of filterLs
     * @return the value of filterLs
     */
    public List<String> getFilterLs(){
        return this.filterLs;
    }
    /**
     * set a new value to filterLs
     * @param filterLs the new value to be used
     */
    public void setFilterLs(List<String> filterLs) {
        this.filterLs=filterLs;
    }
    /**
     * get the value of stopword
     * @return the value of stopword
     */
    public String getStopword(){
        return this.stopword;
    }
    /**
     * set a new value to stopword
     * @param stopword the new value to be used
     */
    public void setStopword(String stopword) {
        this.stopword=stopword;
    }
    /**
     * get the value of stemmer
     * @return the value of stemmer
     */
    public String getStemmer(){
        return this.stemmer;
    }
    /**
     * set a new value to stemmer
     * @param stemmer the new value to be used
     */
    public void setStemmer(String stemmer) {
        this.stemmer=stemmer;
    }
    /**
     * get the value of filters
     * @return the value of filters
     */
    public String getFilters(){
        return this.filters;
    }
    /**
     * set a new value to filters
     * @param filters the new value to be used
     */
    public void setFilters(String filters) {
        this.filters=filters;
        if (filters != null) {
            String[] filterArr = filters.split(filterSep);
            for (int i = 0 ; i < filterArr.length ; ++i) {
                String filter = filterArr[i];
                filter = filter.trim();
                if (filter.length() == 0) continue;
                this.getFilterLs().add(filter);
            }
        }
    }

    /**
     * get the value of shingleAlg
     * @return the value of shingleAlg
     */
    public ShingleAlgorithm getShingleAlg(){
        return this.shingleAlg;
    }
    /**
     * set a new value to shingleAlg
     * @param shingleAlg the new value to be used
     */
    public void setShingleAlg(ShingleAlgorithm shingleAlg) {
        this.shingleAlg=shingleAlg;
    }
    /**
     * get the value of shingleAlgRaw
     * @return the value of shingleAlgRaw
     */
    public String getShingleAlgRaw(){
        return this.shingleAlgRaw;
    }
    /**
     * set a new value to shingleAlgRaw
     * @param shingleAlgRaw the new value to be used
     */
    public void setShingleAlgRaw(String shingleAlgRaw) {
        this.shingleAlgRaw=shingleAlgRaw;
        ShingleAlgorithm shingleAlg = ShingleAlgorithm.getValue(shingleAlgRaw);
        if (shingleAlg == null) {
            shingleAlg = ShingleAlgorithm.defaultAlg();
            shingleAlgRaw = ShingleAlgorithm.defaultAlgRaw();
        }
    }
    /**
     * get the value of dataFile
     * @return the value of dataFile
     */
    public String getDataFile(){
        return this.dataFile;
    }
    /**
     * set a new value to dataFile
     * @param dataFile the new value to be used
     */
    public void setDataFile(String dataFile) {
        this.dataFile=dataFile;
    }

    public Config() {}
    public Config(Properties properties)
      throws InvalidConfigFormatException {
        dataFile = properties.getProperty(keyDataFile);
        shingleAlgRaw = properties.getProperty(keyShingle);
        shingleAlg = ShingleAlgorithm.getValue(shingleAlgRaw);
        if (shingleAlg == null) {
            shingleAlg = ShingleAlgorithm.defaultAlg();
            shingleAlgRaw = ShingleAlgorithm.original;
        }
        stopword = properties.getProperty(keyStopWord);
        stemmer = properties.getProperty(keyStemmer);
        filters = properties.getProperty(keyFilters);
        if (filters != null) {
            String[] filterArr = filters.split(filterSep);
            for (int i = 0 ; i < filterArr.length ; ++i) {
                String filter = filterArr[i];
                filter = filter.trim();
                if (filter.length() == 0) continue;
                this.getFilterLs().add(filter);
            }
        }
        String sizeRange = properties.getProperty(keyShingleSizeRange);
        String[] comps = sizeRange.split(sizeRangeSep);
        if (comps.length != 3)
            throw new InvalidConfigFormatException(errorTextInvalidSizeRange);
        try {
            sizeStart = Integer.valueOf(comps[0].trim());
            sizeStep  = Integer.valueOf(comps[1].trim());
            sizeStop  = Integer.valueOf(comps[2].trim());
        } catch(Exception ex) {
            throw new InvalidConfigFormatException(errorTextInvalidSizeRange);
        }
        possessive = properties.getProperty(keyPossessive);
        tagRemoval = properties.getProperty(keyTagRemoval);
    }

    /**
     * get the value of possessive
     * @return the value of possessive
     */
    public String getPossessive(){
        return this.possessive;
    }
    /**
     * set a new value to possessive
     * @param possessive the new value to be used
     */
    public void setPossessive(String possessive) {
        this.possessive=possessive;
    }

    public boolean includeFilter(String filter) {
        Iterator<String> it = filterLs.iterator();
        while (it.hasNext()) {
            String fname = it.next();
            if (fname.equalsIgnoreCase(filter)) {
                return true;
            }
        }
        return false;
    }

    private static String errorTextInvalidSizeRange
        = "invalid size range configuration";
}

enum ShingleAlgorithm {
    Original, IgnoreOrder, Weighted, None;

    public static String original = "original";
    public static String none = "original";
    public static String weighted = "weighted";
    public static String ignoreOrder = "ignore-order";

    public static ShingleAlgorithm getValue(String value) {
        if (value == null) return null;
        if (value.equalsIgnoreCase(original)) {
            return Original;
        } else if (value.equalsIgnoreCase(weighted)) {
            return Weighted;
        } else if (value.equalsIgnoreCase(ignoreOrder)) {
            return IgnoreOrder;
        } else if (value.equalsIgnoreCase(none)) {
            return None;
        } else {
            return null;
        }
    }

    public static ShingleAlgorithm defaultAlg() {
        return Original;
    }
    public static String defaultAlgRaw() {
        return original;
    }
}

class InvalidConfigFormatException extends Exception {

    public InvalidConfigFormatException(String message) {
        super(message);
    }

    public InvalidConfigFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigFormatException(Throwable cause) {
        super(cause);
    }
}

