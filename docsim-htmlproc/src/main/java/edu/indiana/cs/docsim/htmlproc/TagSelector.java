package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;

/**
 * Select dom tags that are interesting to the tag selector.
 *
 * @author
 * @version
 */
public interface TagSelector {
    /**
     * Check whether this listener is interested in that particular tag.
     *
     * @param tagName
     * @return
     */
    boolean isInterest(Element element);
}
