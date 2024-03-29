package edu.indiana.cs.docsim;

import java.util.List;

/**
 * This interface provides functions for managing a sequence of shingle units.
 *
 * @author
 * @version
 */
public interface ShingleUnitMgr <SU extends ShingleUnit> {
    /**
     * get number of shingle units.
     *
     * @return
     */
    int size();

    /**
     * get number of unique shingle units.
     *
     * @return
     */
    int sizeUnique();

    /**
     * get the shingle at the specific position.
     *
     * @param index
     * @return
     */
    // ShingleUnit getByIndex(int index);
    SU getByIndex(int index);


    /**
     * Get all unique shingle units.
     * Duplicate shingle units just appear once in the returned array.
     *
     * @return
     */
    List<SU> getUniqueShingleUnits();

    /**
     * Get ShingleUnitData of all shingle units.
     *
     * @return
     */
    List<ShingleUnitData<SU>> getShingleUnitsData();

    /**
     * Get all shingle units.
     * Duplicate shingle units are not suppressed.
     *
     * @return
     */
    List<SU> getShingleUnits();

    /**
     * Get number of appearances of a shingle unit.
     *
     * @param shingleunit
     * @return
     */
    int getCount(SU shingleunit);
    // int getCount(ShingleUnit shingleunit);

    /**
     * Get positions of a shingle unit.
     *
     * @param shingleunit
     * @return
     */
    int[] getPos(SU shingleunit);
    // int[] getPos(ShingleUnit shingleunit);

    /**
     * Parse a string and set internal states.
     *
     * @param str
     */
    // void parse(String str);

    /**
     * Add a shingle unit to this manager.
     *
     * @param shingleunit the shingle unit to be added
     * @param position    position of appearance
     */
    void addShingleUnit(SU shingleunit, int position);
    // void addShingleUnit(ShingleUnit shingleunit, int position);

    /**
     * Add a shingle unit built from passed-in <code>str</code> to this
     * manager.
     *
     * @param str
     * @param pos
     */
    void addShingleUnit(String str, int pos);

    /**
     * Remove the specified shingle unit.
     * All related information would be deleted.
     *
     * @param shingleunit
     */
    void removeShingleUnit(SU shingleunit);
}

