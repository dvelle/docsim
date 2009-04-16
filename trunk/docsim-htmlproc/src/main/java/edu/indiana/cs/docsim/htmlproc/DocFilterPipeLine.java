package edu.indiana.cs.docsim.htmlproc;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import edu.indiana.cs.docsim.htmlproc.util.ResourceLoader;
import java.io.InputStream;

public class DocFilterPipeLine extends DocFilterBase {

    private static Logger logger =
        Logger.getLogger(DocFilterPipeLine.class.getName());

    private List<DocFilter> filters = new ArrayList<DocFilter>();

    public void add(DocFilter filter) throws Exception {
        filters.add(filter);
    }

    public void clear() {
        filters.clear();
    }

    public void addHead(DocFilter filter) throws Exception {
        if (filters.size() == 0) {
            filters.add(filter);
        } else {
            filters.add(0, filter);
        }
    }

    public DocFilter remove(int i) throws Exception {
        return filters.remove(i);
    }

    public boolean remove(DocFilter filter) throws Exception {
        return filters.remove(filter);
    }

    public void swap(int i, int j) throws Exception {
        // TODO: figure shallow/deep copy.
        // In each element in List, it's a reference to the real object? or it
        // is actually the real object? If it's the latter case, following
        // code would have problems.
        DocFilter filter1 = filters.get(i);
        DocFilter filter2 = filters.get(j);
        filters.set(i, filter2);
        filters.set(j, filter1);
    }

    public void swap(DocFilter filter1, DocFilter filter2) throws Exception {
        int idx1 = -1 , idx2 = -1;
        for (int i = 0 ; i < filters.size() ; ++i) {
            if (filters.get(i) == filter1) {
                idx1 = i;
            }
            if (filters.get(i) == filter2) {
                idx2 = i;
            }
        }

        if (idx1 == -1 || idx2 == -1 || idx1 == idx2) {
            return;
        } else {
            swap(idx1, idx2);
        }
    }

    /**
     * Filter a document.
     * Note: the passed-in <code>doc</code> is not changed.
     *
     * @param doc Data/content to be filtered.
     * @return resultant string
     */
    @Override
    public String filter(String doc) throws Exception{
        String result = doc;
        for (Iterator<DocFilter> it = filters.iterator(); it.hasNext(); ) {
            DocFilter filter = it.next();
            result = filter.filter(result);
        }
        return result;
    }

    /**
     * Read data from a file.
     *
     * @param fileName name of the file where data is stored.
     *  If it starts with <i>res://</i>, it would be treated as a resource.
     *  Else it is treated just as a regular file.
     *
     * @param charset charset of the file
     * @return resultant string.
     */
    public String filter(String fileName, String charset) throws Exception{
        InputStream is = ResourceLoader.open(fileName);
        return filter(is, charset);
    }

    /**
     * Read data from a file and filter the data.
     *
     * @param file where the data is stored.
     * @param charset charset of data in the file.
     * @return resultant string
     * @throws Exception
     */
    public String filter(File file, String charset) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        return filter(fis, charset);
    }

    /**
     * Get the data from specified url.
     * For HTTP url, type of the retrieved page must be text/html or text/plain.
     *
     * @param url  from where the data is retrieved.
     *  Currently, protocols http and file are supported.
     * @param charset charset of the data
     * @return resultant data
     * @throws Exception
     */
    public String filter(URL url, String charset) throws Exception {
        String protocol = url.getProtocol();
        if (protocol.compareToIgnoreCase("http") == 0) {
            String page = fetchPageHTTP(url);
            if (page != null) {
                return filter(page);
            } else {
                throw new Exception("could not get page from '" +
                        url + "'");
            }
        } else if (protocol.compareToIgnoreCase("file") == 0) {
            String fileName = url.getFile();
            return filter(new File(fileName), charset);
        } else {
            throw new Exception("unsupported protocol type");
        }
    }

    private String fetchPageHTTP (URL url) {

        String page = null;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url.toString());
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        try {
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                logger.severe("Method failed: " + method.getStatusLine());
            }

            page = method.getResponseBodyAsString();

        } catch (HttpException e) {
            logger.severe("Fatal protocol violation: " + e);
        } catch (IOException e) {
            logger.severe("Fatal transport error: " + e);
        } finally {
            method.releaseConnection();
        }
        return page;
    }

    @Override
    public void setControlAttribute(String key, String value) {
    }
}

