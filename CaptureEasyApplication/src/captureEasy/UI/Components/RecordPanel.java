package captureEasy.UI.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;

import captureEasy.Resources.Library;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class RecordPanel extends Library {
	public JPanel RecordPanel;
	public JPanel panel_Control;
	int saveTabIndex=0;
	SettingsPanel settingsPanel=null;
	public JTextField textField_Filename;
	private JLabel lblRecordingInProgress;
	private JLabel label_time;
	private JLabel lblMinimize;
	private JLabel lblStartpause;
	private JLabel lblStop;
	private JLabel lblSave;
	private JLabel label_close;
	private JPanel panel_View;
	private JCheckBox chckbxDelayStart;
	private JSpinner spinner;
	public JPanel panel_Filenames;
	public boolean isLoaded=false;
	public JTextField textField_Foldername;
	public JLabel lblFolderName;
	private JLabel lblFileName;
	private JLabel label_record;
	private JPanel panel;

	public RecordPanel(JTabbedPane TabbledPanel) {
		RecordPanel = new JPanel();
		RecordPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		RecordPanel.setBackground(Color.WHITE);
		RecordPanel.setLayout(null);
		loadRecordPanel();
	}
	public void loadRecordPanel()
	{
		panel_Control = new JPanel();
		panel_Control.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_Control.setBounds(12, 244, 405, 60);
		panel_Control.setLayout(null);
		RecordPanel.add(panel_Control);
		lblStartpause = new JLabel("start/pause");
		lblStartpause.setToolTipText("Start recording");
		lblStartpause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField_Filename.isDisplayable())
					new PopUp("ERROR","error","Sorry !! you cannot START until you save previous recording","Ok, I Understood","").setVisible(true);
				else
				{
					chckbxDelayStart.setVisible(false);
					spinner.setVisible(false);
					lblRecordingInProgress.setVisible(true);
					if(lblStartpause.getToolTipText().equalsIgnoreCase("Pause recording"))
					{
						try{
							lblRecordingInProgress.setText("Recording paused !!!");
							lblRecordingInProgress.setForeground(Color.RED);
							lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(playIcon)).getScaledInstance(lblStartpause.getBounds().width,lblStartpause.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							lblStartpause.setToolTipText("Resume recording");
						}catch (IOException e3) {
							logError(e3,"Exception in Icon loading: Image "+playIcon+" Not Available");
						}
					}
					else
					{
						try{
							lblRecordingInProgress.setText("Recording in progress ...");
							lblRecordingInProgress.setForeground(Color.BLUE);
							lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(pauseIcon)).getScaledInstance(lblStartpause.getBounds().width,lblStartpause.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
							lblStartpause.setToolTipText("Pause recording");

						}catch (IOException e3) {
							logError(e3,"Exception in Icon loading: Image "+pauseIcon+" Not Available");}
					}
				}
			}
		});
		lblStartpause.setBounds(130, 10, 40, 40);
		try {
			lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(startIcon)).getScaledInstance(lblStartpause.getBounds().width,lblStartpause.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+startIcon+" Not Available");

		}
		panel_Control.add(lblStartpause);

		lblStop = new JLabel("stop");
		lblStop.setToolTipText("Stop recording");
		lblStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(chckbxDelayStart.isVisible())
					new PopUp("ERROR","error","Sorry !! you cannot STOP because recording not yet started","Ok, I Understood","").setVisible(true);
				else if(!textField_Filename.isDisplayable())
				{
					panel.setVisible(false);
					label_record.setBounds(33, 55, 70, 70);
					label_time.setBounds(185, 70, 123, 40);
					lblRecordingInProgress.setVisible(false);
					panel_Filenames.setVisible(true);
					panel_Filenames.add(lblFolderName);
					panel_Filenames.add(textField_Foldername);
					panel_Filenames.add(lblFileName);
					panel_Filenames.add(textField_Filename);
					textField_Filename.requestFocusInWindow();
				}
			}
		});
		lblStop.setBounds(182, 10, 40, 40);
		try {
			lblStop.setIcon(new ImageIcon(ImageIO.read(new File(stopIcon)).getScaledInstance(lblStop.getBounds().width,lblStop.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+stopIcon+" Not Available");

		}
		panel_Control.add(lblStop);

		label_close = new JLabel("close");
		label_close.setToolTipText("Close window");
		label_close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ActionGUI.dialog.dispose();
				ActionGUI.leaveControl=true;
			}
		});
		label_close.setBounds(353, 15, 30, 30);
		try {
			label_close.setIcon(new ImageIcon(ImageIO.read(new File(closeIcon)).getScaledInstance(label_close.getBounds().width,label_close.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+closeIcon+" Not Available");

		}
		panel_Control.add(label_close);

		lblMinimize = new JLabel("minimize");
		lblMinimize.setToolTipText("Minimize window");
		lblMinimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				ActionGUI.dialog.setState(Frame.ICONIFIED);
			}
		});
		lblMinimize.setBounds(12, 15, 30, 30);
		try {
			lblMinimize.setIcon(new ImageIcon(ImageIO.read(new File(minimizeIcon)).getScaledInstance(lblMinimize.getBounds().width,lblMinimize.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+minimizeIcon+" Not Available");

		}
		panel_Control.add(lblMinimize);

		lblSave = new JLabel("save");
		lblSave.setToolTipText("Save  ");
		lblSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(chckbxDelayStart.isVisible())
					new PopUp("ERROR","error","Sorry !! you cannot SAVE because recording not yet started","Ok, I Understood","").setVisible(true);
				else
				{

				}
			}
		});
		lblSave.setBounds(234, 10, 40, 40);
		try {
			lblSave.setIcon(new ImageIcon(ImageIO.read(new File(saveIcon)).getScaledInstance(lblSave.getBounds().width,lblSave.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+saveIcon+" Not Available");

		}
		panel_Control.add(lblSave);

		panel_View = new JPanel();
		panel_View.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_View.setBounds(12, 13, 405, 219);
		RecordPanel.add(panel_View);
		panel_View.setLayout(null);

		JLabel lblScreenRecorder = new JLabel("Screen Recorder");
		lblScreenRecorder.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblScreenRecorder.setBounds(105, 13, 222, 25);
		panel_View.add(lblScreenRecorder);

		chckbxDelayStart = new JCheckBox("Delay start");
		chckbxDelayStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(chckbxDelayStart.isSelected())
				{
					spinner.setEnabled(true);
				}
				else
				{
					spinner.setEnabled(false);
				}
			}
		});
		chckbxDelayStart.setFont(new Font("Tahoma", Font.BOLD, 15));
		chckbxDelayStart.setBounds(120, 170, 116, 25);
		panel_View.add(chckbxDelayStart);

		spinner = new JSpinner();
		spinner.setEnabled(false);
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spinner.setBounds(243, 170, 47, 25);
		spinner.setValue(5);
		panel_View.add(spinner);

		label_record = new JLabel("");
		label_record.setBounds(33, 75, 70, 70);
		try {
			label_record.setIcon(new ImageIcon(ImageIO.read(new File(recordIcon)).getScaledInstance(label_record.getBounds().width,label_record.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+recordIcon+" Not Available");

		}
		panel_View.add(label_record);

		label_time = new JLabel("00:00");
		label_time.setFont(new Font("Tahoma", Font.PLAIN, 35));
		label_time.setBounds(185, 90, 123, 40);
		panel_View.add(label_time);

		panel = new JPanel();
		panel.setBounds(33, 166, 337, 40);
		panel_View.add(panel);

		lblRecordingInProgress = new JLabel("Recording in progress ...");
		panel.add(lblRecordingInProgress);
		lblRecordingInProgress.setForeground(Color.BLUE);
		lblRecordingInProgress.setVisible(false);
		lblRecordingInProgress.setFont(new Font("Tahoma", Font.PLAIN, 24));

		panel_Filenames = new JPanel();
		panel_Filenames.setBackground(new Color(255, 255, 204));
		panel_Filenames.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_Filenames.setVisible(false);
		panel_Filenames.setBounds(33, 141, 337, 65);
		panel_View.add(panel_Filenames);

		lblFolderName = new JLabel("Folder name : ");
		lblFolderName.setFont(new Font("Tahoma", Font.BOLD, 15));


		textField_Foldername = new JTextField();
		textField_Foldername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_Foldername.setColumns(12);

		lblFileName = new JLabel("File name : ");
		lblFileName.setFont(new Font("Tahoma", Font.BOLD, 15));

		textField_Filename = new JTextField();
		textField_Filename.setFont(new Font("Tahoma", Font.PLAIN, 15));
		if(property.getBoolean("showFolderNameField",false))
		{
			lblFolderName.setVisible(true);
			textField_Foldername.setVisible(true);
			textField_Filename.setColumns(16);
		}
		else
		{
			lblFolderName.setVisible(false);
			textField_Foldername.setVisible(false);
			textField_Filename.setColumns(22);
		}
		isLoaded=true;
	}
}
