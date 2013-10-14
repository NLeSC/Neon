package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.Float2Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float2Vector;

import org.junit.Test;

public class MatF2Test {

    @Test
    public final void testMatF2() {
        Float2Matrix input1 = new Float2Matrix();
        Float2Matrix expected = new Float2Matrix(1f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 3) {
                assertEquals(1, input1.asArray()[i], FloatMatrixMath.getEpsilon());
            } else {
                assertEquals(0, input1.asArray()[i], FloatMatrixMath.getEpsilon());
            }
        }
    }

    @Test
    public final void testMatF2Float() {
        Float2Matrix input1 = new Float2Matrix(0f);
        Float2Matrix expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(1f);
        expected = new Float2Matrix(1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF2VecF2VecF2VecF2VecF2() {
        Float2Matrix input1 = new Float2Matrix(new Float2Vector(), new Float2Vector());
        Float2Matrix expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(new Float2Vector(1, 0), new Float2Vector(1, 0));
        expected = new Float2Matrix(1f, 0f, 1f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF2FloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix expected = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF2MatF2() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix expected = new Float2Matrix(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        expected = new Float2Matrix(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulMatF2() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix input2 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix expected = new Float2Matrix(0.10890001f, 0.0f, 0.21780002f, 0.10890001f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = new Float2Matrix(0f);
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new Float2Matrix();
        expected = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAddMatF2() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix input2 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix expected = new Float2Matrix(0.66f, 0.0f, 0.66f, 0.66f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = new Float2Matrix(0f);
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new Float2Matrix();
        expected = new Float2Matrix(1.33f, 0.0f, 0.33f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSubMatF2() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix input2 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        Float2Matrix expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = new Float2Matrix(0f);
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new Float2Matrix();
        expected = new Float2Matrix(-0.66999996f, 0.0f, 0.33f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulNumber() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        Float2Matrix expected = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = 0f;
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new Float2Matrix(1.0f, 0.0f, 1.0f, 1.0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAddNumber() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        Float2Matrix expected = new Float2Matrix(1.33f, 1.0f, 1.33f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = 0f;
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new Float2Matrix(1f, 0.6666666666f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSubNumber() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        Float2Matrix expected = new Float2Matrix(-0.66999996f, -1.0f, -0.66999996f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = 0f;
        expected = new Float2Matrix(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new Float2Matrix(-0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testDiv() {
        Float2Matrix input1 = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        Float2Matrix expected = new Float2Matrix(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = 0f;
        expected = new Float2Matrix(Float.NaN);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new Float2Matrix(0.11111112f, 0.0f, 0.11111112f, 0.11111112f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulVecF2() {
        Float2Matrix input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        Float2Vector input2 = new Float2Vector(1f, 1f);
        Float2Vector expected = new Float2Vector(0.33333334f, 0.6666667f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f);
        input2 = new Float2Vector(0f, 0f);
        expected = new Float2Vector(0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = new Float2Vector(3f, 3f);
        expected = new Float2Vector(1f, 2f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAsBuffer() {
        Float2Matrix input1 = new Float2Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);

        FloatBuffer expected = FloatBuffer.allocate(4);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        input1 = new Float2Matrix();

        expected = FloatBuffer.allocate(4);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

    }

    @Test
    public final void testGetIntInt() {
        Float2Matrix input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        float expected = 0f;
        assertEquals(expected, input1.get(0, 0), FloatMatrixMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(0, 1), FloatMatrixMath.getEpsilon());

        expected = 3f;
        assertEquals(expected, input1.get(1, 1), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testGetInt() {
        Float2Matrix input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        float expected = 0f;
        assertEquals(expected, input1.get(0), FloatMatrixMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(1), FloatMatrixMath.getEpsilon());

        expected = 3f;
        assertEquals(expected, input1.get(3), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSetIntIntFloat() {
        Float2Matrix input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        float input2 = 345f;
        input1.set(0, 0, input2);
        Float2Matrix expected = new Float2Matrix(345f, 1f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        input2 = 0f;
        input1.set(0, 1, input2);
        expected = new Float2Matrix(0f, 0f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        input2 = Float.NaN;
        input1.set(1, 1, input2);
        expected = new Float2Matrix(0f, 1f, 2f, Float.NaN);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSetIntFloat() {
        Float2Matrix input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        float input2 = 345f;
        input1.set(0, input2);
        Float2Matrix expected = new Float2Matrix(345f, 1f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        input2 = 0f;
        input1.set(1, input2);
        expected = new Float2Matrix(0f, 0f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float2Matrix(0f, 1f, 2f, 3f);

        input2 = Float.NaN;
        input1.set(3, input2);
        expected = new Float2Matrix(0f, 1f, 2f, Float.NaN);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testGetSize() {
        Float2Matrix input1 = new Float2Matrix(0f, 1f, 2f, 3f);
        int expected = 4;

        assertEquals(expected, input1.getSize());

        input1 = new Float2Matrix();
        expected = 4;

        assertEquals(expected, input1.getSize());
    }

}
