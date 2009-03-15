package edu.indiana.cs.docsim;

public class ShingleUnitWordBag
  extends ShingleUnitBag<ShingleUnitWord> {
  public void addShingleUnit(String str, int pos) {
      ShingleUnitWord su = new ShingleUnitWord(str);
      addShingleUnit(su, pos);
  }
}

