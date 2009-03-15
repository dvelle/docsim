package edu.indiana.cs.docsim;

public interface ShingleMetric {
    double distance(Shingle another);
    boolean equals(Shingle another);
}
