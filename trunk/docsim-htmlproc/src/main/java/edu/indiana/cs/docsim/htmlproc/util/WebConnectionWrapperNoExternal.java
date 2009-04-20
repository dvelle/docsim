package edu.indiana.cs.docsim.htmlproc.util;

import java.net.URL;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;
import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;
import java.util.logging.Logger;

public class WebConnectionWrapperNoExternal extends WebConnectionWrapper {
    private Logger logger =
        Logger.getLogger(WebConnectionWrapperNoExternal.class.getName());

    private String allowedURL ;
    public void setAllowedURL(String url) {
        allowedURL = url.trim();
    }
    public WebConnectionWrapperNoExternal(WebConnection arg0)
      throws IllegalArgumentException {
        super(arg0);
    }
    public WebConnectionWrapperNoExternal(WebClient arg0)
      throws IllegalArgumentException {
        super(arg0);
    }

    @Override
        public WebResponse getResponse(final WebRequestSettings webRequestSettings)
          throws IOException {
            final URL urlObject = webRequestSettings.getUrl();
            logger.info("url is :" + urlObject.toString());
            final String url = urlObject.toExternalForm();
            WebResponse webResponse;
            if (url.equalsIgnoreCase(allowedURL)) {
                // webResponse = new StringWebResponse("", null);
                // return webResponse;
                return null;
            } else {
                return super.getResponse(webRequestSettings);
            }

            // final URL urlObject = webRequestSettings.getURL();
            // final String url = urlObject.getPath();
            // WebResponse webResponse;
            // for(int i=0; i<PATTERNS_TO_SKIP.length; i++){
            //     if(url.endsWith(PATTERNS_TO_SKIP[i])){
            //         webResponse = new StringWebResponse("");
            //         return webResponse;
            //     }
            // }
            // return super.getResponse(webRequestSettings);
        }
}

