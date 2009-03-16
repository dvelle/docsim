package edu.indiana.cs.docsim;

public class ShingleUnitWord extends ShingleUnit {
    private String word = "";
    public ShingleUnitWord(String str) {
        this.update(str);
    }

    public String value() {
        return word;
    }
    // public void valueOf(String str) {
    //     this.word = str;
    // }
    public void update(String str) {
        this.word = str;
    }

    public int length() {
        if( word == null ) return 0;
        return word.length();
    }
    public boolean equals(ShingleUnit another) {
        if(this.word.equals(another.value()))
            return true;
        return false;
    }
    public boolean equals(Object object) {
        if (object instanceof ShingleUnit) {
            return this.equals((ShingleUnit)object);
        }
        return false;
    }
    public int hashCode() {
        return this.word.hashCode();
    }

    // FIXME: how to calculate distance of two shingle units in a meaning way
    // In current implementation, the distance is 1 if the two shingle units
    // are the same, and 0 if the two shingle units are different.
    public double distance(ShingleUnit another) {
        if (this.equals(another)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}

