package nl.esciencecenter.esight.textures;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.exceptions.UninitializedException;

import com.jogamp.common.nio.Buffers;

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
 * 3-Dimensional {@link Texture} object representation. Provides some generic
 * methods and variables for all types of 3-Dimensional textures.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class Texture3D extends Texture {

    /**
     * Generic constructor, should be called by all classes extending this class
     * to set the glMultitexUnit. Do not forget to call
     * {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultiTexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     */
    public Texture3D(int glMultiTexUnit) {
        super(glMultiTexUnit);
    }

    /**
     * Initialization method generic for all 3-D Textures. Allocates space on
     * the device and copies data from the pixelBuffer into it. Therefore,
     * pixelBuffer cannot be null, or an error will be generated.
     * 
     * @param gl
     *            The current OpenGL instance.
     */
    public void init(GL3 gl) {
        if (!initialized) {
            if (pixelBuffer == null || width == 0 || height == 0 || depth == 0) {
                System.err
                        .println("Add a pixelbuffer and w/h/d first, by using a custom constructor. The Texture3D constructor is only meant to be extended.");
            }

            gl.glActiveTexture(glMultiTexUnit);
            // gl.glEnable(GL3.GL_TEXTURE_3D);

            // Create new texture pointer and bind it so we can manipulate it.

            pointer = Buffers.newDirectIntBuffer(1);
            gl.glGenTextures(1, pointer);

            gl.glBindTexture(GL3.GL_TEXTURE_3D, pointer.get(0));

            // Wrap.
            gl.glTexParameteri(GL3.GL_TEXTURE_3D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_MIRRORED_REPEAT);
            gl.glTexParameteri(GL3.GL_TEXTURE_3D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_MIRRORED_REPEAT);
            gl.glTexParameteri(GL3.GL_TEXTURE_3D, GL3.GL_TEXTURE_WRAP_R, GL3.GL_MIRRORED_REPEAT);
            gl.glTexParameteri(GL3.GL_TEXTURE_3D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
            gl.glTexParameteri(GL3.GL_TEXTURE_3D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);

            gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

            gl.glTexImage3D(GL3.GL_TEXTURE_3D, 0, // Mipmap level.
                    GL3.GL_RGBA, // GL.GL_RGBA, // Internal Texel Format,
                    width, height, depth, 0, // Border
                    GL3.GL_RGBA, // External format from image,
                    GL3.GL_BYTE, pixelBuffer // Imagedata as ByteBuffer
            );

            initialized = true;
        }
    }

    public void use(GL3 gl) throws UninitializedException {
        if (!initialized) {
            init(gl);
        }

        gl.glActiveTexture(glMultiTexUnit);
        // gl.glEnable(GL3.GL_TEXTURE_3D);
        gl.glBindTexture(GL3.GL_TEXTURE_3D, getPointer());
    }
}
