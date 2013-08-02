package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;
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
 * Generic abstract type for all float vector implementations.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class VectorF extends Vector {
    protected float v[];
    private final FloatBuffer buf;

    protected VectorF(int size) {
        super(size, Type.FLOAT);
        v = new float[size];
        buf = FloatBuffer.wrap(v);
        buf.rewind();
    }

    /**
     * Retrieves the value of the vector at the given index.
     * 
     * @param i
     *            The index.
     * @return The value of the vector at index i.
     */
    public float get(int i) {
        return v[i];
    }

    /**
     * Sets the value of the vector at the given index.
     * 
     * @param i
     *            The index.
     * @param u
     *            The new value.
     */
    public void set(int i, float u) {
        v[i] = u;
    }

    /**
     * Returns the flattened Array associated with this vector.
     * 
     * @return This matrix as a flat Array.
     */
    public float[] asArray() {
        return v;
    }

    /**
     * Returns the FloatBuffer associated with this vector.
     * 
     * @return This vector as a FloatBuffer.
     */
    public FloatBuffer asBuffer() {
        buf.rewind();
        return buf;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < v.length; i++) {
            buf.append(v[i] + " ");
        }

        return buf.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((buf == null) ? 0 : buf.hashCode());
        result = prime * result + Arrays.hashCode(v);
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
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VectorF other = (VectorF) obj;
        if (buf == null) {
            if (other.buf != null) {
                return false;
            }
        } else if (!buf.equals(other.buf)) {
            return false;
        }
        if (!Arrays.equals(v, other.v)) {
            return false;
        }
        return true;
    }

}
