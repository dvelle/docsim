package edu.indiana.cs.docsim;

import java.util.List;
import java.util.logging.Logger;

/**
 * Word based shingle without considering order of the shingle units.
 * Each unit in a shingle is a word.
 *
 * @author
 * @version
 */
// public class WordShingle <SU extends ShingleUnit> extends Shingle {
// public class WordShingle extends Shingle<ShingleUnitWord> {
public class WordShingleNoOrder extends WordShingle {
    private static Logger logger =
        Logger.getLogger(WordShingleNoOrder.class.getName());

    public WordShingleNoOrder() {
        suMgr = new ShingleUnitWordBag();
    }


    //TODO: to implement
    // public boolean equals(Shingle shingle) {
    public boolean equals(Shingle<ShingleUnitWord> shingle) {
        if (shingle instanceof WordShingleNoOrder) {
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

    @Override
    public Double distance(Shingle<ShingleUnitWord> another) {
        if (another instanceof WordShingleNoOrder) {
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
     * This distance calculation does not consider order of the shingle units.
     * E.g. shingle "AAA BBB" is the same as shingle "BBB AAA".
     * Also cardinality of each shingle unit is ignored.
     * E.g. shingle "AAA AAA" is the same as "AAA".
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
        List<ShingleUnitWord> suls1 = sumgr1.getUniqueShingleUnits();
        List<ShingleUnitWord> suls2 = sumgr2.getUniqueShingleUnits();
        if (suls1.size() != suls2.size()) {
            return 1.0;
        } else {
            for (int i = 0 ; i < suls1.size() ; ++i) {
                ShingleUnitWord su1 = suls1.get(i);
                // ShingleUnitWord su2 = suls2.get(i);
                // if ((!suls2.contains(su1)) || (!suls1.contains(su2))) {
                if (!suls2.contains(su1)) {
                    return 1.0;
                }
            }
            return 0.0;
        }
    }
}

