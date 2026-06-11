package app.captureeasy.core.ui.components;

import app.captureeasy.core.ui.UIConstants;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

/**
 * "Record" placeholder panel shown in the Record tab of ActionWindow.
 *
 * <p>The illustration (camera icon + badge) is painted in a dedicated inner panel
 * that is centred both horizontally and vertically via {@link GridBagLayout} so it
 * stays centred as the user resizes the window.
 *
 * <pre>
 *  ┌─────────────────────────────────────────┐ NORTH  (fixed height header)
 *  │  Screen Recording                       │
 *  │  Capture your screen as a video         │
 *  ├─────────────────────────────────────────┤ CENTER (illustration, centred)
 *  │                                         │
 *  │            ┌─────────┐                  │
 *  │            │  🎥  ●  │  COMING SOON     │
 *  │            └─────────┘                  │
 *  │     Screen recording — coming in a      │
 *  │            future version               │
 *  │                                         │
 *  └─────────────────────────────────────────┘
 * </pre>
 */
public class RecordPanel {

    private static final int ICON_SIZE = 72;

    private final JPanel panel;

    private RecordPanel() {
        panel = buildPanel();
    }

    public static RecordPanel init() { return new RecordPanel(); }

    public JPanel getPanel() { return panel; }

    // ── Build ─────────────────────────────────────────────────────────────────

    private JPanel buildPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.AW_BG);

        // NORTH — header
        root.add(PanelComponents.header(
                "Screen Recording",
                "Capture your screen as a video"), BorderLayout.NORTH);

        // CENTER — illustration centred in available space
        JPanel centreOuter = new JPanel(new GridBagLayout());
        centreOuter.setBackground(UIConstants.AW_BG);

        JPanel illustration = buildIllustration();
        centreOuter.add(illustration, new GridBagConstraints());  // default: centred

        root.add(centreOuter, BorderLayout.CENTER);
        return root;
    }

    // ── Illustration ──────────────────────────────────────────────────────────

    private JPanel buildIllustration() {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(UIConstants.AW_BG);
        wrapper.setOpaque(false);

        // Camera icon (custom-painted square)
        JPanel icon = buildCameraIcon();
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(icon);
        wrapper.add(Box.createRigidArea(new Dimension(0, 20)));

        // Primary message
        JLabel title = new JLabel("Coming in a future version");
        title.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        title.setForeground(UIConstants.AW_TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(title);
        wrapper.add(Box.createRigidArea(new Dimension(0, 6)));

        // Subtitle
        JLabel sub = new JLabel("Screen recording will be available in an upcoming update.");
        sub.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        sub.setForeground(UIConstants.AW_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        wrapper.add(sub);

        return wrapper;
    }

    private JPanel buildCameraIcon() {
        final int S = ICON_SIZE;
        return new JPanel() {
            {
                setOpaque(false);
                setPreferredSize(new Dimension(S + 20, S + 20));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int ox = (getWidth() - S) / 2;
                int oy = (getHeight() - S) / 2;

                // Background circle
                g2.setColor(new Color(241, 248, 244));
                g2.fillOval(ox - 8, oy - 8, S + 16, S + 16);
                g2.setColor(UIConstants.AW_ACCENT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(ox - 8, oy - 8, S + 15, S + 15);

                // Camera body
                g2.setColor(UIConstants.AW_DARK);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int cx = ox + 6, cy = oy + 16, cw = S - 12, ch = S - 28;
                g2.drawRoundRect(cx, cy, cw, ch, 10, 10);

                // Lens
                int lx = ox + S / 2 - 12, ly = oy + 22;
                g2.setColor(new Color(52, 152, 219));
                g2.fillOval(lx, ly, 24, 24);
                g2.setColor(Color.WHITE);
                g2.fillOval(lx + 7, ly + 5, 7, 7);

                // Viewfinder bump
                g2.setColor(UIConstants.AW_DARK);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(ox + 18, oy + 10, 14, 8, 4, 4);

                // Record dot (red, top-right)
                g2.setColor(new Color(231, 76, 60));
                g2.fillOval(ox + S - 18, oy + 8, 12, 12);

                // COMING SOON badge
                g2.setColor(UIConstants.AW_ACCENT);
                g2.fillRoundRect(ox, oy + S - 4, S, 18, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 9));
                String badge = "COMING SOON";
                int bw = g2.getFontMetrics().stringWidth(badge);
                g2.drawString(badge, ox + (S - bw) / 2, oy + S + 9);

                g2.dispose();
            }
        };
    }
}
