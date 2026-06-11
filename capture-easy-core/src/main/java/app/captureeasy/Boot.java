package app.captureeasy;

import java.nio.file.Paths;

import app.captureeasy.common.services.PropertyService;
import app.captureeasy.common.ui.SystemNativeDialog;
import app.captureeasy.common.util.SystemUtil;
import app.captureeasy.core.App;
import app.captureeasy.core.CaptureEasy;
import io.github.sanyarnd.applocker.AppLocker;
import io.github.sanyarnd.applocker.exceptions.LockingBusyException;
import io.github.sanyarnd.applocker.exceptions.LockingCommunicationException;
import io.github.sanyarnd.applocker.exceptions.LockingFailedException;
import io.github.sanyarnd.applocker.exceptions.LockingMessageServerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose Application entry point.
 */
public class Boot {

	static {
		// Must run before LogManager.getLogger() so that Log4j2 can resolve
		// ${sys:log.home} in log4j2.xml when the logging context initialises.
		System.setProperty("log.home", SystemUtil.getLogFolder());
	}

	private static final Logger log = LogManager.getLogger(Boot.class);

	public static void main(String[] args) throws Exception {
		try {
			PropertyService propertyService = PropertyService.getInstance();
			AppLocker.create(propertyService.getAppLockKey())
					.setPath(Paths.get(SystemUtil.getRootFolder()))
					.build()
					.lock();

			App app = new CaptureEasy(args);
			app.handleError();
			app.init();
			app.launch();

		} catch (LockingBusyException | LockingCommunicationException | LockingMessageServerException
				| LockingFailedException ex) {
			log.error("Cannot start application — another instance is already running.", ex);
			SystemNativeDialog.showError(null,
					"Sorry !! Cannot start Application. \nAn instance of this Application is already running.",
					"Warning");
		}
	}
}
