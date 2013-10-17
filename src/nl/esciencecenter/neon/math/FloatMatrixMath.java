package nl.esciencecenter.neon.math;

import nl.esciencecenter.neon.exceptions.InverseNotAvailableException;

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
public final class FloatMatrixMath {
    private static final double DEGREESTORADIANS = Math.PI / 180.0;
    private static final float EPSILON = 0.0000001f;

    private FloatMatrixMath() {
        // Only static access.
    }

    /**
     * Get the normal matrix from the modelview matrix.
     * 
     * @param mv
     *            The Modelview matrix to extract the Normal Matrix from.
     * @return The Normal Matrix for this Modelview Matrix. If the inverse
     *         cannot be calculated this will return an identity matrix instead.
     */
    public static Float3Matrix getNormalMatrix(Float4Matrix mv) {
        Float3Matrix upper3x3 = new Float3Matrix(mv.get(0), mv.get(1), mv.get(2), mv.get(4), mv.get(5), mv.get(6),
                mv.get(8), mv.get(9), mv.get(10));

        Float3Matrix inverse;
        try {
            inverse = inverse(upper3x3);
            Float3Matrix transpose = transpose(inverse);

            return transpose;
        } catch (InverseNotAvailableException e) {
            return new Float3Matrix();
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
    public static Float4Matrix ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
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

        Float4Matrix result = new Float4Matrix(2f / dX, 0f, 0f, -(left + right) / dX, 0f, 2f / dY, 0f, -(top + bottom)
                / dY, 0f, 0f, -2f / (zFar - zNear), -(zFar + zNear) / dZ, 0f, 0f, 0f, 1f);
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
    public static Float4Matrix ortho2D(float left, float right, float bottom, float top) {
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
    public static Float4Matrix frustum(float left, float right, float bottom, float top, float zNear, float zFar) {
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

        Float4Matrix result = new Float4Matrix(2f * zNear / dX, 0f, (right + left) / dX, 0f, 0f, 2f * zNear / dY,
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
    public static Float4Matrix perspective(float fovy, float aspect, float zNear, float zFar) {
        if (fovy <= 0f || fovy > 180f) {
            throw new IllegalArgumentException("fovy cannot be smaller than or equal to 0 or greater than 180f");
        } else if (aspect <= 0f) {
            throw new IllegalArgumentException("aspect cannot be smaller than or equal to 0");
        }

        float top = (float) (Math.tan(fovy * DEGREESTORADIANS / 2) * zNear);
        float right = top * aspect;
        float dZ = zFar - zNear;

        if (dZ <= 0f) {
            throw new IllegalArgumentException("zNear cannot be greater than or equal to zFar");
        }

        Float4Matrix result = new Float4Matrix((zNear / right), 0, 0, 0, 0, (zNear / top), 0, 0, 0, 0, -(zFar + zNear)
                / dZ, -2 * zFar * zNear / dZ, 0, 0, -1, 0);

        return result;
    }

    /**
     * Helper method that supplies a viewing transformation that allows us to
     * look at the indicated point,
     * 
     * @see "http://www.opengl.org/sdk/docs/man2/xhtml/gluLookAt.xml"
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
    public static Float4Matrix lookAt(Float4Vector eye, Float4Vector at, Float4Vector up) {
        Float4Vector norm = FloatVectorMath.normalize(eye.sub(at));
        Float4Vector crossUpNorm = FloatVectorMath
                .normalize(FloatVectorMath.cross(FloatVectorMath.normalize(up), norm));
        Float4Vector crossNormUpNorm = FloatVectorMath.normalize(FloatVectorMath.cross(norm, crossUpNorm));
        Float4Vector pointIndicator = new Float4Vector(0f, 0f, 0f, 1f);

        Float4Matrix matrix = new Float4Matrix(crossUpNorm, crossNormUpNorm, norm, pointIndicator);

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
    public static Float4Matrix translate(float x, float y, float z) {
        return new Float4Matrix(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
    }

    /**
     * Helper method that creates a translation matrix
     * 
     * @param vec
     *            The vector with which we want to translate
     * @return A translation matrix
     */
    public static Float4Matrix translate(Float3Vector vec) {
        return translate(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Helper method that creates a translation matrix
     * 
     * @param vec
     *            The vector with which we want to translate
     * @return A translation matrix
     */
    public static Float4Matrix translate(Float4Vector vec) {
        return translate(vec.getX(), vec.getY(), vec.getZ());
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
    public static Float4Matrix scale(float x, float y, float z) {
        return new Float4Matrix(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param newScale
     *            The new uniform scale.
     * @return A scaling matrix
     */
    public static Float4Matrix scale(float newScale) {
        return scale(newScale, newScale, newScale);
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param vec
     *            The vector with which we want to scale
     * @return A scaling matrix
     */
    public static Float4Matrix scale(Float3Vector vec) {
        return scale(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Helper method that creates a scaling matrix
     * 
     * @param vec
     *            The vector with which we want to scale
     * @return A scaling matrix
     */
    public static Float4Matrix scale(Float4Vector vec) {
        return scale(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * x-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static Float4Matrix rotationX(float angleDeg) {
        double angleRad = DEGREESTORADIANS * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        return new Float4Matrix(1, 0, 0, 0, 0, cosa, -sina, 0, 0, sina, cosa, 0, 0, 0, 0, 1);
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * y-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static Float4Matrix rotationY(float angleDeg) {
        double angleRad = DEGREESTORADIANS * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        return new Float4Matrix(cosa, 0, sina, 0, 0, 1, 0, 0, -sina, 0, cosa, 0, 0, 0, 0, 1);
    }

    /**
     * Helper method that creates a matrix describing a rotation around the
     * z-axis
     * 
     * @param angleDeg
     *            The rotation angle, in degrees
     * @return The rotation matrix
     */
    public static Float4Matrix rotationZ(float angleDeg) {
        double angleRad = DEGREESTORADIANS * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);

        Float4Matrix m = new Float4Matrix(cosa, -sina, 0, 0, sina, cosa, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

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
    public static Float4Matrix rotate(float angleDeg, float x, float y, float z) {
        double angleRad = DEGREESTORADIANS * angleDeg;
        float cosa = (float) Math.cos(angleRad);
        float sina = (float) Math.sin(angleRad);
        float invc = 1 - cosa;

        Float3Vector n = FloatVectorMath.normalize(new Float3Vector(x, y, z));
        float nx = n.getX();
        float ny = n.getY();
        float nz = n.getZ();

        Float4Matrix result = new Float4Matrix(invc * nx * nx + cosa, invc * nx * ny - sina * nz, invc * nx * nz + sina
                * ny, 0f, invc * nx * ny + sina * nz, invc * ny * ny + cosa, invc * ny * nz - sina * nx, 0f, invc * nx
                * nz - sina * ny, invc * ny * nz + sina * nx, invc * nz * nz + cosa, 0f, 0f, 0f, 0f, 1f

        );

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
    public static Float4Matrix rotate(float angleDeg, Float3Vector axis) {
        Float4Matrix result = rotate(angleDeg, axis.getX(), axis.getY(), axis.getZ());

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
    public static Float4Matrix rotate(float angleDeg, Float4Vector axis) {
        Float4Matrix result = rotate(angleDeg, axis.getX(), axis.getY(), axis.getZ());

        return result;
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(Float2Matrix m) {
        return m.asArray()[0] * m.asArray()[3] - m.asArray()[2] * m.asArray()[1];
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(Float3Matrix m) {
        float minor00 = determinant(exclude(m, 0, 0));
        float minor01 = determinant(exclude(m, 0, 1));
        float minor02 = determinant(exclude(m, 0, 2));

        return m.asArray()[0] * minor00 - m.asArray()[3] * minor01 + m.asArray()[6] * minor02;
    }

    /**
     * Find the determinant for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the determinant
     */
    public static float determinant(Float4Matrix m) {
        float minor00 = determinant(exclude(m, 0, 0));
        float minor01 = determinant(exclude(m, 0, 1));
        float minor02 = determinant(exclude(m, 0, 2));
        float minor03 = determinant(exclude(m, 0, 3));

        return m.asArray()[0] * minor00 - m.asArray()[4] * minor01 + m.asArray()[8] * minor02 + m.asArray()[12]
                * minor03;
    }

    /**
     * Find the cofactors for this matrix
     * 
     * @param m
     *            the input matrix
     * @return the cofactors
     */
    public static Float3Matrix cofactors(Float3Matrix m) {
        Float3Matrix checkerboard = new Float3Matrix(1f, -1f, 1f, -1f, 1f, -1f, 1f, -1f, 1f);
        Float3Matrix minors = minors(m);
        Float3Matrix result = new Float3Matrix();

        for (int i = 0; i < result.getSize(); i++) {
            result.asArray()[i] = minors.asArray()[i] * checkerboard.asArray()[i];
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
    public static Float4Matrix cofactors(Float4Matrix m) {
        Float4Matrix checkerboard = new Float4Matrix(1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f, 1f,
                -1f, 1f);
        Float4Matrix minors = minors(m);
        Float4Matrix result = new Float4Matrix();

        for (int i = 0; i < result.getSize(); i++) {
            result.asArray()[i] = minors.asArray()[i] * checkerboard.asArray()[i];
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
    public static Float3Matrix minors(Float3Matrix m) {
        Float3Matrix result = new Float3Matrix();

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iCol = 0; iCol < 3; iCol++) {
                Float2Matrix excluded = exclude(m, iCol, iRow);
                float det = determinant(excluded);
                result.asArray()[iRow * 3 + iCol] = det;
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
    public static Float4Matrix minors(Float4Matrix m) {
        Float4Matrix result = new Float4Matrix();

        for (int iRow = 0; iRow < 4; iRow++) {
            for (int iCol = 0; iCol < 4; iCol++) {
                Float3Matrix excluded = exclude(m, iCol, iRow);
                float det = determinant(excluded);
                result.asArray()[iRow * 4 + iCol] = det;
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
    public static Float2Matrix exclude(Float3Matrix m, int col, int row) {
        Float2Matrix result = new Float2Matrix();
        int index = 0;

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iCol = 0; iCol < 3; iCol++) {
                if (iRow != row && iCol != col) {
                    result.asArray()[index] = m.asArray()[iRow * 3 + iCol];
                    index++;
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
    public static Float3Matrix exclude(Float4Matrix m, int col, int row) {
        Float3Matrix result = new Float3Matrix();
        int index = 0;

        for (int iRow = 0; iRow < 4; iRow++) {
            for (int iCol = 0; iCol < 4; iCol++) {
                if (iRow != row && iCol != col) {
                    result.asArray()[index] = m.asArray()[iRow * 4 + iCol];
                    index++;
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
    public static Float2Matrix transpose(Float2Matrix m) {
        Float2Matrix result = new Float2Matrix();

        result.asArray()[0] = m.asArray()[0];
        result.asArray()[1] = m.asArray()[2];

        result.asArray()[2] = m.asArray()[1];
        result.asArray()[3] = m.asArray()[3];

        return result;
    }

    /**
     * Get the transposed matrix
     * 
     * @param m
     *            the input matrix
     * @return the transposed matrix
     */
    public static Float3Matrix transpose(Float3Matrix m) {
        Float3Matrix result = new Float3Matrix();

        result.asArray()[0] = m.asArray()[0];
        result.asArray()[1] = m.asArray()[3];
        result.asArray()[2] = m.asArray()[6];

        result.asArray()[3] = m.asArray()[1];
        result.asArray()[4] = m.asArray()[4];
        result.asArray()[5] = m.asArray()[7];

        result.asArray()[6] = m.asArray()[2];
        result.asArray()[7] = m.asArray()[5];
        result.asArray()[8] = m.asArray()[8];

        return result;
    }

    /**
     * Get the transposed matrix
     * 
     * @param m
     *            the input matrix
     * @return the transposed matrix
     */
    public static Float4Matrix transpose(Float4Matrix m) {
        Float4Matrix result = new Float4Matrix();

        result.asArray()[0] = m.asArray()[0];
        result.asArray()[1] = m.asArray()[4];
        result.asArray()[2] = m.asArray()[8];
        result.asArray()[3] = m.asArray()[12];

        result.asArray()[4] = m.asArray()[1];
        result.asArray()[5] = m.asArray()[5];
        result.asArray()[6] = m.asArray()[9];
        result.asArray()[7] = m.asArray()[13];

        result.asArray()[8] = m.asArray()[2];
        result.asArray()[9] = m.asArray()[6];
        result.asArray()[10] = m.asArray()[10];
        result.asArray()[11] = m.asArray()[14];

        result.asArray()[12] = m.asArray()[3];
        result.asArray()[13] = m.asArray()[7];
        result.asArray()[14] = m.asArray()[11];
        result.asArray()[15] = m.asArray()[15];

        return result;
    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static Float2Matrix adjoint(Float2Matrix m) {
        Float2Matrix result = new Float2Matrix();

        result.asArray()[0] = m.asArray()[3];
        result.asArray()[1] = -m.asArray()[1];
        result.asArray()[2] = -m.asArray()[2];
        result.asArray()[3] = m.asArray()[0];

        return result;

    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static Float3Matrix adjoint(Float3Matrix m) {
        return transpose(cofactors(m));

    }

    /**
     * Get the adjoint matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the adjoint matrix
     */
    public static Float4Matrix adjoint(Float4Matrix m) {
        return transpose(cofactors(m));
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static Float2Matrix inverse(Float2Matrix m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        Float2Matrix adj = adjoint(m);

        Float2Matrix inverse = adj.mul(1f / det);

        return inverse;
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static Float3Matrix inverse(Float3Matrix m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        Float3Matrix adj = adjoint(m);

        Float3Matrix inverse = adj.mul(1f / det);

        return inverse;
    }

    /**
     * Get the inverse matrix of the given matrix
     * 
     * @param m
     *            the input matrix
     * @return the inverse matrix
     */
    public static Float4Matrix inverse(Float4Matrix m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException("Determinant 0");
        }

        Float4Matrix adj = adjoint(m);

        Float4Matrix inverse = adj.mul(1f / det);

        return inverse;
    }

    /**
     * Getter for epsilon.
     * 
     * @return the epsilon.
     */
    public static float getEpsilon() {
        return EPSILON;
    }
}
