package app.techy10souvik.captureeasy.core.services;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;



public class KeypressListener implements NativeKeyListener  {
//	private static int key=0;
//	private boolean isSpclAltPressed=false;
//	private SystemServiceImpl systemService;
	
	private CaptureService cs;

	/**
	 * @throws IOException 
	 * @throws ConfigurationException 
	 * 
	 */
	public KeypressListener()  {
		//this.systemService=new SystemServiceImpl();
		this.cs = new CaptureService();
	}

	public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
		
	}

	public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
		// TODO Auto-generated method stub
//		cs.captureScreenshot();
//		System.out.println("ok");
	}

	public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
		// TODO Auto-generated method stub
		
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
