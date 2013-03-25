package nl.esciencecenter.esight.input;

interface TouchEventHandler {
    public void OnTouchPoints(double timestamp, TouchPoint[] points, int n);
}
