package nl.esciencecenter.esight.examples.graphs;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.esight.ESightGLEventListener;
import nl.esciencecenter.esight.datastructures.FBO;
import nl.esciencecenter.esight.datastructures.IntPBO;
import nl.esciencecenter.esight.examples.graphs.DataReader.MapPoint;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.models.Axis;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.shaders.ShaderProgram;
import nl.esciencecenter.esight.text.MultiColorText;

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
 * Example implementation of a ESightGLEventListener. Renders Axes in different
 * colors to a texture and renders then this texture to the screen.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GraphsGLEventListener extends ESightGLEventListener {
    // Two example shader program definitions.
    private ShaderProgram axesShaderProgram, textShaderProgram;

    // Model definitions, the quad is necessary for Full-screen rendering. The
    // axes are the model we wish to render (example)
    private Model xAxis, yAxis, zAxis;

    private DataReader dr;
    private Histogram2D hist;
    private int[] histData = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private LineGraph2D lineGraph;

    private ScatterPlot3D scat;

    // Global (singleton) settings instance.
    private final GraphsSettings settings = GraphsSettings.getInstance();

    // Pixelbuffer Object, we use this to get screenshots.
    private IntPBO finalPBO;

    // Global (singleton) inputhandler instance.
    private final GraphsInputHandler inputHandler = GraphsInputHandler.getInstance();

    // State keeping variable
    private boolean screenshotWanted;

    // Example of text to display on screen, and the size of the font for this.
    private MultiColorText hudText;
    private final int fontSize = 30;

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
     * Basic constructor for ESightExampleGLEventListener.
     */
    public GraphsGLEventListener() {
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
        // ESightNewtWindow by adding the line
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

        // Here we implement some text to show on the Heads-Up-Display (HUD),
        // which is another term for an interface that doesn't move with the
        // scene.
        String text = "Example text";
        hudText = new MultiColorText(gl, getFont(), text, Color4.WHITE, fontSize);
        hudText.init(gl);

        // Here we define a PixelBufferObject, which is used for getting
        // screenshots.
        finalPBO = new IntPBO(canvasWidth, canvasHeight);
        finalPBO.init(gl);

        Color4[] vegetationColors = new Color4[] { Color4.BLUE, new Color4("00755E"), new Color4("014421"),
                new Color4("008000"), Color4.GREEN, Color4.CYAN, new Color4(135, 156, 69, 255), Color4.YELLOW,
                Color4.WHITE, Color4.WHITE };
        String[] vegetationNames = new String[] { "Water", "Rain Forest", "Deciduous Forest", "Evergreen Forest",
                "Grassland", "Tundra", "Shrubland", "Desert", "Ice", "" };

        // Read data
        try {
            dr = new DataReader();

            hist = new Histogram2D(1f, 1f, new VecF3(), vegetationColors, vegetationNames);
            hist.init(gl);

            // lineGraph = new LineGraph2D(1f, 1f, 100, vegetationColors);
            // lineGraph.init(gl);

            scat = new ScatterPlot3D();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
        // ESightNewtWindow by adding the line
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
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.translate(new VecF3(0f, 0f, inputHandler.getViewDist())));

        // Rotate tha camera according to the rotation angles defined in the
        // inputhandler.
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationX(inputHandler.getRotation().getX()));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationY(inputHandler.getRotation().getY()));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.rotationZ(inputHandler.getRotation().getZ()));

        // Render the scene with these modelview settings. In this case, the end
        // result of this action will be that the AxesFBO has been filled with
        // the right pixels.
        renderScene(gl, modelViewMatrix);

        // Render the text on the Heads-Up-Display
        try {
            renderHUDText(gl, modelViewMatrix, textShaderProgram);
        } catch (UninitializedException e1) {
            e1.printStackTrace();
        }

        // Render the FBO's to screen, doing any post-processing actions that
        // might be wanted.
        // renderTexturesToScreen(gl, canvasWidth, canvasHeight);

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
            renderAxes(gl, new MatF4(mv), axesShaderProgram);

            renderScatterplot(gl, mv, textShaderProgram);

            // renderLineGraph(gl, mv, textShaderProgram);

            renderHistogram(gl, mv, axesShaderProgram);
            textShaderProgram.setUniformMatrix("PMatrix", makePerspectiveMatrix());
            hist.drawLabels(gl, mv, textShaderProgram);

            dr.next();

        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Axes rendering method. This assumes rendering to an {@link FBO}. This is
     * not a necessity, but it allows for post processing.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param target
     *            The {@link ShaderProgram} to use for rendering.
     * @param target
     *            The target {@link FBO} to render to.
     * @throws UninitializedException
     *             if either the shader Program or FBO used in this method are
     *             uninitialized before use.
     */
    private void renderAxes(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv);

        // Stage the Color vector in the ShaderProgram.
        program.setUniformVector("Color", new VecF4(1f, 0f, 0f, 1f));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        xAxis.draw(gl, program);

        // Do this 2 more times, with different colors and models.
        program.setUniformVector("Color", new VecF4(0f, 1f, 0f, 1f));
        program.use(gl);
        yAxis.draw(gl, program);

        program.setUniformVector("Color", new VecF4(0f, 0f, 1f, 1f));
        program.use(gl);
        zAxis.draw(gl, program);
    }

    /**
     * Renders the histogram.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     */
    private void renderHistogram(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv);

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);

        int type = dr.getType(), max = 0;
        int[] oldHistData = histData;
        histData = new int[oldHistData.length];

        float[] scaledData = new float[oldHistData.length];
        for (int i = 0; i < oldHistData.length; i++) {
            if (type == i) {
                histData[i] = oldHistData[i] + 1;
            } else {
                histData[i] = oldHistData[i];
            }
            if (histData[i] > max) {
                max = histData[i];
            }
        }

        for (int i = 0; i < oldHistData.length; i++) {
            scaledData[i] = (float) histData[i] / (float) max;
        }

        hist.setValues(gl, scaledData);

        hist.drawBars(gl, program);
    }

    /**
     * Renders the histogram.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     */
    private void renderLineGraph(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv.mul(MatrixFMath.rotationY(90)));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);

        lineGraph.draw(gl, program);
    }

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
        program.setUniformMatrix("MVMatrix", mv.mul(MatrixFMath.rotationY(90)).mul(MatrixFMath.rotationZ(180)));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);

        MapPoint mp = dr.getMapPoint();

        Color4 color = new Color4();
        if (mp.getLandType() == 0) {
            color = Color4.BLUE;
        } else if (mp.getLandType() == 1) {
            color = new Color4("00755E");
        } else if (mp.getLandType() == 2) {
            color = new Color4("014421");
        } else if (mp.getLandType() == 3) {
            color = new Color4("008000");
        } else if (mp.getLandType() == 4) {
            color = Color4.GREEN;
        } else if (mp.getLandType() == 5) {
            color = Color4.CYAN;
        } else if (mp.getLandType() == 6) {
            color = new Color4(135, 156, 69, 255);
        } else if (mp.getLandType() == 7) {
            color = Color4.YELLOW;
        } else if (mp.getLandType() == 8) {
            color = Color4.WHITE;
        }

        scat.add(new Point4(-mp.getLatitude() / 180f, -mp.getHeight() / 40000f, mp.getLongitude() / 360f), color);
        scat.init(gl);

        scat.draw(gl, program);
    }

    /**
     * Rendering method for text on the Heads-Up-Display (HUD). This currently
     * prints a random string in white.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param target
     *            The {@link ShaderProgram} to use for rendering.
     * @param target
     *            The target {@link FBO} to render to.
     * @throws UninitializedException
     *             if the FBO used in this method is uninitialized before use.
     */
    private void renderHUDText(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Set a new text for the string
        String randomString = "Basic Test, random: " + Math.random();
        hudText.setString(gl, randomString, Color4.WHITE, fontSize);

        // Draw the text to the renderbuffer, to (arbitrary unit) location 30x
        // 30y counted from left bottom.
        hudText.drawHudRelative(gl, program, canvasWidth, canvasHeight, 30f, 30f);
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
        // ESightNewtWindow by adding the line
        // "glp = GLProfile.get(GLProfile.GL3);"
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // set the new canvas size and aspect ratio in the global variables.
        canvasWidth = GLContext.getCurrent().getGLDrawable().getWidth();
        canvasHeight = GLContext.getCurrent().getGLDrawable().getHeight();
        setAspect((float) canvasWidth / (float) canvasHeight);

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
        // ESightNewtWindow by adding the line
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
