package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.Float4Matrix;
import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float4Vector;

import org.junit.Test;

public class MatF4Test {

    @Test
    public final void testMatF4() {
        Float4Matrix input1 = new Float4Matrix();
        Float4Matrix expected = new Float4Matrix(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
        for (int i = 0; i < 16; i++) {
            if (i == 0 || i == 5 || i == 10 || i == 15) {
                assertEquals(1, input1.asArray()[i], FloatMatrixMath.getEpsilon());
            } else {
                assertEquals(0, input1.asArray()[i], FloatMatrixMath.getEpsilon());
            }
        }
    }

    @Test
    public final void testMatF4Float() {
        Float4Matrix input1 = new Float4Matrix(0f);
        Float4Matrix expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(1f);
        expected = new Float4Matrix(1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF4VecF4VecF4VecF4VecF4() {
        Float4Matrix input1 = new Float4Matrix(new Float4Vector(), new Float4Vector(), new Float4Vector(), new Float4Vector());
        Float4Matrix expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(new Float4Vector(1, 0, 0, 0), new Float4Vector(1, 0, 0, 0), new Float4Vector(1, 0, 0, 0), new Float4Vector(1, 0, 0, 0));
        expected = new Float4Matrix(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF4FloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloatFloat() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMatF4MatF4() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        expected = new Float4Matrix(input1);

        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulMatF4() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix input2 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(0.21780002f, 0.21780002f, 0.21780002f, 0.32670003f, 0.32670003f, 0.21780002f,
                0.21780002f, 0.21780002f, 0.21780002f, 0.21780002f, 0.32670003f, 0.21780002f, 0.21780002f, 0.32670003f,
                0.21780002f, 0.21780002f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = new Float4Matrix(0f);
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new Float4Matrix();
        expected = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAddMatF4() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix input2 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(0.66f, 0.0f, 0.66f, 0.66f, 0.66f, 0.66f, 0.66f, 0.0f, 0.66f, 0.66f, 0.0f, 0.66f,
                0.0f, 0.66f, 0.66f, 0.66f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = new Float4Matrix(0f);
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new Float4Matrix();
        expected = new Float4Matrix(1.330f, 0.000f, 0.330f, 0.330f, 0.330f, 1.330f, 0.330f, 0.000f, 0.330f, 0.330f, 1.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 1.330f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSubMatF4() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix input2 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        Float4Matrix expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = new Float4Matrix(0f);
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f, 0.000f,
                0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        input2 = new Float4Matrix();
        expected = new Float4Matrix(-0.67f, 0.000f, 0.330f, 0.330f, 0.330f, -0.67f, 0.330f, 0.000f, 0.330f, 0.330f, -1.000f,
                0.330f, 0.000f, 0.330f, 0.330f, -0.67f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulNumber() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        Float4Matrix expected = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = 0f;
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new Float4Matrix(1f, 0f, 1f, 1f, 1f, 1f, 1f, 0f, 1f, 1f, 0f, 1f, 0f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAddNumber() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        Float4Matrix expected = new Float4Matrix(1.330f, 1.000f, 1.330f, 1.330f, 1.330f, 1.330f, 1.330f, 1.000f, 1.330f, 1.330f,
                1.000f, 1.330f, 1.000f, 1.330f, 1.330f, 1.330f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = 0f;
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new Float4Matrix(1f, 0.6666666666f, 1f, 1f, 1f, 1f, 1f, 0.6666666666f, 1f, 1f, 0.6666666666f, 1f,
                0.6666666666f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.add(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSubNumber() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        Float4Matrix expected = new Float4Matrix(-0.66999996f, -1.0f, -0.66999996f, -0.66999996f, -0.66999996f, -0.66999996f,
                -0.66999996f, -1.0f, -0.66999996f, -0.66999996f, -1.0f, -0.66999996f, -1.0f, -0.66999996f,
                -0.66999996f, -0.66999996f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = 0f;
        expected = new Float4Matrix(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 0.6666666666f;
        expected = new Float4Matrix(-0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f, -0.33333334f, -0.33333334f,
                -0.33333334f, -0.6666667f, -0.33333334f, -0.33333334f, -0.6666667f, -0.33333334f, -0.6666667f,
                -0.33333334f, -0.33333334f, -0.33333334f);

        assertArrayEquals(expected.asArray(), input1.sub(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testDiv() {
        Float4Matrix input1 = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);
        float input2 = 1f;
        Float4Matrix expected = new Float4Matrix(0.330f, 0.000f, 0.330f, 0.330f, 0.330f, 0.330f, 0.330f, 0.000f, 0.330f, 0.330f,
                0.000f, 0.330f, 0.000f, 0.330f, 0.330f, 0.330f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = 0f;
        expected = new Float4Matrix(Float.NaN);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = 3f;
        expected = new Float4Matrix(0.11111112f, 0.0f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.11111112f, 0.0f,
                0.11111112f, 0.11111112f, 0.0f, 0.11111112f, 0.0f, 0.11111112f, 0.11111112f, 0.11111112f);

        assertArrayEquals(expected.asArray(), input1.div(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testMulVecF4() {
        Float4Matrix input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        Float4Vector input2 = new Float4Vector(1f, 1f, 1f, 1f);
        Float4Vector expected = new Float4Vector(1f, 1f, 1f, 1f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f);
        input2 = new Float4Vector(0f, 0f, 0f, 0f);
        expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
                0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.000f, 0.3333333333f, 0.000f, 0.3333333333f,
                0.3333333333f, 0.3333333333f);
        input2 = new Float4Vector(3f, 3f, 3f, 0f);
        expected = new Float4Vector(2f, 3f, 2f, 2f);

        assertArrayEquals(expected.asArray(), input1.mul(input2).asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAsBuffer() {
        Float4Matrix input1 = new Float4Matrix(0.3333333333f, 0.000f, 0.3333333333f, 0.3333333333f, 0.3333333333f, 0.3333333333f,
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

        input1 = new Float4Matrix();

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
        Float4Matrix input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float expected = 0f;
        assertEquals(expected, input1.get(0, 0), FloatMatrixMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(0, 1), FloatMatrixMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(0, 2), FloatMatrixMath.getEpsilon());

        expected = 4f;
        assertEquals(expected, input1.get(1, 0), FloatMatrixMath.getEpsilon());

        expected = 8f;
        assertEquals(expected, input1.get(2, 0), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testGetInt() {
        Float4Matrix input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float expected = 0f;
        assertEquals(expected, input1.get(0), FloatMatrixMath.getEpsilon());

        expected = 1f;
        assertEquals(expected, input1.get(1), FloatMatrixMath.getEpsilon());

        expected = 2f;
        assertEquals(expected, input1.get(2), FloatMatrixMath.getEpsilon());

        expected = 5f;
        assertEquals(expected, input1.get(5), FloatMatrixMath.getEpsilon());

        expected = 15f;
        assertEquals(expected, input1.get(15), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSetIntIntFloat() {
        Float4Matrix input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float input2 = 345f;
        input1.set(0, 0, input2);
        Float4Matrix expected = new Float4Matrix(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = 0f;
        input1.set(0, 1, input2);
        expected = new Float4Matrix(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = Float.NaN;
        input1.set(1, 1, input2);
        expected = new Float4Matrix(0f, 1f, 2f, 3f, 4f, Float.NaN, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testSetIntFloat() {
        Float4Matrix input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        float input2 = 345f;
        input1.set(0, input2);
        Float4Matrix expected = new Float4Matrix(345f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = 0f;
        input1.set(1, input2);
        expected = new Float4Matrix(0f, 0f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());

        input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);

        input2 = Float.NaN;
        input1.set(5, input2);
        expected = new Float4Matrix(0f, 1f, 2f, 3f, 4f, Float.NaN, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        assertArrayEquals(expected.asArray(), input1.asArray(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testGetSize() {
        Float4Matrix input1 = new Float4Matrix(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 1f, 12f, 13f, 14f, 15f);
        int expected = 16;

        assertEquals(expected, input1.getSize());

        input1 = new Float4Matrix();
        expected = 16;

        assertEquals(expected, input1.getSize());
    }

}
