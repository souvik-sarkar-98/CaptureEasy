
package captureEasy.ScreenRecorder;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TimerTask;
import org.jcodec.api.awt.AWTSequenceEncoder;

import captureEasy.Resources.Library;
import captureEasy.UI.Components.RecordPanel;

public class ScreenRecorder extends TimerTask {

	public AWTSequenceEncoder imgEncoder;
	Robot robot;
	BufferedImage image;
	private Rectangle screenDimension;
	public ScreenRecorder(String filename) {
		/*try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
			{
				if ("Nimbus".equals(info.getName())) 
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				} 
			}
		}catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
			Library.logError(e,"Exception occured while setinng UI (ScreenRecord)");
		}*/
		try {
			robot = new Robot();
			imgEncoder = AWTSequenceEncoder.createSequenceEncoder(new File(filename), 24 / 8);
			screenDimension=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (Exception ex) {
			//
			Library.logError(ex,"Exception occured while creating Robot object");
		}
	}

	@Override
	public void run() {
		if(RecordPanel.isRecording)
		{
			image = robot.createScreenCapture(screenDimension);
			try {
				imgEncoder.encodeImage(image);
			} catch (Exception ex) {
				//
				Library.logError(ex,"Exception occured while creating Robot object");
			}
		}
		
	}
}
