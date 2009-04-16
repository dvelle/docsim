package edu.indiana.cs.docsim.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 *
 *
 * @author
 * @version
 */
public class PageRepository {

    private static String defaultDataFileName = "res://searchResult.txt";

    private String dataFileName;

    private String searchService;

    private List<QueryEntry> queryEntries = new ArrayList<QueryEntry>();

    private void init(boolean useDefault, String fileName) throws IOException {
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

    private void loadData() throws IOException {
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

    public PageRepository(String fileName) throws IOException{
        init(false, fileName);
    }

    public PageRepository(boolean useDefault) throws IOException {
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

