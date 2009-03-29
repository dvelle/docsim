package edu.indiana.cs.docsim;

/**
 * Represents shingle metric interface.
 */
// public interface ShingleMetric <SU extends ShingleUnit> {
public interface ShingleMetric <S extends Shingle> {
    // Double distance(Shingle<SU> another);
    // boolean equals(Shingle<SU> another);
    Double distance(S another);
    boolean equals(S another);
}
