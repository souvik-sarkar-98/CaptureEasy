package captureEasy.Resources;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import captureEasy.UI.PopUp;


public class SharedResources extends PathsNKeys {

	
	public static Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Logger logger = Logger.getLogger(PathsNKeys.class);
	public static boolean PauseThread=false;
	public static boolean stopThread=false;
	public static int progress=0;
	public static Map<String,String> comments=new HashMap<>();
	public static PropertiesConfiguration property ;
	public static PropertiesConfiguration versionInfo ;
	public static PropertiesConfiguration log4j ;

	public static void init() 
	{
		try {
			new File(PropertyFilePath).createNewFile();
			property = new PropertiesConfiguration(PropertyFilePath);
			property.setAutoSave(true);
		} catch (ConfigurationException | IOException e) {
			Library.logError(e,"Unable to initialize propery configuration");
			new PopUp("ERROR","error","FAILED!\nUnable to initialize propery configuration."+e.getClass().getSimpleName()+" Occured.","Okay","").setVisible(true);
		}
		try {
			new File(versionInfoFilePath).createNewFile();
			versionInfo = new PropertiesConfiguration(versionInfoFilePath);
			versionInfo.setAutoSave(true);
			if(!(new File(versionInfoFilePath).exists()) || versionInfo.getString("URL","").replaceAll("\\s", "").equals(""))
			{
				versionInfo.setProperty("URL", gitHubBaseURL);
			}
			File tempFile=new File(tempFilePath);
			if(tempFile.exists())
			{
				Properties tempProp = new Properties();
				try {
					tempProp.load(new FileReader(tempFile));
					if(tempProp.getProperty("deleted","false").equalsIgnoreCase("true") && tempProp.getProperty("copied","false").equalsIgnoreCase("true"))
					{
						versionInfo.setProperty("CurrentVersion", tempProp.getProperty("CurrentVersion"));
						versionInfo.setProperty("LasUpdateDate", tempProp.getProperty("LasUpdateDate"));
						versionInfo.setProperty("LasUpdateDate",tempProp.getProperty("LasUpdateDate"));
					}
				} catch (IOException e) {
					
				}
				if(tempProp.getProperty("TempPath")!=null)
				{
					property.setProperty("TempPath", tempProp.getProperty("TempPath"));
				}
				
			}
		} catch (ConfigurationException | IOException e) {
			Library.logError(e,"Unable to initialize propery configuration");
			new PopUp("ERROR","error","FAILED!\nUnable to initialize versioninfo configuration."+e.getClass().getSimpleName()+" Occured.","Okay","").setVisible(true);
		}
		try {
			
			new File(Log4jPropertyFilePath).createNewFile();
			log4j = new PropertiesConfiguration(Log4jPropertyFilePath);
			log4j.setAutoSave(true);
			if(!(new File(Log4jPropertyFilePath).exists()) || log4j.isEmpty() || log4j.getString("log4j.rootLogger").replaceAll("\\s", "").equals(""))
			{
				log4j.setProperty("log4j.rootLogger", "INFO, FileAppender");
				log4j.setProperty("log4j.appender.FileAppender", "org.apache.log4j.FileAppender");
				log4j.setProperty("log4j.appender.FileAppender.File", "${logfilename}");
				log4j.setProperty("log4j.appender.FileAppender.layout", "org.apache.log4j.PatternLayout");
				log4j.setProperty("log4j.appender.FileAppender.append", "true");
				log4j.setProperty("log4j.appender.FileAppender.layout.ConversionPattern", "[%-5p] [%d{dd MMM yyyy HH:mm:ss}]  %nUser Message: '%m'%n %n");
			}
		} catch (ConfigurationException | IOException e) {
			Library.logError(e,"Unable to initialize propery configuration");
			new PopUp("ERROR","error","FAILED!\nUnable to initialize log4j propery ."+e.getClass().getSimpleName()+" Occured.","Okay","").setVisible(true);
		}
		if(versionInfo.getString("ApplicationPath")==null)
		{
			File[] App= new File(System.getProperty("user.dir")).listFiles();
			for(File f :App)
			{
				if(f.getName().toLowerCase().contains(".exe") && (f.getName().toLowerCase().contains("capture") || f.getName().toLowerCase().contains("app")))
				{
					versionInfo.setProperty("ApplicationPath",f.getAbsolutePath());
				}
			}
			versionInfo.setProperty("UpdateFrequency",3600000);
		}
	}
}
