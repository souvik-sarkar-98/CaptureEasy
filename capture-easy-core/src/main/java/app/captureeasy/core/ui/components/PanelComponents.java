package app.captureeasy.core.ui.components;

import app.captureeasy.core.ui.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Package-private factory for common styled Swing components used across all
 * ActionWindow panels. Centralising these helpers keeps the three panel classes
 * focused on layout and logic instead of rendering boilerplate.
 */
final class PanelComponents {

    private PanelComponents() {}

    // ── Header ────────────────────────────────────────────────────────────────

    /**
     * Creates the dark header strip shared by every panel.
     * Height is fixed to {@link UIConstants#AW_HEADER_H}; width stretches with the window.
     */
    static JPanel header(String title, String subtitle) {
        final int ht = UIConstants.AW_HEADER_H;
        JPanel h = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UIConstants.AW_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(UIConstants.AW_ACCENT);
                g2.fillRect(0, getHeight() - 3, getWidth(), 3);
                g2.dispose();
            }
        };
        h.setLayout(new javax.swing.BoxLayout(h, javax.swing.BoxLayout.Y_AXIS));
        h.setBorder(new EmptyBorder(11, 20, 8, 20));
        h.setPreferredSize(new Dimension(0, ht));
        h.setMinimumSize(new Dimension(0, ht));
        h.setMaximumSize(new Dimension(Integer.MAX_VALUE, ht));

        JLabel t = new JLabel(title);
        t.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        t.setForeground(Color.WHITE);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(t);
        h.add(Box.createRigidArea(new Dimension(0, 3)));

        JLabel s = new JLabel(subtitle);
        s.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        s.setForeground(new Color(189, 195, 199));
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        h.add(s);

        return h;
    }

    // ── Section card ──────────────────────────────────────────────────────────

    /**
     * Creates a white card panel with a titled border.
     * Intended to be added to a {@code BoxLayout.Y_AXIS} content panel.
     * The caller is responsible for setting an appropriate preferred size.
     */
    static JPanel sectionCard(String title) {
        JPanel card = new JPanel(new java.awt.GridBagLayout());
        card.setBackground(UIConstants.AW_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.AW_BORDER),
                "  " + title + "  ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font(UIConstants.FONT_FAMILY, Font.BOLD, 11),
                UIConstants.AW_MUTED));
        return card;
    }

    // ── Form helpers ──────────────────────────────────────────────────────────

    static JLabel rowLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        lbl.setForeground(UIConstants.AW_TEXT);
        return lbl;
    }

    static JLabel sectionHeading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 11));
        lbl.setForeground(UIConstants.AW_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    static JTextField styledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        tf.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(UIConstants.AW_BORDER),
                new EmptyBorder(5, 8, 5, 8)));
        tf.setBackground(new Color(252, 253, 254));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return tf;
    }

    static JLabel hintLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font(UIConstants.FONT_FAMILY, Font.ITALIC, 11));
        lbl.setForeground(UIConstants.AW_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ── Buttons ───────────────────────────────────────────────────────────────

    static JButton primaryButton(String text) {
        return filledButton(text, UIConstants.BTN_PRIMARY, Font.BOLD);
    }

    static JButton secondaryButton(String text) {
        return filledButton(text, UIConstants.BTN_SECONDARY, Font.PLAIN);
    }

    /**
     * Outline (ghost) button: white fill, coloured border and text, fills with
     * accent colour on press.
     */
    static JButton outlineButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(UIConstants.AW_ACCENT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    Color border = getModel().isRollover() ? UIConstants.AW_ACCENT : UIConstants.AW_BORDER;
                    g2.setColor(border);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                    g2.setColor(getModel().isRollover() ? UIConstants.AW_ACCENT : UIConstants.AW_TEXT);
                }
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        applyBase(btn);
        btn.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        return btn;
    }

    // ── Footer row ────────────────────────────────────────────────────────────

    /**
     * Creates a bottom footer panel (flush to window edge, right-aligned buttons,
     * with a top divider line).
     */
    static JPanel footer() {
        JPanel f = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 10));
        f.setBackground(UIConstants.AW_BG);
        f.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.AW_BORDER));
        return f;
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private static JButton filledButton(String text, Color base, int style) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? base.darker()
                        : getModel().isRollover() ? base.brighter() : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        applyBase(btn);
        btn.setFont(new Font(UIConstants.FONT_FAMILY, style, 13));
        return btn;
    }

    private static void applyBase(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
