package nl.esciencecenter.esight.examples;

import nl.esciencecenter.esight.util.Settings;
import nl.esciencecenter.esight.util.TypedProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESightExampleSettings extends Settings {
    private final static Logger logger = LoggerFactory
            .getLogger(ESightExampleSettings.class);

    private static class SingletonHolder {
        public final static ESightExampleSettings instance = new ESightExampleSettings();
    }

    public static ESightExampleSettings getInstance() {
        return SingletonHolder.instance;
    }

    private String screenshotFileName;

    private ESightExampleSettings() {
        super();

        try {
            final TypedProperties props = new TypedProperties();
            props.loadFromClassPath("settings.properties");

            screenshotFileName = props.getProperty("SCREENSHOT_FILENAME");
        } catch (NumberFormatException e) {
            logger.debug(e.getMessage());
        }
    }

    /**
     * Getter for screenshotFileName.
     * 
     * @return the screenshotFileName.
     */
    public String getScreenshotFileName() {
        return screenshotFileName;
    }

    /**
     * Setter for screenshotFileName.
     * 
     * @param var
     *            the screenshotFileName to set
     */
    public void setScreenshotFileName(String var) {
        screenshotFileName = var;
    }

}
