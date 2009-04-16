package edu.indiana.cs.docsim.htmlproc.stem;

import java.util.logging.Logger;

import edu.indiana.cs.docsim.htmlproc.DocFilterBase;
import edu.indiana.cs.docsim.htmlproc.util.ResourceLoader;

public class PaiceStemmerWrapper extends DocFilterBase {
    private static Logger logger =
        Logger.getLogger(PaiceStemmerWrapper.class.getName());

    private static String identifier = "paice";
    private static String defaultRuleFile   = "res://stemrules.txt";
    private static String defaultPrefixStrip = "/p";

    // The flag that indicates prefix strip is on
    private static String prefixStripOnFlag = "/p";

    private static String ruleFileKey = "rulefile";
    private static String prefixStripKey = "prefixstrip";

    private String  ruleFile;
    private boolean prefixStrip;
    private String  strPrefixStrip;

    {
        try {
            this.setRuleFile(defaultRuleFile);
            this.setStrPrefixStrip(defaultPrefixStrip);
        } catch(Exception ex) {
            logger.severe("Failed to initialize PaiceStemmerWrapper." + ex);
            ex.printStackTrace();
        }
    }

    // private PorterStemmer stemmer = new PorterStemmer();

    public String filter(String doc) throws Exception {
        PaiceStemmer stemmer = new PaiceStemmer(ruleFile, strPrefixStrip);
        String[] array = doc.split("\\s");
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < array.length ; ++i) {
            String str = array[i].trim();
            if (str.length() == 0)
                continue;
            sb.append(stemmer.stripAffixes(str));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * Set control attributes.
     *       _____________________________________________________
     *      |     key     |             value                     |
     *      |-------------|---------------------------------------|
     *      | prefixstrip | true/1: execute prefix strip.         |
     *      |             | false/0: don't execute prefix strip   |
     *      |-------------|---------------------------------------|
     *      | rulefile    | The input file.                       |
     *      |_____________|_______________________________________|
     *
     * @param key
     * @param value
     */
    public void setControlAttribute(String key, String value) {
        if (key.compareToIgnoreCase(ruleFileKey) == 0) {
            try {
                setRuleFile(value);
            }
            catch(Exception ex) {
                logger.severe("Make sure the value \"" + value + "\" is correct");
            }
        } else if (key.compareToIgnoreCase(prefixStripKey) ==0) {
            setPrefixStrip(value);
        } else {
            logger.warning("The key \"" + key + "\" is not defined for filter " +
                    PaiceStemmerWrapper.class.getName());
        }
    }

    private void setPrefixStrip(String flag) {
        if (flag.compareToIgnoreCase(prefixStripOnFlag) == 0) {
            this.prefixStrip = true;
        } else {
            this.prefixStrip = false;
        }
        this.strPrefixStrip = flag;
    }

    /**
     * get the value of ruleFile
     * @return the value of ruleFile
     */
    public String getRuleFile(){
        return this.ruleFile;
    }
    /**
     * set a new value to ruleFile.
     *
     * @param ruleFile the new value to be used
     */
    public void setRuleFile(String ruleFile) throws Exception{
        String absPath = ResourceLoader.getAbsolutePath(ruleFile);
        this.ruleFile=absPath;
    }
    /**
     * get the value of strPrefixStrip
     * @return the value of strPrefixStrip
     */
    public String getStrPrefixStrip(){
        return this.strPrefixStrip;
    }
    /**
     * set a new value to strPrefixStrip
     * @param strPrefixStrip the new value to be used
     */
    public void setStrPrefixStrip(String strPrefixStrip) {
        this.strPrefixStrip=strPrefixStrip;
        if (prefixStripOnFlag.compareToIgnoreCase(strPrefixStrip) == 0) {
            prefixStrip = true;
        } else {
            prefixStrip = false;
        }
    }
}

