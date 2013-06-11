package nl.esciencecenter.esight;

import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;

import nl.esciencecenter.esight.input.InputHandler;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;
import nl.esciencecenter.esight.shaders.ShaderProgramLoader;
import nl.esciencecenter.esight.text.jogampExperimental.Font;
import nl.esciencecenter.esight.text.jogampExperimental.FontFactory;

/* Copyright 2013 Netherlands eScience Center
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
 * Common (extendible) class for OpenGL event listeners, providing several
 * helper methods.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class ESightGLEventListener implements GLEventListener {
    /** General radius variable needed for lookAt method */
    protected final float radius = 1.0f;
    /** General ftheta variable needed for lookAt method */
    protected final float ftheta = 0.0f;
    /** General phi variable needed for lookAt method */
    protected final float phi = 0.0f;

    /**
     * General Field of View Y-direction variable needed for a default
     * perspective
     */
    protected final float fovy = 45.0f;
    /** General Near clipping plane variable needed for a default perspective */
    protected final float zNear = 0.1f;
    /** General Far clipping plane variable needed for a default perspective */
    protected final float zFar = 3000.0f;

    /**
     * A default implementation of the ProgramLoader, needed for programmable
     * shader functionality
     */
    protected final ShaderProgramLoader loader;

    /**
     * Aspect ratio variable, normally set by the reshape function
     */
    protected float aspect;

    /** Ubuntu fontset is used for HUD elements */
    protected int fontSet = FontFactory.UBUNTU;
    /** font is used for HUD elements @see fontSet */
    protected Font font;

    /**
     * This variable is used (among others) in the lookAt helper function to
     * define the ModelView matrix, if no inputHandler was specified when
     * constructing this class.
     */
    protected float inputRotationX;
    /**
     * This variable is used (among others) in the lookAt helper function to
     * define the ModelView matrix, if no inputHandler was specified when
     * constructing this class.
     */
    protected float inputRotationY;
    /**
     * This variable is used (among others) in the lookAt helper function to
     * define the ModelView matrix, if no inputHandler was specified when
     * constructing this class.
     */
    protected float inputViewDistance;

    /**
     * This inputHandler is used to define the Modelview Matrix in the lookAt
     * helper function if it is specified upon constructing this class.
     */
    protected InputHandler inputHandler;

    /**
     * Creates a new GLEventListener
     */
    public ESightGLEventListener() {
        this.loader = new ShaderProgramLoader();
        this.font = FontFactory.get(fontSet).getDefault();
    }

    /**
     * Creates a new GLEventListener with a predefined inputHandler
     * 
     * @param inputHandler
     *            A predefined Input event Handler (example available in
     *            nl.esciencecenter.visualization.openglCommon.input)
     */
    public ESightGLEventListener(InputHandler inputHandler) {
        this.loader = new ShaderProgramLoader();
        this.font = FontFactory.get(fontSet).getDefault();

        this.inputHandler = inputHandler;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT) && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }

        // if (drawable.getGLProfile().isGL3()) {

        final GL3 gl = drawable.getGL().getGL3();

        // Enable Anti-Aliasing
        gl.glEnable(GL3.GL_LINE_SMOOTH);
        gl.glHint(GL3.GL_LINE_SMOOTH_HINT, GL3.GL_NICEST);
        gl.glEnable(GL3.GL_POLYGON_SMOOTH);
        gl.glHint(GL3.GL_POLYGON_SMOOTH_HINT, GL3.GL_NICEST);

        // Enable Depth testing
        gl.glEnable(GL3.GL_DEPTH_TEST);
        gl.glDepthFunc(GL3.GL_LEQUAL);
        gl.glClearDepth(1.0f);

        // Enable Culling
        gl.glEnable(GL3.GL_CULL_FACE);
        gl.glCullFace(GL3.GL_BACK);

        // Enable Blending (needed for both Transparency and Anti-Aliasing
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL3.GL_BLEND);

        // Enable Vertical Sync
        gl.setSwapInterval(1);

        // Set black background
        gl.glClearColor(0f, 0f, 0f, 0f);

        // } else {
        // // First, init the 'normal' context
        // GL2ES2 gl = drawable.getGL().getGL2ES2();
        //
        // // Enable Anti-Aliasing
        // gl.glEnable(GL2ES2.GL_LINE_SMOOTH);
        // gl.glHint(GL2ES2.GL_LINE_SMOOTH_HINT, GL2ES2.GL_NICEST);
        //
        // // Enable Depth testing
        // gl.glEnable(GL2ES2.GL_DEPTH_TEST);
        // gl.glDepthFunc(GL2ES2.GL_LEQUAL);
        // gl.glClearDepth(1.0f);
        //
        // // Enable Culling
        // gl.glEnable(GL2ES2.GL_CULL_FACE);
        // gl.glCullFace(GL2ES2.GL_BACK);
        //
        // // Enable Blending (needed for both Transparency and Anti-Aliasing
        // gl.glBlendFunc(GL2ES2.GL_SRC_ALPHA, GL2ES2.GL_ONE_MINUS_SRC_ALPHA);
        // gl.glEnable(GL2ES2.GL_BLEND);
        //
        // // Enable Vertical Sync
        // gl.setSwapInterval(1);
        //
        // // Set black background
        // gl.glClearColor(0f, 0f, 0f, 0f);
        // }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT) && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }

        // if (drawable.getGLProfile().isGL3()) {
        final GL3 gl = drawable.getContext().getGL().getGL3();
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        // } else {
        // final GL2ES2 gl = drawable.getContext().getGL().getGL2ES2();
        // gl.glClear(GL2ES2.GL_COLOR_BUFFER_BIT | GL2ES2.GL_DEPTH_BUFFER_BIT);
        // }
    }

    /**
     * A helper function that generates a ModelView Matrix based on either the
     * current inputRotationX, inputRotationY and inputViewDistance, or, if an
     * Input event Handler was defined by the constructor, on the viewDist and
     * rotation defined therein. Also uses the predifined radius, ftheta and phi
     * global variables.
     * 
     * @return A new Modelview Matrix that defines a rotated and translated view
     *         at coordinates (0,0,0).
     */
    public MatF4 lookAt() {
        Point4 eye = new Point4((float) (radius * Math.sin(ftheta) * Math.cos(phi)),
                (float) (radius * Math.sin(ftheta) * Math.sin(phi)), (float) (radius * Math.cos(ftheta)), 1.0f);
        Point4 at = new Point4(0.0f, 0.0f, 0.0f, 1.0f);
        VecF4 up = new VecF4(0.0f, 1.0f, 0.0f, 0.0f);

        MatF4 mv = MatrixFMath.lookAt(eye, at, up);

        if (inputHandler != null) {
            mv = mv.mul(MatrixFMath.translate(new VecF3(0f, 0f, inputViewDistance)));
            mv = mv.mul(MatrixFMath.rotationX(inputRotationX));
            mv = mv.mul(MatrixFMath.rotationY(inputRotationY));
        } else {
            mv = mv.mul(MatrixFMath.translate(new VecF3(0f, 0f, inputHandler.getViewDist())));
            mv = mv.mul(MatrixFMath.rotationX(inputHandler.getRotation().get(0)));
            mv = mv.mul(MatrixFMath.rotationY(inputHandler.getRotation().get(1)));
        }

        return mv;
    }

    /**
     * A helper function that generates a Perspective Matrix. Uses the fovy,
     * aspect, zNear and zFar global variables.
     * 
     * @return A new Perspective Matrix that defines a common perspective
     *         frustum.
     */
    public MatF4 perspective() {
        return MatrixFMath.perspective(fovy, aspect, zNear, zFar);

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT) && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }

        // if (drawable.getGLProfile().isGL3()) {
        final GL3 gl = drawable.getContext().getGL().getGL3();

        int width = drawable.getWidth();
        int height = drawable.getHeight();
        aspect = (float) width / (float) height;

        gl.glViewport(0, 0, width, height);
        // } else {
        // final GL2ES2 gl = drawable.getContext().getGL().getGL2ES2();
        //
        // int width = drawable.getWidth();
        // int height = drawable.getHeight();
        // aspect = (float) width / (float) height;
        //
        // gl.glViewport(0, 0, width, height);
        // gl.glViewport(0, 0, w, h);
        // }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        try {
            final int status = drawable.getContext().makeCurrent();
            if ((status != GLContext.CONTEXT_CURRENT) && (status != GLContext.CONTEXT_CURRENT_NEW)) {
                System.err.println("Error swapping context to onscreen.");
            }
        } catch (final GLException e) {
            System.err.println("Exception while swapping context to onscreen.");
            e.printStackTrace();
        }

        // if (drawable.getGLProfile().isGL3()) {
        final GL3 gl = drawable.getGL().getGL3();

        loader.cleanup(gl);
        // } else {
        // GL2ES2 gl = drawable.getGL().getGL2ES2();
        //
        // loader.cleanup(gl);
        // }
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @return the inputRotationX
     */
    public float getInputRotationX() {
        return inputRotationX;
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @param inputRotationX
     *            the inputRotationX to set
     */
    public void setInputRotationX(float inputRotationX) {
        this.inputRotationX = inputRotationX;
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @return the inputRotationY
     */
    public float getInputRotationY() {
        return inputRotationY;
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @param inputRotationY
     *            the inputRotationY to set
     */
    public void setInputRotationY(float inputRotationY) {
        this.inputRotationY = inputRotationY;
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @return the inputViewDistance
     */
    public float getInputViewDistance() {
        return inputViewDistance;
    }

    /**
     * Variable used (among others) to define the ModelView matrix in the lookAt
     * helper function if no inputHandler was specified when constructing this
     * class.
     * 
     * @param inputViewDistance
     *            the inputViewDistance to set
     */
    public void setInputViewDistance(float inputViewDistance) {
        this.inputViewDistance = inputViewDistance;
    }
}
