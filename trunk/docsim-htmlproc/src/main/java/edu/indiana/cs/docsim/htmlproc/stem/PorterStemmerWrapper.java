package edu.indiana.cs.docsim.htmlproc.stem;

import edu.indiana.cs.docsim.htmlproc.DocFilterBase;

public class PorterStemmerWrapper extends DocFilterBase {

    // private PorterStemmer stemmer = new PorterStemmer();

    public String filter(String doc) throws Exception {
        PorterStemmer stemmer = new PorterStemmer();
        char[] charray = doc.toCharArray();
        stemmer.add(charray, charray.length);
        stemmer.stem();
        return stemmer.toString();
    }
}

