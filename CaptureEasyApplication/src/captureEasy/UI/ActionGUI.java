package captureEasy.UI;

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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.JSONException;

import captureEasy.Launch.Application;
import captureEasy.Resources.Library;
import captureEasy.Resources.SoftwareUpdate;

import captureEasy.UI.Components.ManageDocumentPanel;
import captureEasy.UI.Components.RecordPanel;
import captureEasy.UI.Components.SavePanel;
import captureEasy.UI.Components.SettingsPanel;
import captureEasy.UI.Components.UpdatePanel;
import captureEasy.UI.Components.ViewPanel;

public class ActionGUI extends Library  implements ChangeListener,MouseListener,MouseMotionListener
{
	public static JFrame dialog;
	public final JPanel contentPanel = new JPanel();
	public String selectedTab="";
	public JTabbedPane TabbledPanel;
	public boolean finish=false;
	public static final String PRE_HTML = "<html>"
			+ "<p style=\"text-align: center; "
			+ "margin-top: 10px;"
			+ "margin-bottom: 10px;"
			+ "margin-left: 1px;"
			+ "margin-right: 1px;"
			+ "width: 70px\">";
	public static final String POST_HTML = "</p></html>";
	public static boolean tabLoaded=false;
	public static SettingsPanel settingsPanel;
	public ManageDocumentPanel documentPanel;
	public static ViewPanel viewPanel;
	public static SavePanel savePanel;
	public static RecordPanel screenRecord;

	List<String> tabs;
	int i;
	private boolean loadSaveTab=false;
	public static UpdatePanel updatePanel;
	public static int xDialog,yDialog, xyDialog,xxDialog;
	public static boolean leaveControl=true,tagDrop=true;
	public static int redirectingTabID=0;


	//	@SuppressWarnings({ })
	public ActionGUI(List<String> tabs)
	{
		try{Application.sensor.pause();}catch(Exception e){}
		this.tabs=tabs;
		leaveControl=false;
		dialog=new JFrame();
		dialog.setSize(new Dimension(575, 350));
		dialog.setFont(new Font("Dialog", 1, 20));
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setBounds(screensize.width / 2 - 300, screensize.height / 2 - 300, 575, 350);
		dialog.setUndecorated(true);
		dialog.setLocation(screensize.width / 2 - 300, screensize.height / 2 - 300);
		dialog.getContentPane().setLayout(new BorderLayout());
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			icons.add(ImageIO.read(new File(taskbarIcon)));
			dialog.setIconImages(icons);

		} catch (IOException e2) {
			logError(e2,"Exception occured while reading icon");
		}
		dialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				ActionGUI.leaveControl=true;
			}
		});
		contentPanel.setSize(new Dimension(575, 350));
		contentPanel.setBackground(new Color(127, 255, 212));
		this.contentPanel.setFont(new Font("Tahoma", 0, 18));
		this.contentPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		dialog.getContentPane().add(this.contentPanel, "Center");
		dialog.addMouseListener(this);
		dialog.addMouseMotionListener(this);
		{
			TabbledPanel = new JTabbedPane(JTabbedPane.LEFT);
			TabbledPanel.setSize(new Dimension(551, 324));
			TabbledPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			TabbledPanel.setBounds(12, 13, 551, 324);
			TabbledPanel.setBackground(new Color(255, 255, 255));
			TabbledPanel.setOpaque(true);
			TabbledPanel.setAutoscrolls(true);
			TabbledPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			TabbledPanel.setFont(new Font("Tahoma", Font.BOLD, 16));
			TabbledPanel.setPreferredSize(new Dimension(550, 260));
			TabbledPanel.addMouseListener(this);
			TabbledPanel.addChangeListener(this);
			TabbledPanel.addMouseMotionListener(this);
			contentPanel.setLayout(null);
			contentPanel.add(TabbledPanel);


			for(int i=0;i<tabs.size();i++)
			{
				this.i=i;
				if(tabs.get(i).equalsIgnoreCase("Save"))
				{
					savePanel=new SavePanel(TabbledPanel);
					TabbledPanel.addTab("Save", null,savePanel.SaveScrollPane, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Save" + POST_HTML);
					if(i==0)
					{
						TabbledPanel.setSelectedIndex(0);
						savePanel.textField_Filename.requestFocusInWindow();
						savePanel.rdbtnNewDoc.setEnabled(false);
						savePanel.btnDone.setVisible(false);
						savePanel.exitbtn.setVisible(true);
					}
				}
				else if(tabs.get(i).equalsIgnoreCase("View"))
				{
					viewPanel=new ViewPanel(TabbledPanel);
					TabbledPanel.addTab("ViewImages", null, viewPanel.ViewScrollPane, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "View\nImages" + POST_HTML);
					if(i==0)
					{
						TabbledPanel.setSelectedIndex(0);
						viewPanel.lblExit.setEnabled(true);
					}
				}
				else if(tabs.get(i).equalsIgnoreCase("Document"))
				{	
					documentPanel=new ManageDocumentPanel(TabbledPanel);
					TabbledPanel.addTab("ManageDocument", null, documentPanel.DocumentScrollPane, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Manage\nDocument" + POST_HTML);
					if(i==0)
					{
						TabbledPanel.setSelectedIndex(0);
						documentPanel.lblCross.setEnabled(true);
					}

				}
				else if(tabs.get(i).equalsIgnoreCase("Settings"))
				{
					settingsPanel=new SettingsPanel(TabbledPanel);
					TabbledPanel.addTab("Settings", null, settingsPanel.SettingsScrollPane, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Settings" + POST_HTML);
					if(i==0)
					{
						TabbledPanel.setSelectedIndex(0);
						settingsPanel.CancelBtn.setEnabled(true);
					}

				}
				else if(tabs.get(i).equalsIgnoreCase("Record"))
				{
					screenRecord=new RecordPanel(TabbledPanel);
					TabbledPanel.addTab("Screen Record", null, screenRecord.RecordPanel, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Screen\nRecording" + POST_HTML);
					if(i==0)
					{
						TabbledPanel.setSelectedIndex(0);
					}
				}

				else if(tabs.get(i).equalsIgnoreCase("Update"))
				{
					updatePanel=new UpdatePanel(TabbledPanel);
					TabbledPanel.addTab("Settings", null, updatePanel.panel_Update, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Software\nUpdate" + POST_HTML);
				}
				else
				{
					/*actionPanel=new ActionPanel(TabbledPanel);
					TabbledPanel.addTab("", null, actionPanel.ActionPanel, null);
					TabbledPanel.setTitleAt(i, PRE_HTML + "Action" + POST_HTML);*/
				}

			}
		}
		TabbledPanel.setSelectedIndex(0);
	}
	public static String tabName;

	@Override
	public void stateChanged(ChangeEvent arg0) {
		tabName=TabbledPanel.getTitleAt(TabbledPanel.getSelectedIndex()).toString();	
		if(ActionGUI.dialog.isVisible())
		{
			tabLoaded=false;
			new ToastMsg("Loading...",dialog.getBounds().x+430/2+75,dialog.getBounds().y+315/2)
			{
				private static final long serialVersionUID = 1L;
				public void terminationLogic() throws InterruptedException
				{
					do{Thread.sleep(100);}while(!ActionGUI.tabLoaded);
				}
			}.showToast();
		}
		if(tabName.contains("Save"))	
		{
			if(!savePanel.saveLoaded)
			{
				savePanel.loadSaveTab();
				savePanel.SaveScrollPane.add(savePanel.panel_Save_Buttons);
				savePanel.SaveScrollPane.add(savePanel.SavePanel);
				savePanel.btnDone.requestFocusInWindow();
				if(i!=0 && loadSaveTab)
					savePanel.exitbtn.setEnabled(false);
			}

			if(property.getBoolean("showFolderNameField",false))
			{
				savePanel.lblParFol.setVisible(true);
				savePanel.textField_ParFol.setVisible(true);
				savePanel.textField_Filename.setColumns(16);
			}
			else
			{
				savePanel.lblParFol.setVisible(false);
				savePanel.textField_ParFol.setVisible(false);
				savePanel.textField_Filename.setColumns(22);
			}
			if(savePanel.textField_Filename.getText().replaceAll("\\s", "").equals(""))
			{
				savePanel.btnDone.setVisible(false);
			}
			savePanel.lblDoYouWant.setFont(new Font("Tahoma", Font.PLAIN, 14));
			savePanel.lblDoYouWant.setToolTipText("Do you want to continue with current screenshots ?");
			savePanel.textField_Filename.requestFocusInWindow();
			if(savePanel.lblChooseFile.isVisible())
			{
				savePanel.lblParFol.setVisible(false);
				savePanel.textField_ParFol.setVisible(false);
			}
			dialog.getRootPane().setDefaultButton(savePanel.btnDone);
			tabLoaded=true;
		}
		else if(tabName.contains("View"))
		{
			if(!viewPanel.viewLoaded)
			{
				viewPanel.loadViewTab();
				viewPanel.ViewScrollPane.add(viewPanel.panel_Image);
				viewPanel.ViewScrollPane.add(viewPanel.panel_Button);
			}

			///if(property.getBoolean("showFolderNameField",false))
			ViewPanel.files=new File(property.getString("TempPath")).listFiles();
			sortFiles(ViewPanel.files);
			if(ViewPanel.ImageLabel.getToolTipText()==null)
				ViewPanel.imgId=ViewPanel.files.length-1;

			try {
				if(ViewPanel.files.length>0)
				{
					ViewPanel.ImageLabel.setIcon(new ImageIcon(ImageIO.read(ViewPanel.files[ViewPanel.imgId]).getScaledInstance(410,250, java.awt.Image.SCALE_SMOOTH)));
					ViewPanel.ImageLabel.setToolTipText("<html>Filename : "+ViewPanel.files[ViewPanel.imgId].getName()+"<br><br>Click image to zoom</html>");
					if(comments.get(ViewPanel.files[ViewPanel.imgId].getName())!=null)
						ViewPanel.ImageLabel.setToolTipText("<html>Filename : "+ViewPanel.files[ViewPanel.imgId].getName()+"<br>Comment:"+comments.get(ViewPanel.files[ViewPanel.imgId].getName())+"<br><br>Click image to zoom</html>");

				}
				/*else
				{
					ViewPanel.ImageLabel.setIcon(null);
					ViewPanel.ImageLabel.setText("                                             You have nothing to view");
					ViewPanel.ImageLabel.setToolTipText("");
					viewPanel.label_SetComment.setEnabled(false);
					viewPanel.label_Next.setEnabled(false);
					viewPanel.label_Prev.setEnabled(false);
					viewPanel.label_Delete.setEnabled(false);
					viewPanel.Label_FullView.setEnabled(false);
					for(int i=0;i<TabbledPanel.getTabCount();i++)
					{
						if(TabbledPanel.getTitleAt(i).toLowerCase().contains("save"))
						{
							TabbledPanel.removeTabAt(i);
							viewPanel.lblExit.setEnabled(true);
							break;
						}
					}
				}*/
			} catch (IOException e) {
				logError(e,"Exception occured while loading imageviewer");
			}
			tabLoaded=true;
		}
		else if(tabName.contains("Manage"))
		{
			if(!documentPanel.isloaded)
			{
				try{
					documentPanel.loadDocumentsTab(property.getString("DocPath",""));
					documentPanel.DocumentScrollPane.add(documentPanel.panel_Selection);
					documentPanel.DocumentScrollPane.add(documentPanel.panel_View);
					documentPanel.showRootFile();
					documentPanel.label_SearchBtn.requestFocusInWindow();
				}catch(Exception e){
					logError(e,"Exception occured while loading Manage documents tab");
				}
			}
			else if(SettingsPanel.DocPath_Previous!=null && !SettingsPanel.DocPath_Previous.equalsIgnoreCase(property.getString("DocPath")))
			{
				documentPanel.DocumentScrollPane.remove(documentPanel.panel_View);
				documentPanel.loadDocumentsTab(property.getString("DocPath",""));
				documentPanel.DocumentScrollPane.add(documentPanel.panel_View);
				documentPanel.showRootFile();
				documentPanel.label_SearchBtn.requestFocusInWindow();
			}
			else if(ManageDocumentPanel.changed)
			{
				System.out.println("this is me");
				
			}
			tabLoaded=true;
		}
		else if(tabName.contains("Record"))
		{
			if(!screenRecord.isLoaded)
			{
				try{
					screenRecord.loadRecordPanel();
					screenRecord.RecordPanel.add(screenRecord.panel_Control);

				}catch(Exception e){
					logError(e,"Exception occured while loading Screen Recording tab");
				}
			}
			if(property.getBoolean("showFolderNameField",false))
			{
				screenRecord.lblFolderName.setVisible(true);
				screenRecord.textField_Foldername.setVisible(true);
				screenRecord.textField_Filename.setColumns(16);
			}
			else
			{
				screenRecord.lblFolderName.setVisible(false);
				screenRecord.textField_Foldername.setVisible(false);
				screenRecord.textField_Filename.setColumns(22);
			}
			screenRecord.textField_Filename.requestFocusInWindow();
			tabLoaded=true;
		}
		else if(tabName.contains("Settings"))
		{	
			selectedTab="Settings";
			ActionGUI.tagDrop=false;
			if(!settingsPanel.loadSettingsTab)
			{
				settingsPanel.loadSettingsTab();
				settingsPanel.SettingsScrollPane.add(settingsPanel.SettingsPane);
				settingsPanel.loadSettingsTab=true;
			}
			settingsPanel.textField_DocDestFolder.setText(property.getString("DocPath",""));
			settingsPanel.chckbxShowFilderNameField.setSelected(property.getBoolean("showFolderNameField",false));
			if(property.getBoolean("showFolderNameField",false))
			{
				settingsPanel.chckbxSetFoldernameMandatory.setVisible(true);
				settingsPanel.chckbxSetFoldernameMandatory.setSelected(property.getBoolean("setFolderNameMandatory",false));
			}
			if(!settingsPanel.chckbxShowFilderNameField.isSelected())
			{
				settingsPanel.chckbxSetFoldernameMandatory.setVisible(false);
				settingsPanel.chckbxSetFoldernameMandatory.setSelected(false);
			}
			settingsPanel.chckbxAutoUpdate.setSelected(property.getBoolean("autoupdate",false)); 
			SettingsPanel.lblLocationx.setText("Location : ( "+property.getInteger("Xlocation",screensize.width-160)+" , "+property.getInteger("Ylocation",screensize.height/2+100)+" )");
			settingsPanel.SettingsPane_Recordpanel_RecordFlag.setSelected(Boolean.valueOf(property.getString("ScreenRecording")));
			settingsPanel.comboBox_ImageFormat.setSelectedItem(property.getString("ImageFormat","png"));
			ActionGUI.tagDrop=false;
			settingsPanel.comboBox_CaptureKey.setSelectedItem(property.getString("CaptureKey","PrtSc"));
			if(!TabbledPanel.getTitleAt(0).contains("Settings"))
			{
				settingsPanel.btnUpdateFrameLocation.setEnabled(false);
			}

			tabLoaded=true;
		}
		else if(tabName.contains("Update"))
		{
			/*new Thread(new Runnable(){
				@Override
				public void run() {
					PopUp window = new PopUp("INFORMATION","info","\n\n                  Loading ...","Close","");
					window.setVisible(true);
					window.lblIcon.setBounds(20, 45,50,50);
				}

			}).start();;*/
			SoftwareUpdate su= new SoftwareUpdate();
			try {
				if(su.checkForUpdates())
				{
					updatePanel.loadNeedUpdate();
					updatePanel.panel_Update.add(updatePanel.panel_UpdateYes);
					updatePanel.lblName.setText(" Name : "+su.JSONObj.get("name"));
					updatePanel.lblVersion.setText(" Version  : "+su.JSONObj.get("tag_name"));
					updatePanel.lblPublish.setText(" Published at : "+su.JSONObj.get("published_at"));
					updatePanel.lblPublish.setToolTipText(su.JSONObj.get("published_at").toString());

					//if download flag is false
					UpdatePanel.lblprogressflag.setVisible(false);
				}
				else
				{
					updatePanel.loadUpdated();
					updatePanel.lblLastUpdatedOn.setText(" Last updated on : "+versionInfo.getString("LasUpdateDate","Data not available"));
					updatePanel.lblTime.setText(" Time : "+versionInfo.getString("LasUpdateTime","Data not available"));
					updatePanel.lblCurrentVersion.setText(" Current version : "+versionInfo.getString("CurrentVersion","Base"));

				}

			} catch (JSONException e) {
				logError(e,"Exception occured while checking for updates");
			}

			tabLoaded=true;
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		xDialog = arg0.getXOnScreen();
		yDialog = arg0.getYOnScreen();
		dialog.setLocation(xDialog - xxDialog, yDialog - xyDialog); 		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		xxDialog = e.getX();
		xyDialog = e.getY();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

}