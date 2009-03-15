package edu.indiana.cs.docsim;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Lists;

public abstract class ShingleUnitBag <SU extends ShingleUnit>
  implements ShingleUnitMgr<SU> {
    Logger logger = Logger.getLogger(ShingleUnitBag.class.getName());

    // private String rawData;
    private List<SU> shingleUnitList = Lists.newArrayList();
    private List<ShingleUnitData> shingleUnitListUnique = Lists.newArrayList();

    public abstract void addShingleUnit(String str, int pos);

    /**
     * Add a shingle unit to this manager.
     *
     * @param shingleunit the shingle unit to be added
     * @param pos         position of appearance
     */
    public void addShingleUnit(SU shingleunit, int pos) {
        Iterator<ShingleUnitData> it = shingleUnitListUnique.iterator();
        boolean found = false;
        while (it.hasNext()) {
            ShingleUnitData dataEntry = it.next();
            if (dataEntry.equals(shingleunit)) {//found it
                dataEntry.addPos(pos);
                found = true;
                break;
            }
        }
        if (!found) {
            ShingleUnitData sud = new ShingleUnitData(shingleunit, pos);
            shingleUnitListUnique.add(sud);
        }
        shingleUnitList.add(shingleunit);
    }

    /**
     * get number of shingle units.
     *
     * @return
     */
    public int size() {
        return shingleUnitList.size();
    }

    /**
     * get number of unique shingle units.
     *
     * @return
     */
    public int sizeUnique() {
        return shingleUnitListUnique.size();
    }

    /**
     * get the shingle at the specific position.
     *
     * @param index
     * @return
     */
    // ShingleUnit getByIndex(int index) {
    public SU getByIndex(int index) {
        try {
            return shingleUnitList.get(index);
        } catch(IndexOutOfBoundsException e) {
            logger.severe("Index " + index + " is out of bound\n" + e);
        }
        return null;
    }


    /**
     * Get all unique shingle units.
     * Duplicate shingle units just appear once in the returned array.
     *
     * @return
     */
    // ShingleUnit[] getUniqueShingleUnits();
    public List<SU> getUniqueShingleUnits() {
        List<SU> list = Lists.newArrayList();
        Iterator<ShingleUnitData> it = shingleUnitListUnique.iterator();
        while (it.hasNext()) {
            ShingleUnitData dataEntry = it.next();
            list.add(dataEntry.getShingleunit());
        }
        return list;
    }

    /**
     * Get number of appearances of a shingle unit.
     *
     * @param shingleunit
     * @return
     */
    // int getCount(ShingleUnit shingleunit);
    public int getCount(SU shingleunit) {
        ShingleUnitData sud = getSUD(shingleunit);
        if (sud == null) {
            return 0;
        } else {
            return sud.getCount();
        }
    }

    private ShingleUnitData getSUD(SU shingleunit) {
        Iterator<ShingleUnitData> it = shingleUnitListUnique.iterator();
        while (it.hasNext()) {
            ShingleUnitData dataEntry = it.next();
            if (dataEntry.equals(shingleunit)) {//found it
                return dataEntry;
            }
        }
        return null;
    }

    /**
     * Get positions of a shingle unit.
     *
     * @param shingleunit
     * @return
     */
    // int[] getPos(ShingleUnit shingleunit);
    public int[] getPos(SU shingleunit) {
        ShingleUnitData sud = getSUD(shingleunit);
        if (sud == null) {
            return new int[0];
        } else {
            return sud.getPositions();
        }
    }

    /**
     * Remove the specified shingle unit.
     *
     * @param shingleunit
     */
    public void removeShingleUnit(SU shingleunit) {
        shingleUnitList.remove(shingleunit);
        shingleUnitListUnique.remove(new ShingleUnitData(shingleunit, -1));
    }

    class ShingleUnitData {
        private Set<Integer> pos = Sets.newHashSet();
        private SU shingleunit;

        public ShingleUnitData(SU shingleunit, int position) {
            this.shingleunit = shingleunit;
            pos.add(position);
        }
        public void addPos(int position) {
            pos.add(position);
        }

        /**
         * Get position of appearances of this shingle unit.
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
                intArray[i] = it.next();
                ++i;
            }
            return intArray;
        }

        public int getCount() {
            return pos.size();
        }

        public boolean equals(ShingleUnitData sud) {
            return this.shingleunit.equals(sud.getShingleunit());
        }
        public boolean equals(ShingleUnit su) {
            return this.shingleunit.equals(su);
        }

        //TODO: to imple. Maintain the invariant: equal objects have same hash
        //code.
        public int hashCode(){
            return -1;
        }

        @Override
        public boolean equals(Object obj) {
            logger.severe("You probably should compare objects of wrong types!!!");
            return false;
        }

        /**
         * get the value of shingleunit
         * @return the value of shingleunit
         */
        public SU getShingleunit() {
            return this.shingleunit;
        }
        /**
         * set a new value to shingleunit
         * @param shingleunit the new value to be used
         */
        public void setShingleunit(SU shingleunit) {
            this.shingleunit=shingleunit;
        }
    }
}

