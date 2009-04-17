package edu.indiana.cs.docsim;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

/**
 * formatter.
 *
 * @author
 * @version
 */
public abstract class StatisticsFormatter {
    public static Logger logger =
        Logger.getLogger(StatisticsFormatter.class.getName());

    public abstract String format(DocSimLatticeStatistics statistics);

    public boolean format2File(DocSimLatticeStatistics statistics,
            String fileName, boolean append) {
        try {
            String result = format(statistics);
            FileOutputStream fos  = new FileOutputStream(fileName, append);
            IOUtils.write(result, fos, "UTF-8");
            IOUtils.closeQuietly(fos);
            return true;
        } catch(Exception ex) {
            logger.severe(ex.toString());
            return false;
        }
    }
}

