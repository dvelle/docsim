package edu.indiana.cs.docsim;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.util.ResourceLoader;

public class ShingleTextParser implements ShingleTextParserBase {
    private TextTokenizer tokenizer;

    private static Logger logger =
        Logger.getLogger(ShingleTextParser.class.getName());

    public ShingleTextParser() {
        tokenizer = new TextTokenizerWord();
    }

    private void clear() {

    }
    //FIXME: implementation in this method is not efficient.
    public ShingleSet parseText(String text, int shinglesize,
            String txtEncoding){
        logger.info("Starting to parse text.");
        tokenizer.tokenize(text, txtEncoding);
        ShingleSet shingleset = new ShingleSet();
        String[] window = new String[shinglesize];
        int shinglecursor = 0, unitcursor = 0;
        Shingle shingle;

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
                shingle = new WordShingle();
                shingle.buildShingle(window);
                shingleset.addShingle(shingle, shinglecursor);
                ++shinglecursor;
            } else {
                window[unitcursor-1] = token;
            }
        }
        if (shinglecursor == 0) {//text length < shingle size
            shingle = new WordShingle();
            String[] newArray = new String[unitcursor];
            System.arraycopy(window, 0, newArray, 0, newArray.length);
            shingle.buildShingle(newArray);
            shingleset.addShingle(shingle, shinglecursor);
            shinglecursor = 1;
        }
        logger.info("# of shingles:" + shinglecursor);
        return shingleset;
    }

    public ShingleSet parseFile(String fileName, int shinglesize,
            String txtEncoding) throws Exception{
        // File file = new File(fileName);
        // Sring content = FileUtils.readFileToString(file, txtEncoding);
        InputStream is = ResourceLoader.open(fileName);
        String content = IOUtils.toString(is, txtEncoding);
        return parseText(content, shinglesize, txtEncoding);
    }
}
