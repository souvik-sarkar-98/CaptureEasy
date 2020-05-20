package captureEasy.Resources;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.Components.RecordPanel;

public class DetectKeypress extends Library implements NativeKeyListener  {
	int key=0;
	public void nativeKeyPressed(NativeKeyEvent e) 
	{
		String captureKey=property.getString("CaptureKey","PrtSc");
		if(captureKey.equalsIgnoreCase("PrtSc"))
			captureKey="Print Screen"; 
		else if(e.getKeyCode()==NativeKeyEvent.VC_PRINTSCREEN && e.getRawCode()==44)
		{
			SensorGUI.frame.setLocation(10000,10000);
			if(!captureKey.equalsIgnoreCase("ALT+Prtsc"))
			{
				ImageSelection.setClipboardImage();
			}
			else
			{
				try{Thread.sleep(250);}catch(Exception p8){}
			}
			SensorGUI.frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
		}
		if(key==0 && e.getKeyCode()==NativeKeyEvent.VC_ALT && captureKey.equalsIgnoreCase("ALT+PrtSc"))
		{
			SensorGUI.frame.setVisible(false);
		}
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(captureKey)) {
			if(ActionGUI.leaveControl && !SharedResources.PauseThread)
			{
				if(captureKey.equalsIgnoreCase("Print Screen"))
				{
					if(key!=56)
						captureScreen();
					else
					{
						SensorGUI.frame.setVisible(false);
						try{Thread.sleep(250);}catch(Exception p8){}
						SensorGUI.frame.setVisible(false);
					}
				}
				else
					captureScreen();
			}
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_ESCAPE)
		{
			if((property.getString("DocPath")!=null) && !IsEmpty(property.getString("TempPath","")))
			{
				ActionGUI.dialog.dispose();
				ActionGUI.leaveControl=true;
			}
		}
		else if(key==29 && e.getKeyCode() ==56 && captureKey.equalsIgnoreCase("Ctrl+ALT") && ActionGUI.leaveControl && !SharedResources.PauseThread)
		{
			captureScreen();
		}
		else if(key==29 && e.getKeyCode() ==42 && captureKey.equalsIgnoreCase("Ctrl+Shift") && ActionGUI.leaveControl && !SharedResources.PauseThread)
		{
			captureScreen();
		}
		else if(key==NativeKeyEvent.VC_ALT && e.getKeyCode() ==NativeKeyEvent.VC_PRINTSCREEN &&e.getRawCode()==44 && captureKey.equalsIgnoreCase("ALT+Prtsc") && ActionGUI.leaveControl && !SharedResources.PauseThread)
		{
			try {
				Thread.sleep(500);
				Transferable content=Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
				BufferedImage image=(BufferedImage) content.getTransferData(DataFlavor.imageFlavor);
				File file = new File(createFolder(property.getString("TempPath")) + "\\" + String.valueOf(++(Library.c)) + "." + property.getString("ImageFormat","png").toLowerCase());
				ImageIO.write(image, property.getString("ImageFormat","png").toLowerCase(), file);
			} catch (Exception e1) {
				PopUp p=new PopUp("ERROR","error","Screen Captute Failed !  "+e1.getClass().getSimpleName()+" Occured while getting data from clipboard. Please try again. ","Okay","");
				PopUp.PopDia=p;
				p.setVisible(true);;
				logError(e1,e.getClass().getSimpleName()+" Occured while getting data from clipboard.");
			}
		}
		else if(e.getKeyCode()==NativeKeyEvent.VC_LEFT && ActionGUI.viewPanel!=null)
		{
			if(PopUp.PopDia!=null && !PopUp.PopDia.isVisible() ) 
			try{if(!PopUp.PopDia.isVisible() ) 
				ActionGUI.viewPanel.gotoNextImage();
			}catch(Exception e88){ActionGUI.viewPanel.gotoNextImage();}
		}
		else if(e.getKeyCode()==NativeKeyEvent.VC_RIGHT  && ActionGUI.viewPanel!=null)
		{
			try{if(!PopUp.PopDia.isVisible() ) 
				ActionGUI.viewPanel.gotoNextImage();
			}catch(Exception e88){ActionGUI.viewPanel.gotoNextImage();}
		}
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_0 )
		{
			PopUp p=new PopUp("Confirm Exit","warning","Do you want to exit the application?\n","Yes","No");
			PopUp.PopDia=p;
			PopUp.btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					senGUI.closeApplication(true);
				}

			});
			}
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_1 )
		{
			senGUI.playPauseAction();
		}
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_2 )
		{
			senGUI.deleteAction();
		}
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_3 )
		{
			senGUI.saveAction();	
		}
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_4 )
			senGUI.viewAction();	
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_5 )
			senGUI.manageDocumentsActions();	
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_6 )
			senGUI.settingsAction();
		else if(key==NativeKeyEvent.VC_CONTROL && e.getKeyCode() ==NativeKeyEvent.VC_7 )
			senGUI.recordAction();
		else if(e.getKeyCode()==NativeKeyEvent.VC_ENTER && ActionGUI.dialog!=null && ActionGUI.dialog.isVisible())
		{

			String tabname=ActionGUI.TabbledPanel.getTitleAt(ActionGUI.TabbledPanel.getSelectedIndex());
			if(tabname.contains("Settings")){}
				//ActionGUI.settingsPanel.save();
			else if(tabname.contains("Manage"))
				ActionGUI.documentPanel.searchAction();
			else if(tabname.contains("View"))
			{
				if(PopUp.PopDia!=null && PopUp.PopDia.isVisible() )
					PopUp.btnNewButton.doClick();
				else 
					ActionGUI.viewPanel.addComment();
			}	
			else if(tabname.contains("Record"))
			{
				ActionGUI.screenRecord.saveVideo();
			}
		}
		
		
		if(key==0)
			key=e.getKeyCode();
		if(e.getKeyCode() ==NativeKeyEvent.VC_PRINTSCREEN &&e.getRawCode()==106)
			logKeyStrokes("'*' Pressed. ( KeyCode="+e.getKeyCode()+",RawCode="+e.getRawCode()+" )");
		else
			logKeyStrokes("'"+NativeKeyEvent.getKeyText(e.getKeyCode())+"' Pressed. ( KeyCode="+e.getKeyCode()+",RawCode="+e.getRawCode()+" )");
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if(key==e.getKeyCode())
		{
			key=0;
			if(!SensorGUI.frame.isVisible() && RecordPanel.isRecording==false)
				SensorGUI.frame.setVisible(true);
			if(SensorGUI.frame.getLocation().x==10000 && SensorGUI.frame.getLocation().y==10000 )
				SensorGUI.frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
			if(e.getKeyCode() ==NativeKeyEvent.VC_PRINTSCREEN &&e.getRawCode()==106)
				logKeyStrokes("'*' Released. ( KeyCode="+e.getKeyCode()+",RawCode="+e.getRawCode()+" )");
			else
				logKeyStrokes("'"+NativeKeyEvent.getKeyText(e.getKeyCode())+"' Released. (KeyCode="+e.getKeyCode()+",RawCode="+e.getRawCode()+")\n");
		}
		else
		{
			if(e.getKeyCode() ==NativeKeyEvent.VC_PRINTSCREEN &&e.getRawCode()==106)
				logKeyStrokes("'*' Released. ( KeyCode="+e.getKeyCode()+",RawCode="+e.getRawCode()+" )");
			else
				logKeyStrokes("'"+NativeKeyEvent.getKeyText(e.getKeyCode())+"' Released. ( KeyCode="+e.getKeyCode()+" , RawCode="+e.getRawCode()+" )");

		}
	}
	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
	}

}


class ImageSelection implements Transferable {
	private Image image;

	public ImageSelection(Image image) {
		this.image = image;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.imageFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DataFlavor.imageFlavor.equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (!DataFlavor.imageFlavor.equals(flavor)) {
			throw new UnsupportedFlavorException(flavor);
		}
		return image;
	}

	public static void setClipboardImage()
	{
		ImageSelection imgSel;
		try {
			imgSel = new ImageSelection(new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imgSel, null);
		} catch (Exception e) {
			try{Thread.sleep(1000);}catch(Exception e5){}
			setClipboardImage();
		}
	}
}
