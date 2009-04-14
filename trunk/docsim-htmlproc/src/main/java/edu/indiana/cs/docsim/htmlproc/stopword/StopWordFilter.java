package edu.indiana.cs.docsim.htmlproc.stopword;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import edu.indiana.cs.docsim.htmlproc.DocFilterBase;
import edu.indiana.cs.docsim.htmlproc.util.ResourceLoader;
import edu.indiana.cs.docsim.htmlproc.util.TextTokenizerWord;

class StringIgnoreCase {
    private String data;

    public String getData() {
        return data;
    }

    public StringIgnoreCase(String another) {
        data = another;
    }

    @Override
    public boolean equals(Object another) {
        if (data == another) return true;
        if (another instanceof String) {
            return data.equalsIgnoreCase((String)another);
        } else if (another instanceof StringIgnoreCase) {
            return data.equalsIgnoreCase(((StringIgnoreCase)another).getData());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return data.toLowerCase().hashCode();
    }
}

class StopWordRepository {

    private Set<StringIgnoreCase> stopWords = new HashSet<StringIgnoreCase>();

    public void add(String stopWord) {
        StringIgnoreCase sis = new StringIgnoreCase(stopWord);
        stopWords.add(sis);
    }
    public boolean contains(String word) {
        return stopWords.contains(new StringIgnoreCase(word));
    }
    public void clear() {
        stopWords.clear();
    }
}

/**
 * This filter removes stop words.
 *
 * @author
 * @version
 */
public class StopWordFilter extends DocFilterBase {
    private static StopWordRepository stopWords;
    private static TextTokenizerWord  tokenizer;
    private static String stopWordFileName;

    static {
        tokenizer = new TextTokenizerWord();
        stopWords = new StopWordRepository();
        stopWordFileName = "res://"
            // + StopWordFilter.class.getPackage().getName().replace('.', '/') + "/"
            + "stop_words.txt";
    }

    /**
     *
     *
     * @param useDefaultFile whether to load stop words from default file.
     */
    public StopWordFilter(boolean useDefaultFile) throws Exception {
        init(useDefaultFile, null);
    }

    public StopWordFilter(String stopWordFileName) throws Exception {
        init(false, stopWordFileName);
    }

    private void init(boolean useDefaultFile, String fileName)
      throws Exception {
        // TODO: should we clear the existing stop words first by default?
        if (useDefaultFile) {
            loadFromFile(stopWordFileName);
        } else if (fileName != null) {
            loadFromFile(fileName);
        } else {
        }
    }

    public void clear() {
        stopWords.clear();
    }

    private void loadFromFile(String strWordFile) throws Exception {
        InputStream is = ResourceLoader.open(strWordFile);
        String content = IOUtils.toString(is, "UTF-8");
        String[] words = content.split("\n");
        for (int i = 0 ; i < words.length ; ++i) {
            String word = words[i];
            word = word.trim();
            if (word.length() == 0) continue;
            addStopWord(word);
        }
    }

    public void addStopWord(String stopword) {
        stopWords.add(stopword);
    }

    public String filter(String doc) throws Exception {
        tokenizer.tokenize(doc);
        StringBuilder sb = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!stopWords.contains(token)) {
                sb.append(token + " ");
            }
        }
        return sb.toString().trim();
    }
}

