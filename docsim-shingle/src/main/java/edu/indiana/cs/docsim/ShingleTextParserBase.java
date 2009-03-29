package edu.indiana.cs.docsim;

/**
 * Given a string input or a file, build shingle set for the data.
 */
interface ShingleTextParserBase <S extends Shingle> {
    /**
     *
     * @param text        text to be processed
     * @param shinglesize size of each shingle
     * @param txtEncoding charset of <code>text</code>
     * @return
     */
    ShingleSet<S> parseText(String text, int shinglesize, String txtEncoding);

    /**
     * Reads data from the file, processes it and build shingle set.
     *
     * @param fileName      name of the file where data is stored.
     * @param shinglesize   size of each shingle.
     * @param txtEncoding   charset of data in the file.
     * @return
     */
    ShingleSet<S> parseFile(String fileName, int shinglesize, String txtEncoding)
        throws Exception;
}
