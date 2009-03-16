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
    private String          textEncoding;

    /* Internal token array which stores results of parsing raw input */
    private String[]        tokens;

    /* Position of the cursor */
    private int             cursor = 0;

    /* Whether this tokenzier has been initialized.
     * Not used now.
     */
    private boolean         isInit = false;

    public TextTokenizerWord() {
    }
    public TextTokenizerWord(String text, String encoding)
      throws TextTooLongException {
        if( text.length() > lenLimit ) {
            throw new TextTooLongException(ERROR_STR_TOOLONG);
        }
        tokenize(text, encoding);
    }
    private void parse() {
        if (this.rawData == null) { return; }
        isInit = true;
        tokens = rawData.split(regex);
        purify();
        StringBuilder sb = new StringBuilder();
        sb.append("Tokenizing:" + this.rawData);
        sb.append("\nLength of token array is " + tokens.length);
        logger.info(sb.toString());
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
     * Checks wheter a token is legal.
     *
     * @param token the token to be checked.
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

    public String nextToken() {
        if (tokens == null) {
            logger.warning("StringTokenzierWord may have not been initialized");
            return null;
        }
        return tokens[cursor++];
    }

    public void tokenize(String text, String textEncoding) {
        this.cursor = 0;
        this.rawData = text;
        this.textEncoding = textEncoding;
        this.isInit = false;
        this.tokens = null;
        parse();
    }
}

