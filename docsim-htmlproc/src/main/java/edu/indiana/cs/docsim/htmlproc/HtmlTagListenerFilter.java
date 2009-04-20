package edu.indiana.cs.docsim.htmlproc;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.indiana.cs.docsim.htmlproc.util.HtmlUtil;
import edu.indiana.cs.docsim.htmlproc.util.WebConnectionWrapperNoExternal;

public class HtmlTagListenerFilter extends DocFilterBase {
    private static Logger logger =
        Logger.getLogger(HtmlTagListenerFilter.class.getName());

    private DomTagListenerRegistry listenersReg;
    private WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
    {
        webClient.setCssEnabled(false);
        webClient.setTimeout(1000 *  20);
        webClient.setAppletEnabled(false);
        webClient.setPopupBlockerEnabled(true);
        // webClient.setCookiesEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.setJavaScriptEnabled(false);
    }

    public void clear() {
        listenersReg.clear();
    }

    public HtmlTagListenerFilter (DomTagListenerRegistry reg) {
        listenersReg = reg;
    }

    public void registerListener(DomTagListener listener) {
        listenersReg.register(listener);
    }

    public String filter(String doc) throws Exception {
        // This is an ugly workaournd. HtmlTagRemover does not accept a string
        // as input document. So we first write the string to a temp file and
        // then feed that file to HtmlTagRemover.
        File tmpfile = File.createTempFile("doc-preprocess", ".html");
        String charset = "UTF-8";
        FileOutputStream fos = new FileOutputStream(tmpfile);
        doc = HtmlUtil.removeIFrame(doc);
        doc = HtmlUtil.removeMeta(doc);
        fos.write(doc.getBytes(charset));
        fos.close();

        // Following code of two lines does not work.
        // Because the underlying system treats the file as a resource. So it
        // would search for the file in the way that resources are searched
        // for.
        // String filename = tmpfile.getAbsolutePath();
        // String result = tagRemover.tagRemoveFromURL(filename);

        // logger.info("temp file:" + tmpfile);
        traverseHtml(tmpfile.toURI().toURL());
        // tmpfile.delete();

        return doc;
    }

    private void traverseHtml(URL url) {
        try {
            if (url == null) {
                throw new Exception("parameter url should not be null");
            }
            // logger.info("URI is this: <" + this.getClass().getName() + "> " + url);
            // WebConnection conn = webClient.getWebConnection();
            // if (conn instanceof WebConnectionWrapperNoExternal) {
            // } else {
            //     WebConnectionWrapperNoExternal conn1 = new WebConnectionWrapperNoExternal(webClient);
            //     conn1.setAllowedURL(url.toExternalForm());
            //     webClient.setWebConnection(conn1);
            // }
            Page page = webClient.getPage(url);
            if (page == null) {
                throw new Exception("the page is null at '" + url + "'");
            }
            if (page instanceof HtmlPage) {
                // traverseDom(((HtmlPage)page).getBody());
                traverseDom(((HtmlPage)page).getDocumentElement());
            } else {
                logger.severe("You should pass in a html page");
            }
        } catch(Exception ex) {
            logger.severe(""+ex);
            ex.printStackTrace();
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

