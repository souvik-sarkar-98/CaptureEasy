package app.techy10souvik.captureeasy.common.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Utility class for creating and managing Java Properties.
 */
public class PropertyUtil {

    /**
     * Creates a new empty Properties object.
     * @return a new Properties instance
     */
    public static Properties createProperties() {
        return new Properties();
    }

    /**
     * Loads properties from a file.
     * @param filePath the path to the properties file
     * @return the loaded Properties object
     * @throws IOException if an I/O error occurs
     */
    public static Properties loadProperties(String filePath) throws IOException {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(filePath)) {
            props.load(input);
        }
        return props;
    }

    /**
     * Saves properties to a file.
     * @param props the Properties object to save
     * @param filePath the path to the file
     * @param comments comments to include in the file
     * @throws IOException if an I/O error occurs
     */
    public static void saveProperties(Properties props, String filePath, String comments) throws IOException {
        try (OutputStream output = new FileOutputStream(filePath)) {
            props.store(output, comments);
        }
    }

    /**
     * Gets a property value by key.
     * @param props the Properties object
     * @param key the property key
     * @return the property value, or null if not found
     */
    public static String getProperty(Properties props, String key) {
        return props.getProperty(key);
    }

    /**
     * Sets a property value.
     * @param props the Properties object
     * @param key the property key
     * @param value the property value
     */
    public static void setProperty(Properties props, String key, String value) {
        props.setProperty(key, value);
    }

    /**
     * Removes a property by key.
     * @param props the Properties object
     * @param key the property key
     */
    public static void removeProperty(Properties props, String key) {
        props.remove(key);
    }
}
