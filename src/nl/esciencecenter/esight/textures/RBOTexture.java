package nl.esciencecenter.esight.textures;

import javax.media.opengl.GL3;

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
 * Generic wrapper for a {@link Texture} for use as a RenderBuffer Object. Does
 * not allocate memory for a ByteBuffer, since it is not needed.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class RBOTexture extends Texture2D {

    /**
     * Default constructor, use this in combination with RenderBuffer. Do not
     * forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param width
     *            The width of this texture. Generally the same size as the
     *            canvas width.
     * @param height
     *            The height of this texture. Generally the same size as the
     *            canvas height.
     */
    public RBOTexture(int width, int height, int glMultiTexUnit) {
        super(glMultiTexUnit);
        this.height = height;
        this.width = width;
    }

    @Override
    public void init(GL3 gl) {
        if (!initialized) {
            // Tell OpenGL we want to use textures
            gl.glEnable(GL3.GL_TEXTURE_2D);
            gl.glActiveTexture(this.glMultiTexUnit);

            // Create a Texture Object
            pointer = Buffers.newDirectIntBuffer(1);
            gl.glGenTextures(1, pointer);

            // Tell OpenGL that this texture is 2D and we want to use it
            gl.glBindTexture(GL3.GL_TEXTURE_2D, pointer.get(0));

            // Wrap.
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_S, GL3.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_WRAP_T, GL3.GL_CLAMP_TO_EDGE);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR);
            gl.glTexParameteri(GL3.GL_TEXTURE_2D, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);

            // Specifies the alignment requirements for the start of each pixel
            // row in memory.
            gl.glPixelStorei(GL3.GL_UNPACK_ALIGNMENT, 1);

            gl.glTexImage2D(GL3.GL_TEXTURE_2D, 0, // Mipmap level.
                    GL3.GL_RGBA32F, // GL.GL_RGBA, // Internal Texel Format,
                    width, height, 0, // Border
                    GL3.GL_RGBA, // External format from image,
                    GL3.GL_UNSIGNED_BYTE, null // Imagedata as ByteBuffer
            );

            initialized = true;
        }
    }
}
