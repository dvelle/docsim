package edu.indiana.cs.docsim.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
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
      throws IOException {
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
        String result = serialize2XML();
        write2File(result, outputDir + outputXmlFile);
    }

    public String serialize2XML() {
        if (pageRepo == null)
            return "";
        List<QueryEntry> qEntries = pageRepo.getQueryEntries();
        Iterator<QueryEntry> it = qEntries.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(xmlDecl);
        sb.append("\n\n<queries>\n");
        while (it.hasNext()) {
            sb.append("\t<query>\n");
            QueryEntry entry = it.next();
            sb.append("\t\t<querystring>" + entry.getQuery() + "</querystring>\n");
            sb.append("\t\t<docs>\n");
            List<SearchResultEntry> results = entry.getResults();
            Iterator<SearchResultEntry> it2 = results.iterator();
            while (it2.hasNext()) {
                sb.append("\t\t\t<doc>\n");
                SearchResultEntry srentry = it2.next();
                sb.append("\t\t\t\t<url>\n");
                String strUrl = srentry.getUrl();
                String fileName = outputDir + (++cursor) + ".html";
                try {
                    String pageContent = fetchPageHTTP(new URL(strUrl));
                    if (pageContent == null) {
                        pageContent = "";
                    }
                    File file = new File(fileName);
                    logger.info("abs path:" + file.getAbsolutePath());
                    fileName = file.getAbsolutePath();
                    write2File(pageContent, fileName);
                } catch(Exception ex) {
                    logger.warning(ex.toString());
                }
                sb.append("\t\t\t\t" + strUrl + "\n");
                sb.append("\t\t\t\t</url>\n");
                sb.append("\t\t\t\t<file>\n");
                sb.append("\t\t\t\t" + fileName + "\n");
                sb.append("\t\t\t\t</file>\n");
                sb.append("\t\t\t</doc>\n");
            }
            sb.append("\t\t</docs>\n");
            sb.append("\t<query>\n\n");
        }
        sb.append("\n</queries>");
        return sb.toString();
    }

    public void write2File(String content, String fileName) {
        try {
            FileUtils.writeStringToFile(new File(fileName), content, "UTF-8");
        } catch(Exception ex) {
            logger.warning("error during writing data to file '" +
                    fileName + "'");
        }
    }

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

            page = method.getResponseBodyAsString();

        } catch (HttpException e) {
            logger.severe("Fatal protocol violation: " + e);
        } catch (IOException e) {
            logger.severe("Fatal transport error: " + e);
        } finally {
            method.releaseConnection();
        }
        return page;
    }

    private static String xmlDecl = "<? xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    private static int cursor = 0;

    public static void main (String []args) throws Exception {
        Options options = new Options();
        options.addOption("d", "output-directory", true, "output directory");
        options.addOption("o", "output-file", true, "output xml file");
        options.addOption("c", "config", true, "Specify the configuration file");
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
}

