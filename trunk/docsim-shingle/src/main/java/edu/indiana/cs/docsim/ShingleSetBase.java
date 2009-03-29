package edu.indiana.cs.docsim;

import java.util.List;

/**
 * Represents a shingle set.
 *
 * @author
 * @version
 */
// public abstract class ShingleSetBase {
public interface ShingleSetBase <S extends Shingle> {
    /**
     * Calculate union of this set and passed set.
     *
     * @param set another set.
     * @return A new ShingleSet object which contains the result.
     */
    ShingleSetBase<S> union(ShingleSetBase<S> set);
    // public abstract ShingleSet union(ShingleSetBase set);

    /**
     * Calculate intersect of this set and passed set.
     *
     * @param set another set
     * @return A new ShingleSet object which contains the result.
     */
    ShingleSetBase<S> intersect(ShingleSetBase<S> set);
    // public abstract ShingleSet intersect(ShingleSetBase set);

    /**
     * Calculate difference of this set and passed set.
     *
     * @param set
     * @return <This set> - <passed set>
     */
    ShingleSetBase<S> difference(ShingleSetBase<S> set);
    // public abstract ShingleSet difference(ShingleSetBase set);

    /**
     * Get all shingles in this set.
     *
     * @return A list that includes all shingles.
     */
    // List<Shingle> getShingleList();
    List<S> getShingleList();

    /**
     * Get all unique shingles in this set.
     * @return
     */
    // List<Shingle> getUniqueShingleList();
    List<S> getUniqueShingleList();

    /**
     *
     * @return
     */
    List<ShingleData<S>> getShingleData();

    /**
     * Add a shingle to this shingle set.
     * If the shingle already exists at the specified location, the position
     * information would be added to the existing shingle data strcuture.
     *
     * @param shingle shingle to be added
     * @param pos     the position of the shingle in original document.
     *                <code>pos</code> parameter is NOT the position where the
     *                shingle would be put in this shingle set.
     */
    // void addShingle(Shingle shingle, int pos);
    void addShingle(S shingle, int pos);

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
    // public int[] getPos(Shingle shingle);
    public int[] getPos(S shingle);

    /**
     * Whether this set contains the specified shingle.
     *
     * @param shingle
     * @return
     */
    // public boolean contains(Shingle shingle);
    public boolean contains(S shingle);
}

