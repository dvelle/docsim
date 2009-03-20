package edu.indiana.cs.docsim.htmlproc;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class TagBlackList {
    private List<String> tagls = new ArrayList<String>();

    private static TagBlackList instance  = null;

    public static TagBlackList getInstance() {
        if (instance == null) {
            String[] tags = {"script", "noscript"};
            instance = new TagBlackList();
            // TODO: Add more tags in balck list
            instance.add(tags);
        }
        return instance;
    }

    public void add(String tag) {
        if (!contains(tag))
            tagls.add(tag);
    }

    public void add(String[] tags) {
        for (int i = 0 ; i < tags.length ; ++i) {
            add(tags[i]);
        }
    }

    public boolean contains(String tag) {
        for (Iterator<String>it = tagls.iterator(); it.hasNext(); ) {
            String taginlist = (String)it.next();
            if (tag.compareToIgnoreCase(taginlist) == 0) {
                return true;
            }
        }
        return false;
    }
}

public class DomTagRemover {
    private static Logger logger =
        Logger.getLogger(DomTagRemover.class.getName());

    private static int DefaultMaxTxtLength = 1024 * 1024;// 1 M
    private static String  DefaultEncoding = "UTF-8";
    private static TagBlackList tagBlackList = TagBlackList.getInstance();

    // maximum length of output of tag removal operation
    private int     maxTxtLength;
    private String  encoding;
    private int     count = 0;

    /**
     * flat to indicate whether white spaces(\t\n\x0B\f\r) are suppressed.
     * All white spaces are converted to blank spaces. And then adjacent blank
     * spaces are collapsed to one single blank space.
     */
    private boolean supressWhiteSpaces = true;

    private void init(Integer maxTxtLength, String encoding) {
        if (maxTxtLength != null) {
            this.maxTxtLength = maxTxtLength;
        } else {
            this.maxTxtLength = DefaultMaxTxtLength;
        }

        if (encoding != null) {
            this.encoding = encoding;
        } else {
            this.encoding = DefaultEncoding;
        }
    }

    public DomTagRemover(int maxTxtLength) {
        init(maxTxtLength, null);
    }

    public DomTagRemover(String encoding) {
        init(null, encoding);
    }

    /**
     * Constructor.
     *
     * @param maxTxtLength max allowed length of output of tag removal.
     * Currently default value is 1 MBytes.
     * @param encoding encoding of the content in dom element.
     */
    public DomTagRemover(int maxTxtLength, String encoding) {
        init(maxTxtLength, encoding);
    }

    public DomTagRemover() {
        init(null, null);
    }

    /**
     * Remove all tags in the passed DOM element.
     * Tags and their attributes are all removed.
     *
     * @param element tags in the element would be removed.
     * @return a string
     */
    public String tagRemove(Element element)
      throws Exception {
        StringWriter sw = new StringWriter();
        tagRemove(element, sw);
        sw.close();
        return sw.toString();
    }

    /**
     * Remove tags.
     * How to gut the encoding:
     *  (1) Try to get encoding spec from DOM element
     *    (a) getInputEncoding
     *    (b) getXmlEncoding
     *  (2) Use property "encoding" of this instance
     *
     * FIXME: The order of checking encoding may should be interchanged.
     * @param element
     * @param os
     */
    public void tagRemove(Element element, OutputStream os)
      throws Exception{
        Document doc = element.getOwnerDocument();
        String encoding = null;
        if (doc != null) {
            encoding = doc.getInputEncoding();
            if (encoding == null) {
                encoding = doc.getXmlEncoding();
            }
        }
        if (encoding == null) {
            encoding = this.encoding;
        }
        tagRemove(element, os, encoding);
    }

    /**
     * Remove tags in the element and resultant data is written to the stream
     * <code>os</code>.
     *
     * Usually this method should not be used because encoding can be
     * retrieved from the element. In case you build an in-memory DOM element,
     * this method may be helpful.
     *
     * @param element
     * @param os
     * @param encoding the encoding type.
     */
    public void tagRemove(Element element, OutputStream os, String encoding)
      throws Exception{
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, encoding));
        tagRemove(element, bw);
    }

    /**
     * Remove tags and resultant data is written to the <code>writer</code>.
     * All other related methods should finally invoke this method. If not,
     * problems may occur.
     *
     * @param element
     * @param writer
     */
    public void tagRemove(Element element, Writer writer)
      throws Exception {
        count = 0;
        StringBuilder sb = new StringBuilder();
        tagRemoveRecursive(element, writer, sb);
        writer.write(filter(sb.toString()).trim());
        writer.flush();
        count = 0;
    }

    /**
     * This is the main method to remove tags internally.
     *
     * @param element
     * @param writer
     */
    private void tagRemoveRecursive(Element element, Writer writer,
            StringBuilder sb)
      throws Exception{

        // check whether the element is blocked
        if (tagBlackList.contains(element.getTagName())) {
            return;
        }

        NodeList children = element.getChildNodes();

        // TODO: If maxTxtLength is greater than max allowed value of int
        // type, following statement would have problems. Usually, we would
        // not want to parse so large (2^31 -> 2G) dom elements.
        for (int i = 0 ; i < children.getLength() ; ++i) {
            Node node = children.item(i);
            switch (node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    tagRemoveRecursive((Element)node, writer, sb);
                    break;
                case Node.TEXT_NODE:
                    String value = node.getNodeValue();
                    count += value.length();
                    if (count > this.maxTxtLength) {
                        throw new Exception("Exceeds max allowed output " +
                                "of tag removal");
                    }
                    sb.append(filter(value));
                    break;
                default:
            }
        }
        sb.replace(0, sb.length(), filter(sb.toString()));
    }

    public String tagRemove(String strElement)
      throws Exception{
        return tagRemove(parse(strElement));
    }
    public void tagRemove(String strElement, OutputStream os)
      throws Exception{
        tagRemove(parse(strElement), os);
    }
    public void tagRemove(String strElement, OutputStream os, String encoding)
      throws Exception{
        tagRemove(parse(strElement), os, encoding);
    }
    public void tagRemove(String strElement, Writer writer)
      throws Exception{
        tagRemove(parse(strElement), writer);
    }
    private Element parse(String str) throws Exception{
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(str)));
            Element element = doc.getDocumentElement();
            return element;
        }
        catch(Exception ex) {
            throw new Exception("Exception in converting string to dom " +
                    ex);
        }
    }

    private String filter(String str) {
        if (supressWhiteSpaces) {
            return " " + str.trim().replaceAll("\\s+"," ");
        } else{
            return str;
        }
    }

    /**
     * get the value of supressWhiteSpaces
     * @return the value of supressWhiteSpaces
     */
    public boolean getSupressWhiteSpaces(){
        return this.supressWhiteSpaces;
    }
    /**
     * set a new value to supressWhiteSpaces
     * @param supressWhiteSpaces the new value to be used
     */
    public void setSupressWhiteSpaces(boolean supressWhiteSpaces) {
        this.supressWhiteSpaces=supressWhiteSpaces;
    }
}

