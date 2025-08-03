package app.techy10souvik.captureeasy.common.services;

import app.techy10souvik.captureeasy.common.util.PropertyUtil;
import app.techy10souvik.captureeasy.common.util.SystemUtil;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * Service class to access properties using PropertyUtil.
 */
public class PropertyService {
    private final Properties properties;

    /**
     * Loads properties from the given file path.
     * @throws IOException if loading fails
     */
    private PropertyService()  {
        try {
            this.properties = PropertyUtil.loadProperties(SystemUtil.getPropFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PropertyService getInstance() {
        return new PropertyService();
    }

    /**
     * Gets a property value as String.
     * @param key the property key
     * @return the property value, or null if not found
     */
    public String getString(String key) {
        return PropertyUtil.getProperty(properties, key);
    }

    /**
     * Gets a property value as int.
     * @param key the property key
     * @param defaultValue the default value if property is missing or invalid
     * @return the property value as int
     */
    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Gets a property value as boolean.
     * @param key the property key
     * @param defaultValue the default value if property is missing
     * @return the property value as boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }

    /**
     * Reloads properties from the file.
     * @throws IOException if loading fails
     */
    public void saveAndReload()  {
        try {
            PropertyUtil.saveProperties(properties, SystemUtil.getPropFile(),"");
            properties.clear();
            properties.putAll(PropertyUtil.loadProperties(SystemUtil.getPropFile()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addProperty(String key, String val) {
        PropertyUtil.setProperty(properties, key, val);
        saveAndReload();
    }

    public String getAppLockKey() {
        if(!properties.containsKey("APP_LOCK_KEY")){
            addProperty("APP_LOCK_KEY", UUID.randomUUID().toString());
        }
        return properties.getProperty("APP_LOCK_KEY"); // Replace with actual key
    }


    public String getTempFolder(boolean createNewFolder) {
        if(!properties.containsKey("TEMP_FOLDER") || createNewFolder){
            try {
                addProperty("TEMP_FOLDER", SystemUtil.getTempPath().toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties.getProperty("TEMP_FOLDER");
    }

    public String getTempFolder() {
        return getTempFolder(false);
    }

    public Point getGUILocation() {
        if (!properties.containsKey("X_LOCATION") || !properties.containsKey("Y_LOCATION")) {
            int windowWidth = 400;
            int yLocation = 20;
            Dimension screenSize = SystemUtil.getScreenSize();
            int xLocation = screenSize.width - windowWidth;
            addProperty("X_LOCATION", String.valueOf(xLocation));
            addProperty("Y_LOCATION", String.valueOf(yLocation));
        }
        return new Point(Integer.parseInt(properties.getProperty("X_LOCATION")), Integer.parseInt(properties.getProperty("Y_LOCATION")));
    }

    public String getAppVersion() {
        if(!properties.containsKey("APP_VERSION")){
            addProperty("APP_VERSION", "1.0");
        }
        return properties.getProperty("APP_VERSION"); // Replace with actual version
    }
}
