package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.MatF3;
import nl.esciencecenter.neon.math.MatrixFMath;
import nl.esciencecenter.neon.math.VecF3;

import org.junit.Test;

public class MatF3Test {

    @Test
    public final void testMatF3() {
        MatF3 input1 = new MatF3();
        MatF3 expected = new MatF3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
        for (int i = 0; i < 9; i++) {
            if (i == 0 || i == 4 || i == 8) {
                assertEquals(1, input1.asArray()[i], MatrixFMath.getEpsilon());
            } else {
                assertEquals(0, input1.asArray()[i], MatrixFMath.getEpsilon());
            }
        }
    }

    @Test
    public final void testMatF3Float() {
        MatF3 input1 = new MatF3(0f);
        MatF3 expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(1f);
        expected = new MatF3(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF3VecF3VecF3VecF3VecF3() {
        MatF3 input1 = new MatF3(new VecF3(), new VecF3(), new VecF3());
        MatF3 expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(new VecF3(1, 0, 0), new VecF3(1, 0, 0), new VecF3(1, 0, 0));
        expected = new MatF3(1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF3FloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 expected = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF3MatF3() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 expected = new MatF3(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        expected = new MatF3(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulMatF3() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 input2 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 expected = new MatF3(0.21780002f, 0.0f, 0.21780002f, 0.32670003f, 0.10890001f, 0.32670003f, 0.21780002f,
                0.0f, 0.21780002f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = new MatF3(0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        input2 = new MatF3();
        expected = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddMatF3() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 input2 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 expected = new MatF3(0.66f, 0.0f, 0.66f, 0.66f, 0.66f, 0.66f, 0.66f, 0.0f, 0.66f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = new MatF3(0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        input2 = new MatF3();
        expected = new MatF3(1.33f, 0.0f, 0.33f, 0.33f, 1.33f, 0.33f, 0.33f, 0.0f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubMatF3() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 input2 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        MatF3 expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = new MatF3(0f);
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        input2 = new MatF3();
        expected = new MatF3(-0.66999996f, 0.0f, 0.33f, 0.33f, -0.66999996f, 0.33f, 0.33f, 0.0f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulNumber() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        float input2 = 1f;
        MatF3 expected = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = 0f;
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF3(1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddNumber() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        float input2 = 1f;
        MatF3 expected = new MatF3(1.33f, 1.0f, 1.33f, 1.33f, 1.33f, 1.33f, 1.33f, 1.0f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = 0f;
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF3(1.0f, 0.6666667f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.6666667f, 1.0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubNumber() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        float input2 = 1f;
        MatF3 expected = new MatF3(-0.66999996f, -1.0f, -0.66999996f, -0.66999996f, -0.66999996f, -0.66999996f,
                -0.66999996f, -1.0f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = 0f;
        expected = new MatF3(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF3(-0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f, -0.33333334f, -0.33333334f,
                -0.33333334f, -0.6666667f, -0.33333334f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testDiv() {
        MatF3 input1 = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);
        float input2 = 1f;
        MatF3 expected = new MatF3(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = 0f;
        expected = new MatF3(Float.NaN);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF3(0.11111112f, 0.0f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.0f,
                0.11111112f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulVecF3() {
        MatF3 input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        VecF3 input2 = new VecF3(1f, 1f, 1f);
        VecF3 expected = new VecF3(0.6666667f, 1f, 0.6666667f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f);
        input2 = new VecF3(0f, 0f, 0f);
        expected = new VecF3(0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);
        input2 = new VecF3(3f, 3f, 3f);
        expected = new VecF3(2f, 3f, 2f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAsBuffer() {
        MatF3 input1 = new MatF3(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f);

        FloatBuffer expected = FloatBuffer.allocate(9);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        input1 = new MatF3();

        expected = FloatBuffer.allocate(9);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

    }

    @Test
    public final void testGetIntInt() {
        MatF3 input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        float expected = 0f;
        assertEquals(expected, input1.get(0, 0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(0, 1), MatrixFMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(0, 2), MatrixFMath.getEpsilon());

        expected = 3f;
        assertEquals(expected, input1.get(1, 0), MatrixFMath.getEpsilon());

        expected = 6f;
        assertEquals(expected, input1.get(2, 0), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetInt() {
        MatF3 input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        float expected = 0f;
        assertEquals(expected, input1.get(0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(1), MatrixFMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(2), MatrixFMath.getEpsilon());

        expected = 5f;
        assertEquals(expected, input1.get(5), MatrixFMath.getEpsilon());

        expected = 8f;
        assertEquals(expected, input1.get(8), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntIntFloat() {
        MatF3 input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        float input2 = 345f;
        input1.set(0, 0, input2);
        MatF3 expected = new MatF3(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        input2 = 0f;
        input1.set(0, 1, input2);
        expected = new MatF3(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        input2 = Float.NaN;
        input1.set(1, 1, input2);
        expected = new MatF3(0f, 1f, 2f, 3f, Float.NaN, 5f, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntFloat() {
        MatF3 input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        float input2 = 345f;
        input1.set(0, input2);
        MatF3 expected = new MatF3(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        input2 = 0f;
        input1.set(1, input2);
        expected = new MatF3(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);

        input2 = Float.NaN;
        input1.set(5, input2);
        expected = new MatF3(0f, 1f, 2f, 3f, 4f, Float.NaN, 6f, 7f, 8f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetSize() {
        MatF3 input1 = new MatF3(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f);
        int expected = 9;

        assertEquals(expected, input1.getSize());

        input1 = new MatF3();
        expected = 9;

        assertEquals(expected, input1.getSize());
    }

}
