package app.captureEasy.Resources;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Resources.Library;
import app.captureEasy.Resources.PathsNKeys;
import app.captureEasy.UI.SensorGUI;
import app.captureEasy.UI.Components.PopUp;
import app.captureEasy.UI.Components.ToastMsg;


public class SharedResources extends PathsNKeys {

	public static Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
	public static Logger logger = Logger.getLogger(SharedResources.class);
	public static boolean PauseThread=false,stopThread=false,duplicateAvailable=false;
	public static int progress=0;
	public static Map<String,String> comments=new HashMap<>();
	public static PropertiesConfiguration property ;
	public static PropertiesConfiguration versionInfo ;
	public static PropertiesConfiguration log4j ;
	public Exception e=null;
	public static SensorGUI senGUI;
	public ToastMsg tm;
	public static Robot robot;


	@NoLogging
	public static void init()  
	{
		try {
			Class<?> cls=Class.forName("app.captureEasy.Resources.PathsNKeys");
			Object obj=cls.newInstance();
			Field[] fields=	cls.getDeclaredFields();
			for(Field f:fields)
			{
				Library.createFolder(f.get(obj).toString(),"");
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e1) {}
		try {
			new File(PropertyFilePath).createNewFile();
			property = new PropertiesConfiguration(PropertyFilePath);
			property.setAutoSave(true);
		} catch (ConfigurationException | IOException e) {
			Library.logError(e,"Unable to initialize propery configuration");
			PopUp p=new PopUp("ERROR","error","FAILED!\nUnable to initialize propery configuration."+e.getClass().getSimpleName()+" Occured.","Okay","");
			p.setVisible(true);
			PopUp.PopDia=p;
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
						versionInfo.setProperty("LasUpdateTime",tempProp.getProperty("LasUpdateTime"));
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
			PopUp p=new PopUp("ERROR","error","FAILED!\nUnable to initialize versioninfo configuration."+e.getClass().getSimpleName()+" Occured.","Okay","");
			p.setVisible(true);
			PopUp.PopDia=p;
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
				log4j.setProperty("log4j.appender.FileAppender.layout.ConversionPattern", "[%-5p][%d{dd MMM yyyy HH:mm:ss}] %m%n");
			}
		} catch (ConfigurationException | IOException e) {
			Library.logError(e,"Unable to initialize propery configuration");
			PopUp p=new PopUp("ERROR","error","FAILED!\nUnable to initialize log4j propery ."+e.getClass().getSimpleName()+" Occured.","Okay","");
			p.setVisible(true);
			PopUp.PopDia=p;
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
		try {
			robot=new Robot();
		} catch (AWTException e) {
			Library.logError(e,"Exception Occured while creating Robot object");
			PopUp p=new PopUp("ERROR","error","FAILED!\nUnable to create Robot object ."+e.getClass().getSimpleName()+" Occured.","Okay","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}
	}
}
