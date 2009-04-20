package edu.indiana.cs.docsim.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author
 * @version
 */
public class PageRepository {
    private static Logger logger =
        Logger.getLogger(PageRepository.class.getName());

    private static String defaultDataFileName = "res://searchResult.txt";

    private String dataFileName;

    private String searchService;

    private List<QueryEntry> queryEntries = new ArrayList<QueryEntry>();

    private void init(boolean useDefault, String fileName) throws Exception {
        if (useDefault) {
            dataFileName = defaultDataFileName;
        } else {
            dataFileName = fileName;
        }
        loadData();
    }

    public void add(QueryEntry entry) {
        queryEntries.add(entry);
    }

    private void loadData () throws Exception {
        if (dataFileName == null) {
            logger.warning("You have not set the data file");
            return;
        }
        if (dataFileName.endsWith(".xml")) {
            loadDataFromXML();
        } else {
            loadDataFromNewsCrawler();
        }
    }

    private void loadDataFromXML() throws IOException,
            ParserConfigurationException, SAXException, Exception {
        if (dataFileName == null) return;
        InputStream is = ResourceLoader.open(dataFileName);
        String data = IOUtils.toString(is);
        DocumentBuilderFactory docFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFac.newDocumentBuilder();
        Document docDom = docBuilder.parse(is);
        Element rootEle = docDom.getDocumentElement();
        NodeList nodeLs = rootEle.getElementsByTagName("query");

        for (int i = 0 ; i < nodeLs.getLength() ; ++i) {
            Node node = nodeLs.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element query = (Element)node;
                QueryEntry entry = QueryEntry.buildQueryEntry(query);
                add(entry);
            }
        }
    }

    private void loadDataFromNewsCrawler() throws IOException {
        if (dataFileName == null) return;
        InputStream is = ResourceLoader.open(dataFileName);
        String data = IOUtils.toString(is);
        String[] querydata = data.split("\n\n");
        for (int i = 0 ; i < querydata.length ; ++i) {
            String singlequery = querydata[i];
            singlequery = singlequery.trim();
            if (singlequery.length() == 0) continue;
            try {
                QueryEntry entry = QueryEntry.buildQueryEntry(singlequery);
                add(entry);
            } catch(Exception ex) {
            }
        }
    }

    public PageRepository(String fileName) throws Exception{
        init(false, fileName);
    }

    public PageRepository(boolean useDefault) throws Exception {
        init(useDefault, null);
    }

    /**
     * get the value of searchService
     * @return the value of searchService
     */
    public String getSearchService(){
        return this.searchService;
    }
    /**
     * set a new value to searchService
     * @param searchService the new value to be used
     */
    public void setSearchService(String searchService) {
        this.searchService=searchService;
    }

    /**
     * get the value of queryEntries
     * @return the value of queryEntries
     */
    public List<QueryEntry> getQueryEntries(){
        return this.queryEntries;
    }
    /**
     * set a new value to queryEntries
     * @param queryEntries the new value to be used
     */
    public void setQueryEntries(List<QueryEntry> queryEntries) {
        this.queryEntries=queryEntries;
    }
}

