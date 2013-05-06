package nl.esciencecenter.esight.datastructures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;
import nl.esciencecenter.esight.textures.RBOTexture;
import nl.esciencecenter.esight.textures.Texture2D;

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
 * Framebuffer object, used for rendering images without directly outputting
 * them to the screen.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class FBO {
    private final static Logger logger = LoggerFactory.getLogger(FBO.class);

    /** OpenGL internal pointer to the framebuffer */
    private final IntBuffer fboPointer;
    /** OpenGL internal pointer to the renderbuffer */
    private final IntBuffer rboPointer;
    /** OpenGL internal pointer to the renderbuffer texture (storage) */
    private final RBOTexture rboTexture;

    /** The width and height for this framebuffer */
    private final int width, height;

    /** Object-internal state-keeping */
    private boolean initialized = false;

    /**
     * Basic constructor for FBO.
     * 
     * @param width
     *            The width for this framebuffer object and it's final output
     *            texture.
     * @param height
     *            The height for this framebuffer object and it's final output
     *            texture.
     * @param glMultitexUnit
     *            The OpenGL-internal multitexture unit to be associated with
     *            this FBO's storage texture.
     */
    public FBO(int width, int height, int glMultitexUnit) {
        this.width = width;
        this.height = height;

        fboPointer = IntBuffer.allocate(1);
        rboPointer = IntBuffer.allocate(1);

        rboTexture = new RBOTexture(width, height, glMultitexUnit);
    }

    /**
     * OpenGL initialization method. Call this before using this FBO.
     * 
     * @param gl
     *            The opengl instance.
     */
    public void init(GL3 gl) {
        try {
            checkNoError(gl, "PRE: ", true);

            // Create and bind frame buffer
            gl.glGenFramebuffers(1, fboPointer);
            gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fboPointer.get(0));

            // Setup texture and color buffer
            rboTexture.init(gl);
            rboTexture.use(gl);
            gl.glFramebufferTexture2D(GL3.GL_FRAMEBUFFER,
                    GL3.GL_COLOR_ATTACHMENT0, GL3.GL_TEXTURE_2D,
                    rboTexture.getPointer(), 0);
            rboTexture.unBind(gl);

            // Setup the depth buffer
            gl.glGenRenderbuffers(1, rboPointer);
            gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, rboPointer.get(0));
            gl.glRenderbufferStorage(GL.GL_RENDERBUFFER,
                    GL3.GL_DEPTH_COMPONENT16, width, height);
            gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);

            // Attach both buffers to the frame buffer
            gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                    GL.GL_COLOR_ATTACHMENT0, GL.GL_TEXTURE_2D,
                    rboTexture.getPointer(), 0);
            gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
                    GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER,
                    rboPointer.get(0));

            checkNoError(gl, "POST: ", false);

            checkStatus(gl, "Framebuffer error:");
        } catch (UninitializedException e) {
            e.printStackTrace();
        }

        // Unbind. The FBO is now ready for use.
        gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);

        initialized = true;
    }

    /**
     * Internal method to check for OpenGL errorsLogs errors if there were any.
     * 
     * @param gl
     *            The opengl instance.
     * @param exceptionMessage
     *            The message Prefix.
     * @param quietlyRemoveAllPreviousErrors
     *            Set whether this method should focus on the current error
     *            only, or print all errors that are buffered by opengl.
     * @return true if there was no error.
     */
    private boolean checkNoError(GL gl, String exceptionMessage,
            boolean quietlyRemoveAllPreviousErrors) {
        int error = gl.glGetError();
        if (!quietlyRemoveAllPreviousErrors) {
            if (GL.GL_NO_ERROR != error) {
                logger.error("GL ERROR(s) " + exceptionMessage + " : ");
                while (GL.GL_NO_ERROR != error) {
                    logger.error(" GL Error 0x" + Integer.toHexString(error));
                    error = gl.glGetError();
                }
                return false;
            }
        } else {
            while (GL.GL_NO_ERROR != error) {
                error = gl.glGetError();
            }
        }
        return true;
    }

    /**
     * Internal method to check the status for the framebuffer object after
     * supposed creation.
     * 
     * @param gl
     *            The opengl instance
     * @param prefix
     *            The prefix for any error messages.
     */
    private void checkStatus(GL3 gl, String prefix) {
        boolean errDetected = false;
        String errString = prefix + " ";
        // Check for errors
        int status = gl.glCheckFramebufferStatus(GL3.GL_FRAMEBUFFER);
        if (status != GL3.GL_FRAMEBUFFER_COMPLETE) {
            errDetected = true;
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT.";
            }
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS.";
            }
            if (status == GL3.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
                errString += "GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT.";
            }
            if (status == GL3.GL_FRAMEBUFFER_UNSUPPORTED) {
                errString += "GL_FRAMEBUFFER_UNSUPPORTED.";
            }
        }

        if (errDetected) {
            logger.error(errString);
        }
    }

    /**
     * Method which sets this framebuffer as the one in use for rendering.
     * 
     * @param gl
     *            The opengl instance
     * @throws UninitializedException
     *             if this FBO was not initialized before use with the
     *             {@link #init(GL3)} method.
     */
    public void bind(GL3 gl) throws UninitializedException {
        if (initialized) {
            rboTexture.use(gl);
            gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, fboPointer.get(0));
        } else {
            throw new UninitializedException("FBO not initialized.");
        }
    }

    /**
     * Getter for the internal storage texture for this FBO.
     * 
     * @return The storage texture.
     */
    public Texture2D getTexture() {
        return rboTexture;
    }

    /**
     * Method to unbind this (and any other) FBO, once again outputting any
     * render actions directly to the screen.
     * 
     * @param gl
     *            The opengl instance.
     */
    public void unBind(GL3 gl) {
        // gl.glBindRenderbuffer(GL3.GL_RENDERBUFFER, 0);
        gl.glBindFramebuffer(GL3.GL_FRAMEBUFFER, 0);
        // gl.glDrawBuffer(GL3.GL_BACK);
    }

    /**
     * Safely remove this FBO completely from the opengl context.
     * 
     * @param gl
     *            The opengl instance.
     */
    public void delete(GL3 gl) {
        gl.glDeleteRenderbuffers(1, rboPointer);
        gl.glDeleteFramebuffers(1, fboPointer);
    }

    /**
     * Getter method for the pointer to the opengl-internal fbo.
     * 
     * @return The fboPointer.
     */
    public IntBuffer getPointer() {
        return fboPointer;
    }
}
