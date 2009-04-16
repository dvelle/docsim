package edu.indiana.cs.docsim.data;

import com.google.common.base.Objects;

public class SearchResultEntry {
    private String title;
    private String url;

    /**
     * get the value of url
     * @return the value of url
     */
    public String getUrl(){
        return this.url;
    }
    /**
     * set a new value to url
     * @param url the new value to be used
     */
    public void setUrl(String url) {
        this.url=url;
    }
    /**
     * get the value of title
     * @return the value of title
     */
    public String getTitle(){
        return this.title;
    }
    /**
     * set a new value to title
     * @param title the new value to be used
     */
    public void setTitle(String title) {
        this.title=title;
    }

    public SearchResultEntry(String title, String url) {
        this.title = title;
        this.url   = url;
    }

    public boolean equals(Object another) {
        if (this == another) return true;
        if (another instanceof SearchResultEntry) {
            SearchResultEntry entry = (SearchResultEntry)another;
            return this.getTitle().equals(entry.getTitle()) &&
                this.getUrl().equals(entry.getUrl());
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(getTitle(), getUrl());
    }
}
