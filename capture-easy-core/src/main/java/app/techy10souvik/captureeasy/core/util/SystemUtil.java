package app.techy10souvik.captureeasy.core.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import mslinks.ShellLink;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose
 */
public class SystemUtil extends org.apache.commons.lang.SystemUtils {


	public static String getRootFolder() {
		Path rootFolder = null;
		if (IS_OS_WINDOWS) {
			rootFolder = Paths.get(System.getenv("LOCALAPPDATA"), "CaptureEasy");
		} else if (IS_OS_MAC) {
			rootFolder = Paths.get(System.getProperty("user.home"), "Library", "Application Support", "CaptureEasy");
		} else if (IS_OS_LINUX) {
			rootFolder = Paths.get(System.getProperty("user.home"), ".captureeasy");
		}
		
		if (!rootFolder.toFile().exists()) {
			createFolder(rootFolder.toString());
		}
		return rootFolder.toString();
	}

	public static String getLogFolder() {

		Path logFolder = Paths.get(getRootFolder(), "logs");
		if (!logFolder.toFile().exists()) {
			createFolder(logFolder.toString());
		}
		return logFolder.toString();
	}

	public static String getPropFile() throws IOException {
		File propfile = new File(getRootFolder(), "app.properties");
		if (!propfile.exists()) {
			propfile.createNewFile();
		}
		return propfile.getAbsolutePath();
	}

	public static String createFolder(String path) {
		File theDir = new File(path);
		if (!theDir.exists()) {
			theDir.mkdirs();
		}
		return theDir.getAbsolutePath();
	}

	
	
	public static String getDocumentFolder()  {
		Path docFolder = Paths.get(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath(),"Screenshots");
		return docFolder.toString();
	}

	public static void createDesktopShortcut(String target, String shortcut) throws IOException {
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		String shortcutPath = Paths.get(home.getAbsolutePath(), shortcut + ".lnk").toString();
		// https://github.com/DmitriiShamrikov/mslinks
		ShellLink.createLink(target, shortcutPath);
	}

	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	/**
	 * @purpose 
	 * @date 02-Jul-2022
	 * @return
	 */
	public static int getScreenshotCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
