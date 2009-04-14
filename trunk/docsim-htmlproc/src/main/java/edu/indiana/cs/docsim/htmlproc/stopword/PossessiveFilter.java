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

/**
 * This filter removes possessive.
 * For example:
 *  text
 *      this is gerald's code.
 *  would be transformed to text
 *      this is gerald code
 *
 * @author
 * @version
 */
public class PossessiveFilter extends DocFilterBase {

    // TODO: refine it?
    private static String possessivePattern = "'s";

    public String filter(String doc) throws Exception {
        int idx = 0;
        int pos;
        StringBuilder sb = new StringBuilder();
        while ((pos=doc.indexOf(possessivePattern, idx)) != -1) {
            boolean strip = false;
            try {
                char charAfter = doc.charAt(pos + possessivePattern.length());
                if (charAfter == '\r' || charAfter == '\n' || charAfter == ' '
                        ||charAfter == '\t') {
                    strip = true;
                }
            } catch(Exception ex) {
                strip = true;
            }

            if (strip) {
                String seg = doc.substring(idx, pos);
                sb.append(seg);
            } else {
                String seg = doc.substring(idx, pos + possessivePattern.length());
                sb.append(seg);
            }
            idx = pos + possessivePattern.length();
        }

        sb.append(doc.substring(idx, doc.length()));

        return sb.toString().trim();
    }
}

