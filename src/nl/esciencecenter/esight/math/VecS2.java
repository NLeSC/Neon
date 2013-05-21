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
 * A 2-place short integer vector implementation.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class VecS2 extends VectorS {
    /**
     * Creates a new vector, initialized to 0.
     */
    public VecS2() {
        super(2);
        this.v[0] = 0;
        this.v[1] = 0;
    }

    /**
     * Creates a new vector by copying the given vector.
     * 
     * @param v
     *            The vector to be copied.
     */
    public VecS2(VecS2 v) {
        super(2);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
    }

    /**
     * Creates a new vector with the given values.
     * 
     * @param x
     *            The value to be put in the first position.
     * @param y
     *            The value to be put in the second position.
     */
    public VecS2(short x, short y) {
        super(2);
        this.v[0] = x;
        this.v[1] = y;
    }

    /**
     * Gives the negated vector of this vector.
     * 
     * @return The new negated vector.
     */
    public VecS2 neg() {
        VecS2 result = new VecS2();
        result.v[0] = (short) -v[0];
        result.v[1] = (short) -v[1];
        return result;
    }

    /**
     * Adds the given vector to the current vector, and returns the result.
     * 
     * @param u
     *            The vector to be added to this vector.
     * @return The new vector.
     */
    public VecS2 add(VecS2 u) {
        VecS2 result = new VecS2();
        result.v[0] = (short) (v[0] + u.v[0]);
        result.v[1] = (short) (v[1] + u.v[1]);
        return result;
    }

    /**
     * Substracts the given vector from this vector.
     * 
     * @param u
     *            The vector to be substracted from this one.
     * @return The new Vector, which is a result of the substraction.
     */
    public VecS2 sub(VecS2 u) {
        VecS2 result = new VecS2();
        result.v[0] = (short) (v[0] - u.v[0]);
        result.v[1] = (short) (v[1] - u.v[1]);
        return result;
    }

    /**
     * Multiplies the given scalar with this vector.
     * 
     * @param n
     *            The scalar to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecS2 mul(Number n) {
        short fn = n.shortValue();
        VecS2 result = new VecS2();
        result.v[0] = (short) (v[0] * fn);
        result.v[1] = (short) (v[1] * fn);
        return result;
    }

    /**
     * Multiplies the given vector with this vector.
     * 
     * @param u
     *            The vector to be multiplied with this one.
     * @return The new Vector, which is a result of the multiplication.
     */
    public VecS2 mul(VecS2 u) {
        VecS2 result = new VecS2();
        result.v[0] = (short) (v[0] * u.v[0]);
        result.v[1] = (short) (v[1] * u.v[1]);
        return result;
    }

    /**
     * Divides the current vector with the given scalar.
     * 
     * @param n
     *            The scalar to be divided with.
     * @return The new Vector, which is a result of the division.
     */
    public VecS2 div(Number n) {
        short f = n.shortValue();

        VecS2 result = new VecS2();
        result.v[0] = (short) (v[0] / f);
        result.v[1] = (short) (v[1] / f);
        return result;
    }

    @Override
    public VecS2 clone() {
        return new VecS2(this);
    }

    @Override
    public int hashCode() {
        int hashCode = (v[0] + 23 * 6833 + v[1] + 7 * 7207);
        return hashCode;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (this == thatObject)
            return true;
        if (!(thatObject instanceof VecS2))
            return false;

        // cast to native object is now safe
        VecS2 that = (VecS2) thatObject;

        // now a proper field-by-field evaluation can be made
        return (v[0] == that.v[0] && v[1] == that.v[1]);
    }
}
