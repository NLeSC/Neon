package nl.esciencecenter.esight.io.netcdf;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.esciencecenter.esight.util.Settings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

public class NetCDFUtil {
    private final static Settings settings = Settings.getInstance();
    private final static Logger   logger   = LoggerFactory
                                                   .getLogger(NetCDFUtil.class);

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

    private static String getSequenceNumber(File file) {
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;

        String[] split = fullPath.split("[^0-9]");

        boolean foundOne = false;
        String sequenceNumberString = "";
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            try {
                Integer.parseInt(s);
                if (s.length() >= 4) {
                    sequenceNumberString = s;
                    if (!foundOne) {
                        foundOne = true;
                    } else {
                        System.err
                                .println("ERROR: Filename includes two possible sequence numbers.");
                    }
                }
            } catch (NumberFormatException e) {
                // IGNORE
            }
        }

        return sequenceNumberString;
    }

    public static String getPrefix(File file) {
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;

        String seqNum = getSequenceNumber(file);
        int index = fullPath.lastIndexOf(seqNum);

        return fullPath.substring(0, index);
    }

    public static String getPostfix(File file) {
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;

        String seqNum = getSequenceNumber(file);
        int index = fullPath.lastIndexOf(seqNum) + seqNum.length();

        return fullPath.substring(index);
    }

    public static String getPath(File file) {
        final String path = file.getPath().substring(0,
                file.getPath().length() - file.getName().length());
        return path;
    }

    public static NetcdfFile open(String filename) {
        NetcdfFile ncfile = null;
        try {
            ncfile = NetcdfFile.open(filename);
        } catch (IOException ioe) {
            logger.error("trying to open " + filename, ioe);
        }

        return ncfile;
    }

    public static NetcdfFile open(File file) {
        NetcdfFile ncfile = null;
        try {
            ncfile = NetcdfFile.open(file.getAbsolutePath());
        } catch (IOException ioe) {
            logger.error("trying to open " + file.getAbsolutePath(), ioe);
        }

        return ncfile;
    }

    public static void close(NetcdfFile ncfile) {
        try {
            ncfile.close();
        } catch (IOException ioe) {
            logger.error("trying to close file.", ioe);
        }
    }

    public static int getNumColorMaps() {
        final String[] ls = new File("colormaps").list(new ExtFilter("ncmap"));

        return ls.length;
    }

    public static String[] getColorMaps() {
        final String[] ls = new File("colormaps").list(new ExtFilter("ncmap"));
        final String[] result = new String[ls.length];

        for (int i = 0; i < ls.length; i++) {
            result[i] = ls[i].split("\\.")[0];
        }

        return result;
    }

    public static int getNumFiles(File file) {
        final String path = getPath(file);
        int fileNameLength = file.getName().length();

        final String[] ls = new File(path).list(new ExtFilter(settings
                .getCurrentNetCDFExtension()));

        int result = 0;
        for (String s : ls) {
            if (s.length() == fileNameLength) {
                result++;
            }
        }

        return result;
    }

    public static int getNumFiles(String pathName, String ext) {
        final String[] ls = new File(pathName).list(new ExtFilter(ext));

        return ls.length;
    }

    public static void printInfo(NetcdfFile ncfile) {
        System.out.println(ncfile.getDetailInfo());
    }

    public static Array getData(NetcdfFile ncfile, String varName)
            throws NetCDFNoSuchVariableException {
        Variable v = ncfile.findVariable(varName);
        Array data = null;
        if (null == v)
            throw new NetCDFNoSuchVariableException("no such variable "
                    + varName);
        try {
            data = v.read();
        } catch (IOException ioe) {
            logger.error("trying to read " + varName, ioe);
        }

        return data;
    }

    public static String getUsedDimensionName(NetcdfFile ncfile,
            String... permutations) throws NetCDFNoSuchVariableException {
        List<Dimension> dims = ncfile.getDimensions();

        int i = 0;
        String current = permutations[i];

        boolean success = false;
        while (!success) {
            for (Dimension d : dims) {
                if (d.getName().compareTo(current) == 0) {
                    success = true;
                    break;
                }
            }

            i++;

            if (i > permutations.length - 1 || success) {
                break;
            } else {

                current = permutations[i];
            }
        }
        if (!success) {
            String perms = "";
            for (String s : permutations) {
                perms += s + "; ";
            }

            throw new NetCDFNoSuchVariableException(
                    "Dimension finder: All permutations (" + perms + ") failed");
        }
        return current;
    }

    public static ArrayList<String> getUsedDimensionNamesBySubstring(
            NetcdfFile ncfile, String subString)
            throws NetCDFNoSuchVariableException {
        List<Dimension> dims = ncfile.getDimensions();

        ArrayList<String> result = new ArrayList<String>();
        for (Dimension d : dims) {
            if (d.getName().contains(subString)) {
                result.add(d.getName());
            }
        }

        return result;
    }

    public static ArrayList<String> getVarNames(NetcdfFile ncfile,
            ArrayList<String> latCandidates, ArrayList<String> lonCandidates) {
        ArrayList<String> varNames = new ArrayList<String>();

        List<Variable> variables = ncfile.getVariables();
        for (Variable v : variables) {

            List<Dimension> availableDims = v.getDimensions();
            List<String> availableDimNames = new ArrayList<String>();

            for (Dimension d : availableDims) {
                availableDimNames.add(d.getName());
            }

            boolean successLat = false;
            for (String name : latCandidates) {
                if (availableDimNames.contains(name)) {
                    successLat = true;
                }
            }
            boolean successLon = false;
            for (String name : lonCandidates) {
                if (availableDimNames.contains(name)) {
                    successLon = true;
                }
            }

            if (successLat && successLon) {
                varNames.add(v.getFullName());
            }
        }

        return varNames;
    }

    public static String getFancyVarName(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getDescription();
            }
        }

        return "";
    }

    public static String getFancyVarUnits(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getUnitsString();
            }
        }

        return "";
    }

    public static int[] getDimensions(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getShape();
            }
        }

        return null;
    }

    public static Dimension[] getUsedDimensionsBySubstring(NetcdfFile ncfile,
            String subString) throws NetCDFNoSuchVariableException {
        List<Dimension> dims = ncfile.getDimensions();

        ArrayList<Dimension> result = new ArrayList<Dimension>();
        for (Dimension d : dims) {
            if (d.getName().contains(subString)) {
                result.add(d);
            }
        }

        return result.toArray(new Dimension[0]);
    }

    public static Variable[] getQualifyingVariables(NetcdfFile ncfile,
            Dimension[] latCandidates, Dimension[] lonCandidates) {
        ArrayList<Variable> qualifyingVariables = new ArrayList<Variable>();

        List<Variable> variables = ncfile.getVariables();
        for (Variable v : variables) {
            List<Dimension> availableDims = v.getDimensions();

            boolean successLat = false;
            for (Dimension lat : latCandidates) {
                if (availableDims.contains(lat)) {
                    successLat = true;
                }
            }
            boolean successLon = false;
            for (Dimension lon : lonCandidates) {
                if (availableDims.contains(lon)) {
                    successLon = true;
                }
            }

            if (successLat && successLon) {
                qualifyingVariables.add(v);
            }
        }

        for (Variable v : qualifyingVariables) {
            System.out.println(v.getShortName());
        }
        return qualifyingVariables.toArray(new Variable[0]);
    }

    public static String[] getUsedDimensionNames(NetcdfFile ncfile,
            String... permutations) throws NetCDFNoSuchVariableException {
        List<Dimension> dims = ncfile.getDimensions();

        ArrayList<String> result = new ArrayList<String>();
        for (String current : permutations) {
            for (Dimension d : dims) {
                if (d.getName().compareTo(current) == 0) {
                    result.add(current);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    public static String[] getVarNames(NetcdfFile ncfile,
            String... mandatoryDims) {
        ArrayList<String> varNames = new ArrayList<String>();

        List<Variable> variables = ncfile.getVariables();
        for (Variable v : variables) {
            boolean success = true;

            List<Dimension> availableDims = v.getDimensions();
            List<String> availableDimNames = new ArrayList<String>();

            for (Dimension d : availableDims) {
                availableDimNames.add(d.getName());
            }

            for (String name : mandatoryDims) {
                if (!availableDimNames.contains(name)) {
                    success = false;
                }
            }
            if (success) {
                varNames.add(v.getFullName());
            }
        }
        String[] result = varNames.toArray(new String[0]);

        for (String s : result) {
            System.out.println(s);
        }
        return result;
    }

    public static String[] getVarNames(NetcdfFile ncfile,
            String[] latCandidates, String[] lonCandidates) {
        ArrayList<String> varNames = new ArrayList<String>();

        List<Variable> variables = ncfile.getVariables();
        for (Variable v : variables) {

            List<Dimension> availableDims = v.getDimensions();
            List<String> availableDimNames = new ArrayList<String>();

            for (Dimension d : availableDims) {
                availableDimNames.add(d.getName());
            }

            boolean successLat = false;
            for (String name : latCandidates) {
                if (availableDimNames.contains(name)) {
                    successLat = true;
                }
            }
            boolean successLon = false;
            for (String name : lonCandidates) {
                if (availableDimNames.contains(name)) {
                    successLon = true;
                }
            }

            if (successLat && successLon) {
                varNames.add(v.getFullName());
            }
        }
        String[] result = varNames.toArray(new String[0]);

        for (String s : result) {
            System.out.println(s);
        }
        return result;
    }

    public static Array getDataSubset(NetcdfFile ncfile, String varName,
            String subsections) throws NetCDFNoSuchVariableException {
        Variable v = ncfile.findVariable(varName);
        Array data = null;
        if (null == v)
            throw new NetCDFNoSuchVariableException("no such variable "
                    + varName);
        try {
            data = v.read(subsections);
        } catch (IOException ioe) {
            logger.error("trying to read " + varName, ioe);
        } catch (InvalidRangeException e) {
            logger.error("invalid Range for " + varName, e);
        }

        return data;
    }

    public static int getFrameNumber(File file) {
        String number = getSequenceNumber(file);

        return Integer.parseInt(number);
    }

    public static File getSeqLowestFile(File initialFile) {
        String prefix = getPrefix(initialFile);
        String postfix = getPostfix(initialFile);

        int numberLength = getSequenceNumber(initialFile).length();

        String format = "%0" + numberLength + "d";

        for (int i = 0; i < 100000; i++) {
            String number = String.format(format, i);

            File fileTry = new File(prefix + number + postfix);
            if (fileTry.exists())
                return fileTry;
        }

        return null;
    }

    public static boolean isAcceptableFile(File file) {
        String[] accExts = settings.getAcceptableNetCDFExtensions();
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;
        final String[] ext = fullPath.split("[.]");

        boolean result = false;
        for (int i = 0; i < accExts.length; i++) {
            if (ext[ext.length - 1].compareTo(accExts[i]) != 0) {
                result = true;
            }
        }

        return result;
    }

    public static File getSeqPreviousFile(File initialFile) {
        String prefix = getPrefix(initialFile);
        String postfix = getPostfix(initialFile);

        int initialNumber = Integer.parseInt(getSequenceNumber(initialFile));
        int numberLength = getSequenceNumber(initialFile).length();

        String format = "%0" + numberLength + "d";

        String number = String.format(format, initialNumber - 1);

        File fileTry = new File(prefix + number + postfix);
        if (fileTry.exists())
            return fileTry;

        return null;
    }

    public static File getSeqFile(File initialFile, int value)
            throws IOException {
        String prefix = getPrefix(initialFile);
        String postfix = getPostfix(initialFile);

        int numberLength = getSequenceNumber(initialFile).length();

        String format = "%0" + numberLength + "d";

        String number = String.format(format, value);

        File fileTry = new File(prefix + number + postfix);

        logger.debug("Opening file: " + prefix + number + postfix);

        if (fileTry.exists())
            return fileTry;

        return null;
    }

    public static File getSeqNextFile(File initialFile) {
        String prefix = getPrefix(initialFile);
        String postfix = getPostfix(initialFile);

        int initialNumber = Integer.parseInt(getSequenceNumber(initialFile));
        int numberLength = getSequenceNumber(initialFile).length();

        String format = "%0" + numberLength + "d";

        int attempts = 1;
        String number = String.format(format, (initialNumber + attempts));
        File fileTry = new File(prefix + number + postfix);
        while (!fileTry.exists() && attempts < 10000) {
            attempts++;
            number = String.format(format, (initialNumber + attempts));
            fileTry = new File(prefix + number + postfix);
        }

        if (attempts == 10000) {
            return null;
        } else {
            return fileTry;
        }
    }

}
