package nl.esciencecenter.esight.input;

/**
 * Bytebuffer (as byte[]) transformation class. Used for Touch events.
 * 
 * @author Paul Melis, SurfSARA
 */
class ByteBufferView {
    private static final int BITSHIFT = 0xff;

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
        int value = (buffer[offset] & BITSHIFT) | (buffer[offset + 1] & BITSHIFT) << 8
                | (buffer[offset + 2] & BITSHIFT) << 16 | (buffer[offset + 3] & BITSHIFT) << 24;
        offset += 4;
        return value;
    }

    public long getLong() {
        long value = buffer[offset] & BITSHIFT | (long) (buffer[offset + 1] & BITSHIFT) << 8
                | (long) (buffer[offset + 2] & BITSHIFT) << 16 | (long) (buffer[offset + 3] & BITSHIFT) << 24
                | (long) (buffer[offset + 4] & BITSHIFT) << 32 | (long) (buffer[offset + 5] & BITSHIFT) << 40
                | (long) (buffer[offset + 6] & BITSHIFT) << 48 | (long) (buffer[offset + 7] & BITSHIFT) << 56;
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
