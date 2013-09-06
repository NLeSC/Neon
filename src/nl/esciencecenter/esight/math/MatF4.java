package nl.esciencecenter.esight.math;

import java.util.Arrays;

/* Copyright 2013 Netherlands eScience Center
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
 * 4x4 float matrix implementation
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class MatF4 extends MatrixF {
    /** The number of elements in this matrix */
    private static final int SIZE = 16;

    /**
     * Creates a new identity matrix.
     */
    public MatF4() {
        super(SIZE);
        identity();
    }

    private void identity() {
        Arrays.fill(asArray(), 0f);
        asArray()[0] = 1.0f;
        asArray()[5] = 1.0f;
        asArray()[10] = 1.0f;
        asArray()[15] = 1.0f;
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
     * @param v3
     *            The fourth row of the matrix.
     */
    public MatF4(VecF4 v0, VecF4 v1, VecF4 v2, VecF4 v3) {
        super(SIZE);
        asArray()[0] = v0.getX();
        asArray()[1] = v0.getY();
        asArray()[2] = v0.getZ();
        asArray()[3] = v0.getW();

        asArray()[4] = v1.getX();
        asArray()[5] = v1.getY();
        asArray()[6] = v1.getZ();
        asArray()[7] = v1.getW();

        asArray()[8] = v2.getX();
        asArray()[9] = v2.getY();
        asArray()[10] = v2.getZ();
        asArray()[11] = v2.getW();

        asArray()[12] = v3.getX();
        asArray()[13] = v3.getY();
        asArray()[14] = v3.getZ();
        asArray()[15] = v3.getW();
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
    public MatF4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
            float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
        super(SIZE);
        asArray()[0] = m00;
        asArray()[1] = m01;
        asArray()[2] = m02;
        asArray()[3] = m03;
        asArray()[4] = m10;
        asArray()[5] = m11;
        asArray()[6] = m12;
        asArray()[7] = m13;
        asArray()[8] = m20;
        asArray()[9] = m21;
        asArray()[10] = m22;
        asArray()[11] = m23;
        asArray()[12] = m30;
        asArray()[13] = m31;
        asArray()[14] = m32;
        asArray()[15] = m33;
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
            asArray()[i] = n.get(i);
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
                    a.asArray()[i * 4 + j] += asArray()[i * 4 + k] * n.asArray()[k * 4 + j];
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
    public MatF4 sub(MatF4 n) {
        MatF4 result = new MatF4(0f);

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
    public MatF4 mul(Number n) {
        MatF4 result = new MatF4(0f);

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
    public MatF4 add(Number n) {
        MatF4 result = new MatF4(0f);

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
    public MatF4 sub(Number n) {
        MatF4 result = new MatF4(0f);

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
    public MatF4 div(Number n) {
        MatF4 result = new MatF4(0f);

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
    public VecF4 mul(VecF4 v) {
        return new VecF4(asArray()[0] * v.getX() + asArray()[1] * v.getY() + asArray()[2] * v.getZ() + asArray()[3]
                * v.getW(), asArray()[4] * v.getX() + asArray()[5] * v.getY() + asArray()[6] * v.getZ() + asArray()[7]
                * v.getW(), asArray()[8] * v.getX() + asArray()[9] * v.getY() + asArray()[10] * v.getZ()
                + asArray()[11] * v.getW(), asArray()[12] * v.getX() + asArray()[13] * v.getY() + asArray()[14]
                * v.getZ() + asArray()[15] * v.getW());
    }
}
