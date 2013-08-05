package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;
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
 * Abstract class for all Matrices that provides several utility functions.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public abstract class MatrixF {
    /** The main storage array for this matrix. */
    private float m[];

    /** The same storage, but in FloatBuffer form. */
    private FloatBuffer buf;

    private int size;

    /**
     * Basic constructor for MatrixF.
     * 
     * @param size
     *            the number of floats to be stored in this matrix.
     */
    protected MatrixF(int size) {
        this.size = size;
        m = new float[size];
        buf = FloatBuffer.wrap(m);
        buf.rewind();
    }

    /**
     * Returns the flattened Array associated with this matrix.
     * 
     * @return This matrix as a flat Array.
     */
    public float[] asArray() {
        return getMatrix();
    }

    /**
     * Returns the FloatBuffer associated with this matrix.
     * 
     * @return This matrix as a FloatBuffer.
     */
    public FloatBuffer asBuffer() {
        getBuffer().rewind();
        return getBuffer();
    }

    /**
     * Returns the value of this matrix at position i,j.
     * 
     * @param i
     *            The column.
     * @param j
     *            The row.
     * @return The value at index i,j.
     */
    public float get(int i, int j) {
        int rowSize = (int) Math.sqrt(getMatrix().length);
        return getMatrix()[i * rowSize + j];
    }

    /**
     * Returns the value of this matrix at position i.
     * 
     * @param i
     *            The index.
     * @return The value at index i.
     */
    public float get(int i) {
        return getMatrix()[i];
    }

    /**
     * Sets the value of this matrix at position i,j.
     * 
     * @param i
     *            The column.
     * @param j
     *            The row.
     * @param f
     *            The new value.
     */
    public void set(int i, int j, float f) {
        int rowSize = (int) Math.sqrt(getMatrix().length);
        getMatrix()[i * rowSize + j] = f;
    }

    /**
     * Sets the value of this matrix at position i.
     * 
     * @param i
     *            The column.
     * @param f
     *            The new value.
     */
    public void set(int i, float f) {
        getMatrix()[i] = f;
    }

    @Override
    public String toString() {
        int rowSize = (int) Math.sqrt(getMatrix().length);

        StringBuffer tmpBuf = new StringBuffer();
        for (int i = 0; i < getMatrix().length; i++) {
            if (i != 0 && i % rowSize == 0) {
                tmpBuf.append("\n");
            }
            tmpBuf.append(getMatrix()[i]);
            tmpBuf.append(" ");
        }

        return tmpBuf.toString();
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
        result = prime * result + ((getBuffer() == null) ? 0 : getBuffer().hashCode());
        result = prime * result + Arrays.hashCode(getMatrix());
        result = prime * result + getSize();
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
        MatrixF other = (MatrixF) obj;
        if (getBuffer() == null) {
            if (other.getBuffer() != null) {
                return false;
            }
        } else if (!getBuffer().equals(other.getBuffer())) {
            return false;
        }
        if (!Arrays.equals(getMatrix(), other.getMatrix())) {
            return false;
        }
        if (getSize() != other.getSize()) {
            return false;
        }
        return true;
    }

    /**
     * Getter for m.
     * 
     * @return the m.
     */
    public float[] getMatrix() {
        return m;
    }

    /**
     * Setter for m.
     * 
     * @param m
     *            the m to set
     */
    public void setMatrix(float m[]) {
        this.m = new float[m.length];
        for (int i = 0; i < m.length; i++) {
            this.m[i] = m[i];
        }
    }

    /**
     * Getter for buf.
     * 
     * @return the buf.
     */
    public FloatBuffer getBuffer() {
        return buf;
    }

    /**
     * Setter for buf.
     * 
     * @param buf
     *            the buf to set
     */
    public void setBuffer(FloatBuffer buf) {
        this.buf = buf;
    }

    /**
     * Getter for size.
     * 
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Setter for size.
     * 
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

}
