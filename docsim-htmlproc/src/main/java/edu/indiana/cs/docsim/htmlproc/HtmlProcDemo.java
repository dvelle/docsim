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

public class HtmlProcDemo {
    private WebClient webClient;
    private static Logger logger =
        Logger.getLogger(HtmlProcDemo.class.getName());

    private void init() {
        webClient = new WebClient();

        // Don't load external resources referenced in the loaded page
        webClient.setCssEnabled(false);
        webClient.setAppletEnabled(false);
        webClient.setPopupBlockerEnabled(true);
        // webClient.setCookiesEnabled(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.setJavaScriptEnabled(false);

        // webClient.setPrintContentOnFailingStatusCode(false);
        // webClient.setThrowExceptionOnFailingStatusCode(false);
        // webClient.setThrowExceptionOnScriptError(false);

        // webClient.setCookieManager(cookies);

        // webClient.addRequestHeader("Cookie", this.cookie);
    }

    public HtmlProcDemo () {
        init();
    }
    public void demo(URL urlWebPage) {
        try {
            Page page = webClient.getPage(urlWebPage);
            if (page instanceof HtmlPage) {
                HtmlPage hpage = (HtmlPage)page;
                HtmlBody body = (HtmlBody)hpage.getBody();
                // NodeList nodels = hpage.getElementsByTagName("body");
                // logger.info("# is" + nodels.getLength());
                // HtmlBody body = (HtmlBody)nodels.item(0);
                logger.info(body.toString());
                if (body == null) {
                    logger.severe("Body is null");
                } else {
                    parseBody(body);
                }
                // logger.info("Title:" + hpage.getTitleText());
            } else {
                logger.warning("Unrecognized page type");
            }
        } catch(Exception ex) {
            logger.severe("error in method 'demo'" + ex);
        }
    }

    private void parseBody(HtmlBody body) {
        NodeList nodels = body.getChildNodes();
        int nodecount = nodels.getLength();
        for (int i = 0 ; i < nodecount ; ++i) {
            Node node = nodels.item(i);
            // logger.info("ele node type:" + Node.ELEMENT_NODE);
            // logger.info("type:" + node.getNodeName() + ":" +
            //         node.getNodeType() + ";" + node.toString());
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    parseElement((HtmlElement)node);
                    break;
            }
        }
    }

    private void parseElement(HtmlElement element) {

        // dump current node
        String tagName = element.getTagName();
        String infoattrname = "docsiminfo";
        if (tagName.compareToIgnoreCase("div") == 0) {
            HtmlDivision div = (HtmlDivision)element;
            String value = div.getAttribute(infoattrname);
            // logger.info("div size:" + value);
        }

        // String value = element.getAttribute(infoattrname);
        // logger.info("size:" + value);

        // recursively parse child nodes
        NodeList nodels = element.getChildNodes();
        int nodecount = nodels.getLength();
        for (int i = 0 ; i < nodecount ; ++i) {
            Node node = nodels.item(i);
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    parseElement((HtmlElement)node);
            }
        }
    }

    public void demoMain() {
        String fileName = "augmented_page.html";
        URL url = mappingFile2URL(fileName);
        logger.info("URL is " + url);
        demo(url);
    }

    private URL mappingFile2URL(String fileName) {
        ClassLoader cl = HtmlProcDemo.class.getClassLoader();
        URL url = cl.getResource(fileName.trim());
        return url;
    }
}
