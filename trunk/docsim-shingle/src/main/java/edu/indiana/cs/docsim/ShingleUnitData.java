package edu.indiana.cs.docsim;

import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;

/**
 * Represents a shingle unti and its positions.
 * It may appear in multple places.
 *
 */
public class ShingleUnitData <SU extends ShingleUnit> {
    private Logger logger =
        Logger.getLogger(ShingleUnitData.class.getName());

    private Map<String, Object> properties =
        new HashMap<String, Object>();

    public Object getProperty(String name) {
        return properties.get(name);
    }
    public void setProperty(String name, Object value){
        properties.put(name, value);
    }

    private Set<Integer> pos = Sets.newHashSet();
    // private ShingleUnit shingleunit;
    private SU shingleunit;

    // public ShingleUnitData(ShingleUnit shingleunit, int position) {
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

    public boolean equals(ShingleUnitData<SU> sud) {
        return this.shingleunit.equals(sud.getShingleunit());
    }
    public boolean equals(ShingleUnit su) {
        return this.shingleunit.equals(su);
    }

    public int hashCode(){
        return Objects.hashCode(getShingleunit());
    }

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() == obj.getClass()) {
            return this.equals((ShingleUnitData<SU>)obj);
        } else if (obj instanceof ShingleUnit) {
            return this.equals((ShingleUnit)obj);
        }
        logger.severe("You probably compare objects of wrong types!!!");
        return false;
    }

    /**
     * get the value of shingleunit
     * @return the value of shingleunit
     */
    // public ShingleUnit getShingleunit() {
    public SU getShingleunit() {
        return this.shingleunit;
    }

    /**
     * set a new value to shingleunit
     * @param shingleunit the new value to be used
     */
    // public void setShingleunit(ShingleUnit shingleunit) {
    public void setShingleunit(SU shingleunit) {
        this.shingleunit=shingleunit;
    }
}

