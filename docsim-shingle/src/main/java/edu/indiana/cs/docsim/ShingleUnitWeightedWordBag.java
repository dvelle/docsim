package edu.indiana.cs.docsim;

public class ShingleUnitWeightedWordBag
  extends ShingleUnitWeightedBag<ShingleUnitWord> {
  public void addShingleUnit(String str, int pos) {
      ShingleUnitWord su = new ShingleUnitWord(str);
      addShingleUnit(su, pos);
  }
//   public ShingleUnitWord getShingleUnit(String str) {
//       ShingleUnitWord su = new ShingleUnitWord(str);
    public boolean setWeight(String suStr, double weight) {
        return setWeight(new ShingleUnitWord(suStr), weight);
    }
    public double getWeight(String suStr) {
        return getWeight(new ShingleUnitWord(suStr));
    }
}

