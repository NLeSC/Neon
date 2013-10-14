package nl.esciencecenter.neon.math;

import java.nio.FloatBuffer;

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
 * A 4-place float vector implementation.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class Float4Vector implements FloatVector {
    /** The number of elements in this vector */
    private static final int SIZE = 4;

    private float x, y, z, w;

    /**
     * Creates a new vector, initialized to 0.
     */
    public Float4Vector() {
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
        this.w = 0f;
    }

    /**
     * Creates a new vector by copying the given vector.
     * 
     * @param v
     *            The vector to be copied.
     */
    public Float4Vector(Float4Vector v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        this.w = v.getW();
    }

    /**
     * Creates a new vector by copying the given vector, supplemented by the
     * scalar.
     * 
     * @param v
     *            The vector to be copied.
     * @param v3
     *            The additional value to be put into the fourth index.
     */
    public Float4Vector(Float3Vector v, float v3) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        this.w = v3;
    }

    /**
     * Creates a new vector with the given values.
     * 
     * @param x
     *            The value to be put in the first position.
     * @param y
     *            The value to be put in the second position.
     * @param z
     *            The value to be put in the third position.
     * @param w
     *            The value to be put in the fourth position.
     */
    public Float4Vector(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Gives the negated vector of this vector.
     * 
     * @return The new negated vector.
     */
    public Float4Vector neg() {
        Float4Vector result = new Float4Vector();
        result.setX(-x);
        result.setY(-y);
        result.setZ(-z);
        result.setW(-w);
        return result;
    }

    /**
     * Adds the given vector to the current vector, and returns the result.
     * 
     * @param u
     *            The vector to be added to this vector.
     * @return The new vector.
     */
    public Float4Vector add(Float4Vector u) {
        Float4Vector result = new Float4Vector();
        result.setX(x + u.getX());
        result.setY(y + u.getY());
        result.setZ(z + u.getZ());
        result.setW(w + u.getW());
        return result;
    }

    /**
     * Adds the given vector to the current vector, and returns the result.
     * leaves the last place of the original vector untouched.
     * 
     * @param u
     *            The vector to be added to this vector.
     * @return The new vector.
     */
    public Float4Vector add(Float3Vector u) {
        Float4Vector result = new Float4Vector();
        result.setX(x + u.getX());
        result.setY(y + u.getY());
        result.setZ(z + u.getZ());
        result.setW(w);
        return result;
    }

    /**
     * Substracts the given vector from this vector.
     * 
     * @param u
     *            The vector to be substracted from this one.
     * @return The new Vector, which is a result of the substraction.
     */
    public Float4Vector sub(Float4Vector u) {
        Float4Vector result = new Float4Vector();
        result.setX(x - u.getX());
        result.setY(y - u.getY());
        result.setZ(z - u.getZ());
        result.setW(w - u.getW());
        return result;
    }

    /**
     * Multiplies the given scalar with this vector.
     * 
     * @param n
     *            The scalar to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public Float4Vector mul(Number n) {
        float fn = n.floatValue();
        Float4Vector result = new Float4Vector();
        result.setX(x * fn);
        result.setY(y * fn);
        result.setZ(z * fn);
        result.setW(w * fn);

        return result;
    }

    /**
     * Divides the current vector with the given scalar.
     * 
     * @param n
     *            The scalar to be divided with.
     * @return The new Vector, which is a result of the division.
     */
    public Float4Vector div(Number n) {
        float fn = n.floatValue();
        if (fn == 0f) {
            return new Float4Vector();
        }
        float divfn = 1f / fn;

        Float4Vector result = new Float4Vector();
        result.setX(x * divfn);
        result.setY(y * divfn);
        result.setZ(z * divfn);
        result.setW(w * divfn);
        return result;
    }

    public Float3Vector stripAlpha() {
        return new Float3Vector(x, y, z);
    }

    @Override
    public FloatBuffer asBuffer() {
        FloatBuffer result = FloatBuffer.allocate(SIZE);
        result.put(x);
        result.put(y);
        result.put(z);
        result.put(w);

        result.rewind();

        return result;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    /**
     * Getter for x.
     * 
     * @return the x.
     */
    public float getX() {
        return x;
    }

    /**
     * Setter for x.
     * 
     * @param x
     *            the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Getter for y.
     * 
     * @return the y.
     */
    public float getY() {
        return y;
    }

    /**
     * Setter for y.
     * 
     * @param y
     *            the y to set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Getter for z.
     * 
     * @return the z.
     */
    public float getZ() {
        return z;
    }

    /**
     * Setter for z.
     * 
     * @param z
     *            the z to set
     */
    public void setZ(float z) {
        this.z = z;
    }

    /**
     * Getter for u.
     * 
     * @return the u.
     */
    public float getW() {
        return w;
    }

    /**
     * Setter for w.
     * 
     * @param w
     *            the w to set
     */
    public void setW(float w) {
        this.w = w;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(w);
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Float4Vector other = (Float4Vector) obj;
        if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w)) {
            return false;
        }
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Float4Vector [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
    }

    public float[] asArray() {
        return new float[] { x, y, z, w };
    }

}
