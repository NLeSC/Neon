package nl.esciencecenter.esight.math;

import java.util.Arrays;

/* Copyright [2013] [Netherlands eScience Center]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 3x3 float matrix implementation
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class MatF3 extends MatrixF {
    /** The number of elements in this matrix */
    public static final int SIZE = 9;

    /**
     * Creates a new 3x3 identity matrix.
     */
    public MatF3() {
        super(SIZE);
        identity();
    }

    /**
     * Helper method to create a new identity matrix.
     */
    private void identity() {
        Arrays.fill(m, 0f);
        m[0] = m[4] = m[8] = 1.0f;
    }

    /**
     * Creates a new matrix with all slots filled with the parameter.
     * 
     * @param in
     *            The value to be put in all matrix fields.
     */
    public MatF3(float in) {
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
     */
    public MatF3(VecF3 v0, VecF3 v1, VecF3 v2) {
        super(SIZE);
        buf.put(v0.asBuffer());
        buf.put(v1.asBuffer());
        buf.put(v2.asBuffer());
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
     * Creates a new matrix by copying the matrix used as parameter.
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

    /**
     * Multiplies this matrix with the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be multiplied with the current matrix.
     * @return The new matrix that is the result of the multiplication.
     */
    public MatF3 mul(MatF3 n) {
        MatF3 result = new MatF3(0f);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    result.m[i * 3 + j] += m[i * 3 + k] * n.m[k * 3 + j];
                }
            }
        }

        return result;
    }

    /**
     * Adds this matrix to the given matrix, returning a new matrix.
     * 
     * @param n
     *            The matrix to be added to the current matrix.
     * @return The new matrix that is the result of the addition.
     */
    public MatF3 add(MatF3 n) {
        MatF3 result = new MatF3(0f);

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
    public MatF3 sub(MatF3 n) {
        MatF3 result = new MatF3(0f);

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
    public MatF3 mul(Number n) {
        MatF3 result = new MatF3(0f);

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
    public MatF3 add(Number n) {
        MatF3 result = new MatF3(0f);

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
    public MatF3 sub(Number n) {
        MatF3 result = new MatF3(0f);

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
    public MatF3 div(Number n) {
        MatF3 result = new MatF3(0f);

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
    public VecF3 mul(VecF3 v) {
        VecF3 result = new VecF3(m[0] * v.v[0] + m[1] * v.v[1] + m[2] * v.v[2],
                m[3] * v.v[0] + m[4] * v.v[1] + m[5] * v.v[2], m[6] * v.v[0]
                        + m[7] * v.v[1] + m[8] * v.v[2]);
        return result;
    }

    @Override
    public MatF3 clone() {
        return new MatF3(this);
    }
}
