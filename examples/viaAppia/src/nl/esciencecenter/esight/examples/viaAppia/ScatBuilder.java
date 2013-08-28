package nl.esciencecenter.esight.examples.viaAppia;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL3;

import nl.esciencecenter.esight.examples.viaAppia.DataReader.MapPoint;
import nl.esciencecenter.esight.math.Color4;
import nl.esciencecenter.esight.math.Point4;
import nl.esciencecenter.esight.models.graphs.ScatterPlot3D;

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
        while (true) {
            if (dr.hasAddedFile()) {
                System.out.println("Added file detected, now rebuilding scatterplot");

                MapPoint minP = dr.getMinimumPoint();
                MapPoint maxP = dr.getMaximumPoint();

                float minLat = minP.getLatitude(), minLon = minP.getLongitude(), minHgt = minP.getHeight();
                float maxLat = maxP.getLatitude(), maxLon = maxP.getLongitude(), maxHgt = maxP.getHeight();
                float diffLat = maxLat - minLat;
                float diffLon = maxLon - minLon;
                float diffHgt = maxHgt - minHgt;

                List<MapPoint> points = dr.getPoints();

                List<Point4> pointsToAdd = new ArrayList<Point4>();
                List<Color4> colorsToAdd = new ArrayList<Color4>();

                for (MapPoint mp : points) {
                    if (mp != null) {

                        float x = (mp.getLatitude() - minLat) / diffLat;
                        float y = (mp.getLongitude() - minLon) / diffLon;
                        float z = (mp.getHeight() - minHgt) / diffHgt;
                        pointsToAdd.add(new Point4(x, y, z));
                        colorsToAdd.add(new Color4(mp.getR() / 255f, mp.getG() / 255f, mp.getB() / 255f, 1f));
                    }
                }

                addToScat(pointsToAdd, colorsToAdd);
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private synchronized void addToScat(List<Point4> pointsToAdd, List<Color4> colorsToAdd) {
        scat.addAll(pointsToAdd, colorsToAdd);
        scat.prepareBuffers();
    }

    public synchronized ScatterPlot3D getScatterPlot(GL3 gl) {
        if (scat != null) {
            scat.init(gl);
        }

        return scat;
    }

}
