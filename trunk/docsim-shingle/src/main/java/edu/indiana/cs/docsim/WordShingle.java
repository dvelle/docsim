package edu.indiana.cs.docsim;

import java.util.logging.Logger;

/**
 * Word based shingle.
 * Each unit in a shingle is a word.
 *
 * @author
 * @version
 */
public class WordShingle extends Shingle {
    private static Logger logger =
        Logger.getLogger(WordShingle.class.getName());

    public WordShingle() {
        suMgr = new ShingleUnitWordBag();
    }
    public void buildShingle(String []tokens) {
        if (tokens == null || tokens.length == 0) {
            logger.warning("The parameter tokens is null or length " +
                    "of the array is 0. Maybe this is unexpected.");
            return;
        }
        // logger.info("add " + tokens.length + " tokens");
        for (int i = 0 ; i < tokens.length ; ++i) {
            addShingleUnit(tokens[i], i);
        }
    }

    public void addShingleUnit(String token, int pos) {
        if (token == null) {
            logger.warning("The parameter token is null. " +
                    "Maybe this is unexpected.");
            return;
        }
        // logger.info("add token: " + token + ":" + pos);
        suMgr.addShingleUnit(token, pos);
        this.setSize(this.getSize() + 1);
    }

    //TODO: to implement
    public boolean equals(Shingle shingle) {
        // return false;
        return true;
    }
    public boolean equals(Object object) {
        if (object instanceof Shingle) {
            return this.equals((Shingle)object);
        }
        logger.warning("You probably should not compare equility with" +
                "objects whose types are Object.");
        return false;
    }
}

