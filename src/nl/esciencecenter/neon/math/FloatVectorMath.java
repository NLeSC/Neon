package nl.esciencecenter.neon.math;

import java.nio.FloatBuffer;
import java.util.List;

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
 * Utility class for float vector calculations.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public final class FloatVectorMath {
    private FloatVectorMath() {
        // Utility class
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
    public static float dot(Float2Vector u, Float2Vector v) {
        return u.getX() * v.getX() + u.getY() * v.getY();
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
    public static float dot(Float3Vector u, Float3Vector v) {
        return u.getX() * v.getX() + u.getY() * v.getY() + u.getZ() * v.getZ();
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
    public static float dot(Float4Vector u, Float4Vector v) {
        return u.getX() * v.getX() + u.getY() * v.getY() + u.getZ() * v.getZ() + u.getW() * v.getW();
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static float length(Float2Vector v) {
        return (float) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static float length(Float3Vector v) {
        return (float) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to calculate the length of a vector.
     * 
     * @param v
     *            The vector.
     * @return The length of the vector.
     */
    public static float length(Float4Vector v) {
        return (float) Math.sqrt(dot(v, v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static Float2Vector normalize(Float2Vector v) {
        return v.div(length(v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static Float3Vector normalize(Float3Vector v) {
        return v.div(length(v));
    }

    /**
     * Helper method to normalize a vector.
     * 
     * @param v
     *            The vector.
     * @return The normal of the vector.
     */
    public static Float4Vector normalize(Float4Vector v) {
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
    public static Float3Vector cross(Float3Vector u, Float3Vector v) {
        return new Float3Vector(u.getY() * v.getZ() - u.getZ() * v.getY(), u.getZ() * v.getX() - u.getX() * v.getZ(),
                u.getX() * v.getY() - u.getY() * v.getX());
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
    public static Float4Vector cross(Float4Vector u, Float4Vector v) {
        return new Float4Vector(u.getY() * v.getZ() - u.getZ() * v.getY(), u.getZ() * v.getX() - u.getX() * v.getZ(),
                u.getX() * v.getY() - u.getY() * v.getX(), 0.0f);
    }

    /**
     * Helper method to create a FloatBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer toBuffer(float[] array) {
        FloatBuffer result = FloatBuffer.allocate(array.length);
        result.put(array);

        result.rewind();
        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer toBuffer(Float2Vector[] array) {
        FloatBuffer result = FloatBuffer.allocate(array.length * 2);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer toBuffer(Float3Vector[] array) {
        FloatBuffer result = FloatBuffer.allocate(array.length * 3);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an array of vectors.
     * 
     * @param array
     *            The array of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer toBuffer(Float4Vector[] array) {
        FloatBuffer result = FloatBuffer.allocate(array.length * 4);

        for (int i = 0; i < array.length; i++) {
            result.put(array[i].asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer listToBuffer(List<Float> list) {
        FloatBuffer result = FloatBuffer.allocate(list.size());

        for (Float f : list) {
            result.put(f);
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer vec2ListToBuffer(List<Float2Vector> list) {
        FloatBuffer result = FloatBuffer.allocate(list.size() * 2);

        for (Float2Vector v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer vec3ListToBuffer(List<Float3Vector> list) {
        FloatBuffer result = FloatBuffer.allocate(list.size() * 3);

        for (Float3Vector v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Helper method to create a FloatBuffer from an list of vectors.
     * 
     * @param list
     *            The List of vectors.
     * @return The new FloatBuffer
     */
    public static FloatBuffer vec4ListToBuffer(List<Float4Vector> list) {
        FloatBuffer result = FloatBuffer.allocate(list.size() * 4);

        for (Float4Vector v : list) {
            result.put(v.asBuffer());
        }

        result.rewind();

        return result;
    }

    /**
     * Bezier curve interpolation between two points with control vectors (this
     * could be particle speed at the points).
     * 
     * @param steps
     *            The number of steps on the bezier curve to calculate.
     * @param startLocation
     *            The starting point for this bezier curve.
     * @param startControl
     *            The starting point's control vector.
     * @param endControl
     *            The end point for this bezier curve.
     * @param endLocation
     *            The end point's control vector.
     * @return The array of points on the new bezier curve.
     */
    public static Float4Vector[] bezierCurve(int steps, Float4Vector startLocation, Float3Vector startControl,
            Float3Vector endControl, Float4Vector endLocation) {
        Float4Vector[] newBezierPoints = new Float4Vector[steps];
        for (int i = 0; i < steps; i++) {
            newBezierPoints[i] = new Float4Vector(0f, 0f, 0f, 1f);
        }

        float t = 1f / steps;
        float temp = t * t;

        for (int coord = 0; coord < 3; coord++) {
            float p[] = new float[4];
            if (coord == 0) {
                p[0] = startLocation.getX();
                p[1] = (startLocation.add(startControl)).getX();
                p[2] = (endLocation.add(endControl.neg())).getX();
                p[3] = endLocation.getX();
            } else if (coord == 1) {
                p[0] = startLocation.getY();
                p[1] = (startLocation.add(startControl)).getY();
                p[2] = (endLocation.add(endControl.neg())).getY();
                p[3] = endLocation.getY();
            } else if (coord == 2) {
                p[0] = startLocation.getZ();
                p[1] = (startLocation.add(startControl)).getZ();
                p[2] = (endLocation.add(endControl.neg())).getZ();
                p[3] = endLocation.getZ();
            } else if (coord == 3) {
                p[0] = startLocation.getW();
                p[1] = (startLocation.add(startControl)).getW();
                p[2] = (endLocation.add(endControl.neg())).getW();
                p[3] = endLocation.getW();
            }

            // The algorithm itself begins here ==
            float f, fd, fdd, fddd, fdd_per_2, fddd_per_2, fddd_per_6; // NOSONAR

            // I've tried to optimize the amount of
            // multiplications here, but these are exactly
            // the same formulas that were derived earlier
            // for f(0), f'(0)*t etc.
            f = p[0];
            fd = 3 * (p[1] - p[0]) * t;
            fdd_per_2 = 3 * (p[0] - 2 * p[1] + p[2]) * temp;
            fddd_per_2 = 3 * (3 * (p[1] - p[2]) + p[3] - p[0]) * temp * t;

            fddd = fddd_per_2 + fddd_per_2;
            fdd = fdd_per_2 + fdd_per_2;
            fddd_per_6 = fddd_per_2 * (1f / 3);

            for (int loop = 0; loop < steps; loop++) {
                if (coord == 0) {
                    newBezierPoints[loop].setX(f);
                } else if (coord == 1) {
                    newBezierPoints[loop].setY(f);
                } else if (coord == 2) {
                    newBezierPoints[loop].setZ(f);
                } else if (coord == 3) {
                    newBezierPoints[loop].setW(f);
                }

                f = f + fd + fdd_per_2 + fddd_per_6;
                fd = fd + fdd + fddd_per_2;
                fdd = fdd + fddd;
                fdd_per_2 = fdd_per_2 + fddd_per_2;
            }
        }

        return newBezierPoints;
    }

    /**
     * Bezier curve interpolation for _rotation_ between two points with control
     * vectors (this could be particle speed at the points). Outputs a number of
     * degrees for rotations.
     * 
     * @param steps
     *            The number of steps on the bezier curve to calculate.
     * @param startLocation
     *            The starting point for this bezier curve.
     * @param startControl
     *            The starting point's control vector.
     * @param endControl
     *            The end point for this bezier curve.
     * @param endLocation
     *            The end point's control vector.
     * @return The array of points on the new bezier curve.
     */
    public static Float3Vector[] degreesBezierCurve(int steps, Float3Vector startLocation, Float3Vector startControl,
            Float3Vector endControl, Float3Vector endLocation) {
        Float3Vector[] newBezierPoints = new Float3Vector[steps];
        for (int i = 0; i < steps; i++) {
            newBezierPoints[i] = new Float3Vector();
        }

        float t = 1f / steps;
        float temp = t * t;

        for (int coord = 0; coord < 3; coord++) {
            float p[] = new float[4];
            if (coord == 0) {
                p[0] = startLocation.getX();
                p[1] = (startLocation.add(startControl)).getX();
                p[2] = (endLocation.add(endControl.neg())).getX();
                p[3] = endLocation.getX();
            } else if (coord == 1) {
                p[0] = startLocation.getY();
                p[1] = (startLocation.add(startControl)).getY();
                p[2] = (endLocation.add(endControl.neg())).getY();
                p[3] = endLocation.getY();
            } else if (coord == 2) {
                p[0] = startLocation.getZ();
                p[1] = (startLocation.add(startControl)).getZ();
                p[2] = (endLocation.add(endControl.neg())).getZ();
                p[3] = endLocation.getZ();
            }

            if (p[0] - p[3] < 0f) {
                p[0] = p[0] + 360f;
            } else if (p[0] + p[3] > 360f) {
                p[0] = p[0] - 360f;
            }

            // The algorithm itself begins here ==
            float f, fd, fdd, fddd, fdd_per_2, fddd_per_2, fddd_per_6; // NOSONAR

            // I've tried to optimize the amount of
            // multiplications here, but these are exactly
            // the same formulas that were derived earlier
            // for f(0), f'(0)*t etc.
            f = p[0];
            fd = 3 * (p[1] - p[0]) * t;
            fdd_per_2 = 3 * (p[0] - 2 * p[1] + p[2]) * temp;
            fddd_per_2 = 3 * (3 * (p[1] - p[2]) + p[3] - p[0]) * temp * t;

            fddd = fddd_per_2 + fddd_per_2;
            fdd = fdd_per_2 + fdd_per_2;
            fddd_per_6 = fddd_per_2 * (1f / 3);

            for (int loop = 0; loop < steps; loop++) {
                if (coord == 0) {
                    newBezierPoints[loop].setX(f);
                } else if (coord == 1) {
                    newBezierPoints[loop].setY(f);
                } else if (coord == 2) {
                    newBezierPoints[loop].setZ(f);
                }

                f = f + fd + fdd_per_2 + fddd_per_6;
                fd = fd + fdd + fddd_per_2;
                fdd = fdd + fddd;
                fdd_per_2 = fdd_per_2 + fddd_per_2;
            }
        }

        return newBezierPoints;
    }

    public static Color4[] interpolateColors(int steps, Color4 startColor, Color4 endColor) {
        Color4[] newColors = new Color4[steps];

        float rstep = (endColor.getR() - startColor.getR()) / steps;
        float gstep = (endColor.getG() - startColor.getG()) / steps;
        float bstep = (endColor.getB() - startColor.getB()) / steps;
        float astep = (endColor.getA() - startColor.getA()) / steps;

        for (int i = 0; i < steps; i++) {
            Color4 stepColor = new Color4();
            stepColor.setR(startColor.getR() + (rstep * i));
            stepColor.setG(startColor.getG() + (gstep * i));
            stepColor.setB(startColor.getB() + (bstep * i));
            stepColor.setA(startColor.getA() + (astep * i));

            newColors[i] = stepColor;
        }

        return newColors;
    }
}
