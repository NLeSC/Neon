package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.MatrixFMath;
import nl.esciencecenter.neon.math.VecF2;

import org.junit.Test;

public class VecF2Test {

    @Test
    public final void testVecF2() {
        VecF2 input = new VecF2();
        VecF2 expected = new VecF2(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

    }

    @Test
    public final void testVecF2VecF2() {
        // Test normal case
        VecF2 input = new VecF2(new VecF2());
        VecF2 expected = new VecF2(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF2(new VecF2(1, 0));
        expected = new VecF2(1, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF2(new VecF2(Float.NaN, Float.NaN));
        expected = new VecF2(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());

    }

    @Test
    public final void testVecF2FloatFloatFloat() {
        // Test normal case
        VecF2 input = new VecF2(0f, 0f);
        VecF2 expected = new VecF2();

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF2(1f, 0);
        expected = new VecF2(1, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF2(0, 0);
        expected = new VecF2(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF2(Float.NaN, Float.NaN);
        expected = new VecF2(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());

    }

    @Test
    public final void testNeg() {
        // Test normal case
        VecF2 input = new VecF2(0f, 0f).neg();
        VecF2 expected = new VecF2(-0f, -0f);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF2(1f, 0).neg();
        expected = new VecF2(-1f, -0f);

        assertEquals(expected, input);
        assertEquals(-1f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF2(0, 0).neg();
        expected = new VecF2(-0f, -0f);

        assertEquals(expected, input);
        assertEquals(-0f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF2(Float.NaN, Float.NaN).neg();
        expected = new VecF2(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());

    }

    @Test
    public final void testAddVecF2() {
        // Test normal case
        VecF2 input1 = new VecF2(0f, 0f);
        VecF2 input2 = new VecF2(0f, 0f);
        VecF2 expected = new VecF2(0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF2(1f, 0f);
        input2 = new VecF2(1f, 0f);
        expected = new VecF2(2f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF2(1f, 0f);
        input2 = new VecF2(-1f, 0f);
        expected = new VecF2(0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF2(1f, 345f);
        input2 = new VecF2(-1f, 0f);
        expected = new VecF2(0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new VecF2(Float.NaN, 345f);
        input2 = new VecF2(Float.NaN, 0f);
        expected = new VecF2(Float.NaN, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testSub() {
        // Test normal case
        VecF2 input1 = new VecF2(0f, 0f);
        VecF2 input2 = new VecF2(0f, 0f);
        VecF2 expected = new VecF2(0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = new VecF2(1f, 1f);
        expected = new VecF2(0f, 1f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF2(1f, 0f);
        input2 = new VecF2(-1f, 0f);
        expected = new VecF2(2f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF2(1f, 345f);
        input2 = new VecF2(-1f, 0f);
        expected = new VecF2(2f, 345f);

        assertEquals(expected, input1.sub(input2));

        // Test weird case
        input1 = new VecF2(Float.NaN, 345f);
        input2 = new VecF2(Float.NaN, 0f);
        expected = new VecF2(Float.NaN, 345f);

        assertEquals(expected, input1.sub(input2));
    }

    @Test
    public final void testMul() {
        // Test normal case
        VecF2 input1 = new VecF2(0f, 0f);
        float input2 = 0f;
        VecF2 expected = new VecF2(0f, 0f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = 1f;
        expected = new VecF2(1f, 2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = 10f;
        expected = new VecF2(10f, 20f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = -1f;
        expected = new VecF2(-1f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF2(Float.NaN, 2f);
        input2 = -1f;
        expected = new VecF2(Float.NaN, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = Float.NaN;
        expected = new VecF2(Float.NaN, Float.NaN);

        assertEquals(expected, input1.mul(input2));
    }

    @Test
    public final void testDiv() {
        // Test normal case
        VecF2 input1 = new VecF2(0f, 0f);
        float input2 = 0f;
        VecF2 expected = new VecF2(0f, 0f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = 1f;
        expected = new VecF2(1f, 2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = 10f;
        expected = new VecF2(0.1f, 0.2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = -1f;
        expected = new VecF2(-1f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF2(Float.NaN, 2f);
        input2 = -1f;
        expected = new VecF2(Float.NaN, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF2(1f, 2f);
        input2 = Float.NaN;
        expected = new VecF2(Float.NaN, Float.NaN);

        assertEquals(expected, input1.div(input2));
    }

    @Test
    public final void testAsBuffer() {
        // Test normal case
        VecF2 input1 = new VecF2(0f, 0f);
        FloatBuffer expected = FloatBuffer.allocate(2);
        expected.put(0f);
        expected.put(0f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF2(1f, -1f);

        expected = FloatBuffer.allocate(2);
        expected.put(1f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF2(Float.NaN, -1f);

        expected = FloatBuffer.allocate(2);
        expected.put(Float.NaN);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());
    }

    @Test
    public final void testGetSize() {
        // Test normal case
        VecF2 input1 = new VecF2();
        int expected = 2;

        assertEquals(expected, input1.getSize());

        // Test normal case
        input1 = new VecF2(1f, 0f);
        expected = 2;

        assertEquals(expected, input1.getSize());
    }
}
