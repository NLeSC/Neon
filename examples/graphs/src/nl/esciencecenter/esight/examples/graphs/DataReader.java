package nl.esciencecenter.esight.examples.graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
    private File dataFile;

    private final List<Integer> landType;
    private int index = 0;

    public DataReader() throws FileNotFoundException {
        File newFile = new File("examples/graphs/data/ISCCP.D2GRID.0.GLOBAL.1983.99.99.9999.GPC");

        if (newFile != null && newFile.exists()) {
            dataFile = newFile;
        } else {
            throw new FileNotFoundException();
        }

        landType = new ArrayList<Integer>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dataFile));

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {

                String[] splitLine = sCurrentLine.trim().split("\\s+");

                try {
                    int number = Integer.parseInt(splitLine[10]);
                    landType.add(number);

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

    public int getNextType() {
        int type = landType.get(index);
        index++;

        if (index == landType.size()) {
            index = 0;
        }
        return type;
    }

}
