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
public class WordWeightedShingle extends WordShingle {
    private static Logger logger =
        Logger.getLogger(WordWeightedShingle.class.getName());

    private ShingleUnitWeightedWordBag sumgr;

    public WordWeightedShingle() {
        // suMgr = new ShingleUnitWordBag();
        suMgr = new ShingleUnitWeightedWordBag();
    }

    public boolean equals(Shingle<ShingleUnitWord> shingle) {
    // public boolean equals(Shingle shingle) {
        if (shingle instanceof WordWeightedShingle) {
            double dist = distance(shingle);
            if (doubleEquals(dist, 0.0)) {
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
        if (another instanceof WordWeightedShingle) {
            ShingleUnitMgr suMgr2 = another.getSuMgr();
            if (suMgr2 instanceof ShingleUnitWeightedWordBag) {
                return distanceOriginal(
                        (ShingleUnitWeightedWordBag)this.getSuMgr(),
                        (ShingleUnitWeightedWordBag)suMgr2);
            } else {
                return 1.0;
            }

        } else {
            return 1.0;
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
    // TODO how to compare weighted shingle units of two shingles
    // Currently, just consider shingle units in command. Maybe in the future,
    // the difference also should be taken into account.
    private static Double distanceOriginal(
            ShingleUnitWeightedWordBag sumgr1, ShingleUnitWeightedWordBag sumgr2) {
        // List<ShingleUnit> suls1 = sumgr1.getShingleUnits();
        List<ShingleUnitData<ShingleUnitWord>> suls1 = sumgr1.getShingleUnitsData();
        List<ShingleUnitData<ShingleUnitWord>> suls2 = sumgr2.getShingleUnitsData();
        double distance = 0.0;

        for (int i = 0 ; i < suls1.size() ; ++i) {
            ShingleUnitData<ShingleUnitWord> su1 = suls1.get(i);
            // ShingleUnit su2 = suls2.get(i);
            if (suls2.contains(su1)) {
                distance += sumgr2.getWeight(su1.getShingleunit());
            }
        }
        return distance;
    }
}

