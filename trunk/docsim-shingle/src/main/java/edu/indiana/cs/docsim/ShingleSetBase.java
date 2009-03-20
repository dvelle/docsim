package edu.indiana.cs.docsim;

import java.util.List;

/**
 *
 *
 * @author
 * @version
 */
// public abstract class ShingleSetBase {
public interface ShingleSetBase {
    /**
     * Calculate union of this set and passed set.
     *
     * @param set another set.
     * @return A new ShingleSet object which contains the result.
     */
    ShingleSet union(ShingleSetBase set);
    // public abstract ShingleSet union(ShingleSetBase set);

    /**
     * Calculate intersect of this set and passed set.
     *
     * @param set another set
     * @return A new ShingleSet object which contains the result.
     */
    ShingleSet intersect(ShingleSetBase set);
    // public abstract ShingleSet intersect(ShingleSetBase set);

    /**
     * Calculate difference of this set and passed set.
     *
     * @param set
     * @return <This set> - <passed set>
     */
    ShingleSet difference(ShingleSetBase set);
    // public abstract ShingleSet difference(ShingleSetBase set);

    /**
     * Get all shingles in this set.
     *
     * @return A list that includes all shingles.
     */
    List<Shingle> getShingleList();
    // public abstract List<Shingle> getShingleList();

    /**
     * Get all unique shingles in this set.
     * @return
     */
    List<Shingle> getUniqueShingleList();

    /**
     * Add a shingle to this set.
     *
     * @param shingle the shingle to be added to this set.
     */
    void addShingle(Shingle shingle, int pos);

    /**
     * Get number of shingles in this set.
     *
     * @return
     */
    int size();

    /**
     * Get number of unique shingles in this set.
     *
     * @return
     */
    public int sizeUnique();

    /**
     * Get
     *
     * @param shingle
     * @return
     */
    // Shingle getShingle(Shingle shingle);

    public int[] getPos(Shingle shingle);

    /**
     * Whether this set contains the specified shingle.
     *
     * @param shingle
     * @return
     */
    public boolean contains(Shingle shingle);
}
