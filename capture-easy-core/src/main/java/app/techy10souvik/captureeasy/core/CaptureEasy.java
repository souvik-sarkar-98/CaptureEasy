package app.techy10souvik.captureeasy.core;

import javax.swing.SwingUtilities;

import app.techy10souvik.captureeasy.common.events.AppEvent;
import app.techy10souvik.captureeasy.common.events.EventBus;
import app.techy10souvik.captureeasy.common.events.EventType;
import app.techy10souvik.captureeasy.common.services.PropertyService;
import app.techy10souvik.captureeasy.common.ui.SystemNotifier;
import app.techy10souvik.captureeasy.common.util.ResourceUtil;
import app.techy10souvik.captureeasy.core.controller.GlobalController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import app.techy10souvik.captureeasy.common.ui.AlertPopup;
import app.techy10souvik.captureeasy.common.util.SystemUtil;
import app.techy10souvik.captureeasy.core.ui.ControlWindow;
import app.techy10souvik.captureeasy.core.ui.components.SplashScreenComponent;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose
 */
public class CaptureEasy implements App {
	{
		System.setProperty("log.home", SystemUtil.getLogFolder());
	}

	private final Logger log = LogManager.getLogger(this.getClass());
	///private String[] args;
	private SplashScreenComponent splash;

	/**
	 * @param args
	 * @throws
	 */
	public CaptureEasy(String[] args) throws Exception {
		log.info("** Starting application **");
		//this.args = args;
		this.splash=new SplashScreenComponent();
	}

	public void init() throws Exception {
		splash.start();
		splash.setMessage(ResourceUtil.getString("splash.init.configs")); // e.g., "Initializing default configurations..."
 		Thread.sleep(500);
		splash.setVersion(PropertyService.getInstance().getAppVersion());
	}

	public void launch() throws Exception {
		log.info("** Launching GUI **");
		splash.setMessage(ResourceUtil.getString("splash.init.ui")); // e.g., "Initializing user interface..."
		Thread.sleep(500);
		SwingUtilities.invokeLater(() -> {
			try {
				ControlWindow.init().show();
			} catch (Exception e) {
				log.error("** Failed to Launch GUI **", e);
				AlertPopup.init().type(AlertPopup.ERROR)
						.message(ResourceUtil.getString("error.launch.gui") + ": " + e.getMessage())
						.button2(ResourceUtil.getString("button.ok"));
			}
		});
		splash.setMessage(ResourceUtil.getString("splash.init.nativekey")); // e.g., "Initializing Native Key Listener"
		Thread.sleep(500);
		// GlobalScreen.addNativeKeyListener(new KeypressListener());
		// GlobalScreen.registerNativeHook();
		GlobalController.registerControllers();
		splash.setMessage(ResourceUtil.getString("splash.launching")); // e.g., "Launching Application"
		Thread.sleep(500);
		splash.stop();
		EventBus.publish(new AppEvent<>(EventType.APP_LOADED));
	}

	public void handleError() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
		 		splash.setMessage(e.getClass().getSimpleName()+" : "+e.getMessage());
				log.error("Exception Occurred!!",e);
				SystemNotifier.showSystemNotification("Error Occurred", "Something went wrong! Try restarting the application.", "ERROR");
			}
		});

	}

}
