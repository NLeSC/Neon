package nl.esciencecenter.esight.math;

import java.nio.ShortBuffer;

public abstract class VectorS {
    protected short v[];
    private final ShortBuffer buf;

    protected VectorS(int size) {
        v = new short[size];
        buf = ShortBuffer.wrap(v);
        buf.rewind();
    }

    /**
     * Retrieves the value of the vector at the given index.
     * 
     * @param i
     *            The index.
     * @return The value of the vector at index i.
     */
    public short get(int i) {
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
    public void set(int i, short u) {
        v[i] = u;
    }

    /**
     * Returns the flattened Array associated with this vector.
     * 
     * @return This matrix as a flat Array.
     */
    public short[] asArray() {
        return v;
    }

    /**
     * Returns the ShortBuffer associated with this vector.
     * 
     * @return This vector as a ShortBuffer.
     */
    public ShortBuffer asBuffer() {
        buf.rewind();
        return buf;
    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < v.length; i++) {
            result += (v[i] + " ");
        }

        return result;
    }
}
