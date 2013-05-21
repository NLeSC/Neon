package nl.esciencecenter.esight.datastructures;

import java.nio.Buffer;

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
 * GLSL attribute object, containing the necessary information to be succesfully
 * sent to the GPU. In GLSL shaders, these attributes are referred to by the
 * keyword "in".
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class GLSLAttrib {
    public static int SIZE_FLOAT = 4;
    public static int SIZE_SHORT = 2;

    public Buffer buffer;
    public String name;
    public int numVectors;
    public int vectorSize;

    /**
     * Basic constructor for GLSLAttrib.
     * 
     * @param buffer
     *            The buffer to be associated with this attribute. The size for
     *            this buffer must correspond to
     * @param name
     *            The GLSL name for this attribute, must correspond to the "in"
     *            variable in your GLSL shader.
     * @param numVectors
     *            The number of vectors to be represented by this attribute.
     * @param vectorSize
     *            The size (number of places) per vector.
     */
    public GLSLAttrib(Buffer buffer, String name, int numVectors, int vectorSize) {
        this.buffer = buffer;
        this.name = name;
        this.numVectors = numVectors;
        this.vectorSize = vectorSize;
    }
}
