package nl.esciencecenter.esight.input;

class TouchPoint {
    public int id;
    public int state;
    public float tx, ty; // Touch coordinates
    public float nx, ny; // Screen coordinates (normalized)
    public int   si, sj; // Screen coordinates (pixels)
}
