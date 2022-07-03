package app.techy10souvik.captureeasy.common.ui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.SystemColor;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlertPopup extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int ERROR = 0;
	public static final int INFO = 1;
	public static final int WARNING = 3;
	public static final int COMMENT = 4;

	private static final String errorIcon = "/icons/error.png";

	private static final String warningIcon = "/icons/warning.png";

	private static final String commentIcon = "/icons/comment.png";

	private static final String infoIcon = "/icons/information.png";

	int xx, xy;
//	public static boolean control=true;
//	public static boolean decision;
	private JButton btnAction1;
	private JButton btnAction2;
	private JTextArea messageBox;
	private JLabel lblIcon;
	private JLabel lblTitle;
	// private JDialog PopDia;

	public AlertPopup() {
		initGUI();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setVisible(true);
			}
		});
	}

	public static AlertPopup init() {
		return new AlertPopup();
	}

	public AlertPopup type(int type) {
		Dimension size = lblIcon.getSize();

		try {
			if (type == AlertPopup.ERROR) {
				lblTitle.setText("ERROR");
				lblIcon.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(errorIcon))
						.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH)));
				messageBox.setForeground(Color.RED);
				messageBox.setBackground(new Color(255, 255, 204));
				messageBox.setEditable(false);
				messageBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				messageBox.setFocusable(false);
			} else if (type == AlertPopup.WARNING) {
				lblTitle.setText("WARNING");
				lblIcon.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(warningIcon))
						.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH)));
				messageBox.setForeground(Color.BLACK);
				messageBox.setBackground(new Color(255, 255, 204));
				messageBox.setEditable(false);
				messageBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				messageBox.setFocusable(false);
			} else if (type == AlertPopup.INFO) {
				lblTitle.setText("INFO");
				lblIcon.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(infoIcon))
						.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH)));
				messageBox.setForeground(Color.BLACK);
				messageBox.setBackground(new Color(255, 255, 204));
				messageBox.setEditable(false);
				messageBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				messageBox.setFocusable(false);
			} else if (type == AlertPopup.COMMENT) {
				lblTitle.setText("COMMENT");
				lblIcon.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(commentIcon))
						.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH)));
				messageBox.setForeground(Color.BLACK);
				messageBox.setFocusable(true);
				messageBox.setEditable(true);
				messageBox.setEnabled(true);
				messageBox.setBackground(Color.WHITE);
				messageBox.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				messageBox.requestFocusInWindow();
			}

		} catch (Exception e) {
			// Library.logError(e,"Exception on reading image in POPUP");
		}
		return this;
	}

	public AlertPopup message(String message) {
		int len = message.length();
		String tooltipText = "<html>";
		if (len > 50) {
			int i;
			for (i = 0; i < len / 50; ++i) {
				tooltipText = String.valueOf(tooltipText) + message.substring(i * 50, 50 * (i + 1)) + "<br>";
			}
			tooltipText += message.substring(i * 50);
		}
		tooltipText += "</html>";

		messageBox.setToolTipText(tooltipText);
		if (len > 140) {
			message = message.substring(0, 140) + " " + "...";
		}
		messageBox.setText(message);
		return this;
	}

	public AlertPopup button1(String name, ActionListener act1) {
		btnAction1.setText(name);
		btnAction1.addActionListener(act1);
		btnAction1.setVisible(true);
		return this;
	}

	public AlertPopup button2(String name, ActionListener act2) {
		btnAction2.setText(name);
		btnAction2.addActionListener(act2);
		btnAction2.setVisible(true);
		return this;
	}

	public AlertPopup button1(String name) {
		btnAction1.setText(name);
		btnAction1.setVisible(true);
		return this;
	}

	public AlertPopup button2(String name) {
		btnAction2.setText(name);
		btnAction2.setVisible(true);
		return this;
	}

	public void initGUI() {
		setResizable(false);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		setUndecorated(true);
		setAlwaysOnTop(true);
		setLocation(screensize.width / 2 - 300, screensize.height / 2 - 300);
		setBounds(screensize.width / 2 - 300, screensize.height / 2 - 300, 425, 225);
		getContentPane().setLayout(null);
		addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				int x = arg0.getXOnScreen();
				int y = arg0.getYOnScreen();
				setLocation(x - xx, y - xy);
				setBounds(x - xx, y - xy, 425, 225);
			}
		});

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(0, 0, 0)));
		mainPanel.setBounds(0, 0, 425, 225);
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.add(initNorthPanel(), BorderLayout.NORTH);
		mainPanel.add(initSouthPanel(), BorderLayout.SOUTH);
		mainPanel.add(new JPanel(), BorderLayout.EAST);
		mainPanel.add(new JPanel(), BorderLayout.WEST);
		mainPanel.add(initCenterPanel(), BorderLayout.CENTER);
		getContentPane().add(mainPanel);
		//setVisible(true);
		try {
			getRootPane().setDefaultButton(btnAction1);
		} catch (Exception e) {
		}
	}

	private JPanel initNorthPanel() {
		JPanel northPanel = new JPanel();
		lblTitle = new JLabel();
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 16));
		northPanel.add(lblTitle);
		return northPanel;
	}

	private JPanel initSouthPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setBackground(SystemColor.menu);

		btnAction1 = new JButton();
		btnAction1.setToolTipText("Click");
		btnAction1.setSelected(true);
		btnAction1.setBackground(new Color(204, 204, 255));
		btnAction1.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAction1.setVisible(false);
		southPanel.add(btnAction1);

		btnAction2 = new JButton();
		btnAction2.setBackground(new Color(204, 204, 255));
		btnAction2.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnAction2.setVisible(false);
		btnAction2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		southPanel.add(btnAction2);
		return southPanel;
	}

	private JPanel initCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setLayout(null);

		JPanel subCenterPanel = new JPanel();
		subCenterPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		subCenterPanel.setBackground(new Color(255, 255, 204));
		subCenterPanel.setBounds(12, 13, 377, 125);
		centerPanel.add(subCenterPanel);
		lblIcon = new JLabel("Icon");
		lblIcon.setBounds(10, 35, 50, 50);
		subCenterPanel.setLayout(null);
		subCenterPanel.add(lblIcon);

		JPanel messagePanel = new JPanel();
		messagePanel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		messagePanel.setBackground(new Color(255, 255, 204));
		messagePanel.setBounds(75, 13, 285, 110);

		messageBox = new JTextArea();
		messageBox.setBounds(0, 0, 285, 100);
		messageBox.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
			}

			public void insertUpdate(DocumentEvent e) {
				if (messageBox.isEditable())
					messageBox.setToolTipText(messageBox.getText());
			}

			public void removeUpdate(DocumentEvent e) {
				if (messageBox.isEditable())
					messageBox.setToolTipText(messageBox.getText());
			}
		});
		messageBox.setRows(5);
		messageBox.setLineWrap(true);
		messageBox.setFont(new Font("Tahoma", Font.BOLD, 16));
		messageBox.setColumns(18);
		messagePanel.setLayout(null);
		messagePanel.add(messageBox);
		subCenterPanel.add(messagePanel);

		return centerPanel;
	}

}
