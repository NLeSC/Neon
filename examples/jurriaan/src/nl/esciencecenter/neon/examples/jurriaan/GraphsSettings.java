package nl.esciencecenter.neon.examples.jurriaan;

import nl.esciencecenter.neon.util.Settings;
import nl.esciencecenter.neon.util.TypedProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Example settings class implementation that reads a settings.properties file
 * from the classpath, and loads its values accordingly. This class uses the
 * Singleton design pattern found here:
 * 
 * http://en.wikipedia.org/wiki/Singleton_pattern
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GraphsSettings extends Settings {
    private final static Logger logger = LoggerFactory.getLogger(GraphsSettings.class);

    private static class SingletonHolder {
        public final static GraphsSettings instance = new GraphsSettings();
    }

    public static GraphsSettings getInstance() {
        return SingletonHolder.instance;
    }

    private String screenshotFileName;

    private GraphsSettings() {
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
