package edu.indiana.cs.docsim.htmlproc.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class HttpUtil {
    private static Logger logger =
        Logger.getLogger(HttpUtil.class.getName());

    public static String fetchPageHTTP (URL url) {
        String page = null;
        if (url.getProtocol().equalsIgnoreCase("file")) {
            try {
                return FileUtils.readFileToString(FileUtils.toFile(url), "UTF-8");
            } catch(Exception ex) {
                logger.severe("get content from file '" + url + "' " + ex);
            }
        } else if (url.getProtocol().equalsIgnoreCase("http")) {
            HttpClient client = new HttpClient();
            GetMethod method = new GetMethod(url.toString());
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(1, false));
            method.getParams().setSoTimeout(1000 * 20);

            try {
                int statusCode = client.executeMethod(method);

                if (statusCode != HttpStatus.SC_OK) {
                    logger.severe("Method failed: " + method.getStatusLine());
                }

                // page = method.getResponseBodyAsString();
                InputStream is = method.getResponseBodyAsStream();
                page = IOUtils.toString(is, "UTF-8");

            } catch (HttpException e) {
                logger.severe("Fatal protocol violation: " + e);
            } catch (IOException e) {
                logger.severe("Fatal transport error: " + e);
            } finally {
                method.releaseConnection();
            }
        }
        return page;
    }

    public static File fetchPageHTTP2File(URL url) {
        try {
            String content = fetchPageHTTP(url);
            File file = File.createTempFile("", "html");
            FileUtils.writeStringToFile(file, content, "UTF-8");
            return file;
        } catch(Exception ex) {
            logger.severe("fetchPageHTTP:" + ex);
            return null;
        }
    }
}

