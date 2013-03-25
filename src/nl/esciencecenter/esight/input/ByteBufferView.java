package nl.esciencecenter.esight.input;

class ByteBufferView {
    private byte[] buffer;
    private int    offset;

    public ByteBufferView(byte[] buffer) {
        this.buffer = buffer;
        offset = 0;
    }

    public void initialize(byte[] buffer) {
        this.buffer = buffer;
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

    public short getShort() {
        short value = (short) (buffer[offset] | (buffer[offset + 1] << 8));
        offset += 2;
        return value;
    }

    public int getUnsignedShort() {
        int value = buffer[offset] | (buffer[offset + 1] << 8);
        offset += 2;
        return value;
    }

    public int getInt() {
        int value = (buffer[offset] & 0xff) | (buffer[offset + 1] & 0xff) << 8
                | (buffer[offset + 2] & 0xff) << 16
                | (buffer[offset + 3] & 0xff) << 24;
        offset += 4;
        return value;
    }

    public long getLong() {
        long value = buffer[offset] & 0xff
                | (long) (buffer[offset + 1] & 0xff) << 8
                | (long) (buffer[offset + 2] & 0xff) << 16
                | (long) (buffer[offset + 3] & 0xff) << 24
                | (long) (buffer[offset + 4] & 0xff) << 32
                | (long) (buffer[offset + 5] & 0xff) << 40
                | (long) (buffer[offset + 6] & 0xff) << 48
                | (long) (buffer[offset + 7] & 0xff) << 56;
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
