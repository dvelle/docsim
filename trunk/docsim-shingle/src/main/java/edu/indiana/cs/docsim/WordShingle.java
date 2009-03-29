package edu.indiana.cs.docsim;

import java.util.List;
import java.util.logging.Logger;

/**
 * Word based shingle.
 * Each unit in a shingle is a word.
 *
 * @author
 * @version
 */
// public class WordShingle <SU extends ShingleUnit> extends Shingle {
public class WordShingle extends Shingle<ShingleUnitWord> {
    private static Logger logger =
        Logger.getLogger(WordShingle.class.getName());

    public WordShingle() {
        suMgr = new ShingleUnitWordBag();
    }

    public void buildShingle(String[] tokens) {
        if (tokens == null || tokens.length == 0) {
            logger.warning("The parameter tokens is null or length " +
                    "of the array is 0. Maybe this is unexpected.");
            return;
        }
        // logger.info("add " + tokens.length + " tokens");
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < tokens.length ; ++i) {
            addShingleUnit(tokens[i], i);
            sb.append(tokens[i] + " ");
        }
        this.setRawData(sb.toString());
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
        this.setRawData(this.getRawData() + " " + token);
    }

    //TODO: to implement
    public boolean equals(Shingle<ShingleUnitWord> shingle) {
    // public boolean equals(Shingle shingle) {
        if (shingle instanceof WordShingle) {
            double dist = distance(shingle);
            if (doubleEquals(dist, 0.0)) {
                // logger.info("found equal shingles: " + this.getRawData() + ":" + shingle.getRawData());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean equals(Object object) {
        if (object instanceof Shingle) {
            return this.equals((Shingle)object);
        }
        logger.warning("You probably should not compare equility with" +
                "objects whose types are Object.");
        return false;
    }
    protected boolean doubleEquals(double d1, double d2) {
        double epsilon = 1e-5;
        return Math.abs(d1-d2) < epsilon ? true : false;
    }

    @Override
    public Double distance(Shingle<ShingleUnitWord> another) {
        if (another instanceof WordShingle) {
            ShingleUnitMgr suMgr2 = another.getSuMgr();
            if (suMgr2 instanceof ShingleUnitWordBag) {
                return distanceOriginal((ShingleUnitWordBag)this.getSuMgr(),
                        (ShingleUnitWordBag)suMgr2);
            } else {
                return 1.0;
            }

        } else {
            return 1.0;
            // return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * This distance calculation is based on exact matching.
     *
     * @param sumgr1
     * @param sumgr2
     * @return return 0.0 if the two shingle unit managers are exactly same.
     * else return 1.0.
     */
    private static Double distanceOriginal(
            // ShingleUnitMgr sumgr1, ShingleUnitMgr sumgr2) {
            ShingleUnitWordBag sumgr1, ShingleUnitWordBag sumgr2) {
        // List<ShingleUnit> suls1 = sumgr1.getShingleUnits();
        List<ShingleUnitWord> suls1 = sumgr1.getShingleUnits();
        List<ShingleUnitWord> suls2 = sumgr2.getShingleUnits();
        if (suls1.size() != suls2.size()) {
            return 1.0;
        } else {
            for (int i = 0 ; i < suls1.size() ; ++i) {
                ShingleUnit su1 = suls1.get(i);
                ShingleUnit su2 = suls2.get(i);
                if (!su1.equals(su2)) {
                    return 1.0;
                }
            }
            return 0.0;
        }
    }

     // *  <ol>
     // *    <li>Ignore shingle unit frequency</li>
     // *  </ol>
    // private static double distanceFreq() {
    // }
}

