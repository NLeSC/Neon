/* $Id: TypedProperties.java 13240 2011-04-11 15:18:31Z ndrost $ */

package nl.esciencecenter.neon.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Copyright 2013 Netherlands eScience Center
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
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
 * Utility to extract and check typed properties.
 * 
 * @author Niels Drost <n.drost@esciencecenter.nl>
 */
public class TypedProperties extends Properties {
    private final static Logger logger = LoggerFactory.getLogger(TypedProperties.class);

    private static final long serialVersionUID = 1L;

    /** Constructs an empty typed properties object. */
    public TypedProperties() {
        super();
    }

    /**
     * Constructs a typed properties object with the specified defaults.
     * 
     * @param defaults
     *            the defaults.
     */
    public TypedProperties(Properties defaults) {
        super(defaults);
    }

    /**
     * Adds the specified properties to the current ones.
     * 
     * @param properties
     *            the properties to add.
     */
    public void addProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = properties.getProperty(key);
            setProperty(key, value);
        }
    }

    /**
     * Adds the specified properties.
     * 
     * @param properties
     *            properties to add.
     */
    public void putAll(Properties properties) {
        addProperties(properties);
    }

    /**
     * Tries to load properties from a properties file on the classpath.
     */
    public void loadFromClassPath(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceName);

        if (inputStream != null) {
            try {
                load(inputStream);
                logger.debug("Opened properties file: " + resourceName);
            } catch (Exception e) {
                // IGNORE
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    // IGNORE
                }
            }
        }
    }

    /**
     * Expands all existing environment variables of the form ${...} to their
     * value in the value parts of all key,value pairs.
     */
    public void expandSystemVariables() {
        Set<Object> keys = keySet();
        // everything starting with '${' followed by one or more
        // [A-Z][a-z]_[0-9] and ending with '}'
        Pattern pattern = Pattern.compile("\\$\\{\\w+?\\}");
        for (Object key : keys) {
            Matcher matcher = pattern.matcher((String) get(key));
            while (matcher.find()) {
                // this will be something of the form "${...}"
                String fullSystemVar = matcher.group();
                // remove the prefix '${' and the postfix '}'
                String strippedSystemVar = fullSystemVar.substring(2, fullSystemVar.length() - 1);
                if (System.getenv(strippedSystemVar) != null) {
                    put(key, ((String) get(key)).replace(fullSystemVar, System.getenv(strippedSystemVar)));
                    // update the matcher
                    matcher = pattern.matcher((String) get(key));
                }
            }
        }
    }

    /**
     * Tries to load properties from a file. Does not throw any exceptions if
     * unsuccessful.
     * 
     * @param fileName
     *            name of file to load from.
     */
    public void loadFromFile(String fileName) {
        if (fileName == null) {
            return;
        }

        try {
            FileInputStream inputStream = new FileInputStream(fileName);

            try {
                load(inputStream);
                logger.debug("Loading from properties file: " + fileName);
            } catch (IOException e) {
                // IGNORE
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                // IGNORE
            }

        } catch (FileNotFoundException e) {
            // IGNORE
        }
    }

    /**
     * Tries to load properties from a file, which is located relative to the
     * users home directory. Does not throw any exceptions if unsuccessful.
     * 
     * @param fileName
     *            name of file to load from.
     */
    public void loadFromHomeFile(String fileName) {
        loadFromFile(System.getProperty("user.home") + File.separator + fileName);
    }

    /**
     * Returns true if property <code>name</code> is defined and has a value
     * that is conventionally associated with 'true' (as in Ant): any of 1, on,
     * true, yes, or nothing.
     * 
     * @return true if property is defined and set
     * @param name
     *            property name
     */
    public boolean getBooleanProperty(String name) {
        return getBooleanProperty(name, false);
    }

    /**
     * Returns true if property <code>name</code> has a value that is
     * conventionally associated with 'true' (as in Ant): any of 1, on, true,
     * yes, or nothing. If the property is not defined, return the specified
     * default value.
     * 
     * @return true if property is defined and set
     * @param key
     *            property name
     * @param defaultValue
     *            the value that is returned if the property is absent
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);

        if (value != null) {
            logger.debug("Boolean property loaded: " + key + "->" + value);
            return value.equals("1") || value.equals("on") || value.equals("") || value.equals("true")
                    || value.equals("yes");
        }

        logger.debug("Boolean property default: " + key + "->" + defaultValue);

        return defaultValue;
    }

    /**
     * Returns the integer value of property.
     * 
     * @return the integer value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not an integer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Integer property loaded: " + key + "->" + value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Integer expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the integer value of property.
     * 
     * @return the integer value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an integer
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Integer property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Integer property loaded: " + key + "->" + value);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Integer expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the long value of property.
     * 
     * @return the long value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not an long
     */
    public long getLongProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Long property loaded: " + key + "->" + value);

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Long expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the long value of property.
     * 
     * @return the long value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an Long
     */
    public long getLongProperty(String key, long defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Long property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Long property loaded: " + key + "->" + value);

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Long expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the short value of property.
     * 
     * @return the short value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not an short
     */
    public short getShortProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Short property loaded: " + key + "->" + value);

        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Short expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the short value of property.
     * 
     * @return the short value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an Short
     */
    public short getShortProperty(String key, short defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Short property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Short property loaded: " + key + "->" + value);

        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Short expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the double value of property.
     * 
     * @return the double value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not an double
     */
    public double getDoubleProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Double property loaded: " + key + "->" + value);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Double expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the double value of property.
     * 
     * @return the double value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an Double
     */
    public double getDoubleProperty(String key, double defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Double property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Double property loaded: " + key + "->" + value);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Double expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the float value of property.
     * 
     * @return the float value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not an float
     */
    public float getFloatProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Float property loaded: " + key + "->" + value);

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Float expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the float value of property.
     * 
     * @return the float value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an Float
     */
    public float getFloatProperty(String key, float defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Float property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Float property loaded: " + key + "->" + value);

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Float expected for property " + key + ", not \""
                    + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the string value of property.
     * 
     * @return the string value of property
     * @param key
     *            property name
     */
    @Override
    public String getProperty(String key) {
        String value = super.getProperty(key);

        logger.debug("String property loaded: " + key + "->" + value);

        return value;
    }

    /**
     * Returns the float value of property.
     * 
     * @return the float value of property
     * @param key
     *            property name
     * @param defaultValue
     *            default value if the property is undefined
     * @throws NumberFormatException
     *             if the property defined and not an Float
     */
    @Override
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("String property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("String property loaded: " + key + "->" + value);

        return value;
    }

    /**
     * Returns the long value of a size property. Valid values for the property
     * are a long, a long followed by K, a long followed by M or a long followed
     * by G. Size modifiers multiply the value by 1024, 1024^2 and 1024^3
     * respectively.
     * 
     * @return the size value of property
     * @param key
     *            property name
     * @throws NumberFormatException
     *             if the property is undefined or not a valid size
     */
    public long getSizeProperty(String key) {
        String value = getProperty(key);

        if (value == null) {
            throw new NumberFormatException("property undefined: " + key);
        }
        logger.debug("Size property loaded: " + key + "->" + value);

        return getSizeProperty(key, 0);
    }

    /**
     * Returns the long value of a size property. Valid values for the property
     * are a long, a long followed by K, a long followed by M or a long followed
     * by G. Size modifiers multiply the value by 1024, 1024^2 and 1024^3
     * respectively. Returns the default value if the property is undefined.
     * 
     * @return the size value of property
     * @param key
     *            property name
     * @param defaultValue
     *            the default value
     * @throws NumberFormatException
     *             if the property is not a valid size
     */
    public long getSizeProperty(String key, long defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            logger.debug("Size property default: " + key + "->" + defaultValue);
            return defaultValue;
        }
        logger.debug("Size property loaded: " + key + "->" + value);

        try {

            if (value.endsWith("G") || value.endsWith("g")) {
                return Long.parseLong(value.substring(0, value.length() - 1)) * 1024 * 1024 * 1024;
            }

            if (value.endsWith("M") || value.endsWith("m")) {
                return Long.parseLong(value.substring(0, value.length() - 1)) * 1024 * 1024;
            }

            if (value.endsWith("K") || value.endsWith("k")) {
                return Long.parseLong(value.substring(0, value.length() - 1)) * 1024;
            }

            return Long.parseLong(value);

        } catch (NumberFormatException e) {
            NumberFormatException ex = new NumberFormatException("Long[G|g|M|m|K|k] expected for property " + key
                    + ", not \"" + value + "\"");
            ex.setStackTrace(e.getStackTrace());

            throw ex;
        }
    }

    /**
     * Returns the split-up value of a string property. The value is supposed to
     * be a comma-separated string, with each comma preceded and followed by any
     * amount of whitespace. See {@link java.lang.String#split(String)} for
     * details of the splitting. If the property is not defined, an empty array
     * of strings is returned.
     * 
     * @param key
     *            the property name
     * @return the split-up property value.
     */
    public String[] getStringList(String key) {
        return getStringList(key, "\\s*,\\s*", new String[0]);
    }

    /**
     * Returns the split-up value of a string property. The value is split up
     * according to the specified delimiter. See
     * {@link java.lang.String#split(String)} for details of the splitting. If
     * the property is not defined, an empty array of strings is returned.
     * 
     * @param key
     *            the property name
     * @param delim
     *            the delimiter
     * @return the split-up property value.
     */
    public String[] getStringList(String key, String delim) {
        return getStringList(key, delim, new String[0]);
    }

    /**
     * Returns the split-up value of a string property. The value is split up
     * according to the specified delimiter. See
     * {@link java.lang.String#split(String)} for details of the splitting. If
     * the property is not defined, the specified default value is returned.
     * 
     * @param key
     *            the property name
     * @param delim
     *            the delimiter
     * @param defaultValue
     *            the default value
     * @return the split-up property value.
     */
    public String[] getStringList(String key, String delim, String[] defaultValue) {
        String value = getProperty(key);

        if (value == null) {
            return defaultValue;
        }

        return value.split(delim);
    }

    /**
     * Returns true if property name is defined and has a string value that
     * equals match.
     * 
     * @return true if property is defined and equals match
     * @param key
     *            property name
     * @param match
     *            value to be matched
     */
    public boolean stringPropertyMatch(String key, String match) {
        String value = getProperty(key);
        return value != null && value.equals(match);
    }

    /**
     * Returns true if the given element is a member of the given list.
     * 
     * @param list
     *            the given list.
     * @param element
     *            the given element.
     * @return true if the given element is a member of the given list.
     */
    private static boolean contains(String[] list, String element) {
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.length; i++) {
            if (element.equalsIgnoreCase(list[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given string starts with one of the given prefixes.
     * 
     * @param string
     *            the given string.
     * @param prefixes
     *            the given prefixes.
     * @return true if the given string starts with one of the given prefixes.
     */
    private static boolean startsWith(String string, String[] prefixes) {
        if (prefixes == null) {
            return false;
        }
        for (int i = 0; i < prefixes.length; i++) {
            if (string.startsWith(prefixes[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks all properties with the given prefix for validity.
     * 
     * @return a Property object containing all unrecognized properties.
     * @param prefix
     *            the prefix that should be checked
     * @param validKeys
     *            the set of valid keys (all with the prefix).
     * @param validSubPrefixes
     *            if a propery starts with one of these prefixes, it is declared
     *            valid
     * @param printWarning
     *            if true, a warning is printed to standard error for each
     *            unknown property
     */
    public TypedProperties checkProperties(String prefix, String[] validKeys, String[] validSubPrefixes,
            boolean printWarning) {
        String tmpPrefix = prefix;
        TypedProperties result = new TypedProperties();

        if (tmpPrefix == null) {
            tmpPrefix = "";
        }

        for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();

            if (key.startsWith(tmpPrefix)) {
                String suffix = key.substring(tmpPrefix.length());
                String value = getProperty(key);

                if (!startsWith(suffix, validSubPrefixes) && !contains(validKeys, key)) {
                    if (printWarning) {
                        logger.error("Warning, unknown property: " + key + " with value: " + value);
                    }
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    /**
     * Returns all properties who's key start with a certain prefix.
     * 
     * @return a Property object containing all matching properties.
     * @param prefix
     *            the desired prefix
     * @param removePrefix
     *            should the prefix be removed from the property name?
     * @param removeProperties
     *            should the returned properties be removed from the current
     *            properties?
     */
    public TypedProperties filter(String prefix, boolean removePrefix, boolean removeProperties) {
        String tmpPrefix = prefix;

        TypedProperties result = new TypedProperties();

        if (tmpPrefix == null) {
            tmpPrefix = "";
        }

        for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();

            if (key.startsWith(tmpPrefix)) {

                String value = getProperty(key);

                if (removePrefix) {
                    result.put(key.substring(tmpPrefix.length()), value);
                } else {
                    result.put(key, value);
                }

                if (removeProperties) {
                    remove(key);
                }
            }
        }

        return result;
    }

    /**
     * Returns all properties who's key start with a certain prefix.
     * 
     * @return a Property object containing all matching properties.
     * @param prefix
     *            the desired prefix
     */
    public TypedProperties filter(String prefix) {
        return filter(prefix, false, false);
    }

    /**
     * Prints properties (including default properties) to a stream.
     * 
     * @param out
     *            The stream to write output to.
     * @param prefix
     *            Only print properties which start with the given prefix. If
     *            null, will print all properties
     */
    public void printProperties(PrintStream out, String prefix) {
        String tmpPrefix = prefix;
        if (tmpPrefix == null) {
            tmpPrefix = "";
        }

        for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = getProperty(key);

            if (key.toLowerCase().startsWith(tmpPrefix.toLowerCase())) {
                out.println(key + " = " + value);
            }
        }
    }

    /**
     * Creates a string representation of this properties object.
     * 
     * @return the string representation.
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            String value = getProperty(key);

            buf.append(key + " = " + value + "\n");
        }

        return buf.toString();
    }

    String[] getPropertyNames() {
        ArrayList<String> list = new ArrayList<String>();
        for (Enumeration<?> e = propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            list.add(key);
        }
        String[] result = list.toArray(new String[list.size()]);
        Arrays.sort(result);
        return result;
    }

}
