package nl.esciencecenter.esight.math;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * A 3-place short integer vector implementation.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class VecS3 extends VectorS {
    /**
     * Creates a new vector, initialized to 0.
     */
    public VecS3() {
        super(3);
        this.v[0] = 0;
        this.v[1] = 0;
        this.v[2] = 0;
    }

    /**
     * Creates a new vector by copying the given vector.
     * 
     * @param v
     *            The vector to be copied.
     */
    public VecS3(VecS3 v) {
        super(3);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];
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
     */
    public VecS3(short x, short y, short z) {
        super(3);
        this.v[0] = x;
        this.v[1] = y;
        this.v[2] = z;
    }

    public VecS3(VecS4 v) {
        super(3);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];
    }

    /**
     * Gives the negated vector of this vector.
     * 
     * @return The new negated vector.
     */
    public VecS3 neg() {
        VecS3 result = new VecS3();
        result.v[0] = (short) -v[0];
        result.v[1] = (short) -v[1];
        result.v[2] = (short) -v[2];
        return result;
    }

    /**
     * Adds the given vector to the current vector, and returns the result.
     * 
     * @param u
     *            The vector to be added to this vector.
     * @return The new vector.
     */
    public VecS3 add(VecS3 u) {
        VecS3 result = new VecS3();
        result.v[0] = (short) (v[0] + u.v[0]);
        result.v[1] = (short) (v[1] + u.v[1]);
        result.v[2] = (short) (v[2] + u.v[2]);
        return result;
    }

    /**
     * Substracts the given vector from this vector.
     * 
     * @param u
     *            The vector to be substracted from this one.
     * @return The new Vector, which is a result of the substraction.
     */
    public VecS3 sub(VecS3 u) {
        VecS3 result = new VecS3();
        result.v[0] = (short) (v[0] - u.v[0]);
        result.v[1] = (short) (v[1] - u.v[1]);
        result.v[2] = (short) (v[2] - u.v[2]);
        return result;
    }

    /**
     * Multiplies the given scalar with this vector.
     * 
     * @param n
     *            The scalar to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecS3 mul(Number n) {
        short fn = n.shortValue();
        VecS3 result = new VecS3();
        result.v[0] = (short) (v[0] * fn);
        result.v[1] = (short) (v[1] * fn);
        result.v[2] = (short) (v[2] * fn);
        return result;
    }

    /**
     * Multiplies the given vector with this vector.
     * 
     * @param u
     *            The vector to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecS3 mul(VecS3 u) {
        VecS3 result = new VecS3();
        result.v[0] = (short) (v[0] * u.v[0]);
        result.v[1] = (short) (v[1] * u.v[1]);
        result.v[2] = (short) (v[2] * u.v[2]);
        return result;
    }

    /**
     * Divides the current vector with the given scalar.
     * 
     * @param n
     *            The scalar to be divided with.
     * @return The new Vector, which is a result of the division.
     */
    public VecS3 div(Number n) {
        short f = n.shortValue();

        VecS3 result = new VecS3();
        result.v[0] = (short) (v[0] / f);
        result.v[1] = (short) (v[1] / f);
        result.v[2] = (short) (v[2] / f);
        return result;
    }

    @Override
    public VecS3 clone() {
        return new VecS3(this);
    }

    @Override
    public int hashCode() {
        int hashCode = (v[0] + 23 * 6833 + v[1] + 7 * 7207 + v[2] + 11 * 7919);
        return hashCode;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject)
            return true;
        if (!(thatObject instanceof VecS3))
            return false;

        // cast to native object is now safe
        VecS3 that = (VecS3) thatObject;

        // now a proper field-by-field evaluation can be made
        return (v[0] == that.v[0] && v[1] == that.v[1] && v[2] == that.v[2]);
    }
}
