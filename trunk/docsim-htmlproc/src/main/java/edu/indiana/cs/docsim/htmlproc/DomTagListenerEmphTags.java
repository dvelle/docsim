package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;

/**
 * This class represents tag listeners that are interested in emphasis html
 * tags.
 *
 */
public class DomTagListenerEmphTags extends DomTagListenerTagName {
    private static String[] tagNames = { "b", "em"};

    public DomTagListenerEmphTags() {
        super(tagNames);
    }

    public void handle(Element element) {
        super.handle(element);
    }
}

