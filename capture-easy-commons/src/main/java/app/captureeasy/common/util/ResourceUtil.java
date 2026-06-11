package app.captureeasy.common.util;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.net.URL;
import java.util.ResourceBundle;

public class ResourceUtil {
    private static final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<>();
    private static final ResourceBundle strings = ResourceBundle.getBundle("strings");

    public static ImageIcon getIcon(String path) {
        return iconCache.computeIfAbsent(path, p -> {
            URL url = ResourceUtil.class.getResource(p);
            return url != null ? new ImageIcon(url) : null;
        });
    }

    public static String getString(String key) {
        try {
            return strings.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}