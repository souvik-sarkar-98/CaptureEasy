package captureEasy.UI.Components;

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
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import captureEasy.Launch.Application;
import captureEasy.Resources.Library;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import javax.swing.ButtonGroup;

public class SettingsPanel extends Library implements MouseListener,MouseMotionListener{

	public JPanel SettingsPane;
	public JPanel SettingsPane_DocFolderPanel;
	public JTextField SettingsPane_DocFolderPanel_textField_DocDestFolder;
	public JPanel SettingsPane_FramePanel;
	public JCheckBox SettingsPane_Recordpanel_RecordFlag;
	public JButton btnUpdateFrameLocation;
	public static JLabel lblLocationx;
	public JButton SettingsPane_Btnpanel_SaveBtn;
	public JComboBox<?> comboBox_ImageFormat;
	int Xlocation,Ylocation;
	SensorGUI sen;
	public JComboBox<?> comboBox_CaptureKey;
	public JButton CancelBtn;
	public final ButtonGroup buttonGroup = new ButtonGroup();
	public JCheckBox chckbxShowFilderNameField;
	public JButton SettingsPane_DocFolderPanel_Chooser;
	public boolean BtnPanelState=property.getBoolean("SensorBTNPanelVisible",false);
	public JCheckBox chckbxSetFoldernameMandatory;
	public JRadioButton DocumentDestination;
	public JPanel SettingsScrollPane;
	public boolean loadSettingsTab=false;
	private JTabbedPane TabbledPanel;
	public SettingsPanel(JTabbedPane TabbledPanel)
	{
		this.TabbledPanel=TabbledPanel;
		SettingsScrollPane= new JPanel();
		SettingsScrollPane.setSize(new Dimension(439, 320));
		SettingsScrollPane.setBackground(Color.WHITE);
		SettingsScrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));					
		SettingsScrollPane.setLayout(null);
		SettingsScrollPane.addMouseListener(this);
		SettingsScrollPane.addMouseMotionListener(this);
	}
	public void loadSettingsTab()
	{
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
				SettingsPane_DocFolderPanel_textField_DocDestFolder = new JTextField();
				SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.WHITE);
				SettingsPane_DocFolderPanel_textField_DocDestFolder.getDocument().addDocumentListener(new DocumentListener()
				{
					public void changedUpdate(DocumentEvent e) {
						if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals(""))
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.PINK);
						else 
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
					public void insertUpdate(DocumentEvent e) {
						if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals(""))
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.PINK);
						else 
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
					public void removeUpdate(DocumentEvent e) {
						if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals(""))
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.PINK);
						else 
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.WHITE);

						/*if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("") && !btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						else if(!SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals("")  && btnUpdateFrameLocation.getText().equalsIgnoreCase("Update frame location"))
							SettingsPane_Btnpanel_SaveBtn.setEnabled(true);*/
					}
				});

				SettingsPane_DocFolderPanel_textField_DocDestFolder.setBounds(25, 40, 335, 25);
				SettingsPane_DocFolderPanel_textField_DocDestFolder.setFont(new Font("Tahoma", Font.PLAIN, 16));
				SettingsPane_DocFolderPanel.add(SettingsPane_DocFolderPanel_textField_DocDestFolder);
				SettingsPane_DocFolderPanel_textField_DocDestFolder.setColumns(20);
			}
			{
				SettingsPane_DocFolderPanel_Chooser = new JButton();
				SettingsPane_DocFolderPanel_Chooser.setBounds(373, 22, 29, 39);
				SettingsPane_DocFolderPanel.add(SettingsPane_DocFolderPanel_Chooser);
				SettingsPane_DocFolderPanel_Chooser.setToolTipText("Choose file");
				SettingsPane_DocFolderPanel_Chooser.setBackground(Color.WHITE);
				Dimension Size1=SettingsPane_DocFolderPanel_Chooser.getSize();
				SettingsPane_DocFolderPanel_Chooser.addActionListener(new ActionListener(){
					private JFileChooser fileChooser;

					@Override
					public void actionPerformed(ActionEvent e) {
						ActionGUI.dialog.setAlwaysOnTop(false);
						try {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e1) {
							logger.error(e1.getClass().getName()+" Occured while setting look and feel of JDirectoryChooser");
						}
						fileChooser = new JFileChooser();
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int option = fileChooser.showOpenDialog(new Frame());
						if(option == JFileChooser.APPROVE_OPTION){
							SettingsPane_DocFolderPanel_textField_DocDestFolder.setText(fileChooser.getSelectedFile().getPath());
							ActionGUI.dialog.setAlwaysOnTop(true);
						}else {
							if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals(""))
							{
								new PopUp("ERROR","error","No Folder Selected","Ok, I understood","");
								SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.PINK);
							}
							else
								new PopUp("WARNING","warning","No Folder Selected","Ok, I understood","");
						}
						try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
					}
				});
				SettingsPane_DocFolderPanel_Chooser.setMargin(new Insets(2, 2, 2, 2));
				BufferedImage master1;
				try {
					master1 = ImageIO.read(new File(uploadIcon));
					Image scaled1 = master1.getScaledInstance(Size1.width, Size1.height, java.awt.Image.SCALE_SMOOTH);
					SettingsPane_DocFolderPanel_Chooser.setIcon(new ImageIcon(scaled1));
				} catch (IOException e1) {
					logError(e1," Exception occurec file not found "+uploadIcon);
				}
				
			}
			{
				{
					chckbxShowFilderNameField = new JCheckBox("Show folder name field");
					chckbxShowFilderNameField.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
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
			SettingsPane_DocFolderPanel.add(chckbxSetFoldernameMandatory);
			
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
					SettingsPane_Recordpanel_RecordFlag = new JCheckBox("Screen recording");
					SettingsPane_Recordpanel_RecordFlag.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							new PopUp("Information","info","Sorry !! This facility is currently  unavailable.","Ok, Fine","");
							SettingsPane_Recordpanel_RecordFlag.setSelected(false);
							try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
						}
					});
					SettingsPane_Recordpanel_RecordFlag.setFont(new Font("Tahoma", Font.BOLD, 16));
					SettingsPane_Recordpanel_RecordFlag.setBounds(116, 9, 180, 25);
					SettingsPane_FramePanel.add(SettingsPane_Recordpanel_RecordFlag);
				}
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
							BtnPanelState=sen.button_panel.isVisible();
							btnUpdateFrameLocation.setBackground(UIManager.getColor("CheckBox.background"));
							Xlocation=SensorGUI.x;Ylocation=SensorGUI.y;
							lblLocationx.setText("Location : ( "+SensorGUI.x+" , "+SensorGUI.y+" )");
							SensorGUI.frame.dispose();
							btnUpdateFrameLocation.setText("Update frame location");
							SensorGUI.clickable=true;
							SensorGUI.label_Count.setVisible(false);
							SensorGUI.frame.setAlwaysOnTop(true);
						}
						else
						{
							btnUpdateFrameLocation.setBackground(UIManager.getColor("CheckBox.background"));
							PopUp window=new PopUp("INSTRUCTION","INFO","To "+btnUpdateFrameLocation.getText().toLowerCase()+", drag the green frame to your preferred location and set frame expansion then click Done.","Ok, I Understood","");
							window.setVisible(true);
							window.getRootPane().setDefaultButton(window.btnNewButton);
							sen=new SensorGUI();
							SensorGUI.frame.setVisible(true);
							SensorGUI.label_Count.setVisible(false);
							try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
							sen.sensor_panel.setToolTipText("Drag this window to your preferred location.");
							sen.lebel_Power.setToolTipText("");
							sen.Label_Pause.setToolTipText("");
							sen.label_delete.setToolTipText("");
							sen.label_Save.setToolTipText("");
							sen.label_View.setToolTipText("");
							sen.label_Document.setToolTipText("");
							sen.label_Settings.setToolTipText("");
							SensorGUI.clickable=false;
							btnUpdateFrameLocation.setText("Done");
							new Timer(5000, new ActionListener() {
						        @Override
						        public void actionPerformed(ActionEvent e) {
						        	window.dispose();
						        }
						      }).start();
						}

					}
				});
				btnUpdateFrameLocation.setFont(new Font("Tahoma", Font.BOLD, 13));
				btnUpdateFrameLocation.setBounds(12, 10, 176, 25);
				SettingsPane_Locationpanel.add(btnUpdateFrameLocation);

				lblLocationx = new JLabel("Location : ( x , y )");
				lblLocationx.setBounds(22, 40, 166, 20);
				SettingsPane_Locationpanel.add(lblLocationx);}
			{
				JPanel SettingsPane_Btnpanel = new JPanel();
				SettingsPane_Btnpanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				SettingsPane_Btnpanel.setBounds(12, 272, 413, 35);
				SettingsPane.add(SettingsPane_Btnpanel);
				SettingsPane_Btnpanel.setLayout(null);
				{
					SettingsPane_Btnpanel_SaveBtn = new JButton("Save");
					if(SettingsPane_DocFolderPanel_textField_DocDestFolder.getText().equals(""))
						//SettingsPane_Btnpanel_SaveBtn.setEnabled(false);
						SettingsPane_Btnpanel_SaveBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								String DocPath_Current=SettingsPane_DocFolderPanel_textField_DocDestFolder.getText();
								String DocPath_Previous=property.getString("DocPath");
								boolean showFolderNameField_Current=chckbxShowFilderNameField.isSelected();
								boolean showFolderNameField_Previous=property.getBoolean("showFolderNameField",false);
								boolean setFoldernameMandatory_Current=chckbxSetFoldernameMandatory.isSelected();
								boolean setFoldernameMandatory_Previous=property.getBoolean("setFolderNameMandatory",false);
								String ScreenRecording_Current=String.valueOf(SettingsPane_Recordpanel_RecordFlag.isSelected());
								String ScreenRecording_Prev=property.getString("ScreenRecording");
								int Xvalue_Prev, Yvalue_Prev;
								String xv=property.getInteger("Xlocation",screensize.width-160).toString();
								String yv=property.getInteger("Ylocation",screensize.width-160).toString();
								try{ Xvalue_Prev=Integer.parseInt(xv);}catch(Exception q){Xvalue_Prev=0;}
								try{  Yvalue_Prev=Integer.parseInt(yv);}catch(Exception q){Yvalue_Prev=0;}
								String ImageFormat_Current=String.valueOf(comboBox_ImageFormat.getSelectedItem());
								String ImageFormat_Prev=property.getString("ImageFormat");
								String CaptureKey_Current=String.valueOf(comboBox_CaptureKey.getSelectedItem());
								String CaptureKey_Prev=property.getString("CaptureKey");
								if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Done"))
								{
									PopUp window = new PopUp("ERROR","error","Please click Done before save !!","Ok, I understood","");
									window.setVisible(true);
									btnUpdateFrameLocation.setBackground(Color.PINK);
									try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
								}
								
								else if(BtnPanelState==property.getBoolean("SensorBTNPanelVisible",false) && CaptureKey_Current.equals(CaptureKey_Prev) && DocPath_Current.equals(DocPath_Previous) && showFolderNameField_Current==showFolderNameField_Previous&& setFoldernameMandatory_Current==setFoldernameMandatory_Previous && ScreenRecording_Current.equals(ScreenRecording_Prev) && (Math.abs(Xvalue_Prev-Xlocation)==0 || Math.abs(Xvalue_Prev-Xlocation)==Xvalue_Prev) && (Math.abs(Yvalue_Prev-Ylocation)==0 || Math.abs(Yvalue_Prev-Xlocation)==Yvalue_Prev)  && ImageFormat_Current.equals(ImageFormat_Prev))
								{
									PopUp window = new PopUp("ERROR","error","No changes have been made !!","Ok, I understood","");
									window.setVisible(true);
									window.btnNewButton.addActionListener(new ActionListener(){

										@Override
										public void actionPerformed(ActionEvent e) {
											try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
										}
										
									});
									try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
								}
								else if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Set frame location") || DocPath_Current.equals(""))
								{
									PopUp window = new PopUp("ERROR","error","All mandatory fields must be set before save !!","Ok, I understood","");
									window.setVisible(true);
									if(btnUpdateFrameLocation.getText().equalsIgnoreCase("Set frame location"))
										btnUpdateFrameLocation.setBackground(Color.PINK);
									if(DocPath_Current.equals(""))
										SettingsPane_DocFolderPanel_textField_DocDestFolder.setBackground(Color.PINK);
									try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
								}

								else
								{
									property.setProperty("DocPath",DocPath_Current);
									property.setProperty("showFolderNameField",showFolderNameField_Current);
									property.setProperty("setFolderNameMandatory",setFoldernameMandatory_Current);
									property.setProperty("ScreenRecording",ScreenRecording_Current);
									property.setProperty("CaptureKey",CaptureKey_Current);
									try
									{
										if(Xlocation!=0&& Math.abs(Xlocation-Xvalue_Prev)>0)
											property.setProperty("Xlocation",String.valueOf(Xlocation));
										if(Ylocation!=0 &&Math.abs(Ylocation-Yvalue_Prev)>0)
											property.setProperty("Ylocation",String.valueOf(Ylocation));
										if(!BtnPanelState==property.getBoolean("SensorBTNPanelVisible"))
											property.setProperty("SensorBTNPanelVisible",BtnPanelState);
									}
									catch(Exception pp){
										logError(pp,"Exception while saving frame location");
										property.setProperty("Xlocation",String.valueOf(Xlocation));
										property.setProperty("Ylocation",String.valueOf(Ylocation));	
										property.setProperty("SensorBTNPanelVisible",BtnPanelState);
									}
									property.setProperty("ImageFormat",ImageFormat_Current);

									
									PopUp window = new PopUp("INFORMATION","info","Successfully Saved !!","Close","");
									window.setVisible(true);
									window.getRootPane().setDefaultButton(window.btnNewButton);
									
									new Timer(1000, new ActionListener() {
								        @Override
								        public void actionPerformed(ActionEvent e) {
								        	window.dispose();
								        }
								      }).start();
									
									if(ActionGUI.settingsPanel.CancelBtn.isEnabled())
									{
										ActionGUI.dialog.dispose();
										ActionGUI.leaveControl=true;
										Application.sensor.play();
										if(xv!=null && yv!=null)
										{
											new SensorGUI();
											SensorGUI.frame.setVisible(true);
											SensorGUI.frame.setAlwaysOnTop(true);
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
											else 
												ActionGUI.savePanel.textField_Filename.setColumns(22);

											
										
										}
										if(ActionGUI.savePanel.rdbtnSavePDF.isSelected() && SavePanel.chckbxOverwriteSelectedFile.getText().equalsIgnoreCase("Rename selected file")&& !SavePanel.chckbxOverwriteSelectedFile.isSelected())
											ActionGUI.savePanel.btnDone.setVisible(true);
										if(ActionPanel.panel_4!=null)
											ActionGUI.redirectingTabID=2;
										else
											ActionGUI.redirectingTabID=0;

										}catch(Exception e58){}
										
										TabbledPanel.setSelectedIndex(ActionGUI.redirectingTabID);
										
										if(ActionGUI.savePanel!=null && !ActionGUI.savePanel.textField_Filename.getText().replaceAll("\\s", "").equals(""))
										{
											ActionGUI.savePanel.btnDone.setVisible(true);
											ActionGUI.savePanel.textField_ParFol.setBackground(Color.WHITE);
										}
										ActionGUI.redirectingTabID=0;
									}
									try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e6){}
								}
							}
						});
					SettingsPane_Btnpanel_SaveBtn.setBackground(Color.BLUE);
					SettingsPane_Btnpanel_SaveBtn.setForeground(Color.BLACK);
					SettingsPane_Btnpanel_SaveBtn.setBorder(new LineBorder(new Color(0, 0, 0)));
					SettingsPane_Btnpanel_SaveBtn.setFont(new Font("Tahoma", Font.BOLD, 16));
					SettingsPane_Btnpanel_SaveBtn.setBounds(100, 5, 100, 25);
					SettingsPane_Btnpanel.add(SettingsPane_Btnpanel_SaveBtn);
				}
				CancelBtn = new JButton("Cancel");
				CancelBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if(property.getString("DocPath")==null)
						{
						PopUp p=new PopUp("ERROR","error","Sorry !! You cannot cancel during primary setup. Please complete primary setup process","Ok, I understood","");
						p.setVisible(true);
						try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
						/*p.btnNewButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								try{ActionGUI.dialog.setAlwaysOnTop(true);}catch(Exception e){}								
							}
						})*/;
						}
						else
						{
							ActionGUI.dialog.dispose();
							ActionGUI.leaveControl=true;
							Application.sensor.play();
							try{SensorGUI.frame.setAlwaysOnTop(true);}catch(Exception e5){}
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

			String[] captureKey={"PrtSc","Ctrl+ALT","Ctrl+Shift","F7","F8","F9"};
			comboBox_CaptureKey = new JComboBox<Object>(captureKey);
			
			comboBox_CaptureKey.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(ActionGUI.dialog.isVisible() && ActionGUI.tagDrop)
					{
						new PopUp("Information","info","Please pause the application while you are using "+comboBox_CaptureKey.getSelectedItem().toString()+" button for other use.","Ok,I'll Remember","").setVisible(true);
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
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
