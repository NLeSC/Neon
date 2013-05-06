package nl.esciencecenter.esight.math;

public class Vector {
    public static enum Type {
        LONG, FLOAT, INT, SHORT, BYTE
    }

    public static int SIZE_LONG = 8;
    public static int SIZE_FLOAT = 4;
    public static int SIZE_INT = 4;
    public static int SIZE_SHORT = 2;
    public static int SIZE_BYTE = 1;

    public final int size;
    public final Type type;

    public Vector(int size, Type type) {
        this.size = size;
        this.type = type;
    }

    /**
     * Getter for the size (number of places) for this vector.
     * 
     * @return the size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter for the type.
     * 
     * @return the type.
     */
    public Type getType() {
        return type;
    }
}
