package nl.esciencecenter.esight.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import org.junit.Test;

public class MatF4Test {

    @Test
    public final void testMatF4() {
        MatF4 input1 = new MatF4();
        MatF4 expected = new MatF4(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 5 || i == 10 || i == 15) {
                assertEquals(1, input1.asArray()[i], MatrixFMath.getEpsilon());
            } else {
                assertEquals(0, input1.asArray()[i], MatrixFMath.getEpsilon());
            }
        }
    }

    @Test
    public final void testMatF4Float() {
        MatF4 input1 = new MatF4(0f);
        MatF4 expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(1f);
        expected = new MatF4(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF4VecF4VecF4VecF4VecF4() {
        MatF4 input1 = new MatF4(new VecF4(), new VecF4(), new VecF4(), new VecF4());
        MatF4 expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(new VecF4(1, 0, 0, 0), new VecF4(1, 0, 0, 0), new VecF4(1, 0, 0, 0), new VecF4(1, 0, 0, 0));
        expected = new MatF4(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF4FloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMatF4MatF4() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        expected = new MatF4(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulMatF4() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 input2 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(0.21780002f, 0.21780002f, 0.21780002f, 0.32670003f, 0.32670003f, 0.21780002f,
                0.21780002f, 0.21780002f, 0.21780002f, 0.21780002f, 0.32670003f, 0.21780002f, 0.21780002f, 0.32670003f,
                0.21780002f, 0.21780002f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = new MatF4(0f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new MatF4();
        expected = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddMatF4() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 input2 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(0.66f, 0.0f, 0.66f, 0.66f, 0.66f, 0.66f, 0.66f, 0.0f, 0.66f, 0.66f, 0.0f, 0.66f,
                0.0f, 0.66f, 0.66f, 0.66f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = new MatF4(0f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new MatF4();
        expected = new MatF4(1.330f, 0.000f, 0.330f, 0.330f, 0.330f, 1.330f, 0.330f, 0.000f, 0.330f, 0.330f, 1.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 1.330f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubMatF4() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 input2 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        MatF4 expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = new MatF4(0f);
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new MatF4();
        expected = new MatF4(-0.67f, 0.000f, 0.330f, 0.330f, 0.330f, -0.67f, 0.330f, 0.000f, 0.330f, 0.330f, -1.000f,
                0.330f, 0.000f, 0.330f, 0.330f, -0.67f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulNumber() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF4 expected = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = 0f;
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF4(1f, 0f, 1f, 1f, 1f, 1f, 1f, 0f, 1f, 1f, 0f, 1f, 0f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddNumber() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF4 expected = new MatF4(1.330f, 1.000f, 1.330f, 1.330f, 1.330f, 1.330f, 1.330f, 1.000f, 1.330f, 1.330f,
                1.000f, 1.330f, 1.000f, 1.330f, 1.330f, 1.330f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = 0f;
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF4(1f, 0.6666666666f, 1f, 1f, 1f, 1f, 1f, 0.6666666666f, 1f, 1f, 0.6666666666f, 1f,
                0.6666666666f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSubNumber() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF4 expected = new MatF4(-0.66999996f, -1.0f, -0.66999996f, -0.66999996f, -0.66999996f, -0.66999996f,
                -0.66999996f, -1.0f, -0.66999996f, -0.66999996f, -1.0f, -0.66999996f, -1.0f, -0.66999996f,
                -0.66999996f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = 0f;
        expected = new MatF4(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new MatF4(-0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f, -0.33333334f, -0.33333334f,
                -0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f, -0.6666667f, -0.33333334f, -0.6666667f,
                -0.33333334f, -0.33333334f, -0.33333334f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testDiv() {
        MatF4 input1 = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        MatF4 expected = new MatF4(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = 0f;
        expected = new MatF4(Float.NaN);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new MatF4(0.11111112f, 0.0f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.0f,
                0.11111112f, 0.11111112f, 0.0f, 0.11111112f, 0.0f, 0.11111112f, 0.11111112f, 0.11111112f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testMulVecF4() {
        MatF4 input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        VecF4 input2 = new VecF4(1f, 1f, 1f, 1f);
        VecF4 expected = new VecF4(1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f);
        input2 = new VecF4(0f, 0f, 0f, 0f);
        expected = new VecF4(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = new VecF4(3f, 3f, 3f, 0f);
        expected = new VecF4(2f, 3f, 2f, 2f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAsBuffer() {
        MatF4 input1 = new MatF4(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);

        FloatBuffer expected = FloatBuffer.allocate(16);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.000f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.put(0.3333333333f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        input1 = new MatF4();

        expected = FloatBuffer.allocate(16);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

    }

    @Test
    public final void testGetIntInt() {
        MatF4 input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float expected = 0f;
        assertEquals(expected, input1.get(0, 0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(0, 1), MatrixFMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(0, 2), MatrixFMath.getEpsilon());

        expected = 4f;
        assertEquals(expected, input1.get(1, 0), MatrixFMath.getEpsilon());

        expected = 8f;
        assertEquals(expected, input1.get(2, 0), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetInt() {
        MatF4 input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float expected = 0f;
        assertEquals(expected, input1.get(0), MatrixFMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(1), MatrixFMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(2), MatrixFMath.getEpsilon());

        expected = 5f;
        assertEquals(expected, input1.get(5), MatrixFMath.getEpsilon());

        expected = 15f;
        assertEquals(expected, input1.get(15), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntIntFloat() {
        MatF4 input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float input2 = 345f;
        input1.set(0, 0, input2);
        MatF4 expected = new MatF4(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = 0f;
        input1.set(0, 1, input2);
        expected = new MatF4(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = Float.NaN;
        input1.set(1, 1, input2);
        expected = new MatF4(0f, 1f, 2f, 3f, 4f, Float.NaN, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testSetIntFloat() {
        MatF4 input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float input2 = 345f;
        input1.set(0, input2);
        MatF4 expected = new MatF4(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = 0f;
        input1.set(1, input2);
        expected = new MatF4(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());

        input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = Float.NaN;
        input1.set(5, input2);
        expected = new MatF4(0f, 1f, 2f, 3f, 4f, Float.NaN, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testGetSize() {
        MatF4 input1 = new MatF4(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        int expected = 16;

        assertEquals(expected, input1.getSize());

        input1 = new MatF4();
        expected = 16;

        assertEquals(expected, input1.getSize());
    }

}
