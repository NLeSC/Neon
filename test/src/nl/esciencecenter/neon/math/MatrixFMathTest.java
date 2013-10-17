/**
 * 
 */
package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nl.esciencecenter.neon.exceptions.InverseNotAvailableException;
import nl.esciencecenter.neon.math.Float2Matrix;
import nl.esciencecenter.neon.math.Float3Matrix;
import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;

import org.junit.Test;

/* Copyright [2013] [Netherlands eScience Center]
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
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class MatrixFMathTest {

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#getNormalMatrix(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testGetNormalMatrix() {
        // Test of 'normal' cases
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix input2 = new Float4Matrix(0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f);

        // Test cases where the normalmatrix should not be available, and where
        // the identity matrix should be returned
        Float4Matrix input3 = new Float4Matrix(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        Float4Matrix input4 = new Float4Matrix(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // Test case with faulty input values
        Float4Matrix input5 = new Float4Matrix(Float.NaN, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        Float3Matrix expected1 = new Float3Matrix(3.030303f, -3.030303f, -0.0f, -3.030303f, 3.030303f, 3.030303f, 3.030303f, 0.0f,
                -3.030303f);
        Float3Matrix expected2 = new Float3Matrix(-1.0f, 2.0f, -1.0f, 2.0f, -6.75f, 4.5f, -1.0f, 3.75f, -2.5f);
        Float3Matrix expected3 = new Float3Matrix();
        Float3Matrix expected4 = new Float3Matrix();
        Float3Matrix expected5 = new Float3Matrix(Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN,
                Float.NaN, Float.NaN);

        assertArrayEquals(expected1.asArray(), FloatMatrixMath.getNormalMatrix(input1).asArray(), FloatMatrixMath.getEpsilon());
        assertArrayEquals(expected2.asArray(), FloatMatrixMath.getNormalMatrix(input2).asArray(), FloatMatrixMath.getEpsilon());
        assertArrayEquals(expected3.asArray(), FloatMatrixMath.getNormalMatrix(input3).asArray(), FloatMatrixMath.getEpsilon());
        assertArrayEquals(expected4.asArray(), FloatMatrixMath.getNormalMatrix(input4).asArray(), FloatMatrixMath.getEpsilon());
        assertArrayEquals(expected5.asArray(), FloatMatrixMath.getNormalMatrix(input5).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#ortho(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho() {
        // Test of normal case
        float left = -1f, right = 1f, bottom = -1f, top = 1f, zNear = 0.0001f, zFar = 3000f;

        Float4Matrix expected = new Float4Matrix(1.0f, 0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f, -0.0f, 0.0f, 0.0f, -6.6666666E-4f, -1.0f,
                0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.ortho(left, right, bottom, top, zNear, zFar).asArray(),
                FloatMatrixMath.getEpsilon());

        // Test illegal arguments (zNear, zFar cannot be equal)
        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = 1f;
        zFar = 1f;

        try {
            FloatMatrixMath.ortho(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (left cannot be >= right)
        left = 100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = -1f;
        zFar = 1f;

        try {
            FloatMatrixMath.ortho(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (bottom cannot be >= top)
        left = 0f;
        right = 10f;
        bottom = 100f;
        top = 10f;
        zNear = -1f;
        zFar = 1f;

        try {
            FloatMatrixMath.ortho(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#ortho2D(float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho2D() {
        // Test normal cases, implementation is same as ortho() with fixed
        // arguments, so we depend on that test for the rest
        float left = -1f, right = 1f, bottom = -1f, top = 1f;

        Float4Matrix expected = FloatMatrixMath.ortho(left, right, bottom, top, -1f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.ortho2D(left, right, bottom, top).asArray(),
                FloatMatrixMath.getEpsilon());

        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;

        expected = FloatMatrixMath.ortho(left, right, bottom, top, -1f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.ortho2D(left, right, bottom, top).asArray(),
                FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#frustum(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testFrustum() {
        // Test normal case
        float left = -1f, right = 1f, bottom = -1f, top = 1f, zNear = 0.0001f, zFar = 3000f;

        Float4Matrix expected = new Float4Matrix(1.0E-4f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0E-4f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-4f,
                0.0f, 0.0f, -1.0f, 0.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.frustum(left, right, bottom, top, zNear, zFar).asArray(),
                FloatMatrixMath.getEpsilon());

        // Test illegal arguments (zNear, zFar cannot be equal)
        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = 1f;
        zFar = 1f;

        try {
            FloatMatrixMath.frustum(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (left cannot be >= right)
        left = 100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = -1f;
        zFar = 1f;

        try {
            FloatMatrixMath.frustum(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (bottom cannot be >= top)
        left = 0f;
        right = 10f;
        bottom = 100f;
        top = 10f;
        zNear = -1f;
        zFar = 1f;

        try {
            FloatMatrixMath.frustum(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#perspective(float, float, float, float)}
     * .
     */
    @Test
    public final void testPerspective() {
        float fovy = 90f, aspect = 1920f / 1080f, zNear = 0.00001f, zFar = 3000f;

        Float4Matrix expected = new Float4Matrix(0.56249994f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-5f,
                0.0f, 0.0f, -1.0f, 0.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.perspective(fovy, aspect, zNear, zFar).asArray(),
                FloatMatrixMath.getEpsilon());

        fovy = 0f;
        aspect = 1f;
        zNear = 0.00001f;
        zFar = 1f;

        expected = new Float4Matrix(0.56249994f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-5f, 0.0f,
                0.0f, -1.0f, 0.0f);

        // Test illegal arguments (zNear cannot be <= zFar)
        fovy = 10f;
        aspect = 1f;
        zNear = 1f;
        zFar = 1f;

        try {
            FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (fovy cannot be <= 0)
        fovy = 0f;
        aspect = 1f;
        zNear = 0.1f;
        zFar = 1f;

        try {
            FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (aspect cannot be <= 0)
        fovy = 10f;
        aspect = -1f;
        zNear = 0.1f;
        zFar = 1f;

        try {
            FloatMatrixMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#lookAt(nl.esciencecenter.neon.math.Float4Vector, nl.esciencecenter.neon.math.Float4Vector, nl.esciencecenter.neon.math.Float4Vector)}
     * .
     */
    @Test
    public final void testLookAt() {
        // Default (correct) case
        Float4Vector eye = new Float4Vector(0f, 0f, 1f, 1f);
        Float4Vector at = new Float4Vector(0f, 0f, 0f, 1f);
        Float4Vector up = new Float4Vector(0f, 1f, 0f, 0f);

        Float4Matrix expected = new Float4Matrix(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
                0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.lookAt(eye, at, up).asArray(), FloatMatrixMath.getEpsilon());

        // Another correct case (there are no faulty cases)
        eye = new Float4Vector(0f, 0f, 1f, 1f);
        at = new Float4Vector(0f, 0f, 1f, 1f);
        up = new Float4Vector(0f, 0f, 1f, 0f);

        expected = new Float4Matrix(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.lookAt(eye, at, up).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#translate(float, float, float)}
     * .
     */
    @Test
    public final void testTranslateFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.translate(x, y, z).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#translate(nl.esciencecenter.neon.math.Float3Vector)}
     * .
     */
    @Test
    public final void testTranslateVecF3() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.translate(new Float3Vector(x, y, z)).asArray(),
                FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#translate(nl.esciencecenter.neon.math.Float4Vector)}
     * .
     */
    @Test
    public final void testTranslateVecF4() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.translate(new Float4Vector(x, y, z, 0f)).asArray(),
                FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#scale(float, float, float)}
     * .
     */
    @Test
    public final void testScaleFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.scale(x, y, z).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#scale(float)}.
     */
    @Test
    public final void testScaleFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f;
        Float4Matrix expected = new Float4Matrix(x, 0, 0, 0, 0, x, 0, 0, 0, 0, x, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.scale(x).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#scale(nl.esciencecenter.neon.math.Float3Vector)}
     * .
     */
    @Test
    public final void testScaleVecF3() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.scale(new Float3Vector(x, y, z)).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#scale(nl.esciencecenter.neon.math.Float4Vector)}
     * .
     */
    @Test
    public final void testScaleVecF4() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        Float4Matrix expected = new Float4Matrix(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.scale(new Float4Vector(x, y, z, 0f)).asArray(),
                FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotationX(float)}.
     */
    @Test
    public final void testRotationX() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        Float4Matrix expected = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationX(deg).asArray(), FloatMatrixMath.getEpsilon());

        deg = 0f;
        expected = new Float4Matrix();

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationX(deg).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotationY(float)}.
     */
    @Test
    public final void testRotationY() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        Float4Matrix expected = new Float4Matrix(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, -1f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationY(deg).asArray(), FloatMatrixMath.getEpsilon());

        deg = 0f;
        expected = new Float4Matrix();

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationY(deg).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotationZ(float)}.
     */
    @Test
    public final void testRotationZ() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        Float4Matrix expected = new Float4Matrix(0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationZ(deg).asArray(), FloatMatrixMath.getEpsilon());

        deg = 0f;
        expected = new Float4Matrix();

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotationZ(deg).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotate(float, float, float, float)}
     * .
     */
    @Test
    public final void testRotateFloatFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        Float3Vector axis = new Float3Vector(0.5f, 0f, 0.5f);
        Float4Matrix expected = new Float4Matrix(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotate(deg, axis.getX(), axis.getY(), axis.getZ()).asArray(),
                FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotate(float, nl.esciencecenter.neon.math.Float3Vector)}
     * .
     */
    @Test
    public final void testRotateFloatVecF3() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        Float3Vector axis = new Float3Vector(0.5f, 0f, 0.5f);
        Float4Matrix expected = new Float4Matrix(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotate(deg, axis).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#rotate(float, nl.esciencecenter.neon.math.Float4Vector)}
     * .
     */
    @Test
    public final void testRotateFloatVecF4() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        Float4Vector axis = new Float4Vector(0.5f, 0f, 0.5f, 0.5f);

        Float4Matrix expected = new Float4Matrix(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.rotate(deg, axis).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#determinant(nl.esciencecenter.neon.math.Float2Matrix)}
     * .
     */
    @Test
    public final void testDeterminantMatF2() {
        // Regression test, there are no cases where this could fail.
        Float2Matrix input = new Float2Matrix(0.5f, 1f, 0f, 3f);
        float expected = 1.5f;
        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());

        input = new Float2Matrix(1f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#determinant(nl.esciencecenter.neon.math.Float3Matrix)}
     * .
     */
    @Test
    public final void testDeterminantMatF3() {
        // Regression test, there are no cases where this could fail.
        Float3Matrix input = new Float3Matrix(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        float expected = .25f;
        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());

        input = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#determinant(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testDeterminantMatF4() {
        // Regression test, there are no cases where this could fail.
        Float4Matrix input = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float expected = -0.0355776f;
        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());

        input = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, FloatMatrixMath.determinant(input), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#cofactors(nl.esciencecenter.neon.math.Float3Matrix)}
     * .
     */
    @Test
    public final void testCofactorsMatF3() {
        // Regression test, there are no cases where this could fail.
        Float3Matrix input = new Float3Matrix(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        Float3Matrix expected = new Float3Matrix(2f, -2f, 2f, 2f, 3f, -3f, 0f, 10f, 0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.cofactors(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#cofactors(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testCofactorsMatF4() {
        // Regression test, there are no cases where this could fail.
        Float4Matrix input = new Float4Matrix(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        Float4Matrix expected = new Float4Matrix(-4f, 2f, -16f, 6f, -1f, 1f, -2f, 1f, 2f, 0f, 4f, -2f, 1f, -1f, 4f, -1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.cofactors(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#minors(nl.esciencecenter.neon.math.Float3Matrix, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF3() {
        // Regression test, there are no cases where this could fail.
        Float3Matrix input = new Float3Matrix(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        Float3Matrix expected = new Float3Matrix(2f, 2f, 2f, -2f, 3f, 3f, 0f, -10f, 0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.minors(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#minors(nl.esciencecenter.neon.math.Float4Matrix, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF4() {
        // Regression test, there are no cases where this could fail.
        Float4Matrix input = new Float4Matrix(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        Float4Matrix expected = new Float4Matrix(-4.0f, -2.0f, -16.0f, -6.0f, 1.0f, 1.0f, 2.0f, 1.0f, 2.0f, 0.0f, 4.0f, 2.0f, -1.0f,
                -1.0f, -4.0f, -1.0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.minors(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#exclude(nl.esciencecenter.neon.math.Float3Matrix, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF3() {
        // Regression test, there are no cases where this could fail.
        Float3Matrix input = new Float3Matrix(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        int col = 0;
        int row = 0;
        Float2Matrix expected = new Float2Matrix(0.5f, 0f, 0.5f, 0.5f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

        col = 1;
        row = 0;
        expected = new Float2Matrix(0.5f, 0f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

        col = 2;
        row = 0;
        expected = new Float2Matrix(0.5f, 0.5f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#exclude(nl.esciencecenter.neon.math.Float4Matrix, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF4() {
        Float4Matrix input = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        int col = 0;
        int row = 0;
        Float3Matrix expected = new Float3Matrix(0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

        col = 1;
        row = 0;
        expected = new Float3Matrix(0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

        col = 0;
        row = 1;
        expected = new Float3Matrix(0.000f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.exclude(input, col, row).asArray(), FloatMatrixMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#transpose(nl.esciencecenter.neon.math.Float2Matrix)}
     * .
     */
    @Test
    public final void testTransposeMatF2() {
        Float2Matrix input = new Float2Matrix(0.5f, 1f, 0f, 3f);
        Float2Matrix expected = new Float2Matrix(0.5f, 0f, 1f, 3f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float2Matrix(1f, 0f, 0f, 1f);
        expected = new Float2Matrix(1f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#transpose(nl.esciencecenter.neon.math.Float3Matrix)}
     * .
     */
    @Test
    public final void testTransposeMatF3() {
        Float3Matrix input = new Float3Matrix(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        Float3Matrix expected = new Float3Matrix(0.5f, 0.5f, 0.0f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#transpose(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testTransposeMatF4() {
        Float4Matrix input = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(0.330f, 0.330f, 0.330f, 0.000f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.transpose(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#adjoint(nl.esciencecenter.neon.math.Float2Matrix)}
     * .
     */
    @Test
    public final void testAdjointMatF2() {
        Float2Matrix input = new Float2Matrix(0.5f, 1f, 0f, 3f);
        Float2Matrix expected = new Float2Matrix(3f, -1f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#adjoint(nl.esciencecenter.neon.math.Float3Matrix)}
     * .
     */
    @Test
    public final void testAdjointMatF3() {
        Float3Matrix input = new Float3Matrix(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        Float3Matrix expected = new Float3Matrix(0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f, -0.25f, 0.25f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float3Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        expected = new Float3Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#adjoint(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testAdjointMatF4() {
        Float4Matrix input = new Float4Matrix(0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f);
        Float4Matrix expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 4f, -8f, 4f, 0f, -8f, 16f, -8f, 0f, 4f, -8f, 4f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float4Matrix(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        expected = new Float4Matrix(-4f, -1f, 2f, 1f, 2f, 1f, 0f, -1f, -16f, -2f, 4f, 4f, 6f, 1f, -2f, -1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

        input = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), FloatMatrixMath.adjoint(input).asArray(), FloatMatrixMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#inverse(nl.esciencecenter.neon.math.Float2Matrix)}
     * .
     */
    @Test
    public final void testInverseMatF2() {
        Float2Matrix input = new Float2Matrix(0.5f, 1f, 0f, 3f);
        Float2Matrix expected = new Float2Matrix(2f, -2f / 3f, 0f, 1f / 3f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new Float2Matrix(0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#inverse(nl.esciencecenter.neon.math.Float3Matrix)}
     * .
     */
    @Test
    public final void testInverseMatF3() {
        Float3Matrix input = new Float3Matrix(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        Float3Matrix expected = new Float3Matrix(1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f, 1f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new Float3Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new Float3Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.neon.math.FloatMatrixMath#inverse(nl.esciencecenter.neon.math.Float4Matrix)}
     * .
     */
    @Test
    public final void testInverseMatF4() {
        Float4Matrix input = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(1.010101f, 1.010101f, 1.010101f, -2.020202f, -2.020202f, 1.010101f, 1.010101f,
                1.010101f, 1.010101f, 1.010101f, -2.020202f, 1.010101f, 1.010101f, -2.020202f, 1.010101f, 1.010101f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), FloatMatrixMath.inverse(input).asArray(), FloatMatrixMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }
    }

}
