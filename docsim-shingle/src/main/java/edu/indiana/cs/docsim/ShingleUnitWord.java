package edu.indiana.cs.docsim;

import com.google.common.base.Objects;

/**
 * This class represents a shingle unit in unit of word.
 * In other words, a shingle unit is a word.
 *
 * @author
 * @version
 */
public class ShingleUnitWord extends ShingleUnit {
    private String word = "";

    public ShingleUnitWord(String str) {
        this.update(str);
    }

    @Override
    public String value() {
        return word;
    }

    @Override
    public void update(String str) {
        this.word = str;
    }

    @Override
    public int length() {
        if( word == null ) return 0;
        return word.length();
    }

    public boolean equals(ShingleUnit another) {
        if (another.getClass() == ShingleUnitWord.class) {
            return this.equals((ShingleUnitWord)another);
        }
        return false;
    }

    public boolean equals(ShingleUnitWord another) {
        if(this.word.equals(another.value()))
            return true;
        return false;
    }

    public boolean equals(Object object) {
        if (object.getClass() == ShingleUnitWord.class) {
        // if (object instanceof ShingleUnit) {
            return this.equals((ShingleUnitWord)object);
        }
        return false;
    }
    public int hashCode() {
        // return this.word.hashCode();
        return Objects.hashCode(value());
    }

    // FIXME: how to calculate distance of two shingle units in a meaning way
    // In current implementation, the distance is 1 if the two shingle units
    // are the same, and 0 if the two shingle units are different.
    public Double distance(ShingleUnit another) {
        if (this.equals(another)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}

