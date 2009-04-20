package edu.indiana.cs.docsim.htmlproc;

/**
 * This filter convert string to lower case.
 *
 * @author
 * @version
 */
public class LowerCaseFilter extends DocFilterBase {
    public String filter(String doc) throws Exception {
        return doc.trim().toLowerCase();
    }
}

