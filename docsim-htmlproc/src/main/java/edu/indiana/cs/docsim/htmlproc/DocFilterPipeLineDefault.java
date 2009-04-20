package edu.indiana.cs.docsim.htmlproc;

import java.net.URL;

import edu.indiana.cs.docsim.htmlproc.stem.PaiceStemmerWrapper;
import edu.indiana.cs.docsim.htmlproc.stopword.PossessiveFilter;
import edu.indiana.cs.docsim.htmlproc.stopword.StopWordFilter;
import java.io.File;

/**
 * Default document filter pipe line.
 * Currently it includes
 *   (*) a html tag remover
 *   (*) a possessive remover
 *   (*) stop word remover
 *   (*) a stemmer
 *
 * If this configuration does not satisfy your requirement, you could create a
 * subclass and override method <code>init</code>.
 *
 * @author
 * @version
 */
public class DocFilterPipeLineDefault {
    protected DocFilterPipeLine pipeline = new DocFilterPipeLine();
    // protected HtmlTagListenerFilter  listenerFilter;

    protected void init() throws Exception {
        // DomTagListenerRegistry listenerReg = new DomTagListenerRegistry();
        // HtmlTagListenerFilterlistenerFilter = new HtmlTagListenerFilter(listenerReg);
        // pipeline.addHead(listenerFilter);

        beforeInit();
        pipeline.add(new LowerCaseFilter());
        pipeline.add(new HtmlTagRemoverFilter());
        pipeline.add(new PossessiveFilter());
        pipeline.add(new StopWordFilter(true));
        pipeline.add(new PaiceStemmerWrapper());
        afterInit();
    }

    protected void beforeInit() { }
    protected void afterInit() { }

    public void clear() {
        pipeline.clear();
    }

    // public void registerListener(DomTagListener listener) {
    //     listenerFilter.registerListener(listener);
    // }

    public DocFilterPipeLineDefault() throws Exception{
        init();
    }

    public String filter(String content) throws Exception {
        return pipeline.filter(content);
    }

    public String filter(URL url) throws Exception{
        String charset = "UTF-8";
        return pipeline.filter(url, charset);
    }

    public String filter(String fileName, String charset) throws Exception{
        return pipeline.filter(fileName, charset);
    }
    public String filter(File file, String charset) throws Exception{
        return pipeline.filter(file, charset);
    }
}

