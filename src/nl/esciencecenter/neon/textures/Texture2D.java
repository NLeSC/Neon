package nl.esciencecenter.neon.textures;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLException;

import nl.esciencecenter.neon.exceptions.UninitializedException;

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
 * 2-Dimensional {@link Texture} object representation. Provides some generic
 * methods and variables for all types of 2-Dimensional textures.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class Texture2D extends Texture {
    private final static Logger logger = LoggerFactory.getLogger(Texture2D.class);

    /**
     * Generic constructor, should be called by all classes extending this class
     * to set the glMultitexUnit. Do not forget to call
     * {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     */
    public Texture2D(int glMultitexUnit) {
        super(glMultitexUnit);
    }

    /**
     * Initialization method generic for all 2-D Textures. Allocates space on
     * the device and copies data from the pixelBuffer into it. Therefore,
     * pixelBuffer cannot be null, or an error will be generated.
     * 
     * @param gl
     *            The current OpenGL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            if (pixelBuffer == null) {
                logger.error("Add a pixelbuffer first, by using a custom constructor. The Texture2D constructor is only meant to be extended.");
            }

            // Tell OpenGL we want to use 2D textures
            gl.glActiveTexture(getGlMultiTexUnit());

            // Create a Texture Object
            pointer = IntBuffer.allocate(1);
            gl.glGenTextures(1, pointer);

            // Tell OpenGL that this texture is 2D and we want to use it
            int num_mipmaps = 6;
            gl.glBindTexture(GL3.GL_TEXTURE_2D, pointer.get(0));
            gl.glEnable(GL3.GL_TEXTURE_2D);
            gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

            gl.glTexStorage2D(GL3.GL_TEXTURE_2D, num_mipmaps, GL3.GL_RGBA32F, width, height);
            gl.glTexSubImage2D(GL3.GL_TEXTURE_2D, 0, 0, 0, width, height, GL3.GL_RGBA, GL3.GL_UNSIGNED_BYTE,
                    pixelBuffer);
            gl.glGenerateMipmap(GL3.GL_TEXTURE_2D);

            // Wrap.
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);

            // Specifies the alignment requirements for the start of each pixel
            // row in memory.
            // gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);
            //
            // gl.glTexImage2D(GL3.GL_TEXTURE_2D, 0, // Mipmap level.
            // GL3.GL_RGBA32F, // GL.GL_RGBA, // Internal Texel Format,
            // width, height, 0, // Border
            // GL3.GL_RGBA, // External format from image,
            // GL3.GL_UNSIGNED_BYTE, pixelBuffer // Imagedata as ByteBuffer
            // );
            //
            // logger.debug("generating mipmaps for texture : " +
            // getMultitexNumber());
            // gl.glGenerateMipmap(GL3.GL_TEXTURE_2D);
            // checkNoError(gl, "mipmaps?", true);
            // logger.debug("done mipmaps");

            initialized = true;
        }
    }

    public void use(GL3 gl) throws UninitializedException {
        if (!initialized) {
            init(gl);
        }

        gl.glActiveTexture(getGlMultiTexUnit());
        gl.glBindTexture(GL3.GL_TEXTURE_2D, getPointer().get(0));
    }

    public void unBind(GL3 gl) {
        gl.glBindTexture(GL3.GL_TEXTURE_2D, 0);
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
    private boolean checkNoError(GL gl, String exceptionMessage, boolean quietlyRemoveAllPreviousErrors) {
        int error = gl.glGetError();
        if (!quietlyRemoveAllPreviousErrors) {
            if (GL.GL_NO_ERROR != error) {
                logger.error("GL ERROR(s) " + exceptionMessage + " : ");
                while (GL.GL_NO_ERROR != error) {
                    GLException exception = new GLException(" GL Error 0x" + Integer.toHexString(error));
                    logger.error("Error in OpenGL operation while initializing Texture2D", exception);
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
}