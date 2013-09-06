package nl.esciencecenter.esight.math;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import org.junit.Test;

public class VecF3Test {

    @Test
    public final void testVecF3() {
        VecF3 input = new VecF3();
        VecF3 expected = new VecF3(0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testVecF3VecF3() {
        // Test normal case
        VecF3 input = new VecF3(new VecF3());
        VecF3 expected = new VecF3(0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF3(new VecF3(1, 0, 0));
        expected = new VecF3(1, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF3(new VecF3(Float.NaN, Float.NaN, Float.NaN));
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testVecF3FloatFloatFloat() {
        // Test normal case
        VecF3 input = new VecF3(0f, 0f, 0f);
        VecF3 expected = new VecF3();

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF3(1f, 0, 0);
        expected = new VecF3(1, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF3(0, 0, 0);
        expected = new VecF3(0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF3(Float.NaN, Float.NaN, Float.NaN);
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testNeg() {
        // Test normal case
        VecF3 input = new VecF3(0f, 0f, 0f).neg();
        VecF3 expected = new VecF3(-0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF3(1f, 0, 0).neg();
        expected = new VecF3(-1f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(-1f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getZ(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF3(0, 0, 0).neg();
        expected = new VecF3(-0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(-0f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getZ(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF3(Float.NaN, Float.NaN, Float.NaN).neg();
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddVecF3() {
        // Test normal case
        VecF3 input1 = new VecF3(0f, 0f, 0f);
        VecF3 input2 = new VecF3(0f, 0f, 0f);
        VecF3 expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 0f);
        input2 = new VecF3(1f, 0f, 0f);
        expected = new VecF3(2f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 0f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 345f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF3(0f, 0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new VecF3(Float.NaN, Float.NaN, 345f);
        input2 = new VecF3(Float.NaN, 0f, 0f);
        expected = new VecF3(Float.NaN, Float.NaN, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testSub() {
        // Test normal case
        VecF3 input1 = new VecF3(0f, 0f, 0f);
        VecF3 input2 = new VecF3(0f, 0f, 0f);
        VecF3 expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = new VecF3(1f, 0f, 1f);
        expected = new VecF3(0f, 0f, 1f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 0f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF3(2f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 345f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF3(2f, 0f, 345f);

        assertEquals(expected, input1.sub(input2));

        // Test weird case
        input1 = new VecF3(Float.NaN, Float.NaN, 345f);
        input2 = new VecF3(Float.NaN, 0f, 0f);
        expected = new VecF3(Float.NaN, Float.NaN, 345f);

        assertEquals(expected, input1.sub(input2));
    }

    @Test
    public final void testMul() {
        // Test normal case
        VecF3 input1 = new VecF3(0f, 0f, 0f);
        float input2 = 0f;
        VecF3 expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = 1f;
        expected = new VecF3(1f, 0f, 2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = 10f;
        expected = new VecF3(10f, 0f, 20f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = -1f;
        expected = new VecF3(-1f, -0f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF3(Float.NaN, Float.NaN, 2f);
        input2 = -1f;
        expected = new VecF3(Float.NaN, Float.NaN, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = Float.NaN;
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.mul(input2));
    }

    @Test
    public final void testDiv() {
        // Test normal case
        VecF3 input1 = new VecF3(0f, 0f, 0f);
        float input2 = 0f;
        VecF3 expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = 1f;
        expected = new VecF3(1f, 0f, 2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = 10f;
        expected = new VecF3(0.1f, 0f, 0.2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = -1f;
        expected = new VecF3(-1f, -0f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF3(Float.NaN, Float.NaN, 2f);
        input2 = -1f;
        expected = new VecF3(Float.NaN, Float.NaN, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF3(1f, 0f, 2f);
        input2 = Float.NaN;
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.div(input2));
    }

    @Test
    public final void testAsBuffer() {
        // Test normal case
        VecF3 input1 = new VecF3(0f, 0f, 0f);
        FloatBuffer expected = FloatBuffer.allocate(3);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF3(1f, 0f, -1f);

        expected = FloatBuffer.allocate(3);
        expected.put(1f);
        expected.put(0f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF3(Float.NaN, 0f, -1f);

        expected = FloatBuffer.allocate(3);
        expected.put(Float.NaN);
        expected.put(0f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());
    }

    @Test
    public final void testGetSize() {
        // Test normal case
        VecF3 input1 = new VecF3();
        int expected = 3;

        assertEquals(expected, input1.getSize());

        // Test normal case
        input1 = new VecF3(1f, 0f, 0f);
        expected = 3;

        assertEquals(expected, input1.getSize());
    }
}
