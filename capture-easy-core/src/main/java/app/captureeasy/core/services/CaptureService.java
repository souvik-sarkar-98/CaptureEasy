package app.captureeasy.core.services;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import app.captureeasy.common.services.PropertyService;
import app.captureeasy.common.util.SystemUtil;

import javax.imageio.ImageIO;


/**
 * @author Souvik Sarkar
 * @createdOn 02-Jul-2022
 * @purpose 
 */
public class CaptureService {
	private final Robot robot;
	private final PropertyService properties;

	public CaptureService() throws AWTException {
		this.robot = new Robot();
		this.properties = PropertyService.getInstance();
	}

	/** Package-private constructor for unit testing — skips Robot instantiation. */
	CaptureService(PropertyService properties) {
		this.robot = null;
		this.properties = properties;
	}

	public String captureScreenshot() throws Exception {
		if (robot == null) throw new IllegalStateException("CaptureService not initialised with a Robot");
		Rectangle screenRect = new Rectangle(SystemUtil.getScreenSize());
		BufferedImage screenshot = robot.createScreenCapture(screenRect);
		String filePath = properties.getTempFolder() + "/screenshot_" + System.currentTimeMillis() + ".png";
		File outputFile = new File(filePath);
		ImageIO.write(screenshot, "png", outputFile);
		return outputFile.getAbsolutePath();
	}

	public long getScreenshotCount() {
		return SystemUtil.countFilesInDirectory(properties.getTempFolder()).orElse(0L);
	}

	public void deleteScreenshots() throws IOException {
		File folder = new File(properties.getTempFolder());
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					Files.delete(file.toPath());
				}
			}
		}
		folder.delete();
		properties.generateTempFolder();
	}
}
