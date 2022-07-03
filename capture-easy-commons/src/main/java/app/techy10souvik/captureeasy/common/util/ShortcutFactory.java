package app.techy10souvik.captureeasy.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.filechooser.FileSystemView;

class ShortcutFactory {
	/**
	 * Creates a Shortcut on the desktop with the passed name and linked to the passed source<br>
	 * Note - this will pause thread until shortcut has been created
	 * @param source - The path to the source file to create a Shortcut to
	 * @param linkName - The name of the Shortcut that will be created
	 * @throws FileNotFoundException - if source File does not exist
	 */
	protected static void createDesktopShortcut(String source, String linkName) throws FileNotFoundException {
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		String linkPath = home+"/"+linkName;
		createShortcut(source, linkPath);
	}
	
	/**
	 * Creates a Shortcut at the passed location linked to the passed source<br>
	 * Note - this will pause thread until shortcut has been created
	 * @param source - The path to the source file to create a Shortcut to
	 * @param linkPath - The path of the Shortcut that will be created
	 * @throws FileNotFoundException 
	 */
	protected static void createShortcut(String source, String linkPath) throws FileNotFoundException {
		File sourceFile = new File(source);
		if(!sourceFile.exists()) {
			throw new FileNotFoundException("The Path: "+sourceFile.getAbsolutePath()+" does not exist!");
		}
		try {
			source = sourceFile.getAbsolutePath();
			
			String vbsCode = String.format(
				  "Set wsObj = WScript.CreateObject(\"WScript.shell\")%n"
				+ "scPath = \"%s\"%n"
				+ "Set scObj = wsObj.CreateShortcut(scPath)%n"
				+ "\tscObj.TargetPath = \"%s\"%n"
				+ "scObj.Save%n",
				linkPath, source
				);
		
			newVBS(vbsCode);
		} catch (IOException | InterruptedException e) {
			System.err.println("Could not create and run VBS!");
			e.printStackTrace();
		} 
	}
	
	/*
	 * Creates a VBS file with the passed code and runs it, deleting it after the run has completed
	 */
	private static void newVBS(String code) throws IOException, InterruptedException {
		File script = File.createTempFile("scvbs", ".vbs"); // File where script will be created
		
		// Writes to script file
		FileWriter writer = new FileWriter(script);
		writer.write(code);
		writer.close();
		
		Process p = Runtime.getRuntime().exec( "wscript \""+script.getAbsolutePath()+"\""); // executes vbs code via cmd
		p.waitFor(); // waits for process to finish
		if(!script.delete()) { // deletes script
			System.err.println("Warning Failed to delete tempory VBS File at: \""+script.getAbsolutePath()+"\"");
		}
	}
}