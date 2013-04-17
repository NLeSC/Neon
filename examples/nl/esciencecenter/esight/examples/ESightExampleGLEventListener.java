package nl.esciencecenter.esight.examples;

import java.io.File;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;

import nl.esciencecenter.esight.ESightGLEventListener;
import nl.esciencecenter.esight.datastructures.FBO;
import nl.esciencecenter.esight.datastructures.IntPBO;
import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.models.Axis;
import nl.esciencecenter.esight.models.Model;
import nl.esciencecenter.esight.models.Quad;
import nl.esciencecenter.esight.shaders.ShaderProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright [2013] [Netherlands eScience Center]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
public class ESightExampleGLEventListener extends ESightGLEventListener {
    private final static Logger logger = LoggerFactory
            .getLogger(ESightExampleGLEventListener.class);

    private ShaderProgram axesShaderProgram, postprocessShader;

    private FBO axesFBO;

    private Quad FSQ_postprocess;
    private Model xAxis, yAxis, zAxis;

    private final ESightExampleSettings settings = ESightExampleSettings
            .getInstance();

    private IntPBO finalPBO;

    private final ESightExampleInputHandler inputHandler;
    private boolean screenshotWanted;

    public ESightExampleGLEventListener(ESightExampleInputHandler inputHandler) {
        super();

        this.inputHandler = inputHandler;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT)
                    && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }
        final GL3 gl = GLContext.getCurrentGL().getGL3();

        final int width = GLContext.getCurrent().getGLDrawable().getWidth();
        final int height = GLContext.getCurrent().getGLDrawable().getHeight();
        this.aspect = (float) width / (float) height;

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        final Point4 eye = new Point4(
                (float) (radius * Math.sin(ftheta) * Math.cos(phi)),
                (float) (radius * Math.sin(ftheta) * Math.sin(phi)),
                (float) (radius * Math.cos(ftheta)), 1.0f);
        final Point4 at = new Point4(0.0f, 0.0f, 0.0f, 1.0f);
        final VecF4 up = new VecF4(0.0f, 1.0f, 0.0f, 0.0f);

        MatF4 modelViewMatrix = MatrixFMath.lookAt(eye, at, up);
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath.translate(new VecF3(
                0f, 0f, inputHandler.getViewDist())));

        modelViewMatrix = modelViewMatrix.mul(MatrixFMath
                .rotationX(inputHandler.getRotation().get(0)));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath
                .rotationY(inputHandler.getRotation().get(1)));
        modelViewMatrix = modelViewMatrix.mul(MatrixFMath
                .rotationZ(inputHandler.getRotation().get(2)));

        renderScene(gl, modelViewMatrix.clone());

        renderTexturesToScreen(gl, width, height);

        if (screenshotWanted) {
            finalPBO.makeScreenshotPNG(gl, settings.getScreenshotFileName());

            screenshotWanted = false;
        }

        try {
            drawable.getContext().release();
        } catch (final GLException e) {
            e.printStackTrace();
        }

    }

    private void renderScene(GL3 gl, MatF4 mv) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        try {
            renderAxes(gl, mv.clone(), axesFBO);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    private void renderAxes(GL3 gl, MatF4 mv, FBO target)
            throws UninitializedException {
        target.bind(gl);
        gl.glClear(GL.GL_DEPTH_BUFFER_BIT | GL.GL_COLOR_BUFFER_BIT);

        final MatF4 p = MatrixFMath.perspective(fovy, aspect, zNear, zFar);
        axesShaderProgram.setUniformMatrix("PMatrix", p);
        axesShaderProgram.setUniformMatrix("MVMatrix", mv);

        axesShaderProgram.setUniformVector("Color", new VecF4(1f, 0f, 0f, 1f));
        axesShaderProgram.use(gl);
        xAxis.draw(gl, axesShaderProgram);

        axesShaderProgram.setUniformVector("Color", new VecF4(0f, 1f, 0f, 1f));
        axesShaderProgram.use(gl);
        yAxis.draw(gl, axesShaderProgram);

        axesShaderProgram.setUniformVector("Color", new VecF4(0f, 0f, 1f, 1f));
        axesShaderProgram.use(gl);
        zAxis.draw(gl, axesShaderProgram);

        target.unBind(gl);
    }

    private void renderTexturesToScreen(GL3 gl, int width, int height) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        postprocessShader.setUniform("axesTexture", axesFBO.getTexture()
                .getMultitexNumber());

        postprocessShader.setUniformMatrix("MVMatrix", new MatF4());
        postprocessShader.setUniformMatrix("PMatrix", new MatF4());

        postprocessShader.setUniform("scrWidth", width);
        postprocessShader.setUniform("scrHeight", height);

        try {
            postprocessShader.use(gl);

            FSQ_postprocess.draw(gl, postprocessShader);
        } catch (final UninitializedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        final GL3 gl = drawable.getGL().getGL3();
        axesFBO.delete(gl);
        axesFBO = new FBO(w, h, GL.GL_TEXTURE0);
        axesFBO.init(gl);

        finalPBO.delete(gl);
        finalPBO = new IntPBO(w, h);
        finalPBO.init(gl);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        final GL3 gl = drawable.getGL().getGL3();

        axesFBO.delete(gl);
        finalPBO.delete(gl);

        loader.cleanup(gl);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT)
                    && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }

        final GL3 gl = drawable.getGL().getGL3();
        final int width = GLContext.getCurrent().getGLDrawable().getWidth();
        final int height = GLContext.getCurrent().getGLDrawable().getHeight();

        // Anti-Aliasing
        gl.glEnable(GL3.GL_LINE_SMOOTH);
        gl.glHint(GL3.GL_LINE_SMOOTH_HINT, GL3.GL_NICEST);
        gl.glEnable(GL3.GL_POLYGON_SMOOTH);
        gl.glHint(GL3.GL_POLYGON_SMOOTH_HINT, GL3.GL_NICEST);

        // Depth testing
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glClearDepth(1.0f);

        // Culling
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);

        // Enable Blending (needed for both Transparency and
        // Anti-Aliasing
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);

        // Enable Vertical Sync
        gl.setSwapInterval(1);

        // Set black background
        gl.glClearColor(0f, 0f, 0f, 0f);

        // enable setting of point sizes
        gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);

        // Load and compile shaders, then use program.
        try {
            axesShaderProgram = loader.createProgram(gl, "axes", new File(
                    "shaders/vs_axes.vp"), new File("shaders/fs_axes.fp"));
            postprocessShader = loader.createProgram(gl, "postProcess",
                    new File("shaders/vs_postprocess.vp"), new File(
                            "shaders/fs_examplePostprocess.fp"));
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // AXES
        xAxis = new Axis(new VecF3(-1f, 0f, 0f), new VecF3(1f, 0f, 0f), .1f,
                .02f);
        xAxis.init(gl);
        yAxis = new Axis(new VecF3(0f, -1f, 0f), new VecF3(0f, 1f, 0f), .1f,
                .02f);
        yAxis.init(gl);
        zAxis = new Axis(new VecF3(0f, 0f, -1f), new VecF3(0f, 0f, 1f), .1f,
                .02f);
        zAxis.init(gl);

        // FULL SCREEN QUADS
        FSQ_postprocess = new Quad(2, 2, new VecF3(0, 0, 0.1f));
        FSQ_postprocess.init(gl);

        // Full screen textures (for post processing) done with FBO's
        axesFBO = new FBO(width, height, GL.GL_TEXTURE0);

        axesFBO.init(gl);

        finalPBO = new IntPBO(width, height);
        finalPBO.init(gl);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
