package edu.indiana.cs.docsim;

import com.google.common.collect.Sets;

/** 
 * Represents a shingle. 
 * 
 * @author 
 * @version 
 */
public abstract class Shingle{
    protected String rawData;
    protected ShingleUnitMgr suMgr;
    protected ShingleTokenizer tokenizer;

    /**
     * get the value of rawData
     * @return the value of rawData
     */
    public String getRawData(){
        return this.rawData;
    }
    /**
     * set a new value to rawData
     * @param rawData the new value to be used
     */
    public void setRawData(String rawData) {
        this.rawData=rawData;
    }
    /**
     * get the value of suMgr
     * @return the value of suMgr
     */
    public ShingleUnitMgr getSuMgr(){
        return this.suMgr;
    }
    /**
     * set a new value to suMgr
     * @param suMgr the new value to be used
     */
    public void setSuMgr(ShingleUnitMgr suMgr) {
        this.suMgr=suMgr;
    }
    /**
     * get the value of tokenizer
     * @return the value of tokenizer
     */
    public ShingleTokenizer getTokenizer(){
        return this.tokenizer;
    }
    /**
     * set a new value to tokenizer
     * @param tokenizer the new value to be used
     */
    public void setTokenizer(ShingleTokenizer tokenizer) {
        this.tokenizer=tokenizer;
    }

    public abstract void parse(String text);
}

