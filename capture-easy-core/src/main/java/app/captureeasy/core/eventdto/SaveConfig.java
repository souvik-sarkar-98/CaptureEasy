package app.captureeasy.core.eventdto;

import lombok.Value;

/**
 * Payload for {@code EventType.SAVE_SCREENSHOTS}.
 * Describes how and where screenshots should be saved to a Word document.
 */
@Value
public class SaveConfig {

    public enum Format {
        /** Create a new .docx file at {@code targetFolder/filename.docx}. */
        NEW_WORD,
        /** Append screenshots to an existing .docx whose full path is {@code targetFolder}. */
        EXISTING_WORD
    }

    Format format;

    /**
     * For {@link Format#NEW_WORD}: the destination directory path.
     * For {@link Format#EXISTING_WORD}: the full path of the existing .docx file.
     */
    String targetFolder;

    /** Base filename (no extension) for {@link Format#NEW_WORD}; ignored for {@link Format#EXISTING_WORD}. */
    String filename;
}
