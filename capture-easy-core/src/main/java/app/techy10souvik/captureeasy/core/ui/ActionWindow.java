package app.techy10souvik.captureeasy.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.techy10souvik.captureeasy.common.util.SystemUtil;
import app.techy10souvik.captureeasy.core.ui.components.SavePanel;
import app.techy10souvik.captureeasy.core.ui.components.SettingsPanel;

/**
 * @author Souvik Sarkar
 * @createdOn 04-Jun-2022
 * @purpose
 */
public class ActionWindow implements MouseListener, MouseMotionListener,ChangeListener {
	public static final int SAVE=0,VIEW=1,RECORD=2,SETTINGS=3;
	private static JFrame dialog;
	private JTabbedPane tabbledPanel;
	private static final String PRE_HTML = "<html>" + "<p style=\"text-align: center; " + "margin-top: 10px;"
			+ "margin-bottom: 10px;" + "margin-left: 1px;" + "margin-right: 1px;" + "width: 70px\">";
	private static final String POST_HTML = "</p></html>";
	private static final String taskbarIcon = "/icons/taskbar_icon.png";;
	private int xDialog, yDialog, xyDialog, xxDialog;

	/**
	 * 
	 */
	public ActionWindow(int...codes) {
		if(dialog == null) {
			initGUI();
			for(int code:codes) {
				switch(code) {
				case 0: initSaveTab(); break;
				case 1: initViewTab(); break;
				case 2: initRecordTab(); break;
				case 4: initSettingsTab(); break;
				}
			}
		}
	}

	/**
	 * @purpose 
	 * @date 12-Jun-2022
	 */
	private void initSaveTab() {
		if(tabbledPanel != null) {
			SavePanel sp=SavePanel.init(dialog);
			tabbledPanel.addTab("Save", null,sp.getPanel(), null);
			tabbledPanel.setTitleAt(tabbledPanel.getTabRunCount()-1, PRE_HTML + "Save" + POST_HTML);
		}		
	}

	/**
	 * @purpose 
	 * @date 12-Jun-2022
	 */
	private void initViewTab() {
		if(tabbledPanel != null) {
			SavePanel sp=SavePanel.init(dialog);
			tabbledPanel.addTab("View", null,sp.getPanel(), null);
			tabbledPanel.setTitleAt(tabbledPanel.getTabRunCount()-1, PRE_HTML + "View" + POST_HTML);
		}
	}

	/**
	 * @purpose 
	 * @date 12-Jun-2022
	 */
	private void initRecordTab() {
		if(tabbledPanel != null) {
			SavePanel sp=SavePanel.init(dialog);
			tabbledPanel.addTab("Record", null,sp.getPanel(), null);
			tabbledPanel.setTitleAt(tabbledPanel.getTabRunCount()-1, PRE_HTML + "Record" + POST_HTML);
		}
	}

	/**
	 * @purpose 
	 * @date 12-Jun-2022
	 */
	private void initSettingsTab() {
		if(tabbledPanel != null) {
			SettingsPanel sp=SettingsPanel.init(dialog);
			tabbledPanel.addTab("Settings", null,sp.getPanel(), null);
			tabbledPanel.setTitleAt(tabbledPanel.getTabRunCount()-1, PRE_HTML + "Settings" + POST_HTML);
		}
	}

	/**
	 * @purpose 
	 * @date 11-Jun-2022
	 */
	public void loadSettingsTab() {
		
		
	}

	/**
	 * @purpose 
	 * @date 11-Jun-2022
	 */
	public void loadRecordTab() {
		
		
	}

	/**
	 * @purpose 
	 * @date 11-Jun-2022
	 */
	public void loadViewTab() {
		
		
	}

	/**
	 * @purpose 
	 * @date 11-Jun-2022
	 */
	public void loadSaveTab() {
		
		
	}

	public static ActionWindow init(int...codes) {
		return new ActionWindow(codes);
	}

	private void initGUI() {
		dialog = new JFrame();
		dialog.setName("Action_Window");
		dialog.setSize(new Dimension(575, 350));
		dialog.setFont(new Font("Dialog", 1, 20));
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setBounds(SystemUtil.getScreenSize().width / 2 - 300, SystemUtil.getScreenSize().height / 2 - 300, 575,
				350);
		dialog.setUndecorated(true);
		dialog.setLocation(SystemUtil.getScreenSize().width / 2 - 300, SystemUtil.getScreenSize().height / 2 - 300);
		dialog.getContentPane().setLayout(new BorderLayout());
		List<Image> icons = new ArrayList<Image>();
		try {
			icons.add(ImageIO.read(this.getClass().getResource(taskbarIcon)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		dialog.addMouseListener(this);
		dialog.addMouseMotionListener(this);
		JPanel contentPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(final Graphics g) {
				super.paintComponent(g);
				final Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				final int w = this.getWidth();
				final int h = this.getHeight();
				final Color color1 = new Color(238, 130, 238);
				final Color color2 = new Color(127, 255, 212);
				final GradientPaint gp = new GradientPaint(0.0f, 0.0f, color1, 0.0f, (float) h, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, w, h);
			}
		};
		contentPanel.setSize(new Dimension(575, 350));
		contentPanel.setBackground(new Color(127, 255, 212));
		contentPanel.setFont(new Font("Tahoma", 0, 18));
		contentPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPanel.add(initTabbedPane(JTabbedPane.LEFT));
		contentPanel.setLayout(null);
		dialog.getContentPane().add(contentPanel, "Center");

	}

	private JTabbedPane initTabbedPane(int position) {
		tabbledPanel = new JTabbedPane(position);
		tabbledPanel.setSize(new Dimension(551, 324));
		tabbledPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		tabbledPanel.setBounds(12, 13, 551, 324);
		tabbledPanel.setBackground(new Color(255, 255, 255));
		tabbledPanel.setOpaque(true);
		tabbledPanel.setAutoscrolls(true);
		tabbledPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbledPanel.setFont(new Font("Tahoma", Font.BOLD, 16));
		tabbledPanel.setPreferredSize(new Dimension(550, 260));
		tabbledPanel.addMouseListener(this);
		tabbledPanel.addMouseMotionListener(this);
		return tabbledPanel;
	}
	
	
	
//	public static String tabName;
//
//	@Override
//	public void stateChanged(ChangeEvent arg0) {
//		tabName=TabbledPanel.getTitleAt(TabbledPanel.getSelectedIndex()).toString();	
//		if(ActionGUI.dialog.isVisible())
//		{
//			tabLoaded=false;
//			tm=new ToastMsg("Loading...")
//			{
//				private static final long serialVersionUID = 1L;
//				public void terminationLogic() throws InterruptedException
//				{
//					do{
//					Thread.sleep(100);
//					}while(!ActionGUI.tabLoaded);
//				}
//			};
//			tm.setLocation(dialog.getBounds().x+430/2+75,dialog.getBounds().y+315/2);
//			tm.showToast();
//		}
//		if(tabName.contains("Save"))	
//		{
//			if(!savePanel.saveLoaded)
//			{
//				try {
//					savePanel.loadSaveTab();
//					savePanel.SaveScrollPane.add(savePanel.panel_Save_Buttons);
//					savePanel.SaveScrollPane.add(savePanel.SavePanel);
//					savePanel.btnDone.requestFocusInWindow();
//					if(i!=0 && loadSaveTab)
//						savePanel.exitbtn.setEnabled(false);
//					super.e=null;
//				} catch (Exception e) {
//					//tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					super.e=e;
//				}
//			}
//
//			if(property.getBoolean("showFolderNameField",false))
//			{
//				savePanel.lblParFol.setVisible(true);
//				savePanel.textField_ParFol.setVisible(true);
//				savePanel.textField_Filename.setColumns(16);
//			}
//			else
//			{
//				savePanel.lblParFol.setVisible(false);
//				savePanel.textField_ParFol.setVisible(false);
//				savePanel.textField_Filename.setColumns(22);
//			}
//			if(savePanel.textField_Filename.getText().replaceAll("\\s", "").equals(""))
//			{
//				savePanel.btnDone.setVisible(false);
//			}
//			savePanel.lblDoYouWant.setFont(new Font("Tahoma", Font.PLAIN, 14));
//			savePanel.lblDoYouWant.setToolTipText("Do you want to continue with current screenshots ?");
//			savePanel.textField_Filename.requestFocusInWindow();
//			if(savePanel.lblChooseFile.isVisible())
//			{
//				savePanel.lblParFol.setVisible(false);
//				savePanel.textField_ParFol.setVisible(false);
//			}
//			dialog.getRootPane().setDefaultButton(savePanel.btnDone);
//			tabLoaded=true;
//		}
//		else if(tabName.contains("View"))
//		{
//			if(!viewPanel.viewLoaded)
//			{
//				try {
//					viewPanel.loadViewTab();
//					viewPanel.ViewScrollPane.add(viewPanel.panel_Image);
//					viewPanel.ViewScrollPane.add(viewPanel.panel_Button);
//					super.e=null;
//				} catch (Exception e) {
//					tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					super.e=e;
//				}
//				
//			}
//			ViewPanel.files=new File(property.getString("TempPath")).listFiles();
//			sortFiles(ViewPanel.files);
//			if(ViewPanel.ImageLabel.getToolTipText()==null)
//				ViewPanel.imgId=ViewPanel.files.length-1;
//
//			try {
//				if(ViewPanel.files.length>0)
//				{
//					ViewPanel.ImageLabel.setIcon(new ImageIcon(ImageIO.read(ViewPanel.files[ViewPanel.imgId]).getScaledInstance(410,250, java.awt.Image.SCALE_SMOOTH)));
//					ViewPanel.ImageLabel.setToolTipText("<html>Filename : "+ViewPanel.files[ViewPanel.imgId].getName()+"<br><br>Click image to zoom</html>");
//					if(comments.get(ViewPanel.files[ViewPanel.imgId].getName())!=null)
//						ViewPanel.ImageLabel.setToolTipText("<html>Filename : "+ViewPanel.files[ViewPanel.imgId].getName()+"<br>Comment:"+comments.get(ViewPanel.files[ViewPanel.imgId].getName())+"<br><br>Click image to zoom</html>");
//
//				}
//			} catch (IOException e) {
//				logError(e,"Exception occured while loading imageviewer");
//			}
//			tabLoaded=true;
//		}
//		else if(tabName.contains("Manage"))
//		{
//			System.out.println(documentPanel.isloaded);
//			if(!documentPanel.isloaded)
//			{
//				//System.out.println(documentPanel.isloaded);
//
//				try{
//					documentPanel.loadDocumentsTab(property.getString("DocPath",""));
//					documentPanel.DocumentScrollPane.add(documentPanel.panel_Selection);
//					documentPanel.DocumentScrollPane.add(documentPanel.panel_View);
//					documentPanel.showRecent();
//					documentPanel.label_SearchBtn.requestFocusInWindow();
//					super.e=null;
//				}catch(Exception e){
//					super.e=e;
//					tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					logError(e,"Exception occured while loading Manage documents tab");
//				}
//			}
//			else if(SettingsPanel.DocPath_Previous!=null && !SettingsPanel.DocPath_Previous.equalsIgnoreCase(property.getString("DocPath")))
//			{
//				try {
//					documentPanel.DocumentScrollPane.remove(documentPanel.panel_View);
//					documentPanel.loadDocumentsTab(property.getString("DocPath",""));
//					documentPanel.DocumentScrollPane.add(documentPanel.panel_View);
//					documentPanel.showRootFile();
//					documentPanel.label_SearchBtn.requestFocusInWindow();
//					super.e=null;
//				} catch (Exception e) {
//					tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					super.e=e;
//				}
//				
//			}
//			tabLoaded=true;
//		}
//		else if(tabName.contains("Record"))
//		{
//			if(!screenRecord.isLoaded)
//			{
//				try{
//					screenRecord.loadRecordPanel();
//					screenRecord.RecordPanel.add(screenRecord.panel_Control);
//					super.e=null;
//				}catch(Exception e){
//					tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					super.e=e;
//					logError(e,"Exception occured while loading Screen Recording tab");
//				}
//			}
//			if(property.getBoolean("showFolderNameField",false))
//			{
//				screenRecord.lblFolderName.setVisible(true);
//				screenRecord.textField_Foldername.setVisible(true);
//				screenRecord.textField_Filename.setColumns(16);
//			}
//			else
//			{
//				screenRecord.lblFolderName.setVisible(false);
//				screenRecord.textField_Foldername.setVisible(false);
//				screenRecord.textField_Filename.setColumns(22);
//			}
//			screenRecord.textField_Filename.requestFocusInWindow();
//			tabLoaded=true;
//		}
//		else if(tabName.contains("Settings"))
//		{	
//			selectedTab="Settings";
//			ActionGUI.tagDrop=false;
//			if(!settingsPanel.loadSettingsTab)
//			{
//				try {
//					settingsPanel.loadSettingsTab();
//					settingsPanel.SettingsScrollPane.add(settingsPanel.SettingsPane);
//					settingsPanel.loadSettingsTab=true;
//					super.e=null;
//				} catch (Exception e) {
//					tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//					tm.terminateAfter(2000);
//					super.e=e;
//				}
//			}
//			settingsPanel.textField_DocDestFolder.setText(property.getString("DocPath",""));
//			settingsPanel.chckbxShowFilderNameField.setSelected(property.getBoolean("showFolderNameField",false));
//			if(property.getBoolean("showFolderNameField",false))
//			{
//				settingsPanel.chckbxSetFoldernameMandatory.setVisible(true);
//				settingsPanel.chckbxSetFoldernameMandatory.setSelected(property.getBoolean("setFolderNameMandatory",false));
//			}
//			if(!settingsPanel.chckbxShowFilderNameField.isSelected())
//			{
//				settingsPanel.chckbxSetFoldernameMandatory.setVisible(false);
//				settingsPanel.chckbxSetFoldernameMandatory.setSelected(false);
//			}
//			settingsPanel.chckbxAutoUpdate.setSelected(property.getBoolean("autoupdate",false)); 
//			SettingsPanel.lblLocationx.setText("Location : ( "+property.getInteger("Xlocation",screensize.width-160)+" , "+property.getInteger("Ylocation",screensize.height/2+100)+" )");
//			//settingsPanel.SettingsPane_Recordpanel_RecordFlag.setSelected(Boolean.valueOf(property.getString("DuplicateFlag")));
//			settingsPanel.comboBox_ImageFormat.setSelectedItem(property.getString("ImageFormat","png"));
//			ActionGUI.tagDrop=false;
//			settingsPanel.comboBox_CaptureKey.setSelectedItem(property.getString("CaptureKey","PrtSc"));
//			if(!TabbledPanel.getTitleAt(0).contains("Settings"))
//			{
//				settingsPanel.btnUpdateFrameLocation.setEnabled(false);
//			}
//			dialog.getRootPane().setDefaultButton(settingsPanel.SaveBtn);
//			tabLoaded=true;
//		}
//		else if(tabName.contains("Update"))
//		{
//			SoftwareUpdate su= new SoftwareUpdate();
//			try {
//				if(su.checkForUpdates())
//				{
//					try {
//						updatePanel.loadNeedUpdate();
//						updatePanel.panel_Update.add(updatePanel.panel_UpdateYes);
//						updatePanel.lblName.setText(" Name : "+su.JSONObj.get("name"));
//						updatePanel.lblVersion.setText(" Version  : "+su.JSONObj.get("tag_name"));
//						updatePanel.lblPublish.setText(" Published at : "+su.JSONObj.get("published_at"));
//						updatePanel.lblPublish.setToolTipText(su.JSONObj.get("published_at").toString());
//						UpdatePanel.lblprogressflag.setVisible(false);
//						super.e=null;
//					} catch (Exception e) {
//						tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//						tm.terminateAfter(2000);
//						super.e=e;
//					}
//					
//				}
//				else
//				{
//					try{
//						updatePanel.loadUpdated();
//						updatePanel.lblLastUpdatedOn.setText(" Last updated on : "+versionInfo.getString("LasUpdateDate","Data not available"));
//						updatePanel.lblTime.setText(" Time : "+versionInfo.getString("LasUpdateTime","Data not available"));
//						updatePanel.lblCurrentVersion.setText(" Current version : "+versionInfo.getString("CurrentVersion","Base"));
//						super.e=null;
//					}
//					catch(Exception e){
//						tm.setText(e.getClass().getSimpleName()+" Occured. Please try again. Visit Log for more details.");
//						tm.terminateAfter(2000);
//						super.e=e;}
//				}
//
//			} catch (JSONException e) {
//				logError(e,"Exception occured while checking for updates");
//			}
//
//			tabLoaded=true;
//		}
//	}

	/**
	 * @return
	 * @purpose
	 * @date 08-Jun-2022
	 */
	public JFrame show() {
		dialog.setVisible(true);
		tabbledPanel.setSelectedIndex(0);
		return dialog;

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		xDialog = e.getXOnScreen();
		yDialog = e.getYOnScreen();
		dialog.setLocation(xDialog - xxDialog, yDialog - xyDialog);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		xxDialog = e.getX();
		xyDialog = e.getY();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		System.out.println(e);
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {}

}
