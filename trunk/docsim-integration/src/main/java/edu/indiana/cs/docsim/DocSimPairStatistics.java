package edu.indiana.cs.docsim;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocSimPairStatistics {

    private String docSrc1;
    private String docSrc2;
    private ShingleAlgorithm shingleAlg;

    public static String doubleFormat = "0.000000";
    DecimalFormat doubleFormatter = new DecimalFormat(doubleFormat);

    private int     shingleSize                         = -1;
    private int     docShingleSetSize1                  = -1;
    private int     docShingleSetSizeUnique1            = -1;
    private int     docShingleSetSize2                  = -1;
    private int     docShingleSetSizeUnique2            = -1;
    private int     docShingleSetUnionSize              = -1;
    private int     docShingleSetUnionSizeUnique        = -1;
    private int     docShingleSetIntersectSize          = -1;
    private int     docShingleSetIntersectSizeUnique    = -1;
    private boolean positive                            = true;

    private List<Double> additionalSimilarity = new ArrayList<Double>();

    public void addAdditionalSimilarity(double value) {
        additionalSimilarity.add(value);
    }

    /**
     * get the value of additionalSimilarity
     * @return the value of additionalSimilarity
     */
    public List<Double> getAdditionalSimilarity(){
        return this.additionalSimilarity;
    }
    /**
     * set a new value to additionalSimilarity
     * @param additionalSimilarity the new value to be used
     */
    public void setAdditionalSimilarity(List<Double> additionalSimilarity) {
        this.additionalSimilarity=additionalSimilarity;
    }

    // private Double  docShinglingRatio;

    /**
     * get the value of docShinglingRatio
     * @return the value of docShinglingRatio
     */
    public Double getDocShinglingRatioUnique(){
        if (this.getDocShingleSetUnionSizeUnique() == 0)
            return Double.POSITIVE_INFINITY;
        if (this.getDocShingleSetUnionSizeUnique() == -1 ||
            this.getDocShingleSetIntersectSizeUnique() == -1) {
            return Double.NaN;
        }
        return this.getDocShingleSetIntersectSizeUnique() * 1.0
            /this.getDocShingleSetUnionSizeUnique();
    }

    /**
     * get the value of docShingleSetIntersectSize
     * @return the value of docShingleSetIntersectSize
     */
    public int getDocShingleSetIntersectSize(){
        return this.docShingleSetIntersectSize;
    }
    /**
     * set a new value to docShingleSetIntersectSize
     * @param docShingleSetIntersectSize the new value to be used
     */
    public void setDocShingleSetIntersectSize(int docShingleSetIntersectSize) {
        this.docShingleSetIntersectSize=docShingleSetIntersectSize;
    }

    /**
     * get the value of docSrc2
     * @return the value of docSrc2
     */
    public String getDocSrc2(){
        return this.docSrc2;
    }
    /**
     * set a new value to docSrc2
     * @param docSrc2 the new value to be used
     */
    public void setDocSrc2(String docSrc2) {
        this.docSrc2=docSrc2;
    }
    /**
     * get the value of docSrc1
     * @return the value of docSrc1
     */
    public String getDocSrc1(){
        return this.docSrc1;
    }
    /**
     * set a new value to docSrc1
     * @param docSrc1 the new value to be used
     */
    public void setDocSrc1(String docSrc1) {
        this.docSrc1=docSrc1;
    }
    /**
     * get the value of shingleSize
     * @return the value of shingleSize
     */
    public int getShingleSize(){
        return this.shingleSize;
    }
    /**
     * set a new value to shingleSize
     * @param shingleSize the new value to be used
     */
    public void setShingleSize(int shingleSize) {
        this.shingleSize=shingleSize;
    }
    /**
     * get the value of docShingleSetSize1
     * @return the value of docShingleSetSize1
     */
    public int getDocShingleSetSize1(){
        return this.docShingleSetSize1;
    }
    /**
     * set a new value to docShingleSetSize1
     * @param docShingleSetSize1 the new value to be used
     */
    public void setDocShingleSetSize1(int docShingleSetSize1) {
        this.docShingleSetSize1=docShingleSetSize1;
    }
    /**
     * get the value of docShingleSetSizeUnique1
     * @return the value of docShingleSetSizeUnique1
     */
    public int getDocShingleSetSizeUnique1(){
        return this.docShingleSetSizeUnique1;
    }
    /**
     * set a new value to docShingleSetSizeUnique1
     * @param docShingleSetSizeUnique1 the new value to be used
     */
    public void setDocShingleSetSizeUnique1(int docShingleSetSizeUnique1) {
        this.docShingleSetSizeUnique1=docShingleSetSizeUnique1;
    }
    /**
     * get the value of docShingleSetSize2
     * @return the value of docShingleSetSize2
     */
    public int getDocShingleSetSize2(){
        return this.docShingleSetSize2;
    }
    /**
     * set a new value to docShingleSetSize2
     * @param docShingleSetSize2 the new value to be used
     */
    public void setDocShingleSetSize2(int docShingleSetSize2) {
        this.docShingleSetSize2=docShingleSetSize2;
    }
    /**
     * get the value of docShingleSetSizeUnique2
     * @return the value of docShingleSetSizeUnique2
     */
    public int getDocShingleSetSizeUnique2(){
        return this.docShingleSetSizeUnique2;
    }
    /**
     * set a new value to docShingleSetSizeUnique2
     * @param docShingleSetSizeUnique2 the new value to be used
     */
    public void setDocShingleSetSizeUnique2(int docShingleSetSizeUnique2) {
        this.docShingleSetSizeUnique2=docShingleSetSizeUnique2;
    }
    /**
     * get the value of docShingleSetUnionSize
     * @return the value of docShingleSetUnionSize
     */
    public int getDocShingleSetUnionSize(){
        return this.docShingleSetUnionSize;
    }
    /**
     * set a new value to docShingleSetUnionSize
     * @param docShingleSetUnionSize the new value to be used
     */
    public void setDocShingleSetUnionSize(int docShingleSetUnionSize) {
        this.docShingleSetUnionSize=docShingleSetUnionSize;
    }
    /**
     * get the value of docShingleSetUnionSizeUnique
     * @return the value of docShingleSetUnionSizeUnique
     */
    public int getDocShingleSetUnionSizeUnique(){
        return this.docShingleSetUnionSizeUnique;
    }
    /**
     * set a new value to docShingleSetUnionSizeUnique
     * @param docShingleSetUnionSizeUnique the new value to be used
     */
    public void setDocShingleSetUnionSizeUnique(int docShingleSetUnionSizeUnique) {
        this.docShingleSetUnionSizeUnique=docShingleSetUnionSizeUnique;
    }
    /**
     * get the value of docShingleSetIntersectSizeUnique
     * @return the value of docShingleSetIntersectSizeUnique
     */
    public int getDocShingleSetIntersectSizeUnique(){
        return this.docShingleSetIntersectSizeUnique;
    }
    /**
     * set a new value to docShingleSetIntersectSizeUnique
     * @param docShingleSetIntersectSizeUnique the new value to be used
     */
    public void setDocShingleSetIntersectSizeUnique(int docShingleSetIntersectSizeUnique) {
        this.docShingleSetIntersectSizeUnique=docShingleSetIntersectSizeUnique;
    }
    /**
     * get the value of shingleAlg
     * @return the value of shingleAlg
     */
    public ShingleAlgorithm getShingleAlg(){
        return this.shingleAlg;
    }
    /**
     * set a new value to shingleAlg
     * @param shingleAlg the new value to be used
     */
    public void setShingleAlg(ShingleAlgorithm shingleAlg) {
        this.shingleAlg=shingleAlg;
    }
    /**
     * get the value of positive
     * @return the value of positive
     */
    public boolean getPositive(){
        return this.positive;
    }
    /**
     * set a new value to positive
     * @param positive the new value to be used
     */
    public void setPositive(boolean positive) {
        this.positive=positive;
    }
}
