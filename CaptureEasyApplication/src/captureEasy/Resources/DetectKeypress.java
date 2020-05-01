package captureEasy.Resources;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import captureEasy.Launch.Application;
import captureEasy.UI.ActionGUI;
import captureEasy.UI.SensorGUI;


public class DetectKeypress extends Library implements NativeKeyListener  {
	int key=0;

	public void nativeKeyPressed(NativeKeyEvent e) 
	{
		String captureKey=property.getString("CaptureKey","PrtSc");
		if(captureKey.equalsIgnoreCase("PrtSc"))
			captureKey="Print Screen";
		else if(e.getKeyCode()==NativeKeyEvent.VC_PRINTSCREEN)
		{
			SensorGUI.frame.setLocation(10000,10000);
			ImageSelection.setClipboardImage();
			SensorGUI.frame.setLocation(property.getInteger("Xlocation",screensize.width-160),property.getInteger("Ylocation",screensize.height/2+100));
		}
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(captureKey)) {
			if(ActionGUI.leaveControl && !SharedResources.PauseThread)
			{
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
			key=0;
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
