package nl.esciencecenter.esight.math;

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
public class VecF4 extends VectorF {
    /**
     * Creates a new vector, initialized to 0.
     */
    public VecF4() {
        super(4);
        this.v[0] = 0f;
        this.v[1] = 0f;
        this.v[2] = 0f;
        this.v[3] = 0f;
    }

    /**
     * Creates a new vector by copying the given vector.
     * 
     * @param v
     *            The vector to be copied.
     */
    public VecF4(VecF4 v) {
        super(4);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];
        this.v[3] = v.v[3];
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
    public VecF4(VecF3 v, float v3) {
        super(4);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];
        this.v[3] = v3;
    }

    /**
     * Creates a new vector by copying the given vector, supplemented by the
     * scalar.
     * 
     * @param vector
     *            The vector to be copied.
     * @param f
     *            The additional value to be put into the fourth index.
     */
    public VecF4(Vector vector, float f) {
        super(4);
        if (vector.getSize() == 3) {
            this.v[0] = ((VecF3) vector).v[0];
            this.v[1] = ((VecF3) vector).v[1];
            this.v[2] = ((VecF3) vector).v[2];

        }
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
    public VecF4(float x, float y, float z, float w) {
        super(4);
        this.v[0] = x;
        this.v[1] = y;
        this.v[2] = z;
        this.v[3] = w;
    }

    /**
     * Gives the negated vector of this vector.
     * 
     * @return The new negated vector.
     */
    public VecF4 neg() {
        VecF4 result = new VecF4();
        result.v[0] = -v[0];
        result.v[1] = -v[1];
        result.v[2] = -v[2];
        result.v[3] = -v[3];
        return result;
    }

    /**
     * Adds the given vector to the current vector, and returns the result.
     * 
     * @param u
     *            The vector to be added to this vector.
     * @return The new vector.
     */
    public VecF4 add(VecF4 u) {
        VecF4 result = new VecF4();
        result.v[0] = v[0] + u.v[0];
        result.v[1] = v[1] + u.v[1];
        result.v[2] = v[2] + u.v[2];
        result.v[3] = v[3] + u.v[3];
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
    public VecF4 add(VecF3 u) {
        VecF4 result = new VecF4();
        result.v[0] = v[0] + u.v[0];
        result.v[1] = v[1] + u.v[1];
        result.v[2] = v[2] + u.v[2];
        result.v[3] = v[3];
        return result;
    }

    /**
     * Substracts the given vector from this vector.
     * 
     * @param u
     *            The vector to be substracted from this one.
     * @return The new Vector, which is a result of the substraction.
     */
    public VecF4 sub(VecF4 u) {
        VecF4 result = new VecF4();
        result.v[0] = v[0] - u.v[0];
        result.v[1] = v[1] - u.v[1];
        result.v[2] = v[2] - u.v[2];
        result.v[3] = v[3] - u.v[3];
        return result;
    }

    /**
     * Multiplies the given scalar with this vector.
     * 
     * @param n
     *            The scalar to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecF4 mul(Number n) {
        float fn = n.floatValue();
        VecF4 result = new VecF4();
        result.v[0] = v[0] * fn;
        result.v[1] = v[1] * fn;
        result.v[2] = v[2] * fn;
        result.v[3] = v[3] * fn;
        return result;
    }

    /**
     * Multiplies the given vector with this vector.
     * 
     * @param u
     *            The vector to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecF4 mul(VecF4 u) {
        VecF4 result = new VecF4();
        result.v[0] = v[0] * u.v[0];
        result.v[1] = v[1] * u.v[1];
        result.v[2] = v[2] * u.v[2];
        result.v[3] = v[3] * u.v[3];
        return result;
    }

    /**
     * Divides the current vector with the given scalar.
     * 
     * @param n
     *            The scalar to be divided with.
     * @return The new Vector, which is a result of the division.
     */
    public VecF4 div(Number n) {
        float f = n.floatValue();
        if (f == 0f)
            return new VecF4();
        float fn = 1f / f;

        VecF4 result = new VecF4();
        result.v[0] = v[0] * fn;
        result.v[1] = v[1] * fn;
        result.v[2] = v[2] * fn;
        result.v[3] = v[3] * fn;
        return result;
    }

    public VecF3 stripAlpha() {
        return new VecF3(v[0], v[1], v[2]);
    }

    @Override
    public VecF4 clone() {
        return new VecF4(this);
    }
}
