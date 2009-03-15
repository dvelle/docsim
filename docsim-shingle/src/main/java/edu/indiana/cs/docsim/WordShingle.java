package edu.indiana.cs.docsim;

/**
 * Word based shingle.
 * Each unit in a shingle is a word.
 *
 * @author
 * @version
 */
public class WordShingle extends Shingle {
    public WordShingle() {
        suMgr = new ShingleUnitWordBag();
    }
    public void buildShingle(String []tokens) {
        for (int i = 0 ; i < tokens.length ; ++i) {
            suMgr.addShingleUnit(tokens[i], i);
        }
        this.setSize(tokens.length);
    }
    public void addShingleUnit(String token, int pos) {
        suMgr.addShingleUnit(token, pos);
        this.setSize(this.getSize() + 1);
    }
}

