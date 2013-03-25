package nl.esciencecenter.esight.math;

public class Point4 extends VecF4 {
	/**
	 * Stand-in for a 4-place vector, where the fourth place is always 1f.
	 */
	public Point4() {
		super();
		this.v[3] = 1f;
	}

	/**
	 * Stand-in for a 4-place vector, where the fourth place is always 1f.	 
	 * @param vec
	 * 		The xyz values of this point.
	 */
	public Point4(VecF3 vec) {
		super(vec, 1f);
	}
	
	/**
	 * Stand-in for a 4-place vector, where the fourth place is always 1f.	 
	 * @param v
	 * 		A vector of which the fourth place will be discarded and replaced by 1f.
	 */
	public Point4(VecF4 v) {
		super(v);
		this.v[3] = 1f;
	}
	
	/**
	 * Stand-in for a 4-place vector, where the fourth place is always 1f.	 
	 * @param x
	 * 		The x value of this point.
	 * @param y
	 * 		The y value of this point.
	 * @param z
	 * 		The z value of this point.	 
	 * @param w
	 * 		This value is discarded in favour of 1f.
	 */
	public Point4(float x, float y, float z, float w) {
		super(x, y, z, 1f);
	}
}
