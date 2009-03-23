package edu.indiana.cs.docsim.htmlproc;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import edu.indiana.cs.docsim.ShingleSet;
import edu.indiana.cs.docsim.Shingle;
import edu.indiana.cs.docsim.ShingleUnit;
import edu.indiana.cs.docsim.ShingleUnitBag;
import edu.indiana.cs.docsim.ShingleTextParser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.net.URL;

public class DocShingleBuilderTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(DocShingleBuilderTest.class.getName());

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;

    private ShingleTextParser        shingleTxtParser;
    private DocFilterPipeLineDefault docFilterPipeLine;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DocShingleBuilderTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DocShingleBuilderTest.class);
    }

    @Override
    public void setUp() {
        shingleTxtParser = new ShingleTextParser();
        try {
            docFilterPipeLine = new DocFilterPipeLineDefault();
        } catch(Exception ex) {
            logger.severe("Exception in creating DocFilterPipeLineDefault " +
                    ex);
        }
    }

    public void testShingleTextFromFile() throws Exception {
        String filename = "res://augmented_page.html";
        String charset = "UTF-8";
        String result = docFilterPipeLine.filter(filename, charset);
        logger.info("Get data from file \"" + filename + "\" result:\n" + result);

        int shinglesize = 5;
        String dumpStr;
        logger.info("Testing by feeding simple text");
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        // dumpStr = dumpeShingleSet(shingleset);
        // logger.info(dumpStr);

        ShingleSet shingleset2 = shingleTxtParser.parseText(result, shinglesize, charset);
        ShingleSet union = shingleset.union(shingleset2);
        ShingleSet intersect = shingleset.intersect(shingleset2);

        int size1 = union.sizeUnique();
        int size2 = intersect.sizeUnique();
        logger.info("union size:" + size1 + ";intersect size:" + size2);
        // dumpStr = dumpeShingleSet(shingleset);
        // logger.info(dumpStr);
    }

    public void testShingleTextFromURL() throws Exception {
        String strUrl1 = "http://cs.indiana.edu";
        String strUrl2 = "http://www.informatics.indiana.edu/";
        URL url1 = new URL(strUrl1);
        URL url2 = new URL(strUrl2);

        String charset = "UTF-8";
        String result = docFilterPipeLine.filter(url1);
        logger.info("Get data from url \"" + strUrl1 + "\" result:\n" + result);

        int shinglesize = 5;
        logger.info("Testing by feeding simple text");
        ShingleSet shingleset = shingleTxtParser.parseText(result, shinglesize, charset);
        // dumpStr = dumpeShingleSet(shingleset);
        // logger.info(dumpStr);

        result = docFilterPipeLine.filter(url2);
        logger.info("Get data from url \"" + strUrl2 + "\" result:\n" + result);
        ShingleSet shingleset2 = shingleTxtParser.parseText(result, shinglesize, charset);
        ShingleSet union = shingleset.union(shingleset2);
        ShingleSet intersect = shingleset.intersect(shingleset2);

        int size1 = union.sizeUnique();
        int size2 = intersect.sizeUnique();
        logger.info("union size:" + size1 + ";intersect size:" + size2);
        //
        // dumpStr = dumpeShingleSet(shingleset);
        // logger.info(dumpStr);
    }

    private String dumpeShingleSet(ShingleSet shingleset) {
        // List<Shingle> shingles = shingleset.getShingleList();
        List<Shingle> shingles = shingleset.getUniqueShingleList();
        StringBuilder sb = new StringBuilder();
        for (Iterator<Shingle> it = shingles.iterator() ; it.hasNext();) {
            Shingle shingle = it.next();
            sb.append("\nshingle:\n");
            sb.append("\t" + dumpShingle(shingle));
            int[] pos = shingleset.getPos(shingle);
            sb.append("\n\tpos:" + serIntArray(pos));
        }
        return sb.toString();
    }
    private String dumpShingle(Shingle shingle) {
        ShingleUnitBag sub = shingle.getSuMgr();
        List<ShingleUnit> sulist = sub.getUniqueShingleUnits();
        Iterator<ShingleUnit> it = sulist.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ShingleUnit su = it.next();
            int[] pos = sub.getPos(su);
            sb.append(su.value());
            sb.append(":" + serIntArray(pos) + "; ");
        }
        return sb.toString();
    }
    private String serIntArray(int[] array){
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            return "";
        } else {
            for (int i = 0 ; i < array.length ; ++i) {
                if (i == array.length-1) {
                    sb.append(array[i]);
                } else {
                    sb.append(array[i]+",");
                }
            }
        }
        return sb.toString();
    }
}
