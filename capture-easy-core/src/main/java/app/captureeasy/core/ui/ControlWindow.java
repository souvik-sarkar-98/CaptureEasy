package app.captureeasy.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.common.ui.SystemNativeDialog;
import app.captureeasy.core.enums.TriggerSource;
import org.jnativehook.mouse.SwingMouseAdapter;

/**
 * @author Souvik Sarkar
 * @createdOn 04-Jun-2022
 * @purpose
 */
public class ControlWindow {

	private static final String menuIcon = "/icons/menu.png";
	private static final String taskbarIcon = "/icons/taskbar_icon.png";
	private static final String pauseIcon = "/icons/pause.png";
	private static final String viewIcon = "/icons/view.png";
	private static final String deleteIcon = "/icons/delete.png";
	private static final String settingsIcon = "/icons/settings.png";
	private static final String recordIcon = "/icons/record.png";
	private static final String playIcon = "/icons/play.png";
	private static final String powerIcon="/icons/power.png";
	private static final String saveIcon = "/icons/save.png";

	private JFrame frame;
	private int xy;
	private int xx;
	private int x;
	private int y;
	private JPanel mainPanel;
	private JLabel label_Count;
	private JPanel clickPad;
	private JButton menuButton;
	private JPanel controlPanel;
	private JButton pauseButton;
	private JButton powerButton;
	private JButton viewButton;
	private JButton saveButton;
	private JButton deleteButton;
	private JButton settingsButton;
	private JButton recordButton;
	private boolean isActionWindowOpened;

	/**
	 * @param
	 * @return 
	 * @throws Exception 
	 * @throws IOException
	 * @throws
	 * 
	 */
	
	private ControlWindow() throws Exception  {
		initGUI();
		registerClickPadAction();
		registerMenuButtonAction();
		registerPauseButtonAction();
		registerPowerButtonAction();
		registerDeleteButtonAction();
		registerViewButtonAction();
		registerSaveButtonAction();
		registerRecordButtonAction();
		registerSettingsButtonAction();
	}


	public static ControlWindow init() throws Exception  {
		return new ControlWindow();
	}
	
	
	public JFrame show() {
		frame.setVisible(true);
		return frame;
	}

	private void initGUI() throws Exception  {
		frame = new JFrame();
		frame.setName("Control_Window");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(taskbarIcon));
		frame.setSize(new Dimension(UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_COLLAPSED_HEIGHT));
		frame.setAlwaysOnTop(true);
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(this.getClass().getResource(taskbarIcon)));
			frame.setIconImages(icons);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		frame.setLocation(PropertyService.getInstance().getGUILocation());
		frame.setAlwaysOnTop(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.getContentPane().add(initMainPanel());

	}

	private JPanel initMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_COLLAPSED_HEIGHT);
		mainPanel.setBorder(null);
		mainPanel.setLayout(null);
		mainPanel.setBackground(new Color(0, 0, 0, 0));
		mainPanel.add(initClickPanel());
		mainPanel.add(initMenuButton());
		mainPanel.add(initControlPanel());
		return mainPanel;
	}

	private JButton initMenuButton() {
		menuButton = new JButton();
		menuButton.setBounds(2, UIConstants.MENU_BUTTON_Y,
				UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height);
		menuButton.setBackground(new Color(0, 0, 0, 0));
		menuButton.setBorderPainted(false);
		menuButton.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
		try {
			menuButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(menuIcon))
					.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
		} catch (IOException e4) {
			menuButton.setText("Menu");
		}
		return menuButton;
	}

	private JPanel initClickPanel() {
		clickPad = new JPanel();
		clickPad.setBackground(new Color(51, 255, 153));
		clickPad.setToolTipText("Click here to take screenshots");
		clickPad.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
		clickPad.setBounds(0, 0, UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CLICK_PAD_HEIGHT);
		clickPad.setLayout(new FlowLayout(1, 5, 5));

		label_Count = new JLabel();
		label_Count.setFont(new Font("Tahoma", 1, 20));
		clickPad.add(label_Count);
		label_Count.setText("0");

		clickPad.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent arg0) {
				x = arg0.getXOnScreen() - xx;
				y = arg0.getYOnScreen() - xy;
				frame.setLocation(x, y);
			}
		});
		clickPad.addMouseListener(new SwingMouseAdapter() {
			public static final long serialVersionUID = 1L;

			@Override
			public void mousePressed(final MouseEvent arg0) {
				xx = arg0.getX();
				xy = arg0.getY();
			}
		});
		
		return clickPad;
	}

	private JPanel initControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setBorder(null);
		controlPanel.setBackground(Color.WHITE);
		controlPanel.setBounds(0, 115, 54, 500);
		controlPanel.setLayout(null);
		controlPanel.setBackground(new Color(0, 0, 0, 0));
		controlPanel.add(initPowerButton());
		controlPanel.add(initPauseButton());
		controlPanel.add(initDeleteButton());
		controlPanel.add(initSaveButton());
		controlPanel.add(initViewButton());
		controlPanel.add(initRecordingButton());
		controlPanel.add(initSettingsButton());
		controlPanel.setVisible(false);
		return controlPanel;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initSettingsButton() {
		settingsButton = iconButton(settingsIcon, "Click Here for settings", UIConstants.SETTINGS_BUTTON_Y);
		if (settingsButton.getIcon() == null) settingsButton.setText("Settings");
		return settingsButton;
	}

	private JButton initRecordingButton() {
		recordButton = iconButton(recordIcon, "Click here to record screen", UIConstants.RECORD_BUTTON_Y);
		if (recordButton.getIcon() == null) recordButton.setText("Record");
		return recordButton;
	}

	private JButton initViewButton() {
		viewButton = iconButton(viewIcon, "Click here to view screenshots", UIConstants.VIEW_BUTTON_Y);
		if (viewButton.getIcon() == null) viewButton.setText("View");
		return viewButton;
	}

	private JButton initSaveButton() {
		saveButton = iconButton(saveIcon, "Click here to Save", UIConstants.SAVE_BUTTON_Y);
		saveButton.setName("SAVE");
		if (saveButton.getIcon() == null) saveButton.setText("Save");
		return saveButton;
	}

	private JButton initDeleteButton() {
		deleteButton = iconButton(deleteIcon, "Click Here to Delete", UIConstants.DELETE_BUTTON_Y);
		if (deleteButton.getIcon() == null) deleteButton.setText("Delete");
		return deleteButton;
	}

	private JButton initPauseButton() {
		pauseButton = new JButton();
		pauseButton.setBorderPainted(false);
		pauseButton.setToolTipText("Click Here to Pause");
		pauseButton.setLocation(UIConstants.BUTTON_X, UIConstants.PAUSE_BUTTON_Y);
		pauseButton.setSize(UIConstants.ICON_BUTTON_SIZE);
		pauseButton.setBackground(new Color(0, 0, 0, 0));
		try {
			pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(pauseIcon))
					.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
		} catch (IOException e) {
			pauseButton.setText("Pause");
		}
		return pauseButton;
	}

	private JButton initPowerButton() {
		powerButton = new JButton();
		powerButton.setBounds(2, UIConstants.POWER_BUTTON_Y,
				UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height);
		powerButton.setBackground(new Color(0, 0, 0, 0));
		powerButton.setBorderPainted(false);
		powerButton.setBorder(null);
		powerButton.setToolTipText("Click here to exit application");
		try {
			powerButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(powerIcon))
					.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
		} catch (IOException e) {
			powerButton.setText("Close");
		}
		return powerButton;
	}

	private JButton iconButton(String iconPath, String tooltip, int yPos) {
		JButton btn = new JButton();
		btn.setBounds(UIConstants.BUTTON_X, yPos,
				UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height);
		btn.setBackground(new Color(0, 0, 0, 0));
		btn.setBorderPainted(false);
		btn.setToolTipText(tooltip);
		try {
			btn.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(iconPath))
					.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
		} catch (IOException e) {
			// fallback text set by caller
		}
		return btn;
	}

	private void registerPauseButtonAction() {
		pauseButton.addActionListener(e -> {
			if (pauseButton.getToolTipText().equalsIgnoreCase("Click Here to Pause")) {
				menuButton.setEnabled(false);
				try {
					pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(playIcon))
							.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
				} catch (IOException ex) {
					pauseButton.setText("Resume");
				}
				pauseButton.setToolTipText("Click Here to Resume");
				menuButton.setToolTipText("");
			} else {
				menuButton.setEnabled(true);
				menuButton.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
				try {
					pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(pauseIcon))
							.getScaledInstance(UIConstants.ICON_BUTTON_SIZE.width, UIConstants.ICON_BUTTON_SIZE.height, UIConstants.ICON_SCALE_HINT)));
				} catch (IOException ex) {
					pauseButton.setText("Pause");
				}
				pauseButton.setToolTipText("Click Here to Pause");
			}
		});
	}

	private void registerClickPadAction() {
		clickPad.addMouseListener(new SwingMouseAdapter() {
			public static final long serialVersionUID = 1L;
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				if (!isActionWindowOpened) {
					EventBus.publish(new AppEvent<>(EventType.TAKE_SCREENSHOT, TriggerSource.MOUSE_CLICK));
				}
			}
		});
		// Hide the window when a capture is about to start (published by ControlWindowController
		// before it calls CaptureService, so the window is gone before the screenshot is taken).
		EventBus.subscribe(EventType.SCREENSHOT_CAPTURE_STARTED, event -> {
			frame.setOpacity(0.0f);
		});
		// Restore the window and refresh the count once the capture (or failure) is resolved.
		EventBus.subscribe(EventType.UPDATE_SCREENSHOT_COUNT, (AppEvent<Integer> event) -> {
			frame.setOpacity(1.0f);
			label_Count.setText(String.valueOf(event.getData()));
		});
	}

	private void registerMenuButtonAction() {
		menuButton.addActionListener(e -> {
			if (!menuButton.isEnabled()) return;
			if (controlPanel.isVisible()) {
				frame.setSize(UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_COLLAPSED_HEIGHT);
				mainPanel.setSize(UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_COLLAPSED_HEIGHT);
				controlPanel.setVisible(false);
				menuButton.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
			} else {
				frame.setSize(UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_EXPANDED_HEIGHT);
				mainPanel.setSize(UIConstants.CONTROL_WINDOW_WIDTH, UIConstants.CONTROL_WINDOW_EXPANDED_HEIGHT);
				controlPanel.setVisible(true);
				menuButton.setToolTipText("<html>Click here to collapse<br>OR Right click to explore Feature Menu</html>");
			}
		});
	}

	private void registerPowerButtonAction() {
		powerButton.addActionListener(e -> {
			if (SystemNativeDialog.showConfirmation(null, "Are you sure that you want to exit the application?", "Confirm")) {
				EventBus.publish(new AppEvent<>(EventType.CLOSE_APP));
			}
		});
	}

	private void registerDeleteButtonAction() {
		deleteButton.addActionListener(e -> {
			if (SystemNativeDialog.showConfirmation(null, "Are you sure that you want to delete all screenshots?", "Warning")) {
				EventBus.publish(new AppEvent<>(EventType.DELETE_SCREENSHOTS));
			}
		});
	}

	private void registerViewButtonAction() {
		viewButton.addActionListener(e -> EventBus.publish(new AppEvent<>(EventType.SHOW_LATEST_SCREENSHOT)));
	}

	private void registerSaveButtonAction() {
		saveButton.addActionListener(e -> openActionWindow(ActionWindow.SAVE));
	}

	private void registerRecordButtonAction() {
		recordButton.addActionListener(e -> openActionWindow(ActionWindow.RECORD));
	}

	private void registerSettingsButtonAction() {
		settingsButton.addActionListener(e -> openActionWindow(ActionWindow.SETTINGS));
	}

	/**
	 * Opens {@link ActionWindow} at the given tab. Sets {@code isActionWindowOpened}
	 * so that click-pad screenshot capture is suppressed while the dialog is visible.
	 * The flag is cleared via the {@code onClose} callback when the dialog is disposed.
	 */
	private void openActionWindow(int tab) {
		if (isActionWindowOpened) return;
		isActionWindowOpened = true;
		new ActionWindow(() -> isActionWindowOpened = false, tab).show();
	}

}
