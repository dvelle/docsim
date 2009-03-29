package edu.indiana.cs.docsim;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

class ShingleData <S extends Shingle> {
    private static Logger logger =
        Logger.getLogger(Shingle.class.getName());

    private Set<Integer> pos = Sets.newHashSet();
    private S shingle;
    // private Shingle shingle;

    // public ShingleData(Shingle shingle, int position) {
    //     this.shingle = shingle;
    //     pos.add(position);
    // }
    public ShingleData(S shingle, int position) {
        this.shingle = shingle;
        pos.add(position);
    }

    public void addPos(int position) {
        pos.add(position);
    }

    /**
     * Get position of appearances of this shingle.
     * Users can modify the returned value WITHOUT affecting the original
     * data. Currently, the positions are not ordered.
     * TODO: guarantee the returned positions from small to large?
     *
     * @return
     */
    public int[] getPositions() {
        int[] intArray = new int[pos.size()];
        Iterator<Integer> it = pos.iterator();
        int i = 0;
        while (it.hasNext()) {
            intArray[i++] = it.next();
        }
        return intArray;
    }

    public int getCount() {
        return pos.size();
    }

    public boolean equals(ShingleData<S> sd) {
        return this.equals(sd.getShingle());
    }

    public boolean equals(Shingle shingle) {
        return this.shingle.equals(shingle);
    }

    public boolean equals(Object object) {
        if (this.getClass() == object.getClass()) {
            return this.equals((ShingleData<S>)object);
        } else if (object instanceof Shingle) {
            return this.equals((Shingle)object);
        } else {
            logger.severe("You probably compare objects of wrong types!!!");
            return false;
        }
    }

    public int hashCode(){
        return Objects.hashCode(getShingle());
    }


    /**
     * get the value of shingleunit
     * @return the value of shingleunit
     */
    // public Shingle getShingle() {
    public S getShingle() {
        return this.shingle;
    }
    /**
     * set a new value to shingleunit
     * @param shingleunit the new value to be used
     */
    // public void setShingle(Shingle shingle) {
    public void setShingle(S shingle) {
        this.shingle = shingle;
    }
}

