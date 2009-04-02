package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;

/**
 * This class represents tag listeners that are interested in emphasis html
 * tags.
 *
 */
public class DomTagListenerTitleTag extends DomTagListenerTagName {

    private static String[] tagNames = { "title" };

    public DomTagListenerTitleTag() {
        super(tagNames);
    }

    public void handle(Element element) {
        super.handle(element);
    }
}

