package captureEasy.Resources;

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
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
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

import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.SplashScreen;
import captureEasy.UI.Components.ManageDocumentPanel;
import captureEasy.UI.Components.SavePanel;


public class Library extends SharedResources
{
	/****
	 * 
	 * @utility= logging
	 */

	public static void logError(Exception e,String usermessage)
	{
		System.setProperty("logfilename", logFolderPath + "/Error.log");
		PropertyConfigurator.configure(Log4jPropertyFilePath);
		String exceptionClass="\nException Class : "+e.getClass().getName()+"\n";
		String exceptionMessage="Exception Message : "+e.getMessage()+"\n";
		String exceptionCause="Exception Cause : "+e.getCause();
		String stack="\n\nStackTrace:";
		StackTraceElement[] trace=e.getStackTrace();
		for(StackTraceElement s:trace)
		{
			stack=stack+"\n\t"+s;
		}
		logger.error(usermessage+exceptionClass+exceptionMessage+exceptionCause+stack+"\n\n");
		e.printStackTrace();
	}

	public static void logDebug(String message)
	{
		System.setProperty("logfilename", logFolderPath + "/Debug.log");
		PropertyConfigurator.configure(Log4jPropertyFilePath);
		logger.info(message+"\n");
	}

	public static String timeStamp()
	{
		return new Timestamp(new Date().getTime()).toString().replaceAll(":", "-");
	}

	/*****
	 * 
	 * @utility= file/folder operation
	 */
	public static Path copyToFolder(String src, String dest)
	{
		try {
			Files.copy(Paths.get(src), Paths.get(dest),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logError(e,"Exception occured while copying data");
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
		try{
			if(path!=null){
				createFolder(new File(path).getParent());

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
			new PopUp("ERROR","error",e.getClass().getName()+"Exception occured while creating folder. Visit 'Error.log' for more details.","Ok, I understood","").setVisible(true);

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
		SensorGUI.frame.setLocation(10000,10000);
		try {
			BufferedImage image ;
			String screenshot_name = String.valueOf(++c) + "." + Imageformat;
			image = (new Robot()).createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			File file = new File(createFolder(property.getString("TempPath")) + "\\" + screenshot_name);
			ImageIO.write(image, Imageformat, file);
		} catch (Exception e) 
		{
			logError(e,e.getClass().getName()+" Exception occured while taking screenshot");
			new PopUp("ERROR","error",e.getClass().getName()+"Exception occured while taking screenshot","Ok, I understood","").setVisible(true);
		}  
		SensorGUI.frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
		SensorGUI.frame.setAlwaysOnTop(true);
		return true;
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
				new PopUp("ERROR","error",e.getClass().getName()+" occured while pasteing '"+files[i].getName()+"'. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
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
				new PopUp("ERROR","error",e.getClass().getName()+" occured while loading screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);

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
			new PopUp("ERROR","error",e.getClass().getName()+" occured while saving screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);

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

				@SuppressWarnings("resource")
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
					new PopUp("ERROR","error","Exception Occured.Probable cause is user has selected a corrupted File Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
					
				}
				else
				{
					logError(e1,"Exception occured while loading previous screenshots in a PDF");
					new PopUp("ERROR","error",e1.getClass().getName()+" occured while loading  previous screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
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
			new PopUp("ERROR","error",e.getClass().getName()+" occured while handeling screenshots in PDF. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);

		}              
		try {
			FileUtils.deleteDirectory(new File (extractedPath));
		} catch (IOException e) {
			logError(e,"Exception occured while loading screenshots in a PDF");
			new PopUp("ERROR","error",e.getClass().getName()+" occure whiile delete directory","Ok, I understood","").setVisible(true);
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
			new PopUp("ERROR","error",e.getClass().getName()+" occured while createNewWord. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
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
				new PopUp("ERROR","error","Exception Occured.Probable cause is user has selected a corrupted File Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
			}
			else
			{
				logError(e,e.getClass().getName()+" occured while addToExistingWord. Path :"+filePath+" \nModified File name: "+fileName);
				new PopUp("ERROR","error",e.getClass().getName()+" occured while addToExistingWord. Visit 'Error.log for more details.","Ok, I understood","").setVisible(true);
			}	
		}
	}

	public static void clearTempImages(int interval)
	{
		new Thread(new Runnable(){

			@Override
			public void run() {
				while (!stopThread)
				{
					try{
						File file=new File(createFolder(System.getProperty("user.dir")+"/Resources/bin/Temp"));
						File[] filesdelete=file.listFiles();
						for(File f:filesdelete)
						{
							if(!f.equals(new File(property.getString("TempPath"))))
							{
								FileUtils.deleteDirectory(f);
							}
						}
						Thread.sleep(interval);
					}catch(Exception w){}
				}
			}
		}).start();	
	}
	public static long getPID()
	{
		RuntimeMXBean bean= ManagementFactory.getRuntimeMXBean();
		return Long.valueOf(bean.getName().split("@")[0]);
	}
	public static void updateUI()
	{
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (!stopThread)
				{
					try{
						int count=new File(property.getString("TempPath")).listFiles().length;
						SensorGUI.label_Count.setText(String.valueOf(count)); 
						SensorGUI.frame.setAlwaysOnTop(true);
					}catch(Exception w){}
					try{
						if(ActionGUI.dialog !=null && SensorGUI.window!=null && ActionGUI.dialog.isVisible() )
						{
							SensorGUI.window.dispose();
							SensorGUI.window=null;
							PopUp.control=true;
							
						}
					}catch(Exception e){}
					SplashScreen.displaySplash=false;
					for(int i=0;i<ManageDocumentPanel.pathTraverse.size();i++)
					System.out.println(ManageDocumentPanel.pathTraverse.get(i) +"   "+ManageDocumentPanel.pointer);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	//
}

