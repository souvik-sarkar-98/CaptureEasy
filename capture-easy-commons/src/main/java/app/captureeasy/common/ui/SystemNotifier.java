package app.captureeasy.common.ui;

import java.awt.*;

public class SystemNotifier {

    private static TrayIcon trayIcon;

    /**
     * Displays a system tray notification.
     *
     * @param title   The title of the notification
     * @param message The message body
     * @param type    The message type: "ERROR", "INFO", or "WARNING"
     */
    public static void showSystemNotification(String title, String message, String type) {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray not supported on this platform.");
            return;
        }

        try {
            // Initialize tray icon only once
            if (trayIcon == null) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage(new byte[0]); // blank image
                trayIcon = new TrayIcon(image, "CaptureEasy");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
            }

            TrayIcon.MessageType messageType = switch (type.toUpperCase()) {
                case "ERROR" -> TrayIcon.MessageType.ERROR;
                case "WARNING" -> TrayIcon.MessageType.WARNING;
                default -> TrayIcon.MessageType.INFO;
            };

            trayIcon.displayMessage(title, message, messageType);

        } catch (Exception e) {
            System.err.println("Failed to show notification: " + e.getMessage());
        }
    }

    // Example usage
    public static void main(String[] args) {
        showSystemNotification("Error Occurred", "Something went wrong!", "ERROR");
        showSystemNotification("Success", "Operation completed!", "INFO");
        showSystemNotification("Watch out!", "Check your input.", "WARNING");
    }
}
