package nl.esciencecenter.esight.io.netcdf;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/* Copyright [2013] [Netherlands eScience Center]
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
 * Stand-alone class that facilitates reading from NetCDF files and getting some
 * information from collections of such files.
 * 
 * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
 * 
 */
public class NetCDFUtil {
    private final static Logger logger = LoggerFactory
            .getLogger(NetCDFUtil.class);

    /**
     * Extension filter for files.
     * 
     * @author Maarten van Meersbergen <m.van.meersbergen@esciencecenter.nl>
     * 
     */
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

    /**
     * Static function to get a sequence number from a filename in a collection
     * of netcdf files. This currently assumes a file name format with a
     * sequence number of no less than 4 characters wide, and only one such
     * number per file name. example filename:
     * t.t0.1_42l_nccs01.007502.interp900x602.nc, where 007502 is the sequence
     * number.
     * 
     * @param file
     *            A file in the sequence of which the sequence number is
     *            requested.
     * @return The sequence number of the file.
     */
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

    /**
     * Static function to get the part of a filename that comes before the
     * sequence number.
     * 
     * @param file
     *            A file in the sequence for which the prefix is requested.
     * @return The part of the filename that comes before the sequence number.
     */
    public static String getPrefix(File file) {
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;

        String seqNum = getSequenceNumber(file);
        int index = fullPath.lastIndexOf(seqNum);

        return fullPath.substring(0, index);
    }

    /**
     * Static function to get the part of a filename that comes after the
     * sequence number.
     * 
     * @param file
     *            A file in the sequence for which the postfix is requested.
     * @return The part of the filename that comes after the sequence number.
     */
    public static String getPostfix(File file) {
        final String path = getPath(file);
        final String name = file.getName();
        final String fullPath = path + name;

        String seqNum = getSequenceNumber(file);
        int index = fullPath.lastIndexOf(seqNum) + seqNum.length();

        return fullPath.substring(index);
    }

    /**
     * Static function to get the path of a file.
     * 
     * @param file
     *            The file for which the path is requested.
     * @return The path of the file.
     */
    public static String getPath(File file) {
        final String path = file.getPath().substring(0,
                file.getPath().length() - file.getName().length());
        return path;
    }

    /**
     * Static function to open a file with the netcdf library. Will generate a
     * log error when this fails.
     * 
     * @param filename
     *            The file's full name.
     * @return The (now open) NetcdfFile.
     */
    public static NetcdfFile open(String filename) {
        NetcdfFile ncfile = null;
        try {
            ncfile = NetcdfFile.open(filename);
        } catch (IOException ioe) {
            logger.error("trying to open " + filename, ioe);
        }

        return ncfile;
    }

    /**
     * Static function to open a file with the netcdf library. Will generate a
     * log error when this fails.
     * 
     * @param filename
     *            The file to open.
     * @return The (now open) NetcdfFile.
     */
    public static NetcdfFile open(File file) {
        NetcdfFile ncfile = null;
        try {
            ncfile = NetcdfFile.open(file.getAbsolutePath());
        } catch (IOException ioe) {
            logger.error("trying to open " + file.getAbsolutePath(), ioe);
        }

        return ncfile;
    }

    /**
     * Static function to close a NetcdfFile. Will generate a log error when
     * this fails.
     * 
     * @param ncfile
     *            The file to close.
     */
    public static void close(NetcdfFile ncfile) {
        try {
            ncfile.close();
        } catch (IOException ioe) {
            logger.error("trying to close file.", ioe);
        }
    }

    /**
     * Static function to count the number of files matching a certain
     * extension.
     * 
     * @param file
     *            An example file in the directory to search in.
     * @param ext
     *            The extension to filer on.
     * @return The number of files in the directory of file that match the
     *         extension ext.
     */
    public static int getNumFiles(File file, String ext) {
        final String path = getPath(file);
        int fileNameLength = file.getName().length();

        final String[] ls = new File(path).list(new ExtFilter(ext));

        int result = 0;
        for (String s : ls) {
            if (s.length() == fileNameLength) {
                result++;
            }
        }

        return result;
    }

    /**
     * Static function to count the number of files matching a certain
     * extension.
     * 
     * @param pathName
     *            The pathname of the directory to search in.
     * @param ext
     *            The extension to filer on.
     * @return The number of files in the directory of file that match the
     *         extension ext.
     */
    public static int getNumFiles(String pathName, String ext) {
        final String[] ls = new File(pathName).list(new ExtFilter(ext));

        return ls.length;
    }

    /**
     * Debug method that prints some info about a NetcdfFile.
     * 
     * @param ncfile
     *            the file to print info on.
     */
    public static void printInfo(NetcdfFile ncfile) {
        System.out.println(ncfile.getDetailInfo());
    }

    /**
     * Static function to get specific data from a netcdf dataset file.
     * Generates a logfile error if there was an IOException during the read.
     * 
     * @param ncfile
     *            The file to get data from.
     * @param varName
     *            The Netcdf variable to read from the data file.
     * @return The Netcdf Array containing the requested data.
     * @throws NetCDFNoSuchVariableException
     *             if the variable was not found.
     */
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

    /**
     * Experimental function to retrieve a used dimension name out of a list of
     * possible permutations.
     * 
     * @param ncfile
     *            The file to check for used dimensions.
     * @param permutations
     *            The list of name permutations to check.
     * @return The actually used name out of the list.
     * @throws NetCDFNoSuchVariableException
     *             If all permutations ended up not being used.
     */
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

    /**
     * Experimental method to extract the used dimension names from a NetcdfFile
     * by a substring in that dimension's name.
     * 
     * @param ncfile
     *            The NetcdfFile to check.
     * @param subString
     *            The substring to check for.
     * @return The used dimension names matching the substring.
     * @throws NetCDFNoSuchVariableException
     *             If no dimension name was found with the given substring.
     */
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

    /**
     * Experimental method to extract the used variable names from a NetcdfFile
     * by candidate dimension names.
     * 
     * @param ncfile
     *            The file to extract the variables from.
     * @param latCandidates
     *            The list of candidates for the first dimension.
     * @param lonCandidates
     *            The list of candidates for the second dimension.
     * @return The list of variable names matching at least one of both of the
     *         first and last dimension names given.
     */
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

    /**
     * Static method to extract the full name of a Netcdf variable.
     * 
     * @param ncfile
     *            The NetcdfFile to extract the information from.
     * @param varName
     *            The shorthand variable name.
     * 
     * @return The full variable name.
     */
    public static String getFancyVarName(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getDescription();
            }
        }

        return "";
    }

    /**
     * Static method to extract the full name of a variable's units.
     * 
     * @param ncfile
     *            The NetcdfFile to extract the information from.
     * @param varName
     *            The shorthand variable name.
     * 
     * @return The full variable's units.
     */
    public static String getFancyVarUnits(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getUnitsString();
            }
        }

        return "";
    }

    /**
     * Static method to extract the shape of a variable's dimensions.
     * 
     * @param ncfile
     *            The NetcdfFile to extract the information from.
     * @param varName
     *            The shorthand variable name.
     * 
     * @return The full variable's dimensions.
     */
    public static int[] getDimensions(NetcdfFile ncfile, String varName) {
        List<Variable> vars = ncfile.getVariables();

        for (Variable v : vars) {
            if (v.getFullName().compareTo(varName) == 0) {
                return v.getShape();
            }
        }

        return null;
    }

    /**
     * Experimental method to extract the used dimensions from a NetcdfFile by a
     * substring in that dimension's name.
     * 
     * @param ncfile
     *            The NetcdfFile to check.
     * @param subString
     *            The substring to check for.
     * @return The used dimensions matching the substring.
     * @throws NetCDFNoSuchVariableException
     *             If no dimension name was found with the given substring.
     */
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

    /**
     * Experimental method to extract the used variable names from a NetcdfFile
     * by candidate dimensions.
     * 
     * @param ncfile
     *            The file to extract the variables from.
     * @param latCandidates
     *            The list of candidates for the first dimension.
     * @param lonCandidates
     *            The list of candidates for the second dimension.
     * @return The list of variable names matching at least one of both of the
     *         first and last dimensions given.
     */
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

        // for (Variable v : qualifyingVariables) {
        // System.out.println(v.getShortName());
        // }
        return qualifyingVariables.toArray(new Variable[0]);
    }

    /**
     * Experimental method to extract the used dimensions from a NetcdfFile
     * based on permutations of dimension names.
     * 
     * @param ncfile
     *            The NetcdfFile to check.
     * @param permutations
     *            The permutations to check for.
     * @return The used dimensions matching the permutations.
     * @throws NetCDFNoSuchVariableException
     *             If no dimension name was found among the permutations.
     */
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

    /**
     * Experimental method to extract the used variable names from a NetcdfFile
     * by mandatory dimension names.
     * 
     * @param ncfile
     *            The file to extract the variables from.
     * @param mandatoryDims
     *            The list of mandatory dimension names for the variables to
     *            have.
     * @return The list of variable names matching all mandatory dimensions.
     */
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

        // for (String s : result) {
        // System.out.println(s);
        // }
        return result;
    }

    /**
     * Experimental method to extract the used variable names from a NetcdfFile
     * by candidate dimension names.
     * 
     * @param ncfile
     *            The file to extract the variables from.
     * @param latCandidates
     *            The list of candidates for the first dimension.
     * @param lonCandidates
     *            The list of candidates for the second dimension.
     * @return The array of variable names matching at least one of both of the
     *         first and last dimension names given.
     */
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

        // for (String s : result) {
        // System.out.println(s);
        // }
        return result;
    }

    /**
     * Experimental method to get a subset of the data, denoted by the variable
     * name and a subsection according to the Netcdf format for specifying these
     * subsections.
     * 
     * @param ncfile
     *            The NetcdfFile to extract the data from.
     * @param varName
     *            The name of the variable to extract.
     * @param subsections
     *            The netcdf format string for specification of subsections.
     * @return The subsection of data.
     * @throws NetCDFNoSuchVariableException
     */
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

    /**
     * Get the frame (sequence) number from a file.
     * 
     * @param file
     *            The file to check for the presence of a sequence number.
     * @return The sequence number in integer format.
     */
    public static int getFrameNumber(File file) {
        String number = getSequenceNumber(file);

        return Integer.parseInt(number);
    }

    /**
     * Get the lowest file in a sequence of files.
     * 
     * @param initialFile
     *            Any file in the sequence.
     * @return The file that is the lowest in the sequence of files.
     */
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

    /**
     * Check whether the file's extension is acceptable.
     * 
     * @param file
     *            The file to check.
     * @param accExts
     *            The list of acceptable extensions.
     * @return True if the file's extension is present in the list of acceptable
     *         extensions.
     */
    public static boolean isAcceptableFile(File file, String[] accExts) {
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

    /**
     * Get the file that is the previous one in a sequence of files.
     * 
     * @param initialFile
     *            The file to get the predecessor of.
     * @return The file that is the predecessor of the given file in the
     *         sequence.
     */
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

    /**
     * Getter for a file in a sequence of files, that corresponds to a certain
     * sequence number.
     * 
     * @param initialFile
     *            Any file in the sequence.
     * @param value
     *            The sequence number of the file to get.
     * @return The file corresponding to the sequence number given, that is in
     *         the sequence denoted by the initial file given.
     * @throws IOException
     *             If there was no file in the sequence with the given sequence
     *             number.
     */
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

    /**
     * Getter for the next file in the sequence denoted by the file given.
     * 
     * @param initialFile
     *            Any file in the sequence.
     * @return The next file in the sequence.
     */
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
