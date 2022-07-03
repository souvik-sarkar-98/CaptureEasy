package app.techy10souvik.captureeasy.core.services;

import java.util.ArrayList;
import java.util.List;

import app.techy10souvik.captureeasy.core.apis.CaptureEvent;


/**
 * @author Souvik Sarkar
 * @createdOn 02-Jul-2022
 * @purpose 
 */
public class CaptureService {
	private static int i=0;
	private static List<CaptureEvent> ssEvent=new ArrayList<CaptureEvent>();
	
	public void addCaptureListener(CaptureEvent e) {
		ssEvent.add(e);
	}
	
	public void captureScreenshot() {
		for (CaptureEvent event:ssEvent) {
			event.updateCount(i++);
		}
	}
	

}
