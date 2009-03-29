package edu.indiana.cs.docsim;

import java.util.List;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestUtil
  extends TestCase {
    protected static String dumpShingleSet(ShingleSet shingleset) {
        // List<Shingle> shingles = shingleset.getShingleList();
        List<Shingle> shingles = shingleset.getUniqueShingleList();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Iterator<Shingle> it = shingles.iterator() ; it.hasNext();) {
            Shingle shingle = it.next();
            if (!first) {
                sb.append("\n");
            } else {
                first = false;
            }
            sb.append("shingle:\n");
            sb.append("\t" + dumpShingle(shingle));
            int[] pos = shingleset.getPos(shingle);
            sb.append("\n\tpos:" + serIntArray(pos));
        }
        return sb.toString();
    }
    protected static String dumpShingle(Shingle shingle) {
        // ShingleUnitBag sub = shingle.getSuMgr();
        ShingleUnitMgr sub = shingle.getSuMgr();
        List<ShingleUnit> sulist = sub.getUniqueShingleUnits();
        Iterator<ShingleUnit> it = sulist.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ShingleUnit su = it.next();
            int[] pos = sub.getPos(su);
            sb.append(su.value());
            sb.append(":" + serIntArray(pos) + "; ");
        }
        return sb.toString();
    }
    protected static String serIntArray(int[] array){
        StringBuilder sb = new StringBuilder();
        if (array == null) {
            return "";
        } else {
            for (int i = 0 ; i < array.length ; ++i) {
                if (i == array.length-1) {
                    sb.append(array[i]);
                } else {
                    sb.append(array[i]+",");
                }
            }
        }
        return sb.toString();
    }
    protected static String dumpWordShingle(WordShingle shingle) {
        ShingleUnitMgr bag = shingle.getSuMgr();
        List<ShingleUnit> shingleunits = bag.getUniqueShingleUnits();
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < shingleunits.size() ; ++i) {
            ShingleUnit su = shingleunits.get(i);
            int[] pos = bag.getPos(su);
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(su.value() + serIntArray(pos));
        }
        return sb.toString();
    }
    public void testDummy()  {
    }
}

