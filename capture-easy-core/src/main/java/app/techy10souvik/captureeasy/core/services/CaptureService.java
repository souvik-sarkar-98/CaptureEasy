package app.techy10souvik.captureeasy.core.services;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import app.techy10souvik.captureeasy.common.services.PropertyService;
import app.techy10souvik.captureeasy.common.util.PropertyUtil;
import app.techy10souvik.captureeasy.common.util.SystemUtil;

import javax.imageio.ImageIO;


/**
 * @author Souvik Sarkar
 * @createdOn 02-Jul-2022
 * @purpose 
 */
public class CaptureService {
	private static Robot robot;
	private final PropertyService properties;

	public CaptureService() throws Exception {
		robot = new Robot();
		properties = PropertyService.getInstance();
	}

	public String captureScreenshot() throws Exception {
		Rectangle screenRect = new Rectangle(SystemUtil.getScreenSize());
		BufferedImage screenshot = robot.createScreenCapture(screenRect);
		String filePath = properties.getTempFolder() + "/" +"screenshot_"+System.currentTimeMillis()+".png";
		File outputFile = new File(filePath);
		ImageIO.write(screenshot, "png", outputFile);
		return outputFile.getAbsolutePath();
	}

	public long getScreenshotCount() {
		return SystemUtil.countFilesInDirectory(properties.getTempFolder());
	}
}
