package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float2Vector;

import org.junit.Test;

public class VecF2Test {

    @Test
    public final void testVecF2() {
        Float2Vector input = new Float2Vector();
        Float2Vector expected = new Float2Vector(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

    }

    @Test
    public final void testVecF2VecF2() {
        // Test normal case
        Float2Vector input = new Float2Vector(new Float2Vector());
        Float2Vector expected = new Float2Vector(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float2Vector(new Float2Vector(1, 0));
        expected = new Float2Vector(1, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float2Vector(new Float2Vector(Float.NaN, Float.NaN));
        expected = new Float2Vector(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());

    }

    @Test
    public final void testVecF2FloatFloatFloat() {
        // Test normal case
        Float2Vector input = new Float2Vector(0f, 0f);
        Float2Vector expected = new Float2Vector();

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float2Vector(1f, 0);
        expected = new Float2Vector(1, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float2Vector(0, 0);
        expected = new Float2Vector(0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float2Vector(Float.NaN, Float.NaN);
        expected = new Float2Vector(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());

    }

    @Test
    public final void testNeg() {
        // Test normal case
        Float2Vector input = new Float2Vector(0f, 0f).neg();
        Float2Vector expected = new Float2Vector(-0f, -0f);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float2Vector(1f, 0).neg();
        expected = new Float2Vector(-1f, -0f);

        assertEquals(expected, input);
        assertEquals(-1f, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getY(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float2Vector(0, 0).neg();
        expected = new Float2Vector(-0f, -0f);

        assertEquals(expected, input);
        assertEquals(-0f, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getY(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float2Vector(Float.NaN, Float.NaN).neg();
        expected = new Float2Vector(Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());

    }

    @Test
    public final void testAddVecF2() {
        // Test normal case
        Float2Vector input1 = new Float2Vector(0f, 0f);
        Float2Vector input2 = new Float2Vector(0f, 0f);
        Float2Vector expected = new Float2Vector(0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 0f);
        input2 = new Float2Vector(1f, 0f);
        expected = new Float2Vector(2f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 0f);
        input2 = new Float2Vector(-1f, 0f);
        expected = new Float2Vector(0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 345f);
        input2 = new Float2Vector(-1f, 0f);
        expected = new Float2Vector(0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new Float2Vector(Float.NaN, 345f);
        input2 = new Float2Vector(Float.NaN, 0f);
        expected = new Float2Vector(Float.NaN, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testSub() {
        // Test normal case
        Float2Vector input1 = new Float2Vector(0f, 0f);
        Float2Vector input2 = new Float2Vector(0f, 0f);
        Float2Vector expected = new Float2Vector(0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = new Float2Vector(1f, 1f);
        expected = new Float2Vector(0f, 1f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 0f);
        input2 = new Float2Vector(-1f, 0f);
        expected = new Float2Vector(2f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 345f);
        input2 = new Float2Vector(-1f, 0f);
        expected = new Float2Vector(2f, 345f);

        assertEquals(expected, input1.sub(input2));

        // Test weird case
        input1 = new Float2Vector(Float.NaN, 345f);
        input2 = new Float2Vector(Float.NaN, 0f);
        expected = new Float2Vector(Float.NaN, 345f);

        assertEquals(expected, input1.sub(input2));
    }

    @Test
    public final void testMul() {
        // Test normal case
        Float2Vector input1 = new Float2Vector(0f, 0f);
        float input2 = 0f;
        Float2Vector expected = new Float2Vector(0f, 0f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = 1f;
        expected = new Float2Vector(1f, 2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = 10f;
        expected = new Float2Vector(10f, 20f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = -1f;
        expected = new Float2Vector(-1f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float2Vector(Float.NaN, 2f);
        input2 = -1f;
        expected = new Float2Vector(Float.NaN, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = Float.NaN;
        expected = new Float2Vector(Float.NaN, Float.NaN);

        assertEquals(expected, input1.mul(input2));
    }

    @Test
    public final void testDiv() {
        // Test normal case
        Float2Vector input1 = new Float2Vector(0f, 0f);
        float input2 = 0f;
        Float2Vector expected = new Float2Vector(0f, 0f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = 1f;
        expected = new Float2Vector(1f, 2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = 10f;
        expected = new Float2Vector(0.1f, 0.2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = -1f;
        expected = new Float2Vector(-1f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float2Vector(Float.NaN, 2f);
        input2 = -1f;
        expected = new Float2Vector(Float.NaN, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float2Vector(1f, 2f);
        input2 = Float.NaN;
        expected = new Float2Vector(Float.NaN, Float.NaN);

        assertEquals(expected, input1.div(input2));
    }

    @Test
    public final void testAsBuffer() {
        // Test normal case
        Float2Vector input1 = new Float2Vector(0f, 0f);
        FloatBuffer expected = FloatBuffer.allocate(2);
        expected.put(0f);
        expected.put(0f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new Float2Vector(1f, -1f);

        expected = FloatBuffer.allocate(2);
        expected.put(1f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new Float2Vector(Float.NaN, -1f);

        expected = FloatBuffer.allocate(2);
        expected.put(Float.NaN);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());
    }

    @Test
    public final void testGetSize() {
        // Test normal case
        Float2Vector input1 = new Float2Vector();
        int expected = 2;

        assertEquals(expected, input1.getSize());

        // Test normal case
        input1 = new Float2Vector(1f, 0f);
        expected = 2;

        assertEquals(expected, input1.getSize());
    }
}
