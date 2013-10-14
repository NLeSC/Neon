package nl.esciencecenter.neon.examples.viaAppia;

import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.neon.NeonGLEventListener;
import nl.esciencecenter.neon.datastructures.IntPBO;
import nl.esciencecenter.neon.datastructures.InterpolatedGeoGrid;
import nl.esciencecenter.neon.exceptions.InverseNotAvailableException;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.input.InputHandler;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.MatF4;
import nl.esciencecenter.neon.math.MatrixFMath;
import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.VecF3;
import nl.esciencecenter.neon.math.VecF4;
import nl.esciencecenter.neon.models.Axis;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.models.graphs.BezierGraph2D;
import nl.esciencecenter.neon.models.graphs.Histogram2D;
import nl.esciencecenter.neon.models.graphs.LineGraph2D;
import nl.esciencecenter.neon.models.graphs.ScatterPlot3D;
import nl.esciencecenter.neon.models.graphs.VisualGrid;
import nl.esciencecenter.neon.shaders.ShaderProgram;

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
 * Example implementation of a NeonGLEventListener. Renders Axes in different
 * colors to a texture and renders then this texture to the screen.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ViaAppiaGLEventListener extends NeonGLEventListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(ViaAppiaGLEventListener.class);

    // Two example shader program definitions.
    private ShaderProgram axesShaderProgram, textShaderProgram, lineShaderProgram;

    // Model definitions, the quad is necessary for Full-screen rendering. The
    // axes are the model we wish to render (example)
    private Model xAxis, yAxis, zAxis;

    private DataReader dr;
    private Histogram2D hist;
    private final int[] histData = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private LineGraph2D lineGraph;
    private BezierGraph2D bezierGraph;

    private static int GRID_WIDTH = 300;
    private static int GRID_HEIGHT = 300;
    private InterpolatedGeoGrid intpGeoGrid;
    private VisualGrid visualGrid;

    private ScatBuilder scatBuilder;
    private ScatterPlot3D scat;

    // Global (singleton) settings instance.
    private final ViaAppiaSettings settings = ViaAppiaSettings.getInstance();

    // Pixelbuffer Object, we use this to get screenshots.
    private IntPBO finalPBO;

    // Global (singleton) inputhandler instance.
    private final ViaAppiaInputHandler inputHandler = ViaAppiaInputHandler.getInstance();

    // State keeping variable
    private boolean screenshotWanted;

    // Height and width of the drawable area. We extract this from the opengl
    // instance in the reshape method every time it is changed, but set it in
    // the init method initially. The default values are defined by the settings
    // class.
    private int canvasWidth, canvasHeight;

    // Variables needed to calculate the viewpoint and camera angle.
    final Point4 eye = new Point4((float) (getRadius() * Math.sin(getFtheta()) * Math.cos(getPhi())),
            (float) (getRadius() * Math.sin(getFtheta()) * Math.sin(getPhi())),
            (float) (getRadius() * Math.cos(getFtheta())));
    final Point4 at = new Point4(0.0f, 0.0f, 0.0f);
    final VecF4 up = new VecF4(0.0f, 1.0f, 0.0f, 0.0f);

    /**
     * Basic constructor for NeonExampleGLEventListener.
     */
    public ViaAppiaGLEventListener() {
        super();

    }

    // Initialization method, this is called by the animator before anything
    // else, and is therefore the perfect place to initialize all of the
    // ShaderPrograms, FrameBuffer objects and such.
    @Override
    public void init(GLAutoDrawable drawable) {
        // Get the Opengl context from the drawable, and make it current, so
        // we can see it and draw on it. I've never seen this fail, but there is
        // error checking anyway.
        contextOn(drawable);

        // Once we have the context current, we can extract the OpenGL instance
        // from it. We have defined a OpenGL 3.0 instance in the
        // NeonNewtWindow by adding the line
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // set the canvas size and aspect ratio in the global variables.
        canvasWidth = GLContext.getCurrent().getGLDrawable().getWidth();
        canvasHeight = GLContext.getCurrent().getGLDrawable().getHeight();
        setAspect((float) canvasWidth / (float) canvasHeight);

        // Enable Anti-Aliasing (smoothing of jagged edges on the edges of
        // objects).
        gl.glEnable(GL3.GL_LINE_SMOOTH);
        gl.glHint(GL3.GL_LINE_SMOOTH_HINT, GL3.GL_NICEST);
        gl.glEnable(GL3.GL_POLYGON_SMOOTH);
        gl.glHint(GL3.GL_POLYGON_SMOOTH_HINT, GL3.GL_NICEST);

        // Enable Depth testing (Render only those objects that are not obscured
        // by other objects).
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glClearDepth(1.0f);

        // Enable Culling (render only the camera-facing sides of objects).
        // gl.glEnable(GL3.GL_CULL_FACE);
        // gl.glCullFace(GL3.GL_BACK);

        // Enable Blending (needed for both Transparency and Anti-Aliasing)
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);

        // Enable Vertical Sync
        gl.setSwapInterval(1);

        // Set black background
        gl.glClearColor(0f, 0f, 0f, 0f);

        // Enable programmatic setting of point size, for rendering points (not
        // needed for this example application).
        gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);

        // Load and compile shaders from source Files (there are other options;
        // check the ShaderProgram Javadoc).
        try {
            // Create the ShaderProgram that we're going to use for the Example
            // Axes. The source code for the VertexShader: shaders/vs_axes.vp,
            // and the source code for the FragmentShader: shaders/fs_axes.fp
            axesShaderProgram = getLoader().createProgram(gl, "axes", new File("shaders/vs_axes.vp"),
                    new File("shaders/fs_axes.fp"));
            // Do the same for the text shader
            textShaderProgram = getLoader().createProgram(gl, "text", new File("shaders/vs_multiColorTextShader.vp"),
                    new File("shaders/fs_multiColorTextShader.fp"));
            // Do the same for the line shader
            lineShaderProgram = getLoader().createProgram(gl, "line", new File("shaders/vs_lineShader.vp"),
                    new File("shaders/fs_lineShader.fp"));

            // Same for the postprocessing shader.
            // postprocessShader = getLoader().createProgram(gl, "postProcess",
            // new File("shaders/vs_postprocess.vp"),
            // new File("shaders/fs_examplePostprocess.fp"));
        } catch (final Exception e) {
            // If compilation fails, we will output the error message and quit
            // the application.
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Here we define the Axis models, and initialize them.
        xAxis = new Axis(new VecF3(-1f, 0f, 0f), new VecF3(1f, 0f, 0f), .1f, .02f);
        xAxis.init(gl);
        yAxis = new Axis(new VecF3(0f, -1f, 0f), new VecF3(0f, 1f, 0f), .1f, .02f);
        yAxis.init(gl);
        zAxis = new Axis(new VecF3(0f, 0f, -1f), new VecF3(0f, 0f, 1f), .1f, .02f);
        zAxis.init(gl);

        // Here we define a PixelBufferObject, which is used for getting
        // screenshots.
        finalPBO = new IntPBO(canvasWidth, canvasHeight);
        finalPBO.init(gl);

        Color4[] vegetationColors = new Color4[] { Color4.BLUE, new Color4("00755E"), new Color4("014421"),
                new Color4("008000"), Color4.GREEN, Color4.CYAN, new Color4(135, 156, 69, 255), Color4.YELLOW,
                Color4.WHITE };
        String[] vegetationNames = new String[] { "Water", "Rain Forest", "Deciduous Forest", "Evergreen Forest",
                "Grassland", "Tundra", "Shrubland", "Desert", "Ice" };

        // Read data

        // dr = new DataReader();
        //
        // intpGeoGrid = new InterpolatedGeoGrid(GRID_WIDTH, GRID_HEIGHT);
        // visualGrid = new VisualGrid(GRID_WIDTH, GRID_HEIGHT);
        //
        // do {
        // MapPoint mp = dr.getMapPoint();
        //
        // if (mp != null) {
        // intpGeoGrid.addData(mp.getLatitude() / 180f, mp.getLongitude() /
        // 360f,
        // new float[] { mp.getHeight() });
        // }
        //
        // } while (dr.next());
        //
        // float[][] gridifiedData = intpGeoGrid.calculate();
        //
        // if (gridifiedData != null) {
        // VecF4[][] points = new VecF4[GRID_HEIGHT][GRID_WIDTH], colors =
        // new VecF4[GRID_HEIGHT][GRID_WIDTH];
        //
        // Dimensions dims = new Dimensions(0f, 1f);
        //
        // for (int latIndex = 0; latIndex < GRID_HEIGHT; latIndex++) {
        // for (int lonIndex = 0; lonIndex < GRID_WIDTH; lonIndex++) {
        // points[latIndex][lonIndex] = new VecF4((float) latIndex / (float)
        // GRID_HEIGHT, 0f,
        // (float) lonIndex / (float) GRID_WIDTH, 1f);
        //
        // int visualIndex = latIndex * GRID_HEIGHT + lonIndex;
        // float[] gridPointData = gridifiedData[visualIndex];
        //
        // Color swingColor = ColormapInterpreter.getColor("hotres", dims,
        // gridPointData[0], Float.NaN);
        // Color4 color = new Color4(swingColor.getRed(),
        // swingColor.getGreen(), swingColor.getBlue(),
        // swingColor.getAlpha());
        //
        // colors[latIndex][lonIndex] = color;
        // }
        // }
        //
        // visualGrid.setGrid(points, colors);
        // visualGrid.init(gl);
        // }
        //
        // hist = new Histogram2D(vegetationColors, vegetationNames);
        // hist.init(gl);
        //
        // lineGraph = new LineGraph2D(50, vegetationColors,
        // vegetationNames, "Height above sealevel in meters",
        // "% of total vegetation of this type");
        // lineGraph.init(gl);
        //
        // bezierGraph = new BezierGraph2D(50, vegetationColors,
        // vegetationNames, "Latitude in degrees",
        // "% of total vegetation of this type");
        // bezierGraph.init(gl);

        scatBuilder = new ScatBuilder();
        new Thread(scatBuilder).start();

        // Release the context.
        contextOff(drawable);
    }

    // Display method, this is called by the animator thread to render a single
    // frame. Expect this to be running 60 times a second.
    // The GLAutoDrawable is a JOGL concept that holds the current opengl state.
    @Override
    public void display(GLAutoDrawable drawable) {
        // Get the Opengl context from the drawable, and make it current, so
        // we can see it and draw on it. I've never seen this fail, but there is
        // error checking anyway.
        contextOn(drawable);

        // Once we have the context current, we can extract the OpenGL instance
        // from it. We have defined a OpenGL 3.0 instance in the
        // NeonNewtWindow by adding the line
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // First, we clear the buffer to start with a clean slate to draw on.
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Construct a modelview matrix out of camera viewpoint and angle.
        MatF4 modelViewMatrix = MatrixFMath.lookAt(eye, at, up);

        // Translate the camera backwards according to the inputhandler's view
        // distance setting.
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.translate(new VecF3(inputHandler.getTranslation().getX(),
                inputHandler.getTranslation().getY(), inputHandler.getViewDist())));

        // Rotate tha camera according to the rotation angles defined in the
        // inputhandler.
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationX(inputHandler.getRotation().getX()));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationY(inputHandler.getRotation().getY()));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationZ(inputHandler.getRotation().getZ()));

        // Render the scene with these modelview settings. In this case, the end
        // result of this action will be that the AxesFBO has been filled with
        // the right pixels.
        renderScene(gl, modelViewMatrix);

        ViaAppiaInputHandler.PickRequest pr = ViaAppiaInputHandler.doPick();

        if (pr != null) {
            try {
                VecF4 pickResult = MatrixFMathExt.unProject(makePerspectiveMatrix(), modelViewMatrix, new float[] { 0,
                        0, canvasWidth, canvasHeight }, new VecF3(pr.x, pr.y, -1f));
                System.out.println("Pick result 1: " + pickResult);

                pickResult = MatrixFMathExt.unProject(makePerspectiveMatrix(), modelViewMatrix, new float[] { 0, 0,
                        canvasWidth, canvasHeight }, new VecF3(pr.x, pr.y, 1f));
                System.out.println("Pick result 2: " + pickResult);
            } catch (InverseNotAvailableException e) {
                e.printStackTrace();
            }
        }

        // Make a screenshot, when wanted. The PBO copies the current
        // framebuffer. We then set the state back because we dont want to make
        // a screenshot 60 times a second.
        if (screenshotWanted) {
            finalPBO.makeScreenshotPNG(gl, settings.getScreenshotFileName());

            screenshotWanted = false;
        }

        contextOff(drawable);
    }

    private MatF4 makePerspectiveMatrix() {
        return MatrixFMath.perspective(getFovy(), getAspect(), getzNear(), getzFar());
    }

    /**
     * Scene rendering method. we can add more things here to render than only
     * axes.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     */
    private void renderScene(GL3 gl, MatF4 mv) {
        try {
            // renderAxes(gl, new MatF4(mv), axesShaderProgram);

            renderScatterplot(gl, mv, textShaderProgram);

            // renderLineGraph(gl, mv, lineShaderProgram);
            //
            // renderBezierGraph(gl, mv, lineShaderProgram);
            //
            // renderVisualGrid(gl, mv, textShaderProgram);
            //
            // renderHistogram(gl, mv, axesShaderProgram);
            // textShaderProgram.setUniformMatrix("PMatrix",
            // makePerspectiveMatrix());
            // hist.drawLabels(gl, mv.mul(MatrixFMath.translate(1, 0, 0)),
            // textShaderProgram);
            //
            // ModelViewStack mvStack = new ModelViewStack();
            // mvStack.putTop(MatrixFMath.translate(-1f, 0f, -1f));
            // lineGraph.drawLabels(gl, mv, mvStack, textShaderProgram);
            //
            // mvStack = new ModelViewStack();
            // mvStack.putTop(MatrixFMath.translate(-1f, 0f, 0f));
            // bezierGraph.drawLabels(gl, mv, mvStack, textShaderProgram);

        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    // /**
    // * Axes rendering method. This assumes rendering to an {@link FBO}. This
    // is
    // * not a necessity, but it allows for post processing.
    // *
    // * @param gl
    // * The current openGL instance.
    // * @param mv
    // * The current modelview matrix.
    // * @param target
    // * The {@link ShaderProgram} to use for rendering.
    // * @param target
    // * The target {@link FBO} to render to.
    // * @throws UninitializedException
    // * if either the shader Program or FBO used in this method are
    // * uninitialized before use.
    // */
    // private void renderAxes(GL3 gl, MatF4 mv, ShaderProgram program) throws
    // UninitializedException {
    // // Stage the Perspective and Modelview matrixes in the ShaderProgram.
    // program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
    // program.setUniformMatrix("MVMatrix", mv);
    //
    // // Stage the Color vector in the ShaderProgram.
    // program.setUniformVector("Color", new VecF4(1f, 0f, 0f, 1f));
    //
    // // Load all staged variables into the GPU, check for errors and
    // // omissions.
    // program.use(gl);
    // // Call the model's draw method, this links the model's VertexBuffer to
    // // the ShaderProgram and then calls the OpenGL draw method.
    // xAxis.draw(gl, program);
    //
    // // Do this 2 more times, with different colors and models.
    // program.setUniformVector("Color", new VecF4(0f, 1f, 0f, 1f));
    // program.use(gl);
    // yAxis.draw(gl, program);
    //
    // program.setUniformVector("Color", new VecF4(0f, 0f, 1f, 1f));
    // program.use(gl);
    // zAxis.draw(gl, program);
    // }

    // /**
    // * Renders the histogram.
    // *
    // * @param gl
    // * The current openGL instance.
    // * @param mv
    // * The current modelview matrix.
    // * @param program
    // * The {@link ShaderProgram} to use for rendering.
    // * @throws UninitializedException
    // */
    // private void renderHistogram(GL3 gl, MatF4 mv, ShaderProgram program)
    // throws UninitializedException {
    // // Stage the Perspective and Modelview matrixes in the ShaderProgram.
    // program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
    // program.setUniformMatrix("MVMatrix", mv.mul(MatrixFMath.translate(1, 0,
    // 0)));
    //
    // // Load all staged variables into the GPU, check for errors and
    // // omissions.
    // program.use(gl);
    //
    // MapPoint mp = dr.getMapPoint();
    //
    // int type = mp.getLandType(), max = 0;
    // if (type != Integer.MIN_VALUE) {
    // int[] oldHistData = histData;
    // histData = new int[oldHistData.length];
    //
    // float[] scaledData = new float[oldHistData.length];
    // for (int i = 0; i < oldHistData.length; i++) {
    // if (type == i) {
    // histData[i] = oldHistData[i] + 1;
    // } else {
    // histData[i] = oldHistData[i];
    // }
    // if (histData[i] > max) {
    // max = histData[i];
    // }
    // }
    //
    // for (int i = 0; i < oldHistData.length; i++) {
    // scaledData[i] = (float) histData[i] / (float) max;
    // }
    //
    // hist.setValues(gl, scaledData);
    // }
    //
    // hist.drawBars(gl, program);
    // }
    //
    // /**
    // * Renders the line graph.
    // *
    // * @param gl
    // * The current openGL instance.
    // * @param mv
    // * The current modelview matrix.
    // * @param program
    // * The {@link ShaderProgram} to use for rendering.
    // * @throws UninitializedException
    // */
    // private void renderLineGraph(GL3 gl, MatF4 mv, ShaderProgram program)
    // throws UninitializedException {
    // // Stage the Perspective and Modelview matrixes in the ShaderProgram.
    // program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
    //
    // MapPoint mp = dr.getMapPoint();
    //
    // if (mp != null) {
    // lineGraph.addData(mp.getLandType(), mp.getHeight(), 1f);
    // lineGraph.init(gl);
    // }
    //
    // ModelViewStack mvStack = new ModelViewStack();
    // mvStack.putTop(MatrixFMath.translate(-1f, 0f, -1f));
    //
    // lineGraph.draw(gl, mv, mvStack, program);
    // }
    //
    // /**
    // * Renders the bezier graph.
    // *
    // * @param gl
    // * The current openGL instance.
    // * @param mv
    // * The current modelview matrix.
    // * @param program
    // * The {@link ShaderProgram} to use for rendering.
    // * @throws UninitializedException
    // */
    // private void renderBezierGraph(GL3 gl, MatF4 mv, ShaderProgram program)
    // throws UninitializedException {
    // // Stage the Perspective and Modelview matrixes in the ShaderProgram.
    // program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
    //
    // MapPoint mp = dr.getMapPoint();
    // if (mp != null) {
    // bezierGraph.addData(mp.getLandType(), mp.getLatitude(), 1f);
    // bezierGraph.init(gl);
    // }
    //
    // ModelViewStack mvStack = new ModelViewStack();
    // mvStack.putTop(MatrixFMath.translate(-1f, 0f, 0f));
    //
    // bezierGraph.draw(gl, mv, mvStack, program);
    // }

    /**
     * Renders the scatterplot.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     */
    private void renderScatterplot(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix",
                mv.mul(MatrixFMath.translate(-0.5f, 0, 4.5f)).mul(MatrixFMath.rotationX(-90)));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);

        scat = scatBuilder.getScatterPlot(gl);
        if (scat != null) {
            scat.draw(gl, program);
        }
    }

    /**
     * Renders the visual grid.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     */
    private void renderVisualGrid(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix",
                mv.mul(MatrixFMath.rotationX(180)).mul(MatrixFMath.rotationY(90)).mul(MatrixFMath.rotationZ(180)));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);

        try {
            visualGrid.draw(gl, program);
        } catch (UninitializedException e) {
            LOGGER.debug("uninitialized, but expected if < 5 : " + e);
        }
    }

    // The reshape method is automatically called by the openGL animator if the
    // window holding the OpenGL 'canvas' is resized.
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        // Get the Opengl context from the drawable, and make it current, so
        // we can see it and draw on it. I've never seen this fail, but there is
        // error checking anyway.
        contextOn(drawable);

        // Once we have the context current, we can extract the OpenGL instance
        // from it. We have defined a OpenGL 3.0 instance in the
        // NeonNewtWindow by adding the line
        // "glp = GLProfile.get(GLProfile.GL3);"
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // set the new canvas size and aspect ratio in the global variables.
        canvasWidth = GLContext.getCurrent().getGLDrawable().getWidth();
        canvasHeight = GLContext.getCurrent().getGLDrawable().getHeight();
        setAspect((float) canvasWidth / (float) canvasHeight);

        gl.glViewport(0, 0, canvasWidth, canvasHeight);

        // Resize the PixelBuffer Object that can be used for screenshots.
        finalPBO.delete(gl);
        finalPBO = new IntPBO(w, h);
        finalPBO.init(gl);

        // Release the context.
        contextOff(drawable);
    }

    // This dispose method is called when the OpenGL 'canvas' is destroyed. It
    // is used for cleanup.
    @Override
    public void dispose(GLAutoDrawable drawable) {
        // Get the Opengl context from the drawable, and make it current, so
        // we can see it and draw on it. I've never seen this fail, but there is
        // error checking anyway.
        contextOn(drawable);

        // Once we have the context current, we can extract the OpenGL instance
        // from it. We have defined a OpenGL 3.0 instance in the
        // NeonNewtWindow by adding the line
        // "glp = GLProfile.get(GLProfile.GL3);"
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // Delete the FramBuffer Objects.
        finalPBO.delete(gl);

        // Let the ShaderProgramLoader clean up. This deletes all of the
        // ShaderProgram instances as well.
        try {
            getLoader().cleanup(gl);
        } catch (UninitializedException e1) {
            e1.printStackTrace();
        }

        // Release the context.
        contextOff(drawable);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
