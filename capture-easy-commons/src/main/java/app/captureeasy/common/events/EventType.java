package app.captureeasy.common.events;

public enum EventType {

    // ── Lifecycle ─────────────────────────────────────────────────────────────
    APP_LOADED,

    // ── Screenshot flow ───────────────────────────────────────────────────────
    /** Emitted by UI / keyboard adapter to request a capture. */
    TAKE_SCREENSHOT,
    /**
     * Emitted by ControlWindowController immediately after receiving
     * TAKE_SCREENSHOT and before performing the capture.
     * UI subscribes to this (not TAKE_SCREENSHOT) for side-effects such as
     * hiding the control window before the screen is captured.
     */
    SCREENSHOT_CAPTURE_STARTED,

    // ── Screenshot state updates ──────────────────────────────────────────────
    /** Payload: {@code Integer} — current screenshot count. */
    UPDATE_SCREENSHOT_COUNT,

    // ── User commands ─────────────────────────────────────────────────────────
    CLOSE_APP,
    DELETE_SCREENSHOTS,
    SHOW_LATEST_SCREENSHOT,

    // ── Save flow ─────────────────────────────────────────────────────────────
    /** Payload: {@link app.captureeasy.core.eventdto.SaveConfig}. */
    SAVE_SCREENSHOTS,
    /** Payload: {@link app.captureeasy.core.eventdto.SaveResult}. */
    SAVE_COMPLETED,
    /** Payload: {@code String} — human-readable error message. */
    SAVE_FAILED,

    // ── Settings flow ─────────────────────────────────────────────────────────
    /** Payload: {@link app.captureeasy.core.eventdto.SettingsDto}. */
    SAVE_SETTINGS,
    /** Fired after settings are persisted; payload: none. */
    SETTINGS_SAVED,
}
