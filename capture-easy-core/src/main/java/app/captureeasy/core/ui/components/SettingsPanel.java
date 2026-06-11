package app.captureeasy.core.ui.components;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.core.eventdto.SettingsDto;
import app.captureeasy.core.services.KeyboardService;
import app.captureeasy.core.ui.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * "Settings" panel for the Settings tab in ActionWindow.
 *
 * <p>This panel is <em>view-only</em> with respect to persistence: it never
 * calls {@link PropertyService} directly for writes. Instead, when the user
 * clicks Save, it publishes a {@link EventType#SAVE_SETTINGS} event carrying
 * a {@link SettingsDto}; {@code SettingsController} handles all PropertyService
 * writes. Similarly, hotkey capture delegates to
 * {@link KeyboardService#startHotkeyCapture} so the panel has no direct
 * dependency on jnativehook's {@code GlobalScreen}.</p>
 *
 * <p>Layout — fully responsive to window resize:
 * <pre>
 *  ┌─────────────────────────────────────────┐ NORTH  (fixed height, custom-painted header)
 *  ├─────────────────────────────────────────┤ CENTER (BoxLayout.Y_AXIS, stretches)
 *  │  ┌─ Capture ──────────────────────────┐ │
 *  │  │  Screenshot hotkey │ [F9] [Change] │ │
 *  │  │  Image format      │ [PNG ▾]       │ │
 *  │  └────────────────────────────────────┘ │
 *  │  ┌─ Window ───────────────────────────┐ │
 *  │  │  Window position   │ [Reset]       │ │
 *  │  └────────────────────────────────────┘ │
 *  ├─────────────────────────────────────────┤ SOUTH  (Save button)
 *  └─────────────────────────────────────────┘
 * </pre>
 */
public class SettingsPanel {

    static final String KEY_IMAGE_FORMAT = "IMAGE_FORMAT";

    private final JPanel panel;
    private final JComboBox<String> formatCombo;
    private final JButton resetPositionBtn;
    private final JButton saveBtn;
    private final JFrame parent;

    /** Green badge that shows the currently selected hotkey name. */
    private final JLabel hotkeyDisplayLabel;
    /**
     * Key code and modifier mask chosen in the capture dialog but not yet
     * persisted. Both are {@code null} when the user has not changed the
     * hotkey this session.
     */
    private Integer pendingKeyCode        = null;
    private Integer pendingModifierMask   = null;

    private SettingsPanel(JFrame parent) {
        this.parent = parent;

        formatCombo      = formatComboBox();
        resetPositionBtn = PanelComponents.outlineButton("Reset");
        resetPositionBtn.setPreferredSize(new Dimension(90, 30));
        saveBtn          = PanelComponents.primaryButton("Save");

        // Load the currently active hotkey name for the initial badge display.
        hotkeyDisplayLabel = hotkeyBadge(KeyboardService.getHotkeyDisplayName());

        panel = buildPanel();
        wireActions();
    }

    public static SettingsPanel init(JFrame parent) { return new SettingsPanel(parent); }

    public Component getPanel() { return panel; }

    // ── Build ─────────────────────────────────────────────────────────────────

    private JPanel buildPanel() {
        // Load saved format for the combo initial value (read-only — no write here)
        String savedFormat = PropertyService.getInstance().getString(KEY_IMAGE_FORMAT, "PNG");

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.AW_BG);

        root.add(PanelComponents.header(
                "Settings",
                "Manage capture preferences"), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIConstants.AW_BG);
        content.setBorder(new EmptyBorder(16, 20, 8, 20));

        content.add(buildCaptureCard(savedFormat));
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        content.add(buildWindowCard());
        content.add(Box.createRigidArea(new Dimension(0, 16)));
        content.add(buildVersionLabel());
        content.add(Box.createVerticalGlue());

        root.add(content, BorderLayout.CENTER);

        JPanel footer = PanelComponents.footer();
        saveBtn.setPreferredSize(new Dimension(100, 34));
        footer.add(saveBtn);
        root.add(footer, BorderLayout.SOUTH);

        return root;
    }

    // ── Section cards ─────────────────────────────────────────────────────────

    private JPanel buildCaptureCard(String savedFormat) {
        JPanel card = PanelComponents.sectionCard("Capture");
        stretchCard(card);

        GridBagConstraints gbc = baseGbc();

        // Row 0: Screenshot hotkey
        gbc.gridy = 0;
        addRow(card, gbc, "Screenshot hotkey", buildHotkeyWidget());

        // Divider
        gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 12, 0, 12);
        card.add(divider(), gbc);

        // Row 2: Image format — pre-select saved value
        for (int i = 0; i < formatCombo.getItemCount(); i++) {
            if (formatCombo.getItemAt(i).equals(savedFormat)) {
                formatCombo.setSelectedIndex(i);
                break;
            }
        }
        gbc.gridy = 2; gbc.gridwidth = 1;
        addRow(card, gbc, "Image format", formatCombo);

        // Bottom spacer
        gbc.gridy = 3; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        card.add(transparentPanel(), gbc);

        return card;
    }

    private JPanel buildWindowCard() {
        JPanel card = PanelComponents.sectionCard("Window");
        stretchCard(card);

        GridBagConstraints gbc = baseGbc();

        gbc.gridy = 0;
        addRow(card, gbc, "Window position", resetPositionBtn);

        gbc.gridy = 1; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        card.add(transparentPanel(), gbc);

        return card;
    }

    // ── Hotkey widget ─────────────────────────────────────────────────────────

    private JPanel buildHotkeyWidget() {
        JPanel widget = new JPanel();
        widget.setLayout(new BoxLayout(widget, BoxLayout.X_AXIS));
        widget.setOpaque(false);

        widget.add(hotkeyDisplayLabel);
        widget.add(Box.createRigidArea(new Dimension(10, 0)));

        JButton changeBtn = PanelComponents.outlineButton("Change");
        changeBtn.setPreferredSize(new Dimension(80, 28));
        changeBtn.setMaximumSize(new Dimension(80, 28));
        changeBtn.addActionListener(e -> showHotkeyCaptureDialog());
        widget.add(changeBtn);

        return widget;
    }

    /**
     * Opens a small modal that shows "Press any key…" and delegates to
     * {@link KeyboardService#startHotkeyCapture} so this class never imports
     * or calls {@code GlobalScreen} directly.
     */
    private void showHotkeyCaptureDialog() {
        JDialog dlg = new JDialog(parent, "Change Hotkey", true);
        dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dlg.setResizable(false);

        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(UIConstants.AW_CARD);
        body.setBorder(new EmptyBorder(24, 28, 20, 28));

        JLabel prompt = new JLabel("Hold modifiers (Ctrl/Alt/Shift), then press a key", SwingConstants.CENTER);
        prompt.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        prompt.setForeground(UIConstants.AW_TEXT);

        JLabel sub = new JLabel("(Escape to cancel)", SwingConstants.CENTER);
        sub.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        sub.setForeground(UIConstants.AW_MUTED);

        JLabel detected = new JLabel(" ", SwingConstants.CENTER);
        detected.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 22));
        detected.setForeground(UIConstants.AW_ACCENT);
        detected.setBorder(new EmptyBorder(14, 0, 14, 0));

        JPanel textStack = new JPanel();
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        textStack.setOpaque(false);
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        detected.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        textStack.add(prompt);
        textStack.add(Box.createRigidArea(new Dimension(0, 4)));
        textStack.add(detected);
        textStack.add(sub);

        body.add(textStack, BorderLayout.CENTER);
        dlg.setContentPane(body);
        dlg.pack();
        dlg.setMinimumSize(new Dimension(280, 160));

        if (parent != null) {
            int px = parent.getX() + (parent.getWidth()  - dlg.getWidth())  / 2;
            int py = parent.getY() + (parent.getHeight() - dlg.getHeight()) / 2;
            dlg.setLocation(px, py);
        }

        // Delegate key capture to KeyboardService — no GlobalScreen in this panel.
        // The BiConsumer receives (keyCode, modifierMask) from the jnativehook thread.
        KeyboardService.startHotkeyCapture((kc, modMask) -> SwingUtilities.invokeLater(() -> {
            String name = KeyboardService.buildDisplayName(kc, modMask);
            detected.setText(name);
            pendingKeyCode      = kc;
            pendingModifierMask = modMask;
            hotkeyDisplayLabel.setText(name);
            // Brief pause so the user sees the captured combo before the dialog closes.
            Timer t = new Timer(600, ev -> dlg.dispose());
            t.setRepeats(false);
            t.start();
        }));

        dlg.setVisible(true); // blocks (modal)
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    private void wireActions() {
        saveBtn.addActionListener(e -> onSave());

        // Reset position: still a local PropertyService write because it's an
        // immediate UI-infrastructure reset, not a user preference.
        resetPositionBtn.addActionListener(e -> {
            PropertyService settings = PropertyService.getInstance();
            settings.remove("X_LOCATION");
            settings.remove("Y_LOCATION");
            JOptionPane.showMessageDialog(panel,
                    "Window position reset. It will take effect on next launch.",
                    "Settings", JOptionPane.INFORMATION_MESSAGE);
        });

        // Subscribe to SETTINGS_SAVED to show the confirmation dialog on the EDT.
        EventBus.subscribe(EventType.SETTINGS_SAVED, event ->
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(panel, "Settings saved.",
                                "Settings", JOptionPane.INFORMATION_MESSAGE)));
    }

    private void onSave() {
        // Build DTO — null fields mean "unchanged this session"
        SettingsDto dto = new SettingsDto(
                (String) formatCombo.getSelectedItem(),
                pendingKeyCode,
                pendingModifierMask);

        // Publish — SettingsController persists; this panel has no PropertyService write
        EventBus.publishAsync(new AppEvent<>(EventType.SAVE_SETTINGS, dto));

        pendingKeyCode      = null; // consumed
        pendingModifierMask = null;
    }

    // ── Widget helpers ────────────────────────────────────────────────────────

    private static JComboBox<String> formatComboBox() {
        JComboBox<String> cb = new JComboBox<>(new String[]{"PNG", "JPEG", "BMP"});
        cb.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return cb;
    }

    private static JLabel hotkeyBadge(String keyName) {
        JLabel lbl = new JLabel(keyName);
        lbl.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        lbl.setForeground(Color.WHITE);
        lbl.setBackground(UIConstants.AW_ACCENT);
        lbl.setOpaque(true);
        lbl.setBorder(new EmptyBorder(3, 10, 3, 10));
        return lbl;
    }

    private static JPanel divider() {
        JPanel d = new JPanel();
        d.setOpaque(false);
        d.setPreferredSize(new Dimension(0, 1));
        d.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        d.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.AW_BORDER));
        return d;
    }

    private static JLabel buildVersionLabel() {
        JLabel lbl = new JLabel("CaptureEasy  v0.0.1");
        lbl.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        lbl.setForeground(UIConstants.AW_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private static void addRow(JPanel card, GridBagConstraints gbc, String labelText, Component widget) {
        int row = gbc.gridy;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 12, 10, 12);
        card.add(PanelComponents.rowLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 12);
        card.add(widget, gbc);
    }

    private static void stretchCard(JPanel card) {
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    private static JPanel transparentPanel() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        return p;
    }

    private static GridBagConstraints baseGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;
        return g;
    }
}
