package app.captureeasy.common.services;

import app.captureeasy.common.util.SystemUtil;

import java.awt.Dimension;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.UUID;

/**
 * Single settings layer for the application.
 *
 * <p>Backed by {@code app.properties} in the app's root folder. All reads are
 * unsynchronised (safe because {@link Properties} is backed by {@link java.util.Hashtable}).
 * All write sequences are {@code synchronized} to prevent lost-update races on the
 * check-then-set patterns used by the domain helpers below.</p>
 *
 * <p>Replaces the former {@code PreferencesUtil} (OS Preferences API) and
 * {@code PropertyUtil} (thin I/O wrapper), which have been removed.</p>
 */
public class PropertyService {

    private final Properties properties = new Properties();
    private final String propFilePath;

    private PropertyService() {
        try {
            this.propFilePath = SystemUtil.getPropFile();
            load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load settings file", e);
        }
    }

    /** Package-private constructor for unit testing — bypasses {@link SystemUtil#getPropFile()}. */
    PropertyService(String propFilePath) throws IOException {
        this.propFilePath = propFilePath;
        java.io.File f = new java.io.File(propFilePath);
        if (f.exists()) {
            load();
        }
    }

    private static class Holder {
        private static final PropertyService INSTANCE = new PropertyService();
    }

    public static PropertyService getInstance() {
        return Holder.INSTANCE;
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value.trim());
    }

    // ── Write ────────────────────────────────────────────────────────────────

    /** Stores a key-value pair and immediately persists it to disk. */
    public synchronized void set(String key, String value) {
        properties.setProperty(key, value);
        save();
    }

    /** Removes a key and immediately persists the change to disk. */
    public synchronized void remove(String key) {
        properties.remove(key);
        save();
    }

    // ── Persistence ──────────────────────────────────────────────────────────

    private void load() throws IOException {
        try (InputStream in = new FileInputStream(propFilePath)) {
            properties.load(in);
        }
    }

    private synchronized void save() {
        try (OutputStream out = new FileOutputStream(propFilePath)) {
            properties.store(out, "CaptureEasy settings");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save settings", e);
        }
    }

    // ── Domain helpers ───────────────────────────────────────────────────────

    public String getAppLockKey() {
        return getOrCreate("APP_LOCK_KEY", UUID.randomUUID().toString());
    }

    public String getTempFolder() {
        return getOrCreateTempFolder(false);
    }

    public String generateTempFolder() {
        return getOrCreateTempFolder(true);
    }

    public Point getGUILocation() {
        if (!properties.containsKey("X_LOCATION") || !properties.containsKey("Y_LOCATION")) {
            Dimension screen = SystemUtil.getScreenSize();
            synchronized (this) {
                set("X_LOCATION", String.valueOf(screen.width - 400));
                set("Y_LOCATION", "20");
            }
        }
        return new Point(
                Integer.parseInt(properties.getProperty("X_LOCATION")),
                Integer.parseInt(properties.getProperty("Y_LOCATION")));
    }

    public String getAppVersion() {
        return getOrCreate("APP_VERSION", "1.0");
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private synchronized String getOrCreate(String key, String defaultValue) {
        if (!properties.containsKey(key)) {
            set(key, defaultValue);
        }
        return properties.getProperty(key);
    }

    private synchronized String getOrCreateTempFolder(boolean forceNew) {
        if (!properties.containsKey("TEMP_FOLDER") || forceNew) {
            try {
                set("TEMP_FOLDER", SystemUtil.getTempPath().toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create temp folder", e);
            }
        }
        return properties.getProperty("TEMP_FOLDER");
    }
}
