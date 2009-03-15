package edu.indiana.cs.docsim;

import com.google.common.collect.Lists;

import java.util.logging.Logger;
import java.util.List;

/**
 * Tokenize a string based on words.
 *
 * @author
 * @version
 */
public class TextTokenizerWord
  implements TextTokenizer {
    private static int      lenLimit = 1024 * 1024; //1 M
    // private static String   regex = "\\s";
    private static String   regex = "\\b";
    private static String   wordPat = "\\w{1,}";
    private static String   ERROR_STR_TOOLONG = "Input string is too long";
    private Logger          logger =
        Logger.getLogger(TextTokenizerWord.class.getName());

    private String          rawData;
    private String[]        tokens;
    private int             cursor = 0;

    public TextTokenizerWord(String str)
      throws TextTooLongException{
        if( str.length() > lenLimit ){
            throw new TextTooLongException(ERROR_STR_TOOLONG);
        }
        rawData = str;
        parse();
    }
    private void parse() {
        if (rawData == null) { return; }
        tokens = rawData.split(regex);
        purify();
    }

    private void purify() {
        List<String> list = Lists.newArrayList();
        for (int i = 0 ; i < tokens.length ; ++i) {
            String tk = tokens[i];
            if (isLegalToken(tk)) {
                list.add(tk);
            }
        }
        tokens = list.toArray(new String[0]);
    }

    /**
     * Check wheter a token is legal.
     *
     * @param token
     * @return
     */
    private boolean isLegalToken(String token) {
        if (token == null){
            return false;
        } else if (token.matches(wordPat)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasMoreTokens() {
        if (tokens == null) {
            return false;
        } else {
            if (cursor < tokens.length) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Get next token.
     * Note: we don't check whether next token exists!!! Users should use
     * function hasMoreTokens first to check whether next token is available.
     *
     * @return
     */
    public String nextToken() {
        if (tokens == null) {
            logger.warning("StringTokenzierWord may have not been initialized");
            return null;
        }
        return tokens[cursor++];
    }
}

