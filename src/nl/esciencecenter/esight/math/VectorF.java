package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;

public abstract class VectorF {
	protected float v[];
	private FloatBuffer buf;
	
	protected VectorF(int size) {
		v = new float[size];
		buf = FloatBuffer.wrap(v);
		buf.rewind();
	}
	
	/**
     * Retrieves the value of the vector at the given index.
     * @param i
     * 		The index.
     * @return
     * 		The value of the vector at index i.
     */
    public float get(int i) {
        return v[i];
    }
    
    /**
     * Sets the value of the vector at the given index.
     * @param i
     * 		The index.     
     * @param u
     * 		The new value.
     */
    public void set(int i, float u) {  
    	v[i] = u;
    }
	
	/**
     * Returns the flattened Array associated with this vector.
     * @return
     * 		This matrix as a flat Array.
     */    
    public float[] asArray() {
        return v;
    }
    
    /**
     * Returns the FloatBuffer associated with this vector.
     * @return
     * 		This vector as a FloatBuffer.
     */
    public FloatBuffer asBuffer() {
    	buf.rewind();
    	return buf;
    }
    
    public String toString() {
    	String result = "";
		for (int i=0; i<v.length; i++) {
			result += (v[i]+" ");
		}
		
		return result;
	}
}
