package nl.esciencecenter.esight.examples.graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
    public class MapPoint {
        private final float latitude, longitude, height;
        private final int   landType;

        public MapPoint(float latitude, float longitude, float height, int landType) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.height = height;
            this.landType = landType;
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

        public int getLandType() {
            return landType;
        }
    }

    private File                 dataFile;

    private final List<Integer>  landType;
    private final List<MapPoint> mapPoints;
    private int                  index = 0;

    public DataReader() throws FileNotFoundException {
        File newFile = new File("examples/graphs/data/ISCCP.D2GRID.0.GLOBAL.1983.99.99.9999.GPC");

        if (newFile != null && newFile.exists()) {
            dataFile = newFile;
        } else {
            throw new FileNotFoundException();
        }

        landType = new ArrayList<Integer>();
        mapPoints = new ArrayList<MapPoint>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] splitLine = sCurrentLine.trim().split("\\s+");

                try {
                    float lat = Float.parseFloat(splitLine[5]);
                    float lon = Float.parseFloat(splitLine[6]);
                    float hgt = Float.parseFloat(splitLine[9]);
                    int number = Integer.parseInt(splitLine[10]);
                    landType.add(number);
                    mapPoints.add(new MapPoint(lat, lon, hgt, number));

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
    }

    public int getType() {
        int type = landType.get(index);
        return type;
    }

    public MapPoint getMapPoint() {
        MapPoint result = mapPoints.get(index);
        return result;

    }

    public void next() {
        index++;

        if (index == mapPoints.size()) {
            index = 0;
        }
    }

}
