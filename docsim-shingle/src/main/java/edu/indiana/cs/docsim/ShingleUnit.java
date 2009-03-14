package edu.indiana.cs.docsim;

abstract class ShingleUnit implements ShingleUnitMetric{
    public abstract String value();
    public abstract int    length();
    public abstract void   valueOf(String str);
}
