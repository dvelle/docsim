package edu.indiana.cs.docsim.htmlproc;

import java.io.File;
import java.io.FileOutputStream;

public class HtmlTagRemoverFilter extends DocFilterBase {
    private HtmlTagRemover tagRemover;

    public HtmlTagRemoverFilter() {
        tagRemover = new HtmlTagRemover();
    }

    public String filter(String doc) throws Exception {
        // This is a ugly workaournd. HtmlTagRemover does not accept a string
        // as input document. So we first write the string to a temp file and
        // then feed that file to HtmlTagRemover.
        File tmpfile = File.createTempFile("doc-preprocess", ".html");
        String charset = "UTF-8";
        FileOutputStream fos = new FileOutputStream(tmpfile);
        fos.write(doc.getBytes(charset));
        fos.close();

        // Following code of two lines does not work.
        // Because the underlying system treats the file as a resource. So it
        // would search for the file in the way that resources are searched
        // for.
        // String filename = tmpfile.getAbsolutePath();
        // String result = tagRemover.tagRemoveFromURL(filename);

        String result = tagRemover.tagRemoveFromURL(tmpfile.toURI().toURL());
        tmpfile.delete();

        return result;
    }
}
