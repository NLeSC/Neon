package nl.esciencecenter.esight.datastructures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;

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
 * PixelBuffer object used for copying the frambuffer to a file (screenshots).
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 */
public class IntPBO {
    private final static Logger logger = LoggerFactory.getLogger(IntPBO.class);

    /** Internal OpenGL pointer to the PBO */
    private final IntBuffer pboPointer;

    /** width and height of this PBO, and the resulting picture. */
    private final int width, height;

    /** Internal state keeping */
    private boolean initialized = false;

    /** The data storage for this pbo. */
    private ByteBuffer data;

    /**
     * Basic constructor for IntPBO.
     * 
     * @param width
     *            The width of the canvas to take a screenshot of.
     * @param height
     *            The height of the canvas to take a screenshot of.
     */
    public IntPBO(int width, int height) {
        this.width = width;
        this.height = height;

        pboPointer = IntBuffer.allocate(1);

        data = ByteBuffer.allocate(width * height * 4);
    }

    /**
     * OpenGL initialization method, sets up storage. Call this during
     * initialization step.
     * 
     * @param gl
     *            The OpenGL instance
     */
    public void init(GL3 gl) {
        checkNoError(gl, "PRE: ", true);

        /* generate the pixel buffer object */
        gl.glGenBuffers(1, pboPointer);
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, pboPointer.get(0));
        gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

        gl.glBufferData(GL3.GL_PIXEL_PACK_BUFFER, width * height * 4, null, GL3.GL_STREAM_READ);
        gl.glReadPixels(0, 0, width, height, GL3.GL_BGRA, GL3.GL_UNSIGNED_BYTE, 0);

        checkNoError(gl, "POST: ", false);

        // Unbind. The FBO is now ready for use.
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, 0);

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
    private boolean checkNoError(GL gl, String exceptionMessage, boolean quietlyRemoveAllPreviousErrors) {
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
     * Makes the calls to the OpenGL instance necessary to copy the current
     * framebuffer to the data storage.
     * 
     * @param gl
     *            The opengl instance.
     * @throws UninitializedException
     *             if the PBO was not initialized before use.
     */
    public void copyToPBO(GL3 gl) throws UninitializedException {
        if (initialized) {
            gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, pboPointer.get(0));

            gl.glReadPixels(0, 0, width, height, GL3.GL_BGRA, GL3.GL_UNSIGNED_BYTE, 0);
            data = gl.glMapBuffer(GL3.GL_PIXEL_PACK_BUFFER, GL3.GL_WRITE_ONLY);
        } else {
            throw new UninitializedException("PBO not initialized.");
        }
    }

    /**
     * Getter for the data storage.
     * 
     * @return The data store containing a picture.
     */
    public ByteBuffer getBuffer() {
        return data;
    }

    /**
     * @param gl
     *            The OpenGL instance
     */
    public void unBind(GL3 gl) {
        gl.glUnmapBuffer(GL3.GL_PIXEL_PACK_BUFFER);
        gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, 0);
    }

    /**
     * OpenGL internal deletion method for this PBO.
     * 
     * @param gl
     *            The OpenGL instance
     */
    public void delete(GL3 gl) {
        gl.glDeleteBuffers(1, pboPointer);
    }

    /**
     * Getter for the pointer to this PBO.
     * 
     * @return The OpenGL internal pointer to this PBO.
     */
    public IntBuffer getPointer() {
        return pboPointer;
    }

    /**
     * Convenience method that makes the neccesary calls to the OpenGL instance
     * in succession to copy the PBO to a buffer and then write it to disk using
     * the {@link BufferedImage} construct.
     * 
     * @param gl
     *            The OpenGL instance
     * @param filename
     *            The filename to use forwriting this picture.
     */
    public void makeScreenshotPNG(GL3 gl, String filename) {
        try {
            copyToPBO(gl);
            ByteBuffer bb = getBuffer();
            bb.rewind();

            int pixels = width * height;
            int[] array = new int[pixels];
            IntBuffer ib = IntBuffer.wrap(array);

            for (int i = 0; i < (pixels * 4); i += 4) {
                int b = bb.get(i) & 0xFF;
                int g = bb.get(i + 1) & 0xFF;
                int r = bb.get(i + 2) & 0xFF;
                // int a = bb.get(i + 3) & 0xFF;

                int argb = (r << 16) | (g << 8) | b;
                ib.put(argb);
            }
            ib.rewind();

            int[] destArray = new int[pixels];
            IntBuffer dest = IntBuffer.wrap(destArray);

            int[] rowPix = new int[width];
            for (int row = 0; row < height; row++) {
                ib.get(rowPix);
                dest.position((height - row - 1) * width);
                dest.put(rowPix);
            }

            BufferedImage bufIm = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufIm.setRGB(0, 0, width, height, dest.array(), 0, width);
            try {
                new File(filename).mkdirs();
                ImageIO.write(bufIm, "png", new File(filename));
                System.out.println("Saved screenshot: " + filename);
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            unBind(gl);

        } catch (UninitializedException e) {
            logger.error("error while tring to make screenshot.");
        }
    }
}
