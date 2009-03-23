package edu.indiana.cs.docsim.htmlproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;

public class DomTagListenerRegistry {
    // private List<ListenerEntry> listeners = new ArrayList<ListenerEntry>();
    private List<DomTagListener> listeners = new ArrayList<DomTagListener>();

    /**
     * Clear all installed listeners.
     */
    public void clear() {
        listeners.clear();
    }

    public void register(DomTagListener listener) {
        try {
            // ListenerEntry entry = new ListenerEntry(tags, listener);
            listeners.add(listener);
        } catch(Exception ex) {
        }
    }

    public void handle(Element element) {
        for (Iterator<DomTagListener> it = listeners.iterator() ; it.hasNext() ;) {
            DomTagListener listener = it.next();
            if (listener.isInterest(element)) {
                listener.handle(element);
            }
        }
    }
}

// class ListenerEntry {
//     private List<String> tagls = null;
//     private DomTagListener listener = null;

//     public ListenerEntry (String[] tags, DomTagListener listener)
//       throws Exception{
//         if (tags == null || listener == null) {
//             throw new Exception("Invalid argument");
//         }
//         tagls = new ArrayList<String>();
//         for (int i = 0 ; i < tags.length ; ++i) {
//             tagls.add(tags[i]);
//         }
//         this.listener = listener;
//     }

//     public void handle(Element element) {
//         String tagName = element.getTagName();
//         for (Iterator<String> it = tagls.iterator() ; it.hasNext() ;) {
//             String tag = it.next();
//             if (tag.compareToIgnoreCase(tagName) == 0) {
//                 listener.handle(element);
//                 break;
//             }
//         }
//     }
// }

