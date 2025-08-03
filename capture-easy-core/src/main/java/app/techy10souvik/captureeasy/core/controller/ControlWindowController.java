package app.techy10souvik.captureeasy.core.controller;

import app.techy10souvik.captureeasy.common.events.AppEvent;
import app.techy10souvik.captureeasy.common.events.EventBus;
import app.techy10souvik.captureeasy.common.events.EventType;
import app.techy10souvik.captureeasy.core.eventdto.CaptureData;
import app.techy10souvik.captureeasy.core.services.CaptureService;

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
		captureService = new CaptureService();
	}


	/**
	 * Register event handlers for UI events.
	 */
	private void registerScreenshotCaptureEvent() {
		EventBus.subscribe(EventType.CAPTURE_SCREENSHOT, event -> {
            try {
                String filePath= captureService.captureScreenshot();
				int count = Math.toIntExact(captureService.getScreenshotCount());
				EventBus.publish(new AppEvent<>(EventType.SCREENSHOT_CAPTURED,new CaptureData(filePath, count)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
		});
	}
	
	public void closeApplication() {
		System.exit(0);
	}
	
	public void deleteScreenshots() throws Exception {
	}
	
	public void saveDocuments(String extension) {
		System.exit(0);
	}


}
