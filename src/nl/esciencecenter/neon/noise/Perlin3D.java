package nl.esciencecenter.neon.noise;

import nl.esciencecenter.neon.textures.Texture3D;

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
 * 3-Dimensional Perlin Noise Texture.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Perlin3D extends Texture3D {

    /**
     * Generates a 3D blob of Perlin Noise.
     * 
     * Do not forget to call {@link #init(javax.media.opengl.GL3)} before use.
     * 
     * @param glMultitexUnit
     *            The OpenGL-internal MultitexUnit (GL.GL_TEXTUREX) this texture
     *            uses.
     * @param width
     *            The width of this texture.
     * @param height
     *            The height of this texture.
     * @param depth
     *            The depth of this texture.
     */
    public Perlin3D(int gLMultiTexUnit, int width, int height, int depth) {
        super(gLMultiTexUnit);

        this.width = width;
        this.height = height;
        this.depth = depth;

        Noise n = new Noise(4, width, height, depth);

        pixelBuffer = n.getPixelBuffer();
    }
}
