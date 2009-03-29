package edu.indiana.cs.docsim;

public abstract class ShingleUnitWeightedBag <SU extends ShingleUnit>
  extends ShingleUnitBag<SU> {
    private static String keyWeight = "weight";

    public boolean setWeight(SU shingleunit, double weight) {
        ShingleUnitData sud = getSUD(shingleunit);
        if (sud == null) {
            return false;
        } else {
            sud.setProperty(keyWeight, weight);
            return true;
        }
    }
    public double getWeight(SU shingleunit) {
        ShingleUnitData sud = getSUD(shingleunit);
        if (sud == null) {
            return -1;
        } else {
            return (Double)sud.getProperty(keyWeight);
        }
    }
}

