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

public class ColormapInterpreter {
    private final static Logger logger = LoggerFactory
            .getLogger(ColormapInterpreter.class);

    static class ExtFilter implements FilenameFilter {
        private final String ext;

        public ExtFilter(String ext) {
            this.ext = ext;
        }

        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(ext));
        }
    }

    public static class Dimensions {
        public float min, max;

        public Dimensions(float min, float max) {
            this.min = min;
            this.max = max;
        }

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

    private static HashMap<String, ArrayList<Color>> colorMaps;
    private static HashMap<String, Color[][]> legends;

    private final static int LEGEND_WIDTH = 150;
    private final static int LEGEND_HEIGHT = 150;

    static {
        rebuild();
    }

    public static void rebuild() {
        colorMaps = new HashMap<String, ArrayList<Color>>();
        legends = new HashMap<String, Color[][]>();

        try {
            String[] colorMapFileNames = getColorMaps();
            for (String fileName : colorMapFileNames) {
                ArrayList<Color> colorMap = new ArrayList<Color>();

                BufferedReader in = new BufferedReader(new FileReader(
                        "colormaps/" + fileName + ".ncmap"));
                String str;

                while ((str = in.readLine()) != null) {
                    String[] numbers = str.split(" ");
                    colorMap.add(new Color(Integer.parseInt(numbers[0]) / 255f,
                            Integer.parseInt(numbers[1]) / 255f, Integer
                                    .parseInt(numbers[2]) / 255f, 1f));
                }

                in.close();

                colorMaps.put(fileName, colorMap);
                legends.put(fileName,
                        makeLegendImage(LEGEND_WIDTH, LEGEND_HEIGHT, colorMap));
                System.out.println("Colormap " + fileName
                        + " registered for use.");
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static String[] getColorMaps() {
        final String[] ls = new File("colormaps").list(new ExtFilter("ncmap"));
        final String[] result = new String[ls.length];

        for (int i = 0; i < ls.length; i++) {
            result[i] = ls[i].split("\\.")[0];
        }

        return result;
    }

    public static String[] getColormapNames() {
        String[] names = new String[legends.size()];
        int i = 0;
        for (Entry<String, Color[][]> entry : legends.entrySet()) {
            names[i] = entry.getKey();
            i++;
        }

        return names;
    }

    public synchronized static Color getLogColor(String colorMapName,
            Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: "
                    + colorMapName);
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

    public synchronized static Color getColor(String colorMapName,
            Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: "
                    + colorMapName);
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

    public synchronized static java.awt.Color getSwingColor(
            String colorMapName, Dimensions dim, float var) {
        if (!colorMaps.containsKey(colorMapName)) {
            System.err.println("Unregistered color map requested: "
                    + colorMapName);
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

    public static JComboBox getLegendJComboBox(Dimension preferredDimensions) {
        int width = (int) (preferredDimensions.width * .8), height = (int) (preferredDimensions.height * .8);

        ImageIcon[] images = new ImageIcon[legends.size()];

        int i = 0;
        Integer[] intArray = new Integer[legends.size()];
        String[] names = new String[legends.size()];
        for (Entry<String, Color[][]> entry : legends.entrySet()) {
            String name = entry.getKey();
            Color[][] legendImageBuffer = makeLegendImage(width, height,
                    colorMaps.get(name));

            intArray[i] = new Integer(i);
            BufferedImage legend = new BufferedImage(width, height,
                    BufferedImage.TYPE_3BYTE_BGR);
            WritableRaster raster = legend.getRaster();

            for (int col = 0; col < width; col++) {
                for (int row = 0; row < height; row++) {
                    raster.setSample(col, row, 0,
                            legendImageBuffer[col][row].blue * 255);
                    raster.setSample(col, row, 1,
                            legendImageBuffer[col][row].green * 255);
                    raster.setSample(col, row, 2,
                            legendImageBuffer[col][row].red * 255);
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
        ImageComboBoxRenderer renderer = new ImageComboBoxRenderer(names,
                images);
        renderer.setPreferredSize(preferredDimensions);
        legendList.setRenderer(renderer);
        legendList.setMaximumRowCount(10);

        return legendList;
    }

    private static Color[][] makeLegendImage(int width, int height,
            ArrayList<Color> colorMap) {
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

    private static float getInterpolatedColor(float high, float low,
            float colorInterval) {
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
