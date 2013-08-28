package nl.esciencecenter.esight.examples.viaAppia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader implements Runnable {
    public class MapPoint {
        private float latitude, longitude, height, r, g, b;

        public MapPoint(MapPoint other) {
            this.latitude = other.latitude;
            this.longitude = other.longitude;
            this.height = other.height;
            this.r = other.r;
            this.g = other.g;
            this.b = other.b;
        }

        public MapPoint(float latitude, float longitude, float height, float r, float g, float b) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.height = height;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public float getLatitude() {
            return latitude;
        }

        public float getLongitude() {
            return longitude;
        }

        public float getHeight() {
            return height;
        }

        public float getR() {
            return r;
        }

        public float getG() {
            return g;
        }

        public float getB() {
            return b;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public void setR(float r) {
            this.r = r;
        }

        public void setG(float g) {
            this.g = g;
        }

        public void setB(float b) {
            this.b = b;
        }
    }

    private List<MapPoint> mapPoints;

    private MapPoint minimumPoint;
    private MapPoint maximumPoint;

    private boolean addedFile = false;

    public DataReader() throws FileNotFoundException {
        mapPoints = new ArrayList<MapPoint>();
        minimumPoint = new MapPoint(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE,
                Float.MAX_VALUE, Float.MAX_VALUE);
        maximumPoint = new MapPoint(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE,
                Float.MIN_VALUE, Float.MIN_VALUE);
    }

    private void readFile(File dataFile) {
        List<MapPoint> tmpList = new ArrayList<MapPoint>();

        MapPoint newMinimum = new MapPoint(minimumPoint);
        MapPoint newMaximum = new MapPoint(maximumPoint);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] splitLine = sCurrentLine.trim().split("\\s+");

                try {
                    float lat = Float.parseFloat(splitLine[0]);
                    float lon = Float.parseFloat(splitLine[1]);
                    float hgt = Float.parseFloat(splitLine[2]);
                    float r = Float.parseFloat(splitLine[3]);
                    float g = Float.parseFloat(splitLine[4]);
                    float b = Float.parseFloat(splitLine[5]);

                    if (lat < newMinimum.getLatitude()) {
                        newMinimum.setLatitude(lat);
                    }
                    if (lon < newMinimum.getLongitude()) {
                        newMinimum.setLongitude(lon);
                    }
                    if (hgt < newMinimum.getHeight()) {
                        newMinimum.setHeight(hgt);
                    }
                    if (r < newMinimum.getR()) {
                        newMinimum.setR(r);
                    }
                    if (g < newMinimum.getG()) {
                        newMinimum.setG(g);
                    }
                    if (b < newMinimum.getB()) {
                        newMinimum.setG(b);
                    }

                    if (lat > newMaximum.getLatitude()) {
                        newMaximum.setLatitude(lat);
                    }
                    if (lon > newMaximum.getLongitude()) {
                        newMaximum.setLongitude(lon);
                    }
                    if (hgt > newMaximum.getHeight()) {
                        newMaximum.setHeight(hgt);
                    }
                    if (r > newMaximum.getR()) {
                        newMaximum.setR(r);
                    }
                    if (g > newMaximum.getG()) {
                        newMaximum.setG(g);
                    }
                    if (b > newMaximum.getB()) {
                        newMaximum.setG(b);
                    }

                    tmpList.add(new MapPoint(lat, lon, hgt, r, g, b));

                } catch (NumberFormatException e) {
                    System.out.println(splitLine[10]);
                    // ignore this entry
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        addPoints(tmpList, newMinimum, newMaximum);
    }

    private synchronized void addPoints(List<MapPoint> toAddList, MapPoint newMinimum, MapPoint newMaximum) {
        if (!addedFile) {
            mapPoints.clear();
        }

        List<MapPoint> tmpList = new ArrayList<MapPoint>(mapPoints);
        tmpList.addAll(toAddList);

        mapPoints = tmpList;
        minimumPoint = newMinimum;
        maximumPoint = newMaximum;

        addedFile = true;
    }

    public synchronized List<MapPoint> getPoints() {
        addedFile = false;

        return new ArrayList<MapPoint>(mapPoints);
    }

    public MapPoint getMinimumPoint() {
        return minimumPoint;
    }

    public MapPoint getMaximumPoint() {
        return maximumPoint;
    }

    public synchronized boolean hasAddedFile() {
        return addedFile;
    }

    @Override
    public void run() {
        File dataFile;
        for (int i = 1; i < 18; i++) {
            dataFile = new File("examples/viaAppia/data/Rome-000" + String.format("%03d", i) + ".las.txt");
            if (dataFile != null && dataFile.exists()) {
                System.out.println("Scanning:" + dataFile.getAbsolutePath());
                readFile(dataFile);
                System.out.println("Scan result OK, mapPoints size now :" + mapPoints.size());
            }
        }
    }
}
