package nl.esciencecenter.esight.util;

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
    private boolean stereoRendering = true;
    private boolean stereoSwitched = true;

    private float stereoOcularDistance_min = 0f;
    private float stereoOcularDistance_def = .2f;
    private float stereoOcularDistance_max = 1f;

    // Size settings for default startup and screenshots
    private int defaultScreen_width = 1920;
    private int defaultScreen_height = 720;

    // Settings for the initial view
    private int initialSimulation_frame = 0;
    private float initialRotationX = 17f;
    private float initialRotationY = -25f;
    private float initialZoom = -390.0f;

    // Setting per movie frame
    private boolean movieRotate = true;
    private final float movieRotationSpeedMin = -1f;
    private final float movieRotationSpeedMax = 1f;
    private float movieRotationSpeedDef = -0.25f;

    // Settings for the gas cloud octree
    private final int maxOctreeDepth = 25;
    private final float octreeEdges = 800f;

    // Settings that should never change, but are listed here to make sure they
    // can be found if necessary
    private final int maxExpectedModels = 1000;

    private String screenshotPath = System.getProperty("user.dir") + System.getProperty("path.separator");

    private final String[] acceptableNetcdfExtenstions = { ".nc" };

    private final String currentNetcdfExtenstion = "nc";

    private static final boolean touch_connection_enabled = false;
    private static int interfaceWidth = 240;
    private static int interfaceHeight = 720;

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

            stereoOcularDistance_min = props.getFloatProperty("STEREO_OCULAR_DISTANCE_MIN");
            stereoOcularDistance_max = props.getFloatProperty("STEREO_OCULAR_DISTANCE_MAX");
            stereoOcularDistance_def = props.getFloatProperty("STEREO_OCULAR_DISTANCE_DEF");

            // Size settings for default startup and screenshots
            defaultScreen_width = props.getIntProperty("DEFAULT_SCREEN_WIDTH");
            defaultScreen_height = props.getIntProperty("DEFAULT_SCREEN_HEIGHT");

            interfaceWidth = props.getIntProperty("INTERFACE_WIDTH");
            interfaceHeight = props.getIntProperty("INTERFACE_HEIGHT");

            // Settings for the initial view
            initialSimulation_frame = props.getIntProperty("INITIAL_SIMULATION_FRAME");
            initialRotationX = props.getFloatProperty("INITIAL_ROTATION_X");
            initialRotationY = props.getFloatProperty("INITIAL_ROTATION_Y");
            initialZoom = props.getFloatProperty("INITIAL_ZOOM");

            screenshotPath = props.getProperty("SCREENSHOT_PATH");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getStereo() {
        return stereoRendering;
    }

    public void setStereo(int stateChange) {
        if (stateChange == 1)
            stereoRendering = true;
        if (stateChange == 2)
            stereoRendering = false;
    }

    public boolean getStereoSwitched() {
        return stereoSwitched;
    }

    public void setStereoSwitched(int stateChange) {
        if (stateChange == 1)
            stereoSwitched = true;
        if (stateChange == 2)
            stereoSwitched = false;
    }

    public float getStereoOcularDistanceMin() {
        return stereoOcularDistance_min;
    }

    public float getStereoOcularDistanceMax() {
        return stereoOcularDistance_max;
    }

    public float getStereoOcularDistance() {
        return stereoOcularDistance_def;
    }

    public void setStereoOcularDistance(float value) {
        stereoOcularDistance_def = value;
    }

    public int getDefaultScreenWidth() {
        return defaultScreen_width;
    }

    public int getDefaultScreenHeight() {
        return defaultScreen_height;
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
        if (stateChange == 1)
            movieRotate = true;
        if (stateChange == 2)
            movieRotate = false;
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
        return initialSimulation_frame;
    }

    public void setInitial_simulation_frame(int initialSimulationFrame) {
        initialSimulation_frame = initialSimulationFrame;
    }

    public void setInitial_rotation_x(float initialRotationX) {
        this.initialRotationX = initialRotationX;
    }

    public void setInitial_rotation_y(float initialRotationY) {
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
        return acceptableNetcdfExtenstions;
    }

    public boolean isTouchConnected() {
        return touch_connection_enabled;
    }

    public int getInterfaceWidth() {
        return interfaceWidth;
    }

    public int getInterfaceHeight() {
        return interfaceHeight;
    }
}
