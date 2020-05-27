package app.captureEasy.Resources;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import app.captureEasy.Annotations.NoLogging;
import app.captureEasy.Annotations.Update;
import app.captureEasy.UI.RecordPanel;
import app.captureEasy.UI.SavePanel;
import app.captureEasy.UI.SensorGUI;
import app.captureEasy.UI.Components.PopUp;
import app.captureEasy.UI.Components.SplashScreen;


public class Library extends SharedResources
{
	public static long processID=getPID();
	public static String[] monthName = {"January", "February",
			"March", "April", "May", "June", "July",
			"August", "September", "October", "November",
	"December"};
	/****
	 * 
	 * @utility= logging
	 */
	@NoLogging
	public static void logError(Exception e,String usermessage)
	{
		System.setProperty("logfilename", createFolder(logFolderPath) + "/Error-"+LocalDate.now().toString()+".log");
		log4j.setProperty("log4j.appender.FileAppender.layout.ConversionPattern", "[%-5p] [%d{dd MMM yyyy HH:mm:ss}]  %m%n");
		PropertyConfigurator.configure(Log4jPropertyFilePath);		String exceptionClass="\n\nException Class : "+e.getClass().getName()+"\n";
		String exceptionMessage="Exception Message : "+e.getMessage()+"\n";
		String exceptionCause="Exception Cause : "+e.getCause();
		String stack="\n\nStackTrace:";
		StackTraceElement[] trace=e.getStackTrace();
		for(StackTraceElement s:trace)
		{
			stack=stack+"\n\t"+s;
		}
		logger.info("User Message: "+usermessage+exceptionClass+exceptionMessage+exceptionCause+stack+"\n\n");
		e.printStackTrace();
	}

	/*@NoLogging
	public static void logProcess(String processName,String printPattern,String message)
	{	
		log4j.setProperty("log4j.appender.FileAppender.layout.ConversionPattern",printPattern); 
		logProcess(processName,message);
	}
	@NoLogging
	public static void logProcess(String processName,String message)
	{	
		System.setProperty("logfilename", logFolderPath + "/"+processName+"-"+processID+".log");
		PropertyConfigurator.configure(Log4jPropertyFilePath);
		logger.info(message);
		log4j.setProperty("log4j.appender.FileAppender.layout.ConversionPattern", "[%-5p] [%d{dd MMM yyyy HH:mm:ss}]  %m%n");
	}*/
	@NoLogging
	public static void logProcess(String processName,String message)
	{
		logProcess(processName,"",message);
	}
	@NoLogging
	public static void logProcess(String processName,String frontNewLine,String message)
	{
		String filename=logFolderPath + "/"+processName+"-"+processID+".log";
		try{
			FileWriter fw=new FileWriter(filename,true);
			fw.write(frontNewLine+timeStamp()+"  |"+message +"\n");
			fw.flush();
			fw.close();
		}catch(Exception e)
		{

		}
	}


	@NoLogging
	public static String timeStamp()
	{
		return "["+new SimpleDateFormat("dd MMM yyyy hh:mm:ss").format(new Date())+"]";
	}

	/*****
	 * 
	 * @utility= file/folder operation
	 */

	public static Path moveToFolder(String src, String dest)
	{
		try {
			Files.move(Paths.get(src), Paths.get(dest),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logError(e,"Exception occured while moving file");
		};
		return  Paths.get(dest);
	}

	public static Path copyToFolder(String src, String dest)
	{
		try {
			Files.copy(Paths.get(src), Paths.get(dest),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logError(e,"Exception occured while copying file");
		};
		return  Paths.get(dest);
	}

	public static String createTemp()
	{
		return createFolder(tempFolderPath+"\\"+new Random().nextInt(1000000000));
	}

	public static String getSubFolders(String basepath,String folderName)
	{
		String[] monthName = {"January", "February",
				"March", "April", "May", "June", "July",
				"August", "September", "October", "November",
		"December"};
		Calendar cal = Calendar.getInstance();
		String month = monthName[cal.get(Calendar.MONTH)];
		if(true==property.getBoolean("showFolderNameField",false))
		{
			if(true==property.getBoolean("setFolderNameMandatory",false))
			{
				return basepath+"\\"+folderName;
			}
			else
			{
				if(folderName.replaceAll("\\s", "").equals(""))
					return basepath+"\\"+month+" "+cal.get(Calendar.YEAR)+"\\"+month+" "+cal.get(Calendar.DAY_OF_MONTH); 
				else
					return basepath+"\\"+folderName;
			}
		}
		else
		{
			return basepath+"\\"+month+" "+cal.get(Calendar.YEAR)+"\\"+month+" "+cal.get(Calendar.DAY_OF_MONTH);
		}
	}

	public static String createSubFolders(String basepath,String folderName)
	{
		Calendar cal = Calendar.getInstance();
		String month = monthName[cal.get(Calendar.MONTH)];
		if(true==property.getBoolean("showFolderNameField",false))
		{
			if(true==property.getBoolean("setFolderNameMandatory",false))
			{
				return createFolder(basepath+"\\"+folderName);
			}
			else
			{
				if(folderName.replaceAll("\\s", "").equals(""))
					return createFolder(createFolder(basepath+"\\"+month+" "+cal.get(Calendar.YEAR))+"\\"+month+" "+cal.get(Calendar.DAY_OF_MONTH)); 
				else
					return createFolder(basepath+"\\"+folderName);
			}
		}
		else
		{
			return createFolder(createFolder(basepath+"\\"+month+" "+cal.get(Calendar.YEAR))+"\\"+month+" "+cal.get(Calendar.DAY_OF_MONTH));
		}
	}

	/**
	 * @Type: File Processing Method
	 * @name= createFolder(String path)
	 */
	public static String createFolder(String path)
	{
		return createFolder( path,"");
	}

	@NoLogging
	public static String createFolder(String path,String a)
	{
		try{
			if(path!=null)
			{
				createFolder(new File(path).getParent(),"");

				if (! new File(path).exists())
				{
					if(FilenameUtils.getExtension(path).equals(""))
					{
						new File(path).mkdir();
					}
				}
			}
		}
		catch(NullPointerException e){
			logError(e,e.getClass().getName()+" Exception occured while creating folder.\nPath: "+path);
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+"Exception occured while creating folder. Visit 'Error.log' for more details.","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}
		return path;
	}

	public static boolean IsEmpty(String Path) 
	{    
		int count=0;
		boolean var=false;
		File directory = new File(Path);
		String[] a=directory.list(); 
		if(a!=null)
		{
			for(int i=0; i<a.length; i++)
			{
				if (a[i].toLowerCase().contains(".jpeg") || a[i].toLowerCase().contains(".jpg") || a[i].toLowerCase().contains(".png") ||a[i].toLowerCase().contains(".bmp"))
				{
					count++;
				}
			}
		}
		if (count==0)
			var= true;
		else
			var=false;
		return var ;
	}


	/**
	 * 
	 * @uttility= system
	 * @Type: Screenshot Processing Method
	 * @name= resetClipboard()
	 */

	public static void resetClipboard(String text)
	{
		StringSelection stringSelection = new StringSelection(text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents((Transferable) stringSelection, null);
	}

	public static int lastFileName(String dirPath){
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return 0;
		}
		int res=0;
		for (int i = 0; i < files.length; i++) {
			int in=Integer.parseInt(files[i].getName().substring(0,files[i].getName().indexOf(".")));
			if (in>res) {
				res=in;
			}
		}
		return res;
	}
	public static int c=0;

	public static boolean captureScreen() {
		String Imageformat=property.getString("ImageFormat","png").toLowerCase();
		senGUI.frame.setLocation(10000,10000);
		File file = null;
		try {
			if(robot==null)
				robot=new Robot();
			BufferedImage image ;
			String screenshot_name = String.valueOf(++c) + "." + Imageformat;
			image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			file = new File(property.getString("TempPath") + "\\" + screenshot_name);
			ImageIO.write(image, Imageformat, file);
		} catch (Exception e) 
		{
			logError(e,e.getClass().getName()+" Exception occured while taking screenshot");
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+"Exception occured while taking screenshot","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}  
		senGUI.frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
		senGUI.frame.setAlwaysOnTop(true);
		return file.exists();
	}

	/**
	 * @utility: Word Processing Method
	 * @name= LoadImages(String path,XWPFRun run)
	 */

	public static void loadImages(String Temppath,XWPFRun run,String FileName) 
	{

		File[] files = new File(Temppath).listFiles();
		SavePanel.lblUpdatingFiles.setText("Sorting files ...");
		sortFiles(files);

		for(int i=0;i<files.length;i++)
		{
			InputStream pic;
			try {
				pic = new FileInputStream(files[i].getPath());
				SavePanel.lblUpdatingFiles.setText("Storing "+files[i].getName()+" to "+FileName);
				if(comments.get(files[i].getName())!=null)
					run.setText(comments.get(files[i].getName()));
				run.addPicture(pic, XWPFDocument.PICTURE_TYPE_PNG, files[i].getName(), Units.toEMU(470), Units.toEMU(265));
				SharedResources.progress=(int)Math.round(((Double.valueOf(i+1))/Double.valueOf(files.length))*100);
				pic.close();
				if(i!=files.length-1)
				{
					if(i%2==0)
						run.addBreak();
					else 
						run.addBreak(BreakType.PAGE);
				}
			} catch (InvalidFormatException | IOException e) {
				logError(e,e.getClass().getName()+" occured while pasteing '"+files[(int) i].getName()+"'. File Path: "+files[i].getPath());
				PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while pasteing '"+files[i].getName()+"'. Visit 'Error.log for more details.","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
			}

		}
	}

	public static void loadImages(String tempPath,Document document,String FileName,String msg)
	{
		SavePanel.lblUpdatingFiles.setText("Sorting "+msg+" files ...");
		File[] tempFiles = new File(tempPath).listFiles();
		sortFiles(tempFiles);
		for(int i=0;i<tempFiles.length;i++)
		{
			try {                 
				Image image = new Image(ImageDataFactory.create(tempFiles[i].getPath()));                        
				image.setAutoScale(true);
				SavePanel.lblUpdatingFiles.setText("Storing "+msg+" file "+tempFiles[i].getName()+" to "+FileName);
				if(comments.get(tempFiles[i].getName())!=null)
					document.add(new Paragraph(comments.get(tempFiles[i].getName())));
				document.add(image); 
				SharedResources.progress=(int)Math.round(((Double.valueOf(i+1))/Double.valueOf(tempFiles.length))*100);
				if(i!=tempFiles.length-1)
				{
					if(i%2==0)
						document.add(new Paragraph("\n"));
					else 
						document.add(new AreaBreak());
				}
			}catch(Exception e){
				SavePanel.lblUpdatingFiles.setText(e.getClass().getSimpleName()+" Occured ");
				logError(e,"Exception occured while loading screenshots in a PDF");
				PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while loading screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
			}
		}  
	}



	public static void sortFiles(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				int n1 = extractNumber(o1.getName());
				int n2 = extractNumber(o2.getName());
				return n1 - n2;
			}

			private int extractNumber(String name) {
				int i = 0;
				try {
					int e = name.lastIndexOf('.');
					String number = name.substring(0, e);
					i = Integer.parseInt(number);
				} catch(Exception e) {
					i = 0; 
				}
				return i;
			}
		});
	}

	public static void SaveAsPDF(String DocumentPath,String fileName,String foldername)
	{
		String tempPath = property.getString("TempPath");
		try {
			SavePanel.lblUpdatingFiles.setText("Creating "+fileName+".pdf");
			Document document = new Document(new PdfDocument(new PdfWriter(createSubFolders(DocumentPath,foldername)+"\\"+fileName+".pdf")));
			loadImages(tempPath,document,fileName+".pdf","");
			document.close();
			SharedResources.progress=0;
			SavePanel.lblUpdatingFiles.setText("Saving "+fileName+".pdf");
			SavePanel.lblUpdatingFiles.setText(""+fileName+".pdf"+" is ready to use.");
			if(SavePanel.rdbtnNo.isSelected())
			{
				Library.c=0;
				property.setProperty("TempPath", createTemp());
				comments.clear();
			}
		} catch (FileNotFoundException e) {
			SavePanel.lblUpdatingFiles.setText(e.getClass().getSimpleName()+" Occured ");
			logError(e,"Exception occured while loading screenshots in a PDF");
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while saving screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;

		}              


	}

	public static void SaveAsPDFFromWord(String filePath,String fileName)
	{
		Document document = null;
		File f = null;
		boolean exceptionFlag=false;
		String extractedPath=extractedFile;
		String tempPath = property.getString("TempPath");
		try {
			if(SavePanel.chckbxOverwriteSelectedFile.isSelected()==true)
			{
				f=new File(filePath.replace(".docx", ".pdf"));
				document = new Document(new PdfDocument(new PdfWriter(f.getParent()+"\\"+fileName+".pdf")));
			}
			else
			{
				f=new File(filePath.replace(".docx", ".pdf"));
				document = new Document(new PdfDocument(new PdfWriter(f.getPath())));
			}
			SavePanel.lblUpdatingFiles.setText("Creating "+f.getName());
			SavePanel.lblUpdatingFiles.setText("Getting previous files ...");
			int i=1;
			try{

				XWPFDocument docx=new XWPFDocument(new FileInputStream(filePath));
				List<XWPFPictureData> picture=docx.getAllPictures();
				Iterator<XWPFPictureData> iterator=picture.iterator();
				while(iterator.hasNext()){
					XWPFPictureData pic=iterator.next();		
					SavePanel.lblUpdatingFiles.setText("Getting previous file "+i+"."+ pic.suggestFileExtension());
					ImageIO.write(ImageIO.read(new ByteArrayInputStream(pic.getData())), pic.suggestFileExtension(), 
							new File(extractedFile+"\\"+pic.getFileName().replace("image", "")));
					SharedResources.progress=(int)Math.round(((Double.valueOf(i))/Double.valueOf(picture.size()))*100);
					i++;
				}
				docx.close();
				SharedResources.progress=0;
				SavePanel.ProgressBar.setValue(0);
				SavePanel.panel_Progress.repaint();
				SavePanel.lblUpdatingFiles.setText("Storing previous files..");
				loadImages(extractedPath,document,f.getName(),"previous");
			}catch(Exception e1){
				exceptionFlag= true;
				if(e1.getMessage().equalsIgnoreCase("No valid entries or contents found, this is not a valid OOXML (Office Open XML) file"))
				{
					logError(e1,"Exception occured while loading previous screenshots in a PDF. Probable cause is user has selected a corrupted File ");
					PopUp p= new PopUp("ERROR","error","Exception Occured.Probable cause is user has selected a corrupted File Visit 'Error.log for more details.","Ok, I understood","");
					p.setVisible(true);
					PopUp.PopDia=p;
				}
				else
				{
					logError(e1,"Exception occured while loading previous screenshots in a PDF");
					PopUp p=new PopUp("ERROR","error",e1.getClass().getName()+" occured while loading  previous screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","");
					p.setVisible(true);
					PopUp.PopDia=p;
				}

			}
			SharedResources.progress=0;
			SavePanel.ProgressBar.setValue(0);
			SavePanel.panel_Progress.repaint();
			SavePanel.lblUpdatingFiles.setText("Storing current files..");
			loadImages(tempPath,document,f.getName(),"current");
			document.close();
			SharedResources.progress=0;
			SavePanel.lblUpdatingFiles.setText("Saving "+fileName+".pdf");
			SavePanel.lblUpdatingFiles.setText(""+fileName+".pdf"+" is ready to use.");

			if(!exceptionFlag && SavePanel.rdbtnNo.isSelected())
			{
				Library.c=0;
				property.setProperty("TempPath", createTemp());
				comments.clear();
			}
		} catch (FileNotFoundException e) {
			SavePanel.lblUpdatingFiles.setText(e.getClass().getSimpleName()+" Occured ");
			logError(e,"Exception occured while loading screenshots in a PDF");
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while handeling screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;

		}              
		try {
			FileUtils.deleteDirectory(new File (extractedPath));
		} catch (IOException e) {
			logError(e,"Exception occured while loading screenshots in a PDF");
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occure whiile delete directory","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}

	}


	/**
	 * @Type: Word Processing Method
	 * @name= createNewWord(String DocumentPath,String testName)
	 */

	public static void createNewWord(String DocumentPath,String fileName,String foldername)
	{
		try
		{
			SavePanel.lblUpdatingFiles.setText("Creating "+fileName+".docx");
			XWPFDocument createNewWordDocument =new XWPFDocument();
			XWPFRun createNewWordRun=createNewWordDocument.createParagraph().createRun();
			loadImages(property.getString("TempPath"),createNewWordRun,fileName+".docx");
			SavePanel.lblUpdatingFiles.setText("Saving "+fileName+".docx");
			FileOutputStream createNewWordOut=new FileOutputStream(createSubFolders(DocumentPath,foldername)+"\\"+fileName+".docx");
			createNewWordDocument.write(createNewWordOut);
			createNewWordOut.flush();
			createNewWordOut.close();
			createNewWordDocument.close();
			createNewWordDocument=null;
			SharedResources.progress=0;
			SavePanel.lblUpdatingFiles.setText(""+fileName+".docx is ready to use.");
			if(SavePanel.rdbtnNo.isSelected())
			{
				Library.c=0;
				property.setProperty("TempPath", createTemp());
				comments.clear();
			}
		}
		catch(Exception e)	{
			logError(e,e.getClass().getName()+" occured while createNewWord. Path :"+DocumentPath+" \nFile name: "+fileName);
			PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while createNewWord. Visit 'Error.log for more details.","Ok, I understood","");
			p.setVisible(true);
			PopUp.PopDia=p;
		}
	}

	/**
	 * @Type: Word Processing Method
	 * @name= addToExistingWord(File file)
	 */

	public static void addToExistingWord(String filePath,String fileName)
	{
		FileOutputStream addToExistingWordOut;
		String fOut=null;
		try
		{
			File f=new File(filePath);
			SavePanel.lblUpdatingFiles.setText("Loading "+f.getName());
			XWPFDocument addToExistingWordDocument = new XWPFDocument(new FileInputStream(f));
			SavePanel.lblUpdatingFiles.setText("Fetching existing images");
			XWPFRun addToExistingWordRun = addToExistingWordDocument.getLastParagraph().createRun();		
			if(SavePanel.chckbxOverwriteSelectedFile.isSelected()==true)
			{
				fOut=f.getName();
				SavePanel.lblUpdatingFiles.setText("Overwriting "+fOut);
				loadImages(property.getString("TempPath"),addToExistingWordRun,fOut);
				addToExistingWordOut = new FileOutputStream(f);
			}
			else
			{
				fOut=fileName+".docx";
				SavePanel.lblUpdatingFiles.setText("Creating new file "+fileName+".docx");
				loadImages(property.getString("TempPath"),addToExistingWordRun,fOut);
				addToExistingWordOut = new FileOutputStream(f.getParent()+"\\"+fileName+".docx");
			}
			SavePanel.lblUpdatingFiles.setText("Saving "+fOut);
			addToExistingWordDocument.write(addToExistingWordOut);
			addToExistingWordOut.flush();
			addToExistingWordOut.close();
			addToExistingWordDocument.close();
			addToExistingWordDocument=null;
			SavePanel.lblUpdatingFiles.setText(fOut+" is ready to use.");
			SharedResources.progress=0;
			if(SavePanel.rdbtnNo.isSelected())
			{
				c=0;
				property.setProperty("TempPath", createTemp());
				comments.clear();
			}
		}
		catch(Exception e){
			if(e.getMessage().equalsIgnoreCase("No valid entries or contents found, this is not a valid OOXML (Office Open XML) file"))
			{
				logError(e,"Exception occured while addToExistingWord. Probable cause is user has selected a corrupted File ");
				PopUp p=new PopUp("ERROR","error","Exception Occured.Probable cause is user has selected a corrupted File Visit 'Error.log for more details.","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
			}
			else
			{
				logError(e,e.getClass().getName()+" occured while addToExistingWord. Path :"+filePath+" \nModified File name: "+fileName);
				PopUp p=new PopUp("ERROR","error",e.getClass().getName()+" occured while addToExistingWord. Visit 'Error.log for more details.","Ok, I understood","");
				p.setVisible(true);
				PopUp.PopDia=p;
			}	
		}
	}

	public static void clearFilesTask(int interval)
	{
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (!stopThread)
				{
					try{
						File tempFile=new File(tempFolderPath);
						logProcess("Process_ClearFile","\n","Total tempFile count="+tempFile.listFiles().length);
						for(File f:tempFile.listFiles())
						{
							if((!f.equals(new File(property.getString("TempPath") ))&& f.isDirectory()) || (f.length()/1024)>1024)
							{
								try{FileUtils.deleteDirectory(f);
								logProcess("Process_ClearFile","Temporary File "+f.getName()+" Successfully deleted. FilePath : "+f.getAbsolutePath());
								}catch(Exception e){
									logProcess("Process_ClearFile","Temporary File "+f.getName()+" deletion failed. Visit error log for more details. FilePath : "+f.getAbsolutePath());
									logError(e,"");
								}
							}
							else if(RecordPanel.doDelete && f.getName().equalsIgnoreCase("DoNotDelete.mp4"))
							{
								try{FileUtils.forceDelete(f);
								logProcess("Process_ClearFile","Video File "+f.getName()+" Successfully deleted. FilePath : "+f.getAbsolutePath());
								}catch(Exception e){
									logProcess("Process_ClearFile","Video File "+f.getName()+" deletion failed. Visit error log for more details. FilePath : "+f.getAbsolutePath());
									logError(e,"");
								}
							}
							else
								logProcess("Process_ClearFile","File "+f.getName()+" skipped because this file is associated with currently running process ("+processID+"). FilePath : "+f.getAbsolutePath());

						}
						File logFile=new File(logFolderPath);
						logProcess("Process_ClearFile","\n","Total logFile count="+logFile.listFiles().length);

						for(File f:logFile.listFiles())
						{
							if((f.length()/1024)>1024)
							{
								try{FileUtils.forceDelete(f);
								logProcess("Process_ClearFile","Oversized log File "+f.getName()+" Successfully deleted. FilePath : "+f.getAbsolutePath());
								}catch(Exception e){
									logProcess("Process_ClearFile","Oversized log File "+f.getName()+" deletion failed. Visit error log for more details. FilePath : "+f.getAbsolutePath());
									logError(e,"");
								}
							}
							else if(f.getName().toLowerCase().contains(String.valueOf(processID))){
								logProcess("Process_ClearFile","Log File "+f.getName()+" skipped because this file is associated with currently running process ("+processID+"). FilePath : "+f.getAbsolutePath());
							}
							else if(f.getName().toLowerCase().contains(LocalDate.now().toString().toLowerCase())){}
							else
							{
								try{FileUtils.forceDelete(f);
								logProcess("Process_ClearFile","Outdated log File "+f.getName()+" Successfully deleted. FilePath : "+f.getAbsolutePath());
								}catch(Exception e){
									logProcess("Process_ClearFile","Outdated log File "+f.getName()+" deletion failed. Visit error log for more details. FilePath : "+f.getAbsolutePath());
									logError(e,"");
								}
							}
						}
						Thread.sleep(interval);
					}catch(Exception w){w.printStackTrace();}
				}
			}
		}).start();	
	}
	@NoLogging
	public static long getPID()
	{
		RuntimeMXBean bean= ManagementFactory.getRuntimeMXBean();
		return Long.valueOf(bean.getName().split("@")[0]);
	}
	int countPrev;
	@Update
	public void updateUITask()
	{
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (!stopThread)
				{
					try{
						int count=new File(property.getString("TempPath")).listFiles().length;
						SensorGUI.label_Count.setText(String.valueOf(count));
						
						if(count!=countPrev)
							logProcess("Process_UpdateUI","count:"+count+"\tUI Showing:"+SensorGUI.label_Count.getText());
						countPrev=count;
					}catch(Exception e){
						logProcess("Process_UpdateUI","Exception occured while updating file count. Visit log folder");
						logError(e,"error update count ");
					}
					try{
						//if((senGUI!=null && senGUI.frame.isVisible())|| (ActionGUI.dialog!=null && ActionGUI.dialog.isVisible()))
							SplashScreen.displaySplash=false;}catch(Exception e){}
					try{
						if(RecordPanel.isRecording)
						{
							if(RecordPanel.recDialog.getLocation().x==10000 && RecordPanel.recDialog.getLocation().y==10000 )
								RecordPanel.recDialog.setLocation(5, 5);
							else
								RecordPanel.recDialog.setLocation(10000, 10000);
							Thread.sleep(1000);
						}
					}catch(Exception e){}
				}
			}
		}).start();
	}
}

