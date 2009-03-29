package edu.indiana.cs.docsim;

import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Lists;

/**
 * Represents a shingle unit bag.
 *
 * @author
 * @version
 */
public abstract class ShingleUnitBag <SU extends ShingleUnit>
  implements ShingleUnitMgr<SU> {
    private static Logger logger =
        Logger.getLogger(ShingleUnitBag.class.getName());

    // private String rawData;

    // All shingle units are stored in this variable. The order is the same as
    // the order in which those shingle units are inserted.
    private List<SU> shingleUnitList = Lists.newArrayList();

    // Just store the unique shingle units. It means duplicate shingle units
    // are stored as one element. But the cardinality is still preserved.
    private List<ShingleUnitData<SU>> shingleUnitListUnique = Lists.newArrayList();

    // public abstract void addShingleUnit(String str, int pos);

    public List<ShingleUnitData<SU>> getShingleUnitsData () {
        return this.shingleUnitListUnique;
    }

    /**
     * Add a shingle unit to this manager.
     *
     * @param shingleunit the shingle unit to be added
     * @param pos         position of appearance of the shingle unit
     */
    public void addShingleUnit(SU shingleunit, int pos) {
        Iterator<ShingleUnitData<SU>> it = shingleUnitListUnique.iterator();
        boolean found = false;
        while (it.hasNext()) {
            ShingleUnitData<SU> dataEntry = it.next();
            if (dataEntry.equals(shingleunit)) {//found it
                dataEntry.addPos(pos);
                found = true;
                break;
            }
        }
        if (!found) {
            ShingleUnitData<SU> sud = new ShingleUnitData<SU>(shingleunit, pos);
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

    // TODO: How to define equility for two shingle unit bags in a meaningful
    // way.
    public boolean equals(ShingleUnitBag<SU> another) {
        List<SU> sub2 = another.getShingleUnits();
        return this == another;
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
        Iterator<ShingleUnitData<SU>> it = shingleUnitListUnique.iterator();
        while (it.hasNext()) {
            ShingleUnitData<SU> dataEntry = it.next();
            list.add(dataEntry.getShingleunit());
        }
        return list;
    }

    public List<SU> getShingleUnits() {
        return shingleUnitList;
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

    public ShingleUnitData getSUD(SU shingleunit) {
        Iterator<ShingleUnitData<SU>> it = shingleUnitListUnique.iterator();
        while (it.hasNext()) {
            ShingleUnitData<SU> dataEntry = it.next();
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
        while(shingleUnitList.remove(shingleunit)){}
        while(shingleUnitListUnique.remove(new ShingleUnitData<SU>(shingleunit, -1))){}
    }
}

