package edu.indiana.cs.docsim;

/**
 * This interface represents shingle units that are in metric space.
 * It means we can calculate distance between two shingle units and check
 * whether two shingles are "equal".
 *
 * @author
 * @version
 */
interface ShingleUnitMetric {
    Double  distance(ShingleUnit another);
    boolean equals(ShingleUnit another);
}

