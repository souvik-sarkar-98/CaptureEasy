package app.techy10souvik.captureeasy.common.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class for showing system-native popup dialogs using JOptionPane.
 */
public class SystemNativeDialog {

    static {
        // Set the system look and feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Optional: update existing components if needed
            for (Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window);
            }
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
    }

    /**
     * Shows a system-native message dialog.
     */
    public static void showMessageDialog(Component parentComponent, Object message, String title, int messageType) {
        JOptionPane.showMessageDialog(parentComponent, message, title, messageType);
    }

    /**
     * Shows an information dialog.
     */
    public static void showInfo(Component parentComponent, String message, String title) {
        showMessageDialog(parentComponent, wrapHtml(message), title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a warning dialog.
     */
    public static void showWarning(Component parentComponent, String message, String title) {
        showMessageDialog(parentComponent, wrapHtml(message), title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows an error dialog.
     */
    public static void showError(Component parentComponent, String message, String title) {
        showMessageDialog(parentComponent, wrapHtml(message), title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a confirmation dialog with Yes/No options.
     *
     * @return true if user selects Yes, false otherwise.
     */
    public static boolean showConfirmation(Component parentComponent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
                parentComponent,
                wrapHtml(message),
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Utility to wrap message in HTML for richer formatting.
     */
    private static String wrapHtml(String message) {
        if (message == null) return "";
        if (message.trim().startsWith("<html>")) return message;
        return "<html><body style='font-family:sans-serif;font-size:12pt;'>" + message + "</body></html>";
    }

    /**
     * Example usage.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            showInfo(null, "This is an <b>information</b> dialog.", "Info");
            showWarning(null, "This is a <b>warning</b> dialog.", "Warning");
            showError(null, "This is an <b>error</b> dialog.", "Error");
            boolean confirmed = showConfirmation(null, "Do you want to <b>continue</b>?", "Confirm");
            showInfo(null, "User selected: " + (confirmed ? "Yes" : "No"), "Result");
        });
    }
}