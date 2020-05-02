package captureEasy.Resources;

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

import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.Components.UpdatePanel;

public class SoftwareUpdate extends Library {
	public JSONObject JSONObj;
	private double currentProgress;
	public static  boolean doUpdate=true,isInstalled=true;

	public SoftwareUpdate() {
		try {
			System.out.println(versionInfo.getString( "URL",gitHubBaseURL));
			if (isReachableByPing()) {
				this.JSONObj = new JSONObject(GET(versionInfo.getString( "URL",gitHubBaseURL)));
			} else {
				this.JSONObj = null;
				new PopUp("ERROR","error","Internet not available","Okay","").setVisible(true);
			} 
		} catch (Exception e) {
			logError(e,"Exception Occured on connecting web server...")	;	
		} 
	}

	private static String GET(String url) throws Exception {
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


	public boolean checkForUpdates() throws JSONException {
		if (this.JSONObj.getString("tag_name").equalsIgnoreCase(versionInfo.getString("CurrentVersion")))
			return false; 
		if (getDownloadURL() == null)
			return false; 
		return true;
	}

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

	public String getDownloadURL() throws JSONException {
		JSONArray assets = (JSONArray)this.JSONObj.get("assets");
		for (int i = 0; i < assets.length(); i++) {
			String URL = ((JSONObject)assets.get(i)).get("browser_download_url").toString();
			if (URL.contains(String.valueOf(this.JSONObj.getString("tag_name")) + ".jar"))
				return URL.toString(); 
		} 
		return null;
	}

	public String getRemoteFileName() throws JSONException {
		JSONArray assets = (JSONArray)this.JSONObj.get("assets");
		for (int i = 0; i < assets.length(); i++) {
			String name = ((JSONObject)assets.get(i)).get("name").toString();
			if (name.contains(String.valueOf(this.JSONObj.getString("tag_name")) + ".jar"))
				return name.toString(); 
		} 
		return null;
	}

	public String downloadUpdate(String downloadPath) throws JSONException, IOException {
		URL url = new URL(getDownloadURL());
		double downloadedFileSize =0;
		FileOutputStream fis = new FileOutputStream(downloadPath+"\\" + getRemoteFileName());
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
					p.btnNewButton.addActionListener(new ActionListener(){
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
					new PopUp("DOWNLOAD FAILED","error","Download failed !! Please try Again.","Okay","").setVisible(true);
			} catch (IOException | JSONException e) {
				logError(e,e.getClass().getName()+" Occured. Update Failed. ");
				new PopUp("UPDATE FAILED","error",e.getClass().getName()+"Occured. Update failed !! Please try Again.","Okay","").setVisible(true);
			}

		}).start();

	}
	public static void doRestart(String downloadedFilePath)
	{
		try {

			if(new File(property.getString("TempPath")).listFiles().length>0)
			{
				Properties tempProp =new Properties();
				tempProp.setProperty("TempPath", property.getString("TempPath"));
				File tempFile = new File(tempFilePath);									
				tempProp.store(new FileOutputStream(tempFile,false), "This is temporary file. will be deleted shortly");
				
			}
			prepareRestartScript(downloadedFilePath);
			 File f=new File(restartScript);
			 
			if(f.exists())
			{
				SensorGUI.closeApplication(false); 	
				versionInfo.setProperty("CurrentVersion",u.JSONObj.getString("tag_name"));
				versionInfo.setProperty("LasUpdateDate", LocalDate.now());
				versionInfo.setProperty("LasUpdateTime", LocalTime.now());
				//String [] command = {"cmd","/c","start",f.getAbsolutePath()};
				Runtime.getRuntime().exec("Wscript.exe "+ScheduleScript+" "+restartScript);	
			}
			else
			{
				new PopUp("ERROR","error","Restart Script nor created","Ok","").setVisible(true);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		} 
	}
	public static void prepareRestartScript(String downloadedfilepath)
	{
		 try{    
			 new File(restartScript).createNewFile();

	           FileWriter fw=new FileWriter(restartScript);    
	           fw.write(
	           "@echo off"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"echo           PLEASE DON'T CLOSE THIS WINDOW. IT WILL TAKE ONLY 5 SECONDS."+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"echo           		   	KILLING ALL JAVA PROCESS "+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"Taskkill /f /im javaw.exe"+System.getProperty("line.separator")
	           +"Taskkill /f /im javaw.exe"+System.getProperty("line.separator")
	           +"timeout 3"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"echo           		   	   DELETING OLD JARS"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"del "+sourceJarPath+System.getProperty("line.separator")
	           +"timeout 2"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"echo           		   	    COPYING NEW JARS"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"copy "+downloadedfilepath+" "+sourceJarPath+System.getProperty("line.separator")
	           +"timeout 1"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"echo           		   	  RESTARTING APPLICATION"+System.getProperty("line.separator")
	           +"echo --------------------------------------------------------------------------------------"+System.getProperty("line.separator")
	           +"START "+versionInfo.getString("ApplicationPath")+System.getProperty("line.separator")
	           +"exit"+System.getProperty("line.separator")

	        		   );
	           fw.close();   
	          }catch(Exception e){
	        	  logError(e,"Exception occured while preparing restart script");	
	        	  }  
	}
	
	public void autoUpdate()
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				while (!stopThread)
				{
					try{
						Thread.sleep(10000);
						if(doUpdate==false && isInstalled==false)
						{
							PopUp p=new PopUp("INFORMATION","info","Latest Version "+JSONObj.getString("name")+"-"+JSONObj.getString("tag_name")+"is already downloaded. To install please click Yes","Yes","No");
							p.setVisible(true);
							p.btnNewButton.addActionListener(new ActionListener(){
								@Override
								public void actionPerformed(ActionEvent arg0) {
									doRestart(downloadedFilePath);
								}
							});
						}
						else if( doUpdate&& checkForUpdates())
						{
							doUpdate=false;
							do{Thread.sleep(100);}while(!ActionGUI.leaveControl);
							PopUp p=new PopUp("PERMISSION","info","A new version of this application is available. Do you want to download it now?\nName : "+JSONObj.getString("name")+"\nVersion : "+JSONObj.getString("tag_name"),"Yes","No");
							p.setVisible(true);
							//PopUp.control=false;
							p.btnNewButton.addActionListener(new ActionListener(){

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
