package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class MatF3 extends MatrixF {
    public static final int SIZE = 9;

    /**
     * Creates a new 3x3 identity matrix.
     */
    public MatF3() {
        super(SIZE);
        identity();
    }

    private void identity() {
        Arrays.fill(m, 0f);
        m[0] = m[4] = m[8] = 1.0f;
    }

    /**
     * Creates a new 3x3 matrix with all slots filled with the parameter.
     * 
     * @param in
     *            The value to be put in all matrix fields.
     */
    public MatF3(float in) {
        super(SIZE);
        Arrays.fill(m, in);
    }

    /**
     * Creates a new 3x3 matrix, using the 3 vectors in order as filling.
     * 
     * @param v0
     *            The first row of the matrix.
     * @param v1
     *            The second row of the matrix.
     * @param v2
     *            The third row of the matrix.
     */
    public MatF3(VecF3 v0, VecF3 v1, VecF3 v2) {
        super(SIZE);
        buf.put(v0.asBuffer());
        buf.put(v1.asBuffer());
        buf.put(v2.asBuffer());
    }

    /**
     * Creates a new 3x3 matrix using the 9 parameters row-wise as filling.
     * 
     * @param m00
     *            The parameter on position 0x0.
     * @param m01
     *            The parameter on position 0x1.
     * @param m02
     *            The parameter on position 0x2.
     * @param m10
     *            The parameter on position 1x0.
     * @param m11
     *            The parameter on position 1x1.
     * @param m12
     *            The parameter on position 1x2.
     * @param m20
     *            The parameter on position 2x0.
     * @param m21
     *            The parameter on position 2x1.
     * @param m22
     *            The parameter on position 2x2.
     */
    public MatF3(float m00, float m01, float m02, float m10, float m11,
            float m12, float m20, float m21, float m22) {
        super(SIZE);
        m[0] = m00;
        m[1] = m01;
        m[2] = m02;
        m[3] = m10;
        m[4] = m11;
        m[5] = m12;
        m[6] = m20;
        m[7] = m21;
        m[8] = m22;
    }

    /**
     * Creates a new 3x3 matrix by copying the matrix used as parameter.
     * 
     * @param n
     *            The old matrix to be copied.
     */
    public MatF3(MatF3 n) {
        super(SIZE);

        for (int i = 0; i < SIZE; i++) {
            m[i] = n.get(i);
        }
    }

    public MatF3(FloatBuffer src) {
        super(SIZE);

        for (int i = 0; i < SIZE; i++) {
            m[i] = src.get(i);
        }
    }

    /**
     * Multiplies this matrix with the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be multiplied with the current matrix.
     * @return The new 4x4 matrix that is the result of the multiplication.
     */
    public MatF3 mul(MatF3 n) {
        MatF3 a = new MatF3(0);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    a.m[i * 3 + j] += m[i * 3 + k] * n.m[k * 3 + j];
                }
            }
        }

        return a;
    }

    /**
     * Multiplies this matrix with the given vector, returning a new matrix.
     * 
     * @param v
     *            The vector to be multiplied with the current matrix.
     * @return The new 4x4 matrix that is the result of the multiplication.
     */
    public VecF3 mul(VecF3 v) {
        return new VecF3(m[0 * 3 + 0] * v.v[0] + m[0 * 3 + 1] * v.v[1]
                + m[0 * 3 + 2] * v.v[2], m[1 * 3 + 0] * v.v[0] + m[1 * 3 + 1]
                * v.v[1] + m[1 * 3 + 2] * v.v[2], m[2 * 3 + 0] * v.v[0]
                + m[2 * 3 + 1] * v.v[1] + m[2 * 3 + 2] * v.v[2]);
    }

    /**
     * Multiplies this matrix with the given scalar, returning a new matrix.
     * 
     * @param n
     *            The scalar to be multiplied with the current matrix.
     * @return The new 4x4 matrix that is the result of the multiplication.
     */
    public MatF3 mul(Number n) {
        float fn = n.floatValue();
        for (int i = 0; i < SIZE; ++i) {
            m[i] *= fn;
        }

        return this;
    }

    /**
     * Adds this matrix to the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be added to the current matrix.
     * @return The new 4x4 matrix that is the result of the addition.
     */
    public MatF3 add(MatF3 n) {
        for (int i = 0; i < SIZE; ++i) {
            m[i] += n.m[i];
        }

        return this;
    }

    /**
     * Substracts this matrix with the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be substracted from to the current matrix.
     * @return The new 4x4 matrix that is the result of the substraction.
     */
    public MatF3 sub(MatF3 n) {
        for (int i = 0; i < SIZE; ++i) {
            m[i] -= n.m[i];
        }

        return this;
    }

    /**
     * Divides the elements of this matrix with the given scalar, returning a
     * new matrix.
     * 
     * @param n
     *            The scalar with which to divide the values of the current
     *            matrix.
     * @return The new 4x4 matrix that is the result of the division.
     */
    public MatF3 div(Number n) {
        float fn = 1f / n.floatValue();

        for (int i = 0; i < SIZE; ++i) {
            m[i] *= fn;
        }

        return this;
    }

    @Override
    public MatF3 clone() {
        return new MatF3(this);
    }
}
