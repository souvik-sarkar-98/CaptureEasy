package app.captureEasy.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Launch.Application;
import app.captureEasy.Resources.Library;
import app.captureEasy.UI.Components.PopUp;
import app.captureEasy.UI.Components.TextField;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;

public class SettingsPanel extends Library implements MouseListener,MouseMotionListener{

	public JPanel SettingsPane;
	public JPanel SettingsPane_DocFolderPanel;
	public TextField textField_DocDestFolder;
	public JPanel SettingsPane_FramePanel;
	public JCheckBox showConsole;
	public JButton btnUpdateFrameLocation;
	public static JLabel lblLocationx;
	public JButton SaveBtn;
	public JComboBox<?> comboBox_ImageFormat;
	int Xlocation,Ylocation;
	PopUp pop;
	Timer timer;
	public static String DocPath_Previous=null;
	public JComboBox<?> comboBox_CaptureKey;
	public JButton CancelBtn;
	public final ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox chckbxShowFilderNameField;
	public JButton BtnFolderChooser;
	public boolean BtnPanelState=property.getBoolean("SensorBTNPanelVisible",false);
	public JCheckBox chckbxSetFoldernameMandatory;
	public JRadioButton DocumentDestination;
	public JPanel SettingsScrollPane;
	public boolean loadSettingsTab=false;
	public static boolean isframeupdateTouched=false;
	private JTabbedPane TabbledPanel;
	public JCheckBox chckbxAutoUpdate;
	String[] captureKey={"PrtSc","ALT+Prtsc","Ctrl+ALT","Ctrl+Shift","F7","F9"};
	DefaultComboBoxModel<Object> keys=new  DefaultComboBoxModel<Object>(captureKey);
	SensorGUI s;
	public SettingsPanel(JTabbedPane TabbledPanel) 
	{
		SettingsPanelInit(TabbledPanel);
	}
	public void SettingsPanelInit(JTabbedPane TabbledPanel) 
	{
		this.TabbledPanel=TabbledPanel;
		SettingsScrollPane= new JPanel();
		SettingsScrollPane.setSize(new Dimension(439, 320));
		SettingsScrollPane.setBackground(Color.WHITE);
		SettingsScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));					
		SettingsScrollPane.setLayout(null);
		SettingsScrollPane.addMouseListener(this);
		SettingsScrollPane.addMouseMotionListener(this);
		try {
			loadSettingsTab();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void loadSettingsTab() throws Exception
	{
		Xlocation=property.getInteger("Xlocation",0);
		Ylocation=property.getInteger("Ylocation",0);
		SettingsPane = new JPanel();
		SettingsPane.setSize(new Dimension(439, 320));
		SettingsPane.setBackground(Color.WHITE);
		SettingsPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));					
		SettingsPane.setLayout(null);
		SettingsPane.addMouseListener(this);
		SettingsPane.addMouseMotionListener(this);


		{
			SettingsPane_DocFolderPanel = new JPanel();
			SettingsPane_DocFolderPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			SettingsPane_DocFolderPanel.setBounds(12, 13, 414, 110);
			SettingsPane.add(SettingsPane_DocFolderPanel);
			SettingsPane_DocFolderPanel.setLayout(null);
			{
				textField_DocDestFolder = new TextField();
				textField_DocDestFolder.setPlaceholder("Select document destination folder");
				textField_DocDestFolder.setBackground(Color.WHITE);
				textField_DocDestFolder.getDocument().addDocumentListener(new DocumentListener()
				{
					public void changedUpdate(DocumentEvent e) {
						if(textField_DocDestFolder.getText().equals(""))
							textField_DocDestFolder.setBackground(Color.PINK);
						else 
							textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
					public void insertUpdate(DocumentEvent e) {
						if(textField_DocDestFolder.getText().equals(""))
							textField_DocDestFolder.setBackground(Color.PINK);
						else 
							textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
					public void removeUpdate(DocumentEvent e) {
						if(textField_DocDestFolder.getText().equals(""))
							textField_DocDestFolder.setBackground(Color.PINK);
						else 
							textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
				});

				textField_DocDestFolder.setBounds(25, 40, 335, 25);
				textField_DocDestFolder.setFont(new Font("Tahoma", Font.PLAIN, 16));
				SettingsPane_DocFolderPanel.add(textField_DocDestFolder);
				textField_DocDestFolder.setColumns(20);
			}
			{
				BtnFolderChooser = new JButton();
				BtnFolderChooser.setBounds(373, 22, 29, 39);
				SettingsPane_DocFolderPanel.add(BtnFolderChooser);
				BtnFolderChooser.setToolTipText("Choose file");
				BtnFolderChooser.setBackground(Color.WHITE);
				Dimension Size1=BtnFolderChooser.getSize();
				BtnFolderChooser.addActionListener(new ActionListener(){
					private JFileChooser fileChooser;

					@Override
					public void actionPerformed(ActionEvent e) {
						ActionGUI.dialog.setAlwaysOnTop(false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e1) {
							logError(e1,e1.getClass().getName()+" Occured while setting look and feel of JDirectoryChooser");
						}
						fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int option = fileChooser.showOpenDialog(new Frame());
						if(option == JFileChooser.APPROVE_OPTION){
							textField_DocDestFolder.setText(fileChooser.getSelectedFile().getPath());
							ActionGUI.dialog.setAlwaysOnTop(true);
						}else {
							if(textField_DocDestFolder.getText().equals(""))
							{
								PopUp p=new PopUp("ERROR","error","No Folder Selected","Ok, I understood","");
								PopUp.PopDia=p;
								p.setVisible(true);
								textField_DocDestFolder.setBackground(Color.PINK);
							}
							else
							{
								PopUp p=new PopUp("WARNING","warning","No Folder Selected","Ok, I understood","");
								PopUp.PopDia=p;
								p.setVisible(true);
							}
						}
						//try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
					}
				});
				BtnFolderChooser.setMargin(new Insets(2, 2, 2, 2));
				BufferedImage master1;
				try {
					master1 = ImageIO.read(new File(uploadIcon));
					Image scaled1 = master1.getScaledInstance(Size1.width, Size1.height, java.awt.Image.SCALE_SMOOTH);
					BtnFolderChooser.setIcon(new ImageIcon(scaled1));
				} catch (IOException e1) {
					logError(e1," Exception occurec file not found "+uploadIcon);
				}

			}
			{
				{
					chckbxShowFilderNameField = new JCheckBox("Show folder name field");
					chckbxShowFilderNameField.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {

							SettingsPane_DocFolderPanel.add(chckbxSetFoldernameMandatory);
							if(chckbxShowFilderNameField.isSelected())
							{

								chckbxSetFoldernameMandatory.setSelected(false);
								chckbxSetFoldernameMandatory.setVisible(true);
							}
							else
							{
								chckbxSetFoldernameMandatory.setSelected(false);
								chckbxSetFoldernameMandatory.setVisible(false);
							}
						}
					});
					chckbxShowFilderNameField.setFont(new Font("Tahoma", Font.BOLD, 16));
					chckbxShowFilderNameField.setBounds(8, 77, 217, 25);
					chckbxShowFilderNameField.setSelected(false);
					SettingsPane_DocFolderPanel.add(chckbxShowFilderNameField);
				}
			}

			chckbxSetFoldernameMandatory = new JCheckBox("Set as mandatory field");
			chckbxSetFoldernameMandatory.setFont(new Font("Tahoma", Font.BOLD, 13));
			chckbxSetFoldernameMandatory.setBounds(229, 78, 177, 25);
			SettingsPane_DocFolderPanel.add(chckbxSetFoldernameMandatory);
			chckbxSetFoldernameMandatory.setSelected(false);
			chckbxSetFoldernameMandatory.setVisible(false);

			DocumentDestination = new JRadioButton("Update document destination folder ");
			DocumentDestination.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					DocumentDestination.setSelected(true);
				}
			});
			DocumentDestination.setSelected(true);
			DocumentDestination.setFont(new Font("Tahoma", Font.BOLD, 16));
			DocumentDestination.setBounds(8, 9, 352, 25);
			SettingsPane_DocFolderPanel.add(DocumentDestination);
			{
				SettingsPane_FramePanel = new JPanel();
				SettingsPane_FramePanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				SettingsPane_FramePanel.setBounds(12, 136, 414, 43);
				SettingsPane.add(SettingsPane_FramePanel);
				SettingsPane_FramePanel.setLayout(null);
				{
					showConsole = new JCheckBox("Show console");
					showConsole.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							///showConsole.setEnabled(false);
							showConsole.setSelected(false);
							PopUp p=new PopUp("Info","info","Sorry! this facility not available","Close","");
							PopUp.PopDia=p;
							/*if(DuplicateWindow.isSelected())
							{
								lblLocationx.setText("Location :");
								chckbxAutoUpdate.setEnabled(false);
								btnUpdateFrameLocation.setText("Set frame location");
								PopUp p=new PopUp("INSTRUCTION","info","1. Set duplicate frame location\n2. Set preferred capture key","Ok, I undestood","");
								p.setVisible(true);
								PopUp.PopDia=p;
								for(int i=0;i<keys.getSize();i++)
								{
									if(keys.getElementAt(i).toString().equalsIgnoreCase(property.getString("CaptureKey")))
									{
										ActionGUI.tagDrop=false;
										keys.removeElementAt(i);
										ActionGUI.tagDrop=false;
									}
								}
								ActionGUI.tagDrop=false;
								comboBox_CaptureKey.setSelectedIndex(0);
							}
							else
							{
								chckbxAutoUpdate.setEnabled(true);
								btnUpdateFrameLocation.setText("Update frame location");
								lblLocationx.setText("Location : ( "+senGUI.x+" , "+senGUI.y+" )");
							}*/
						}
					});
					showConsole.setFont(new Font("Tahoma", Font.BOLD, 16));
					showConsole.setBounds(30, 9, 138, 25);
					SettingsPane_FramePanel.add(showConsole);
				}

				chckbxAutoUpdate = new JCheckBox("Auto Update");
				chckbxAutoUpdate.setSelected(true);
				chckbxAutoUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
				chckbxAutoUpdate.setBounds(230, 9, 138, 25);
				SettingsPane_FramePanel.add(chckbxAutoUpdate);
			}
			{
				JPanel SettingsPane_Locationpanel = new JPanel();
				SettingsPane_Locationpanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				SettingsPane_Locationpanel.setBounds(12, 190, 200, 70);
				SettingsPane.add(SettingsPane_Locationpanel);
				SettingsPane_Locationpanel.setLayout(null);

				btnUpdateFrameLocation = new JButton("Update frame location");
				btnUpdateFrameLocation.setBackground(UIManager.getColor("CheckBox.background"));
				btnUpdateFrameLocation.setMargin(new Insets(2, 10, 2, 10));
				btnUpdateFrameLocation.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Done"))
						{
							BtnPanelState=s.button_panel.isVisible();
							btnUpdateFrameLocation.setBackground(UIManager.getColor("CheckBox.background"));
							Xlocation=s.frame.getLocationOnScreen().x;Ylocation=s.frame.getLocationOnScreen().y;
							lblLocationx.setText("Location : ( "+Xlocation+" , "+Ylocation+" )");
							s.frame.dispose();
							btnUpdateFrameLocation.setText("Update frame location");
							SensorGUI.clickable=true;
							}
						else
						{
							isframeupdateTouched=true;
							SensorGUI.isExpandable=true;
							btnUpdateFrameLocation.setBackground(UIManager.getColor("CheckBox.background"));
							PopUp window=new PopUp("INSTRUCTION","INFO","To "+btnUpdateFrameLocation.getText().toLowerCase()+", drag the green frame to your preferred location and set frame expansion then click Done.","Ok, I Understood","");
							window.setVisible(true);
							PopUp.PopDia=window;
							PopUp.PopDia.getRootPane().setDefaultButton(PopUp.btnNewButton);
							if(property.getString("DocPath")!=null)
							{
								try{senGUI.frame.dispose();}catch(Exception e){e.printStackTrace();}
							}		
								
							s=new SensorGUI();
							s.frame.setVisible(true);
							s.frame.setVisible(true);
							s.sensor_panel.setToolTipText("Drag this window to your preferred location.");
							s.lebel_Power.setToolTipText("");
							s.Label_Pause.setToolTipText("");
							s.label_delete.setToolTipText("");
							s.label_Save.setToolTipText("");
							s.label_View.setToolTipText("");
							s.label_Document.setToolTipText("");
							s.label_Settings.setToolTipText("");
							s.label_Record.setToolTipText("");
							SensorGUI.clickable=false;
							btnUpdateFrameLocation.setText("Done");
							timer=new Timer(5000, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									window.dispose();
									timer.stop();
								}
							});
							timer.start();
						}
					}
				});
				btnUpdateFrameLocation.setFont(new Font("Tahoma", Font.BOLD, 13));
				btnUpdateFrameLocation.setBounds(12, 10, 176, 25);
				SettingsPane_Locationpanel.add(btnUpdateFrameLocation);

				lblLocationx = new JLabel("Location :");
				lblLocationx.setBounds(22, 40, 166, 20);
				SettingsPane_Locationpanel.add(lblLocationx);}
			{
				JPanel SettingsPane_Btnpanel = new JPanel();
				SettingsPane_Btnpanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				SettingsPane_Btnpanel.setBounds(12, 272, 413, 35);
				SettingsPane.add(SettingsPane_Btnpanel);
				SettingsPane_Btnpanel.setLayout(null);
				{
					SaveBtn = new JButton("Save");
					if(textField_DocDestFolder.getText().equals(""))
						//SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						SaveBtn.addActionListener(new ActionListener() {

							public void actionPerformed(ActionEvent e) {
								save();
							}
						});
					SaveBtn.setForeground(Color.BLACK);
					SaveBtn.setBorder(new LineBorder(new Color(0, 0, 0)));
					SaveBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
					SaveBtn.setBounds(100, 5, 100, 25);
					SettingsPane_Btnpanel.add(SaveBtn);
				}
				CancelBtn = new JButton("Cancel");
				CancelBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(property.getString("DocPath")==null)
						{
							PopUp p=new PopUp("ERROR","error","Sorry !! You cannot cancel during primary setup. Please complete primary setup process","Ok, I understood","");
							p.setVisible(true);
							PopUp.PopDia=p;
							//try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
							/*PopUp.btnNewButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								try{ActionGUI.dialog.setAlwaysOnTop(true);}catch(Exception e){}								
							}
						})*/;
						}
						else if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Done"))
						{
							PopUp window = new PopUp("ERROR","error","Please click Done before cancel !!","Ok, I understood","");
							PopUp.PopDia=window;
							window.setVisible(true);
							btnUpdateFrameLocation.setBackground(Color.PINK);
							//try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
						}
						else
						{
							ActionGUI.dialog.dispose();
							ActionGUI.leaveControl=true;
							
							if(isframeupdateTouched)
							{
								senGUI=new SensorGUI();
								senGUI.frame.setVisible(true);
								senGUI.frame.setAlwaysOnTop(true);
							}
							//getParentWindow().setAlwaysOnTop(true);
						}
					}
				});
				CancelBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
				CancelBtn.setBounds(212, 5, 100, 25);
				SettingsPane_Btnpanel.add(CancelBtn);
			}
		}
		{	
			JPanel panel = new JPanel();
			panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			panel.setBounds(225, 190, 200, 70);
			SettingsPane.add(panel);
			panel.setLayout(null);
			{
				JLabel lblImageFormat = new JLabel("Image format");
				lblImageFormat.setBounds(12, 8, 92, 16);
				lblImageFormat.setFont(new Font("Tahoma", Font.BOLD, 13));
				panel.add(lblImageFormat);
			}
			{
				String[] imageFormats={"PNG","JPG","JPEG","BMP"};
				comboBox_ImageFormat = new JComboBox<Object>(imageFormats);
				comboBox_ImageFormat.setLocation(111, 5);
				comboBox_ImageFormat.setSelectedIndex(0);
				comboBox_ImageFormat.setPreferredSize(new Dimension(60, 22));
				comboBox_ImageFormat.setSize(new Dimension(75, 22));
				panel.add(comboBox_ImageFormat);

			}

			JLabel lblNewLabel = new JLabel("Capture key");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel.setBounds(12, 41, 92, 16);
			panel.add(lblNewLabel);


			comboBox_CaptureKey = new JComboBox<Object>(keys);
			comboBox_CaptureKey.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println(ActionGUI.tagDrop );
					try{Thread.sleep(500);pop.dispose();}catch(Exception m){}
					if(comboBox_CaptureKey.getSelectedItem().toString().equalsIgnoreCase("ALT+Prtsc") && ActionGUI.dialog.isVisible() && ActionGUI.tagDrop )
					{
						pop=new PopUp("Information","info","ALT+Prtsc will capture only focused window, So please make your target window focused.Press ALT key wait till frame disappears then press PrtSc key.","Ok,I'll Remember","");
						PopUp.PopDia=pop;
						pop.setVisible(true);
					}
					else if(ActionGUI.dialog.isVisible() && ActionGUI.tagDrop )
					{
						pop=new PopUp("Information","info","Please pause the applicaton while you are using "+comboBox_CaptureKey.getSelectedItem().toString()+" button for other use.","Ok,I'll Remember","");
						PopUp.PopDia=pop;
						pop.setVisible(true);

					}
					ActionGUI.tagDrop=true;
				}
			});
			comboBox_CaptureKey.setBounds(111, 40, 75, 22);
			comboBox_CaptureKey.setSelectedIndex(0);
			panel.add(comboBox_CaptureKey);
		}
		CancelBtn.setEnabled(false);
		loadSettingsTab=false;

	}
	public void save()
	{
		String DocPath_Current=textField_DocDestFolder.getText();
		DocPath_Previous=property.getString("DocPath");
		boolean showFolderNameField_Current=chckbxShowFilderNameField.isSelected();
		boolean showFolderNameField_Previous=property.getBoolean("showFolderNameField",false);
		boolean setFoldernameMandatory_Current=chckbxSetFoldernameMandatory.isSelected();
		boolean setFoldernameMandatory_Previous=property.getBoolean("setFolderNameMandatory",false);
		//String ScreenRecording_Current=String.valueOf(DuplicateWindow.isSelected());
		//String ScreenRecording_Prev=property.getString("DuplicateWindow");
		int Xvalue_Prev, Yvalue_Prev;
		String xv=property.getInteger("Xlocation",screensize.width-160).toString();
		String yv=property.getInteger("Ylocation",screensize.width-160).toString();
		try{ Xvalue_Prev=Integer.parseInt(xv);}catch(Exception q){Xvalue_Prev=0;}
		try{  Yvalue_Prev=Integer.parseInt(yv);}catch(Exception q){Yvalue_Prev=0;}
		String ImageFormat_Current=String.valueOf(comboBox_ImageFormat.getSelectedItem());
		String ImageFormat_Prev=property.getString("ImageFormat");
		String CaptureKey_Current=String.valueOf(comboBox_CaptureKey.getSelectedItem());
		String CaptureKey_Prev=property.getString("CaptureKey");
		//System.out.println(ScreenRecording_Current+ScreenRecording_Prev);
		if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Done"))
		{
			PopUp window = new PopUp("ERROR","error","Please click Done before save !!","Ok, I understood","");
			window.setVisible(true);
			PopUp.PopDia=window;
			btnUpdateFrameLocation.setBackground(Color.PINK);
			//try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
		}
		else if(chckbxAutoUpdate.isSelected()==property.getBoolean("autoupdate",false) && BtnPanelState==property.getBoolean("SensorBTNPanelVisible",false) && CaptureKey_Current.equals(CaptureKey_Prev) && DocPath_Current.equals(DocPath_Previous) && showFolderNameField_Current==showFolderNameField_Previous&& setFoldernameMandatory_Current==setFoldernameMandatory_Previous && /*ScreenRecording_Current.equals(ScreenRecording_Prev) &&*/ (Math.abs(Xvalue_Prev-Xlocation)==0 || Math.abs(Xvalue_Prev-Xlocation)==Xvalue_Prev) && (Math.abs(Yvalue_Prev-Ylocation)==0 || Math.abs(Yvalue_Prev-Xlocation)==Yvalue_Prev)  && ImageFormat_Current.equals(ImageFormat_Prev))
		{
			PopUp window = new PopUp("ERROR","error","No changes have been made !!","Ok, I understood","");
			window.setVisible(true);
			PopUp.PopDia=window;
			
		}
		else if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Set frame location") || DocPath_Current.equals(""))
		{
			PopUp window = new PopUp("ERROR","error","All mandatory fields must be set before save !!","Ok, I understood","");
			PopUp.PopDia=window;
			window.setVisible(true);
			if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Set frame location"))
				btnUpdateFrameLocation.setBackground(Color.PINK);
			if(DocPath_Current.equals(""))
				textField_DocDestFolder.setBackground(Color.PINK);
		//	try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
		}
		else if(!new File(textField_DocDestFolder.getText()).exists())
		{
			PopUp window = new PopUp("ERROR","error","Selected folder not exists. Please Select again","Ok, I understood","");
			PopUp.PopDia=window;
			window.setVisible(true);
			textField_DocDestFolder.setBackground(Color.PINK);
		}
		else
		{
			textField_DocDestFolder.setBackground(Color.WHITE);
			property.setProperty("DocPath",DocPath_Current);
			property.setProperty("showFolderNameField",showFolderNameField_Current);
			property.setProperty("setFolderNameMandatory",setFoldernameMandatory_Current);
			//property.setProperty("DuplicateFlag",ScreenRecording_Current);
			property.setProperty("CaptureKey",CaptureKey_Current);
			property.setProperty("autoupdate", chckbxAutoUpdate.isSelected());
			try
			{
				if(Xlocation!=0&& Math.abs(Xlocation-Xvalue_Prev)>0)
					property.setProperty("Xlocation",Xlocation);
				if(Ylocation!=0 &&Math.abs(Ylocation-Yvalue_Prev)>0)
					property.setProperty("Ylocation",Ylocation);
				if(!BtnPanelState==property.getBoolean("SensorBTNPanelVisible",false))
					property.setProperty("SensorBTNPanelVisible",BtnPanelState);
			}
			catch(Exception pp){
				logError(pp,"Exception while saving frame location");
				property.setProperty("Xlocation",Xlocation);
				property.setProperty("Ylocation",Ylocation);	
				property.setProperty("SensorBTNPanelVisible",BtnPanelState);
			}
			property.setProperty("ImageFormat",ImageFormat_Current);


			PopUp window = new PopUp("INFORMATION","info","Successfully Saved !!","Close","");
			PopUp.PopDia=window;
			window.setVisible(true);
			PopUp.PopDia.getRootPane().setDefaultButton(PopUp.btnNewButton);


			if(ActionGUI.settingsPanel.CancelBtn.isEnabled())
			{
				ActionGUI.dialog.dispose();
				ActionGUI.leaveControl=true;
				//getParentWindow().setAlwaysOnTop(true);

				if(isframeupdateTouched && !Application.isFirstTime)
				{
					try{
						senGUI=new SensorGUI();
					senGUI.frame.setVisible(true);
					senGUI.frame.setLocation(Xlocation,Ylocation);
					}catch(Exception e){}
				}

			}
			else
			{
				try{
					if(!ActionGUI.savePanel.chckbxSelectExistingDocument.isSelected() &&property.getBoolean("showFolderNameField",false))
					{
						ActionGUI.savePanel.lblParFol.setVisible(true);
						ActionGUI.savePanel.textField_ParFol.setVisible(true);
						ActionGUI.savePanel.textField_Filename.setColumns(16);
					}
					else
					{
						ActionGUI.savePanel.lblParFol.setVisible(false);
						ActionGUI.savePanel.textField_ParFol.setVisible(false);

						if(ActionGUI.savePanel.rdbtnSavePDF.isSelected() && SavePanel.chckbxOverwriteSelectedFile.isSelected())
							ActionGUI.savePanel.textField_Filename.setColumns(16);
						else /*if(ActionGUI.savePanel)*/
							ActionGUI.savePanel.textField_Filename.setColumns(22);

					}
					isframeupdateTouched=false;

					if(ActionGUI.savePanel.rdbtnSavePDF.isSelected() && SavePanel.chckbxOverwriteSelectedFile.getText().equalsIgnoreCase("Rename selected file")&& !SavePanel.chckbxOverwriteSelectedFile.isSelected())
						ActionGUI.savePanel.btnDone.setVisible(true);


					if(ActionGUI.savePanel.saveLoaded && !ActionGUI.savePanel.textField_Filename.getText().replaceAll("\\s", "").equals(""))
					{
						ActionGUI.savePanel.btnDone.setVisible(true);
						ActionGUI.savePanel.textField_ParFol.setBackground(Color.WHITE);
					}
					if(ActionGUI.savePanel.saveLoaded && ActionGUI.savePanel.lblChooseFile.isVisible())
					{
						ActionGUI.savePanel.lblParFol.setVisible(false);
						ActionGUI.savePanel.textField_ParFol.setVisible(false);
					}
					if(ActionGUI.savePanel.saveLoaded && SavePanel.chckbxOverwriteSelectedFile.isVisible())
					{
						ActionGUI.savePanel.textField_Filename.setColumns(16);
					}											
				}catch(Exception e58){}										
			}
			//try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e6){}
			timer=new Timer(1000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					window.dispose();
					PopUp.control=true;
					TabbledPanel.setSelectedIndex(0);
					timer.stop();
				}
			});
			timer.start();
		}
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		ActionGUI.xDialog = arg0.getXOnScreen();
		ActionGUI.yDialog = arg0.getYOnScreen();
		ActionGUI.dialog.setLocation(ActionGUI.xDialog - ActionGUI.xxDialog, ActionGUI.yDialog - ActionGUI.xyDialog); 		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		ActionGUI.xxDialog = e.getX();
		ActionGUI.xyDialog = e.getY();
	}

	@Override @NoLogging
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override @NoLogging
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override @NoLogging
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override @NoLogging
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override @NoLogging
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
