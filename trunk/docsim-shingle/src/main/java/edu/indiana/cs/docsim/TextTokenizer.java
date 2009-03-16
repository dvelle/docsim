package edu.indiana.cs.docsim;

/**
 * Tokenize a shingle.
 *
 * @author
 * @version
 */
public interface TextTokenizer {
    /**
     * Checks whether there are more tokens.
     * If so you can use {@link #nextToken()} to retrieve next token.
     *
     * @return true if there are more tokens.
     */
    boolean hasMoreTokens();

    /**
     * Gets next token.
     *
     * @return next token
     */
    String  nextToken();

    /**
     * Tokenizes the input <code>text</code> which is encoded using
     * <code>textEncoding</code>.
     * State of this tokenizer object would be set according to content in the
     * <code>text</code>.
     * After calling this method, the original content in this object would be
     * eliminated!!! New tokens would fill up.
     *
     * @param text the text to be tokenized.
     * @param textEncoding character encoding of the input <code>text</code>
     */
    void tokenize(String text, String textEncoding);
}
