package captureEasy.Resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.Components.UpdatePanel;

public class SoftwareUpdate extends Library {
	public JSONObject JSONObj;
	private double currentProgress;

	public SoftwareUpdate() {
		try {
			if (isReachableByPing()) {
				this.JSONObj = new JSONObject(GET(versionInfo.getString( "URL",gitHubBaseURL)));
			} else {
				this.JSONObj = null;
				new PopUp("ERROR","error","Internet not available","Okay","").setVisible(true);
			} 
		} catch (JSONException|IOException e) {
			logError(e,"Exception Occured on connecting web server...")	;	
			} 
	}

	private static String GET(String url) throws IOException {
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
			UpdatePanel.lblprogressflag.setText("Downloaded "+Math.round(currentProgress)+"%");
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
	public static void startDownload() {
		new Thread(() -> {		
			try {
				
				SoftwareUpdate u=new SoftwareUpdate();
				String downloadedFilePath = u.downloadUpdate(downloadFolderPath);
				UpdatePanel.lblprogressflag.setText("Download Completed");
				PopUp p=new PopUp("PERMISSION","info","Download Completed. Do you want to restart application now? ","Yes","No");
				p.setVisible(true);
				p.btnNewButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							Runtime.getRuntime().exec("java -jar "+restartJarPath+" "+downloadedFilePath+" "+sourceJarPath+" "+property.getString("ApplicationPath"));
							SensorGUI.closeApplication(); 						
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
				});
				
				
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}
			
			}).start();
		
	}
}
