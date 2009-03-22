package edu.indiana.cs.docsim.htmlproc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

interface DocFilter {

    /**
     * Filter a string.
     * The passed-in string is not changed.
     *
     * @param doc
     * @return
     * @throws Exception
     */
    String  filter(String doc) throws Exception;
    void    filter(String doc, OutputStream os, String charset) throws Exception;
    void    filter(String doc, Writer writer) throws Exception;

    String  filter(InputStream is, String charset) throws Exception;
    void    filter(InputStream is, String charset, OutputStream os) throws Exception;
    void    filter(InputStream is, String charset, Writer writer) throws Exception;

    String  filter(Reader reader) throws Exception;
    void    filter(Reader reader, OutputStream os, String charset) throws Exception;
    void    filter(Reader reader, Writer writer) throws Exception;

    String  filter(byte[] doc, String charset) throws Exception;
    void    filter(byte[] doc, String charset, OutputStream os) throws Exception;
    void    filter(byte[] doc, String charset, Writer writer) throws Exception;

    /**
     * This method sets attributes that control filtering functionality.
     * So each filter may have different attributes.
     * The key SHOULD be case-insensitive.
     *
     * @param key
     * @param value
     */
    void    setControlAttribute(String key, String value);
}

