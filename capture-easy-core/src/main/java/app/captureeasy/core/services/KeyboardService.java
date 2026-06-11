package app.captureeasy.core.services;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.core.enums.TriggerSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.function.BiConsumer;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Global keyboard listener that fires TAKE_SCREENSHOT when the registered
 * hotkey is pressed.
 *
 * <p><strong>Security contract:</strong> no raw keystroke data (characters,
 * text, or key codes of unmatched keys) is ever logged, stored, or forwarded.
 * Only the virtual key code and action-modifier bits of each event are read;
 * all non-matching events are silently discarded here.</p>
 */
public class KeyboardService implements NativeKeyListener {

    private static final Logger log = LogManager.getLogger(KeyboardService.class);

    /**
     * Defines a hotkey as a virtual key code plus an optional set of required
     * modifier keys. A {@code modifierMask} of {@code 0} means no modifiers.
     */
    public record HotkeyConfig(int keyCode, int modifierMask) {
        /** PrintScreen with no modifiers — the built-in default. */
        public static final HotkeyConfig PRINT_SCREEN =
                new HotkeyConfig(NativeKeyEvent.VC_PRINTSCREEN, 0);
    }

    /** PropertyService key used to persist the user-configured hotkey key code. */
    public static final String PROP_KEY_CODE      = "HOTKEY_KEY_CODE";
    /** PropertyService key used to persist the required modifier mask (e.g. Ctrl = CTRL_MASK). */
    public static final String PROP_MODIFIER_MASK = "HOTKEY_MODIFIER_MASK";

    /**
     * Only action-modifier bits (Ctrl / Alt / Shift / Meta). Lock-key bits
     * (Caps Lock, Num Lock, etc.) are masked out to avoid false negatives
     * when the user has those active.
     */
    static final int ACTION_MODIFIER_MASK =
            NativeInputEvent.CTRL_MASK  |
            NativeInputEvent.ALT_MASK   |
            NativeInputEvent.SHIFT_MASK |
            NativeInputEvent.META_MASK;

    public KeyboardService() {
        log.info("Screenshot hotkey listener registered (uses runtime config from PropertyService)");
    }

    // ── Public helpers ────────────────────────────────────────────────────────

    /**
     * Returns the human-readable name of the currently configured hotkey, including
     * any required modifiers (e.g. "Ctrl+F9", "Ctrl+Shift+S", "Print Screen").
     * Reads from {@link PropertyService}; falls back to the default if nothing is stored.
     */
    public static String getHotkeyDisplayName() {
        PropertyService ps = PropertyService.getInstance();
        int kc = ps.getInt(PROP_KEY_CODE,      HotkeyConfig.PRINT_SCREEN.keyCode());
        int mm = ps.getInt(PROP_MODIFIER_MASK,  HotkeyConfig.PRINT_SCREEN.modifierMask());
        return buildDisplayName(kc, mm);
    }

    /**
     * Builds a human-readable hotkey string such as "Ctrl+Alt+F9" from a
     * virtual key code and a modifier mask using {@link NativeInputEvent} bits.
     */
    public static String buildDisplayName(int keyCode, int modifierMask) {
        StringBuilder sb = new StringBuilder();
        if ((modifierMask & NativeInputEvent.CTRL_MASK)  != 0) sb.append("Ctrl+");
        if ((modifierMask & NativeInputEvent.ALT_MASK)   != 0) sb.append("Alt+");
        if ((modifierMask & NativeInputEvent.SHIFT_MASK) != 0) sb.append("Shift+");
        if ((modifierMask & NativeInputEvent.META_MASK)  != 0) sb.append("Meta+");
        sb.append(NativeKeyEvent.getKeyText(keyCode));
        return sb.toString();
    }

    /**
     * Returns {@code true} if {@code keyCode} is a standalone modifier key
     * (Ctrl, Alt, Shift, Meta in any variant).
     */
    public static boolean isModifierKey(int keyCode) {
        return keyCode == NativeKeyEvent.VC_CONTROL
            || keyCode == NativeKeyEvent.VC_ALT        // 2.1.0: no _L/_R split for Alt
            || keyCode == NativeKeyEvent.VC_SHIFT
            || keyCode == NativeKeyEvent.VC_META;
    }

    /**
     * Temporarily registers a one-shot {@link NativeKeyListener} that waits for
     * the user to press a non-modifier key (optionally while holding Ctrl / Alt /
     * Shift / Meta) and delivers {@code (keyCode, modifierMask)} to
     * {@code onCaptured}, then immediately unregisters itself.
     *
     * <p>Pressing Escape cancels the capture — {@code onCaptured} is NOT called.</p>
     *
     * <p>Keeping all {@link GlobalScreen} usage inside this service means UI classes
     * (e.g. SettingsPanel) do not need to import jnativehook directly.</p>
     *
     * @param onCaptured {@code BiConsumer<keyCode, modifierMask>} called on the
     *                   jnativehook dispatch thread; use {@code SwingUtilities.invokeLater}
     *                   when updating Swing components inside this callback.
     */
    public static void startHotkeyCapture(BiConsumer<Integer, Integer> onCaptured) {
        NativeKeyListener[] holder = new NativeKeyListener[1];
        holder[0] = new NativeKeyListener() {
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
            @Override public void nativeKeyReleased(NativeKeyEvent e) {}

            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                int kc = e.getKeyCode();
                if (isModifierKey(kc)) return;             // still waiting — user is holding a modifier
                GlobalScreen.removeNativeKeyListener(holder[0]);
                if (kc != NativeKeyEvent.VC_ESCAPE) {      // Escape = cancel
                    int modMask = e.getModifiers() & ACTION_MODIFIER_MASK;
                    onCaptured.accept(kc, modMask);
                }
            }
        };
        GlobalScreen.addNativeKeyListener(holder[0]);
    }

    // ── NativeKeyListener ─────────────────────────────────────────────────────

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) { /* intentionally empty */ }

    /**
     * Publishes {@link EventType#TAKE_SCREENSHOT} if and only if the pressed key
     * matches the hotkey stored in {@link PropertyService} (default: PrintScreen).
     *
     * <p>The hotkey is read from {@code PropertyService} on every event so that
     * changes made in Settings take effect immediately without restarting the hook.</p>
     *
     * <p>Only {@code e.getKeyCode()} and the action-modifier bits of
     * {@code e.getModifiers()} are read. Non-matching events are silently discarded.</p>
     */
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        PropertyService ps = PropertyService.getInstance();
        int configuredKey = ps.getInt(PROP_KEY_CODE,      HotkeyConfig.PRINT_SCREEN.keyCode());
        int configuredMod = ps.getInt(PROP_MODIFIER_MASK, HotkeyConfig.PRINT_SCREEN.modifierMask());
        if (e.getKeyCode() == configuredKey
                && (e.getModifiers() & ACTION_MODIFIER_MASK) == configuredMod) {
            EventBus.publishAsync(new AppEvent<>(EventType.TAKE_SCREENSHOT, TriggerSource.KEYBOARD_EVENT));
        }
        // All non-matching events are discarded — no key data is accessed or logged.
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) { /* intentionally empty */ }
}


/**
 * Transferable wrapper for placing a captured screenshot on the system clipboard.
 * Uses a bounded retry mechanism to handle temporary clipboard unavailability.
 */
class ImageSelection implements Transferable {
    private static final int MAX_RETRIES = 3;
    private final Image image;

    public ImageSelection(Image image) {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{ DataFlavor.imageFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (!DataFlavor.imageFlavor.equals(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }

    public static void setClipboardImage() {
        setClipboardImage(MAX_RETRIES);
    }

    private static void setClipboardImage(int retriesLeft) {
        if (retriesLeft <= 0) return;
        try {
            ImageSelection imgSel = new ImageSelection(
                    new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
        } catch (Exception e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            }
            setClipboardImage(retriesLeft - 1);
        }
    }
}
