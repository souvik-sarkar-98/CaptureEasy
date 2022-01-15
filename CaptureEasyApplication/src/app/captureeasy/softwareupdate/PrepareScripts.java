package app.captureeasy.softwareupdate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import app.captureeasy.annotations.Update;
import app.captureeasy.resources.Library;
import app.captureeasy.ui.components.PopUp;

public class PrepareScripts extends Library{
	
	
	@Update
	public String updateScheduleScript(String RestartScriptPath)
	{ 
		File f=new File("");
		List<String> list=new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if(strLine.toLowerCase().contains("action.path"))
				{
					strLine="	Action.Path="+Character.toString('"')+RestartScriptPath+Character.toString('"');
				}
				list.add(strLine);
			}
			fstream.close();
			f.delete();
			f.createNewFile();
			FileWriter fw=new FileWriter(f);
			for(String str:list)
			{
				fw.write(str+System.getProperty("line.separator"));
			}
			fw.close();
		} catch (Exception e) {
			new PopUp("ERROR","error","An exception occured while updating scheduleScript. Please visit logfile for more details.","Okay","").setVisible(true);;
			logError(e,"exception occured while updating scheduleScript");
		}
		return new File(f.getAbsolutePath()).getAbsolutePath();
	}
	@Update
	public String updateRestartScript(String downloadpath)
	{ 
		File f=new File("");
		List<String> list=new ArrayList<String>();
		try {
			FileInputStream fstream = new FileInputStream(f);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if(strLine.toLowerCase().contains("srcpath"))
				{
					strLine="srcPath="+Character.toString('"')+downloadpath+Character.toString('"');
				}
				else if(strLine.toLowerCase().contains("destpath"))
				{
					strLine="destpath="+Character.toString('"')+sourceJarPath+Character.toString('"');
				}
				else if(strLine.toLowerCase().contains("apppath"))
				{
					strLine="apppath="+Character.toString('"')+versionInfo.getProperties("ApplicationPath")+Character.toString('"');
				}
				list.add(strLine);
			}
			fstream.close();
			f.delete();
			f.createNewFile();
			FileWriter fw=new FileWriter(f);
			for(String str:list)
			{
				fw.write(str+System.getProperty("line.separator"));
			}
			fw.close();
		} catch (Exception e) {
			new PopUp("ERROR","error","An exception occured while updating restartScript. Please visit logfile for more details.","Okay","").setVisible(true);;
			logError(e,"exception occured while updating restartScript");
		}
		return new File(f.getAbsolutePath()).getAbsolutePath();
	}

}
