package captureEasy.Launch;
import captureEasy.Resources.DetectKeypress;
import captureEasy.Resources.Library;
import captureEasy.Resources.SharedResources;
import captureEasy.Resources.SoftwareUpdate;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.SplashScreen;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jnativehook.GlobalScreen;

public class Application extends Library{
	static boolean TempNeeded=true;
	public static SensorGUI sensor;

	public static void main(String args[]) throws Exception
	{
		new Application().launch();
	}
	public void launch() 
	{	
		SplashScreen sp=new SplashScreen();
		sp.frame.setVisible(true);
		SharedResources.init();
		sp.lblVersion.setText("Version : "+versionInfo.getString("CurrentVersion","Base"));
		try{Thread.sleep(3000);}catch(Exception e){}
		if(!new File(createFolder(PropertyFilePath)).exists() || property.getString("DocPath","").replaceAll("\\s", "").equals(""))
		{
			property.setProperty("TempPath",createTemp());
			File tempFile=new File(tempFilePath);
			if(tempFile.exists())
			{
				Properties tempProp = new Properties();
				try {
					tempProp.load(new FileReader(tempFile));
					if(tempProp.getProperty("TempPath")!=null)
						property.setProperty("TempPath", tempProp.getProperty("TempPath"));
				} catch (IOException e) {}
			}

			SplashScreen.displaySplash=false;
			List<String> tabs=new ArrayList<String>();
			tabs.add("Settings");
			new ActionGUI(tabs);
			ActionGUI.dialog.setVisible(true);
			ActionGUI.settingsPanel.DocumentDestination.setText("Set document destination folder");
			ActionGUI.settingsPanel.btnUpdateFrameLocation.setText("Set frame location");
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
		new File(tempFilePath).delete();
		new File(tempFilePath).deleteOnExit();

		//new File(tempFilePath)
		if (new File(createFolder(PropertyFilePath)).exists()/* && IsEmpty(createFolder(TempFolder))*/)
		{
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
			new SoftwareUpdate().autoUpdate();
		}
	}
}