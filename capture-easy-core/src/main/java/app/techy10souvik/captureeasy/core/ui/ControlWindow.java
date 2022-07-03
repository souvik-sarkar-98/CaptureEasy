package app.techy10souvik.captureeasy.core.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.apache.commons.configuration.ConfigurationException;
import org.jnativehook.mouse.SwingMouseAdapter;

import app.techy10souvik.captureeasy.common.ui.AlertPopup;
import app.techy10souvik.captureeasy.common.util.PropertyUtil;
import app.techy10souvik.captureeasy.core.apis.CaptureEvent;
import app.techy10souvik.captureeasy.core.services.CaptureService;

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
	protected int xy;
	protected int xx;
	protected int x;
	protected int y;
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
	private Dimension buttonSize = new Dimension(50, 50);

	/**
	 * @param splash
	 * @return 
	 * @throws IOException
	 * @throws ConfigurationException
	 * 
	 */
	
	public ControlWindow()  {
		initGUI();
	}
	
	public static ControlWindow init()  {
		return new ControlWindow();
	}
	
	
	public JFrame show() {
		frame.setVisible(true);
		return frame;
	}

	private void initGUI()  {
		frame = new JFrame();
		frame.setName("Control_Window");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(taskbarIcon));
		frame.setSize(new Dimension(54, 110));
		frame.setAlwaysOnTop(true);
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(this.getClass().getResource(taskbarIcon)));
			frame.setIconImages(icons);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		frame.setLocation(PropertyUtil.getGUILocation());
		frame.setAlwaysOnTop(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.getContentPane().add(initMainPanel());

	}

	private JPanel initMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 54, 110);
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(null);
		mainPanel.setLayout(null);
		mainPanel.setBackground(new Color(0, 0, 0, 0));
		mainPanel.add(initClickPanel());
		mainPanel.add(initMenuButton());
		mainPanel.add(initControlPanel());
		return mainPanel;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initMenuButton() {
		menuButton = new JButton();
		menuButton.setBackground(Color.WHITE);
		menuButton.setBounds(2, 55, 50, 50);
		menuButton.setBackground(new Color(0, 0, 0, 0));
		menuButton.setBorderPainted(false);
		menuButton.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
		try {
			menuButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(menuIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
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
		clickPad.setBounds(0, 0, 54, 50);
		clickPad.setLayout(new FlowLayout(1, 5, 5));

		label_Count = new JLabel();
		label_Count.setFont(new Font("Tahoma", 1, 20));
		clickPad.add(label_Count);

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
		return controlPanel;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initSettingsButton() {
		settingsButton = new JButton();
		settingsButton.setSize(new Dimension(50, 50));
		settingsButton.setBounds(1, 330, 50, 50);
		settingsButton.setBackground(new Color(0, 0, 0, 0));
		settingsButton.setBorderPainted(false);
		settingsButton.setToolTipText("Click Here for settings");
		try {
			settingsButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(settingsIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e4) {
			settingsButton.setText("Settings");
			// Library.logError(e4, "Exception in Icon initing: Image /Icons/settings.png
			// Not Available");
		}
		return settingsButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initRecordingButton() {
		recordButton = new JButton();
		recordButton.setBorderPainted(false);
		recordButton.setToolTipText("Click here to record screen");
		//recordButton.setBounds(1, 387, 50, 50);
		recordButton.setBounds(1, 275, 50, 50);
		recordButton.setBackground(new Color(0, 0, 0, 0));
		try {
			recordButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(recordIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e4) {
			recordButton.setText("Record");
			// Library.logError(e4, "Exception in Icon initing: Image /Icons/record.png Not
			// Available");
		}
		return recordButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initViewButton() {
		viewButton = new JButton();
		viewButton.setBorderPainted(false);
		viewButton.setBounds(1, 220, 50, 50);
		viewButton.setBackground(new Color(0, 0, 0, 0));
		viewButton.setToolTipText("Click here to view screenshots");
		viewButton.setPreferredSize(new Dimension(50, 50));
		try {
			viewButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(viewIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e3) {
			viewButton.setText("View");
			// Library.logError(e3, "Exception in Icon initing: Image /Icons/view.png Not
			// Available");
		}
		return viewButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initSaveButton() {
		saveButton = new JButton();
		saveButton.setName("SAVE");
		saveButton.setBounds(1, 165, 50, 50);
		saveButton.setBackground(new Color(0, 0, 0, 0));

		saveButton.setToolTipText("Click here to Save");

		saveButton.setBorderPainted(false);
		saveButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				// SensorGUI.this.saveAction();
			}
		});
		try {
			saveButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(saveIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e3) {
			saveButton.setText("Save");
			// Library.logError(e3, "Exception in Icon initing: Image /Icons/save.png Not
			// Available");
		}
		return saveButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initDeleteButton() {
		deleteButton = new JButton();
		deleteButton.setBounds(1, 110, 50, 50);
		deleteButton.setBackground(new Color(0, 0, 0, 0));
		deleteButton.setBorderPainted(false);
		deleteButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				// SensorGUI.this.deleteAction();
			}
		});

		deleteButton.setToolTipText("Click Here to Delete");
		try {
			deleteButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(deleteIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e4) {
			deleteButton.setText("Delete");
			// Library.logError(e4, "Exception in Icon initing: Image /Icons/delete.png Not
			// Available");
		}
		return deleteButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initPauseButton() {
		pauseButton = new JButton();
		pauseButton.setBorderPainted(false);
		pauseButton.setToolTipText("Click Here to Pause");
		pauseButton.setLocation(1, 55);
		pauseButton.setSize(new Dimension(50, 50));
		pauseButton.setBackground(new Color(0, 0, 0, 0));
		pauseButton.setBackground(Color.WHITE);
		pauseButton.setSize(50, 50);
		// this.controlPanel.add(this.Label_Pause);
		try {
			pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(pauseIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e3) {
			pauseButton.setText("Pause");
			// Library.logError(e3, "Exception in Icon initing: Image /Icons/pause.png Not
			// Available");
		}
		return pauseButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 * @return
	 */
	private JButton initPowerButton() {
		powerButton = new JButton();
		powerButton.setBounds(2, 0, 50, 50);
		powerButton.setBackground(new Color(0, 0, 0, 0));
		powerButton.setBorderPainted(false);

		powerButton.setBackground(Color.WHITE);
		powerButton.setBorder(null);

		powerButton.setToolTipText("Click here to exit application");
		try {
			powerButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(powerIcon))
					.getScaledInstance(buttonSize.width, buttonSize.height, 4)));
		} catch (IOException e4) {
			powerButton.setText("Close");
			// Library.logError(e4, "Exception in Icon initing: Image /Icons/power.png Not
			// Available");
		}
		return powerButton;
	}

	/**
	 * @purpose
	 * @date 04-Jun-2022
	 */
	public ControlWindow registerActions() {
		menuButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				if (menuButton.isEnabled()) {
					if (controlPanel.isVisible()) {
						frame.setSize(new Dimension(54, 110));
						mainPanel.setSize(new Dimension(54, 110));
						controlPanel.setVisible(false);
						menuButton.setToolTipText(
								"<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
					} else {
						frame.setSize(new Dimension(54, 560));
						mainPanel.setSize(new Dimension(54, 560));
						controlPanel.setVisible(true);
						menuButton.setToolTipText(
								"<html>Click here to collapse<br>OR Right click to explore Feature Menu</html>");
					}
				}
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
	            final String toolText = menuButton.getToolTipText();
				if (pauseButton.getToolTipText().equalsIgnoreCase("Click Here to Pause")) {
	                	menuButton.setEnabled(false);
	                    try {
							pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(playIcon)).getScaledInstance(buttonSize.width, buttonSize.height, 4)));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	                    pauseButton.setToolTipText("Click Here to Resume");
	                    menuButton.setToolTipText("");
	                    //this.mntmExpand.setEnabled(false);
	            }
	            else {
	                	menuButton.setEnabled(true);
	                	menuButton.setToolTipText(toolText);
	                    try {
							pauseButton.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource(pauseIcon)).getScaledInstance(buttonSize.width, buttonSize.height, 4)));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	                    pauseButton.setToolTipText("Click Here to Pause");
	                    //this.mntmExpand.setEnabled(true);
	                
	            }
			}
		});

		clickPad.addMouseListener(new SwingMouseAdapter() {
			public static final long serialVersionUID = 1L;
			@Override
			public void mouseClicked(final MouseEvent arg0) {
//				if (ActionGUI.leaveControl && !StaticFields.PauseThread) {
//					StaticFields.senGUI.frame.setOpacity(0.0f);
//					SensorGUI.captureScreen();
//					StaticFields.senGUI.frame.setOpacity(1.0f);
//				}
				
			}

		});

		powerButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				AlertPopup.init().type(AlertPopup.WARNING).message("Do you want to exit the application?").button1("Yes", new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				}).button2("No");
			}
		});

		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				// SensorGUI.this.viewAction();
//				SweetAlert s=new SweetAlert(frame,true);
//				s.showAlert();
//				s.setVisible(true);
				
				//Popup p= new Popup(frame,new JFrame(),200,200);
			}
		});
		recordButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(final ActionEvent arg0) {
				//SensorGUI.this.recordAction();
			}
		});
		settingsButton.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent arg0) {
				// S//ensorGUI.this.settingsAction();
		 		ActionWindow.init(ActionWindow.SAVE,ActionWindow.VIEW).show();

			}
		});
		CaptureService cs = new CaptureService();

		clickPad.addMouseListener(new SwingMouseAdapter() {
			private static final long serialVersionUID = 1L;

			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				cs.captureScreenshot();
			}
		});
		cs.addCaptureListener(new CaptureEvent() {
			
			@Override
			public void updateCount(int count) {
				label_Count.setText(""+count);
				
			}
		});

		return this;
	}

//	{

//        (this.label_Document = new JButton()).setBounds(1, 275, 50, 50);
//        this.controlPanel.add(this.label_Document);
//        this.label_Document.setBackground(new Color(0, 0, 0, 0));
//        if (StaticFields.duplicateAvailable) {
//            this.label_Document.setToolTipText("Click here to manage documents (Shortcut unavailable)");
//        }
//        else {
//            this.label_Document.setToolTipText("Click here to manage documents");
//        }
//        this.label_Document.setBorderPainted(false);
//        this.label_Document.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                SensorGUI.this.manageDocumentsActions();
//            }
//        });
//        try {
//            this.label_Document.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/Icons/document.png")).getScaledInstance(this.size.width, this.size.height, 4)));
//        }
//        catch (IOException e3) {
//            this.label_Document.setText("Documents");
//            Library.logError(e3, "Exception in Icon initing: Image /Icons/document.png Not Available");
//        }

//        (this.sensor_panel = new JPanel()).setBackground(new Color(51, 255, 153));
//        this.sensor_panel.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(final MouseEvent arg0) {
//                SensorGUI.this.x = arg0.getXOnScreen() - SensorGUI.this.xx;
//                SensorGUI.this.y = arg0.getYOnScreen() - SensorGUI.this.xy;
//                try {
//                    SettingsPanel.lblLocationx.setText("Location : ( " + SensorGUI.this.x + " , " + SensorGUI.this.y + " )");
//                }
//                catch (Exception ex) {}
//                SensorGUI.this.frame.setLocation(SensorGUI.this.x, SensorGUI.this.y);
//            }
//        });
//        this.sensor_panel.setToolTipText("Click here to take screenshots");
//        this.sensor_panel.addMouseListener(new SwingMouseAdapter() {
//            public static final long serialVersionUID = 1L;
//            
//            @Override
//            public void mouseClicked(final MouseEvent arg0) {
//                if (ActionGUI.leaveControl && !StaticFields.PauseThread) {
//                    StaticFields.senGUI.frame.setOpacity(0.0f);
//                    SensorGUI.captureScreen();
//                    StaticFields.senGUI.frame.setOpacity(1.0f);
//                }
//            }
//            
//            @Override
//            public void mousePressed(final MouseEvent arg0) {
//                SensorGUI.this.xx = arg0.getX();
//                SensorGUI.this.xy = arg0.getY();
//            }
//        });
//        this.sensor_panel.setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
//        this.sensor_panel.setBounds(0, 0, 54, 50);
//        this.Main_panel.add(this.sensor_panel);
//        this.sensor_panel.setLayout(new FlowLayout(1, 5, 5));
//        SensorGUI.label_Count = new JLabel();
//        this.sensor_panel.add(SensorGUI.label_Count);
//        SensorGUI.label_Count.setFont(new Font("Tahoma", 1, 20));
//        try {
//            SensorGUI.label_Count.setText(String.valueOf(new File(StaticFields.property.getString("TempPath")).listFiles().length));
//        }
//        catch (Exception ex) {}
//        (this.label_Menu = new JButton()).setBackground(Color.WHITE);
//        this.label_Menu.setBounds(2, 55, 50, 50);
//        this.Main_panel.add(this.label_Menu);
//        this.label_Menu.setBackground(new Color(0, 0, 0, 0));
//        this.label_Menu.setBorderPainted(false);
//        this.label_Menu.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                if (SensorGUI.this.label_Menu.isEnabled()) {
//                    if (SensorGUI.this.controlPanel.isVisible()) {
//                        SensorGUI.this.frame.setSize(new Dimension(54, 110));
//                        SensorGUI.this.Main_panel.setSize(new Dimension(54, 110));
//                        SensorGUI.this.controlPanel.setVisible(false);
//                        SensorGUI.this.label_Menu.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
//                    }
//                    else {
//                        SensorGUI.this.frame.setSize(new Dimension(54, 560));
//                        SensorGUI.this.Main_panel.setSize(new Dimension(54, 560));
//                        SensorGUI.this.controlPanel.setVisible(true);
//                        SensorGUI.this.label_Menu.setToolTipText("<html>Click here to collapse<br>OR Right click to explore Feature Menu</html>");
//                    }
//                }
//            }
//        });
//        this.label_Menu.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
//        try {
//            this.label_Menu.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/Icons/menu.png")).getScaledInstance(this.size.width, this.size.height, 4)));
//        }
//        catch (IOException e4) {
//            this.label_Menu.setText("Menu");
//            Library.logError(e4, "Exception in Icon initing: Image /Icons/menu.png Not Available");
//        }
//        (this.popupMenu_1 = new JPopupMenu()).setBorder(new MatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
//        this.popupMenu_1.setFont(new Font("Tahoma", 0, 16));
//        this.popupMenu_1.setBackground(Color.WHITE);
//        this.addPopup(this.label_Menu, this.popupMenu_1);
//        (this.mntmExpand = new JMenuItem()).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                if (SensorGUI.this.label_Menu.isEnabled()) {
//                    if (SensorGUI.this.controlPanel.isVisible()) {
//                        SensorGUI.this.frame.setSize(new Dimension(54, 110));
//                        SensorGUI.this.Main_panel.setSize(new Dimension(54, 110));
//                        SensorGUI.this.controlPanel.setVisible(false);
//                        SensorGUI.this.label_Menu.setToolTipText("<html>Click here to expand<br>OR Right click to explore Feature Menu</html>");
//                    }
//                    else {
//                        SensorGUI.this.frame.setSize(new Dimension(54, 560));
//                        SensorGUI.this.Main_panel.setSize(new Dimension(54, 560));
//                        SensorGUI.this.controlPanel.setVisible(true);
//                        SensorGUI.this.label_Menu.setToolTipText("<html>Click here to collapse<br>OR Right click to explore Feature Menu</html>");
//                    }
//                }
//            }
//        });
//        this.mntmExpand.setFont(new Font("Tahoma", 1, 16));
//        this.mntmExpand.setBackground(UIManager.getColor("CheckBox.background"));
//        this.popupMenu_1.add(this.mntmExpand);
//        (this.mntmLaunchIdTool = new JMenuItem("Launch ID Tool")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                SensorGUI.this.IDTool();
//            }
//        });
//        this.mntmLaunchIdTool.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmLaunchIdTool);
//        (this.mntmViewConsole = new JMenuItem("Snipping Tool")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent e) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        new SnippingTool().createAnsShowGui();
//                    }
//                });
//            }
//        });
//        this.mntmViewConsole.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmViewConsole);
//        (this.mntmAddComment = new JMenuItem("Add Comment")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                Library.sortFiles(ViewPanel.files = new File(StaticFields.property.getString("TempPath")).listFiles());
//                String populateComment = StaticFields.comments.get(ViewPanel.files[ViewPanel.files.length - 1].getName());
//                if (populateComment == null) {
//                    populateComment = "";
//                }
//                final PopUp pp = new PopUp("Enter comment for " + ViewPanel.files[ViewPanel.files.length - 1].getName(), "comment", populateComment, "Done", "Cancel");
//                pp.setVisible(true);
//                PopUp.PopDia = pp;
//                PopUp.btnNewButton.addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(final ActionEvent arg0) {
//                        StaticFields.comments.put(ViewPanel.files[ViewPanel.files.length - 1].getName(), pp.txtrExceptionOccuredPlease.getText());
//                        if (ViewPanel.ImageLabel != null) {
//                            ViewPanel.ImageLabel.setToolTipText("<html>Filename : " + ViewPanel.files[ViewPanel.files.length - 1].getName() + "<br>ick here to Zoom Image.<br>Comment:" + StaticFields.comments.get(ViewPanel.files[ViewPanel.files.length - 1].getName()) + "<br><br>Click image to zoom</html>");
//                        }
//                    }
//                });
//                PopUp.PopDia.getRootPane().setDefaultButton(PopUp.btnNewButton);
//            }
//        });
//        this.mntmAddComment.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmAddComment);
//        (this.mntmRefresh = new JMenuItem("Refresh")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                SensorGUI.refresh();
//            }
//        });
//        this.mntmRefresh.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmRefresh);
//        (this.mntmGuide = new JMenuItem("User Guide")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                SensorGUI.this.wait = true;
//                final ToastMsg toast = new ToastMsg("Please Wait...") {
//                    private static final long serialVersionUID = 1L;
//                    
//                    @NoLogging
//                    @Override
//                    public void terminationLogic() throws InterruptedException {
//                        do {
//                            Thread.sleep(100L);
//                        } while (SensorGUI.this.wait);
//                    }
//                };
//                toast.setEndLocation(StaticFields.senGUI.sensor_panel.getLocationOnScreen().x, StaticFields.senGUI.sensor_panel.getLocationOnScreen().y);
//                toast.showToast();
//                final File guideFile = new File(StaticFields.userGuidePath);
//                if (!guideFile.exists()) {
//                    try {
//                        final GitHub g = new GitHub();
//                        final RestAPIs api = new RestAPIs();
//                        final JSONObject JSONObj = new JSONObject(api.HttpGet("https://api.github.com/repos/souvik-sarkar-98/CaptureEasy/releases/latest").toString());
//                        g.downinit(g.getDowninitURL("guide", JSONObj), StaticFields.userGuidePath);
//                    }
//                    catch (Exception e) {
//                        Library.logError(e, String.valueOf(e.getClass().getName()) + " Exception occured ");
//                        final PopUp p = new PopUp("ERROR", "error", String.valueOf(e.getClass().getName()) + " Occured. Please try again.\n\nIf still not resolved then 'Report a Bug' from Feature Menu.", "Ok, I understood", "");
//                        p.setVisible(true);
//                        PopUp.PopDia = p;
//                    }
//                }
//                try {
//                    Desktop.getDesktop().open(guideFile);
//                }
//                catch (IOException e2) {
//                    e2.printStackTrace();
//                }
//                SensorGUI.this.wait = false;
//            }
//        });
//        this.mntmGuide.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmGuide);
//        (this.mntmreport = new JMenuItem("Report Bug")).addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(final ActionEvent arg0) {
//                if (ActionGUI.leaveControl) {
//                    final ToastMsg toast = new ToastMsg("Loading...") {
//                        private static final long serialVersionUID = 1L;
//                        
//                        @NoLogging
//                        @Override
//                        public void terminationLogic() throws InterruptedException {
//                            do {
//                                Thread.sleep(100L);
//                            } while (ActionGUI.leaveControl);
//                        }
//                    };
//                    toast.setEndLocation(StaticFields.senGUI.sensor_panel.getLocationOnScreen().x, StaticFields.senGUI.sensor_panel.getLocationOnScreen().y);
//                    toast.showToast();
//                    final List<String> tabs = new ArrayList<String>();
//                    tabs.add("Bug");
//                    new ActionGUI(tabs);
//                    ActionGUI.dialog.setVisible(true);
//                }
//                else {
//                    ActionGUI.dialog.setAlwaysOnTop(true);
//                    if (ActionGUI.dialog.getState() == 1) {
//                        ActionGUI.dialog.setState(0);
//                    }
//                }
//            }
//        });
//        this.mntmreport.setFont(new Font("Tahoma", 1, 16));
//        this.popupMenu_1.add(this.mntmreport);

//        final boolean bool = StaticFields.property.getBoolean("SensorBTNPanelVisible", false);
//        if (bool) {
//            this.frame.setSize(new Dimension(54, 560));
//            this.Main_panel.setSize(new Dimension(54, 560));
//        }
//        else {
//            this.frame.setSize(new Dimension(54, 110));
//            this.Main_panel.setSize(new Dimension(54, 110));
//        }
//        this.controlPanel.setVisible(bool);
//        final Component[] comp = this.popupMenu_1.getComponents();
//        for (int i = 1; i < comp.length; i += 2) {
//            comp[i].setBackground(Color.WHITE);
//        }
//        Logger.aspectOf().logAllAfterMethod(jp, null);
//    }
//    
//    public static void refresh() {
//        final JoinPoint jp = Factory.makeJP(SensorGUI.ajc$tjp_1, null, null);
//        Logger.aspectOf().logAllBeforeMethod(jp);
//        try {
//            GlobalScreen.unregisterNativeHook();
//            Thread.sleep(100L);
//            GlobalScreen.registerNativeHook();
//        }
//        catch (Exception ex) {}
//        DetectKeypress.key = 0;
//        final File file = new File(StaticFields.property.getString("TempPath"));
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        SensorGUI.label_Count.setText(String.valueOf(file.listFiles().length));
//        Logger.aspectOf().logAllAfterMethod(jp, null);
//	}

}
