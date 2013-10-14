package nl.esciencecenter.neon.examples.viaAppia;

import java.io.FileNotFoundException;
import java.nio.FloatBuffer;

import javax.media.opengl.GL3;

import nl.esciencecenter.neon.models.graphs.ScatterPlot3D;

public class ScatBuilder implements Runnable {
    private ScatterPlot3D scat;
    private DataReader dr;

    public ScatBuilder() {
        try {
            dr = new DataReader();
            new Thread(dr).start();

            scat = new ScatterPlot3D();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        FloatBuffer[] buffers;

        while (true) {
            buffers = dr.getBuffers();
            if (buffers != null) {
                System.out.println("Added file detected, now rebuilding scatterplot");

                FloatBuffer vertices = buffers[0];
                FloatBuffer colors = buffers[1];

                addToScat(vertices, colors);
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void addToScat(FloatBuffer vertices, FloatBuffer colors) {
        scat.addAll(vertices, colors);
    }

    public synchronized ScatterPlot3D getScatterPlot(GL3 gl) {
        if (scat != null) {
            scat.init(gl);
        }

        return scat;
    }

}
