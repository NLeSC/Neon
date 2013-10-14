package nl.esciencecenter.neon.examples.jurriaan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
    public class MapPoint {
        private final float x, y, z;

        public MapPoint(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }

    private File dataFile;

    private final List<MapPoint> mapPoints;
    private int index = 0;

    public DataReader() throws FileNotFoundException {
        File newFile = new File("examples/jurriaan/data/evalresults.txt");

        if (newFile != null && newFile.exists()) {
            dataFile = newFile;
        } else {
            throw new FileNotFoundException();
        }

        mapPoints = new ArrayList<MapPoint>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] splitLine = sCurrentLine.trim().split("\\s+");

                try {
                    float xCoord = Float.parseFloat(splitLine[1]);
                    float yCoord = Float.parseFloat(splitLine[2]);
                    float zCoord = Float.parseFloat(splitLine[3]);
                    mapPoints.add(new MapPoint(xCoord, yCoord, zCoord));

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

    public MapPoint getMapPoint() {
        if (index == mapPoints.size()) {
            return null;
        }
        MapPoint result = mapPoints.get(index);
        return result;
    }

    public boolean next() {
        if (index < mapPoints.size() - 1) {
            index++;
            System.out.println(index);
            return true;
        }
        return false;
    }

}
