package nl.esciencecenter.neon.util;

import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.neon.math.Float4Matrix;

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
 * Convenience class to implement a stack of Modelview matrices, like the one
 * originally used in OpenGL 2.0
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ModelViewStack {
    private final List<Float4Matrix> stack;

    /**
     * Basic constructor for ModelViewStack.
     */
    public ModelViewStack() {
        stack = new ArrayList<Float4Matrix>();
    }

    /**
     * Copy constructor for ModelViewStack.
     * 
     * @param old
     *            The stack to copy.
     */
    public ModelViewStack(ModelViewStack old) {
        stack = new ArrayList<Float4Matrix>();
        for (Float4Matrix oMV : old.stack) {
            stack.add(oMV);
        }
    }

    /**
     * Put a new Modelview matrix on top of the stack.
     * 
     * @param mv
     *            The new modelview matrix to put on top.
     */
    public void putTop(Float4Matrix mv) {
        stack.add(mv);
    }

    /**
     * Put a new Modelview matrix on the bottom of the stack.
     * 
     * @param mv
     *            The new modelview matrix to put on the bottom.
     */
    public void putBottom(Float4Matrix mv) {
        stack.add(0, mv);
    }

    /**
     * Calculate the multiplication of the entire stack with the given input
     * matrix, and return the resulting compound modelview matrix.
     * 
     * @param input
     *            The matrix to multiply the stack with.
     * @return the calculation result.
     */
    public Float4Matrix calc(Float4Matrix input) {
        Float4Matrix result = new Float4Matrix(input);
        for (Float4Matrix element : stack) {
            result = result.mul(element);
        }
        return result;
    }

}
