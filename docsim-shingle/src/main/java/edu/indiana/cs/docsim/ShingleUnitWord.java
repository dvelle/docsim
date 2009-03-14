package edu.indiana.cs.docsim;

class ShingleUnitWord extends ShingleUnit{
    private String word = "";
    public String value(){
        return word;
    }
    public void valueOf(String str){
        this.word = str;
    }

    public int length(){
        if( word == null ) return 0;
        return word.length();
    }
    public boolean equals(ShingleUnit another){
        if(this.word.equals(another.value()))
            return true;
        return false;
    }
    public double distance(ShingleUnit another){
        return 10.0;
    }
}

