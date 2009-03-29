package edu.indiana.cs.docsim;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Represents a shingle set.
 *
 * @author
 * @version
 */
public class ShingleSet <S extends Shingle> implements ShingleSetBase<S> {

    private static Logger logger =
        Logger.getLogger(ShingleSet.class.getName());

    // private List<Shingle> shingles = Lists.newArrayList();
    private List<S> shingles = Lists.newArrayList();
    private List<ShingleData<S>> shinglesUnique = Lists.newArrayList();

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
    // public void addShingle(Shingle shingle, int pos) {
    public void addShingle(S shingle, int pos) {
        Iterator<ShingleData<S>> it = shinglesUnique.iterator();
        boolean found = false;
        while (it.hasNext()) {
            ShingleData<S> dataEntry = it.next();
            if (dataEntry.equals(shingle)) {//found it
                dataEntry.addPos(pos);
                found = true;
                break;
            }
        }
        if (!found) {
            ShingleData<S> sd = new ShingleData<S>(shingle, pos);
            shinglesUnique.add(sd);
        }
        shingles.add(shingle);
    }

    public int size() {
        return shingles.size();
    }

    public int sizeUnique() {
        return shinglesUnique.size();
    }

    /**
     * Calculates union of this shingle set and the passed-in shingle set.
     * After this union operation, extra care should be taken to use the
     * returned ShingleSet.
     * "Unique shingle" is not well defined.
     * So, method {@link #getUniqueShingleList} should not be used.
     *
     * @param set
     * @return
     */
    public ShingleSet<S> union(ShingleSetBase<S> set) {
        //FIXME: here the position of the shingle does not make sense.
        //The same shingle may appear in different places within the
        //two shingle sets.
        ShingleSet<S> result = new ShingleSet<S>();
        for (int i = 0 ; i < shingles.size() ; ++i) {
            S shingle = shingles.get(i);
            result.addShingle(shingle, i);
        }

        List<S> shingles2 = set.getShingleList();
        for (int i = 0 ; i < shingles2.size() ; ++i) {
            S shingle = shingles2.get(i);
            result.addShingle(shingle, i);
        }
        return result;
    }

    /**
     * Calculates intersect of this shingle set and the passed-in shingle set.
     * After this intersect operation, extra care should be taken to use the
     * returned ShingleSet.
     * "Unique shingle" is not well defined.
     * So, method {@link #getUniqueShingleList} should not be used.
     *
     * @param set
     * @return
     */
    public ShingleSet<S> intersect(ShingleSetBase<S> set) {
        ShingleSet<S> result = new ShingleSet<S>();
        List<S> shingles2 = set.getShingleList();
        for (int i = 0 ; i < shingles.size() ; ++i) {
            S shingle = shingles.get(i);
            if (set.contains(shingle)) {
                // FIXME: here the position of the shingle does not make sense
                // The same shingle may appear in different places within the
                // two shingle sets.
                result.addShingle(shingle, i);
            }
        }
        return result;
    }

    //TODO: to implement
    public ShingleSetBase<S> difference(ShingleSetBase<S> set) {
        return null;
    }

    // public List<Shingle> getShingleList() {
    public List<S> getShingleList() {
        return shingles;
    }

    // public List<Shingle> getUniqueShingleList() {
    public List<S> getUniqueShingleList() {
        List<S> list = Lists.newArrayList();
        Iterator<ShingleData<S>> it = shinglesUnique.iterator();
        while (it.hasNext()) {
            ShingleData<S> dataEntry = it.next();
            list.add(dataEntry.getShingle());
        }
        return list;
    }

    // public int[] getPos(Shingle shingle) {
    public int[] getPos(S shingle) {
        ShingleData<S> sd = getSD(shingle);
        if (sd == null) {
            return new int[0];
        } else {
            return sd.getPositions();
        }
    }

    // private ShingleData getSD(Shingle shingle) {
    private ShingleData<S> getSD(S shingle) {
        Iterator<ShingleData<S>> it = shinglesUnique.iterator();
        while (it.hasNext()) {
            ShingleData<S> dataEntry = it.next();
            if (dataEntry.equals(shingle)) {//found it
                return dataEntry;
            }
        }
        return null;
    }

    // public boolean contains(Shingle shingle) {
    public boolean contains(S shingle) {
        for (int i = 0 ; i < shingles.size() ; ++i) {
            if (shingle.equals(shingles.get(i))) {
                return true;
            }
        }
        return false;
    }

    // public List<Shingle> getShingleListUnique() {
    // }

    /**
     * The returned value is backed by this object.
     * So changing elements stored in return value would also change the
     * internal data structure in this object.
     *
     * @return
     */
    public List<ShingleData<S>> getShingleData() {
        return this.shinglesUnique;
    }
}

