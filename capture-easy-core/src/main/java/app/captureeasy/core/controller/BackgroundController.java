package app.captureeasy.core.controller;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.core.eventdto.SaveConfig;
import app.captureeasy.core.eventdto.SaveResult;
import app.captureeasy.core.services.CaptureService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Handles background operations: initial screenshot count on startup and
 * Word document export.
 *
 * <p>No Swing, Desktop, or notification code lives here. After an export
 * completes (or fails), this controller publishes {@link EventType#SAVE_COMPLETED}
 * or {@link EventType#SAVE_FAILED} so that {@link NotificationController} handles
 * all OS-level feedback.</p>
 */
public class BackgroundController {

    private static final Logger log = LogManager.getLogger(BackgroundController.class);

    private final CaptureService captureService;

    protected BackgroundController() throws Exception {
        captureService = new CaptureService();
        registerAppInitializationEvent();
        registerSaveScreenshotsEvent();
    }

    // ── Event handlers ────────────────────────────────────────────────────────

    private void registerAppInitializationEvent() {
        EventBus.subscribe(EventType.APP_LOADED, event -> {
            try {
                int count = Math.toIntExact(captureService.getScreenshotCount());
                EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, count));
            } catch (Exception e) {
                log.error("Failed to read initial screenshot count", e);
            }
        });
    }

    /**
     * Subscribes to {@link EventType#SAVE_SCREENSHOTS} and exports all session
     * screenshots to a Word document (.docx) using Apache POI.
     *
     * <p>On success publishes {@link EventType#SAVE_COMPLETED} with a
     * {@link SaveResult}. On failure publishes {@link EventType#SAVE_FAILED}
     * with a human-readable error string. Either way the publishing thread is
     * the EventBus async executor — the EDT is never touched here.</p>
     */
    private void registerSaveScreenshotsEvent() {
        EventBus.subscribe(EventType.SAVE_SCREENSHOTS, (AppEvent<SaveConfig> event) -> {
            SaveConfig config = event.getData();
            if (config == null) return;
            exportToWord(config);
        });
    }

    // ── Word export ───────────────────────────────────────────────────────────

    private void exportToWord(SaveConfig config) {
        // ── Gather screenshots ────────────────────────────────────────────────
        File tempDir = new File(PropertyService.getInstance().getTempFolder());
        File[] screenshots = tempDir.listFiles(f ->
                f.isFile() && (f.getName().toLowerCase().endsWith(".png")
                        || f.getName().toLowerCase().endsWith(".jpg")
                        || f.getName().toLowerCase().endsWith(".jpeg")));

        if (screenshots == null || screenshots.length == 0) {
            EventBus.publish(new AppEvent<>(EventType.SAVE_FAILED,
                    "No screenshots to save. Take some screenshots first."));
            return;
        }
        Arrays.sort(screenshots, Comparator.comparingLong(File::lastModified));

        // ── Build or open document ────────────────────────────────────────────
        try {
            final XWPFDocument doc;
            final File outputFile;

            if (config.getFormat() == SaveConfig.Format.EXISTING_WORD) {
                outputFile = new File(config.getTargetFolder());
                try (FileInputStream fis = new FileInputStream(outputFile)) {
                    doc = new XWPFDocument(fis);
                }
            } else {
                doc = new XWPFDocument();
                String name = config.getFilename().toLowerCase().endsWith(".docx")
                        ? config.getFilename()
                        : config.getFilename() + ".docx";
                outputFile = new File(config.getTargetFolder(), name);
            }

            // ── Insert each screenshot ────────────────────────────────────────
            for (File screenshot : screenshots) {
                XWPFParagraph para = doc.createParagraph();
                XWPFRun run = para.createRun();

                String fname = screenshot.getName().toLowerCase();
                int pictureType = (fname.endsWith(".jpg") || fname.endsWith(".jpeg"))
                        ? XWPFDocument.PICTURE_TYPE_JPEG
                        : XWPFDocument.PICTURE_TYPE_PNG;

                int targetWidthEMU = (int) Units.toEMU(430);
                int targetHeightEMU;
                BufferedImage img = ImageIO.read(screenshot);
                if (img != null && img.getWidth() > 0) {
                    targetHeightEMU = (int) (targetWidthEMU * (double) img.getHeight() / img.getWidth());
                } else {
                    targetHeightEMU = (int) Units.toEMU(242);
                }

                try (FileInputStream imgStream = new FileInputStream(screenshot)) {
                    run.addPicture(imgStream, pictureType, screenshot.getName(),
                            targetWidthEMU, targetHeightEMU);
                }
            }

            // ── Write to disk ─────────────────────────────────────────────────
            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                doc.write(out);
            }
            doc.close();

            log.info("Saved {} screenshot(s) to {}", screenshots.length, outputFile.getAbsolutePath());

            // ── Notify via event — no Swing or Desktop code here ──────────────
            EventBus.publish(new AppEvent<>(EventType.SAVE_COMPLETED,
                    new SaveResult(screenshots.length, outputFile)));

        } catch (Exception e) {
            log.error("Failed to export screenshots to Word document", e);
            EventBus.publish(new AppEvent<>(EventType.SAVE_FAILED,
                    "Could not save screenshots: " + e.getMessage()));
        }
    }
}
