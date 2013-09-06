package nl.esciencecenter.esight.examples.colormap;

import java.util.HashMap;

import nl.esciencecenter.esight.swing.ColormapInterpreter;
import nl.esciencecenter.esight.util.Settings;
import nl.esciencecenter.esight.util.TypedProperties;

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
public class ColormapExampleSettings extends Settings {
    private final static Logger logger = LoggerFactory.getLogger(ColormapExampleSettings.class);

    private static class SingletonHolder {
        public final static ColormapExampleSettings instance = new ColormapExampleSettings();
    }

    public static ColormapExampleSettings getInstance() {
        return SingletonHolder.instance;
    }

    private String screenshotFileName;

    private final String[] dataModes;
    private int selectedDataModeIndex = 0;

    private final String[] variables;
    private int variableIndex = 0;

    private final String[] colormaps;
    private int selectedColormapIndex;

    private int rangeSliderLowerValue = 10;
    private int rangeSliderUpperValue = 70;

    private final HashMap<String, Float> variableLowerBounds;
    private final HashMap<String, Float> variableUpperBounds;

    private ColormapExampleSettings() {
        super();

        try {
            final TypedProperties props = new TypedProperties();
            props.loadFromClassPath("settings.properties");

            screenshotFileName = props.getProperty("SCREENSHOT_FILENAME");
        } catch (NumberFormatException e) {
            logger.debug(e.getMessage());
        }

        colormaps = ColormapInterpreter.getColormapNames();

        dataModes = new String[] { "dataset1", "dataset2", "difference" };
        variables = new String[] { "var1", "var2", "var3" };

        variableLowerBounds = new HashMap<String, Float>();
        variableUpperBounds = new HashMap<String, Float>();
        for (String varName : variables) {
            variableLowerBounds.put(varName, 0f);
            variableUpperBounds.put(varName, 1f);
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

    public void selectDataMode(int selectedIndex) {
        selectedDataModeIndex = selectedIndex;
    }

    public int getSelectedDataModeIndex() {
        return selectedDataModeIndex;
    }

    public void selectVariable(int selectedIndex) {
        variableIndex = selectedIndex;
    }

    public int getSelectedVariableIndex() {
        return variableIndex;
    }

    public String getSelectedVariableName() {
        return variables[variableIndex];
    }

    public String[] getDataModes() {
        return dataModes;
    }

    public String[] getVariables() {
        return variables;
    }

    public String[] getColormaps() {
        return colormaps;
    }

    public String getSelectedColormapName() {
        return colormaps[selectedColormapIndex];
    }

    public int getSelectedColormapIndex() {
        return selectedColormapIndex;
    }

    public void setSelectedColormapIndex(int selectedColormapIndex) {
        this.selectedColormapIndex = selectedColormapIndex;
    }

    /**
     * Getter for rangeSliderLowerValue.
     * 
     * @return the rangeSliderLowerValue.
     */
    public int getRangeSliderLowerValue() {
        return rangeSliderLowerValue;
    }

    /**
     * Setter for rangeSliderLowerValue.
     * 
     * @param rangeSliderLowerValue
     *            the rangeSliderLowerValue to set
     */
    public void setRangeSliderLowerValue(int rangeSliderLowerValue) {
        this.rangeSliderLowerValue = rangeSliderLowerValue;
    }

    /**
     * Getter for rangeSliderUpperValue.
     * 
     * @return the rangeSliderUpperValue.
     */
    public int getRangeSliderUpperValue() {
        return rangeSliderUpperValue;
    }

    /**
     * Setter for rangeSliderUpperValue.
     * 
     * @param rangeSliderUpperValue
     *            the rangeSliderUpperValue to set
     */
    public void setRangeSliderUpperValue(int rangeSliderUpperValue) {
        this.rangeSliderUpperValue = rangeSliderUpperValue;
    }

    public float getVariableLowerBound(String varName) {
        return variableLowerBounds.get(varName);
    }

    public void setVariableLowerBound(String varName, float var) {
        variableLowerBounds.put(varName, var / 100f);
    }

    public float getVariableUpperBound(String varName) {
        return variableUpperBounds.get(varName);
    }

    public void setVariableUpperBound(String varName, float var) {
        variableUpperBounds.put(varName, var / 100f);
    }

}
