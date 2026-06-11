package app.captureeasy.core.eventdto;

import lombok.Value;

/**
 * Payload for {@link app.captureeasy.common.events.EventType#SAVE_SETTINGS}.
 *
 * <p>SettingsPanel publishes this event so that SettingsController can persist
 * the values via PropertyService. Fields are {@code null} when the user did not
 * change that particular setting during the current session.</p>
 */
@Value
public class SettingsDto {
    /** Selected image format string (e.g. "PNG", "JPEG"). Null = unchanged. */
    String imageFormat;
    /** Virtual key code of the new hotkey. Null = unchanged. */
    Integer hotkeyCode;
    /**
     * Required modifier mask for the hotkey (e.g. {@code NativeInputEvent.CTRL_MASK}).
     * {@code 0} means no modifiers required. Null = unchanged.
     */
    Integer hotkeyModifierMask;
}
