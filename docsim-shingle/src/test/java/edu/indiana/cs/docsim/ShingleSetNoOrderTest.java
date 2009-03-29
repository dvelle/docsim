package edu.indiana.cs.docsim;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

// import edu.indiana.cs.docsim.util.ResourceLoader;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.indiana.cs.docsim.TestUtil.dumpShingleSet;
import static edu.indiana.cs.docsim.TestUtil.dumpShingle;

public class ShingleSetNoOrderTest
  extends TestCase {
    private static Logger logger =
        Logger.getLogger(ShingleSetNoOrderTest.class.getName());
    private String simpleText =
        "Adds a \"See Also\" heading with a link or text entry that points "+
        "to reference. A doc comment may contain any number of @see tags,"+
        "which are all grouped under the same heading. The @see tag has "+
        "three variations; the third form below is the most common." +
        "This tag is valid in any doc comment: overview, package," +
        "class, interface, constructor, method or field. For inserting an " +
        "in-line link within a sentence to a package, class or member.";

    private String dupText =
        "hello world hello world hello world word hello hello world";

    private String moreDupText =
        "hello world hello world hello world hello world hello world";

    private String textEncoding = "UTF-8";
    private String textLiteral;
    private String textFileName;

    // private ShingleSet shingleSet;
    private ShingleTextParserNoOrder shingleTxtParser;

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ShingleSetNoOrderTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ShingleSetNoOrderTest.class);
    }

    @Override
    public void setUp() {
        // shingleSet = new ShingleSet();
        shingleTxtParser = new ShingleTextParserNoOrder();
    }

    public void testSimpleText() throws Exception {
        int shinglesize = 5;
        String dumpStr;
        StringBuilder sb = new StringBuilder();

        ShingleSet<WordShingleNoOrder> shingleset =
            shingleTxtParser.parseText(simpleText, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);

        ShingleSet<WordShingleNoOrder> shingleset2 =
            shingleTxtParser.parseText(dupText, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);

        // Union of two shingle sets
        ShingleSet<WordShingleNoOrder> union = shingleset.union(shingleset2);
        dumpStr = dumpShingleSet(union);
        int size1 = shingleset.size();
        int size2 = shingleset2.size();
        int sizeunion = union.size();
        int sizeunique1 = shingleset.sizeUnique();
        int sizeunique2 = shingleset2.sizeUnique();
        int sizeuniqueunion = union.sizeUnique();

        sb.append("\nUnion of two shingle sets.");
        sb.append("\nshingle size: "+shinglesize);
        sb.append("\nsizes: " + size1 + "," + size2 + ";" + sizeunion);
        sb.append("\n\tUnique size: " + sizeunique1 + "," + sizeunique2 + ";" +
                sizeuniqueunion);
        logger.info(sb.toString());
    }

    public void testDupText() throws Exception {
        int shinglesize = 5;
        String dumpStr;
        StringBuilder sb = new StringBuilder();

        ShingleSet shingleset = shingleTxtParser.parseText(dupText,
                shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);

        ShingleSet shingleset2 = shingleTxtParser.parseText(moreDupText, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset2);

        sb.append("shingle size: " + shinglesize + "\n");
        sb.append(shingleSetUnionAndIntersect(shingleset, shingleset2));

        logger.info(sb.toString());
    }

    public void testTextIntersectFromFile() throws Exception {
        int shinglesize = 1;
        String dumpStr;
        StringBuilder sb = new StringBuilder();

        // String textFile1 = "res://shingle.sample.1";
        String textFile1 = "res://doc1.txt";
        ShingleSet<WordShingleNoOrder> shingleset =
            shingleTxtParser.parseFile(textFile1, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset);

        // String textFile2 = "res://shingle.sample.2";
        String textFile2 = "res://doc2.txt";
        ShingleSet<WordShingleNoOrder> shingleset2 =
            shingleTxtParser.parseFile(textFile2, shinglesize, textEncoding);
        dumpStr = dumpShingleSet(shingleset2);

        sb.append("File 1: " + textFile1);
        sb.append("\nFile 2: " + textFile2);
        sb.append("\nshingle size: " + shinglesize + "\n");
        sb.append(shingleSetUnionAndIntersect(shingleset, shingleset2));

        logger.info(sb.toString());
    }

    private String shingleSetUnionAndIntersect(ShingleSet set1, ShingleSet set2) {
        StringBuilder sb = new StringBuilder();
        String dumpStr;

        // Union
        ShingleSet union = set1.union(set2);
        // dumpStr = dumpShingleSet(union);

        int size1 = set1.size();
        int size2 = set2.size();
        int sizeunion = union.size();
        int sizeunique1 = set1.sizeUnique();
        int sizeunique2 = set2.sizeUnique();
        int sizeuniqueunion = union.sizeUnique();

        sb.append("Union of two shingle sets.");
        sb.append("\n\tsizes: " + size1 + "," + size2 + ";" + sizeunion);
        sb.append("\n\tUnique size: " + sizeunique1 + "," + sizeunique2 + ";" +
                sizeuniqueunion);


        // Intersect
        ShingleSet intersect = set1.intersect(set2);
        // dumpStr = dumpShingleSet(intersect);

        int sizeintersect = intersect.size();
        int sizeuniqueintersect = intersect.sizeUnique();
        sb.append("\nIntersect of two shingle sets.");
        sb.append("\n\tsizes: " + size1 + "," + size2 + ";" + sizeintersect);
        sb.append("\n\tUnique size: " + sizeunique1 + "," + sizeunique2 + ";" +
                sizeuniqueintersect);

        return sb.toString();
    }
}

