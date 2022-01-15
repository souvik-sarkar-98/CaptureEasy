package app.captureeasy.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.Timer;

import org.jnativehook.mouse.SwingMouseAdapter;

import app.captureeasy.resources.Library;
import app.captureeasy.screenrecorder.ScreenRecorder;
import app.captureeasy.ui.components.PopUp;
import app.captureeasy.ui.components.TextField;

import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;



public class RecordPanel extends Library {
	public JPanel RecordPanel;
	public JPanel panel_Control;
	SettingsPanel settingsPanel=null;
	public TextField textField_Filename;
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
	public TextField textField_Foldername;
	public JLabel lblFolderName;
	private JLabel lblFileName;
	private JLabel label_record;
	private JPanel panel;
	Timer timer;
	private TimerTask timeCountTask;
	java.util.Timer timeCount;
	public static boolean isRecording=false;
	public static JDialog recDialog;
	public static boolean doDelete=true;
	public JLabel label_REC;
	int delay=2;
	java.util.Timer task;
	ScreenRecorder srecord;
	public RecordPanel(JTabbedPane TabbledPanel) 
	{
		RecordPanelInit(TabbledPanel) ;
	}
	public void RecordPanelInit(JTabbedPane TabbledPanel) 
	{
		RecordPanel = new JPanel();
		RecordPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		RecordPanel.setBackground(Color.WHITE);
		RecordPanel.setLayout(null);
		try {
			///loadRecordPanel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadRecordPanel() throws Exception
	{
		panel_Control = new JPanel();
		panel_Control.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_Control.setBounds(12, 244, 405, 60);
		panel_Control.setLayout(null);
		RecordPanel.add(panel_Control);
		
		recDialog=new JDialog();
		recDialog.setUndecorated(true);
		recDialog.setResizable(false);
		recDialog.getContentPane().setLayout(null);
		recDialog.setBounds(5, 5,50,50);
		recDialog.setAlwaysOnTop(true);
		recDialog.setVisible(false);
		label_REC=new JLabel();
		label_REC.setBounds(5, 5,50,50);
		recDialog.setBackground(new Color(0,0,0,0));
		label_REC.setBackground(new Color(0,0,0,0));
		recDialog.getContentPane().add(label_REC);
		try{
			label_REC.setIcon(new ImageIcon(ImageIO.read(new File(RECIcon)).getScaledInstance(label_REC.getBounds().width,label_REC.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e7) {
			label_REC.setText("Recording");logError(e7,"Exception in Icon loading: Image "+RECIcon+" Not Available");
		}
		

		label_close = new JLabel("close");
		label_close.setToolTipText("Close window");
		label_close.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				if(chckbxDelayStart.isVisible())
				{
					ActionGUI.dialog.dispose();
					ActionGUI.leaveControl=true;
					//getParentWindow().setAlwaysOnTop(true);
				}
				else
				{
					//setParentWindow(senGUI.frame);
					PopUp p=new PopUp("WARNING","warning","CAUTION !! Your recording will not save. Do you still want to continue?","Yes","No");
					PopUp.btnNewButton.addActionListener(new ActionListener(){

						@Override
						public void actionPerformed(ActionEvent e) {
							ActionGUI.dialog.dispose();
							ActionGUI.leaveControl=true;
							recDialog.setVisible(false);
							senGUI.frame.setVisible(true);
							srecord.cancel();
							task.cancel();
							task.purge();
							try{srecord.imgEncoder.finish();}catch(Exception pe){}
							doDelete=true;
						}
					});
					p.setVisible(true);
					PopUp.PopDia=p;
				}
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
		lblMinimize.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

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
		
		
		JPanel panel_btn = new JPanel();
		panel_btn.setBounds(96, 5, 204, 50);
		panel_Control.add(panel_btn);
		
		lblStartpause = new JLabel("start/pause");
		lblStartpause.setPreferredSize(new Dimension(40, 40));
		lblStartpause.setSize(new Dimension(40, 40));
		panel_btn.add(lblStartpause);
		lblStartpause.setToolTipText("Start recording");
		lblStartpause.addMouseListener(new SwingMouseAdapter() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				/*if(textField_Filename.isDisplayable())
				{
					PopUp p=new PopUp("ERROR","error","Sorry !! You cannot RESUME recording because your recording has already stopped","Ok, I Understood","");
					p.setVisible(true);
					PopUp.PopDia=p;
				}*/
				if(lblStartpause.isVisible())
				{
					doDelete=false;
					lblRecordingInProgress.setVisible(true);
					if(lblStartpause.getToolTipText().equalsIgnoreCase("Pause recording"))
					{
						pauseRecording();
					}
					else
					{
						resumeRecording();
					}
				}
			}
		});
		try {
			lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(startIcon)).getScaledInstance(lblStartpause.getSize().width,lblStartpause.getSize().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+startIcon+" Not Available");

		}
		lblStop = new JLabel("stop");
		lblStop.setPreferredSize(new Dimension(40, 40));
		lblStop.setSize(new Dimension(40, 40));
		panel_btn.add(lblStop);
		lblStop.setVisible(false);
		lblStop.setToolTipText("Stop recording");
		lblStop.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent e) {
				/*if(chckbxDelayStart.isVisible())
				{
					PopUp p=new PopUp("ERROR","error","Sorry !! you cannot STOP because recording not yet started","Ok, I Understood","");
					p.setVisible(true);
					PopUp.PopDia=p;
				}
				else if(!textField_Filename.isDisplayable())*/
				if(lblStop.isVisible())
				{
					lblSave.setVisible(true);
					lblStop.setVisible(false);
					
					try {
						srecord.cancel();
						task.cancel();
						task.purge();
						srecord.imgEncoder.finish();
						lblStartpause.setVisible(false);
						isRecording=false;
						recDialog.setVisible(false);
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
					} catch (IOException e1) {
						//
					}
					
				}
			}
		});
		try {
			lblStop.setIcon(new ImageIcon(ImageIO.read(new File(stopIcon)).getScaledInstance(lblStop.getSize().width,lblStop.getSize().height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			logError(e,"Exception in Icon loading: Image "+stopIcon+" Not Available");

		}
				lblSave = new JLabel("save");
				lblSave.setPreferredSize(new Dimension(40, 40));
				lblSave.setSize(new Dimension(40, 40));
				panel_btn.add(lblSave);
				lblSave.setVisible(false);
				lblSave.setToolTipText("Save  ");
				lblSave.addMouseListener(new SwingMouseAdapter() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void mouseClicked(MouseEvent e) {
						saveVideo();
					}
				});
				try {
					lblSave.setIcon(new ImageIcon(ImageIO.read(new File(saveIcon)).getScaledInstance(lblSave.getSize().width,lblSave.getSize().height, java.awt.Image.SCALE_SMOOTH)));
				} catch (IOException e) {
					logError(e,"Exception in Icon loading: Image "+saveIcon+" Not Available");

				}
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
		JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
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


		textField_Foldername = new TextField();
		textField_Foldername.setPlaceholder("Enter foldername");
		textField_Foldername.setToolTipText("Enter foldername");
		textField_Foldername.setFont(new Font("Tahoma", Font.PLAIN, 15));
		textField_Foldername.setColumns(12);
		textField_Foldername.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {
			}
			public void insertUpdate(DocumentEvent e) {
				DocumentCheck("Insert");
			}
			public void removeUpdate(DocumentEvent e) {
				DocumentCheck("Remove");
			}
		});

		lblFileName = new JLabel("File name : ");
		lblFileName.setFont(new Font("Tahoma", Font.BOLD, 15));

		textField_Filename = new TextField();
		textField_Filename.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {
			}
			public void insertUpdate(DocumentEvent e) {
				DocumentCheck("Insert");
			}
			public void removeUpdate(DocumentEvent e) {
				DocumentCheck("Remove");
			}
		});
		textField_Filename.setPlaceholder("Enter filename");
		textField_Filename.setToolTipText("Enter filename");
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
		//******////
		panel_Filenames.add(textField_Filename);
		

		isLoaded=true;
	}
	public void saveVideo(){
		/*if(chckbxDelayStart.isVisible())
		{
			PopUp p=new PopUp("ERROR","error","Sorry !! you cannot SAVE because recording not yet started","Ok, I Understood","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}
		else*/
		if(lblSave.isVisible())
		{
			if(textField_Filename.isVisible() && textField_Filename.getText().replaceAll("\\s", "").equals(""))
			{
				PopUp p=new PopUp("ERROR","error","Filename cannot be blank. Please enter filename","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
				textField_Filename.setBackground(Color.PINK);
			}
			else if(textField_Filename.getText().endsWith("\\") || textField_Filename.getText().endsWith("/"))
			{
				PopUp p=new PopUp("ERROR","error","Filename cannot end with ' \\ '    or ' / ' . Please modify input.","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
				textField_Filename.setBackground(Color.PINK);
			}
			else if (textField_Filename.getText().contains(Character.toString('"')) /*|| textField_Filename.getText().contains("/") || textField_Filename.getText().contains("\\")*/ || textField_Filename.getText().contains(":") || textField_Filename.getText().contains("*") || textField_Filename.getText().contains("?") || textField_Filename.getText().contains("<") || textField_Filename.getText().contains(">") || textField_Filename.getText().contains("|")) 
			{
				textField_Filename.setBackground(Color.PINK);
				PopUp p=new PopUp("ERROR","error", "A file name can not contain any of the following "
						+ "characters: * : ? " + Character.toString('"') + " < > | ","Ok, I understood","");
				p.setAlwaysOnTop(true);
				PopUp.PopDia=p;
			}
			else if (textField_Foldername.getText().contains(Character.toString('"')) ||  textField_Foldername.getText().contains(":") || textField_Foldername.getText().contains("*") || textField_Foldername.getText().contains("?") || textField_Foldername.getText().contains("<") || textField_Foldername.getText().contains(">") || textField_Foldername.getText().contains("|")) 
			{
				textField_Foldername.setBackground(Color.PINK);
				PopUp p=new PopUp("ERROR","error", "A Folder name can not contain any of the following "
						+ "characters: * : ? " + Character.toString('"') + " < > | ","Ok, I understood","");
				p.setAlwaysOnTop(true);;
				PopUp.PopDia=p;
			}
			else if(property.getBoolean("showFolderNameField",false) && property.getBoolean("setFolderNameMandatory",false) && textField_Foldername.getText().replaceAll("\\s", "").equals(""))
			{
				PopUp p=new PopUp("ERROR","error","You have set parent folder name field as mandatory. Please enter folder name or goto settings and untick the checkbox to proceed further","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
				textField_Foldername.setBackground(Color.PINK);
			}
			else
			{
				textField_Foldername.setBackground(Color.WHITE);
				textField_Filename.setBackground(Color.WHITE);
				copyToFolder(tempFolderPath+"/DoNotDelete.mp4",createFolder(createSubFolders(property.getString("DocPath",""),textField_Foldername.getText())+"\\"+textField_Filename.getText()+".mp4"));
				ActionGUI.dialog.dispose();
				ActionGUI.leaveControl=true;
				doDelete=true;
				//getParentWindow().setAlwaysOnTop(true);
				//setParentWindow(senGUI.frame);
				PopUp window = new PopUp("INFORMATION","info","Successfully Saved !!","Close","");
				PopUp.PopDia=window;
				window.setVisible(true);
				PopUp.PopDia.getRootPane().setDefaultButton(PopUp.btnNewButton);
				timer=new Timer(1000, new ActionListener() {
			        @Override
			        public void actionPerformed(ActionEvent e) {
			        	window.dispose();
			        	PopUp.control=true;
						timer.stop();
						//getParentWindow().setAlwaysOnTop(true);
			        }
			      });
				timer.start();
			}
		}
	}
	public void resumeRecording() {
		lblRecordingInProgress.setForeground(Color.BLUE);
		if(lblStartpause.getToolTipText().equalsIgnoreCase("Start recording"))
		{
			if(spinner.isEnabled() && chckbxDelayStart.isSelected())
				delay=(int) spinner.getValue();
			chckbxDelayStart.setVisible(false);
			spinner.setVisible(false);
			new Thread(new Runnable(){
				@Override
				public void run() {
					do{
						delay=delay-1;
						lblRecordingInProgress.setText("Recording will start in "+delay+" sec");
						try{Thread.sleep(1000);}catch(Exception e){}
					}while(delay!=0);
				}
				
			}).start();
			timer=new Timer(delay*1000, new ActionListener() {
			    int timeInSec = 0;

				@Override
				public void actionPerformed(ActionEvent e) {
					lblStop.setVisible(true);
					ActionGUI.dialog.setState(JFrame.ICONIFIED);
					recDialog.setVisible(true);
					isRecording=true;
					senGUI.frame.setVisible(false);
					task= new java.util.Timer();
					srecord=new ScreenRecorder(tempFolderPath+"/DoNotDelete.mp4");
			        task.scheduleAtFixedRate(srecord, 0, 1000/24);
			        timeCount=new java.util.Timer();
			        timeCountTask=new TimerTask(){
						@Override
						public void run() {
							if(isRecording)
							{
								//int hr = timeInSec/3600;
								  int rem = timeInSec%3600;
								  int mn = rem/60;
								  int sec = rem%60;
								  //String hrStr = (hr<10 ? "0" : "")+hr;
								  String mnStr = (mn<10 ? "0" : "")+mn;
								  String secStr = (sec<10 ? "0" : "")+sec;
								label_time.setText(mnStr+":"+secStr);
								timeInSec++;
							}
						}
			        };
			        timeCount.scheduleAtFixedRate(timeCountTask, 0, 1000);
			        timer.stop();
				}
			});
			timer.start();
		}
		else
		{
			recDialog.setVisible(true);
			lblRecordingInProgress.setText("Recording in progress ...");
			isRecording=true;
			senGUI.frame.setVisible(false);
			ActionGUI.dialog.setState(JFrame.ICONIFIED);
		}
		lblStartpause.setToolTipText("Pause recording");
		try{
			lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(pauseIcon)).getScaledInstance(lblStartpause.getBounds().width,lblStartpause.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		}catch (IOException e3) {
			logError(e3,"Exception in Icon loading: Image "+pauseIcon+" Not Available");}		
	}

	public void pauseRecording() {
		isRecording=false;
		senGUI.frame.setVisible(true);
		ActionGUI.dialog.setState(JFrame.NORMAL);
		lblRecordingInProgress.setText("Recording paused !!!");
		lblRecordingInProgress.setForeground(Color.RED);
		recDialog.setVisible(false);
		lblStartpause.setToolTipText("Resume recording");
		try{
			lblStartpause.setIcon(new ImageIcon(ImageIO.read(new File(playIcon)).getScaledInstance(lblStartpause.getBounds().width,lblStartpause.getBounds().height, java.awt.Image.SCALE_SMOOTH)));
		}catch (IOException e3) {
			logError(e3,"Exception in Icon loading: Image "+playIcon+" Not Available");
		}				
	}
	public void DocumentCheck(String ActionType)  
	{
		try{
			String folder=textField_Foldername.getText();
			if (textField_Filename.getText().contains(Character.toString('"')) /*|| textField_Filename.getText().contains("/") || textField_Filename.getText().contains("\\")*/ || textField_Filename.getText().contains(":") || textField_Filename.getText().contains("*") || textField_Filename.getText().contains("?") || textField_Filename.getText().contains("<") || textField_Filename.getText().contains(">") || textField_Filename.getText().contains("|")) 
			{
				textField_Filename.setBackground(Color.PINK);
				if(ActionType.equalsIgnoreCase("Insert") && PopUp.control)
				{
					PopUp popup =new PopUp("ERROR","error", "A file name can not contain any of the following "
							+ "characters: * : ? " + Character.toString('"') + " < > | ","Ok, I understood","");
					popup.setAlwaysOnTop(true);
					PopUp.PopDia=popup;
				}
			}
			else if (folder.contains(Character.toString('"')) || /*folder.contains("/") || folder.contains("\\") ||*/ folder.contains(":") || folder.contains("*") || folder.contains("?") || folder.contains("<") || folder.contains(">") || folder.contains("|")) 
			{
				textField_Foldername.setBackground(Color.PINK);
				if(ActionType.equalsIgnoreCase("Insert") && PopUp.control)
				{
					PopUp popup=new PopUp("ERROR","error", "A folder name can not contain any of the following "
							+ "characters: * : ? " + Character.toString('"') + " < > | ","Ok, I understood","");
				popup.setAlwaysOnTop(true);
				PopUp.PopDia=popup;
				}
			}
			else if (new File(getSubFolders(property.getString("DocPath"),textField_Foldername.getText()) + "\\" + textField_Filename.getText() + ".mp4").exists()) 
			{
				textField_Filename.setBackground(Color.PINK);
				if(ActionType.equalsIgnoreCase("Insert"))
				{
					ActionGUI.dialog.setAlwaysOnTop(false);
					PopUp popup=new PopUp("ERROR","error","There is already a file with the same name in "+new File(String.valueOf(getSubFolders(property.getString("DocPath"),textField_Foldername.getText())) 
							+ "\\" + textField_Filename.getText() + ".docx").getParentFile()+" folder.","Ok, I understood","");
					popup.setAlwaysOnTop(true);;
					PopUp.PopDia=popup;
				}

			}
			else 
			{
				textField_Filename.setBackground(Color.WHITE);
				textField_Foldername.setBackground(Color.WHITE);
			}
		}catch(Exception e)
		{
			logError(e,"Exception Occured");

		}
	}
}
