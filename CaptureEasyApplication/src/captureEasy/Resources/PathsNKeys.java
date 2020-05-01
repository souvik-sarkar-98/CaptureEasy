package captureEasy.Resources;

public class PathsNKeys{

	/***
	 * @Type= Folder Paths
	 */
	public final static String logFolderPath=Library.createFolder(System.getProperty("user.dir")+"/Resources/Logs");
	public final static String tempFolderPath=Library.createFolder(System.getProperty("user.dir")+"/Resources/bin/Temp");
	public final static String baseDocumentFolderPath=Library.createFolder(System.getProperty("user.home")+"/Documents");
	public final static	String downloadFolderPath =Library. createFolder(String.valueOf(System.getProperty("user.dir")) + "\\Resources\\Downloads");
	public final static String libraryFolderPath =Library.createFolder(System.getProperty("user.dir")+"\\Resources\\lib");
	/**
	 *@ Property file path
	 */
	public final static String PropertyFilePath=Library.createFolder(System.getProperty("user.dir")+"/Resources/Properties/Application.properties");
	public final static String versionInfoFilePath=Library.createFolder(System.getProperty("user.dir")+"/Resources/Properties/SoftwareUpdate.properties");
	public final static String Log4jPropertyFilePath=Library.createFolder(System.getProperty("user.dir")+"/Resources/Properties/Log4j.properties");
	public final static String restartJarPath= System.getProperty("user.dir")+"/Resources/lib/Jars/RestartApplication.jar";
	public final static String sourceJarPath =Library.createFolder(System.getProperty("user.dir")+"\\Resources\\lib\\Jars\\SRC.jar");
	/****
	 * Never ever change this(tempFilePath)
	 */
	public final static String tempFilePath=Library.createFolder(System.getProperty("user.dir")+"/temp.properties");
	

	

	/****
	 * @Type= webURL
	 */
	public static String gitHubBaseURL="https://api.github.com/repos/souvik-sarkar-98/CaptureEasy/releases/latest";
	/***
	 * @Type= Image file path
	 */
	public final static String commentIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/comment.png");
	public final static String createfolderIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/createfolder.png");
	public final static String deleteIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/delete.png");
	public final static String documentIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/document.png");
	public final static String errorIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/error.png");
	public final static String exitIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/exit.png");
	public final static String exportIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/export.png");
	public final static String folderIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/folder.png");
	public final static String redbuttonIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/redbutton.png");
	public final static String informationIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/information.png");
	public final static String leftarrowIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/left_arrow.png");
	public final static String logoIcon1=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/logo1.png");
	public final static String logoIcon2=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/logo2.png");
	public final static String logoIcon3=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/logo3.png");
	public final static String logoIcon4=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/logo4.png");
	public final static String menuIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/menu.png");
	public final static String openIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/open.png");
	public final static String pauseIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/pause.png");
	public final static String playIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/play.png");
	public final static String powerIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/power.png");
	public final static String renameIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/rename.png");
	public final static String rightarrowIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/right_arrow.png");
	public final static String saveIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/save.png");
	public final static String searchIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/search.png");
	public final static String settingIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/settings.png");
	public final static String taskbarIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/taskbar_icon.png");
	public final static String uploadIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/upload.png");
	public final static String viewIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/view.png");
	public final static String warningIcon=Library.createFolder(System.getProperty("user.dir")+"/Resources/lib/Icons/warning.png");

	
}
