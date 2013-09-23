package nl.esciencecenter.esight.examples.viaAppia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class DataReader implements Runnable {
    private boolean addedFile = false;

    private final float OFFSET_X = 296880f;
    private final float OFFSET_Y = 4632360f;
    private final float OFFSET_Z = 110;
    private final float SCALE = 0.01f;

    private final int SKIP_POINTS = 0;

    private FloatBuffer vertices;
    private FloatBuffer colors;

    private final int[] recordCount = new int[] { 1694588, 1645500, 280125, 2599222, 79093, 2546102, 67946, 2805104,
            4310, 181, 70655, 15961079, 708917, 4524, 11611378, 2331688, 17197445, 837308, 5922495, 4924749, 15315,
            88037, 26158, 5726, 3230734, 7942156, 1254, 603, 1089780, 12717516, 18070, 24279, 12856038, 353173, 7,
            9610, 10157273, 2390450, 158, 5304485, 4335435, 17, 5426, 2853649, 6945270, 14, 886, 49, 5355, 700295,
            10905817, 24055, 3820, 924652, 39313, 40736, 8612269, 419450, 525, 2362096, 8246679, 10023482, 12281443,
            2647667, 9049, 8, 679218, 4526626, 99020, 4275357, 24666250, 2109018, 64537, 5591918, 28411, 4690653,
            8251380, 31371, 5753, 3321, 48617, 4324444, 638369, 9111464, 134384, 30, 577, 7753, 24731, 9558585, 720084,
            310, 65, 7135333, 77578281, 47988, 7, 1574, 8225524, 10655

    };

    public DataReader() throws FileNotFoundException {
    }

    private void readFile(File dataFile, int sequenceNumber) {
        int numRecords = // recordCount[sequenceNumber] * 4;
        (int) Math.floor((recordCount[sequenceNumber] / (SKIP_POINTS + 1))) * 4;

        FloatBuffer tmpVertices = FloatBuffer.allocate(numRecords);
        FloatBuffer tmpColors = FloatBuffer.allocate(numRecords);

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));

            String sCurrentLine;
            int count = SKIP_POINTS;

            while ((sCurrentLine = br.readLine()) != null) {
                if (count == 0) {
                    String[] splitLine = sCurrentLine.trim().split("\\s+");

                    try {
                        float lat = (Float.parseFloat(splitLine[0]) - OFFSET_X) * SCALE;
                        float lon = (Float.parseFloat(splitLine[1]) - OFFSET_Y) * SCALE;
                        float hgt = (Float.parseFloat(splitLine[2]) - OFFSET_Z) * SCALE;
                        float r = Float.parseFloat(splitLine[3]) / 255f;
                        float g = Float.parseFloat(splitLine[4]) / 255f;
                        float b = Float.parseFloat(splitLine[5]) / 255f;

                        tmpVertices.put(lat);
                        tmpVertices.put(lon);
                        tmpVertices.put(hgt);
                        tmpVertices.put(1f);

                        tmpColors.put(r);
                        tmpColors.put(g);
                        tmpColors.put(b);
                        tmpColors.put(1f);

                    } catch (NumberFormatException e) {
                        // System.out.println(splitLine[10]);
                        // ignore this entry
                    }
                    count = SKIP_POINTS;
                } else {
                    count--;
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

        tmpVertices.rewind();
        tmpColors.rewind();

        while (!addPoints(tmpVertices, tmpColors)) {
            // keep trying
        }

    }

    private synchronized boolean addPoints(FloatBuffer tmpVertices, FloatBuffer tmpColors) {
        if (!addedFile) {
            vertices = tmpVertices;
            colors = tmpColors;

            addedFile = true;

            return true;
        } else {
            return false;
        }
    }

    public synchronized FloatBuffer[] getBuffers() {
        if (addedFile) {
            System.out.println("got " + vertices.capacity() / 4 + " records to add");
            addedFile = false;

            return new FloatBuffer[] { vertices, colors };
        } else {
            return null;
        }
    }

    @Override
    public void run() {
        int tally = 0;
        File dataFile;
        for (int sequenceNumber = 12; sequenceNumber < 16; sequenceNumber++) {
            dataFile = new File("/media/datahdd1/Via Appia/txt/Rome-000" + String.format("%03d", sequenceNumber)
                    + ".las.txt");
            if (dataFile != null && dataFile.exists()) {
                System.out.println("Scanning:" + dataFile.getAbsolutePath());
                readFile(dataFile, sequenceNumber - 1);

                int newCount = vertices.capacity() / 4;
                tally += newCount;

                System.out
                        .println("Scan result OK, read " + newCount + "points, up to a total of " + tally + " points");
            }
        }
    }
}
