package edu.indiana.cs.docsim;

/** 
 * Tokenize a shingle. 
 * 
 * @author 
 * @version 
 */
interface ShingleTokenizer{
    public boolean hasMoreTokens();
    public String  nextToken();
}
