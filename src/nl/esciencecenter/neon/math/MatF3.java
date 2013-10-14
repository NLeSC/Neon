package nl.esciencecenter.neon.math;

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
    private static final int SIZE = 9;

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
        Arrays.fill(asArray(), 0f);
        asArray()[0] = 1.0f;
        asArray()[4] = 1.0f;
        asArray()[8] = 1.0f;
    }

    /**
     * Creates a new matrix with all slots filled with the parameter.
     * 
     * @param in
     *            The value to be put in all matrix fields.
     */
    public MatF3(float in) {
        super(SIZE);
        Arrays.fill(asArray(), in);
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
        asArray()[0] = v0.getX();
        asArray()[1] = v0.getY();
        asArray()[2] = v0.getZ();

        asArray()[3] = v1.getX();
        asArray()[4] = v1.getY();
        asArray()[5] = v1.getZ();

        asArray()[6] = v2.getX();
        asArray()[7] = v2.getY();
        asArray()[8] = v2.getZ();
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
    public MatF3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        super(SIZE);
        asArray()[0] = m00;
        asArray()[1] = m01;
        asArray()[2] = m02;
        asArray()[3] = m10;
        asArray()[4] = m11;
        asArray()[5] = m12;
        asArray()[6] = m20;
        asArray()[7] = m21;
        asArray()[8] = m22;
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
            asArray()[i] = n.get(i);
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
                    result.asArray()[i * 3 + j] += asArray()[i * 3 + k] * n.asArray()[k * 3 + j];
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
            result.asArray()[i] = asArray()[i] + n.asArray()[i];
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
            result.asArray()[i] = asArray()[i] - n.asArray()[i];
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
            result.asArray()[i] = asArray()[i] * fn;
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
            result.asArray()[i] = asArray()[i] + fn;
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
            result.asArray()[i] = asArray()[i] - fn;
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
            result.asArray()[i] = asArray()[i] * fn;
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
        VecF3 result = new VecF3(asArray()[0] * v.getX() + asArray()[1] * v.getY() + asArray()[2] * v.getZ(),
                asArray()[3] * v.getX() + asArray()[4] * v.getY() + asArray()[5] * v.getZ(), asArray()[6] * v.getX()
                        + asArray()[7] * v.getY() + asArray()[8] * v.getZ());
        return result;
    }
}
