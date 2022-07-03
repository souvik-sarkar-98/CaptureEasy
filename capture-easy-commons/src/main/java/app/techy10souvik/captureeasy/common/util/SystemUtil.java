package app.techy10souvik.captureeasy.common.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

//import mslinks.ShellLinkException;
//import mslinks.ShellLinkHelper;

//import mslinks.ShellLink;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose
 */
public class SystemUtil extends org.apache.commons.lang.SystemUtils {
	
	/**
	 * @purpose 
	 * @date 03-Jul-2022
	 */
	


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

	

	

	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	
	public static Path getDownloadPath(String version) throws IOException {
		Path path=Paths.get(getRootFolder(), "app-"+version,"assets");
		if(!path.toFile().exists()) {
			createFolder(path.toString());
		}
		return path;
	}
	
	public static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                //System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
	
	
	

	public static String getLogFolder() {

		Path logFolder = Paths.get(getRootFolder(), "logs");
		if (!logFolder.toFile().exists()) {
			createFolder(logFolder.toString());
		}
		return logFolder.toString();
	}

	
	

	
	
	public static String getDocumentFolder()  {
		Path docFolder = Paths.get(FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath(),"Screenshots");
		return docFolder.toString();
	}

	public static void createDesktopShortcut(String target, String shortcut) throws IOException {
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		String shortcutPath = Paths.get(home.getAbsolutePath(), shortcut + ".lnk").toString();
		// https://github.com/DmitriiShamrikov/mslinks
		//ShellLinkHelper.createLink(target, shortcutPath);
		//ShortcutFactory.createDesktopShortcut(target, shortcutPath);
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
