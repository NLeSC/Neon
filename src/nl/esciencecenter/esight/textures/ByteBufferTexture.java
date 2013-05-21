package nl.esciencecenter.esight.textures;

import java.nio.ByteBuffer;

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
 * Generic wrapper for a {@link Texture} made out of a pre-generated ByteBuffer.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ByteBufferTexture extends Texture2D {
    /**
     * Default constructor, wrapper for a pre-generated ByteBuffer. Do not
     * forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param pixelBuffer
     *            The pre-generated ByteBuffer holding the pixels.
     * @param width
     *            The width of this texture.
     * @param height
     *            The height of this texture.
     */
    public ByteBufferTexture(int glMultitexUnit, ByteBuffer pixelBuffer, int width, int height) {
        super(glMultitexUnit);

        this.pixelBuffer = pixelBuffer;
        this.width = width;
        this.height = height;
    }
}
