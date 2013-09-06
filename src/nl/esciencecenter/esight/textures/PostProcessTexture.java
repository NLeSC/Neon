package nl.esciencecenter.esight.textures;

import java.nio.ByteBuffer;

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
 * Generic wrapper for a {@link Texture} made out of an (initially) empty
 * ByteBuffer.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class PostProcessTexture extends Texture2D {

    /**
     * Default constructor, wrapper for an (initially) empty ByteBuffer. Do not
     * forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param width
     *            The width of this texture.
     * @param height
     *            The height of this texture.
     */
    public PostProcessTexture(int width, int height, int gLMultiTexUnit) {
        super(gLMultiTexUnit);
        this.height = height;
        this.width = width;

        pixelBuffer = ByteBuffer.allocate(width * height * 4);
    }
}
