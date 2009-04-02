package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Element;
import java.util.Collection;

/**
 * Represents a tag listener.
 * Each tag listener listens to some number of tags in the DOM tree. The
 * internal data stored in each tag listener depends on the corresponding
 * implementation.
 *
 * @author
 * @version
 */
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

