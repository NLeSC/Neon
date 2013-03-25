package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;

import nl.esciencecenter.esight.exceptions.InverseNotAvailableException;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import com.jogamp.common.nio.Buffers;

public class MatrixFMath {

    public static double degreesToRadians = Math.PI / 180.0;

    public static MatF3 getNormalMatrix(MatF4 mv) {
        MatF3 result = new MatF3();

        double[][] d = { { mv.get(0), mv.get(1), mv.get(2) },
                { mv.get(4), mv.get(5), mv.get(6) },
                { mv.get(8), mv.get(9), mv.get(10) } };

        RealMatrix m = new Array2DRowRealMatrix(d);
        RealMatrix inverse = new LUDecomposition(m).getSolver().getInverse();

        inverse = inverse.transpose();

        result.set(0, (float) inverse.getEntry(0, 0));
        result.set(1, (float) inverse.getEntry(0, 1));
        result.set(2, (float) inverse.getEntry(0, 2));

        result.set(3, (float) inverse.getEntry(1, 0));
        result.set(4, (float) inverse.getEntry(1, 1));
        result.set(5, (float) inverse.getEntry(1, 2));

        result.set(6, (float) inverse.getEntry(2, 0));
        result.set(7, (float) inverse.getEntry(2, 1));
        result.set(8, (float) inverse.getEntry(2, 2));

        return result;
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
    public static MatF4 ortho(float left, float right, float bottom, float top,
            float zNear, float zFar) {
        float dX = right - left;
        float dY = top - bottom;
        float dZ = zFar - zNear;
        float n = zNear;
        float f = zFar;
        float t = top;
        float b = bottom;
        float r = right;
        float l = left;

        MatF4 m = new MatF4(2 / dX, 0, 0, -(l + r) / dX, 0, 2 / dY, 0, -(t + b)
                / dY, 0, 0, -2 / (f - n), -(f + n) / dZ, 0, 0, 0, 1);
        return m;
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
        return ortho(left, right, bottom, top, -1, 1);
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
    public static MatF4 frustum(float left, float right, float bottom,
            float top, float zNear, float zFar) {
        float dX = right - left;
        float dY = top - bottom;
        float dZ = zFar - zNear;
        float n = zNear;
        float f = zFar;
        float t = top;
        float b = bottom;
        float r = right;
        float l = left;

        MatF4 m = new MatF4(2 * n / dX, 0, (r + l) / dX, 0, 0, 2 * n / dY,
                (t + b) / dY, 0, 0, 0, -(f + n) / dZ, -2 * f * n / dZ, 0, 0,
                -1, 0);
        return m;
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
    public static MatF4 perspective(float fovy, float aspect, float zNear,
            float zFar) {
        float t = (float) (Math.tan(fovy * degreesToRadians / 2) * zNear);
        float r = t * aspect;
        float n = zNear;
        float f = zFar;
        float dZ = zFar - zNear;

        MatF4 m = new MatF4((n / r), 0, 0, 0, 0, (n / t), 0, 0, 0, 0, -(f + n)
                / dZ, -2 * f * n / dZ, 0, 0, -1, 0);

        return m;
    }

    /**
     * Helper method that supplies a rotation matrix that allows us to look at
     * the indicated point
     * 
     * @param eye
     *            The coordinates of the eye (camera)
     * @param at
     *            The coordinates of the object we want to look at
     * @param up
     *            The vector indicating the up direction for the camera
     * @return A rotation matrix suitable for multiplication with the
     *         perspective matrix
     */
    public static MatF4 lookAt(VecF4 eye, VecF4 at, VecF4 up) {
        VecF4 eyeneg = eye.clone().neg();

        VecF4 n = VectorFMath.normalize(eye.sub(at));
        VecF4 u = VectorFMath.normalize(VectorFMath.cross(up, n));
        VecF4 v = VectorFMath.normalize(VectorFMath.cross(n, u));
        VecF4 t = new VecF4(0, 0, 0, 1);
        MatF4 c = new MatF4(u, v, n, t);

        return c.mul(translate(eyeneg));
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
        MatF4 m = new MatF4(1, 0, 0, x, 0, 1, 0, y, 0, 0, 1, z, 0, 0, 0, 1);
        return m;
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
        MatF4 m = new MatF4(x, 0, 0, 0, 0, y, 0, 0, 0, 0, z, 0, 0, 0, 0, 1);
        return m;
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
        float ca = (float) Math.cos(angleRad);
        float sa = (float) Math.sin(angleRad);

        MatF4 m = new MatF4(1, 0, 0, 0, 0, ca, -sa, 0, 0, sa, ca, 0, 0, 0, 0, 1);
        return m;
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
        float ca = (float) Math.cos(angleRad);
        float sa = (float) Math.sin(angleRad);

        MatF4 m = new MatF4(ca, 0, sa, 0, 0, 1, 0, 0, -sa, 0, ca, 0, 0, 0, 0, 1);

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
        float ca = (float) Math.cos(angleRad);
        float sa = (float) Math.sin(angleRad);

        MatF4 m = new MatF4(ca, -sa, 0, 0, sa, ca, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);

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
        float c = (float) Math.cos(angleRad);
        float s = (float) Math.sin(angleRad);
        float t = 1 - c;

        VecF3 n = VectorFMath.normalize(new VecF3(x, y, z));
        x = n.v[0];
        y = n.v[1];
        z = n.v[2];

        MatF4 R = new MatF4(t * x * x + c, t * x * y - s * z,
                t * x * z + s * y, 0f, t * x * y + s * z, t * y * y + c, t * y
                        * z - s * x, 0f, t * x * z - s * y, t * y * z + s * x,
                t * z * z + c, 0f, 0f, 0f, 0f, 1f

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
        MatF4 R = rotate(angleDeg, axis.get(0), axis.get(1), axis.get(2));

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
    public static MatF4 rotate(float angleDeg, VecF4 axis) {
        MatF4 R = rotate(angleDeg, axis.get(0), axis.get(1), axis.get(2));

        return R;
    }

    public static float determinant(MatF2 m) {
        return m.m[0] * m.m[3] - m.m[2] * m.m[1];
    }

    public static float determinant(MatF3 m) {
        return m.m[0] * (m.m[4] * m.m[8] - m.m[5] * m.m[7]) - m.m[1]
                * (m.m[3] * m.m[8] - m.m[5] * m.m[6]) + m.m[2]
                * (m.m[3] * m.m[7] - m.m[4] * m.m[6]);
    }

    public static float determinant(MatF4 m) {
        return m.m[12] * m.m[9] * m.m[6] * m.m[3] - m.m[8] * m.m[13] * m.m[6]
                * m.m[3] - m.m[12] * m.m[5] * m.m[10] * m.m[3] + m.m[4]
                * m.m[13] * m.m[10] * m.m[3] + m.m[8] * m.m[5] * m.m[14]
                * m.m[3] - m.m[4] * m.m[9] * m.m[14] * m.m[3] - m.m[12]
                * m.m[9] * m.m[2] * m.m[7] + m.m[8] * m.m[13] * m.m[2] * m.m[7]
                + m.m[12] * m.m[1] * m.m[10] * m.m[7] - m.m[0] * m.m[13]
                * m.m[10] * m.m[7] - m.m[8] * m.m[1] * m.m[14] * m.m[7]
                + m.m[0] * m.m[9] * m.m[14] * m.m[7] + m.m[12] * m.m[5]
                * m.m[2] * m.m[11] - m.m[4] * m.m[13] * m.m[2] * m.m[11]
                - m.m[12] * m.m[1] * m.m[6] * m.m[11] + m.m[0] * m.m[13]
                * m.m[6] * m.m[11] + m.m[4] * m.m[1] * m.m[14] * m.m[11]
                - m.m[0] * m.m[5] * m.m[14] * m.m[11] - m.m[8] * m.m[5]
                * m.m[2] * m.m[15] + m.m[4] * m.m[9] * m.m[2] * m.m[15]
                + m.m[8] * m.m[1] * m.m[6] * m.m[15] - m.m[0] * m.m[9] * m.m[6]
                * m.m[15] - m.m[4] * m.m[1] * m.m[10] * m.m[15] + m.m[0]
                * m.m[5] * m.m[10] * m.m[15];
    }

    public static MatF2 minor(MatF3 m, int col, int row) {
        FloatBuffer r = Buffers.newDirectFloatBuffer(4);

        for (int iRow = 0; iRow < 3; iRow++) {
            for (int iCol = 0; iCol < 3; iCol++) {
                if (iRow != row && iCol != col) {
                    r.put(m.m[iRow * 3 + iCol]);
                }
            }
        }

        System.out.println(r);

        for (int i = 0; i < 4; i++) {
            System.out.println(r.get(i));
        }

        MatF2 n = new MatF2(r);
        return n;
    }

    public static MatF3 minor(MatF4 m, int col, int row) {
        FloatBuffer r = Buffers.newDirectFloatBuffer(9);

        for (int iRow = 0; iRow < 4; iRow++) {
            for (int iCol = 0; iCol < 4; iCol++) {
                if (iRow != row && iCol != col) {
                    r.put(m.m[iRow * 4 + iCol]);
                }
            }
        }

        return new MatF3(r);
    }

    public static MatF2 transpose(MatF2 m) {
        MatF2 r = new MatF2();

        r.m[0] = m.m[0];
        r.m[1] = m.m[2];

        r.m[2] = m.m[1];
        r.m[3] = m.m[3];

        return r;
    }

    public static MatF3 transpose(MatF3 m) {
        MatF3 r = new MatF3();

        r.m[0] = m.m[0];
        r.m[1] = m.m[3];
        r.m[2] = m.m[6];

        r.m[3] = m.m[1];
        r.m[4] = m.m[4];
        r.m[5] = m.m[7];

        r.m[6] = m.m[2];
        r.m[7] = m.m[5];
        r.m[8] = m.m[8];

        return r;
    }

    public static MatF4 transpose(MatF4 m) {
        MatF4 r = new MatF4();

        r.m[0] = m.m[0];
        r.m[1] = m.m[4];
        r.m[2] = m.m[8];
        r.m[3] = m.m[12];

        r.m[4] = m.m[1];
        r.m[5] = m.m[5];
        r.m[6] = m.m[9];
        r.m[7] = m.m[13];

        r.m[8] = m.m[2];
        r.m[9] = m.m[6];
        r.m[10] = m.m[10];
        r.m[11] = m.m[14];

        r.m[12] = m.m[3];
        r.m[13] = m.m[7];
        r.m[14] = m.m[11];
        r.m[15] = m.m[15];

        return r;
    }

    public static MatF2 adjoint(MatF2 m) {
        MatF2 r = new MatF2();

        r.m[0] = m.m[3];
        r.m[1] = -m.m[1];
        r.m[2] = -m.m[2];
        r.m[3] = m.m[1];

        return r;

    }

    public static MatF3 adjoint(MatF3 m) {
        MatF3 r = new MatF3(determinant(minor(m, 0, 0)), -determinant(minor(m,
                1, 0)), determinant(minor(m, 2, 0)),
                -determinant(minor(m, 0, 1)), determinant(minor(m, 1, 1)),
                -determinant(minor(m, 2, 1)), determinant(minor(m, 0, 2)),
                -determinant(minor(m, 1, 2)), determinant(minor(m, 2, 2)));

        return MatrixFMath.transpose(r);

    }

    public static MatF4 adjoint(MatF4 m) {
        MatF4 r = new MatF4(determinant(minor(m, 0, 0)), -determinant(minor(m,
                1, 0)), determinant(minor(m, 2, 0)),
                -determinant(minor(m, 3, 0)), -determinant(minor(m, 0, 1)),
                determinant(minor(m, 1, 1)), -determinant(minor(m, 2, 1)),
                determinant(minor(m, 3, 1)), determinant(minor(m, 0, 2)),
                -determinant(minor(m, 1, 2)), determinant(minor(m, 2, 2)),
                -determinant(minor(m, 3, 2)), -determinant(minor(m, 0, 3)),
                determinant(minor(m, 1, 3)), -determinant(minor(m, 2, 3)),
                determinant(minor(m, 3, 3)));

        return MatrixFMath.transpose(r);
    }

    public static MatF2 inverse(MatF2 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException();
        }

        MatF2 adj = adjoint(m);

        MatF2 inverse = adj.mul(1f / det);

        return inverse;
    }

    public static MatF3 inverse(MatF3 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException();
        }

        MatF3 adj = adjoint(m);

        MatF3 inverse = adj.mul(1f / det);

        return inverse;
    }

    public static MatF4 inverse(MatF4 m) throws InverseNotAvailableException {
        float det = determinant(m);
        if (det == 0f) {
            throw new InverseNotAvailableException();
        }

        MatF4 adj = adjoint(m);

        MatF4 inverse = adj.mul(1f / det);

        return inverse;
    }
}
