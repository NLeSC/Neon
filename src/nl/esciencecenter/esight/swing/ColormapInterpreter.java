package nl.esciencecenter.esight.swing;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Utility class for the creation and interpretation of colormaps useable in
 * both OpenGL and Java Swing. Reads the "colormaps/" directory in the classpath
 * and tries to statically build colormaps out of each file it finds there with
 * the .ncmap extension.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class ColormapInterpreter {
    private final static Logger logger = LoggerFactory.getLogger(ColormapInterpreter.class);

    /**
     * Extension filter for the filtering of filenames in directory structures.
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     * 
     */
    static class ExtFilter implements FilenameFilter {
        private final String ext;

        /**
         * Basic constructor for ExtFilter.
         * 
         * @param ext
         *            The extension to filter for.
         */
        public ExtFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }

    /**
     * Helper class used to communicate the minimum and maximum values of a
     * dataset.
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     * 
     */
    public static class Dimensions {
        public float min, max;

        /**
         * Constructor for Dimensions.
         * 
         * @param min
         *            The minimum value in the dataset (corresponding with the
         *            bottom end of the colormap).
         * @param max
         *            The maximum value in the dataset (corresponding with the
         *            top end of the colormap).
         */
        public Dimensions(float min, float max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Getter for the difference between max and min.
         * 
         * @return The range of the dataset
         */
        public float getDiff() {
            return max - min;
        }

        @Override
        public int hashCode() {
            return (int) (min + max);
        }

        @Override
        public boolean equals(Object thatObject) {
            if (this == thatObject)
                return true;
            if (!(thatObject instanceof Dimensions))
                return false;

            // cast to native object is now safe
            Dimensions that = (Dimensions) thatObject;

            // now a proper field-by-field evaluation can be made
            return (min == that.min && max == that.max);
        }
    }

    /**
     * helper class for the definition of a color.
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     * 
     */
    public static class Color {
        public float red, green, blue, alpha;
        public static final Color WHITE = new Color(1f, 1f, 1f, 1f);
        public static final Color BLACK = new Color(0f, 0f, 0f, 1f);

        public Color() {
            this.red = 0f;
            this.green = 0f;
            this.blue = 0f;
            this.alpha = 0f;
        }

        public Color(float r, float g, float b, float a) {
            this.red = r;
            this.green = g;
            this.blue = b;
            this.alpha = a;
        }
    }

    /** Storage for the statically read colormaps. */
    private static HashMap<String, ArrayList<Color>> colorMaps;
    /** Storage for the statically built legend images. */
    private static HashMap<String, Color[][]> legends;

    private final static int LEGEND_WIDTH = 150;
    private final static int LEGEND_HEIGHT = 150;

    static {
        rebuild();
    }

    /**
     * Rebuilds (and re-reads) the storage of colormaps. Outputs succesfully
     * read colormap names to the command line.
     */
    public static void rebuild() {
        colorMaps = new HashMap<String, ArrayList<Color>>();
        legends = new HashMap<String, Color[][]>();

        try {
            String[] colorMapFileNames = getColorMaps();
            for (String fileName : colorMapFileNames) {
                ArrayList<Color> colorMap = new ArrayList<Color>();

                BufferedReader in = new BufferedReader(new FileReader("colormaps/" + fileName + ".ncmap"));
                String str;

                while ((str = in.readLine()) != null) {
                    String[] numbers = str.split(" ");
                    colorMap.add(new Color(Integer.parseInt(numbers[0]) / 255f, Integer.parseInt(numbers[1]) / 255f,
                            Integer.parseInt(numbers[2]) / 255f, 1f));
                }

                in.close();

                colorMaps.put(fileName, colorMap);
                legends.put(fileName, makeLegendImage(LEGEND_WIDTH, LEGEND_HEIGHT, colorMap));
                System.out.println("Colormap " + fileName + " registered for use.");
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Getter for the list of colormap names in the directory. Used to load
     * these maps.
     * 
     * @return the array containing all of the colormap names in the directory.
     *         These are unchecked.
     */
    private static String[] getColorMaps() {
        final String[] ls = new File("colormaps").list(new ExtFilter("ncmap"));
        final String[] result = new String[ls.length];

        for (int i = 0; i < ls.length; i++) {
            result[i] = ls[i].split("\\.")[0];
        }

        return result;
    }

    /**
     * Getter for the list of colormap names in the directory. Used to load
     * these maps.
     * 
     * @return the array containing all of the currently available colormap
     *         names.
     */
    public static String[] getColormapNames() {
        String[] names = new String[legends.size()];
        int i = 0;
        for (Entry<String, Color[][]> entry : legends.entrySet()) {
            names[i] = entry.getKey();
            i++;
        }

        return names;
    }

    /**
     * Experimental function to return a color from the colormap with
     * logatithmic scaling between endpoints. Use at your own risk.
     * 
     * @param colorMapName
     *            The name of the colormap to use
     * @param dim
     *            The dimensions in the dataset to pick a color value between.
     * @param var
     *            The value to use.
     * @return The color.
     */
    public synchronized static Color getLogColor(String colorMapName, Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: " + colorMapName);
            colorMaps.get("default");
        }

        ArrayList<Color> colorMap = colorMaps.get(colorMapName);

        int cmEntries = colorMap.size();

        Color color = null;

        float result = (var - dim.min) / dim.getDiff();
        int resultIndex = (int) Math.ceil(result * 256);
        if (resultIndex == 256)
            resultIndex--;

        float rawIndex = (float) ((Math.log(resultIndex) / Math.log(256)) * cmEntries);
        float alpha;

        if (var < -1E33) {
            color = Color.BLACK;
        } else if (var < dim.min) {
            if (result > -1f) {
                alpha = 1 - result;
            } else {
                alpha = 0f;
            }
            color = colorMap.get(0);
            color.alpha = 1f;
        } else if (var > dim.max) {
            if (result < 2f) {
                alpha = 1f - (result - 1f);
            } else {
                alpha = 0f;
            }
            color = colorMap.get(cmEntries - 1);
            color.alpha = 1f;
        } else {
            float red = 0;
            float green = 0;
            float blue = 0;
            alpha = 1f;

            int iLow = (int) Math.floor(rawIndex);
            int iHigh = (int) Math.ceil(rawIndex);

            Color cLow;
            if (iLow == cmEntries) {
                cLow = colorMap.get(cmEntries - 1);
            } else if (iLow < 0) {
                cLow = colorMap.get(0);
            } else {
                cLow = colorMap.get(iLow);
            }

            Color cHigh;
            if (iHigh == cmEntries) {
                cHigh = colorMap.get(cmEntries - 1);
            } else if (iHigh < 0) {
                cHigh = colorMap.get(0);
            } else {
                cHigh = colorMap.get(iHigh);
            }

            float colorInterval = rawIndex - iLow;

            red = getInterpolatedColor(cHigh.red, cLow.red, colorInterval);
            green = getInterpolatedColor(cHigh.green, cLow.green, colorInterval);
            blue = getInterpolatedColor(cHigh.blue, cLow.blue, colorInterval);

            color = new Color(red, green, blue, alpha);
        }

        return color;
    }

    /**
     * Function to return a color from the colormap with linear scaling between
     * endpoints.
     * 
     * @param colorMapName
     *            The name of the colormap to use
     * @param dim
     *            The dimensions in the dataset to pick a color value between.
     * @param var
     *            The value to use.
     * @return The color.
     */
    public synchronized static Color getColor(String colorMapName, Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: " + colorMapName);
            colorMaps.get("default");
        }

        ArrayList<Color> colorMap = colorMaps.get(colorMapName);

        int cmEntries = colorMap.size();

        Color color = null;

        float result = (var - dim.min) / dim.getDiff();
        float rawIndex = result * cmEntries;
        float alpha;

        if (var < -1E33) {
            color = Color.BLACK;
        } else if (var < dim.min) {
            if (result > -1f) {
                alpha = 1 - result;
            } else {
                alpha = 0f;
            }
            color = colorMap.get(0);
            color.alpha = alpha;
        } else if (var > dim.max) {
            if (result < 2f) {
                alpha = 1f - (result - 1f);
            } else {
                alpha = 0f;
            }
            color = colorMap.get(cmEntries - 1);
            color.alpha = alpha;
        } else {
            float red = 0;
            float green = 0;
            float blue = 0;

            int iLow = (int) Math.floor(rawIndex);
            int iHigh = (int) Math.ceil(rawIndex);

            Color cLow;
            if (iLow == cmEntries) {
                cLow = colorMap.get(cmEntries - 1);
            } else if (iLow < 0) {
                cLow = colorMap.get(0);
            } else {
                cLow = colorMap.get(iLow);
            }

            Color cHigh;
            if (iHigh == cmEntries) {
                cHigh = colorMap.get(cmEntries - 1);
            } else if (iHigh < 0) {
                cHigh = colorMap.get(0);
            } else {
                cHigh = colorMap.get(iHigh);
            }

            float colorInterval = rawIndex - iLow;

            red = getInterpolatedColor(cHigh.red, cLow.red, colorInterval);
            green = getInterpolatedColor(cHigh.green, cLow.green, colorInterval);
            blue = getInterpolatedColor(cHigh.blue, cLow.blue, colorInterval);

            color = new Color(red, green, blue, 1f);
        }

        return color;
    }

    /**
     * Function to return a Swing Color from the colormap with linear scaling
     * between endpoints.
     * 
     * @param colorMapName
     *            The name of the colormap to use
     * @param dim
     *            The dimensions in the dataset to pick a color value between.
     * @param var
     *            The value to use.
     * @return The color.
     */
    public synchronized static java.awt.Color getSwingColor(String colorMapName, Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: " + colorMapName);
            colorMaps.get("default");
        }

        ArrayList<Color> colorMap = colorMaps.get(colorMapName);

        int cmEntries = colorMap.size();

        java.awt.Color color = null;

        float result = (var - dim.min) / dim.getDiff();
        float rawIndex = result * cmEntries;

        float red = 0;
        float green = 0;
        float blue = 0;

        int iLow = (int) Math.floor(rawIndex);
        int iHigh = (int) Math.ceil(rawIndex);

        Color cLow;
        if (iLow == cmEntries) {
            cLow = colorMap.get(cmEntries - 1);
        } else if (iLow < 0) {
            cLow = colorMap.get(0);
        } else {
            cLow = colorMap.get(iLow);
        }

        Color cHigh;
        if (iHigh == cmEntries) {
            cHigh = colorMap.get(cmEntries - 1);
        } else if (iHigh < 0) {
            cHigh = colorMap.get(0);
        } else {
            cHigh = colorMap.get(iHigh);
        }

        float colorInterval = rawIndex - iLow;

        red = getInterpolatedColor(cHigh.red, cLow.red, colorInterval);
        green = getInterpolatedColor(cHigh.green, cLow.green, colorInterval);
        blue = getInterpolatedColor(cHigh.blue, cLow.blue, colorInterval);

        color = new java.awt.Color(red, green, blue);

        return color;
    }

    /**
     * Function that returns a combobox with all of the legends of the entire
     * list of colormaps for selection.
     * 
     * @param preferredDimensions
     *            The dimensions of the combobox to be returned.
     * @return The combobox.
     */
    public static JComboBox getLegendJComboBox(Dimension preferredDimensions) {
        int width = (int) (preferredDimensions.width * .8), height = (int) (preferredDimensions.height * .8);

        ImageIcon[] images = new ImageIcon[legends.size()];

        int i = 0;
        Integer[] intArray = new Integer[legends.size()];
        String[] names = new String[legends.size()];
        for (Entry<String, Color[][]> entry : legends.entrySet()) {
            String name = entry.getKey();
            Color[][] legendImageBuffer = makeLegendImage(width, height, colorMaps.get(name));

            intArray[i] = new Integer(i);
            BufferedImage legend = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            WritableRaster raster = legend.getRaster();

            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    raster.setSample(col, row, 0, legendImageBuffer[col][row].blue * 255);
                    raster.setSample(col, row, 1, legendImageBuffer[col][row].green * 255);
                    raster.setSample(col, row, 2, legendImageBuffer[col][row].red * 255);
                }
            }

            images[i] = new ImageIcon(legend);
            if (images[i] != null) {
                images[i].setDescription(name);
            }

            names[i] = name;

            i++;
        }

        JComboBox legendList = new JComboBox(intArray);
        ImageComboBoxRenderer renderer = new ImageComboBoxRenderer(names, images);
        renderer.setPreferredSize(preferredDimensions);
        legendList.setRenderer(renderer);
        legendList.setMaximumRowCount(10);

        return legendList;
    }

    /**
     * Function that returns an 'image' of the entire colormap.
     * 
     * @param preferredDimensions
     *            The dimensions of the image to be returned.
     * @return The image, in a Color[][].
     */
    private static Color[][] makeLegendImage(int width, int height, ArrayList<Color> colorMap) {
        Color[][] outBuf = new Color[width][height];

        for (int col = 0; col < width; col++) {
            float index = col / (float) width;

            int cmEntries = colorMap.size();
            int rawIndex = (int) (index * cmEntries);

            float red = 0;
            float green = 0;
            float blue = 0;

            int iLow = (int) Math.floor(rawIndex);
            int iHigh = (int) Math.ceil(rawIndex);

            Color cLow;
            if (iLow == cmEntries) {
                cLow = colorMap.get(cmEntries - 1);
            } else if (iLow < 0) {
                cLow = colorMap.get(0);
            } else {
                cLow = colorMap.get(iLow);
            }

            Color cHigh;
            if (iHigh == cmEntries) {
                cHigh = colorMap.get(cmEntries - 1);
            } else if (iHigh < 0) {
                cHigh = colorMap.get(0);
            } else {
                cHigh = colorMap.get(iHigh);
            }

            float colorInterval = index - iLow;

            red = getInterpolatedColor(cHigh.red, cLow.red, colorInterval);
            green = getInterpolatedColor(cHigh.green, cLow.green, colorInterval);
            blue = getInterpolatedColor(cHigh.blue, cLow.blue, colorInterval);

            for (int row = 0; row < height; row++) {
                outBuf[col][row] = new Color(blue, green, red, 1f);
            }
        }

        return outBuf;
    }

    /**
     * helper function to interpolate linearly between two single colors.
     * 
     * @param high
     *            The highpoint color
     * @param low
     *            The lowpoint color
     * @param colorInterval
     *            The percentage of the way between low and high.
     * @return The interpolated color.
     */
    private static float getInterpolatedColor(float high, float low, float colorInterval) {
        float result = 0f;

        if (low > high) {
            float temp = high;
            high = low;
            low = temp;

            result = low + (colorInterval * (high - low));
        } else if (low == high) {
            result = low;
        } else {
            result = low + (colorInterval * (high - low));
        }

        return result;
    }

    /**
     * Getter for the index number of a specific colormap name (used by swing).
     * 
     * @param colorMap
     *            The name of the colormap selected
     * @return The index number.
     */
    public static int getIndexOfColormap(String colorMap) {
        int i = 0;
        for (Entry<String, Color[][]> entry : legends.entrySet()) {
            String name = entry.getKey();

            if (name.compareTo(colorMap) == 0) {
                return i;
            }
            i++;
        }

        return -1;
    }
}
