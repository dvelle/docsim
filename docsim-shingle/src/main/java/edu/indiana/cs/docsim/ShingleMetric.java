package edu.indiana.cs.docsim;

interface ShingleMetric{
    double distance(Shingle another);
    boolean equals(Shingle another);
}
