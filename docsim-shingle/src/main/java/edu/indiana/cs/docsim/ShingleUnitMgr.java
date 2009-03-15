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
    // SU[] getUniqueShingleUnits();
    List<SU> getUniqueShingleUnits();
    // ShingleUnit[] getUniqueShingleUnits();

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
     * Remove the specified shingle unit.
     * All related information would be deleted.
     *
     * @param shingleunit
     */
    void removeShingleUnit(SU shingleunit);
}
