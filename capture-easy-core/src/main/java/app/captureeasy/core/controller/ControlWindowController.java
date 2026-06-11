package app.captureeasy.core.controller;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.core.services.CaptureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * Handles screenshot lifecycle commands and user-initiated actions
 * (capture, delete, view folder, close).
 *
 * <p>No direct dependency on any UI class. All communication is through
 * {@link EventBus}.</p>
 */
public class ControlWindowController {

    private static final Logger log = LogManager.getLogger(ControlWindowController.class);

    private final CaptureService captureService;

    protected ControlWindowController() throws Exception {
        captureService = new CaptureService();
        registerScreenshotCaptureEvent();
        registerCloseApplicationEvent();
        registerDeleteScreenshotsEvent();
        registerViewScreenshotEvent();
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    /**
     * Handles {@link EventType#TAKE_SCREENSHOT}.
     *
     * <p>Publishes {@link EventType#SCREENSHOT_CAPTURE_STARTED} first so the UI
     * can hide itself before the screen is captured, then performs the capture
     * and publishes {@link EventType#UPDATE_SCREENSHOT_COUNT} so the UI can
     * restore itself.</p>
     */
    private void registerScreenshotCaptureEvent() {
        EventBus.subscribe(EventType.TAKE_SCREENSHOT, event -> {
            EventBus.publish(new AppEvent<>(EventType.SCREENSHOT_CAPTURE_STARTED));
            try {
                captureService.captureScreenshot();
                int count = Math.toIntExact(captureService.getScreenshotCount());
                EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, count));
            } catch (Exception e) {
                log.error("Screenshot capture failed", e);
                // Restore UI even on failure so the count label refreshes
                try {
                    int count = Math.toIntExact(captureService.getScreenshotCount());
                    EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, count));
                } catch (Exception ignored) {}
            }
        });
    }

    private void registerCloseApplicationEvent() {
        EventBus.subscribe(EventType.CLOSE_APP, event -> System.exit(0));
    }

    private void registerDeleteScreenshotsEvent() {
        EventBus.subscribe(EventType.DELETE_SCREENSHOTS, event -> {
            try {
                captureService.deleteScreenshots();
                int count = Math.toIntExact(captureService.getScreenshotCount());
                EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, count));
            } catch (IOException e) {
                log.error("Failed to delete screenshots", e);
            }
        });
    }

    private void registerViewScreenshotEvent() {
        EventBus.subscribe(EventType.SHOW_LATEST_SCREENSHOT, event -> {
            String folder = PropertyService.getInstance().getTempFolder();
            File directory = new File(folder);
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                try {
                    Desktop.getDesktop().open(directory);
                } catch (IOException e) {
                    log.error("Failed to open screenshot folder", e);
                }
            } else {
                // No screenshots — notify via event so NotificationController handles the message
                EventBus.publish(new AppEvent<>(EventType.SAVE_FAILED, "No screenshots to view."));
            }
        });
    }
}
