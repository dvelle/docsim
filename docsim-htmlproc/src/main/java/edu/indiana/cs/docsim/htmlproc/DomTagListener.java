package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;
import java.util.Collection;

public interface DomTagListener extends TagSelector {

    /**
     * Handle the specified dom element.
     *
     * @param element
     */
    void handle(Element element);

    /**
     * Get data stored in this listener.
     * The format of returned value depends on concrete implementation.
     * @return
     */
    Object getData();
    // Collection<String> getTagsInteresting();
}

