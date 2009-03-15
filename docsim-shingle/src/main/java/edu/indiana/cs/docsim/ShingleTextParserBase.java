package edu.indiana.cs.docsim;

interface ShingleTextParserBase {
    ShingleSet parseText(String text, int shinglesize, String txtEncoding);
    ShingleSet parseFile(String fileName, int shinglesize, String txtEnoding);
}
