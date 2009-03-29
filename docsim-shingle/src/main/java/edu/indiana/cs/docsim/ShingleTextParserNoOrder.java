package edu.indiana.cs.docsim;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.util.ResourceLoader;

/**
 * This class receives string input, tokenizes it, and build shingle set.
 *
 * @author
 * @version
 */
public class ShingleTextParserNoOrder
  implements ShingleTextParserBase<WordShingleNoOrder> {

    /**
     * Tokenizer which splits the whole chunk of text into meaningful pieces.
     * For example, the split can be based on word, phase, sentence...
     * Default tokenizer uses word as unit.
     */
    private TextTokenizer tokenizer;

    private static Logger logger =
        Logger.getLogger(ShingleTextParserNoOrder.class.getName());

    public ShingleTextParserNoOrder() {
        tokenizer = new TextTokenizerWord();
    }

    private void clear() {

    }

    //FIXME: implementation in this method is not efficient.

    /**
     * Everytime a new shingle set is returned.
     * So this method can be invoked multiple times with different data passed
     * in.
     *
     * @param text
     * @param shinglesize
     * @param txtEncoding
     * @return
     */
    public ShingleSet<WordShingleNoOrder> parseText(String text, int shinglesize,
            String txtEncoding){

        // First tokenize the input text
        tokenizer.tokenize(text, txtEncoding);

        ShingleSet<WordShingleNoOrder> shingleset = new ShingleSet();
        String[] window = new String[shinglesize]; //store a shingle
        int shinglecursor = 0, unitcursor = 0;
        WordShingleNoOrder shingle;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            // logger.info(unitcursor + ":" + token);
            ++unitcursor;
            if (unitcursor >= shinglesize) {
                if (unitcursor > shinglesize) {
                    for (int i = 0 ; i < shinglesize-1 ; ++i) {
                        window[i] = window[i+1];
                    }
                }
                window[shinglesize-1] = token;
                shingle = new WordShingleNoOrder();
                shingle.buildShingle(window);
                shingleset.addShingle(shingle, shinglecursor);
                ++shinglecursor;
            } else {
                window[unitcursor-1] = token;
            }
        }

        // If data size < shingle size
        if (shinglecursor == 0) {//text length < shingle size
            shingle = new WordShingleNoOrder();
            String[] newArray = new String[unitcursor];
            System.arraycopy(window, 0, newArray, 0, newArray.length);
            shingle.buildShingle(newArray);
            shingleset.addShingle(shingle, shinglecursor);
            shinglecursor = 1;
        }
        // logger.info("# of shingles:" + shinglecursor);
        return shingleset;
    }

    public ShingleSet<WordShingleNoOrder> parseFile(String fileName, int shinglesize,
            String txtEncoding) throws Exception{
        // File file = new File(fileName);
        // Sring content = FileUtils.readFileToString(file, txtEncoding);
        InputStream is = ResourceLoader.open(fileName);
        String content = IOUtils.toString(is, txtEncoding);
        return parseText(content, shinglesize, txtEncoding);
    }

    /**
     * get the value of tokenizer
     * @return the value of tokenizer
     */
    public TextTokenizer getTokenizer(){
        return this.tokenizer;
    }
    /**
     * set a new value to tokenizer
     * @param tokenizer the new value to be used
     */
    public void setTokenizer(TextTokenizer tokenizer) {
        this.tokenizer=tokenizer;
    }
}

