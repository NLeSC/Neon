package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;

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
    }

    /**
     * Returns the flattened Array associated with this matrix.
     * 
     * @return This matrix as a flat Array.
     */
    public float[] asArray() {
        return m;
    }

    /**
     * Returns the FloatBuffer associated with this matrix.
     * 
     * @return This matrix as a FloatBuffer.
     */
    public FloatBuffer asBuffer() {
        return FloatBuffer.wrap(m);
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
    public float get(int i, int j) throws IllegalArgumentException {
        int rowSize = (int) Math.sqrt(m.length);
        if (i >= rowSize || j >= rowSize) {
            throw new IllegalArgumentException("either i or j was larger than the row/column size of this matrix");
        }
        return m[i * rowSize + j];
    }

    /**
     * Returns the value of this matrix at position i.
     * 
     * @param i
     *            The index.
     * @return The value at index i.
     */
    public float get(int i) {
        return m[i];
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
    public void set(int i, int j, float f) throws IllegalArgumentException {
        int rowSize = (int) Math.sqrt(m.length);
        if (i >= rowSize || j >= rowSize) {
            throw new IllegalArgumentException("either i or j was larger than the row/column size of this matrix");
        }
        m[i * rowSize + j] = f;
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
        m[i] = f;
    }

    @Override
    public String toString() {
        int rowSize = (int) Math.sqrt(m.length);

        StringBuffer tmpBuf = new StringBuffer();
        for (int i = 0; i < m.length; i++) {
            if (i != 0 && i % rowSize == 0) {
                tmpBuf.append("\n");
            }
            tmpBuf.append(m[i]);
            tmpBuf.append(" ");
        }

        return tmpBuf.toString();
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
