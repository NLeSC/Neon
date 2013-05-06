package nl.esciencecenter.esight.math;

public class VecF3 extends VectorF {	
	/**
     * Creates a new vector, initialized to 0.
     */
    public VecF3() {
    	super(3);
        this.v[0] = 0f;
        this.v[1] = 0f;
        this.v[2] = 0f;
    }
    
    /**
     * Creates a new vector by copying the given vector.     
     * @param v
     * 		The vector to be copied.
     */
    public VecF3(VecF3 v) {
    	super(3);
        this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];
    }
    
    /**
     * Creates a new vector with the given values.     
     * @param x
     * 		The value to be put in the first position.
     * @param y
     * 		The value to be put in the second position.
     * @param z
     * 		The value to be put in the third position.
     */
    public VecF3(float x, float y, float z) {
    	super(3);
        this.v[0] = x;
        this.v[1] = y;
        this.v[2] = z;
    }

    
    public VecF3(VecF4 v) {
    	super(3);
    	this.v[0] = v.v[0];
        this.v[1] = v.v[1];
        this.v[2] = v.v[2];    	
	}

	/**
     * Gives the negated vector of this vector. 
     * @return
     * 		The new negated vector.
     */
    public VecF3 neg() {
    	VecF3 result = new VecF3();
    	result.v[0] = -v[0];
    	result.v[1] = -v[1];
    	result.v[2] = -v[2];
    	return result;
    }

    
    /**
     * Adds the given vector to the current vector, and returns the result.
     * @param u
     * 		The vector to be added to this vector.
     * @return
     * 		The new vector.
     */
    public VecF3 add(VecF3 u) {
    	VecF3 result = new VecF3();
    	result.v[0] = v[0] + u.v[0];
    	result.v[1] = v[1] + u.v[1];
    	result.v[2] = v[2] + u.v[2];
    	return result;
    }

	/**
	 * Substracts the given vector from this vector.
	 * @param u
	 * 		The vector to be substracted from this one.
	 * @return
	 * 		The new Vector, which is a result of the substraction.
	 */
	public VecF3 sub(VecF3 u) {
		VecF3 result = new VecF3();
    	result.v[0] = v[0] - u.v[0];
    	result.v[1] = v[1] - u.v[1];
    	result.v[2] = v[2] - u.v[2];
    	return result;
	}

	/**
	 * Multiplies the given scalar with this vector.
	 * @param n
	 * 		The scalar to be multiplied with this one.
	 * @return
	 * 		The new Vector, which is a result of the multiplication.
	 */
	public VecF3 mul(Number n) {
		float fn = n.floatValue();
		VecF3 result = new VecF3();
    	result.v[0] = v[0] *fn;
    	result.v[1] = v[1] *fn;
    	result.v[2] = v[2] *fn;
    	return result;
	}

	/**
	 * Multiplies the given vector with this vector.
	 * @param u
	 * 		The vector to be multiplied with this one.
	 * @return
	 * 		The new Vector, which is a result of the multiplication.
	 */
	public VecF3 mul(VecF3 u) {
		VecF3 result = new VecF3();
    	result.v[0] = v[0] * u.v[0];
    	result.v[1] = v[1] * u.v[1];
    	result.v[2] = v[2] * u.v[2];
    	return result;
	}

	/**
	 * Divides the current vector with the given scalar.
	 * @param n
	 * 		The scalar to be divided with.
	 * @return
	 * 		The new Vector, which is a result of the division.
	 */
	public VecF3 div(Number n) {  
		float f = n.floatValue();
    	if (f == 0f) return new VecF3();
		float fn = 1f / f;

		VecF3 result = new VecF3();
    	result.v[0] = v[0] *fn;
    	result.v[1] = v[1] *fn;
    	result.v[2] = v[2] *fn;
    	return result;
	}
    
    public VecF3 clone() {
    	return new VecF3(this);
    }
    
    @Override
	public int hashCode() {
		int hashCode = (int) (v[0]+23 * 6833 + v[1]+7 *7207 + v[2]+11 * 7919);
		return hashCode;
	}
    
    @Override
	public boolean equals(Object thatObject) {
		if (this == thatObject)
			return true;
		if (!(thatObject instanceof VecF3))
			return false;

		// cast to native object is now safe
		VecF3 that = (VecF3) thatObject;

		// now a proper field-by-field evaluation can be made
		return (v[0] == that.v[0] && v[1] == that.v[1] && v[2] == that.v[2]);
	}
}
