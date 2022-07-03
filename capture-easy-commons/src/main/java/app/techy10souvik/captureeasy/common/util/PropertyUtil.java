package app.techy10souvik.captureeasy.common.util;

import java.awt.Point;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * @author Souvik Sarkar
 * @createdOn 02-Jul-2022
 * @purpose 
 */
public class PropertyUtil {
	private static PropertiesConfiguration property;
	
	public static void init() throws ConfigurationException, IOException {
		property = new PropertiesConfiguration(SystemUtil.getPropFile());
	}
	
	public static String getDocumentPath() {
		return property.getString("DocPath", SystemUtil.getDocumentFolder());
	}
	
	public static String getAppVersion() {
		return property.getString("AppVersion","-");
	}
	
	public static Point getGUILocation() {
		int dXLoc=SystemUtil.getScreenSize().width - 160;
		int dYLoc=SystemUtil.getScreenSize().height / 2 + 100;
		return new Point(property.getInteger("Xlocation", dXLoc),property.getInteger("Ylocation", dYLoc));
	}
	
}
