package nl.esciencecenter.esight.math;

import java.nio.ShortBuffer;
import java.util.List;

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
 * Utility class for short integer vector calculations.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class VectorSMath {

    /**
     * Helper method to calculate the dot product of two vectors
     * 
     * @param u
     *            The first vector.
     * @param v
     *            The second vector.
     * @return The dot product of the two vectors.
     */
    public static short dot(VecS2 u, VecS2 v) {
        return (short) (u.v[0] * v.v[0] + u.v[1] * v.v[1]);
    }

    /**
     * Helper method to calculate the dot product of two vectors
     * 
     * @param u
     *            The first vector.
     * @param v
     *            The second vector.
     * @return The dot product of the two vectors.
     */
    public static short dot(VecS3 u, VecS3 v) {
        return (short) (u.v[0] * v.v[0] + u.v[1] * v.v[1] + u.v[2] * v.v[2]);
    }

    /**
     * Helper method to calculate the dot product of two vectors
     * 
     * @param u
     *            The first vector.
     * @param v
     *            The second vector.
     * @return The dot product of the two vectors.
     */
    public static short dot(VecS4 u, VecS4 v) {
        return (short) (u.v[0] * v.v[0] + u.v[1] * v.v[1] + u.v[2] * v.v[2] + u.v[3] * v.v[3]);
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static short length(VecS2 v) {
        return (short) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static short length(VecS3 v) {
        return (short) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static short length(VecS4 v) {
        return (short) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static VecS2 normalize(VecS2 v) {
        return v.div(length(v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static VecS3 normalize(VecS3 v) {
        return v.div(length(v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static VecS4 normalize(VecS4 v) {
        return v.div(length(v));
    }

    /**
     * Helper method to calculate the cross product of two vectors
     * 
     * @param u
     *            The first vector.
     * @param v
     *            The second vector.
     * @return The new vector, which is the cross product of the two vectors.
     */
    public static VecS3 cross(VecS3 u, VecS3 v) {
        return new VecS3((short) (u.v[1] * v.v[2] - u.v[2] * v.v[1]), (short) (u.v[2] * v.v[0] - u.v[0] * v.v[2]),
                (short) (u.v[0] * v.v[1] - u.v[1] * v.v[0]));
    }

    /**
     * Helper method to calculate the cross product of two vectors
     * 
     * @param u
     *            The first vector.
     * @param v
     *            The second vector.
     * @return The new vector, which is the cross product of the two vectors.
     */
    public static VecS4 cross(VecS4 u, VecS4 v) {
        return new VecS4((short) (u.v[1] * v.v[2] - u.v[2] * v.v[1]), (short) (u.v[2] * v.v[0] - u.v[0] * v.v[2]),
                (short) (u.v[0] * v.v[1] - u.v[1] * v.v[0]), (short) (0.0f));
    }

    /**
     * Helper method to create a ShortBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer toBuffer(short[] array) {
        ShortBuffer result = ShortBuffer.allocate(array.length);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i]);
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer toBuffer(VecS2[] array) {
        ShortBuffer result = ShortBuffer.allocate(array.length * 2);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer toBuffer(VecS3[] array) {
        ShortBuffer result = ShortBuffer.allocate(array.length * 3);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer toBuffer(VecS4[] array) {
        ShortBuffer result = ShortBuffer.allocate(array.length * 4);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from a list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer listToBuffer(List<Short> list) {
        ShortBuffer result = ShortBuffer.allocate(list.size());

        for (Short s : list) {
            result.put(s);
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from a list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer vec2ListToBuffer(List<VecS2> list) {
        ShortBuffer result = ShortBuffer.allocate(list.size() * 2);

        for (VectorS v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from a list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer vec3ListToBuffer(List<VecS3> list) {
        ShortBuffer result = ShortBuffer.allocate(list.size() * 3);

        for (VectorS v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a ShortBuffer from a list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new ShortBuffer
     */
    public static ShortBuffer vec4ListToBuffer(List<VecS4> list) {
        ShortBuffer result = ShortBuffer.allocate(list.size() * 4);

        for (VectorS v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }
}
