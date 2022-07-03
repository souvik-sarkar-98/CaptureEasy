package app.techy10souvik.captureeasy.core;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;


import app.techy10souvik.captureeasy.common.ui.AlertPopup;
import app.techy10souvik.captureeasy.core.services.KeypressListener;
import app.techy10souvik.captureeasy.core.ui.ControlWindow;
import app.techy10souvik.captureeasy.core.ui.components.SplashScreenComponent;
import app.techy10souvik.captureeasy.core.util.PropertyUtil;
import app.techy10souvik.captureeasy.core.util.SystemUtil;

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
	 * @throws ConfigurationException
	 */
	public CaptureEasy(String[] args) throws ConfigurationException {
		log.info("** Starting application **");
		//this.args = args;
		this.splash=new SplashScreenComponent();
	}

	public void init() throws ConfigurationException, IOException {
		splash.start();
		splash.setMessage("Initializing default properties...");
		PropertyUtil.init();
		splash.setVersion(PropertyUtil.getAppVersion());
	}

	public void launch() throws NativeHookException, IOException {
		log.info("** Launching GUI **");
		splash.setMessage("Initializing user interface...");
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ControlWindow.init().registerActions().show();
			}
		});
 		splash.setMessage("Initializing Native Key Listener");
        //GlobalScreen.addNativeKeyListener(new KeypressListener());
       // GlobalScreen.registerNativeHook();
 		splash.setMessage("Launching Application");
		splash.stop();
	}

	public void handleError() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
		 		splash.setMessage(e.getClass().getSimpleName()+" : "+e.getMessage());
		 		AlertPopup.init().type(AlertPopup.WARNING).message(e.getClass().getSimpleName()+" : "+e.getMessage()).button2("Okay");
				e.printStackTrace();
			}
		});
		
	}

}
