package edu.indiana.cs.docsim;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import edu.indiana.cs.docsim.data.PageRepository;
import edu.indiana.cs.docsim.data.util.ResourceLoader;

public class DocSimTrainingConfig {

    private static String defaultConfigFile = "res://training.config";
    private String configFile;
    private Config config;
    private PageRepository pageRepo;

    private void load() throws IOException, InvalidConfigFormatException, Exception {
        if (configFile != null) {
            Properties properties = new Properties();
            InputStream is = ResourceLoader.open(configFile);
            properties.load(is);
            config = new Config(properties);
            pageRepo = new PageRepository(getDataFile());
        }
    }

    private void init(boolean useDefault, String fileName) {
        if (useDefault) {
            configFile = defaultConfigFile;
        } else if (fileName != null) {
            configFile = fileName;
        } else {
            configFile = defaultConfigFile;
        }
    }

    public DocSimTrainingConfig(boolean useDefault, String fileName)
      throws IOException, InvalidConfigFormatException, Exception {
        init(useDefault, fileName);
        load();
    }

    public String getDataFile() {
        if (config != null) {
            return config.getDataFile();
        } else {
            return null;
        }
    }

    /**
     * get the value of pageRepo
     * @return the value of pageRepo
     */
    public PageRepository getPageRepo(){
        return this.pageRepo;
    }
    /**
     * set a new value to pageRepo
     * @param pageRepo the new value to be used
     */
    public void setPageRepo(PageRepository pageRepo) {
        this.pageRepo=pageRepo;
    }
    /**
     * get the value of config
     * @return the value of config
     */
    public Config getConfig(){
        return this.config;
    }
    /**
     * set a new value to config
     * @param config the new value to be used
     */
    public void setConfig(Config config) {
        this.config=config;
    }
}

class Config {
    private static Logger logger =
        Logger.getLogger(Config.class.getName());

    private static String keyDataFile           = "datafile";
    private static String keyShingle            = "shingle-method";
    private static String keyShingleSizeRange   = "size-range";
    private static String keyStopWord           = "stopword";
    private static String keyStemmer            = "stemmer";
    private static String keyPossessive         = "possessive";
    private static String keyTagRemoval         = "tag-removal";
    private static String keyFilters            = "filters";
    private static String filterSep             = ",";
    private static String sizeRangeSep          = ",";

    private String dataFile;
    private String shingleAlgRaw;
    // private ShingleAlgorithm shingleAlg;
    private String stopword;
    private String stemmer;
    private String filters;
    private String possessive;
    private String tagRemoval;
    private List<String> filterLs = new ArrayList<String>();

    private int sizeStart;
    private int sizeStep;
    private int sizeStop;

    private List<ShingleAlgorithm> shingleAlgLs =
        new ArrayList<ShingleAlgorithm>();

    /**
     * get the value of shingleAlgLs
     * @return the value of shingleAlgLs
     */
    public List<ShingleAlgorithm> getShingleAlgLs(){
        return this.shingleAlgLs;
    }
    /**
     * set a new value to shingleAlgLs
     * @param shingleAlgLs the new value to be used
     */
    public void setShingleAlgLs(List<ShingleAlgorithm> shingleAlgLs) {
        this.shingleAlgLs=shingleAlgLs;
    }
    /**
     * get the value of tagRemoval
     * @return the value of tagRemoval
     */
    public String getTagRemoval(){
        return this.tagRemoval;
    }
    /**
     * set a new value to tagRemoval
     * @param tagRemoval the new value to be used
     */
    public void setTagRemoval(String tagRemoval) {
        this.tagRemoval=tagRemoval;
    }

    /**
     * get the value of sizeStart
     * @return the value of sizeStart
     */
    public int getSizeStart(){
        return this.sizeStart;
    }
    /**
     * set a new value to sizeStart
     * @param sizeStart the new value to be used
     */
    public void setSizeStart(int sizeStart) {
        this.sizeStart=sizeStart;
    }
    /**
     * get the value of sizeStep
     * @return the value of sizeStep
     */
    public int getSizeStep(){
        return this.sizeStep;
    }
    /**
     * set a new value to sizeStep
     * @param sizeStep the new value to be used
     */
    public void setSizeStep(int sizeStep) {
        this.sizeStep=sizeStep;
    }
    /**
     * get the value of sizeStop
     * @return the value of sizeStop
     */
    public int getSizeStop(){
        return this.sizeStop;
    }
    /**
     * set a new value to sizeStop
     * @param sizeStop the new value to be used
     */
    public void setSizeStop(int sizeStop) {
        this.sizeStop=sizeStop;
    }

    /**
     * get the value of filterLs
     * @return the value of filterLs
     */
    public List<String> getFilterLs(){
        return this.filterLs;
    }
    /**
     * set a new value to filterLs
     * @param filterLs the new value to be used
     */
    public void setFilterLs(List<String> filterLs) {
        this.filterLs=filterLs;
    }
    /**
     * get the value of stopword
     * @return the value of stopword
     */
    public String getStopword(){
        return this.stopword;
    }
    /**
     * set a new value to stopword
     * @param stopword the new value to be used
     */
    public void setStopword(String stopword) {
        this.stopword=stopword;
    }
    /**
     * get the value of stemmer
     * @return the value of stemmer
     */
    public String getStemmer(){
        return this.stemmer;
    }
    /**
     * set a new value to stemmer
     * @param stemmer the new value to be used
     */
    public void setStemmer(String stemmer) {
        this.stemmer=stemmer;
    }
    /**
     * get the value of filters
     * @return the value of filters
     */
    public String getFilters(){
        return this.filters;
    }
    /**
     * set a new value to filters
     * @param filters the new value to be used
     */
    public void setFilters(String filters) {
        this.filters=filters;
    }

    /**
     * get the value of shingleAlgRaw
     * @return the value of shingleAlgRaw
     */
    public String getShingleAlgRaw(){
        return this.shingleAlgRaw;
    }
    /**
     * set a new value to shingleAlgRaw
     * @param shingleAlgRaw the new value to be used
     */
    public void setShingleAlgRaw(String shingleAlgRaw) {
        this.shingleAlgRaw=shingleAlgRaw;
    }
    /**
     * get the value of dataFile
     * @return the value of dataFile
     */
    public String getDataFile(){
        return this.dataFile;
    }
    /**
     * set a new value to dataFile
     * @param dataFile the new value to be used
     */
    public void setDataFile(String dataFile) {
        this.dataFile=dataFile;
    }

    public Config(Properties properties)
      throws InvalidConfigFormatException {
        dataFile = properties.getProperty(keyDataFile);
        stopword = properties.getProperty(keyStopWord);
        stemmer = properties.getProperty(keyStemmer);
        filters = properties.getProperty(keyFilters);
        if (filters != null) {
            String[] filterArr = filters.split(filterSep);
            for (int i = 0 ; i < filterArr.length ; ++i) {
                String filter = filterArr[i];
                filter = filter.trim();
                if (filter.length() == 0) continue;
                this.getFilterLs().add(filter);
            }
        }
        shingleAlgRaw = properties.getProperty(keyShingle);
        if (shingleAlgRaw != null) {
            String[] algs = shingleAlgRaw.split(filterSep);
            for (int i = 0 ; i < algs.length ; ++i) {
                String alg = algs[i];
                alg = alg.trim();
                if (alg.length() == 0) continue;
                ShingleAlgorithm shingleAlg =
                    ShingleAlgorithm.getValue(alg);
                if (shingleAlg == null) continue;
                this.getShingleAlgLs().add(shingleAlg);
            }
        }
        String sizeRange = properties.getProperty(keyShingleSizeRange);
        String[] comps = sizeRange.split(sizeRangeSep);
        if (comps.length != 3)
            throw new InvalidConfigFormatException(errorTextInvalidSizeRange);
        try {
            sizeStart = Integer.valueOf(comps[0].trim());
            sizeStep  = Integer.valueOf(comps[1].trim());
            sizeStop  = Integer.valueOf(comps[2].trim());
        } catch(Exception ex) {
            throw new InvalidConfigFormatException(errorTextInvalidSizeRange);
        }
        possessive = properties.getProperty(keyPossessive);
        tagRemoval = properties.getProperty(keyTagRemoval);
    }

    /**
     * get the value of possessive
     * @return the value of possessive
     */
    public String getPossessive(){
        return this.possessive;
    }
    /**
     * set a new value to possessive
     * @param possessive the new value to be used
     */
    public void setPossessive(String possessive) {
        this.possessive=possessive;
    }

    public boolean includeFilter(String filter) {
        Iterator<String> it = filterLs.iterator();
        while (it.hasNext()) {
            String fname = it.next();
            if (fname.equalsIgnoreCase(filter)) {
                return true;
            }
        }
        return false;
    }

    private static String errorTextInvalidSizeRange
        = "invalid size range configuration";
}

