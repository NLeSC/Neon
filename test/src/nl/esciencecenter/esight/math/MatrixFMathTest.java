/**
 * 
 */
package nl.esciencecenter.esight.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nl.esciencecenter.esight.exceptions.InverseNotAvailableException;

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
     * {@link nl.esciencecenter.esight.math.MatrixFMath#getNormalMatrix(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testGetNormalMatrix() {
        // Test of 'normal' cases
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 input2 = new MatF4(0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f);

        // Test cases where the normalmatrix should not be available, and where
        // the identity matrix should be returned
        MatF4 input3 = new MatF4(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        MatF4 input4 = new MatF4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        // Test case with faulty input values
        MatF4 input5 = new MatF4(Float.NaN, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

        MatF3 expected1 = new MatF3(3.030303f, -3.030303f, -0.0f, -3.030303f, 3.030303f, 3.030303f, 3.030303f, 0.0f,
                -3.030303f);
        MatF3 expected2 = new MatF3(-1.0f, 2.0f, -1.0f, 2.0f, -6.75f, 4.5f, -1.0f, 3.75f, -2.5f);
        MatF3 expected3 = new MatF3();
        MatF3 expected4 = new MatF3();
        MatF3 expected5 = new MatF3(Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN,
                Float.NaN, Float.NaN);

        assertArrayEquals(expected1.asArray(), MatrixFMath.getNormalMatrix(input1).asArray(), MatrixFMath.getEpsilon());
        assertArrayEquals(expected2.asArray(), MatrixFMath.getNormalMatrix(input2).asArray(), MatrixFMath.getEpsilon());
        assertArrayEquals(expected3.asArray(), MatrixFMath.getNormalMatrix(input3).asArray(), MatrixFMath.getEpsilon());
        assertArrayEquals(expected4.asArray(), MatrixFMath.getNormalMatrix(input4).asArray(), MatrixFMath.getEpsilon());
        assertArrayEquals(expected5.asArray(), MatrixFMath.getNormalMatrix(input5).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#ortho(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho() {
        // Test of normal case
        float left = -1f, right = 1f, bottom = -1f, top = 1f, zNear = 0.0001f, zFar = 3000f;

        MatF4 expected = new MatF4(1.0f, 0.0f, 0.0f, -0.0f, 0.0f, 1.0f, 0.0f, -0.0f, 0.0f, 0.0f, -6.6666666E-4f, -1.0f,
                0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.ortho(left, right, bottom, top, zNear, zFar).asArray(),
                MatrixFMath.getEpsilon());

        // Test illegal arguments (zNear, zFar cannot be equal)
        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = 1f;
        zFar = 1f;

        try {
            MatrixFMath.ortho(left, right, bottom, top, zNear, zFar);
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
            MatrixFMath.ortho(left, right, bottom, top, zNear, zFar);
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
            MatrixFMath.ortho(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#ortho2D(float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho2D() {
        // Test normal cases, implementation is same as ortho() with fixed
        // arguments, so we depend on that test for the rest
        float left = -1f, right = 1f, bottom = -1f, top = 1f;

        MatF4 expected = MatrixFMath.ortho(left, right, bottom, top, -1f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.ortho2D(left, right, bottom, top).asArray(),
                MatrixFMath.getEpsilon());

        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;

        expected = MatrixFMath.ortho(left, right, bottom, top, -1f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.ortho2D(left, right, bottom, top).asArray(),
                MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#frustum(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testFrustum() {
        // Test normal case
        float left = -1f, right = 1f, bottom = -1f, top = 1f, zNear = 0.0001f, zFar = 3000f;

        MatF4 expected = new MatF4(1.0E-4f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0E-4f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-4f,
                0.0f, 0.0f, -1.0f, 0.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.frustum(left, right, bottom, top, zNear, zFar).asArray(),
                MatrixFMath.getEpsilon());

        // Test illegal arguments (zNear, zFar cannot be equal)
        left = -100f;
        right = 10f;
        bottom = 0f;
        top = .1f;
        zNear = 1f;
        zFar = 1f;

        try {
            MatrixFMath.frustum(left, right, bottom, top, zNear, zFar);
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
            MatrixFMath.frustum(left, right, bottom, top, zNear, zFar);
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
            MatrixFMath.frustum(left, right, bottom, top, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#perspective(float, float, float, float)}
     * .
     */
    @Test
    public final void testPerspective() {
        float fovy = 90f, aspect = 1920f / 1080f, zNear = 0.00001f, zFar = 3000f;

        MatF4 expected = new MatF4(0.56249994f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-5f,
                0.0f, 0.0f, -1.0f, 0.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.perspective(fovy, aspect, zNear, zFar).asArray(),
                MatrixFMath.getEpsilon());

        fovy = 0f;
        aspect = 1f;
        zNear = 0.00001f;
        zFar = 1f;

        expected = new MatF4(0.56249994f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, -2.0E-5f, 0.0f,
                0.0f, -1.0f, 0.0f);

        // Test illegal arguments (zNear cannot be <= zFar)
        fovy = 10f;
        aspect = 1f;
        zNear = 1f;
        zFar = 1f;

        try {
            MatrixFMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (fovy cannot be <= 0)
        fovy = 0f;
        aspect = 1f;
        zNear = 0.1f;
        zFar = 1f;

        try {
            MatrixFMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

        // Test illegal arguments (aspect cannot be <= 0)
        fovy = 10f;
        aspect = -1f;
        zNear = 0.1f;
        zFar = 1f;

        try {
            MatrixFMath.perspective(fovy, aspect, zNear, zFar);
            fail("didn't throw IllegalArgumentException when I expected it to");
        } catch (IllegalArgumentException expectedException) {

        }

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#lookAt(nl.esciencecenter.esight.math.VecF4, nl.esciencecenter.esight.math.VecF4, nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testLookAt() {
        // Default (correct) case
        VecF4 eye = new VecF4(0f, 0f, 1f, 1f);
        VecF4 at = new VecF4(0f, 0f, 0f, 1f);
        VecF4 up = new VecF4(0f, 1f, 0f, 0f);

        MatF4 expected = new MatF4(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f,
                0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.lookAt(eye, at, up).asArray(), MatrixFMath.getEpsilon());

        // Another correct case (there are no faulty cases)
        eye = new VecF4(0f, 0f, 1f, 1f);
        at = new VecF4(0f, 0f, 1f, 1f);
        up = new VecF4(0f, 0f, 1f, 0f);

        expected = new MatF4(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.lookAt(eye, at, up).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(float, float, float)}
     * .
     */
    @Test
    public final void testTranslateFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.translate(x, y, z).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testTranslateVecF3() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.translate(new VecF3(x, y, z)).asArray(),
                MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testTranslateVecF4() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.translate(new VecF4(x, y, z, 0f)).asArray(),
                MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(float, float, float)}
     * .
     */
    @Test
    public final void testScaleFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.scale(x, y, z).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(float)}.
     */
    @Test
    public final void testScaleFloat() {
        // Regression test, there are no cases where this could fail.
        float x = 1f;
        MatF4 expected = new MatF4(x, 0, 0, 0, 0, x, 0, 0, 0, 0, x, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.scale(x).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testScaleVecF3() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.scale(new VecF3(x, y, z)).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testScaleVecF4() {
        // Regression test, there are no cases where this could fail.
        float x = 1f, y = 1f, z = 1f;
        MatF4 expected = new MatF4(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);

        assertArrayEquals(expected.asArray(), MatrixFMath.scale(new VecF4(x, y, z, 0f)).asArray(),
                MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationX(float)}.
     */
    @Test
    public final void testRotationX() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        MatF4 expected = new MatF4(1f, 0f, 0f, 0f, 0f, 0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationX(deg).asArray(), MatrixFMath.getEpsilon());

        deg = 0f;
        expected = new MatF4();

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationX(deg).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationY(float)}.
     */
    @Test
    public final void testRotationY() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        MatF4 expected = new MatF4(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, -1f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationY(deg).asArray(), MatrixFMath.getEpsilon());

        deg = 0f;
        expected = new MatF4();

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationY(deg).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationZ(float)}.
     */
    @Test
    public final void testRotationZ() {
        // Regression test, there are no cases where this could fail.
        float deg = 90f;
        MatF4 expected = new MatF4(0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationZ(deg).asArray(), MatrixFMath.getEpsilon());

        deg = 0f;
        expected = new MatF4();

        assertArrayEquals(expected.asArray(), MatrixFMath.rotationZ(deg).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, float, float, float)}
     * .
     */
    @Test
    public final void testRotateFloatFloatFloatFloat() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        VecF3 axis = new VecF3(0.5f, 0f, 0.5f);
        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotate(deg, axis.getX(), axis.getY(), axis.getZ()).asArray(),
                MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testRotateFloatVecF3() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        VecF3 axis = new VecF3(0.5f, 0f, 0.5f);
        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotate(deg, axis).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testRotateFloatVecF4() {
        // Regression test, there are no cases where this could fail.
        float deg = 0.3f;
        VecF4 axis = new VecF4(0.5f, 0f, 0.5f, 0.5f);

        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f, 0.0f, 0.0037023856f, 0.9999863f,
                -0.0037023856f, 0.0f, 6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.rotate(deg, axis).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF2)}
     * .
     */
    @Test
    public final void testDeterminantMatF2() {
        // Regression test, there are no cases where this could fail.
        MatF2 input = new MatF2(0.5f, 1f, 0f, 3f);
        float expected = 1.5f;
        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());

        input = new MatF2(1f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testDeterminantMatF3() {
        // Regression test, there are no cases where this could fail.
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        float expected = .25f;
        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testDeterminantMatF4() {
        // Regression test, there are no cases where this could fail.
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float expected = -0.0355776f;
        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#cofactors(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testCofactorsMatF3() {
        // Regression test, there are no cases where this could fail.
        MatF3 input = new MatF3(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        MatF3 expected = new MatF3(2f, -2f, 2f, 2f, 3f, -3f, 0f, 10f, 0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.cofactors(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#cofactors(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testCofactorsMatF4() {
        // Regression test, there are no cases where this could fail.
        MatF4 input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        MatF4 expected = new MatF4(-4f, 2f, -16f, 6f, -1f, 1f, -2f, 1f, 2f, 0f, 4f, -2f, 1f, -1f, 4f, -1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.cofactors(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#minors(nl.esciencecenter.esight.math.MatF3, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF3() {
        // Regression test, there are no cases where this could fail.
        MatF3 input = new MatF3(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        MatF3 expected = new MatF3(2f, 2f, 2f, -2f, 3f, 3f, 0f, -10f, 0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.minors(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#minors(nl.esciencecenter.esight.math.MatF4, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF4() {
        // Regression test, there are no cases where this could fail.
        MatF4 input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        MatF4 expected = new MatF4(-4.0f, -2.0f, -16.0f, -6.0f, 1.0f, 1.0f, 2.0f, 1.0f, 2.0f, 0.0f, 4.0f, 2.0f, -1.0f,
                -1.0f, -4.0f, -1.0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.minors(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#exclude(nl.esciencecenter.esight.math.MatF3, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF3() {
        // Regression test, there are no cases where this could fail.
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        int col = 0;
        int row = 0;
        MatF2 expected = new MatF2(0.5f, 0f, 0.5f, 0.5f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

        col = 1;
        row = 0;
        expected = new MatF2(0.5f, 0f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

        col = 2;
        row = 0;
        expected = new MatF2(0.5f, 0.5f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#exclude(nl.esciencecenter.esight.math.MatF4, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        int col = 0;
        int row = 0;
        MatF3 expected = new MatF3(0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

        col = 1;
        row = 0;
        expected = new MatF3(0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

        col = 0;
        row = 1;
        expected = new MatF3(0.000f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), MatrixFMath.exclude(input, col, row).asArray(), MatrixFMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#transpose(nl.esciencecenter.esight.math.MatF2)}
     * .
     */
    @Test
    public final void testTransposeMatF2() {
        MatF2 input = new MatF2(0.5f, 1f, 0f, 3f);
        MatF2 expected = new MatF2(0.5f, 0f, 1f, 3f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF2(1f, 0f, 0f, 1f);
        expected = new MatF2(1f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#transpose(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testTransposeMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        MatF3 expected = new MatF3(0.5f, 0.5f, 0.0f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#transpose(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testTransposeMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(0.330f, 0.330f, 0.330f, 0.000f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.transpose(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#adjoint(nl.esciencecenter.esight.math.MatF2)}
     * .
     */
    @Test
    public final void testAdjointMatF2() {
        MatF2 input = new MatF2(0.5f, 1f, 0f, 3f);
        MatF2 expected = new MatF2(3f, -1f, 0f, 0.5f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#adjoint(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testAdjointMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        MatF3 expected = new MatF3(0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f, 0.25f, -0.25f, 0.25f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#adjoint(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testAdjointMatF4() {
        MatF4 input = new MatF4(0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f);
        MatF4 expected = new MatF4(0f, 0f, 0f, 0f, 0f, 4f, -8f, 4f, 0f, -8f, 16f, -8f, 0f, 4f, -8f, 4f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f, 0f, 1f, 4f);
        expected = new MatF4(-4f, -1f, 2f, 1f, 2f, 1f, 0f, -1f, -16f, -2f, 4f, 4f, 6f, 1f, -2f, -1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

        input = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 1f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), MatrixFMath.adjoint(input).asArray(), MatrixFMath.getEpsilon());

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#inverse(nl.esciencecenter.esight.math.MatF2)}
     * .
     */
    @Test
    public final void testInverseMatF2() {
        MatF2 input = new MatF2(0.5f, 1f, 0f, 3f);
        MatF2 expected = new MatF2(2f, -2f / 3f, 0f, 1f / 3f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF2(0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#inverse(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testInverseMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        MatF3 expected = new MatF3(1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f, 1f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#inverse(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testInverseMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(1.010101f, 1.010101f, 1.010101f, -2.020202f, -2.020202f, 1.010101f, 1.010101f,
                1.010101f, 1.010101f, 1.010101f, -2.020202f, 1.010101f, 1.010101f, -2.020202f, 1.010101f, 1.010101f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        try {
            assertArrayEquals(expected.asArray(), MatrixFMath.inverse(input).asArray(), MatrixFMath.getEpsilon());
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }
    }

}
