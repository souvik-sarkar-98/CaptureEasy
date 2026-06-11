package app.captureeasy.core.ui;

import java.awt.Color;
import java.awt.Dimension;

/**
 * Central home for all UI magic numbers used by ControlWindow and ActionWindow.
 * Change a constant here and every component that references it updates automatically.
 */
public final class UIConstants {

    private UIConstants() {}

    // ── Control window geometry ──────────────────────────────────────────────
    /** Width of the control window strip (collapsed or expanded). */
    public static final int CONTROL_WINDOW_WIDTH = 54;

    /** Height when the control panel is hidden. */
    public static final int CONTROL_WINDOW_COLLAPSED_HEIGHT = 110;

    /** Height when the control panel is fully expanded. */
    public static final int CONTROL_WINDOW_EXPANDED_HEIGHT = 560;

    // ── Click pad ────────────────────────────────────────────────────────────
    public static final int CLICK_PAD_HEIGHT = 50;

    // ── Button grid ──────────────────────────────────────────────────────────
    /** Uniform size applied to every icon button in the control panel. */
    public static final Dimension ICON_BUTTON_SIZE = new Dimension(50, 50);

    /** X offset for buttons inside the 54-wide strip. */
    public static final int BUTTON_X = 1;

    /** Y position of the menu button (just below the click pad). */
    public static final int MENU_BUTTON_Y = 55;

    /** Y positions of the action buttons inside the expanded control panel. */
    public static final int POWER_BUTTON_Y    = 0;
    public static final int PAUSE_BUTTON_Y    = 55;
    public static final int DELETE_BUTTON_Y   = 110;
    public static final int SAVE_BUTTON_Y     = 165;
    public static final int VIEW_BUTTON_Y     = 220;
    public static final int RECORD_BUTTON_Y   = 275;
    public static final int SETTINGS_BUTTON_Y = 330;

    // ── Action window geometry ────────────────────────────────────────────────
    public static final Dimension ACTION_WINDOW_SIZE = new Dimension(640, 470);

    // ── Action window panel design tokens ─────────────────────────────────────
    /** Dark header strip (#2C3E50). */
    public static final Color AW_DARK    = new Color(44,  62,  80);
    /** Green accent — bottom rule on header, active borders (#27AE60). */
    public static final Color AW_ACCENT  = new Color(39, 174, 96);
    /** Panel content background (#F1F2F6). */
    public static final Color AW_BG      = new Color(241, 242, 246);
    /** Card background — white. */
    public static final Color AW_CARD    = Color.WHITE;
    /** Primary text — same as DARK. */
    public static final Color AW_TEXT    = new Color(44,  62,  80);
    /** Muted / secondary text (#7F8C8D). */
    public static final Color AW_MUTED   = new Color(127, 140, 141);
    /** Card/field border (#BDC3C7). */
    public static final Color AW_BORDER  = new Color(189, 195, 199);
    /** Primary button background — accent green. */
    public static final Color BTN_PRIMARY   = new Color(39, 174, 96);
    /** Secondary / cancel button (#7F8C8D). */
    public static final Color BTN_SECONDARY = new Color(127, 140, 141);

    /** Standard font family used across all action-window panels. */
    public static final String FONT_FAMILY = "Tahoma";
    /** Header strip height in pixels. */
    public static final int AW_HEADER_H = 58;

    // ── Image scaling hint (java.awt.Image constant) ─────────────────────────
    /** Bicubic scaling for icons — use with Image.getScaledInstance(). */
    public static final int ICON_SCALE_HINT = 4;
}
