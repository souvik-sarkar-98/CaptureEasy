package app.techy10souvik.captureeasy.common.util;

import java.util.prefs.Preferences;

public class PreferencesUtil {
    private static final Preferences prefs = Preferences.userRoot().node("app.techy10souvik.captureeasy");

    public static void put(String key, String value) {
        prefs.put(key, value);
    }

    public static String get(String key, String defaultValue) {
        return prefs.get(key, defaultValue);
    }

    public static void remove(String key) {
        prefs.remove(key);
    }
}