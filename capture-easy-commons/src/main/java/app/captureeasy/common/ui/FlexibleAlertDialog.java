package app.captureeasy.common.ui;

import app.captureeasy.common.util.ResourceUtil;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FlexibleAlertDialog {
    public static void show(Component parent, String messageKey, String titleKey, String iconPath, String[] options, Consumer<Integer> callback) {
        String message = ResourceUtil.getString(messageKey);
        String title = ResourceUtil.getString(titleKey);
        ImageIcon icon = ResourceUtil.getIcon(iconPath);

        int result = JOptionPane.showOptionDialog(parent, message, title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
        if (callback != null) {
            callback.accept(result);
        }
    }
}