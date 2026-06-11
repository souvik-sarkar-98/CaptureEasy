package app.captureeasy.core.ui.components;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.core.eventdto.SaveConfig;
import app.captureeasy.core.ui.UIConstants;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;

/**
 * "Export Screenshots" panel for the Save tab in ActionWindow.
 *
 * <p>Layout — fully responsive to window resize:
 * <pre>
 *  ┌─────────────────────────────────────────┐ NORTH  (fixed height, custom-painted header)
 *  │  Export Screenshots                     │
 *  │  Save all screenshots to a Word doc     │
 *  ├─────────────────────────────────────────┤ CENTER (BoxLayout.Y_AXIS, stretches)
 *  │  SELECT FORMAT                          │
 *  │  [ ○ New Word document             ]    │ ← radio cards stretch horizontally
 *  │  [ ○ Append to existing document   ]    │
 *  │  ┌─ Output settings ──────────────────┐ │ ← CardLayout swaps on radio change
 *  │  │  Destination folder: [__________] │ │
 *  │  │  Filename:           [__________] │ │
 *  │  └────────────────────────────────────┘ │
 *  │  ℹ Screenshots are exported …          │
 *  ├─────────────────────────────────────────┤ SOUTH  (button footer)
 *  │                       [Cancel] [Export] │
 *  └─────────────────────────────────────────┘
 * </pre>
 */
public class SavePanel {

    private final JPanel panel;

    // radio buttons
    private final JRadioButton rdbtnNewDoc;
    private final JRadioButton rdbtnExDoc;

    // "New document" input fields
    private final JTextField textNewFolder;
    private final JTextField textNewFilename;

    // "Existing document" input field
    private final JTextField textExistingFile;

    // input card panels — held as fields so activeBorder() can reach them
    private final JPanel newDocCard;
    private final JPanel existingDocCard;

    // footer action buttons
    private final JButton btnOkay;
    private final JButton btnCancel;

    private final JFrame parent;

    private SavePanel(JFrame parent) {
        this.parent = parent;

        // Radio buttons
        rdbtnNewDoc = radioBtn("Create a new Word document (.docx)");
        rdbtnExDoc  = radioBtn("Append to an existing Word document");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdbtnNewDoc);
        bg.add(rdbtnExDoc);
        rdbtnNewDoc.setSelected(true);

        // Input fields
        textNewFolder    = PanelComponents.styledTextField();
        textNewFilename  = PanelComponents.styledTextField();
        textExistingFile = PanelComponents.styledTextField();

        // Radio option cards (styled containers)
        newDocCard      = radioCard(rdbtnNewDoc, true);
        existingDocCard = radioCard(rdbtnExDoc, false);

        // Footer buttons
        btnOkay   = PanelComponents.primaryButton("Export");
        btnCancel = PanelComponents.secondaryButton("Cancel");

        panel = buildPanel();
        wireActions();
    }

    public static SavePanel init(JFrame parent) { return new SavePanel(parent); }

    public JPanel getPanel() { return panel; }

    // ── Build ─────────────────────────────────────────────────────────────────

    private JPanel buildPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.AW_BG);

        // NORTH — header
        root.add(PanelComponents.header(
                "Export Screenshots",
                "Save all session screenshots to a Word document"), BorderLayout.NORTH);

        // CENTER — scrollable content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UIConstants.AW_BG);
        content.setBorder(new EmptyBorder(16, 20, 8, 20));

        // Format heading
        content.add(PanelComponents.sectionHeading("SELECT EXPORT FORMAT"));
        content.add(Box.createRigidArea(new Dimension(0, 8)));

        // Radio option 1
        content.add(newDocCard);
        content.add(Box.createRigidArea(new Dimension(0, 6)));

        // Radio option 2
        content.add(existingDocCard);
        content.add(Box.createRigidArea(new Dimension(0, 14)));

        // Input section — CardLayout switches between "new doc" and "existing doc"
        JPanel inputWrapper = new JPanel(new CardLayout());
        inputWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        inputWrapper.setPreferredSize(new Dimension(400, 160));
        inputWrapper.add(buildNewDocInputs(), "NEW");
        inputWrapper.add(buildExistingDocInputs(), "EXISTING");
        content.add(inputWrapper);

        // Wire radio buttons to card switching
        rdbtnNewDoc.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                ((CardLayout) inputWrapper.getLayout()).show(inputWrapper, "NEW");
                activeBorder(newDocCard, true);
                activeBorder(existingDocCard, false);
            }
        });
        rdbtnExDoc.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                ((CardLayout) inputWrapper.getLayout()).show(inputWrapper, "EXISTING");
                activeBorder(existingDocCard, true);
                activeBorder(newDocCard, false);
            }
        });

        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(PanelComponents.hintLabel("Screenshots are exported in capture order, one image per page."));
        content.add(Box.createVerticalGlue());

        root.add(content, BorderLayout.CENTER);

        // SOUTH — button footer
        JPanel footer = PanelComponents.footer();
        btnCancel.setPreferredSize(new Dimension(100, 34));
        btnOkay.setPreferredSize(new Dimension(120, 34));
        footer.add(btnCancel);
        footer.add(btnOkay);
        root.add(footer, BorderLayout.SOUTH);

        return root;
    }

    // ── Input cards ───────────────────────────────────────────────────────────

    private JPanel buildNewDocInputs() {
        JPanel card = PanelComponents.sectionCard("Output settings");
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        GridBagConstraints gbc = baseGbc();

        // Row 0: folder label (spans both columns)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 12, 2, 12);
        card.add(PanelComponents.rowLabel("Destination folder:"), gbc);

        // Row 1: folder field + browse
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.insets = new Insets(0, 12, 6, 4);
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(textNewFolder, gbc);

        gbc.gridx = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 6, 12);
        JButton browseFolder = browseButton();
        browseFolder.addActionListener(e -> chooseFolder(textNewFolder));
        card.add(browseFolder, gbc);

        // Row 2: filename label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 12, 2, 12);
        card.add(PanelComponents.rowLabel("Filename (without .docx extension):"), gbc);

        // Row 3: filename field
        gbc.gridy = 3; gbc.insets = new Insets(0, 12, 8, 12);
        card.add(textNewFilename, gbc);

        // Row 4: spacer — pushes rows to the top when card stretches
        gbc.gridy = 4; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 2;
        card.add(new JPanel() {{ setOpaque(false); }}, gbc);

        return card;
    }

    private JPanel buildExistingDocInputs() {
        JPanel card = PanelComponents.sectionCard("Output settings");
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        GridBagConstraints gbc = baseGbc();

        // Row 0: file label
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.weightx = 1.0;
        gbc.insets = new Insets(8, 12, 2, 12);
        card.add(PanelComponents.rowLabel("Select existing Word document (.docx):"), gbc);

        // Row 1: file field + browse
        gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 12, 8, 4);
        gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        card.add(textExistingFile, gbc);

        gbc.gridx = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 0, 8, 12);
        JButton browseFile = browseButton();
        browseFile.addActionListener(e -> chooseDocxFile(textExistingFile));
        card.add(browseFile, gbc);

        // Row 2: spacer
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        card.add(new JPanel() {{ setOpaque(false); }}, gbc);

        return card;
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    private void wireActions() {
        btnOkay.addActionListener(e -> onExport());
        btnCancel.addActionListener(e -> {
            if (parent != null) parent.dispose();
        });
    }

    private void onExport() {
        if (rdbtnNewDoc.isSelected()) {
            String folder   = textNewFolder.getText().trim();
            String filename = textNewFilename.getText().trim();
            if (folder.isEmpty())   { warn("Please specify a destination folder."); return; }
            if (filename.isEmpty()) { warn("Please enter a filename."); return; }
            dispatch(new SaveConfig(SaveConfig.Format.NEW_WORD, folder, filename));
        } else {
            String filepath = textExistingFile.getText().trim();
            if (filepath.isEmpty()) { warn("Please select an existing Word document."); return; }
            File f = new File(filepath);
            if (!f.isFile()) { warn("The selected file does not exist."); return; }
            dispatch(new SaveConfig(SaveConfig.Format.EXISTING_WORD,
                    f.getParent(), f.getName().replaceFirst("\\.docx$", "")));
        }
        if (parent != null) parent.dispose();
    }

    private void dispatch(SaveConfig config) {
        EventBus.publishAsync(new AppEvent<>(EventType.SAVE_SCREENSHOTS, config));
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(panel, msg, "Validation", JOptionPane.WARNING_MESSAGE);
    }

    // ── File choosers ─────────────────────────────────────────────────────────

    private void chooseFolder(JTextField target) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            target.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    private void chooseDocxFile(JTextField target) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Word documents (*.docx)", "docx"));
        if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            target.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }

    // ── Widget helpers ────────────────────────────────────────────────────────

    private static JPanel radioCard(JRadioButton btn, boolean selected) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIConstants.AW_CARD);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.setPreferredSize(new Dimension(400, 44));
        activeBorder(card, selected);
        btn.setBackground(UIConstants.AW_CARD);
        btn.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(0, 12, 0, 12));
        card.add(btn, BorderLayout.CENTER);
        return card;
    }

    private static void activeBorder(JPanel card, boolean active) {
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(
                        active ? UIConstants.AW_ACCENT : UIConstants.AW_BORDER,
                        active ? 2 : 1),
                new EmptyBorder(0, 0, 0, 0)));
    }

    private static JRadioButton radioBtn(String text) {
        JRadioButton r = new JRadioButton(text);
        r.setOpaque(false);
        return r;
    }

    private static JButton browseButton() {
        JButton btn = PanelComponents.outlineButton("Browse\u2026");
        btn.setPreferredSize(new Dimension(88, 30));
        return btn;
    }

    private static GridBagConstraints baseGbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        return g;
    }
}
