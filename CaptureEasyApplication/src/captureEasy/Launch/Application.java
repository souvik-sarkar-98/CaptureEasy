package captureEasy.Launch;
import captureEasy.Resources.DetectKeypress;
import captureEasy.Resources.Library;
import captureEasy.Resources.SharedResources;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.jnativehook.GlobalScreen;

public class Application extends Library{
	static boolean TempNeeded=true;
	public static SensorGUI sensor;

	public static void main(String args[]) throws Exception
	{
		SharedResources.init();
		new Application().launch();
	}
	public void launch() 
	{	
		if(!new File(createFolder(PropertyFilePath)).exists() || property.getString("DocPath","").replaceAll("\\s", "").equals(""))
		{
			property.setProperty("TempPath",createTemp());
			List<String> tabs=new ArrayList<String>();
			tabs.add("Settings");
			new ActionGUI(tabs);
			ActionGUI.dialog.setVisible(true);
			ActionGUI.settingsPanel.DocumentDestination.setText("Set document destination folder");
			ActionGUI.settingsPanel.btnUpdateFrameLocation.setText("Set frame location");
			ActionGUI.settingsPanel.SettingsPane_DocFolderPanel_textField_DocDestFolder.setEnabled(true);
			ActionGUI.settingsPanel.SettingsPane_DocFolderPanel_Chooser.setEnabled(true);
			ActionGUI.tagDrop=false;
			ActionGUI.settingsPanel.comboBox_CaptureKey.setSelectedIndex(0);
			ActionGUI.settingsPanel.comboBox_ImageFormat.setSelectedIndex(0);
			do{try {Thread.sleep(100);} catch (InterruptedException e) {}}while(!ActionGUI.leaveControl);	
		}
		else if (new File(createFolder(PropertyFilePath)).exists() &&  !IsEmpty(createFolder(property.getString("TempPath",""))))
		{
			Library.c=lastFileName(property.getString("TempPath",""));
			try{
				if(property.getString("TempCode","").equals(new File(property.getString("TempPath").toString()).getName()))
				{
					String[] datas=property.getString("Comments","").split("_");
					for(int i=0;i<datas.length;i++)
					{
						if(datas[i]!=null && !datas[i].equals("") && datas[i].split("->").length==2)
							SharedResources.comments.put(datas[i].split("->")[0], datas[i].split("->")[1]);
					}
				}
			}catch(Exception w){
				logError(w,"exception occured while setting previous comments");
			}
			/*List<String> tabs=new ArrayList<String>();
			tabs.add("Action");
			tabs.add("View");

			new ActionGUI(tabs);			
			ActionGUI.dialog.setVisible(true);
			act.viewPanel.lblExit.setEnabled(false);
			act.actionPanel.rdbtnSavePreviousWork.setEnabled(false);
			do{try {Thread.sleep(100);} catch (InterruptedException e) {}}while(!ActionGUI.leaveControl);	
			TempNeeded=false;
			 */
		}



		if (new File(createFolder(PropertyFilePath)).exists()/* && IsEmpty(createFolder(TempFolder))*/)
		{
			/*if(TempNeeded)
			{
				updateProperty(TempFilePath,"TempPath",createFolder(System.getProperty("user.dir")+"/Resources/bin/Temp/"+new Random().nextInt(1000000000)));
			}*/
			GlobalScreen.addNativeKeyListener(new DetectKeypress());
			try {
				GlobalScreen.registerNativeHook();
			} catch (Exception e) {
				PopUp p=new PopUp("ERROR","Error","Exception occured while registering the native hook. As a result system will not able to take screenshots using keys.","Ok, I understoood","Exit Application");
				p.setVisible(true);
				logError(e,"Exception occured while registering the native hook. As a result system will not able to take screenshots using keys.");
			}
			sensor=new SensorGUI();
			SensorGUI.frame.setVisible(true);
			sensor.label_Menu.setEnabled(true);
			sensor.sensor_panel.setEnabled(true);
			updateUI();
			clearTempImages(20000);
		}
	}
}