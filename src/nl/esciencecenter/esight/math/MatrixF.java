package nl.esciencecenter.esight.math;

import java.nio.FloatBuffer;

public abstract class MatrixF {
	protected float m[];
	protected FloatBuffer buf;
	
	protected MatrixF(int size) {
		m = new float[size];
    	buf = FloatBuffer.wrap(m);
    	buf.rewind();
	}
	
    /**
     * Returns the flattened Array associated with this matrix.
     * @return
     * 		This matrix as a flat Array.
     */    
    public float[] asArray() {
        return m;
    }
    
    /**
     * Returns the FloatBuffer associated with this matrix.
     * @return
     * 		This matrix as a FloatBuffer.
     */
    public FloatBuffer asBuffer() {
    	buf.rewind();
    	return buf;
    }
    
    
    /**
     * Returns the value of this matrix at position i,j. 
     * @param i
     * 		The column.
     * @param j
     * 		The row.
     * @return
     * 		The value at index i,j.
     */
    public float get(int i, int j) {
    	int rowSize = (int) Math.sqrt(m.length);
        return m[i * rowSize + j];
    }
    
    /**
     * Returns the value of this matrix at position i. 
     * @param i
     * 		The index.
     * @return
     * 		The value at index i.
     */
    public float get(int i) {
        return m[i];
    }
    
    /**
     * Sets the value of this matrix at position i,j. 
     * @param i
     * 		The column.
     * @param j
     * 		The row.
     * @param f
     * 		The new value.
     */
    public void set(int i, int j, float f) {   
    	int rowSize = (int) Math.sqrt(m.length);
        m[i * rowSize + j] = f;
    }
    
    /**
     * Sets the value of this matrix at position i. 
     * @param i
     * 		The column.
     * @param f
     * 		The new value.
     */
    public void set(int i, float f) {        
        m[i] = f;
    }
    
    public String toString() {
    	int rowSize = (int) Math.sqrt(m.length);
    	String result = "";
    	
    	for (int i=0; i<m.length; i++) {    		
    		if (i != 0 && i % rowSize == 0) result += "\n";
    		
    		result += m[i] + " ";
    	}
    	
    	return result;
    }
}
