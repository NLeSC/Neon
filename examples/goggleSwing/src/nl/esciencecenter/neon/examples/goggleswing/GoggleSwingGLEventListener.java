package nl.esciencecenter.neon.examples.goggleswing;

import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.neon.NeonGLEventListener;
import nl.esciencecenter.neon.datastructures.FrameBufferObject;
import nl.esciencecenter.neon.datastructures.IntPixelBufferObject;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.input.InputHandler;
import nl.esciencecenter.neon.math.Color4;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.models.Axis;
import nl.esciencecenter.neon.models.Model;
import nl.esciencecenter.neon.models.Quad;
import nl.esciencecenter.neon.shaders.ShaderProgram;
import nl.esciencecenter.neon.text.MultiColorText;

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
public class GoggleSwingGLEventListener extends NeonGLEventListener {
    // Two example shader program definitions.
    private ShaderProgram axesShaderProgram, textShaderProgram, postprocessShader;

    // Example framebuffer objects for rendering to textures.
    private FrameBufferObject axesFBO, hudFBO;

    // Model definitions, the quad is necessary for Full-screen rendering. The
    // axes are the model we wish to render (example)
    private Quad FSQ_postprocess;
    private Model xAxis, yAxis, zAxis;

    // Global (singleton) settings instance.
    private final GoggleSwingSettings settings = GoggleSwingSettings.getInstance();

    // Pixelbuffer Object, we use this to get screenshots.
    private IntPixelBufferObject finalPBO;

    // Global (singleton) inputhandler instance.
    private final GoggleSwingInputHandler inputHandler = GoggleSwingInputHandler.getInstance();

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
    final Float4Vector up = new Float4Vector(0.0f, 1.0f, 0.0f, 0.0f);

    /**
     * Basic constructor for GoggleSwingGLEventListener.
     */
    public GoggleSwingGLEventListener() {
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
            axesShaderProgram = getLoader().createProgram(gl, "axes", new File("shaders/vs_axes.vp"),
                    new File("shaders/fs_axes.fp"));
            // Do the same for the text shader
            textShaderProgram = getLoader().createProgram(gl, "text", new File("shaders/vs_multiColorTextShader.vp"),
                    new File("shaders/fs_multiColorTextShader.fp"));

            // Same for the postprocessing shader.
            postprocessShader = getLoader().createProgram(gl, "postProcess", new File("shaders/vs_postprocess.vp"),
                    new File("shaders/fs_examplePostprocess.fp"));
        } catch (final Exception e) {
            // If compilation fails, we will output the error message and quit
            // the application.
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Here we define the Axis models, and initialize them.
        xAxis = new Axis(new Float3Vector(-1f, 0f, 0f), new Float3Vector(1f, 0f, 0f), .1f, .02f);
        xAxis.init(gl);
        yAxis = new Axis(new Float3Vector(0f, -1f, 0f), new Float3Vector(0f, 1f, 0f), .1f, .02f);
        yAxis.init(gl);
        zAxis = new Axis(new Float3Vector(0f, 0f, -1f), new Float3Vector(0f, 0f, 1f), .1f, .02f);
        zAxis.init(gl);

        // Here we implement some text to show on the Heads-Up-Display (HUD),
        // which is another term for an interface that doesn't move with the
        // scene.
        String text = "Example text";
        hudText = new MultiColorText(gl, getFont(), text, Color4.WHITE, fontSize);
        hudText.init(gl);

        // Here we define the Full screen quad model (for postprocessing), and
        // initialize it.
        FSQ_postprocess = new Quad(2, 2, new Float3Vector(0, 0, 0.1f));
        FSQ_postprocess.init(gl);

        // Here we define some intermediate-step full screen textures (which are
        // needed for post processing), done with FrameBufferObjects, so we can
        // render directly to them.
        axesFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE0);
        hudFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE1);
        axesFBO.init(gl);
        hudFBO.init(gl);

        // Here we define a PixelBufferObject, which is used for getting
        // screenshots.
        finalPBO = new IntPixelBufferObject(canvasWidth, canvasHeight);
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
        // NeonNewtWindow by adding the line
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // First, we clear the buffer to start with a clean slate to draw on.
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Construct a modelview matrix out of camera viewpoint and angle.
        Float4Matrix modelViewMatrix = FloatMatrixMath.lookAt(eye, at, up);

        // Translate the camera backwards according to the inputhandler's view
        // distance setting.
        modelViewMatrix = modelViewMatrix.mul(FloatMatrixMath.translate(new Float3Vector(0f, 0f, inputHandler.getViewDist())));

        // Rotate tha camera according to the rotation angles defined in the
        // inputhandler.
        modelViewMatrix = modelViewMatrix.mul(FloatMatrixMath.rotationX(inputHandler.getRotation().getX()));
        modelViewMatrix = modelViewMatrix.mul(FloatMatrixMath.rotationY(inputHandler.getRotation().getY()));
        modelViewMatrix = modelViewMatrix.mul(FloatMatrixMath.rotationZ(inputHandler.getRotation().getZ()));

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

        // Render the FrameBufferObject's to screen, doing any post-processing actions that
        // might be wanted.
        renderTexturesToScreen(gl, canvasWidth, canvasHeight);

        // Make a screenshot, when wanted. The PBO copies the current
        // framebuffer. We then set the state back because we dont want to make
        // a screenshot 60 times a second.
        if (screenshotWanted) {
            finalPBO.makeScreenshotPNG(gl, settings.getScreenshotFileName());

            screenshotWanted = false;
        }

        contextOff(drawable);
    }

    private Float4Matrix makePerspectiveMatrix() {
        return FloatMatrixMath.perspective(getFovy(), getAspect(), getzNear(), getzFar());
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
    private void renderScene(GL3 gl, Float4Matrix mv) {
        try {
            renderAxes(gl, new Float4Matrix(mv), axesShaderProgram, axesFBO);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Axes rendering method. This assumes rendering to an {@link FrameBufferObject}. This is
     * not a necessity, but it allows for post processing.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param target
     *            The {@link ShaderProgram} to use for rendering.
     * @param target
     *            The target {@link FrameBufferObject} to render to.
     * @throws UninitializedException
     *             if either the shader Program or FrameBufferObject used in this method are
     *             uninitialized before use.
     */
    private void renderAxes(GL3 gl, Float4Matrix mv, ShaderProgram program, FrameBufferObject target) throws UninitializedException {
        // Bind the FrameBufferObject so we can start rendering to it
        target.bind(gl);
        // Clear the renderbuffer to start with a clean (black) slate
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv);

        // Stage the Color vector in the ShaderProgram.
        program.setUniformVector("Color", new Float4Vector(1f, 0f, 0f, 1f));

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        xAxis.draw(gl, program);

        // Do this 2 more times, with different colors and models.
        program.setUniformVector("Color", new Float4Vector(0f, 1f, 0f, 1f));
        program.use(gl);
        yAxis.draw(gl, program);

        program.setUniformVector("Color", new Float4Vector(0f, 0f, 1f, 1f));
        program.use(gl);
        zAxis.draw(gl, program);

        // Unbind the FrameBufferObject, making it available for texture
        // extraction.
        target.unBind(gl);
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
     *            The target {@link FrameBufferObject} to render to.
     * @throws UninitializedException
     *             if the FrameBufferObject used in this method is uninitialized before use.
     */
    private void renderHUDText(GL3 gl, Float4Matrix mv, ShaderProgram program, FrameBufferObject target) throws UninitializedException {
        // Set a new text for the string
        String randomString = "Basic Test, random: " + Math.random();
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
        postprocessShader.setUniform("axesTexture", axesFBO.getTexture().getMultitexNumber());
        postprocessShader.setUniform("hudTexture", hudFBO.getTexture().getMultitexNumber());

        // Stage the Perspective and Modelview matrixes in the ShaderProgram.
        // Because we want to render at point-blank range in this stage, we set
        // these to identity matrices.
        postprocessShader.setUniformMatrix("MVMatrix", new Float4Matrix());
        postprocessShader.setUniformMatrix("PMatrix", new Float4Matrix());

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
        // NeonNewtWindow by adding the line
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
        axesFBO.delete(gl);
        hudFBO.delete(gl);

        axesFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE0);
        hudFBO = new FrameBufferObject(canvasWidth, canvasHeight, GL.GL_TEXTURE1);

        axesFBO.init(gl);
        hudFBO.init(gl);

        // Resize the PixelBuffer Object that can be used for screenshots.
        finalPBO.delete(gl);
        finalPBO = new IntPixelBufferObject(w, h);
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
        // glp = GLProfile.get(GLProfile.GL3);
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // Delete the FramBuffer Objects.
        axesFBO.delete(gl);
        finalPBO.delete(gl);

        // Let the ShaderProgramLoader clean up. This deletes all of the
        // ShaderProgram instances as well.
        try {
            getLoader().cleanup(gl);
        } catch (UninitializedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Release the context.
        contextOff(drawable);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
