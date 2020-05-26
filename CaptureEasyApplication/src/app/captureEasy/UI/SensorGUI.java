package app.captureEasy.UI;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.SwingMouseAdapter;

import app.captureEasy.Launch.Application;
import app.captureEasy.Resources.Library;
import app.captureEasy.Resources.SharedResources;
import app.captureEasy.UI.Components.PopUp;
import app.captureEasy.UI.Components.ToastMsg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.FlowLayout;
import javax.swing.JPopupMenu;
import javax.swing.Timer;

import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Image;

public class SensorGUI extends Library 
{
	public int xx,xy,x,y;
	JPopupMenu popupMenu;
	public JFrame frame;
	public JButton label_delete;
	public JButton lebel_Power;
	public JButton Label_Pause ;
	public JButton label_Document ;
	public JButton label_Save;
	public JButton label_View;
	ActionGUI act;
	public static PopUp window;
	public JButton label_Settings;
	public static boolean isRecording=false;
	Timer timer;
	public static boolean isExpandable=true;
	Dimension size= new Dimension(50,50);
	public JButton label_Menu;
	public static JLabel label_Count;
	protected int c;
	public JPanel sensor_panel;
	public static boolean clickable=true;
	JPanel Main_panel;
	public JPanel button_panel;
	PopUp p;

	public SensorGUI()
	{
		Main_panel = new JPanel();
		SensorGUIInit();
	}
	public void SensorGUIInit()
	{
		frame=new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.setUndecorated(true);
		frame.getContentPane().add(Main_panel);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(taskbarIcon));
		frame.setSize(new Dimension(54, 110));
		frame.setAlwaysOnTop(true);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				closeApplication(true);                
			}
		});
		frame.addWindowFocusListener(new WindowFocusListener() {	
			public void windowGainedFocus(WindowEvent e) {	
			}

			@Override
			public void windowLostFocus(WindowEvent arg0) {
			}
		});

		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			frame.setIconImages(icons);

		} catch (IOException e2) {
			logError(e2,"Exception occured while reading icon");
		}

		frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
		frame.setAlwaysOnTop(true);

		frame.setBackground(new Color(0,0,0,0));
		frame.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void mousePressed(MouseEvent e) {

				xx = e.getX();
				xy = e.getY();

			}
		});
		frame.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {

				x = arg0.getXOnScreen()- xx;
				y = arg0.getYOnScreen()- xy;
				try{SettingsPanel.lblLocationx.setText("Location : ( "+x+" , "+y+" )");}catch(Exception e){}
				frame.setLocation(x , y );  
				//tm.setEndLocation(500,500);

			}
		});

		Main_panel.setBounds(0, 0, 54, 110);
		Main_panel.setBackground(Color.WHITE);
		Main_panel.setBorder(null);
		Main_panel.setLayout(null);
		Main_panel.setBackground(new Color(0,0,0,0));


		button_panel = new JPanel();
		button_panel.setBorder(null);
		button_panel.setBackground(Color.WHITE);
		button_panel.setBounds(0, 115, 54, 500);
		Main_panel.add(button_panel);
		button_panel.setLayout(null);
		button_panel.setBackground(new Color(0,0,0,0));

		Label_Pause = new JButton();
		Label_Pause.setBorderPainted(false);
		Label_Pause.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				playPauseAction();
			}
		});
		if(duplicateAvailable)
			Label_Pause.setToolTipText("Click Here to Pause (Shortcut unavailable)");
		else
			Label_Pause.setToolTipText("Click Here to Pause (CRTL+1)");
		Label_Pause.setLocation(1, 55);
		Label_Pause.setSize(new Dimension(50, 50));
		Label_Pause.setBackground(new Color(0,0,0,0));
		Label_Pause.setBackground(Color.WHITE);
		Label_Pause.setSize(50,50);
		button_panel.add(Label_Pause);
		try {
			Label_Pause.setIcon(new ImageIcon(ImageIO.read(new File(pauseIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e1) {
			Label_Pause.setText("Pause");logError(e1,"Exception in Icon loading: Image "+pauseIcon+" Not Available");

		}


		lebel_Power = new JButton();
		lebel_Power.setBounds(2, 0, 50, 50);
		button_panel.add(lebel_Power);
		lebel_Power.setBackground(new Color(0,0,0,0));
		lebel_Power.setBorderPainted(false);
		lebel_Power.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				PopUp p=new PopUp("Confirm Exit","warning","Do you want to exit the application?\n","Yes","No");
				PopUp.PopDia=p;
				PopUp.btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						closeApplication(true);
					}

				});
				JButton restart=new JButton("Restart");
				restart.setVisible(true);
				restart.setBackground(new Color(204, 204, 255));
				restart.setFont(new Font("Tahoma", Font.BOLD, 16));
				restart.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try{ActionGUI.dialog.setAlwaysOnTop(true);}catch(Exception e){}
						p.dispose();
						PopUp.control=true;
						autoRestart();

					}
				});
				p.panel_button.add(restart);
				try{frame.setAlwaysOnTop(true);}catch(Exception e5){}
			}
		});
		
		lebel_Power.setBackground(Color.WHITE);
		lebel_Power.setBorder(null);
		if(duplicateAvailable)
			lebel_Power.setToolTipText("Click here to exit application (Shortcut unavailable)");
		else
			lebel_Power.setToolTipText("Click here to exit application (CTRL+0)");

		try{
			lebel_Power.setIcon(new ImageIcon(ImageIO.read(new File(powerIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			lebel_Power.setText("Close");logError(e,"Exception in Icon loading: Image "+powerIcon+" Not Available");
		}
		label_Document = new JButton();
		label_Document.setBounds(1, 275, 50, 50);
		button_panel.add(label_Document);
		label_Document.setBackground(new Color(0,0,0,0));
		if(duplicateAvailable)
			label_Document.setToolTipText("Click here to manage documents (Shortcut unavailable)");
		else
			label_Document.setToolTipText("Click here to manage documents (CTRL+5)");
		label_Document.setBorderPainted(false);
		label_Document.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				manageDocumentsActions();
			}
		});
		
		try {
			label_Document.setIcon(new ImageIcon(ImageIO.read(new File(documentIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));	
		} catch (IOException e1) {
			label_Document.setText("Documents");logError(e1,"Exception in Icon loading: Image "+documentIcon+" Not Available");
		}

		label_View = new JButton();
		label_View.setBorderPainted(false);
		label_View.setBounds(1, 220, 50, 50);
		button_panel.add(label_View);
		label_View.setBackground(new Color(0,0,0,0));
		if(duplicateAvailable)
			label_View.setToolTipText("Click here to view screenshot (Shortcut unavailable)");
		else
			label_View.setToolTipText("Click here to view screenshot (CTRL+4)");

		label_View.setPreferredSize(new Dimension(50, 50));
		label_View.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewAction();
			}
			
		});
		try {
			label_View.setIcon(new ImageIcon(ImageIO.read(new File(viewIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e1) {
			label_View.setText("View");logError(e1,"Exception in Icon loading: Image "+viewIcon+" Not Available");
		}


		label_Save = new JButton();
		label_Save.setName("SAVE");
		label_Save.setBounds(1, 165, 50, 50);
		button_panel.add(label_Save);
		label_Save.setBackground(new Color(0,0,0,0));
		if(duplicateAvailable)
			label_Save.setToolTipText("Click here to Save (Shortcut unavailable)");
		else
			label_Save.setToolTipText("Click here to Save (CTRL+3)");
		label_Save.setBorderPainted(false);
		label_Save.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveAction();
			}
			
		});
		
		try{
			label_Save.setIcon(new ImageIcon(ImageIO.read(new File(saveIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e1) {
			label_Save.setText("Save");logError(e1,"Exception in Icon loading: Image "+saveIcon+" Not Available");
		}


		label_delete = new JButton();
		label_delete.setBounds(1, 110, 50, 50);
		button_panel.add(label_delete);
		label_delete.setBackground(new Color(0,0,0,0));
		
		label_delete.setBorderPainted(false);
		label_delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteAction();
			}
			
		});
		
		if(duplicateAvailable)
			label_delete.setToolTipText("Click Here to Delete (Shortcut unavailable)");
		else
			label_delete.setToolTipText("Click Here to Delete (CTRL+2)");
		try{
			label_delete.setIcon(new ImageIcon(ImageIO.read(new File(deleteIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			label_delete.setText("Delete");logError(e,"Exception in Icon loading: Image "+deleteIcon+" Not Available");
		}


		sensor_panel = new JPanel();
		sensor_panel.setBackground(new Color(51, 255, 153));
		sensor_panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				x = arg0.getXOnScreen()-xx;
				y = arg0.getYOnScreen()-xy;
				try{SettingsPanel.lblLocationx.setText("Location : ( "+x+" , "+y+" )");}catch(Exception e){}
				frame.setLocation(x, y);
			}
		});
		sensor_panel.setToolTipText("Click here to take screenshots");
		sensor_panel.addMouseListener(new SwingMouseAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(ActionGUI.leaveControl && !SharedResources.PauseThread)
				{
					captureScreen();
				}
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				xx=arg0.getX();
				xy=arg0.getY();
			}
		});
		sensor_panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		sensor_panel.setBounds(0, 0, 54, 50);
		Main_panel.add(sensor_panel);
		sensor_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		label_Count = new JLabel();
		sensor_panel.add(label_Count);
		label_Count.setFont(new Font("Tahoma", Font.BOLD, 20));

		label_Menu = new JButton();
		label_Menu.setBackground(Color.WHITE);
		label_Menu.setBounds(2, 55, 50, 50);
		Main_panel.add(label_Menu);
		label_Menu.setBackground(new Color(0,0,0,0));
		label_Menu.setBorderPainted(false);
		label_Menu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(label_Menu.isEnabled()){
					if( button_panel.isVisible())
					{
						frame.setSize(new Dimension(54, 110));
						Main_panel.setSize(new Dimension(54, 110));
						button_panel.setVisible(false);
						label_Menu.setToolTipText("Click here to expand");
					}
					else
					{
						frame.setSize(new Dimension(54, 560));
						Main_panel.setSize(new Dimension(54, 560));
						button_panel.setVisible(true);
						label_Menu.setToolTipText("Click here to collapse");
					}
				}
			}
			
		});
		
		label_Menu.setToolTipText("Click here to expand");
		try{
			label_Menu.setIcon(new ImageIcon(ImageIO.read(new File(menuIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));

		} catch (IOException e) {
			label_Menu.setText("Menu");logError(e,"Exception in Icon loading: Image "+menuIcon+" Not Available");
		}

		/*frame.setSize(new Dimension(54, 500));
		Main_panel.setSize(new Dimension(54, 500));
		button_panel.setSize(new Dimension(54, 500));*/

		label_Settings = new JButton("");
		label_Settings.setSize(new Dimension(50, 50));
		label_Settings.setBounds(1, 330, 50, 50);
		button_panel.add(label_Settings);
		label_Settings.setBackground(new Color(0,0,0,0));
		label_Settings.setBorderPainted(false);
		label_Settings.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				settingsAction();
			}
			
		});
		
		
		if(duplicateAvailable)
			label_Settings.setToolTipText("Click Here for settings (Shortcut unavailable)");
		else
			label_Settings.setToolTipText("Click Here for settings (CTRL+6)");
		//
		try{
			label_Settings.setIcon(new ImageIcon(ImageIO.read(new File(settingIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			label_Settings.setText("Settings");
			logError(e,"Exception in Icon loading: Image "+settingIcon+" Not Available");
		}
		
				label_Record = new JButton("");
				label_Record.setBorderPainted(false);
				label_Record.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						recordAction();
					}
					
				});
				if(duplicateAvailable)
					label_Record.setToolTipText("Click here to record screen (Shortcut unavailable)");
				else
					label_Record.setToolTipText("Click here to record screen (CTRL+7)");
				
				label_Record.setBounds(1, 387, 50, 50);
				
				button_panel.add(label_Record);
				label_Record.setBackground(new Color(0,0,0,0));
		try{
			label_Record.setIcon(new ImageIcon(ImageIO.read(new File(recordIcon)).getScaledInstance(50,50, java.awt.Image.SCALE_SMOOTH)));
		} catch (IOException e) {
			label_Record.setText("Record");
			logError(e,"Exception in Icon loading: Image "+recordIcon+" Not Available");
		}
		boolean bool=property.getBoolean("SensorBTNPanelVisible",false);
		if(bool)
		{
			frame.setSize(new Dimension(54, 560));
			Main_panel.setSize(new Dimension(54, 560));
		}
		else
		{
			frame.setSize(new Dimension(54, 110));
			Main_panel.setSize(new Dimension(54, 110));
		}
		button_panel.setVisible(bool);	
	}

	String toolText;
	public  JButton label_Record;
	public void deleteAction(){
		if(ActionGUI.leaveControl)
		{
			tm=new ToastMsg("Loading...")
			{ 
				private static final long serialVersionUID = 1L;
				public void terminationLogic() throws InterruptedException
				{
					try{do{
						Thread.sleep(100);
					}while(!p.isVisible());}catch(Exception e){}	
				}
			};
			if(button_panel.isVisible())
				tm.setEndLocation(label_delete.getLocationOnScreen().x,label_delete.getLocationOnScreen().y);
			else
				tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

			tm.showToast();
			if(IsEmpty(property.getString("TempPath")))
			{
				
				p=new PopUp("INFORMATION","Info","You have nothing to delete !! ","Ok, I understood","");
				PopUp.PopDia=p;
				p.setVisible(true);
				timer =new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PopUp.control=true;
						p.dispose();
						timer.stop();
					}
				});
				timer.start();
				try{frame.setAlwaysOnTop(true);}catch(Exception e5){}
			}
			else
			{
				p=new PopUp("Confirm Delete","warning","Are you sure that you want to delete all screenshots?","Yes","No");
				PopUp.PopDia=p;
				PopUp.btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Library.c=0;
						property.setProperty("TempPath",createTemp());
						comments.clear();
					}
				});
			}
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
				ActionGUI.dialog.setState(JFrame.NORMAL);
		}
	}
	public void saveAction()
	{
		if(ActionGUI.leaveControl )
		{
			if(IsEmpty(property.getString("TempPath")))
			{
				PopUp p= new PopUp("INFORMATION","Info","You have nothing to save !! ","Ok, I understood","");
				PopUp.PopDia=p;
				p.setVisible(true);
				timer=new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						p.dispose();
						PopUp.control=true;
						timer.stop();
					}
				});
				timer.start();
				try{frame.setAlwaysOnTop(true);}catch(Exception e5){}
			}
			else
			{
				super.tm =new ToastMsg("Loading..."){
					private static final long serialVersionUID = 1L;
					public void terminationLogic() throws InterruptedException
					{
						try{do{
							Thread.sleep(100);
						}while(!ActionGUI.dialog.isVisible());}catch(Exception e){}
					}
				};
				if(button_panel.isVisible())
					tm.setEndLocation(label_Save.getLocationOnScreen().x,label_Save.getLocationOnScreen().y);
				else
					tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

				tm.showToast();
				//tm.setText("Exception occured!! Please try Again.");
				

				List<String> tabs=new ArrayList<String>();
				tabs.add("Save");
				tabs.add("View");
				tabs.add("Document");
				tabs.add("Settings");

				new ActionGUI(tabs);
				ActionGUI.dialog.setVisible(true);
				ActionGUI.savePanel.textField_Filename.requestFocusInWindow();
				ActionGUI.savePanel.rdbtnNewDoc.setEnabled(false);
				ActionGUI.savePanel.btnDone.setVisible(false);

			}
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
				ActionGUI.dialog.setState(JFrame.NORMAL);
		}
	}
	public void viewAction()
	{
		if(ActionGUI.leaveControl)
		{
			if(IsEmpty(property.getString("TempPath")))
			{
				PopUp p=new PopUp("INFORMATION","Info","You have nothing to view !! ","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
				
				timer=new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						PopUp.PopDia.dispose();
						PopUp.control=true;
						timer.stop();
					}
				});
				timer.start();
				try{frame.setAlwaysOnTop(true);}catch(Exception e5){}
			}
			else
			{
				tm=new ToastMsg("Loading...")
				{
					private static final long serialVersionUID = 1L;
					public void terminationLogic() throws InterruptedException
					{
						do{
							Thread.sleep(100);
							}while(!ActionGUI.dialog.isVisible());	
					}
				};
				if(button_panel.isVisible())
					tm.setEndLocation(label_View.getLocationOnScreen().x,label_View.getLocationOnScreen().y);
				else
					tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

				tm.showToast();
				List<String> tabs=new ArrayList<String>();
				tabs.add("View");
				tabs.add("Save");
				tabs.add("Document");
				tabs.add("Settings");
				new ActionGUI(tabs);
				ActionGUI.dialog.setVisible(true);
			}
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
				ActionGUI.dialog.setState(JFrame.NORMAL);
		}
	}
	public void manageDocumentsActions()
	{
		if(ActionGUI.leaveControl)
		{
			tm=new ToastMsg("Loading...")
			{
				private static final long serialVersionUID = 1L;
				public void terminationLogic() throws InterruptedException
				{
					do{
					Thread.sleep(100);
					}while(!ActionGUI.dialog.isVisible());	
				}
				
			};
			//tm.setLocation(frame.getBounds().x-125,label_Document.getBounds().y+frame.getBounds().y+sensor_panel.getBounds().height+label_Menu.getBounds().height+15);
			if(button_panel.isVisible())
				tm.setEndLocation(label_Document.getLocationOnScreen().x,label_Document.getLocationOnScreen().y);
			else
				tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

			tm.showToast();
			List<String> tabs=new ArrayList<String>();
			tabs.add("Document");
			if(!IsEmpty(property.getString("TempPath")))
			{
				tabs.add("Save");
				tabs.add("View");
			}
			tabs.add("Settings");
			frame.setAlwaysOnTop(false);
			new ActionGUI(tabs);

			ActionGUI.dialog.setVisible(true);
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
				ActionGUI.dialog.setState(JFrame.NORMAL);
		}
	}
	
	public void settingsAction(){
		if(ActionGUI.leaveControl)
		{
			//when not visible then set message beside menu
			tm=new ToastMsg("Loading...")
			{
				private static final long serialVersionUID = 1L;
				public void terminationLogic() throws InterruptedException
				{
					do{
					Thread.sleep(100);
					}while(!ActionGUI.dialog.isVisible());	
				}
			};
			if(button_panel.isVisible())
				tm.setEndLocation(label_Settings.getLocationOnScreen().x,label_Settings.getLocationOnScreen().y);
			else
				tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

			tm.showToast();

			List<String> tabs=new ArrayList<String>();
			tabs.add("Settings");
			tabs.add("Update");
			new ActionGUI(tabs);
			try{ActionGUI.settingsPanel.DuplicateWindow.setEnabled(true);}catch(Exception e){}
			ActionGUI.dialog.setVisible(true);	
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
				ActionGUI.dialog.setState(JFrame.NORMAL);
		}
	}
	public void recordAction(){

		if(ActionGUI.leaveControl)
		{
			tm=new ToastMsg("Loading...")
			{
				private static final long serialVersionUID = 1L;
				public void terminationLogic() throws InterruptedException
				{
					do{
					Thread.sleep(100);
					}while(!ActionGUI.dialog.isVisible());	
				}
			};
			if(button_panel.isVisible())
				tm.setEndLocation(label_Record.getLocationOnScreen().x,label_Record.getLocationOnScreen().y);
			else
				tm.setEndLocation(label_Menu.getLocationOnScreen().x,label_Menu.getLocationOnScreen().y);

			tm.showToast();

			List<String> tabs=new ArrayList<String>();
			tabs.add("Record");
			tabs.add("Settings");
			
			new ActionGUI(tabs);

			ActionGUI.dialog.setVisible(true);	
		}
		else
		{
			ActionGUI.dialog.setAlwaysOnTop(true);
			if(ActionGUI.dialog.getState()==JFrame.ICONIFIED)
			{
				ActionGUI.dialog.setState(JFrame.NORMAL);
			}
		}
	}
	public void playPauseAction()
	{
		if(label_Count.isVisible())
		{
		String toolText=label_Menu.getToolTipText();
		if(Label_Pause.getToolTipText().equalsIgnoreCase("Click Here to Pause"))
		{
			try{
				label_Menu.setEnabled(false);
				Label_Pause.setIcon(new ImageIcon(ImageIO.read(new File(playIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
				Label_Pause.setToolTipText("Click Here to Resume");
				label_Menu.setToolTipText("");;
				SharedResources.PauseThread=true;
			}catch (IOException e3) {Label_Pause.setText("Play");logError(e3,"Exception in Icon loading: Image "+playIcon+" Not Available");}
		}
		else
		{
			try{	
				label_Menu.setEnabled(true);
				label_Menu.setToolTipText(toolText);;
				Label_Pause.setIcon(new ImageIcon(ImageIO.read(new File(pauseIcon)).getScaledInstance(size.width,size.height, java.awt.Image.SCALE_SMOOTH)));
				Label_Pause.setToolTipText("Click Here to Pause");
				SharedResources.PauseThread=false;

			}catch (IOException e3) {
				Label_Pause.setText("Pause"); 
				logError(e3,"Exception in Icon loading: Image "+pauseIcon+" Not Available");}
		}
		}
	}
	public void autoRestart()
	{
		try{ActionGUI.dialog.dispose();
		}catch(Exception e5){}
		frame.dispose();
		SharedResources.stopThread=true;
		String comm="";int c=0;
		File[] f=new File(property.getString("TempPath")).listFiles();
		if(f.length>0)
		{
			for(int i=0;i<f.length;i++)
			{
				if(comments.get(f[i].getName())!=null)
				{
					if(c!=0)
						comm=comm+"_";
					comm=comm+f[i].getName()+"->"+comments.get(f[i].getName());
					c++;
				}
			}
			property.setProperty("Comments",comm);
			property.setProperty("TempCode",new File(property.getString("TempPath")).getName());
		}

		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Application().launch();

	}
	public void closeApplication(boolean true_If_Close_false_If_Restart)
	{
		try{ActionGUI.dialog.dispose();}catch(Exception e5){}
		try{frame.dispose();}catch(Exception e5){}

		SharedResources.stopThread=true;
		String comm="";int c=0;
		File[] f=new File(property.getString("TempPath")).listFiles();
		if(f.length>0)
		{
			for(int i=0;i<f.length;i++)
			{
				if(comments.get(f[i].getName())!=null)
				{
					if(c!=0)
						comm=comm+"_";
					comm=comm+f[i].getName()+"->"+comments.get(f[i].getName());
					c++;
				}
			}
			property.setProperty("Comments",comm);
			property.setProperty("TempCode",new File(property.getString("TempPath")).getName());
		}

		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e1) {
			e1.printStackTrace();
		}
		try {
			if(true_If_Close_false_If_Restart)
				Runtime.getRuntime().exec("Taskkill /f /PID "+getPID());
			else
			{
				Runtime.getRuntime().exec("Taskkill /f /im javaw.exe ");
			}

		} catch (IOException e) {
			logError(e,"");
		} 
	}


}