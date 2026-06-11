package app.captureeasy.core;

import javax.swing.SwingUtilities;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.common.ui.SystemNotifier;
import app.captureeasy.common.util.ResourceUtil;
import app.captureeasy.core.controller.GlobalController;
import app.captureeasy.core.services.KeyboardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import app.captureeasy.common.ui.AlertPopup;
import app.captureeasy.core.ui.ControlWindow;
import app.captureeasy.core.ui.components.SplashScreenComponent;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose
 */
public class CaptureEasy implements App {

	private final Logger log = LogManager.getLogger(this.getClass());
	private SplashScreenComponent splash;

	public CaptureEasy(String[] args) throws Exception {
		log.info("** Starting application **");
		this.splash = new SplashScreenComponent();
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
				AlertPopup.init().type(AlertPopup.AlertType.ERROR)
						.message(ResourceUtil.getString("error.launch.gui") + ": " + e.getMessage())
						.button2(ResourceUtil.getString("button.ok"));
			}
		});
		splash.setMessage(ResourceUtil.getString("splash.init.nativekey")); // e.g., "Initializing Native Key Listener"
		Thread.sleep(500);
		registerNativeKeyHook();
		GlobalController.registerControllers();
		splash.setMessage(ResourceUtil.getString("splash.launching")); // e.g., "Launching Application"
		Thread.sleep(500);
		splash.stop();
		// APP_LOADED triggers the updater companion spawn registered in GlobalController
		EventBus.publish(new AppEvent<>(EventType.APP_LOADED));
	}

	/**
	 * Registers the global native keyboard hook so the PrintScreen hotkey works
	 * even when the application window does not have focus.
	 *
	 * <p>jnativehook logs every key event via java.util.logging by default.
	 * That logger is silenced here to avoid unintentional keystroke logging,
	 * which would be a privacy/security concern.</p>
	 */
	private void registerNativeKeyHook() {
		// Mute jnativehook's own JUL logger — it would otherwise emit a log line
		// for every key event on the system, which is both noisy and a privacy risk.
		java.util.logging.Logger jnhLogger =
				java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		jnhLogger.setLevel(java.util.logging.Level.OFF);
		jnhLogger.setUseParentHandlers(false);

		try {
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new KeyboardService());
			log.info("Native keyboard hook registered (hotkey: PrintScreen)");

			// Ensure the native hook is released when the JVM exits (including System.exit).
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException ignored) { }
			}, "jnativehook-shutdown"));

		} catch (NativeHookException e) {
			log.warn("Could not register native keyboard hook — hotkey will be unavailable", e);
		}
	}

	public void handleError() {
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
			splash.setMessage(e.getClass().getSimpleName() + " : " + e.getMessage());
			log.error("Exception Occurred!!", e);
			SystemNotifier.showSystemNotification("Error Occurred", "Something went wrong! Try restarting the application.", "ERROR");
		});
	}

}
