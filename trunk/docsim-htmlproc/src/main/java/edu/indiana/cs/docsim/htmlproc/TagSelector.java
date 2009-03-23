package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;

public interface TagSelector {
    /**
     * Check whether this listener is interested in that particular tag.
     *
     * @param tagName
     * @return
     */
    boolean isInterest(Element element);
}
