package nl.esciencecenter.esight.math;

import java.util.Arrays;

public class MatF4 extends MatrixF {
    /** The number of elements in this matrix */
    public static final int SIZE = 16;

    /**
     * Creates a new identity matrix.
     */
    public MatF4() {
        super(SIZE);
        identity();
    }

    private void identity() {
        Arrays.fill(m, 0f);
        m[0] = m[5] = m[10] = m[15] = 1.0f;
    }

    /**
     * Helper method to create an identity matrix.
     */

    /**
     * Creates a new matrix with all slots filled with the parameter.
     * 
     * @param in
     *            The value to be put in all matrix fields.
     */
    public MatF4(float in) {
        super(SIZE);
        Arrays.fill(m, in);
    }

    /**
     * Creates a new matrix, using the vectors in order as filling.
     * 
     * @param v0
     *            The first row of the matrix.
     * @param v1
     *            The second row of the matrix.
     * @param v2
     *            The third row of the matrix.
     * @param v3
     *            The fourth row of the matrix.
     */
    public MatF4(VecF4 v0, VecF4 v1, VecF4 v2, VecF4 v3) {
        super(SIZE);
        buf.put(v0.asBuffer());
        buf.put(v1.asBuffer());
        buf.put(v2.asBuffer());
        buf.put(v3.asBuffer());
    }

    /**
     * Creates a new matrix using the parameters row-wise as filling.
     * 
     * @param m00
     *            The parameter on position 0x0.
     * @param m01
     *            The parameter on position 0x1.
     * @param m02
     *            The parameter on position 0x2.
     * @param m03
     *            The parameter on position 0x3.
     * @param m10
     *            The parameter on position 1x0.
     * @param m11
     *            The parameter on position 1x1.
     * @param m12
     *            The parameter on position 1x2.
     * @param m13
     *            The parameter on position 1x3.
     * @param m20
     *            The parameter on position 2x0.
     * @param m21
     *            The parameter on position 2x1.
     * @param m22
     *            The parameter on position 2x2.
     * @param m23
     *            The parameter on position 2x3.
     * @param m30
     *            The parameter on position 3x0.
     * @param m31
     *            The parameter on position 3x1.
     * @param m32
     *            The parameter on position 3x2.
     * @param m33
     *            The parameter on position 3x3.
     */
    public MatF4(float m00, float m01, float m02, float m03, float m10,
            float m11, float m12, float m13, float m20, float m21, float m22,
            float m23, float m30, float m31, float m32, float m33) {
        super(SIZE);
        m[0] = m00;
        m[1] = m01;
        m[2] = m02;
        m[3] = m03;
        m[4] = m10;
        m[5] = m11;
        m[6] = m12;
        m[7] = m13;
        m[8] = m20;
        m[9] = m21;
        m[10] = m22;
        m[11] = m23;
        m[12] = m30;
        m[13] = m31;
        m[14] = m32;
        m[15] = m33;
    }

    /**
     * Creates a new matrix by copying the matrix used as parameter.
     * 
     * @param n
     *            The old matrix to be copied.
     */
    public MatF4(MatF4 n) {
        super(SIZE);

        for (int i = 0; i < SIZE; i++) {
            m[i] = n.get(i);
        }
    }

    /**
     * Multiplies this matrix with the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be multiplied with the current matrix.
     * @return The new 4x4 matrix that is the result of the multiplication.
     */
    public MatF4 mul(MatF4 n) {
        MatF4 a = new MatF4(0f);

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                for (int k = 0; k < 4; ++k) {
                    a.m[i * 4 + j] += m[i * 4 + k] * n.m[k * 4 + j];
                }
            }
        }

        return a;
    }

    /**
     * Adds this matrix to the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be added to the current matrix.
     * @return The new matrix that is the result of the addition.
     */
    public MatF4 add(MatF4 n) {
        MatF4 result = new MatF4(0f);

        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] + n.m[i];
        }

        return result;
    }

    /**
     * Substracts this matrix with the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be substracted from to the current matrix.
     * @return The new matrix that is the result of the substraction.
     */
    public MatF4 sub(MatF4 n) {
        MatF4 result = new MatF4(0f);

        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] - n.m[i];
        }

        return result;
    }

    /**
     * Multiplies this matrix with the given scalar, returning a new matrix.
     * 
     * @param n
     *            The scalar to be multiplied with the current matrix.
     * @return The new matrix that is the result of the multiplication.
     */
    public MatF4 mul(Number n) {
        MatF4 result = new MatF4(0f);

        float fn = n.floatValue();
        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] * fn;
        }

        return result;
    }

    /**
     * Multiplies this matrix with the given scalar, returning a new matrix.
     * 
     * @param n
     *            The scalar to be multiplied with the current matrix.
     * @return The new matrix that is the result of the multiplication.
     */
    public MatF4 add(Number n) {
        MatF4 result = new MatF4(0f);

        float fn = n.floatValue();
        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] + fn;
        }

        return result;
    }

    /**
     * Multiplies this matrix with the given scalar, returning a new matrix.
     * 
     * @param n
     *            The scalar to be multiplied with the current matrix.
     * @return The new matrix that is the result of the multiplication.
     */
    public MatF4 sub(Number n) {
        MatF4 result = new MatF4(0f);

        float fn = n.floatValue();
        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] - fn;
        }

        return result;
    }

    /**
     * Divides the elements of this matrix with the given scalar, returning a
     * new matrix.
     * 
     * @param n
     *            The scalar with which to divide the values of the current
     *            matrix.
     * @return The new matrix that is the result of the division.
     */
    public MatF4 div(Number n) {
        MatF4 result = new MatF4(0f);

        float fn = 1f / n.floatValue();

        for (int i = 0; i < SIZE; ++i) {
            result.m[i] = m[i] * fn;
        }

        return result;
    }

    /**
     * Multiplies this matrix with the given vector, returning a new vector.
     * 
     * @param v
     *            The vector to be multiplied with the current matrix.
     * @return The new vector that is the result of the multiplication.
     */
    public VecF4 mul(VecF4 v) {
        return new VecF4(m[0] * v.v[0] + m[1] * v.v[1] + m[2] * v.v[2] + m[3]
                * v.v[3], m[4] * v.v[0] + m[5] * v.v[1] + m[6] * v.v[2] + m[7]
                * v.v[3], m[8] * v.v[0] + m[9] * v.v[1] + m[10] * v.v[2]
                + m[11] * v.v[3], m[12] * v.v[0] + m[13] * v.v[1] + m[14]
                * v.v[2] + m[15] * v.v[3]);
    }

    @Override
    public MatF4 clone() {
        return new MatF4(this);
    }
}
