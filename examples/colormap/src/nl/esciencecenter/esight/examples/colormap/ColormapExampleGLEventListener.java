package nl.esciencecenter.esight.examples.colormap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.esight.ESightGLEventListener;
import nl.esciencecenter.esight.datastructures.FBO;
import nl.esciencecenter.esight.datastructures.IntPBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.models.GeoSphere;
import nl.esciencecenter.esight.models.Quad;
import nl.esciencecenter.esight.models.Sphere;
import nl.esciencecenter.esight.noise.Noise;
import nl.esciencecenter.esight.shaders.ShaderProgram;
import nl.esciencecenter.esight.swing.ColormapInterpreter;
import nl.esciencecenter.esight.swing.ColormapInterpreter.Color;
import nl.esciencecenter.esight.swing.ColormapInterpreter.Dimensions;
import nl.esciencecenter.esight.text.MultiColorText;
import nl.esciencecenter.esight.textures.ByteBufferTexture;
import nl.esciencecenter.esight.textures.ImageTexture;
import nl.esciencecenter.esight.textures.Texture2D;

import com.jogamp.common.nio.Buffers;

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
public class ColormapExampleGLEventListener extends ESightGLEventListener {
    private static final int NOISE_LONS = 2000;
    private static final int NOISE_LATS = 1000;

    // Two example shader program definitions.
    private ShaderProgram textureShaderProgram, textShaderProgram, postprocessShader;

    // Example framebuffer objects for rendering to textures.
    private FBO geoSphereFBO, sceneFBO, hudFBO;

    // Model definitions, the quad is necessary for Full-screen rendering. The
    // axes are the model we wish to render (example)
    private Quad FSQ_postprocess;
    private GeoSphere geoSphere;
    private Sphere sphere;

    // Texture definitions for the spheres
    private Texture2D sphereTex, surfaceTex, legendTex;

    // Global (singleton) settings instance.
    private final ColormapExampleSettings settings = ColormapExampleSettings.getInstance();

    // Pixelbuffer Object, we use this to get screenshots.
    private IntPBO finalPBO;

    // Global (singleton) inputhandler instance.
    private final ColormapExampleInputHandler inputHandler = ColormapExampleInputHandler.getInstance();

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

    private int cachedColormapIndex, cachedDataModeIndex, cachedVariableindex, cachedRangeSliderLowerValue,
            cachedRangeSliderUpperValue;

    // private float[][] randomdata;
    private final FloatBuffer noise = new Noise(8, NOISE_LONS, NOISE_LATS, 1).getFloats();

    /**
     * Basic constructor for ESightExampleGLEventListener.
     */
    public ColormapExampleGLEventListener() {
        super();

        setInputRotationX(settings.getInitialRotationX());
        setInputRotationY(settings.getInitialRotationY());
        setInputViewDistance(settings.getInitialZoom());

        inputHandler.setRotation(new VecF3(getInputRotationX(), getInputRotationY(), 0.0f));
        inputHandler.setViewDist(getInputViewDistance());

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
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);

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
            textureShaderProgram = getLoader().createProgram(gl, "texture", new File("shaders/vs_texture.vp"),
                    new File("shaders/fs_texture.fp"));
            // Do the same for the text shader
            textShaderProgram = getLoader().createProgram(gl, "text", new File("shaders/vs_multiColorTextShader.vp"),
                    new File("shaders/fs_multiColorTextShader.fp"));

            // Same for the postprocessing shader.
            postprocessShader = getLoader().createProgram(gl, "postProcess", new File("shaders/vs_postprocess.vp"),
                    new File("shaders/fs_ColormapExamplePostprocess.fp"));
        } catch (final Exception e) {
            // If compilation fails, we will output the error message and quit
            // the application.
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Here we define the Sphere models, and initialize them.
        geoSphere = new GeoSphere(30, 30, 50f, false);
        geoSphere.init(gl);

        sphere = new Sphere(5, false);
        sphere.init(gl);

        try {
            sphereTex = new ImageTexture("images/MoonMap_2500x1250.jpg", 0, 0, GL3.GL_TEXTURE2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sphereTex.init(gl);

        // Here we implement some text to show on the Heads-Up-Display (HUD),
        // which is another term for an interface that doesn't move with the
        // scene.
        String text = "Example text";
        hudText = new MultiColorText(gl, getFont(), text, Color4.WHITE, fontSize);
        hudText.init(gl);

        // Here we define the Full screen quad model (for postprocessing), and
        // initialize it.
        FSQ_postprocess = new Quad(2, 2, new VecF3(0, 0, 0.1f));
        FSQ_postprocess.init(gl);

        // Here we define some intermediate-step full screen textures (which are
        // needed for post processing), done with FrameBufferObjects, so we can
        // render directly to them.
        hudFBO = new FBO(canvasWidth, canvasHeight, GL.GL_TEXTURE0);
        sceneFBO = new FBO(canvasWidth, canvasHeight, GL.GL_TEXTURE1);

        hudFBO.init(gl);
        sceneFBO.init(gl);

        // Here we define a PixelBufferObject, which is used for getting
        // screenshots.
        finalPBO = new IntPBO(canvasWidth, canvasHeight);
        finalPBO.init(gl);

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
        // modelViewMatrix =
        // modelViewMatrix.mul(MatrixFMath.rotationZ(inputHandler.getRotation().get(2)));

        // Render the scene with these modelview settings. In this case, the end
        // result of this action will be that the AxesFBO has been filled with
        // the right pixels.
        renderScene(gl, modelViewMatrix);

        // Render the text on the Heads-Up-Display
        try {
            renderHUDText(gl, modelViewMatrix, textShaderProgram, hudFBO);
        } catch (UninitializedException e1) {
            e1.printStackTrace();
        }

        // Render the FBO's to screen, doing any post-processing actions that
        // might be wanted.
        renderTexturesToScreen(gl, canvasWidth, canvasHeight);

        // Make a screenshot, when wanted. The PBO copies the current
        // framebuffer. We then set the state back because we dont want to make
        // a screenshot 60 times a second.
        if (screenshotWanted) {
            finalPBO.makeScreenshotPNG(gl, settings.getScreenshotFileName());

            screenshotWanted = false;
        }

        // Release the context.
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
            rebuildColormapDependentTexturesIfNecessary(gl);

            // Bind the FrameBufferObject so we can start rendering to it
            sceneFBO.bind(gl);

            // Clear the renderbuffer to start with a clean (black) slate
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

            renderGeoSphere(gl, new MatF4(mv), textureShaderProgram);
            renderSphere(gl, new MatF4(mv), textureShaderProgram);

            // Unbind the FrameBufferObject, making it available for texture
            // extraction.
            sceneFBO.unBind(gl);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    private void rebuildColormapDependentTexturesIfNecessary(GL3 gl) {
        int colormapIndex = settings.getSelectedColormapIndex();
        int dataModeIndex = settings.getSelectedDataModeIndex();
        int variableIndex = settings.getSelectedVariableIndex();
        int rangeSliderLowerValue = settings.getRangeSliderLowerValue();
        int rangeSliderUpperValue = settings.getRangeSliderUpperValue();

        if (cachedColormapIndex != colormapIndex || cachedDataModeIndex != dataModeIndex
                || cachedVariableindex != variableIndex || cachedRangeSliderLowerValue != rangeSliderLowerValue
                || cachedRangeSliderUpperValue != rangeSliderUpperValue) {

            if (surfaceTex != null) {
                surfaceTex.delete(gl);
            }
            if (legendTex != null) {
                legendTex.delete(gl);
            }

            // re-build the textures
            String varName = settings.getSelectedVariableName();
            String colorMap = settings.getSelectedColormapName();

            Dimensions colormapDims = new Dimensions(settings.getVariableLowerBound(varName),
                    settings.getVariableUpperBound(varName));

            ByteBuffer surfaceBuffer = Buffers.newDirectByteBuffer(NOISE_LONS * NOISE_LATS * 4);
            noise.rewind();

            float noiseMin = Float.MAX_VALUE;
            float noiseMax = Float.MIN_VALUE;
            float total = 0f;
            for (int y = 0; y < NOISE_LATS; y++) {
                for (int x = 0; x < NOISE_LONS; x++) {
                    float noisePixel = noise.get();
                    total += noisePixel;
                    if (noisePixel < noiseMin) {
                        noiseMin = noisePixel;
                    }
                    if (noisePixel > noiseMax) {
                        noiseMax = noisePixel;
                    }

                    Color color = ColormapInterpreter.getColor(colorMap, colormapDims, (noisePixel / 255f), Float.NaN);
                    surfaceBuffer.put((byte) (color.getRed() * 255));
                    surfaceBuffer.put((byte) (color.getGreen() * 255));
                    surfaceBuffer.put((byte) (color.getBlue() * 255));
                    surfaceBuffer.put((byte) 0);
                }
            }
            System.out.println("min: " + noiseMin);
            System.out.println("max: " + noiseMax);
            System.out.println("avg: " + total / (NOISE_LATS * NOISE_LONS));

            surfaceBuffer.flip();

            surfaceTex = new ByteBufferTexture(GL3.GL_TEXTURE3, surfaceBuffer, NOISE_LONS, NOISE_LATS);
            surfaceTex.init(gl);
        }
    }

    /**
     * GeoSphere rendering method. This assumes rendering to an {@link FBO}.
     * This is not a necessity, but it allows for post processing.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param target
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FBO used in this method are
     *             uninitialized before use.
     */
    private void renderGeoSphere(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {

        // Stage the Perspective and Modelview matrixes in the
        // ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv);

        // Stage the pointer to the texture
        sphereTex.use(gl);
        program.setUniform("texture_map", surfaceTex.getMultitexNumber());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        geoSphere.draw(gl, program);
    }

    /**
     * GeoSphere rendering method. This assumes rendering to an {@link FBO}.
     * This is not a necessity, but it allows for post processing.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param target
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FBO used in this method are
     *             uninitialized before use.
     */
    private void renderSphere(GL3 gl, MatF4 mv, ShaderProgram program) throws UninitializedException {
        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());

        // Translate and scale the 'moon'
        mv = mv.mul(MatrixFMath.translate(100f, 0f, 0f));
        mv = mv.mul(MatrixFMath.scale(10f, 10f, 10f));
        program.setUniformMatrix("MVMatrix", mv);

        // Stage the pointer to the texture
        sphereTex.use(gl);
        program.setUniform("texture_map", sphereTex.getMultitexNumber());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        sphere.draw(gl, program);
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
    private void renderHUDText(GL3 gl, MatF4 mv, ShaderProgram program, FBO target) throws UninitializedException {
        // Set a new text for the string
        String randomString = "Colormap test, random: " + Math.random();
        hudText.setString(gl, randomString, Color4.WHITE, fontSize);

        // Bind the FrameBufferObject so we can start rendering to it
        target.bind(gl);
        // Clear the renderbuffer to start with a clean (black) slate
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

        // Draw the text to the renderbuffer, to (arbitrary unit) location 30x
        // 30y counted from left bottom.
        hudText.drawHudRelative(gl, program, canvasWidth, canvasHeight, 30f, 30f);

        // Unbind the FrameBufferObject, making it available for texture
        // extraction.
        target.unBind(gl);
    }

    /**
     * Final image composition and postprocessing method. makes use of the
     * postprocessShader
     * 
     * @param gl
     *            The current openGL instance.
     * @param width
     *            The width of the openGL 'canvas'
     * @param height
     *            The height of the openGL 'canvas'
     */
    private void renderTexturesToScreen(GL3 gl, int width, int height) {
        // Clear the renderbuffer to start with a clean (black) slate
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Stage a pointer to the Texture picturing the axes and hud
        // (extracted from their FrameBufferObjects)
        // in the postprocessing shaderprogram.
        postprocessShader.setUniform("sceneTex", sceneFBO.getTexture().getMultitexNumber());
        postprocessShader.setUniform("hudTexture", hudFBO.getTexture().getMultitexNumber());

        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        // Because we want to render at point-blank range in this stage, we set
        // these to identity matrices.
        postprocessShader.setUniformMatrix("MVMatrix", new MatF4());
        postprocessShader.setUniformMatrix("PMatrix", new MatF4());

        // Stage the width and height.
        postprocessShader.setUniform("scrWidth", width);
        postprocessShader.setUniform("scrHeight", height);

        try {
            // Load all staged variables into the GPU, check for errors and
            // omissions.
            postprocessShader.use(gl);

            // Call the model's draw method, this links the model's VertexBuffer
            // to
            // the ShaderProgram and then calls the OpenGL draw method.
            FSQ_postprocess.draw(gl, postprocessShader);
        } catch (final UninitializedException e) {
            e.printStackTrace();
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
        // ESightNewtWindow by adding the line
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // set the new canvas size and aspect ratio in the global variables.
        canvasWidth = GLContext.getCurrent().getGLDrawable().getWidth();
        canvasHeight = GLContext.getCurrent().getGLDrawable().getHeight();
        setAspect((float) canvasWidth / (float) canvasHeight);

        // Resize the FrameBuffer Objects that we use for intermediate stages.
        sceneFBO.delete(gl);
        hudFBO.delete(gl);

        hudFBO = new FBO(canvasWidth, canvasHeight, GL.GL_TEXTURE0);
        sceneFBO = new FBO(canvasWidth, canvasHeight, GL.GL_TEXTURE1);

        sceneFBO.init(gl);
        hudFBO.init(gl);

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
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // Delete the FramBuffer Objects.
        geoSphereFBO.delete(gl);
        sceneFBO.delete(gl);
        finalPBO.delete(gl);

        // Delete Models
        geoSphere.delete(gl);
        sphere.delete(gl);
        FSQ_postprocess.delete(gl);

        // Let the ShaderProgramLoader clean up. This deletes all of the
        // ShaderProgram instances as well.
        try {
            getLoader().cleanup(gl);
        } catch (UninitializedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Release the context.

        // Release the context.
        contextOff(drawable);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
