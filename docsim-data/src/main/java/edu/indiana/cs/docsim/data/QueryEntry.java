package edu.indiana.cs.docsim.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class QueryEntry {
    public static String keyQuery = "Query";
    public static String keyTotalResult = "TotalResult";
    public static String keyTitle = "Title";
    public static String keyUrl   = "Url";

    public static QueryEntry buildQueryEntry(String entryData)
      throws Exception {
        QueryEntry qentry = new QueryEntry();
        String[] lines = entryData.split("\n");
        for (int i = 0 ; i < lines.length ; ++i) {
            String line = lines[i];
            int idx = line.indexOf(":");
            if (idx == -1)
                continue;
            String key = line.substring(0, idx).trim();
            String value = line.substring(idx+1).trim();
            if (key.equalsIgnoreCase(keyQuery)) {
                qentry.setQuery(value);
            } else if (key.equalsIgnoreCase(keyTotalResult)) {
                qentry.setResultCount(Integer.valueOf(value));
            } else if (key.equalsIgnoreCase(keyTitle)) {
                ++i;
                String title = value;
                line = lines[i];
                idx = line.indexOf(":");
                if (idx == -1) {
                    --i;
                    continue;
                }
                key = line.substring(0, idx).trim();
                if (!key.equalsIgnoreCase(keyUrl)) {
                    --i;
                    continue;
                }
                String url = line.substring(idx+1).trim();
                SearchResultEntry sre = new SearchResultEntry(title, url);
                qentry.addSearchResultEntry(sre);
            }
        }
        return qentry;
    }

    // Sample data
    /*
    Query: Conquest Vacations shuts down
    TotalResult: 5
    Title: Conquest Vacations shuts down
    Url: http://www.globeinvestor.com/servlet/story/RTGAM.20090415.wconquest0415/GIStory/
    Title: Conquest Vacations shuts down tour operations
    Url: http://www.cbc.ca/canada/new-brunswick/story/2009/04/15/conquest-vacations-ceas.html?ref=rss
    Title: CONQUEST VACATIONS SHUTS DOWN OPERATIONS
    Url: http://www.cfrb.com/conquest
    Title: Conquest Vacations shuts down
    Url: http://www.torontosun.com/news/torontoandgta/2009/04/15/9122671.html
    Title: Conquest Vacations shuts down
    Url: http://www.globaltv.com/globaltv/national/story.html?id=1499209
    */
    private String  query;
    private int     resultCount;
    // private Set<SearchResultEntry> results = new HashSet<SearchResultEntry>();
    private List<SearchResultEntry> results = new ArrayList<SearchResultEntry>();

    /**
     * get the value of results
     * @return the value of results
     */
    public List<SearchResultEntry> getResults(){
        return this.results;
    }
    /**
     * set a new value to results
     * @param results the new value to be used
     */
    public void setResults(List<SearchResultEntry> results) {
        this.results=results;
    }
    /**
     * get the value of resultCount
     * @return the value of resultCount
     */
    public int getResultCount(){
        return this.resultCount;
    }
    /**
     * set a new value to resultCount
     * @param resultCount the new value to be used
     */
    public void setResultCount(int resultCount) {
        this.resultCount=resultCount;
    }
    /**
     * get the value of query
     * @return the value of query
     */
    public String getQuery(){
        return this.query;
    }
    /**
     * set a new value to query
     * @param query the new value to be used
     */
    public void setQuery(String query) {
        this.query=query;
    }

    public void addSearchResultEntry(SearchResultEntry entry) {
        if (!results.contains(entry))
            results.add(entry);
    }
}
