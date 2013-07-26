/**
 * 
 */
package nl.esciencecenter.esight.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import nl.esciencecenter.esight.exceptions.InverseNotAvailableException;
import nl.esciencecenter.esight.math.MatF2;
import nl.esciencecenter.esight.math.MatF3;
import nl.esciencecenter.esight.math.MatF4;
import nl.esciencecenter.esight.math.MatrixFMath;
import nl.esciencecenter.esight.math.VecF3;
import nl.esciencecenter.esight.math.VecF4;

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
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#ortho(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#ortho2D(float, float, float, float)}
     * .
     */
    @Test
    public final void testOrtho2D() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#frustum(float, float, float, float, float, float)}
     * .
     */
    @Test
    public final void testFrustum() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#perspective(float, float, float, float)}
     * .
     */
    @Test
    public final void testPerspective() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#lookAt(nl.esciencecenter.esight.math.VecF4, nl.esciencecenter.esight.math.VecF4, nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testLookAt() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(float, float, float)}
     * .
     */
    @Test
    public final void testTranslateFloatFloatFloat() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testTranslateVecF3() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#translate(nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testTranslateVecF4() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(float, float, float)}
     * .
     */
    @Test
    public final void testScaleFloatFloatFloat() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(float)}.
     */
    @Test
    public final void testScaleFloat() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testScaleVecF3() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#scale(nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testScaleVecF4() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationX(float)}.
     */
    @Test
    public final void testRotationX() {
        float deg = 90f;
        MatF4 expected = new MatF4(1f, 0f, 0f, 0f, 0f, 0f, -1f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 0f, 1f);

        assertEquals(expected, MatrixFMath.rotationX(deg));

        deg = 0f;
        expected = new MatF4();

        assertEquals(expected, MatrixFMath.rotationX(deg));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationY(float)}.
     */
    @Test
    public final void testRotationY() {
        float deg = 90f;
        MatF4 expected = new MatF4(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, -1f, 0f, 0f,
                0f, 0f, 0f, 0f, 1f);

        assertEquals(expected, MatrixFMath.rotationY(deg));

        deg = 0f;
        expected = new MatF4();

        assertEquals(expected, MatrixFMath.rotationY(deg));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotationZ(float)}.
     */
    @Test
    public final void testRotationZ() {
        float deg = 90f;
        MatF4 expected = new MatF4(0f, -1f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f,
                0f, 0f, 0f, 0f, 1f);

        assertEquals(expected, MatrixFMath.rotationZ(deg));

        deg = 0f;
        expected = new MatF4();

        assertEquals(expected, MatrixFMath.rotationZ(deg));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, float, float, float)}
     * .
     */
    @Test
    public final void testRotateFloatFloatFloatFloat() {
        float deg = 0.3f;
        VecF3 axis = new VecF3(0.5f, 0f, 0.5f);
        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f,
                0.0f, 0.0037023856f, 0.9999863f, -0.0037023856f, 0.0f,
                6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f);

        assertEquals(expected,
                MatrixFMath.rotate(deg, axis.get(0), axis.get(1), axis.get(2)));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, nl.esciencecenter.esight.math.VecF3)}
     * .
     */
    @Test
    public final void testRotateFloatVecF3() {
        float deg = 0.3f;
        VecF3 axis = new VecF3(0.5f, 0f, 0.5f);
        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f,
                0.0f, 0.0037023856f, 0.9999863f, -0.0037023856f, 0.0f,
                6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f);

        assertEquals(expected, MatrixFMath.rotate(deg, axis));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#rotate(float, nl.esciencecenter.esight.math.VecF4)}
     * .
     */
    @Test
    public final void testRotateFloatVecF4() {
        float deg = 0.3f;
        VecF4 axis = new VecF4(0.5f, 0f, 0.5f, 0.5f);

        MatF4 expected = new MatF4(0.99999315f, -0.0037023856f, 6.8545337E-6f,
                0.0f, 0.0037023856f, 0.9999863f, -0.0037023856f, 0.0f,
                6.8545337E-6f, 0.0037023856f, 0.99999315f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f);

        assertEquals(expected, MatrixFMath.rotate(deg, axis));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF2)}
     * .
     */
    @Test
    public final void testDeterminantMatF2() {
        MatF2 input = new MatF2(0.5f, 1f, 0f, 3f);
        float expected = 1.5f;
        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);

        input = new MatF2(1f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testDeterminantMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        float expected = .25f;
        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#determinant(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testDeterminantMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f,
                0.330f, 0.330f);
        float expected = -0.0355776f;
        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f);
        expected = 1f;

        assertEquals(expected, MatrixFMath.determinant(input),
                MatrixFMath.EPSILON);
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#cofactors(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testCofactorsMatF3() {
        MatF3 input = new MatF3(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        MatF3 expected = new MatF3(2f, -2f, 2f, 2f, 3f, -3f, 0f, 10f, 0f);

        assertEquals(expected, MatrixFMath.cofactors(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#cofactors(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testCofactorsMatF4() {
        MatF4 input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f,
                2f, 0f, 1f, 4f);
        MatF4 expected = new MatF4(-4f, 2f, -16f, 6f, -1f, 1f, -2f, 1f, 2f, 0f,
                4f, -2f, 1f, -1f, 4f, -1f);

        assertEquals(expected, MatrixFMath.cofactors(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#minors(nl.esciencecenter.esight.math.MatF3, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF3() {
        MatF3 input = new MatF3(3f, 0f, 2f, 2f, 0f, -2f, 0f, 1f, 1f);
        MatF3 expected = new MatF3(2f, 2f, 2f, -2f, 3f, 3f, 0f, -10f, 0f);

        assertEquals(expected, MatrixFMath.minors(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#minors(nl.esciencecenter.esight.math.MatF4, int, int)}
     * .
     */
    @Test
    public final void testMinorsMatF4() {
        MatF4 input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f,
                2f, 0f, 1f, 4f);
        MatF4 expected = new MatF4(-4.0f, -2.0f, -16.0f, -6.0f, 1.0f, 1.0f,
                2.0f, 1.0f, 2.0f, 0.0f, 4.0f, 2.0f, -1.0f, -1.0f, -4.0f, -1.0f);

        assertEquals(expected, MatrixFMath.minors(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#exclude(nl.esciencecenter.esight.math.MatF3, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        int col = 0;
        int row = 0;
        MatF2 expected = new MatF2(0.5f, 0f, 0.5f, 0.5f);

        assertEquals(expected, MatrixFMath.exclude(input, col, row));

        col = 1;
        row = 0;
        expected = new MatF2(0.5f, 0f, 0f, 0.5f);

        assertEquals(expected, MatrixFMath.exclude(input, col, row));

        col = 2;
        row = 0;
        expected = new MatF2(0.5f, 0.5f, 0f, 0.5f);

        assertEquals(expected, MatrixFMath.exclude(input, col, row));

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#exclude(nl.esciencecenter.esight.math.MatF4, int, int)}
     * .
     */
    @Test
    public final void testExcludeMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f,
                0.330f, 0.330f);
        int col = 0;
        int row = 0;
        MatF3 expected = new MatF3(0.330f, 0.330f, 0.000f, 0.330f, 0.000f,
                0.330f, 0.330f, 0.330f, 0.330f);

        assertEquals(expected, MatrixFMath.exclude(input, col, row));

        col = 1;
        row = 0;
        expected = new MatF3(0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f,
                0.000f, 0.330f, 0.330f);

        assertEquals(expected, MatrixFMath.exclude(input, col, row));

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

        assertEquals(expected, MatrixFMath.transpose(input));

        input = new MatF2(1f, 0f, 0f, 1f);

        assertEquals(input, MatrixFMath.transpose(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#transpose(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testTransposeMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        MatF3 expected = new MatF3(0.5f, 0.5f, 0.0f, 0f, 0.5f, 0.5f, 0.5f, 0f,
                0.5f);

        assertEquals(expected, MatrixFMath.transpose(input));

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertEquals(input, MatrixFMath.transpose(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#transpose(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testTransposeMatF4() {
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f,
                0.330f, 0.330f);
        MatF4 expected = new MatF4(0.330f, 0.330f, 0.330f, 0.000f, 0.000f,
                0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.330f);

        assertEquals(expected, MatrixFMath.transpose(input));

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f);

        assertEquals(input, MatrixFMath.transpose(input));
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

        assertEquals(expected, MatrixFMath.adjoint(input));

    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#adjoint(nl.esciencecenter.esight.math.MatF3)}
     * .
     */
    @Test
    public final void testAdjointMatF3() {
        MatF3 input = new MatF3(0.5f, 0f, 0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f);
        MatF3 expected = new MatF3(0.25f, 0.25f, -0.25f, -0.25f, 0.25f, 0.25f,
                0.25f, -0.25f, 0.25f);

        assertEquals(expected, MatrixFMath.adjoint(input));

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        assertEquals(expected, MatrixFMath.adjoint(input));

        input = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        assertEquals(expected, MatrixFMath.adjoint(input));
    }

    /**
     * Test method for
     * {@link nl.esciencecenter.esight.math.MatrixFMath#adjoint(nl.esciencecenter.esight.math.MatF4)}
     * .
     */
    @Test
    public final void testAdjointMatF4() {
        MatF4 input = new MatF4(0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f,
                12f, 13f, 14f, 15f, 16f);
        MatF4 expected = new MatF4(0f, 0f, 0f, 0f, 0f, 4f, -8f, 4f, 0f, -8f,
                16f, -8f, 0f, 4f, -8f, 4f);

        assertEquals(expected, MatrixFMath.adjoint(input));

        input = new MatF4(1f, 0f, 0f, 1f, 0f, 2f, 1f, 2f, 2f, 1f, 0f, 1f, 2f,
                0f, 1f, 4f);
        expected = new MatF4(-4f, -1f, 2f, 1f, 2f, 1f, 0f, -1f, -16f, -2f, 4f,
                4f, 6f, 1f, -2f, -1f);

        assertEquals(expected, MatrixFMath.adjoint(input));

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f);
        expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f);

        assertEquals(expected, MatrixFMath.adjoint(input));

        input = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 1f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f);

        assertEquals(expected, MatrixFMath.adjoint(input));

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
            assertEquals(expected, MatrixFMath.inverse(input));
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
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
            assertEquals(expected, MatrixFMath.inverse(input));
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
        try {
            assertEquals(expected, MatrixFMath.inverse(input));
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        try {
            assertEquals(expected, MatrixFMath.inverse(input));
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
        MatF4 input = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.000f, 0.330f, 0.000f, 0.330f,
                0.330f, 0.330f);
        MatF4 expected = new MatF4(1.010101f, 1.010101f, 1.010101f, -2.020202f,
                -2.020202f, 1.010101f, 1.010101f, 1.010101f, 1.010101f,
                1.010101f, -2.020202f, 1.010101f, 1.010101f, -2.020202f,
                1.010101f, 1.010101f);
        try {
            assertEquals(expected, MatrixFMath.inverse(input));
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f,
                0f, 0f, 1f);
        expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f,
                0f, 0f, 0f, 1f);
        try {
            assertEquals(expected, MatrixFMath.inverse(input));
        } catch (InverseNotAvailableException e) {
            fail("determinant should not be 0 here, but it is.");
        }

        input = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f);
        try {
            assertEquals(expected, MatrixFMath.inverse(input));
            fail("determinant should be 0 here, but it is not.");
        } catch (InverseNotAvailableException e) {
        }
    }

}
