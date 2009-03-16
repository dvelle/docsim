package edu.indiana.cs.docsim;

import com.google.common.collect.Sets;

/**
 * Represents a shingle.
 *
 * @author
 * @version
 */
public abstract class Shingle implements ShingleMetric{
    protected String            rawData;
    protected ShingleUnitBag    suMgr;
    // protected ShingleUnitMgr    suMgr;
    protected TextTokenizer     tokenizer;
    protected int               size = 0;

    /**
     * get the value of size
     * @return the value of size
     */
    public int getSize(){
        return this.size;
    }
    /**
     * set a new value to size
     * @param size the new value to be used
     */
    public void setSize(int size) {
        this.size=size;
    }
    /**
     * get the value of rawData
     * @return the value of rawData
     */
    public String getRawData() {
        return this.rawData;
    }
    /**
     * set a new value to rawData
     * @param rawData the new value to be used
     */
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
    /**
     * get the value of suMgr
     * @return the value of suMgr
     */
    public ShingleUnitBag getSuMgr() {
        return this.suMgr;
    }
    /**
     * set a new value to suMgr
     * @param suMgr the new value to be used
     */
    public void setSuMgr(ShingleUnitBag suMgr) {
        this.suMgr = suMgr;
    }
    /**
     * get the value of tokenizer
     * @return the value of tokenizer
     */
    public TextTokenizer getTokenizer() {
        return this.tokenizer;
    }
    /**
     * set a new value to tokenizer
     * @param tokenizer the new value to be used
     */
    public void setTokenizer(TextTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public abstract void buildShingle(String []tokens);
    public abstract void addShingleUnit(String token, int pos);

    public void parse(String text) throws Exception{
        throw new Exception("Method parse has not been implemented yet!!!");
    }

    /**
     * This is a naive implementation of distance calculation.
     * If two shingles are the same, return 1; else return 0.
     * Usually subclasses should override this method to provide more
     * customized implementation.
     *
     * @param another
     * @return
     */
    public double distance(Shingle another) {
        if (this.equals(another)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    public abstract boolean equals(Shingle shingle);
}

