package edu.indiana.cs.docsim.htmlproc;

import java.net.URL;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlBody;

public class HtmlTagRemover {
    private WebClient webClient;
    private DomTagRemover domTagRemover;
    private static Logger logger =
        Logger.getLogger(HtmlTagRemover.class.getName());

    private void init() {
        domTagRemover = new DomTagRemover();
        domTagRemover. setSupressWhiteSpaces(true);

        webClient = new WebClient();
        // Don't load external resources referenced in the loaded page
        webClient.setCssEnabled(false);
        webClient.setAppletEnabled(false);
        webClient.setPopupBlockerEnabled(true);
        webClient.setCookiesEnabled(false);
        webClient.setJavaScriptEnabled(false);
    }

    public HtmlTagRemover () {
        init();
    }

    private URL mappingFile2URL(String fileName) {
        ClassLoader cl = HtmlTagRemover.class.getClassLoader();
        URL url = cl.getResource(fileName.trim());
        return url;
    }

    /**
     * Remove tags in body part of a html page read from a file.
     *
     * @param fileName where the html page is stored.
     * @return
     * @throws Exception
     */
    public String tagRemoveFromFile(String fileName) {
        URL url = mappingFile2URL(fileName);
        return tagRemove(url);
    }

    /**
     * Remove tags in body part of a html page retrieved from a URL.
     *
     * @param url
     * @return
     */
    public String tagRemoveFromURL(URL url) {
        return tagRemove(url);
    }

    private String tagRemove(URL url) {
        try {
            Page page = webClient.getPage(url);
            if (page instanceof HtmlPage) {
                return tagRemoveBody((HtmlPage)page);
            } else {
                logger.severe("You should pass in a html page");
            }
        } catch(Exception ex) {
            logger.severe("error in method 'demo'" + ex);
        }
        return null;
    }

    /**
     * Remove tags in body part of the html page.
     *
     * @param page
     * @return
     * @throws Exception
     */
    private String tagRemoveBody(HtmlPage hpage) throws Exception{
        HtmlBody body = (HtmlBody)hpage.getBody();
        if (body == null) {
            logger.severe("Body is null");
            return null;
        } else {
            return domTagRemover.tagRemove(body);
        }
    }
}
