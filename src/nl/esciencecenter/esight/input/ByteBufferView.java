package nl.esciencecenter.esight.input;

/**
 * Bytebuffer (as byte[]) transformation class. Used for Touch events.
 * 
 * @author Paul Melis, SurfSARA
 */
class ByteBufferView {
    private byte[] buffer;
    private int offset;

    public ByteBufferView(byte[] buffer) {
        this.buffer = buffer.clone();
        offset = 0;
    }

    public void initialize(byte[] buffer) {
        this.buffer = buffer.clone();
        offset = 0;
    }

    public void initialize() {
        offset = 0;
    }

    public byte getByte() {
        byte value = buffer[offset];
        offset++;
        return value;
    }

    public int getInt() {
        // bytebuffer to integer representation conversion, not boolean
        // complexity as sonar suggests.
        int value = (buffer[offset] & 0xff) | (buffer[offset + 1] & 0xff) << 8 | (buffer[offset + 2] & 0xff) << 16
                | (buffer[offset + 3] & 0xff) << 24; // NOSONAR
        offset += 4;
        return value;
    }

    public long getLong() {
        // bytebuffer to long representation conversion, not boolean complexity
        // as sonar suggests.
        long value = buffer[offset] & 0xff | (long) (buffer[offset + 1] & 0xff) << 8
                | (long) (buffer[offset + 2] & 0xff) << 16 | (long) (buffer[offset + 3] & 0xff) << 24
                | (long) (buffer[offset + 4] & 0xff) << 32 | (long) (buffer[offset + 5] & 0xff) << 40
                | (long) (buffer[offset + 6] & 0xff) << 48 | (long) (buffer[offset + 7] & 0xff) << 56; // NOSONAR
        offset += 8;
        return value;
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }
}
