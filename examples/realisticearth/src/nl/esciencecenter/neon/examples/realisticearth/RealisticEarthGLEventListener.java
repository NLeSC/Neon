package nl.esciencecenter.neon.examples.realisticearth;

import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;

import nl.esciencecenter.neon.NeonGLEventListener;
import nl.esciencecenter.neon.exceptions.UninitializedException;
import nl.esciencecenter.neon.input.InputHandler;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Point4;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;
import nl.esciencecenter.neon.models.GeoSphere;
import nl.esciencecenter.neon.models.InvertedGeoSphere;
import nl.esciencecenter.neon.shaders.ShaderProgram;
import nl.esciencecenter.neon.textures.Texture2D;

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
 * Example implementation of a NeonGLEventListener, built to render a
 * semi-realistic earth using texture maps.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class RealisticEarthGLEventListener extends NeonGLEventListener {
    // Two example shader program definitions.
    private ShaderProgram                    shaderProgram_Universe, shaderProgram_Earth, shaderProgram_Atmosphere;

    // Model definitions, the quad is necessary for Full-screen rendering. The
    // axes are the model we wish to render (example)
    private GeoSphere                        geoSphere, atmSphere, moonSphere;

    private InvertedGeoSphere                universeInvertedSphere;

    // Texture definitions for the spheres
    private Texture2D                        colorTex, specularTex, cityLightsTex, cloudTex, cloudTransparencyTex,
            normalTex, universeTex, moonTex;

    // Global (singleton) settings instance.
    private final RealisticEarthSettings     settings           = RealisticEarthSettings.getInstance();

    // Global (singleton) inputhandler instance.
    private final RealisticEarthInputHandler inputHandler       = RealisticEarthInputHandler.getInstance();

    // Height and width of the drawable area. We extract this from the opengl
    // instance in the reshape method every time it is changed, but set it in
    // the init method initially. The default values are defined by the settings
    // class.
    private int                              canvasWidth, canvasHeight;

    // Variables needed to calculate the viewpoint and camera angle.
    final Point4                             eye                = new Point4(
                                                                        (float) (getRadius() * Math.sin(getFtheta()) * Math
                                                                                .cos(getPhi())),
                                                                        (float) (getRadius() * Math.sin(getFtheta()) * Math
                                                                                .sin(getPhi())),
                                                                        (float) (getRadius() * Math.cos(getFtheta())));
    final Point4                             at                 = new Point4(0.0f, 0.0f, 0.0f);
    final Float4Vector                              up                 = new Float4Vector(0.0f, 1.0f, 0.0f, 0.0f);

    long                                     time               = System.currentTimeMillis();
    double                                   totalMinutesPassed = 0.0;

    /**
     * Basic constructor for NeonExampleGLEventListener.
     */
    public RealisticEarthGLEventListener() {
        super();

        setInputRotationX(settings.getInitialRotationX());
        setInputRotationY(settings.getInitialRotationY());
        setInputViewDistance(settings.getInitialZoom());

        inputHandler.setRotation(new Float3Vector(getInputRotationX(), getInputRotationY(), 0.0f));
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
            shaderProgram_Universe = getLoader().createProgram(gl, "shaderProgram_Universe",
                    new File("shaders/vs_texture.vp"), new File("shaders/fs_texture.fp"));

            shaderProgram_Earth = getLoader().createProgram(gl, "shaderProgram_Earth",
                    new File("shaders/vs_perFragmentLighting.vp"), new File("shaders/fs_perFragmentLighting.fp"));

            shaderProgram_Atmosphere = getLoader().createProgram(gl, "shaderProgram_Atmosphere",
                    new File("shaders/vs_atmosphere.vp"), new File("shaders/fs_atmosphere.fp"));
        } catch (final Exception e) {
            // If compilation fails, we will output the error message and quit
            // the application.
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Here we define the Sphere models, and initialize them.
        geoSphere = new GeoSphere(50, 50, 13f, false);
        geoSphere.init(gl);

        atmSphere = new GeoSphere(50, 50, 13.2f, false);
        atmSphere.init(gl);

        moonSphere = new GeoSphere(50, 50, 0.273f * 13f, false);
        moonSphere.init(gl);

        universeInvertedSphere = new InvertedGeoSphere(50, 50, 500000f, false);
        universeInvertedSphere.init(gl);

        colorTex = new ImageTexture("images/Envisat_mosaic_May_-_November_2004.jpg", 0, 0, GL3.GL_TEXTURE2);
        normalTex = new ImageTexture("images/earthNormalMap_2048.png", 0, 0, GL3.GL_TEXTURE3);
        specularTex = new ImageTexture("images/Envisat_mosaic_May_-_November_2004_Specular.jpg", 0, 0, GL3.GL_TEXTURE4);
        cityLightsTex = new ImageTexture("images/earthlights1k.jpg", 0, 0, GL3.GL_TEXTURE5);
        cloudTex = new ImageTexture("images/earthcloudmap.jpg", 0, 0, GL3.GL_TEXTURE6);
        cloudTransparencyTex = new ImageTexture("images/earthcloudmaptrans.jpg", 0, 0, GL3.GL_TEXTURE7);
        universeTex = new ImageTexture("images/ESOMilkyWay.jpg", 0, 0, GL3.GL_TEXTURE8);
        moonTex = new ImageTexture("images/MoonMap_2500x1250.jpg", 0, 0, GL3.GL_TEXTURE9);

        colorTex.init(gl);
        specularTex.init(gl);
        cityLightsTex.init(gl);
        cloudTex.init(gl);
        cloudTransparencyTex.init(gl);
        normalTex.init(gl);
        universeTex.init(gl);
        moonTex.init(gl);

        inputHandler.setViewDist(-40f);

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
        // "glp = GLProfile.get(GLProfile.GL3)"
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

        // Render the scene with these modelview settings. In this case, the end
        // result of this action will be that the AxesFBO has been filled with
        // the right pixels.
        renderScene(gl, modelViewMatrix);

        // Release the context.
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
            // Bind the FrameBufferObject so we can start rendering to it

            // Clear the renderbuffer to start with a clean (black) slate
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

            long currentTime = System.currentTimeMillis();
            double timePassed = currentTime - time;
            time = currentTime;

            double minutesPassed = timePassed / 60000.0;
            totalMinutesPassed += minutesPassed;

            renderUniverse(gl, new Float4Matrix(mv), shaderProgram_Universe);
            renderGeoSphere(gl, new Float4Matrix(mv), shaderProgram_Earth);
            renderMoon(gl, new Float4Matrix(mv), shaderProgram_Universe);
            renderAtmosphere(gl, new Float4Matrix(mv), shaderProgram_Atmosphere);

            // Unbind the FrameBufferObject, making it available for texture
            // extraction.
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Universe inverse sphere rendering method.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FrameBufferObject used in this method are
     *             uninitialized before use.
     */
    private void renderUniverse(GL3 gl, Float4Matrix mv, ShaderProgram program) throws UninitializedException {

        // Stage the Perspective and Modelview matrixes in the
        // ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv);
        program.setUniformMatrix("NMatrix", FloatMatrixMath.getNormalMatrix(mv));

        // Stage the pointer to the textures
        program.setUniform("texture_map", universeTex.getMultitexNumber());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        universeInvertedSphere.draw(gl, program);
    }

    /**
     * GeoSphere rendering method. Applies the color, specular, citylights and
     * normal maps. Uses the perFragmentLighting shaders.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FrameBufferObject used in this method are
     *             uninitialized before use.
     */
    private void renderGeoSphere(GL3 gl, Float4Matrix mv, ShaderProgram program) throws UninitializedException {

        Float4Vector rawLightPos = new Float4Vector(-30.0f, 4.0f, -20.0f, 0f);
        program.setUniformVector("lightPos", rawLightPos);

        Float4Matrix yRotatedMV = mv.mul(FloatMatrixMath.rotationY((float) (totalMinutesPassed * 360f)));

        // Stage the Perspective and Modelview matrixes in the
        // ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", yRotatedMV);
        program.setUniformMatrix("NMatrix", FloatMatrixMath.getNormalMatrix(mv));

        // Stage the pointer to the textures
        program.setUniform("colorTex", colorTex.getMultitexNumber());
        program.setUniform("specularTex", specularTex.getMultitexNumber());
        program.setUniform("cityLightsTex", cityLightsTex.getMultitexNumber());
        program.setUniform("normalTex", normalTex.getMultitexNumber());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        geoSphere.draw(gl, program);
    }

    /**
     * Universe inverse sphere rendering method.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FrameBufferObject used in this method are
     *             uninitialized before use.
     */
    private void renderMoon(GL3 gl, Float4Matrix mv, ShaderProgram program) throws UninitializedException {

        // Stage the Perspective and Modelview matrixes in the
        // ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", mv.mul(FloatMatrixMath.translate(785, 0, 0)));
        program.setUniformMatrix("NMatrix", FloatMatrixMath.getNormalMatrix(mv));

        // Stage the pointer to the textures
        program.setUniform("texture_map", moonTex.getMultitexNumber());

        // Load all staged variables into the GPU, check for errors and
        // omissions.
        program.use(gl);
        // Call the model's draw method, this links the model's VertexBuffer to
        // the ShaderProgram and then calls the OpenGL draw method.
        moonSphere.draw(gl, program);
    }

    /**
     * Render the atmosphere by applying the cloud color and cloud transparency
     * maps. Uses the atmosphere shaders.
     * 
     * @param gl
     *            The current openGL instance.
     * @param mv
     *            The current modelview matrix.
     * @param program
     *            The {@link ShaderProgram} to use for rendering.
     * @throws UninitializedException
     *             if either the shader Program or FrameBufferObject used in this method are
     *             uninitialized before use.
     */
    private void renderAtmosphere(GL3 gl, Float4Matrix mv, ShaderProgram program) throws UninitializedException {

        Float4Vector rawLightPos = new Float4Vector(-30.0f, 4.0f, -20.0f, 0f);
        program.setUniformVector("lightPos", rawLightPos);

        Float4Matrix yRotatedMV = mv.mul(FloatMatrixMath.rotationY((float) (totalMinutesPassed * 360f)));

        // Stage the Perspective and Modelview matrixes in the
        // ShaderProgram.
        program.setUniformMatrix("PMatrix", makePerspectiveMatrix());
        program.setUniformMatrix("MVMatrix", yRotatedMV);
        program.setUniformMatrix("NMatrix", FloatMatrixMath.getNormalMatrix(mv));

        // Stage the pointer to the textures
        program.setUniform("cloudTex", cloudTex.getMultitexNumber());
        program.setUniform("cloudTransparencyTex", cloudTransparencyTex.getMultitexNumber());

        program.use(gl);
        atmSphere.draw(gl, program);
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
        // "glp = GLProfile.get(GLProfile.GL3)"
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // set the new canvas size and aspect ratio in the global variables.
        canvasWidth = GLContext.getCurrent().getGLDrawable().getWidth();
        canvasHeight = GLContext.getCurrent().getGLDrawable().getHeight();
        setAspect((float) canvasWidth / (float) canvasHeight);

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
        // "glp = GLProfile.get(GLProfile.GL3)"
        // Therefore, we extract a GL3 instance, so we cannot make any
        // unfortunate mistakes (calls to methods that are undefined for this
        // version).
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        // Delete Models
        geoSphere.delete(gl);

        // Let the ShaderProgramLoader clean up. This deletes all of the
        // ShaderProgram instances as well.
        try {
            getLoader().cleanup(gl);
        } catch (UninitializedException e1) {
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
