package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class DocSimMain {

    private static Logger logger =
        Logger.getLogger(DocSimMain.class.getName());

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("c", "config-file", true,
                "Configuration file. By default it is training.config.");
        options.addOption("o", "output-file", true,
                "result file name. By default, it is results.txt");
        options.addOption("h", "help", false, "display help document");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        String configFile = null;
        String outputFile = null;
        if (cmd.hasOption("c")) {
            configFile = cmd.getOptionValue("c");
        }
        if (cmd.hasOption("o")) {
            outputFile = cmd.getOptionValue("o");
        }

        if (cmd.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "docsim", options );
            return;
        }

        DocSim docsim = new DocSim();
        docsim.applyShingling(configFile, outputFile);
    }
}

