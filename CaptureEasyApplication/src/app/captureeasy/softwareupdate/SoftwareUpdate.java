package app.captureeasy.softwareupdate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.captureeasy.annotations.Update;
import app.captureeasy.resources.Library;
import app.captureeasy.ui.ActionGUI;
import app.captureeasy.ui.UpdatePanel;
import app.captureeasy.ui.components.PopUp;

public class SoftwareUpdate extends Library {
	public JSONObject JSONObj;
	private double currentProgress;
	public static  boolean doUpdate=true,isInstalled=true;
	@Update
	public SoftwareUpdate()
	{
		if(isReachableByPing())
		{
			try {
				this.JSONObj = new JSONObject(GET(versionInfo.getString( "URL",gitHubBaseURL))); 
			} catch (Exception e) {
				logError(e,"Exception Occured on connecting web server...")	;	
			}
		}

	}

	@Update
	public static String GET(String url) throws Exception {
		InputStream is = (new URL(url)).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		try {
			int cp;
			while ((cp = rd.read()) != -1)
				sb.append((char)cp); 
		} finally {
			is.close();
		} 
		return sb.toString();
	}

	@Update
	public boolean checkForUpdates() throws JSONException {
		if(isReachableByPing())
		{
			try {
				if (this.JSONObj.getString("tag_name").equalsIgnoreCase(versionInfo.getString("CurrentVersion","Data not available")))
					return false; 
				if (getDownloadURL() == null)
					return false; 
				return true;
			} catch (Exception e) {
				logError(e,"Exception Occured on connecting web server...")	;	
			}
		}
		return false;
	}
	@Update
	public static boolean isReachableByPing() {
		try {
			String host = "www.google.com";
			Process process = Runtime.getRuntime().exec("ping -" + (System.getProperty("os.name").toLowerCase().startsWith("windows") ? "n" : "c") + " 1 " + host);
			process.waitFor();
			return (process.exitValue() == 0);
		} catch (Exception ex) {
			return false;
		} 
	}
	@Update
	public String getDownloadURL() throws JSONException {
		JSONArray assets = (JSONArray)this.JSONObj.get("assets");
		for (int i = 0; i < assets.length(); i++) {
			String URL = ((JSONObject)assets.get(i)).get("browser_download_url").toString();
			if (URL.contains(String.valueOf(this.JSONObj.getString("tag_name")) + ".jar"))
				return URL.toString(); 
		} 
		return null;
	}
	@Update
	public String getRemoteFileName() throws JSONException {
		JSONArray assets = (JSONArray)this.JSONObj.get("assets");
		for (int i = 0; i < assets.length(); i++) {
			String name = ((JSONObject)assets.get(i)).get("name").toString();
			if (name.contains(String.valueOf(this.JSONObj.getString("tag_name")) + ".jar"))
				return name.toString(); 
		} 
		return null;
	}
	@Update
	public String downloadUpdate(String downloadPath) throws JSONException, IOException {
		URL url = new URL(getDownloadURL());
		double downloadedFileSize =0;
		String path=downloadPath+"\\" + getRemoteFileName();
		try{
			FileOutputStream fis = new FileOutputStream(path);
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			double completeFileSize = httpConnection.getContentLength();
			BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream());
			byte[] buffer = new byte[1024];
			int count = 0;

			while ((count = bis.read(buffer, 0, 1024)) != -1) {
				downloadedFileSize += count;
				currentProgress = downloadedFileSize / completeFileSize * 100;
				fis.write(buffer, 0, count);
				try{UpdatePanel.lblprogressflag.setText("Downloaded "+Math.round(currentProgress)+"%");}catch(Exception e){}
			} 
			fis.close();
			bis.close();
		}catch(Exception e){

		}
		if(new File(downloadPath+"\\" + getRemoteFileName()).exists())
		{
			return downloadPath+"\\" + getRemoteFileName();
		}
		else
		{
			return null;
		}
	}
	static SoftwareUpdate u;
	static String downloadedFilePath;
	@Update
	public static void startDownload() {
		new Thread(() -> {		
			try {

				u=new SoftwareUpdate();
				downloadedFilePath = u.downloadUpdate(downloadFolderPath);
				if(downloadedFilePath!=null)
				{
					try{UpdatePanel.lblprogressflag.setText("Download Completed");}catch(Exception e){}
					PopUp p=new PopUp("PERMISSION","info","Download Completed. Do you want to restart application now? It will take near about 20 seconds. ","Yes","No");
					p.setVisible(true);
					PopUp.PopDia=p;
					PopUp.btnNewButton.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							isInstalled=true;
							doRestart(downloadedFilePath);	
						}
					});
					p.btnNo.addActionListener(new ActionListener(){
						@Override
						public void actionPerformed(ActionEvent arg0) {
							isInstalled=false;
						}
					});

				}
				else
				{
					PopUp p=new PopUp("DOWNLOAD FAILED","error","Download failed !! Please try Again.","Okay","");
					p.setVisible(true);
					PopUp.PopDia=p;
				}
			} catch (IOException | JSONException e) {
				logError(e,e.getClass().getName()+" Occured. Update Failed. ");
				PopUp p=new PopUp("UPDATE FAILED","error",e.getClass().getName()+"Occured. Update failed !! Please try Again.","Okay","");
				p.setVisible(true);
				PopUp.PopDia=p;
			}

		}).start();

	}
	@Update
	public static void doRestart(String downloadedFilePath)
	{
		try {

			if(!new File(InstallUpdateScript).exists())
			{
				FileWriter fw;
				try{    
					fw=new FileWriter(InstallUpdateScript);    
					fw.write(

							"Set filesys=CreateObject("+Character.toString('"')+"Scripting.FileSystemObject"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"Set shell=CreateObject("+Character.toString('"')+"Wscript.shell"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"		WScript.Sleep(2000)"+System.getProperty("line.separator")
							+"Set fopen=filesys.OpenTextFile (WScript.Arguments.Item (2),8,True)"+System.getProperty("line.separator")
							+"If Not filesys.FileExists (WScript.Arguments.Item (0)) Then "+System.getProperty("line.separator")
							//         +"		WScript.Echo  "+Character.toString('"')+"Update Failed "+Character.toString('"')+"&vbCrLf&"+Character.toString('"')+"Source file : "+Character.toString('"')+"&WScript.Arguments.Item (0)&"+Character.toString('"')+" Not Found."+Character.toString('"')+"&vbCrLf&"+Character.toString('"')+"Please try again. Time: "+Character.toString('"')+"& Now()"+System.getProperty("line.separator")
							+"		MsgBox "+Character.toString('"')+"Update Failed "+Character.toString('"')+"&vbCrLf&"+Character.toString('"')+"Source file : "+Character.toString('"')+"&WScript.Arguments.Item (0)&"+Character.toString('"')+" Not Found"+Character.toString('"')+",ok,"+Character.toString('"')+"File Not Found"+Character.toString('"')+System.getProperty("line.separator")
							+"Else"+System.getProperty("line.separator")
							+"		On Error Resume Next"+System.getProperty("line.separator")
							+"		WScript.Sleep(2000)"+System.getProperty("line.separator")
							+"		shell.Exec("+Character.toString('"')+"Taskkill /f /im javaw.exe"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"		shell.Exec("+Character.toString('"')+"Taskkill /f /im javaw.exe"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"		filesys.DeleteFile WScript.Arguments.Item (1)"+System.getProperty("line.separator")
							+"		If Err.Description="+Character.toString('"')+"Permission denied"+Character.toString('"')+" Then"+System.getProperty("line.separator")
							+"			shell.Exec("+Character.toString('"')+"Taskkill /f /im javaw.exe"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"			filesys.DeleteFile WScript.Arguments.Item (1)"+System.getProperty("line.separator")
							+"		End If"+System.getProperty("line.separator")
							+"		If filesys.FileExists (WScript.Arguments.Item (1)) Then"+System.getProperty("line.separator")
							+"			fopen.WriteLine ("+Character.toString('"')+"deleted=false"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"		Else"+System.getProperty("line.separator")
							+"			fopen.WriteLine ("+Character.toString('"')+"deleted=true"+Character.toString('"')+")"+System.getProperty("line.separator")
							+"		End If"+System.getProperty("line.separator")
							+"		WScript.Sleep(1000)"+System.getProperty("line.separator")
							+"		filesys.CopyFile WScript.Arguments.Item (0), WScript.Arguments.Item (1)"+System.getProperty("line.separator")
							+"		If filesys.FileExists (WScript.Arguments.Item (1)) Then"+System.getProperty("line.separator")
							+"			fopen.WriteLine ("+Character.toString('"')+"copied=true"+Character.toString('"')+")"+System.getProperty("line.separator")
							//    +" 		WScript.Echo "+Character.toString('"')+"Successfully Installed. Please resatrt application now. Time: "+Character.toString('"')+"& Now()"+System.getProperty("line.separator")
							+" 		MsgBox "+Character.toString('"')+"Successfully Installed. Please restart application now"+Character.toString('"')+",ok,"+Character.toString('"')+"Success"+Character.toString('"')+System.getProperty("line.separator")
							+"		Else"+System.getProperty("line.separator")
							+"			fopen.WriteLine ("+Character.toString('"')+"copied=false"+Character.toString('"')+")"+System.getProperty("line.separator")
							//   +" 		WScript.Echo "+Character.toString('"')+"Installation Failed. Unable to copy "+Character.toString('"')+"&WScript.Arguments.Item (0)&"+Character.toString('"')+" to "+Character.toString('"')+"&WScript.Arguments.Item (1)& "+Character.toString('"')+"  Please Try Again. Time: "+Character.toString('"')+"& Now()"+System.getProperty("line.separator")
							+"			MsgBox  "+Character.toString('"')+"Installation Failed. Unable to copy "+Character.toString('"')+"&WScript.Arguments.Item (0)&"+Character.toString('"')+" to "+Character.toString('"')+"&WScript.Arguments.Item (1)& "+Character.toString('"')+"  Please Try Again."+Character.toString('"')+","+Character.toString('"')+"Failed"+Character.toString('"')+System.getProperty("line.separator")
							+"		End If"+System.getProperty("line.separator")
							+"End If ");
					fw.close();
				}catch(Exception e){
					System.out.println(e);
				} 
			}

			Properties tempProp =new Properties();
			if(new File(property.getString("TempPath")).listFiles().length>0)
			{
				tempProp.setProperty("TempPath", property.getString("TempPath"));
			}
			tempProp.setProperty("CurrentVersion",u.JSONObj.getString("tag_name"));
			tempProp.setProperty("LasUpdateDate", LocalDate.now().toString());
			tempProp.setProperty("LasUpdateTime", LocalTime.now().toString());
			File tempFile = new File(tempFilePath);									
			tempProp.store(new FileOutputStream(tempFile,false), "This is temporary file. will be deleted shortly");

			String[] command={"cmd","/c","Wscript.exe "+InstallUpdateScript+"  "+downloadedFilePath+" "+sourceJarPath+" "+tempFilePath};
			Runtime.getRuntime().exec(command);	
			senGUI.closeApplication(false); 	

			//cscript.exe InstallUpdate.vbs > outfile.log C:\Users\USER\Desktop\Script.bat C:\Users\USER\Desktop\AllSouvik.bat C:\Users\USER\Desktop\test.properties

		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} 
	}

	@Update
	public void autoUpdateTask()
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				new SoftwareUpdate();
				while (!stopThread )
				{
					try{
						Thread.sleep(10000);
						if(doUpdate==false && isInstalled==false && property.getBoolean("autoupdate",false))
						{
							do{Thread.sleep(100);}while(!ActionGUI.leaveControl || !PopUp.control);
							PopUp p=new PopUp("INFORMATION","info","Latest Version "+JSONObj.getString("name")+"-"+JSONObj.getString("tag_name")+"is already downloaded. To install please click Yes","Yes","No");
							p.setVisible(true);
							PopUp.btnNewButton.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									doRestart(downloadedFilePath);
								}
							});
						}
						else if( doUpdate&& checkForUpdates() && property.getBoolean("autoupdate",false))
						{
							doUpdate=false;
							do{Thread.sleep(100);}while(!ActionGUI.leaveControl || !PopUp.control);
							PopUp p=new PopUp("PERMISSION","info","Latest version publishhed. Do you want to download it now?\nVersion : "+JSONObj.getString("name")+"-"+JSONObj.getString("tag_name")+"\nImprovements: "+JSONObj.getString("body"),"Yes","No");
							p.setVisible(true);
							//PopUp.control=false;
							PopUp.btnNewButton.addActionListener(new ActionListener(){

								@Override
								public void actionPerformed(ActionEvent arg0) {
									startDownload();
								}
							});
							p.btnNo.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									SoftwareUpdate.doUpdate=true;

								}
							});
						}
						Thread.sleep(versionInfo.getInteger("UpdateFrequency",3600000));
					}catch(Exception w){}
				}
			}
		}).start();	
	}}
