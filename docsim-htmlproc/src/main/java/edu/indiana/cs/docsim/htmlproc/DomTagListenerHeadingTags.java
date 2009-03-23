package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;

/**
 * This class represents tag listeners that are interested in html header
 * tags.
 *
 */
public class DomTagListenerHeadingTags extends DomTagListenerTagName {
    // See http://www.w3schools.com/TAGS/tryit.asp?filename=tryhtml_headers
    // for heading examples.
    private static String[] tagNames = { "h1", "h2", "h3", "h4" };

    public DomTagListenerHeadingTags() {
        super(tagNames);
    }

    public void handle(Element element) {
        super.handle(element);
    }
}

