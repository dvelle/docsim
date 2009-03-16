package edu.indiana.cs.docsim;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.logging.Logger;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

public class ShingleSet implements ShingleSetBase {
    Logger logger = Logger.getLogger(ShingleSet.class.getName());

    private List<Shingle> shingles = Lists.newArrayList();
    private List<ShingleData> shinglesUnique = Lists.newArrayList();

    public void addShingle(Shingle shingle, int pos) {
        Iterator<ShingleData> it = shinglesUnique.iterator();
        boolean found = false;
        while (it.hasNext()) {
            ShingleData dataEntry = it.next();
            if (dataEntry.equals(shingle)) {//found it
                dataEntry.addPos(pos);
                // logger.info("found it!!");
                found = true;
                break;
            }
        }
        if (!found) {
            ShingleData sd = new ShingleData(shingle, pos);
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

    //TODO: to implement
    public ShingleSet union(ShingleSetBase set) {
        return null;
    }

    //TODO: to implement
    public ShingleSet intersect(ShingleSetBase set) {
        return null;
    }

    //TODO: to implement
    public ShingleSet difference(ShingleSetBase set) {
        return null;
    }

    public List<Shingle> getShingleList() {
        return shingles;
    }

    public List<Shingle> getUniqueShingleList() {
        List<Shingle> list = Lists.newArrayList();
        Iterator<ShingleData> it = shinglesUnique.iterator();
        while (it.hasNext()) {
            ShingleData dataEntry = it.next();
            list.add(dataEntry.getShingle());
        }
        return list;
    }

    public int[] getPos(Shingle shingle) {
        ShingleData sd = getSD(shingle);
        if (sd == null) {
            return new int[0];
        } else {
            return sd.getPositions();
        }
    }

    private ShingleData getSD(Shingle shingle) {
        Iterator<ShingleData> it = shinglesUnique.iterator();
        while (it.hasNext()) {
            ShingleData dataEntry = it.next();
            if (dataEntry.equals(shingle)) {//found it
                return dataEntry;
            }
        }
        return null;
    }


    // public List<Shingle> getShingleListUnique() {
    // }

    class ShingleData {
        private Set<Integer> pos = Sets.newHashSet();
        private Shingle shingle;

        public ShingleData(Shingle shingle, int position) {
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

        public boolean equals(ShingleData sd) {
            return this.shingle.equals(sd.getShingle());
        }

        public boolean equals(Shingle shingle) {
            return this.shingle.equals(shingle);
        }
        public boolean equals(Object object) {
            if (object instanceof ShingleData) {
                return this.equals((ShingleData)object);
            } else if (object instanceof Shingle) {
                return this.equals((Shingle)object);
            } else {
                logger.severe("You probably should compare objects of wrong types!!!");
                return false;
            }
        }

        //TODO: to imple. Maintain the invariant: equal objects have same hash
        //code.
        public int hashCode(){
            return -1;
        }


        /**
         * get the value of shingleunit
         * @return the value of shingleunit
         */
        public Shingle getShingle() {
            return this.shingle;
        }
        /**
         * set a new value to shingleunit
         * @param shingleunit the new value to be used
         */
        public void setShingle(Shingle shingle) {
            this.shingle = shingle;
        }
    }
}

