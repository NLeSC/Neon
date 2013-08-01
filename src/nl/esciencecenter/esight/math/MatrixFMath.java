package nl.esciencecenter.esight.math;

import nl.esciencecenter.esight.exceptions.InverseNotAvailableException;

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
 * Utility class for the more abstract Computer Graphics - related Matrix
 * calculations.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class MatrixFMath {
    public static double degreesToRadians = Math.PI / 180.0;
    public static float EPSILON = 0.0000001f;

    /**
     * Get the normal matrix from the modelview matrix.
     * 
     * @param mv
     *            The Modelview matrix to extract the Normal Matrix from.
     * @return The Normal Matrix for this Modelview Matrix. If the inverse
     *         cannot be calculated this will return an identity matrix instead.
     */
    public static MatF3 getNormalMatrix(MatF4 mv) {
        MatF3 upper3x3 = new MatF3(mv.get(0), mv.get(1), mv.get(2), mv.get(4), mv.get(5), mv.get(6), mv.get(8),
                mv.get(9), mv.get(10));

        MatF3 inverse;
        try {
            inverse = inverse(upper3x3);
            MatF3 transpose = transpose(inverse);

            return transpose;
        } catch (InverseNotAvailableException e) {
            return new MatF3();
        }
    }

    /**
     * Helper method that creates a Orthogonal matrix
     * 
     * @param left
     *            The left clipping plane
     * @param right
     *            The right clipping plane
     * @param bottom
     *            The bottom clipping plane
     * @param top
     *            The top clipping plane
     * @param zNear
     *            The near clipping plane
     * @param zFar
     *            The far clipping plane
     * @return An orthogonal matrix
     */
    public static MatF4 ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
        float dX = right - left;
        float dY = top - bottom;
        float dZ = zFar - zNear;

        if (dX <= 0f) {
            throw new IllegalArgumentException("left cannot be greater than or equal to right");
        } else if (dY <= 0f) {
            throw new IllegalArgumentException("bottom cannot be greater than or equal to top");
        } else if (dZ <= 0f) {
            throw new IllegalArgumentException("zNear cannot be greater than or equal to zFar");
        }

        MatF4 result = new MatF4(2f / dX, 0f, 0f, -(left + right) / dX, 0f, 2f / dY, 0f, -(top + bottom) / dY, 0f, 0f,
                -2f / (zFar - zNear), -(zFar + zNear) / dZ, 0f, 0f, 0f, 1f);
        return result;
    }

    /**
     * Helper method to define an orthogonal matrix for 2d projections
     * 
     * @param left
     *            The left clipping plane
     * @param right
     *            The right clipping plane
     * @param bottom
     *            The bottom clipping plane
     * @param top
     *            The top clipping plane
     * @return An orthogonal matrix
     */
    public static MatF4 ortho2D(float left, float right, float bottom, float top) {
        return ortho(left, right, bottom, top, -1f, 1f);
    }

    /**
     * Helper method that creates a Frustum matrix
     * 
     * @param left
     *            The left clipping plane
     * @param right
     *            The right clipping plane
     * @param bottom
     *            The bottom clipping plane
     * @param top
     *            The top clipping plane
     * @param zNear
     *            The near clipping plane
     * @param zFar
     *            The far clipping plane
     * @return An frustum matrix
     */
    public static MatF4 frustum(float left, float right, float bottom, float top, float zNear, float zFar) {
        float dX = right - left;
        float dY = top - bottom;
        float dZ = zFar - zNear;

        if (dX <= 0f) {
            throw new IllegalArgumentException("left cannot be greater than or equal to right");
        } else if (dY <= 0f) {
            throw new IllegalArgumentException("bottom cannot be greater than or equal to top");
        } else if (dZ <= 0f) {
            throw new IllegalArgumentException("zNear cannot be greater than or equal to zFar");
        }

        MatF4 result = new MatF4(2f * zNear / dX, 0f, (right + left) / dX, 0f, 0f, 2f * zNear / dY,
                (top + bottom) / dY, 0f, 0f, 0f, -(zFar + zNear) / dZ, -2f * zFar * zNear / dZ, 0f, 0f, -1f, 0f);
        return result;
    }

    /**
     * Helper method that creates a perspective matrix
     * 
     * @param fovy
     *            The fov in y-direction, in degrees
     * 
     * @param aspect
     *            The aspect ratio
     * @param zNear
     *            The near clipping plane
     * @param zFar
     *            The far clipping plane
     * @return A perspective matrix
     */
    public static MatF4 perspective(float fovy, float aspect, float zNear, float zFar) {
        if (fovy <= 0f || fovy > 180f) {
            throw new IllegalArgumentException("fovy cannot be smaller than or equal to 0 or greater than 180f");
        } else if (aspect <= 0f) {
            throw new IllegalArgumentException("aspect cannot be smaller than or equal to 0");
        }

        float top = (float) (Math.tan(fovy * degreesToRadians / 2) * zNear);
        float right = top * aspect;
        float dZ = zFar - zNear;

        if (dZ <= 0f) {
            throw new IllegalArgumentException("zNear cannot be greater than or equal to zFar");
        }

        MatF4 result = new MatF4((zNear / right), 0, 0, 0, 0, (zNear / top), 0, 0, 0, 0, -(zFar + zNear) / dZ, -2
                * zFar * zNear / dZ, 0, 0, -1, 0);

        return result;
    }

    /**
     * Helper method that supplies a viewing transformation that allows us to
     * look at the indicated point,
     * 
     * @see http://www.opengl.org/sdk/docs/man2/xhtml/gluLookAt.xml
     * 
     * @param eye
     *            The coordinates of the eye (camera)
     * @param at
     *            The coordinates of the object we want to look at
     * @param up
     *            The vector indicating the up direction for the camera
     * @return A viewing transformation suitable for multiplication with the
     *         perspective matrix
     */
    public static MatF4 lookAt(VecF4 eye, VecF4 at, VecF4 up) {
        VecF4 norm = VectorFMath.normalize(eye.sub(at));
        VecF4 crossUpNorm = VectorFMath.normalize(VectorFMath.cross(VectorFMath.normalize(up), norm));
        VecF4 crossNormUpNorm = VectorFMath.normalize(VectorFMath.cross(norm, crossUpNorm));
        VecF4 pointIndicator = new VecF4(0f, 0f, 0f, 1f);

        MatF4 matrix = new MatF4(crossUpNorm, crossNormUpNorm, norm, pointIndicator);

        return matrix.mul(translate(eye.neg()));
    }

    /**
     * Helper method that creates a translation matrix
     * 
     * @param x
     *            The x translation
     * @param y
     *            The y translation
     * @param z
     *            The z translation
     * @return A translation matrix
     */
    public static MatF4 translate(float x, float y, float z) {
        MatF4 result = new MatF4(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
        return result;
    }

    /**
     * Helper method that creates a translation matrix
     * 
     * @param vec
     *            The vector with which we want to translate
     * @return A translation matrix
     */
    public static MatF4 translate(VecF3 vec) {
        return translate(vec.v[0], vec.v[1], vec.v[2]);
    }

    /**
     * Helper method that creates a translation matrix
     * 
     * @param vec
     *            The vector with which we want to translate
     * @return A translation matrix
     */
    public static MatF4 translate(VecF4 vec) {
        return translate(vec.v[0], vec.v[1], vec.v[2]);
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param x
     *            The x scale
     * @param y
     *            The y scale
     * @param z
     *            The z scale
     * @return A scaling matrix
     */
    public static MatF4 scale(float x, float y, float z) {
        MatF4 result = new MatF4(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);
        return result;
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param newScale
     *            The new uniform scale.
     * @return A scaling matrix
     */
    public static MatF4 scale(float newScale) {
        return scale(newScale, newScale, newScale);
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param vec
     *            The vector with which we want to scale
     * @return A scaling matrix
     */
    public static MatF4 scale(VecF3 vec) {
        return scale(vec.v[0], vec.v[1], vec.v[2]);
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param vec
     *            The vector with which we want to scale
     * @return A scaling matrix
     */
    public static MatF4 scale(VecF4 vec) {
        return scale(vec.v[0], vec.v[1], vec.v[2]);
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * x-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static MatF4 rotationX(float angleDeg) {
        double angleRad = degreesToRadians * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        MatF4 result = new MatF4(1, 0, 0, 0, 0, cosa, -sina, 0, 0, sina, cosa, 0, 0, 0, 0, 1);
        return result;
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * y-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static MatF4 rotationY(float angleDeg) {
        double angleRad = degreesToRadians * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        MatF4 m = new MatF4(cosa, 0, sina, 0, 0, 1, 0, 0, -sina, 0, cosa, 0, 0, 0, 0, 1);

        return m;
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * z-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static MatF4 rotationZ(float angleDeg) {
        double angleRad = degreesToRadians * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        MatF4 m = new MatF4(cosa, -sina, 0, 0, sina, cosa, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

        return m;
    }

    /**
     * Helper method that creates a matrix describing a rotation around an
     * arbitrary axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @param x
     *            The x component of the vector that describes the axis to
     *            rotate around
     * @param y
     *            The y component of the vector that describes the axis to
     *            rotate around
     * @param z
     *            The z component of the vector that describes the axis to
     *            rotate around
     * @return The rotation matrix
     */
    public static MatF4 rotate(float angleDeg, float x, float y, float z) {
        double angleRad = degreesToRadians * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);
        float invc = 1 - cosa;

        VecF3 n = VectorFMath.normalize(new VecF3(x, y, z));
        x = n.v[0];
        y = n.v[1];
        z = n.v[2];

        MatF4 R = new MatF4(invc * x * x + cosa, invc * x * y - sina * z, invc * x * z + sina * y, 0f, invc * x * y
                + sina * z, invc * y * y + cosa, invc * y * z - sina * x, 0f, invc * x * z - sina * y, invc * y * z
                + sina * x, invc * z * z + cosa, 0f, 0f, 0f, 0f, 1f

        );

        return R;
    }

    /**
     * Helper method that creates a matrix describing a rotation around an
     * arbitrary axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @param axis
     *            The axis to rotate around
     * @return The rotation matrix
     */
    public static MatF4 rotate(float angleDeg, VecF3 axis) {
        MatF4 result = rotate(angleDeg, axis.get(0), axis.get(1), axis.get(2));

        return result;
    }

    /**
     * Helper method that creates a matrix describing a rotation around an
     * arbitrary axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @param axis
     *            The axis to rotate around
     * @return The rotation matrix
     */
    public static MatF4 rotate(float angleDeg, VecF4 axis) {
        MatF4 result = rotate(angleDeg, axis.get(0), axis.get(1), axis.get(2));

        return result;
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(MatF2 m) {
        return m.m[0] * m.m[3] - m.m[2] * m.m[1];
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(MatF3 m) {
        float minor00 = determinant(exclude(m, 0, 0));
        float minor01 = determinant(exclude(m, 0, 1));
        float minor02 = determinant(exclude(m, 0, 2));

        return m.m[0] * minor00 - m.m[3] * minor01 + m.m[6] * minor02;
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(MatF4 m) {
        float minor00 = determinant(exclude(m, 0, 0));
        float minor01 = determinant(exclude(m, 0, 1));
        float minor02 = determinant(exclude(m, 0, 2));
        float minor03 = determinant(exclude(m, 0, 3));

        return m.m[0] * minor00 - m.m[4] * minor01 + m.m[8] * minor02 + m.m[12] * minor03;
    }

    /**
     * Find the cofactors for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the cofactors
     */
    public static MatF3 cofactors(MatF3 m) {
        MatF3 checkerboard = new MatF3(1f, -1f, 1f, -1f, 1f, -1f, 1f, -1f, 1f);
        MatF3 minors = minors(m);
        MatF3 result = new MatF3();

        for (int i = 0; i < result.size; i++) {
            result.m[i] = minors.m[i] * checkerboard.m[i];
        }

        return result;
    }

    /**
     * Find the cofactors for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the cofactors
     */
    public static MatF4 cofactors(MatF4 m) {
        MatF4 checkerboard = new MatF4(1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f);
        MatF4 minors = minors(m);
        MatF4 result = new MatF4();

        for (int i = 0; i < result.size; i++) {
            result.m[i] = minors.m[i] * checkerboard.m[i];
        }

        return result;
    }

    /**
     * Find the minors for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the minors
     */
    public static MatF3 minors(MatF3 m) {
        MatF3 result = new MatF3();

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iCol = 0; iCol < 3; iCol++) {
                MatF2 excluded = exclude(m, iCol, iRow);
                float det = determinant(excluded);
                result.buf.put(det);
            }
        }

        return result;
    }

    /**
     * Find the minors for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the minors
     */
    public static MatF4 minors(MatF4 m) {
        MatF4 result = new MatF4();

        for (int iRow = 0; iRow < 4; iRow++) {
            for (int iCol = 0; iCol < 4; iCol++) {
                MatF3 excluded = exclude(m, iCol, iRow);
                float det = determinant(excluded);
                result.buf.put(det);
            }
        }

        return result;
    }

    /**
     * Get the matrix which is the input matrix, but with the column and row
     * given excluded
     * 
     * @param m
     *            the input matrix
     * @return the exclusion matrix
     */
    public static MatF2 exclude(MatF3 m, int col, int row) {
        MatF2 result = new MatF2();

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iCol = 0; iCol < 3; iCol++) {
                if (iRow != row && iCol != col) {
                    result.buf.put(m.m[iRow * 3 + iCol]);
                }
            }
        }

        return result;
    }

    /**
     * Get the matrix which is the input matrix, but with the column and row
     * given excluded
     * 
     * @param m
     *            the input matrix
     * @return the exclusion matrix
     */
    public static MatF3 exclude(MatF4 m, int col, int row) {
        MatF3 result = new MatF3();

        for (int iRow = 0; iRow < 4; iRow++) {
            for (int iCol = 0; iCol < 4; iCol++) {
                if (iRow != row && iCol != col) {
                    result.buf.put(m.m[iRow * 4 + iCol]);
                }
            }
        }

        return result;
    }

    /**
     * Get the transposed matrix
     * 
     * @param m
     *            the input matrix
     * @return the transposed matrix
     */
    public static MatF2 transpose(MatF2 m) {
        MatF2 result = new MatF2();

        result.m[0] = m.m[0];
        result.m[1] = m.m[2];

        result.m[2] = m.m[1];
        result.m[3] = m.m[3];

        return result;
    }

    /**
     * Get the transposed matrix
     * 
     * @param m
     *            the input matrix
     * @return the transposed matrix
     */
    public static MatF3 transpose(MatF3 m) {
        MatF3 result = new MatF3();

        result.m[0] = m.m[0];
        result.m[1] = m.m[3];
        result.m[2] = m.m[6];

        result.m[3] = m.m[1];
        result.m[4] = m.m[4];
        result.m[5] = m.m[7];

        result.m[6] = m.m[2];
        result.m[7] = m.m[5];
        result.m[8] = m.m[8];

        return result;
    }

    /**
     * Get the transposed matrix
     * 
     * @param m
     *            the input matrix
     * @return the transposed matrix
     */
    public static MatF4 transpose(MatF4 m) {
        MatF4 result = new MatF4();

        result.m[0] = m.m[0];
        result.m[1] = m.m[4];
        result.m[2] = m.m[8];
        result.m[3] = m.m[12];

        result.m[4] = m.m[1];
        result.m[5] = m.m[5];
        result.m[6] = m.m[9];
        result.m[7] = m.m[13];

        result.m[8] = m.m[2];
        result.m[9] = m.m[6];
        result.m[10] = m.m[10];
        result.m[11] = m.m[14];

        result.m[12] = m.m[3];
        result.m[13] = m.m[7];
        result.m[14] = m.m[11];
        result.m[15] = m.m[15];

        return result;
    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static MatF2 adjoint(MatF2 m) {
        MatF2 result = new MatF2();

        result.m[0] = m.m[3];
        result.m[1] = -m.m[1];
        result.m[2] = -m.m[2];
        result.m[3] = m.m[0];

        return result;

    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static MatF3 adjoint(MatF3 m) {
        return transpose(cofactors(m));

    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static MatF4 adjoint(MatF4 m) {
        return transpose(cofactors(m));
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static MatF2 inverse(MatF2 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        MatF2 adj = adjoint(m);

        MatF2 inverse = adj.mul(1f / det);

        return inverse;
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static MatF3 inverse(MatF3 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        MatF3 adj = adjoint(m);

        MatF3 inverse = adj.mul(1f / det);

        return inverse;
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static MatF4 inverse(MatF4 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        MatF4 adj = adjoint(m);

        MatF4 inverse = adj.mul(1f / det);

        return inverse;
    }
}
