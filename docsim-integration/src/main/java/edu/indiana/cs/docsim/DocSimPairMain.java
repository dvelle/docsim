package edu.indiana.cs.docsim;

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
import org.apache.commons.cli.PosixParser;

public class DocSimPairMain {

    private static Logger logger =
        Logger.getLogger(DocSimPairMain.class.getName());

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("url1", "url1", true,
                "first document");
        options.addOption("url2", "url2", true,
                "second document");

        options.addOption("c", "config-file", true,
                "configuration file");

        options.addOption("h", "help", false, "display help document");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        String configFile = null;
        String outputFile = null;
        String strUrl1 = null;
        String strUrl2 = null;

        if (cmd.hasOption("url1")) {
            strUrl1 = cmd.getOptionValue("url1");
        }
        if (cmd.hasOption("url2")) {
            strUrl2 = cmd.getOptionValue("url2");
        }
        if (cmd.hasOption("c")) {
            configFile = cmd.getOptionValue("c");
        }

        boolean help = false;
        URL url1 = null;
        URL url2 = null;
        try {
            url1 = new URL(strUrl1);
            url2 = new URL(strUrl2);
        } catch(Exception ex) {
            help = true;
        }
        if (cmd.hasOption("h") || help) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "docsim", options );
            return;
        }

        DocSim docsim = new DocSim();
        String result =
            docsim.applyShinglingOutString(url1, url2, configFile);
        logger.info("result is:" + result);
    }
}

