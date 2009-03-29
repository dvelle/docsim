package edu.indiana.cs.docsim;

public abstract class ShingleUnit implements ShingleUnitMetric {
    /**
     * Get a string representation of this shingle unit.
     *
     * @return
     */
    public abstract String value();

    /**
     * Get length of this shingle unit.
     * @return
     */
    public abstract int length();

    /**
     * Build a shingle unit according to the input string.
     *
     * @param str
     */
    public abstract void update(String str);
}

