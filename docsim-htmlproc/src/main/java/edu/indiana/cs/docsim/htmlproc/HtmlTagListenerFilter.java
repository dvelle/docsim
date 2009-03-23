package edu.indiana.cs.docsim.htmlproc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileOutputStream;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.net.URL;
import java.util.logging.Logger;

public class HtmlTagListenerFilter extends DocFilterBase {
    private static Logger logger =
        Logger.getLogger(HtmlTagListenerFilter.class.getName());

    private DomTagListenerRegistry listenersReg;
    private WebClient webClient = new WebClient();
    {
        webClient.setCssEnabled(false);
        webClient.setAppletEnabled(false);
        webClient.setPopupBlockerEnabled(true);
        webClient.setCookiesEnabled(false);
        webClient.setJavaScriptEnabled(false);
    }

    public HtmlTagListenerFilter (DomTagListenerRegistry reg) {
        listenersReg = reg;
    }

    public String filter(String doc) throws Exception {
        // This is a ugly workaournd. HtmlTagRemover does not accept a string
        // as input document. So we first write the string to a temp file and
        // then feed that file to HtmlTagRemover.
        File tmpfile = File.createTempFile("doc-preprocess", ".html");
        String charset = "UTF-8";
        FileOutputStream fos = new FileOutputStream(tmpfile);
        fos.write(doc.getBytes(charset));
        fos.close();

        // Following code of two lines does not work.
        // Because the underlying system treats the file as a resource. So it
        // would search for the file in the way that resources are searched
        // for.
        // String filename = tmpfile.getAbsolutePath();
        // String result = tagRemover.tagRemoveFromURL(filename);

        traverseHtml(tmpfile.toURI().toURL());
        tmpfile.delete();

        return doc;
    }

    private void traverseHtml(URL url) {
        try {
            Page page = webClient.getPage(url);
            if (page instanceof HtmlPage) {
                traverseDom(((HtmlPage)page).getBody());
            } else {
                logger.severe("You should pass in a html page");
            }
        } catch(Exception ex) {
            logger.severe(""+ex);
        }
    }

    private void traverseDom(Element element) {
        NodeList children = element.getChildNodes();

        // TODO: If maxTxtLength is greater than max allowed value of int
        // type, following statement would have problems. Usually, we would
        // not want to parse so large (2^31 -> 2G) dom elements.
        for (int i = 0 ; i < children.getLength() ; ++i) {
            Node node = children.item(i);
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    listenersReg.handle((Element)node);
                    traverseDom((Element)node);
                    break;
            }
        }
    }
}

