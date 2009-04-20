package edu.indiana.cs.docsim;

import java.util.logging.Logger;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class DocSimStatTest extends TestCase {

    private static Logger logger =
        Logger.getLogger(DocSimStatTest.class.getName());

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocSimStatTest(String testName) {
        super(testName);
    }
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocSimStatTest.class);
    }
    public void testDummy() {
    }
    // public void notestSummary() throws Exception {
    //     String inputDataFile = "/u/zhguo/courses/b652/project/code/docsim/docsim-integration/results_allpairs.txt";
    //     String outputDataFile = "/u/zhguo/courses/b652/project/code/docsim/docsim-integration/results_summary.txt";
    //     DocSimLatticeStatistics stat =
    //         BasicStatisticsFormatter.getStatFromFile(inputDataFile);
    //     BasicStatisticsFormatter.avgCalcBySize2File(stat, outputDataFile);
    // }
    public static void main(String[] args) throws Exception {
        Options options = new Options();
        options.addOption("i", "input-file", true,
                "input data file.");
        options.addOption("o", "output-file", true,
                "output file.");
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
        // DocSimStatTest test = new DocSimStatTest("");
        // test.notestSummary();

        DocSimLatticeStatistics stat =
            BasicStatisticsFormatter.getStatFromFile(inputFile);
        BasicStatisticsFormatter.avgCalcBySize2File(stat, outputFile);
    }
}

