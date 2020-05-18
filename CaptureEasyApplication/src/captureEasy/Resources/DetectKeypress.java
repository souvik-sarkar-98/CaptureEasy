package captureEasy.Resources;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import captureEasy.Launch.Application;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.PopUp;
import captureEasy.UI.SensorGUI;
import captureEasy.UI.Components.RecordPanel;

public class DetectKeypress extends Library implements NativeKeyListener  {
	int key=0;
	//
	public void nativeKeyPressed(NativeKeyEvent e) 
	{
		String captureKey=property.getString("CaptureKey","PrtSc");
		if(captureKey.equalsIgnoreCase("PrtSc"))
			captureKey="Print Screen";
		else if(e.getKeyCode()==NativeKeyEvent.VC_PRINTSCREEN  )
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
				try{Application.sensor.play();}catch(Exception es){};
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
		else if(key==NativeKeyEvent.VC_ALT && e.getKeyCode() ==NativeKeyEvent.VC_PRINTSCREEN && captureKey.equalsIgnoreCase("ALT+Prtsc") && ActionGUI.leaveControl && !SharedResources.PauseThread)
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
			ActionGUI.viewPanel.gotoPreviousImage();
		}
		else if(e.getKeyCode()==NativeKeyEvent.VC_RIGHT  && ActionGUI.viewPanel!=null)
		{
			ActionGUI.viewPanel.gotoNextImage();
		}
		
		if(key==0)
			key=e.getKeyCode();
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
