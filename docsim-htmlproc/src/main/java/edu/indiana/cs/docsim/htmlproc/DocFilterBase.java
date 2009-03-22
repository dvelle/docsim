package edu.indiana.cs.docsim.htmlproc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public abstract class DocFilterBase implements DocFilter {
    private static Logger logger =
        Logger.getLogger(DocFilterBase.class.getName());

    // This is the main method. All other functions finally invoke this
    // method. So the subclasses must implement this method.
    //
    // String  filter(String doc) throws Exception

    public void    filter(String doc, OutputStream os, String charset)
      throws Exception{
        String result = filter(doc);
        os.write(result.getBytes(charset) );
    }
    public void    filter(String doc, Writer writer)
      throws Exception{
        writer.write(filter(doc));
    }

    public String  filter(InputStream is, String charset)
      throws Exception{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, charset));
        return filter(reader);
    }
    public void    filter(InputStream is, String charset, OutputStream os)
      throws Exception{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, charset));
        filter(reader, os, charset);
    }

    public void    filter(InputStream is, String charset, Writer writer)
      throws Exception{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, charset));
        filter(reader, writer);
    }

    public String  filter(Reader reader)
      throws Exception{
        String doc = IOUtils.toString(reader);
        return filter(doc);
    }

    public void    filter(Reader reader, OutputStream os, String charset)
      throws Exception{
        String doc = IOUtils.toString(reader);
        filter(doc, os, charset);
    }

    public void    filter(Reader reader, Writer writer)
      throws Exception{
        String doc = IOUtils.toString(reader);
        filter(doc, writer);
    }

    public String  filter(byte[] doc, String charset)
      throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(doc, 0, doc.length);
        return filter(bais, charset);
    }
    public void    filter(byte[] doc, String charset, OutputStream os)
      throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(doc, 0, doc.length);
        filter(bais, charset, os);
    }
    public void    filter(byte[] doc, String charset, Writer writer)
      throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(doc, 0, doc.length);
        filter(bais, charset, writer);
    }

    public void    setControlAttribute(String key, String value) {
        logger.warning("Maybe you should override method "
                + DocFilterBase.class.getName() + "#setControlAttribute");
    }
}

