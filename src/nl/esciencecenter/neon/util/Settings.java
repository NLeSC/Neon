package nl.esciencecenter.neon.util;

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
 * A singleton pattern generic Settings File reader for use in OpenGL
 * applications. Pair this with a settings.properties file in your project root.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * @see TypedProperties
 */
public class Settings {
    private static final Logger logger = LoggerFactory.getLogger(Settings.class);

    private boolean stereoRendering = true;
    private boolean stereoSwitched = true;

    private float stereoOcularDistanceMin = 0f;
    private float stereoOcularDistanceDef = .2f;
    private float stereoOcularDistanceMax = 1f;

    // Size settings for default startup and screenshots
    private int defaultScreenWidth = 1920;
    private int defaultScreenHeight = 720;

    // Settings for the initial view
    private int initialSimulationFrame = 0;
    private float initialRotationX = 17f;
    private float initialRotationY = -25f;
    private float initialZoom = -390.0f;

    // Setting per movie frame
    private boolean movieRotate = true;
    private float movieRotationSpeedMin = -1f;
    private float movieRotationSpeedMax = 1f;
    private float movieRotationSpeedDef = -0.25f;

    // Settings for the gas cloud octree
    private int maxOctreeDepth = 25;
    private float octreeEdges = 800f;

    // Settings that should never change, but are listed here to make sure they
    // can be found if necessary
    private int maxExpectedModels = 1000;

    private String screenshotPath = System.getProperty("user.dir") + System.getProperty("path.separator");

    private String[] acceptableNetcdfExtenstions = { ".nc" };

    private String currentNetcdfExtenstion = "nc";

    private boolean touchConnectionEnabled = false;
    private int interfaceWidth = 240;
    private int interfaceHeight = 720;

    private static class SingletonHolder {
        public final static Settings instance = new Settings();
    }

    public static Settings getInstance() {
        return SingletonHolder.instance;
    }

    public Settings() {
        try {
            TypedProperties props = new TypedProperties();
            props.loadFromClassPath("settings.properties");

            stereoRendering = props.getBooleanProperty("STEREO_RENDERING");
            stereoSwitched = props.getBooleanProperty("STEREO_SWITCHED");

            stereoOcularDistanceMin = props.getFloatProperty("STEREO_OCULAR_DISTANCE_MIN");
            stereoOcularDistanceMax = props.getFloatProperty("STEREO_OCULAR_DISTANCE_MAX");
            stereoOcularDistanceDef = props.getFloatProperty("STEREO_OCULAR_DISTANCE_DEF");

            // Size settings for default startup and screenshots
            defaultScreenWidth = props.getIntProperty("DEFAULT_SCREEN_WIDTH");
            defaultScreenHeight = props.getIntProperty("DEFAULT_SCREEN_HEIGHT");

            interfaceWidth = props.getIntProperty("INTERFACE_WIDTH");
            interfaceHeight = props.getIntProperty("INTERFACE_HEIGHT");

            // Settings for the initial view
            initialSimulationFrame = props.getIntProperty("INITIAL_SIMULATION_FRAME");
            initialRotationX = props.getFloatProperty("INITIAL_ROTATION_X");
            initialRotationY = props.getFloatProperty("INITIAL_ROTATION_Y");
            initialZoom = props.getFloatProperty("INITIAL_ZOOM");

            screenshotPath = props.getProperty("SCREENSHOT_PATH");
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
        }
    }

    public boolean getStereo() {
        return stereoRendering;
    }

    public void setStereo(int stateChange) {
        if (stateChange == 1) {
            stereoRendering = true;
        }

        if (stateChange == 2) {
            stereoRendering = false;
        }
    }

    public boolean getStereoSwitched() {
        return stereoSwitched;
    }

    public void setStereoSwitched(int stateChange) {
        if (stateChange == 1) {
            stereoSwitched = true;
        }

        if (stateChange == 2) {
            stereoSwitched = false;
        }
    }

    public float getStereoOcularDistanceMin() {
        return stereoOcularDistanceMin;
    }

    public float getStereoOcularDistanceMax() {
        return stereoOcularDistanceMax;
    }

    public float getStereoOcularDistance() {
        return stereoOcularDistanceDef;
    }

    public void setStereoOcularDistance(float value) {
        stereoOcularDistanceDef = value;
    }

    public int getDefaultScreenWidth() {
        return defaultScreenWidth;
    }

    public int getDefaultScreenHeight() {
        return defaultScreenHeight;
    }

    public int getMaxOctreeDepth() {
        return maxOctreeDepth;
    }

    public float getOctreeEdges() {
        return octreeEdges;
    }

    public int getMaxExpectedModels() {
        return maxExpectedModels;
    }

    public float getInitialRotationX() {
        return initialRotationX;
    }

    public float getInitialRotationY() {
        return initialRotationY;
    }

    public float getInitialZoom() {
        return initialZoom;
    }

    public void setMovieRotate(int stateChange) {
        if (stateChange == 1) {
            movieRotate = true;
        }
        if (stateChange == 2) {
            movieRotate = false;
        }
    }

    public boolean getMovieRotate() {
        return movieRotate;
    }

    public void setMovieRotationSpeed(float value) {
        movieRotationSpeedDef = value;
    }

    public float getMovieRotationSpeedMin() {
        return movieRotationSpeedMin;
    }

    public float getMovieRotationSpeedMax() {
        return movieRotationSpeedMax;
    }

    public float getMovieRotationSpeedDef() {
        return movieRotationSpeedDef;
    }

    public int getInitialSimulationFrame() {
        return initialSimulationFrame;
    }

    public void setInitialSimulationFrame(int initialSimulationFrame) {
        this.initialSimulationFrame = initialSimulationFrame;
    }

    public void setInitialRotationX(float initialRotationX) {
        this.initialRotationX = initialRotationX;
    }

    public void setInitialRotationY(float initialRotationY) {
        this.initialRotationY = initialRotationY;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String newPath) {
        screenshotPath = newPath;
    }

    public String getCurrentNetCDFExtension() {
        return currentNetcdfExtenstion;
    }

    public String[] getAcceptableNetCDFExtensions() {
        return acceptableNetcdfExtenstions.clone();
    }

    public boolean isTouchConnected() {
        return touchConnectionEnabled;
    }

    public int getInterfaceWidth() {
        return interfaceWidth;
    }

    public int getInterfaceHeight() {
        return interfaceHeight;
    }

    /**
     * Getter for stereoRendering.
     * 
     * @return the stereoRendering.
     */
    public boolean isStereoRendering() {
        return stereoRendering;
    }

    /**
     * Setter for stereoRendering.
     * 
     * @param stereoRendering
     *            the stereoRendering to set
     */
    public void setStereoRendering(boolean stereoRendering) {
        this.stereoRendering = stereoRendering;
    }

    /**
     * Getter for stereoOcularDistanceDef.
     * 
     * @return the stereoOcularDistanceDef.
     */
    public float getStereoOcularDistanceDef() {
        return stereoOcularDistanceDef;
    }

    /**
     * Setter for stereoOcularDistanceDef.
     * 
     * @param stereoOcularDistanceDef
     *            the stereoOcularDistanceDef to set
     */
    public void setStereoOcularDistanceDef(float stereoOcularDistanceDef) {
        this.stereoOcularDistanceDef = stereoOcularDistanceDef;
    }

    /**
     * Getter for acceptableNetcdfExtenstions.
     * 
     * @return the acceptableNetcdfExtenstions.
     */
    public String[] getAcceptableNetcdfExtenstions() {
        return acceptableNetcdfExtenstions.clone();
    }

    /**
     * Setter for acceptableNetcdfExtenstions.
     * 
     * @param acceptableNetcdfExtenstions
     *            the acceptableNetcdfExtenstions to set
     */
    public void setAcceptableNetcdfExtenstions(String[] acceptableNetcdfExtenstions) {
        this.acceptableNetcdfExtenstions = acceptableNetcdfExtenstions.clone();
    }

    /**
     * Getter for currentNetcdfExtenstion.
     * 
     * @return the currentNetcdfExtenstion.
     */
    public String getCurrentNetcdfExtenstion() {
        return currentNetcdfExtenstion;
    }

    /**
     * Setter for currentNetcdfExtenstion.
     * 
     * @param currentNetcdfExtenstion
     *            the currentNetcdfExtenstion to set
     */
    public void setCurrentNetcdfExtenstion(String currentNetcdfExtenstion) {
        this.currentNetcdfExtenstion = currentNetcdfExtenstion;
    }

    /**
     * Getter for touchConnectionEnabled.
     * 
     * @return the touchConnectionEnabled.
     */
    public boolean isTouchConnectionEnabled() {
        return touchConnectionEnabled;
    }

    /**
     * Setter for touchConnectionEnabled.
     * 
     * @param touchConnectionEnabled
     *            the touchConnectionEnabled to set
     */
    public void setTouchConnectionEnabled(boolean touchConnectionEnabled) {
        this.touchConnectionEnabled = touchConnectionEnabled;
    }

    /**
     * Setter for stereoSwitched.
     * 
     * @param stereoSwitched
     *            the stereoSwitched to set
     */
    public void setStereoSwitched(boolean stereoSwitched) {
        this.stereoSwitched = stereoSwitched;
    }

    /**
     * Setter for stereoOcularDistanceMin.
     * 
     * @param stereoOcularDistanceMin
     *            the stereoOcularDistanceMin to set
     */
    public void setStereoOcularDistanceMin(float stereoOcularDistanceMin) {
        this.stereoOcularDistanceMin = stereoOcularDistanceMin;
    }

    /**
     * Setter for stereoOcularDistanceMax.
     * 
     * @param stereoOcularDistanceMax
     *            the stereoOcularDistanceMax to set
     */
    public void setStereoOcularDistanceMax(float stereoOcularDistanceMax) {
        this.stereoOcularDistanceMax = stereoOcularDistanceMax;
    }

    /**
     * Setter for defaultScreenWidth.
     * 
     * @param defaultScreenWidth
     *            the defaultScreenWidth to set
     */
    public void setDefaultScreenWidth(int defaultScreenWidth) {
        this.defaultScreenWidth = defaultScreenWidth;
    }

    /**
     * Setter for defaultScreenHeight.
     * 
     * @param defaultScreenHeight
     *            the defaultScreenHeight to set
     */
    public void setDefaultScreenHeight(int defaultScreenHeight) {
        this.defaultScreenHeight = defaultScreenHeight;
    }

    /**
     * Setter for initialZoom.
     * 
     * @param initialZoom
     *            the initialZoom to set
     */
    public void setInitialZoom(float initialZoom) {
        this.initialZoom = initialZoom;
    }

    /**
     * Setter for movieRotate.
     * 
     * @param movieRotate
     *            the movieRotate to set
     */
    public void setMovieRotate(boolean movieRotate) {
        this.movieRotate = movieRotate;
    }

    /**
     * Setter for movieRotationSpeedMin.
     * 
     * @param movieRotationSpeedMin
     *            the movieRotationSpeedMin to set
     */
    public void setMovieRotationSpeedMin(float movieRotationSpeedMin) {
        this.movieRotationSpeedMin = movieRotationSpeedMin;
    }

    /**
     * Setter for movieRotationSpeedMax.
     * 
     * @param movieRotationSpeedMax
     *            the movieRotationSpeedMax to set
     */
    public void setMovieRotationSpeedMax(float movieRotationSpeedMax) {
        this.movieRotationSpeedMax = movieRotationSpeedMax;
    }

    /**
     * Setter for movieRotationSpeedDef.
     * 
     * @param movieRotationSpeedDef
     *            the movieRotationSpeedDef to set
     */
    public void setMovieRotationSpeedDef(float movieRotationSpeedDef) {
        this.movieRotationSpeedDef = movieRotationSpeedDef;
    }

    /**
     * Setter for maxOctreeDepth.
     * 
     * @param maxOctreeDepth
     *            the maxOctreeDepth to set
     */
    public void setMaxOctreeDepth(int maxOctreeDepth) {
        this.maxOctreeDepth = maxOctreeDepth;
    }

    /**
     * Setter for octreeEdges.
     * 
     * @param octreeEdges
     *            the octreeEdges to set
     */
    public void setOctreeEdges(float octreeEdges) {
        this.octreeEdges = octreeEdges;
    }

    /**
     * Setter for maxExpectedModels.
     * 
     * @param maxExpectedModels
     *            the maxExpectedModels to set
     */
    public void setMaxExpectedModels(int maxExpectedModels) {
        this.maxExpectedModels = maxExpectedModels;
    }

    /**
     * Setter for interfaceWidth.
     * 
     * @param interfaceWidth
     *            the interfaceWidth to set
     */
    public void setInterfaceWidth(int interfaceWidth) {
        this.interfaceWidth = interfaceWidth;
    }

    /**
     * Setter for interfaceHeight.
     * 
     * @param interfaceHeight
     *            the interfaceHeight to set
     */
    public void setInterfaceHeight(int interfaceHeight) {
        this.interfaceHeight = interfaceHeight;
    }
}
