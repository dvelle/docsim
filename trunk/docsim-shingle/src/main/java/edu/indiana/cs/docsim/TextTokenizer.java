package edu.indiana.cs.docsim;

/**
 * Tokenize a shingle.
 *
 * @author
 * @version
 */
public interface TextTokenizer {
    /**
     * Check whether there are more tokens.
     *
     * @return true if there are more tokens.
     */
    boolean hasMoreTokens();

    /**
     * Get next token.
     *
     * @return next token
     */
    String  nextToken();
}
