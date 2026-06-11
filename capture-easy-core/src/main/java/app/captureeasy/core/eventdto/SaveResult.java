package app.captureeasy.core.eventdto;

import lombok.Value;

import java.io.File;

/**
 * Payload for {@link app.captureeasy.common.events.EventType#SAVE_COMPLETED}.
 *
 * <p>BackgroundController publishes this after a successful Word export so that
 * a dedicated NotificationController can show the OS notification and open the
 * output folder — without BackgroundController needing to know about Swing or
 * the desktop environment.</p>
 */
@Value
public class SaveResult {
    /** Number of screenshots written into the document. */
    int screenshotCount;
    /** The output .docx file. Used by NotificationController to open its parent folder. */
    File outputFile;
}
