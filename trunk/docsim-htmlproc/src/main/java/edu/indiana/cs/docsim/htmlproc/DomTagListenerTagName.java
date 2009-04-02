package edu.indiana.cs.docsim.htmlproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Element;

/**
 * This listener just is interested those elements with specific tag names.
 * In other words, other properties (e.g. attributes) are not taken into
 * account.
 *
 * @author
 * @version
 */
public abstract class DomTagListenerTagName implements DomTagListener {

    private static Logger logger =
        Logger.getLogger(DomTagListenerTagName.class.getName());

    private static DomTagRemover tagRemover = new DomTagRemover();

    protected List<String> datals = new ArrayList<String>();
    protected List<String> tagls = new ArrayList<String>();


    public DomTagListenerTagName(String[] tags) {
        for (int i = 0 ; i < tags.length ; ++i) {
            tagls.add(tags[i]);
        }
    }

    /**
     * Check whether this listener is interested in that particular tag.
     *
     * @param tagName
     * @return
     */
    public boolean isInterest(Element element) {
        String tagName = element.getTagName();
        for (Iterator<String> it = tagls.iterator() ; it.hasNext() ;) {
            String tag = it.next();
            if (tagName.compareToIgnoreCase(tag) == 0) {
                return true;
            }
        }
        return false;
    }

    public void handle(Element element) {
        // String data = element.getNodeValue();
        try {
            String data = tagRemover.tagRemove(element).trim();
            if (data != null && data.length() > 0) {
                this.datals.add(data);
            }
        } catch(Exception ex) {
            logger.severe("Error occurs during dom tag removal in method" +
                    DomTagListenerTagName.class.getName() + "#handle");
        }
    }

    // return value if List&lt;String@gt;
    public Object getData() {
        return this.datals;
    }

    /**
     * get the value of tagls
     * @return the value of tagls
     */
    public List<String> getTagls(){
        return this.tagls;
    }
    /**
     * set a new value to tagls
     * @param tagls the new value to be used
     */
    public void setTagls(List<String> tagls) {
        this.tagls=tagls;
    }
    /**
     * get the value of datals
     * @return the value of datals
     */
    public List<String> getDatals(){
        return this.datals;
    }
    /**
     * set a new value to datals
     * @param datals the new value to be used
     */
    public void setDatals(List<String> datals) {
        this.datals=datals;
    }
}

