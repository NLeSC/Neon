package nl.esciencecenter.esight.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import org.junit.Test;

public class MatF2Test {

    @Test
    public final void testMatF2() {
        MatF2 input1 = new MatF2();
        MatF2 expected = new MatF2(1f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 3) {
                assertEquals(1, input1.asArray()[i], MatrixFMath.getEpsilon());
            } else {
                assertEquals(0, input1.asArray()[i], MatrixFMath.getEpsilon());
            }
        }
    }

    @Test
    public final void testMatF2Float() {
        MatF2 input1 = new MatF2(0f);
        MatF2 expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(1f);
        expected = new MatF2(1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF2VecF2VecF2VecF2VecF2() {
        MatF2 input1 = new MatF2(new VecF2(), new VecF2());
        MatF2 expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(new VecF2(1, 0), new VecF2(1, 0));
        expected = new MatF2(1f, 0f, 1f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF2FloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 expected = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF2MatF2() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 expected = new MatF2(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        expected = new MatF2(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulMatF2() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 input2 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 expected = new MatF2(0.10890001f, 0.0f, 0.21780002f, 0.10890001f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = new MatF2(0f);
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new MatF2();
        expected = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddMatF2() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 input2 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 expected = new MatF2(0.66f, 0.0f, 0.66f, 0.66f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = new MatF2(0f);
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new MatF2();
        expected = new MatF2(1.33f, 0.0f, 0.33f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubMatF2() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 input2 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        MatF2 expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = new MatF2(0f);
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        input2 = new MatF2();
        expected = new MatF2(-0.66999996f, 0.0f, 0.33f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulNumber() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF2 expected = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = 0f;
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF2(1.0f, 0.0f, 1.0f, 1.0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddNumber() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF2 expected = new MatF2(1.33f, 1.0f, 1.33f, 1.33f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = 0f;
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF2(1f, 0.6666666666f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubNumber() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF2 expected = new MatF2(-0.66999996f, -1.0f, -0.66999996f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = 0f;
        expected = new MatF2(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF2(-0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testDiv() {
        MatF2 input1 = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF2 expected = new MatF2(0.330f, 0.000f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = 0f;
        expected = new MatF2(Float.NaN);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF2(0.11111112f, 0.0f, 0.11111112f, 0.11111112f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulVecF2() {
        MatF2 input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        VecF2 input2 = new VecF2(1f, 1f);
        VecF2 expected = new VecF2(0.33333334f, 0.6666667f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f);
        input2 = new VecF2(0f, 0f);
        expected = new VecF2(0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);
        input2 = new VecF2(3f, 3f);
        expected = new VecF2(1f, 2f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAsBuffer() {
        MatF2 input1 = new MatF2(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f);

        FloatBuffer expected = FloatBuffer.allocate(4);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        input1 = new MatF2();

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
        MatF2 input1 = new MatF2(0f, 1f, 2f, 3f);

        float expected = 0f;
        assertEquals(expected, input1.get(0, 0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(0, 1), MatrixFMath.getEpsilon());

        expected = 3f;
        assertEquals(expected, input1.get(1, 1), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetInt() {
        MatF2 input1 = new MatF2(0f, 1f, 2f, 3f);

        float expected = 0f;
        assertEquals(expected, input1.get(0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(1), MatrixFMath.getEpsilon());

        expected = 3f;
        assertEquals(expected, input1.get(3), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntIntFloat() {
        MatF2 input1 = new MatF2(0f, 1f, 2f, 3f);

        float input2 = 345f;
        input1.set(0, 0, input2);
        MatF2 expected = new MatF2(345f, 1f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f, 1f, 2f, 3f);

        input2 = 0f;
        input1.set(0, 1, input2);
        expected = new MatF2(0f, 0f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f, 1f, 2f, 3f);

        input2 = Float.NaN;
        input1.set(1, 1, input2);
        expected = new MatF2(0f, 1f, 2f, Float.NaN);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntFloat() {
        MatF2 input1 = new MatF2(0f, 1f, 2f, 3f);

        float input2 = 345f;
        input1.set(0, input2);
        MatF2 expected = new MatF2(345f, 1f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f, 1f, 2f, 3f);

        input2 = 0f;
        input1.set(1, input2);
        expected = new MatF2(0f, 0f, 2f, 3f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF2(0f, 1f, 2f, 3f);

        input2 = Float.NaN;
        input1.set(3, input2);
        expected = new MatF2(0f, 1f, 2f, Float.NaN);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetSize() {
        MatF2 input1 = new MatF2(0f, 1f, 2f, 3f);
        int expected = 4;

        assertEquals(expected, input1.getSize());

        input1 = new MatF2();
        expected = 4;

        assertEquals(expected, input1.getSize());
    }

}
