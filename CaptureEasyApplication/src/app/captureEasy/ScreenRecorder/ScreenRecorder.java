
package app.captureEasy.ScreenRecorder;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.TimerTask;
import org.jcodec.api.awt.AWTSequenceEncoder;

import app.captureEasy.Resources.Library;
import app.captureEasy.UI.RecordPanel;

public class ScreenRecorder extends TimerTask {

	public AWTSequenceEncoder imgEncoder;
	Robot robot;
	BufferedImage image;
	private Rectangle screenDimension;
	public ScreenRecorder(String filename)
	{
		StartScreenRecording(filename);
	}
	public void StartScreenRecording(String filename)
	{
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
				Library.logError(ex,"Exception occured while creating Robot object");
			}
		}
	}
}
