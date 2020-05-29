package app.captureEasy.Launch;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jnativehook.GlobalScreen;

import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Resources.Library;
import app.captureEasy.Resources.SharedResources;
import app.captureEasy.SoftwareUpdate.SoftwareUpdate;
import app.captureEasy.UI.ActionGUI;
import app.captureEasy.UI.SensorGUI;
import app.captureEasy.UI.Components.PopUp;
import app.captureEasy.UI.Components.SplashScreen;
import app.captureEasy.Utilities.DetectKeypress;

public class Application extends Library{
	static boolean TempNeeded=true;
	public static boolean isFirstTime=false;
	@NoLogging
	public static void main(String args[]) throws Exception
	{
		new Application();
	}
	@NoLogging
	public Application()
	{
		SharedResources.init();
		launch();
	}
	public void launch() 
	{	
		SplashScreen sp=new SplashScreen();
		sp.frame.setVisible(true);
		sp.lblVersion.setText("Version : "+versionInfo.getString("CurrentVersion","Base"));
		if(!new File(PropertyFilePath).exists() || property.getString("DocPath")==null)
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
			isFirstTime=true;
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
			isFirstTime=false;
		}
		else if (new File(PropertyFilePath).exists() &&  !IsEmpty(property.getString("TempPath","")))
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
			
		}
		new File(tempFilePath).delete();
		new File(tempFilePath).deleteOnExit();

		if (new File(PropertyFilePath).exists() && property.getString("DocPath")!=null)
		{
			GlobalScreen.addNativeKeyListener(new DetectKeypress());
			try {
				GlobalScreen.registerNativeHook();
			} catch (Exception e) {
				PopUp p=new PopUp("ERROR","Error","Exception occured while registering the native hook. As a result system will not able to take screenshots using keys.","Continue","Exit Application");
				PopUp.PopDia=p;
				PopUp.PopDia.setVisible(true);
				logError(e,"Exception occured while registering the native hook. As a result system will not able to take screenshots using keys.");
			}
			senGUI=new SensorGUI();
			senGUI.frame.setVisible(true);
			senGUI.label_Menu.setEnabled(true);
			senGUI.sensor_panel.setEnabled(true);
			senGUI.frame.getRootPane().setDefaultButton(senGUI.label_Menu);
			
			updateUITask();
			clearFilesTask(60000);
			new SoftwareUpdate().autoUpdateTask();
		}
	}
}