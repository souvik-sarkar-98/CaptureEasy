package app.captureeasy.core.controller;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.ui.SystemNotifier;
import app.captureeasy.core.eventdto.SaveResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Desktop;
import java.io.IOException;

/**
 * Translates domain events into OS-level user feedback (system tray
 * notifications and desktop folder opens).
 *
 * <p>This is the only class that may call {@link SystemNotifier} or
 * {@link Desktop}. Controllers that finish an operation publish a result
 * event; this controller reacts to those events so that notification
 * concerns are fully isolated from business logic.</p>
 */
public class NotificationController {

    private static final Logger log = LogManager.getLogger(NotificationController.class);

    protected NotificationController() {
        registerSaveCompletedEvent();
        registerSaveFailedEvent();
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    private void registerSaveCompletedEvent() {
        EventBus.subscribe(EventType.SAVE_COMPLETED, (AppEvent<SaveResult> event) -> {
            SaveResult result = event.getData();
            if (result == null) return;

            String message = result.getScreenshotCount() + " screenshot(s) saved → "
                    + result.getOutputFile().getName();
            SystemNotifier.showSystemNotification("Save Complete", message, "INFO");

            try {
                Desktop.getDesktop().open(result.getOutputFile().getParentFile());
            } catch (IOException e) {
                log.warn("Could not open output folder after save", e);
            }
        });
    }

    private void registerSaveFailedEvent() {
        EventBus.subscribe(EventType.SAVE_FAILED, (AppEvent<String> event) -> {
            String reason = event.getData() != null ? event.getData() : "Unknown error.";
            SystemNotifier.showSystemNotification("Save Failed", reason, "ERROR");
        });
    }
}
