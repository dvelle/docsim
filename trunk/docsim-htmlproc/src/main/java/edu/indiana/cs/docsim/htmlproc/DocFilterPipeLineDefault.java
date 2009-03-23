package edu.indiana.cs.docsim.htmlproc;

import edu.indiana.cs.docsim.htmlproc.stem.PaiceStemmerWrapper;
import java.net.URL;

public class DocFilterPipeLineDefault {
    protected DocFilterPipeLine pipeline = new DocFilterPipeLine();

    protected void init() throws Exception {
        pipeline.add(new HtmlTagRemoverFilter());
        pipeline.add(new PaiceStemmerWrapper());
    }

    public DocFilterPipeLineDefault() throws Exception{
        init();
    }

    public String filter(URL url) throws Exception{
        String charset = "UTF-8";
        return pipeline.filter(url, charset);
    }
    public String filter(String fileName, String charset) throws Exception{
        return pipeline.filter(fileName, charset);
    }
}

