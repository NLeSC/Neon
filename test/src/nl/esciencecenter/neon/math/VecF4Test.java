package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.MatrixFMath;
import nl.esciencecenter.neon.math.VecF3;
import nl.esciencecenter.neon.math.VecF4;

import org.junit.Test;

public class VecF4Test {

    @Test
    public final void testVecF4() {
        VecF4 input = new VecF4();
        VecF4 expected = new VecF4(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testVecF4VecF4() {
        // Test normal case
        VecF4 input = new VecF4(new VecF4());
        VecF4 expected = new VecF4(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(new VecF4(1, 0, 0, 0));
        expected = new VecF4(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF4(new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN));
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testVecF4VecF3Float() {
        // Test normal case
        VecF4 input = new VecF4(new VecF3(), 0f);
        VecF4 expected = new VecF4(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(new VecF3(1, 0, 0), 0f);
        expected = new VecF4(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(new VecF3(0, 0, 0), 1f);
        expected = new VecF4(0, 0, 0, 1);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(1, input.getW(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF4(new VecF3(Float.NaN, Float.NaN, Float.NaN), Float.NaN);
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testVecF4FloatFloatFloatFloat() {
        // Test normal case
        VecF4 input = new VecF4(0f, 0f, 0f, 0f);
        VecF4 expected = new VecF4();

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(1f, 0, 0, 0f);
        expected = new VecF4(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(0, 0, 0, 1f);
        expected = new VecF4(0, 0, 0, 1);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(1, input.getW(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testNeg() {
        // Test normal case
        VecF4 input = new VecF4(0f, 0f, 0f, 0f).neg();
        VecF4 expected = new VecF4(-0f, -0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(0, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(1f, 0, 0, 0f).neg();
        expected = new VecF4(-1f, -0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(-1f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getW(), MatrixFMath.getEpsilon());

        // Test normal case
        input = new VecF4(0, 0, 0, 1f).neg();
        expected = new VecF4(-0f, -0f, -0f, -1f);

        assertEquals(expected, input);
        assertEquals(-0f, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(-0f, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(-1f, input.getW(), MatrixFMath.getEpsilon());

        // Test weird case
        input = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN).neg();
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), MatrixFMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), MatrixFMath.getEpsilon());
    }

    @Test
    public final void testAddVecF4() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        VecF4 input2 = new VecF4(0f, 0f, 0f, 0f);
        VecF4 expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        input2 = new VecF4(1f, 0f, 0f, 0f);
        expected = new VecF4(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        input2 = new VecF4(-1f, 0f, 0f, 0f);
        expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 345f);
        input2 = new VecF4(-1f, 0f, 0f, 0f);
        expected = new VecF4(0f, 0f, 0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new VecF4(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new VecF4(Float.NaN, 0f, 0f, 0f);
        expected = new VecF4(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testAddVecF3() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        VecF3 input2 = new VecF3(0f, 0f, 0f);
        VecF4 expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        input2 = new VecF3(1f, 0f, 0f);
        expected = new VecF4(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 345f);
        input2 = new VecF3(-1f, 0f, 0f);
        expected = new VecF4(0f, 0f, 0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new VecF4(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new VecF3(Float.NaN, 0f, 0f);
        expected = new VecF4(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testSub() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        VecF4 input2 = new VecF4(0f, 0f, 0f, 0f);
        VecF4 expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = new VecF4(1f, 0f, 0f, 1f);
        expected = new VecF4(0f, 0f, 0f, 1f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        input2 = new VecF4(-1f, 0f, 0f, 0f);
        expected = new VecF4(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 345f);
        input2 = new VecF4(-1f, 0f, 0f, 0f);
        expected = new VecF4(2f, 0f, 0f, 345f);

        assertEquals(expected, input1.sub(input2));

        // Test weird case
        input1 = new VecF4(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new VecF4(Float.NaN, 0f, 0f, 0f);
        expected = new VecF4(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.sub(input2));
    }

    @Test
    public final void testMul() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        float input2 = 0f;
        VecF4 expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = 1f;
        expected = new VecF4(1f, 0f, 0f, 2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = 10f;
        expected = new VecF4(10f, 0f, 0f, 20f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = -1f;
        expected = new VecF4(-1f, -0f, -0f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF4(Float.NaN, Float.NaN, 0f, 2f);
        input2 = -1f;
        expected = new VecF4(Float.NaN, Float.NaN, -0f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = Float.NaN;
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.mul(input2));
    }

    @Test
    public final void testDiv() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        float input2 = 0f;
        VecF4 expected = new VecF4(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = 1f;
        expected = new VecF4(1f, 0f, 0f, 2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = 10f;
        expected = new VecF4(0.1f, 0f, 0f, 0.2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = -1f;
        expected = new VecF4(-1f, -0f, -0f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF4(Float.NaN, Float.NaN, 0f, 2f);
        input2 = -1f;
        expected = new VecF4(Float.NaN, Float.NaN, -0f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 2f);
        input2 = Float.NaN;
        expected = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.div(input2));
    }

    @Test
    public final void testStripAlpha() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        VecF3 expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test normal case
        input1 = new VecF4(0f, 0f, 0f, 1f);
        expected = new VecF3(0f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 1f);
        expected = new VecF3(1f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test weird case
        input1 = new VecF4(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
        expected = new VecF3(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.stripAlpha());
    }

    @Test
    public final void testAsBuffer() {
        // Test normal case
        VecF4 input1 = new VecF4(0f, 0f, 0f, 0f);
        FloatBuffer expected = FloatBuffer.allocate(4);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, -1f);

        expected = FloatBuffer.allocate(4);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new VecF4(Float.NaN, 0f, 0f, -1f);

        expected = FloatBuffer.allocate(4);
        expected.put(Float.NaN);
        expected.put(0f);
        expected.put(0f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());
    }

    @Test
    public final void testGetSize() {
        // Test normal case
        VecF4 input1 = new VecF4();
        int expected = 4;

        assertEquals(expected, input1.getSize());

        // Test normal case
        input1 = new VecF4(1f, 0f, 0f, 0f);
        expected = 4;

        assertEquals(expected, input1.getSize());
    }
}
