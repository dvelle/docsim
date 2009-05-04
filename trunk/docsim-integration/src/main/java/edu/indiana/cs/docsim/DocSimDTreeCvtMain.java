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

public class DocSimDTreeCvtMain {

    private static Logger logger =
        Logger.getLogger(DocSimDTreeCvtMain.class.getName());

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("i", "input-file", true,
                "input data file.");
        options.addOption("o", "output-file", true,
                "base output file name.");
        options.addOption("h", "help", false, "display help document");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        String inputFile = null;
        String outputFile = null;
        if (cmd.hasOption("i")) {
            inputFile = cmd.getOptionValue("i");
        }
        if (cmd.hasOption("o")) {
            outputFile = cmd.getOptionValue("o");
        }
        boolean help = false;
        if (inputFile == null || outputFile == null) {
            help = true;
        }
        if (cmd.hasOption("h") || help) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "docsim stat", options );
            return;
        }

        DocSimLatticeStatistics stat =
            BasicStatisticsFormatter.getStatFromFile(inputFile);
        BasicStatisticsFormatter.toArffFile(stat, outputFile);
    }
}

