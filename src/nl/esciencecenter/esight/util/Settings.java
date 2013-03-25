package nl.esciencecenter.esight.util;

/**
 * A singleton pattern generic Settings File reader for use in OpenGL
 * applications.
 * Pair this with a settings.properties file in your project root.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * @see TypedProperties
 */
public class Settings {
    private boolean              STEREO_RENDERING              = true;
    private boolean              STEREO_SWITCHED               = true;

    private float                STEREO_OCULAR_DISTANCE_MIN    = 0f;
    private float                STEREO_OCULAR_DISTANCE_DEF    = .2f;
    private float                STEREO_OCULAR_DISTANCE_MAX    = 1f;

    // Size settings for default startup and screenshots
    private int                  DEFAULT_SCREEN_WIDTH          = 1920;
    private int                  DEFAULT_SCREEN_HEIGHT         = 1080;

    private int                  SCREENSHOT_SCREEN_WIDTH       = 1920;
    private int                  SCREENSHOT_SCREEN_HEIGHT      = 1080;

    // Settings for the initial view
    private int                  INITIAL_SIMULATION_FRAME      = 0;
    private float                INITIAL_ROTATION_X            = 17f;
    private float                INITIAL_ROTATION_Y            = -25f;
    private float                INITIAL_ZOOM                  = -390.0f;

    // Setting per movie frame
    private boolean              MOVIE_ROTATE                  = true;
    private float                MOVIE_ROTATION_SPEED_MIN      = -1f;
    private float                MOVIE_ROTATION_SPEED_MAX      = 1f;
    private float                MOVIE_ROTATION_SPEED_DEF      = -0.25f;

    // Settings for the gas cloud octree
    private int                  MAX_OCTREE_DEPTH              = 25;
    private float                OCTREE_EDGES                  = 800f;

    // Settings that should never change, but are listed here to make sure they
    // can be found if necessary
    private int                  MAX_EXPECTED_MODELS           = 1000;

    protected String             SCREENSHOT_PATH               = System.getProperty("user.dir")
                                                                       + System.getProperty("path.separator");

    private final String[]       ACCEPTABLE_NETCDF_EXTENSTIONS = { ".nc" };

    private final String         CURRENT_NETCDF_EXTENSTION     = "nc";

    private static final boolean TOUCH_CONNECTION_ENABLED      = false;
    private static int           INTERFACE_WIDTH               = 240;
    private static int           INTERFACE_HEIGHT              = 1080;

    private static class SingletonHolder {
        public final static Settings instance = new Settings();
    }

    public static Settings getInstance() {
        return SingletonHolder.instance;
    }

    protected Settings() {
        try {
            TypedProperties props = new TypedProperties();
            props.loadFromFile("settings.properties");

            STEREO_RENDERING = props.getBooleanProperty("STEREO_RENDERING");
            STEREO_SWITCHED = props.getBooleanProperty("STEREO_SWITCHED");

            STEREO_OCULAR_DISTANCE_MIN = props
                    .getFloatProperty("STEREO_OCULAR_DISTANCE_MIN");
            STEREO_OCULAR_DISTANCE_MAX = props
                    .getFloatProperty("STEREO_OCULAR_DISTANCE_MAX");
            STEREO_OCULAR_DISTANCE_DEF = props
                    .getFloatProperty("STEREO_OCULAR_DISTANCE_DEF");

            // Size settings for default startup and screenshots
            DEFAULT_SCREEN_WIDTH = props.getIntProperty("DEFAULT_SCREEN_WIDTH");
            DEFAULT_SCREEN_HEIGHT = props
                    .getIntProperty("DEFAULT_SCREEN_HEIGHT");

            INTERFACE_WIDTH = props.getIntProperty("INTERFACE_WIDTH");
            INTERFACE_HEIGHT = props
                    .getIntProperty("INTERFACE_HEIGHT");

            SCREENSHOT_SCREEN_WIDTH = props
                    .getIntProperty("SCREENSHOT_SCREEN_WIDTH");
            SCREENSHOT_SCREEN_HEIGHT = props
                    .getIntProperty("SCREENSHOT_SCREEN_HEIGHT");

            // Settings for the initial view
            INITIAL_SIMULATION_FRAME = props
                    .getIntProperty("INITIAL_SIMULATION_FRAME");
            INITIAL_ROTATION_X = props.getFloatProperty("INITIAL_ROTATION_X");
            INITIAL_ROTATION_Y = props.getFloatProperty("INITIAL_ROTATION_Y");
            INITIAL_ZOOM = props.getFloatProperty("INITIAL_ZOOM");

            // Setting per movie frame
            MOVIE_ROTATE = props.getBooleanProperty("MOVIE_ROTATE");
            MOVIE_ROTATION_SPEED_MIN = props
                    .getFloatProperty("MOVIE_ROTATION_SPEED_MIN");
            MOVIE_ROTATION_SPEED_MAX = props
                    .getFloatProperty("MOVIE_ROTATION_SPEED_MAX");
            MOVIE_ROTATION_SPEED_DEF = props
                    .getFloatProperty("MOVIE_ROTATION_SPEED_DEF");

            // Settings for the gas cloud octree
            MAX_OCTREE_DEPTH = props.getIntProperty("MAX_OCTREE_DEPTH");
            OCTREE_EDGES = props.getFloatProperty("OCTREE_EDGES");

            // Settings that should never change, but are listed here to make
            // sure they can be found if necessary
            MAX_EXPECTED_MODELS = props.getIntProperty("MAX_EXPECTED_MODELS");

            SCREENSHOT_PATH = props.getProperty("SCREENSHOT_PATH");
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getStereo() {
        return STEREO_RENDERING;
    }

    public void setStereo(int stateChange) {
        if (stateChange == 1)
            STEREO_RENDERING = true;
        if (stateChange == 2)
            STEREO_RENDERING = false;
    }

    public boolean getStereoSwitched() {
        return STEREO_SWITCHED;
    }

    public void setStereoSwitched(int stateChange) {
        if (stateChange == 1)
            STEREO_SWITCHED = true;
        if (stateChange == 2)
            STEREO_SWITCHED = false;
    }

    public float getStereoOcularDistanceMin() {
        return STEREO_OCULAR_DISTANCE_MIN;
    }

    public float getStereoOcularDistanceMax() {
        return STEREO_OCULAR_DISTANCE_MAX;
    }

    public float getStereoOcularDistance() {
        return STEREO_OCULAR_DISTANCE_DEF;
    }

    public void setStereoOcularDistance(float value) {
        STEREO_OCULAR_DISTANCE_DEF = value;
    }

    public int getDefaultScreenWidth() {
        return DEFAULT_SCREEN_WIDTH;
    }

    public int getDefaultScreenHeight() {
        return DEFAULT_SCREEN_HEIGHT;
    }

    public int getScreenshotScreenWidth() {
        return SCREENSHOT_SCREEN_WIDTH;
    }

    public int getScreenshotScreenHeight() {
        return SCREENSHOT_SCREEN_HEIGHT;
    }

    public int getMaxOctreeDepth() {
        return MAX_OCTREE_DEPTH;
    }

    public float getOctreeEdges() {
        return OCTREE_EDGES;
    }

    public int getMaxExpectedModels() {
        return MAX_EXPECTED_MODELS;
    }

    public float getInitialRotationX() {
        return INITIAL_ROTATION_X;
    }

    public float getInitialRotationY() {
        return INITIAL_ROTATION_Y;
    }

    public float getInitialZoom() {
        return INITIAL_ZOOM;
    }

    public void setMovieRotate(int stateChange) {
        if (stateChange == 1)
            MOVIE_ROTATE = true;
        if (stateChange == 2)
            MOVIE_ROTATE = false;
    }

    public boolean getMovieRotate() {
        return MOVIE_ROTATE;
    }

    public void setMovieRotationSpeed(float value) {
        MOVIE_ROTATION_SPEED_DEF = value;
    }

    public float getMovieRotationSpeedMin() {
        return MOVIE_ROTATION_SPEED_MIN;
    }

    public float getMovieRotationSpeedMax() {
        return MOVIE_ROTATION_SPEED_MAX;
    }

    public float getMovieRotationSpeedDef() {
        return MOVIE_ROTATION_SPEED_DEF;
    }

    public int getInitialSimulationFrame() {
        return INITIAL_SIMULATION_FRAME;
    }

    public void setInitial_simulation_frame(int initialSimulationFrame) {
        INITIAL_SIMULATION_FRAME = initialSimulationFrame;
    }

    public void setInitial_rotation_x(float initialRotationX) {
        INITIAL_ROTATION_X = initialRotationX;
    }

    public void setInitial_rotation_y(float initialRotationY) {
        INITIAL_ROTATION_Y = initialRotationY;
    }

    public String getScreenshotPath() {
        return SCREENSHOT_PATH;
    }

    public void setScreenshotPath(String newPath) {
        SCREENSHOT_PATH = newPath;
    }

    public String getCurrentNetCDFExtension() {
        return CURRENT_NETCDF_EXTENSTION;
    }

    public String[] getAcceptableNetCDFExtensions() {
        return ACCEPTABLE_NETCDF_EXTENSTIONS;
    }

    public boolean isTouchConnected() {
        return TOUCH_CONNECTION_ENABLED;
    }

    public int getInterfaceWidth() {
        return INTERFACE_WIDTH;
    }

    public int getInterfaceHeight() {
        return INTERFACE_HEIGHT;
    }
}
