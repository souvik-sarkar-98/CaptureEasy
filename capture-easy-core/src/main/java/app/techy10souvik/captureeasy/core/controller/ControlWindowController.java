package app.techy10souvik.captureeasy.core.controller;

import app.techy10souvik.captureeasy.common.events.AppEvent;
import app.techy10souvik.captureeasy.common.events.EventBus;
import app.techy10souvik.captureeasy.common.events.EventType;
import app.techy10souvik.captureeasy.common.services.PropertyService;
import app.techy10souvik.captureeasy.common.ui.SystemNotifier;
import app.techy10souvik.captureeasy.core.eventdto.CaptureData;
import app.techy10souvik.captureeasy.core.services.CaptureService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Souvik Sarkar
 * @createdOn 10-Sep-2022
 * @purpose 
 */
public class ControlWindowController {

	private final CaptureService captureService;

	/**
	 * @throws Exception 
	 * 
	 */
	protected ControlWindowController() throws Exception {
		registerScreenshotCaptureEvent();
		registerCloseApplicationEvent();
		registerDeleteScreenshotsEvent();
		registerViewScreenshotEvent();
		captureService = new CaptureService();
	}



	/**
	 * Register event handlers for UI events.
	 */
	private void registerScreenshotCaptureEvent() {
		EventBus.subscribe(EventType.TAKE_SCREENSHOT, event -> {
            try {
                String filePath= captureService.captureScreenshot();
				int count = Math.toIntExact(captureService.getScreenshotCount());
				EventBus.publish(new AppEvent<>(EventType.SCREENSHOT_CAPTURED,filePath));
				EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT,count));
			} catch (Exception e) {
                throw new RuntimeException(e);
            }
		});
	}

	private void registerCloseApplicationEvent() {
		EventBus.subscribe(EventType.CLOSE_APP, event -> {
			System.exit(0);
		});
	}

	private void registerDeleteScreenshotsEvent() {
		EventBus.subscribe(EventType.DELETE_SCREENSHOTS, event -> {
            try {
                captureService.deleteScreenshots();
				int count = Math.toIntExact(captureService.getScreenshotCount());
				EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT,count));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

		});
	}
	
	public void saveDocuments(String extension) {
		//System.exit(0);
	}

	private void registerViewScreenshotEvent() {
		EventBus.subscribe(EventType.SHOW_LATEST_SCREENSHOT, event -> {
			String folder=PropertyService.getInstance().getTempFolder();
			File files = new File(folder);
			if(files.listFiles().length > 0) {
                try {
                    Desktop.getDesktop().open(files);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
			} else {
				SystemNotifier.showSystemNotification("Info", "No screenshots to view.", "INFO");
			}
		});
	}



}
