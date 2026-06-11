package app.captureeasy.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.captureeasy.common.util.SystemUtil;
import app.captureeasy.core.ui.components.RecordPanel;
import app.captureeasy.core.ui.components.SavePanel;
import app.captureeasy.core.ui.components.SettingsPanel;

/**
 * @author Souvik Sarkar
 * @createdOn 04-Jun-2022
 * @purpose Secondary dialog window with tabs for Save, View, Record, and Settings.
 */
public class ActionWindow implements MouseListener, MouseMotionListener, ChangeListener {

    public static final int SAVE = 0, VIEW = 1, RECORD = 2, SETTINGS = 3;

    /** Instance field — was incorrectly static, which broke re-open after dispose. */
    private JFrame dialog;
    private JTabbedPane tabbledPanel;
    private final Runnable onClose;

    private static final String PRE_HTML  = "<html><p style=\"text-align:center;margin:10px 1px;width:70px\">";
    private static final String POST_HTML = "</p></html>";
    private static final String TASKBAR_ICON = "/icons/taskbar_icon.png";

    private int xDialog, yDialog, xyDialog, xxDialog;

    public ActionWindow(Runnable onClose, int... codes) {
        this.onClose = onClose;
        initGUI();
        for (int code : codes) {
            switch (code) {
                case SAVE:     initSaveTab();     break;
                case VIEW:     initViewTab();     break;
                case RECORD:   initRecordTab();   break;
                case SETTINGS: initSettingsTab(); break;  // was case 4 — off-by-one fixed
            }
        }
    }

    public static ActionWindow init(Runnable onClose, int... codes) {
        return new ActionWindow(onClose, codes);
    }

    // ── Tab initialisation ────────────────────────────────────────────────────

    private void initSaveTab() {
        if (tabbledPanel == null) return;
        SavePanel sp = SavePanel.init(dialog);
        tabbledPanel.addTab("Save", null, sp.getPanel(), null);
        tabbledPanel.setTitleAt(tabbledPanel.getTabCount() - 1, PRE_HTML + "Save" + POST_HTML);
    }

    private void initViewTab() {
        if (tabbledPanel == null) return;
        SavePanel sp = SavePanel.init(dialog);
        tabbledPanel.addTab("View", null, sp.getPanel(), null);
        tabbledPanel.setTitleAt(tabbledPanel.getTabCount() - 1, PRE_HTML + "View" + POST_HTML);
    }

    private void initRecordTab() {
        if (tabbledPanel == null) return;
        RecordPanel rp = RecordPanel.init();
        tabbledPanel.addTab("Record", null, rp.getPanel(), null);
        tabbledPanel.setTitleAt(tabbledPanel.getTabCount() - 1, PRE_HTML + "Record" + POST_HTML);
    }

    private void initSettingsTab() {
        if (tabbledPanel == null) return;
        SettingsPanel sp = SettingsPanel.init(dialog);
        tabbledPanel.addTab("Settings", null, (java.awt.Component) sp.getPanel(), null);
        tabbledPanel.setTitleAt(tabbledPanel.getTabCount() - 1, PRE_HTML + "Settings" + POST_HTML);
    }

    // ── GUI initialisation ────────────────────────────────────────────────────

    private void initGUI() {
        dialog = new JFrame();
        dialog.setName("Action_Window");
        dialog.setSize(UIConstants.ACTION_WINDOW_SIZE);
        dialog.setMinimumSize(new Dimension(480, 380));
        dialog.setFont(new Font("Dialog", Font.BOLD, 20));
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int cx = SystemUtil.getScreenSize().width  / 2 - UIConstants.ACTION_WINDOW_SIZE.width  / 2;
        int cy = SystemUtil.getScreenSize().height / 2 - UIConstants.ACTION_WINDOW_SIZE.height / 2;
        dialog.setLocation(cx, cy);

        List<Image> icons = new ArrayList<>();
        try {
            icons.add(ImageIO.read(this.getClass().getResource(TASKBAR_ICON)));
            dialog.setIconImages(icons);
        } catch (IOException ignored) {}

        dialog.addMouseListener(this);
        dialog.addMouseMotionListener(this);

        // Notify ControlWindow when this dialog is closed so it can re-enable screenshot capture.
        if (onClose != null) {
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    onClose.run();
                }
            });
        }

        // Use BorderLayout so the tabbed pane grows with the window.
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(initTabbedPane(JTabbedPane.LEFT), BorderLayout.CENTER);
    }

    private JTabbedPane initTabbedPane(int position) {
        tabbledPanel = new JTabbedPane(position);
        tabbledPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        tabbledPanel.setBackground(Color.WHITE);
        tabbledPanel.setOpaque(true);
        tabbledPanel.setAutoscrolls(true);
        tabbledPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbledPanel.setFont(new Font("Tahoma", Font.BOLD, 16));
        tabbledPanel.addMouseListener(this);
        tabbledPanel.addMouseMotionListener(this);
        return tabbledPanel;
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public JFrame show() {
        if (dialog.isVisible()) {
            dialog.toFront();
            return dialog;
        }
        dialog.setVisible(true);
        if (tabbledPanel != null && tabbledPanel.getTabCount() > 0) {
            tabbledPanel.setSelectedIndex(0);
        }
        return dialog;
    }

    // ── Mouse drag (window repositioning) ─────────────────────────────────────

    @Override
    public void mouseDragged(MouseEvent e) {
        xDialog = e.getXOnScreen();
        yDialog = e.getYOnScreen();
        dialog.setLocation(xDialog - xxDialog, yDialog - xyDialog);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        xxDialog = e.getX();
        xyDialog = e.getY();
    }

    @Override public void stateChanged(ChangeEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
}
