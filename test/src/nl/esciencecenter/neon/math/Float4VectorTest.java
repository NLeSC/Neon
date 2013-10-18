package nl.esciencecenter.neon.math;

import static org.junit.Assert.assertEquals;

import java.nio.FloatBuffer;

import nl.esciencecenter.neon.math.FloatMatrixMath;
import nl.esciencecenter.neon.math.Float3Vector;
import nl.esciencecenter.neon.math.Float4Vector;

import org.junit.Test;

public class Float4VectorTest {

    @Test
    public final void testFloat4Vector() {
        Float4Vector input = new Float4Vector();
        Float4Vector expected = new Float4Vector(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testFloat4VectorFloat4Vector() {
        // Test normal case
        Float4Vector input = new Float4Vector(new Float4Vector());
        Float4Vector expected = new Float4Vector(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(new Float4Vector(1, 0, 0, 0));
        expected = new Float4Vector(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float4Vector(new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN));
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testFloat4VectorFloat3VectorFloat() {
        // Test normal case
        Float4Vector input = new Float4Vector(new Float3Vector(), 0f);
        Float4Vector expected = new Float4Vector(0, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(new Float3Vector(1, 0, 0), 0f);
        expected = new Float4Vector(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(new Float3Vector(0, 0, 0), 1f);
        expected = new Float4Vector(0, 0, 0, 1);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(1, input.getW(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float4Vector(new Float3Vector(Float.NaN, Float.NaN, Float.NaN), Float.NaN);
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testFloat4VectorFloatFloatFloatFloat() {
        // Test normal case
        Float4Vector input = new Float4Vector(0f, 0f, 0f, 0f);
        Float4Vector expected = new Float4Vector();

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(1f, 0, 0, 0f);
        expected = new Float4Vector(1, 0, 0, 0);

        assertEquals(expected, input);
        assertEquals(1, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(0, 0, 0, 1f);
        expected = new Float4Vector(0, 0, 0, 1);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(1, input.getW(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testNeg() {
        // Test normal case
        Float4Vector input = new Float4Vector(0f, 0f, 0f, 0f).neg();
        Float4Vector expected = new Float4Vector(-0f, -0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(0, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(0, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(1f, 0, 0, 0f).neg();
        expected = new Float4Vector(-1f, -0f, -0f, -0f);

        assertEquals(expected, input);
        assertEquals(-1f, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getW(), FloatMatrixMath.getEpsilon());

        // Test normal case
        input = new Float4Vector(0, 0, 0, 1f).neg();
        expected = new Float4Vector(-0f, -0f, -0f, -1f);

        assertEquals(expected, input);
        assertEquals(-0f, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(-0f, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(-1f, input.getW(), FloatMatrixMath.getEpsilon());

        // Test weird case
        input = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN).neg();
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input);
        assertEquals(Float.NaN, input.getX(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getY(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getZ(), FloatMatrixMath.getEpsilon());
        assertEquals(Float.NaN, input.getW(), FloatMatrixMath.getEpsilon());
    }

    @Test
    public final void testAddFloat4Vector() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        Float4Vector input2 = new Float4Vector(0f, 0f, 0f, 0f);
        Float4Vector expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        input2 = new Float4Vector(1f, 0f, 0f, 0f);
        expected = new Float4Vector(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        input2 = new Float4Vector(-1f, 0f, 0f, 0f);
        expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 345f);
        input2 = new Float4Vector(-1f, 0f, 0f, 0f);
        expected = new Float4Vector(0f, 0f, 0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new Float4Vector(Float.NaN, 0f, 0f, 0f);
        expected = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testAddFloat3Vector() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        Float3Vector input2 = new Float3Vector(0f, 0f, 0f);
        Float4Vector expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        input2 = new Float3Vector(1f, 0f, 0f);
        expected = new Float4Vector(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        input2 = new Float3Vector(-1f, 0f, 0f);
        expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.add(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 345f);
        input2 = new Float3Vector(-1f, 0f, 0f);
        expected = new Float4Vector(0f, 0f, 0f, 345f);

        assertEquals(expected, input1.add(input2));

        // Test weird case
        input1 = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new Float3Vector(Float.NaN, 0f, 0f);
        expected = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.add(input2));
    }

    @Test
    public final void testSub() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        Float4Vector input2 = new Float4Vector(0f, 0f, 0f, 0f);
        Float4Vector expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = new Float4Vector(1f, 0f, 0f, 1f);
        expected = new Float4Vector(0f, 0f, 0f, 1f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        input2 = new Float4Vector(-1f, 0f, 0f, 0f);
        expected = new Float4Vector(2f, 0f, 0f, 0f);

        assertEquals(expected, input1.sub(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 345f);
        input2 = new Float4Vector(-1f, 0f, 0f, 0f);
        expected = new Float4Vector(2f, 0f, 0f, 345f);

        assertEquals(expected, input1.sub(input2));

        // Test weird case
        input1 = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);
        input2 = new Float4Vector(Float.NaN, 0f, 0f, 0f);
        expected = new Float4Vector(Float.NaN, Float.NaN, 0f, 345f);

        assertEquals(expected, input1.sub(input2));
    }

    @Test
    public final void testMul() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        float input2 = 0f;
        Float4Vector expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = 1f;
        expected = new Float4Vector(1f, 0f, 0f, 2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = 10f;
        expected = new Float4Vector(10f, 0f, 0f, 20f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = -1f;
        expected = new Float4Vector(-1f, -0f, -0f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float4Vector(Float.NaN, Float.NaN, 0f, 2f);
        input2 = -1f;
        expected = new Float4Vector(Float.NaN, Float.NaN, -0f, -2f);

        assertEquals(expected, input1.mul(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = Float.NaN;
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.mul(input2));
    }

    @Test
    public final void testDiv() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        float input2 = 0f;
        Float4Vector expected = new Float4Vector(0f, 0f, 0f, 0f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = 1f;
        expected = new Float4Vector(1f, 0f, 0f, 2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = 10f;
        expected = new Float4Vector(0.1f, 0f, 0f, 0.2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = -1f;
        expected = new Float4Vector(-1f, -0f, -0f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float4Vector(Float.NaN, Float.NaN, 0f, 2f);
        input2 = -1f;
        expected = new Float4Vector(Float.NaN, Float.NaN, -0f, -2f);

        assertEquals(expected, input1.div(input2));

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 2f);
        input2 = Float.NaN;
        expected = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.div(input2));
    }

    @Test
    public final void testStripAlpha() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        Float3Vector expected = new Float3Vector(0f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test normal case
        input1 = new Float4Vector(0f, 0f, 0f, 1f);
        expected = new Float3Vector(0f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 1f);
        expected = new Float3Vector(1f, 0f, 0f);

        assertEquals(expected, input1.stripAlpha());

        // Test weird case
        input1 = new Float4Vector(Float.NaN, Float.NaN, Float.NaN, Float.NaN);
        expected = new Float3Vector(Float.NaN, Float.NaN, Float.NaN);

        assertEquals(expected, input1.stripAlpha());
    }

    @Test
    public final void testAsBuffer() {
        // Test normal case
        Float4Vector input1 = new Float4Vector(0f, 0f, 0f, 0f);
        FloatBuffer expected = FloatBuffer.allocate(4);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.put(0f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, -1f);

        expected = FloatBuffer.allocate(4);
        expected.put(1f);
        expected.put(0f);
        expected.put(0f);
        expected.put(-1f);
        expected.rewind();

        assertEquals(expected, input1.asBuffer());

        // Test normal case
        input1 = new Float4Vector(Float.NaN, 0f, 0f, -1f);

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
        Float4Vector input1 = new Float4Vector();
        int expected = 4;

        assertEquals(expected, input1.getSize());

        // Test normal case
        input1 = new Float4Vector(1f, 0f, 0f, 0f);
        expected = 4;

        assertEquals(expected, input1.getSize());
    }
}
