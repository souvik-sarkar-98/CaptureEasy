package app.techy10souvik.captureeasy;

import java.nio.file.Paths;

import app.techy10souvik.captureeasy.common.services.PropertyService;
import app.techy10souvik.captureeasy.common.ui.AlertPopup;
import app.techy10souvik.captureeasy.common.ui.SystemNativeDialog;
import app.techy10souvik.captureeasy.common.util.PropertyUtil;
import app.techy10souvik.captureeasy.common.util.SystemUtil;
import app.techy10souvik.captureeasy.core.App;
import app.techy10souvik.captureeasy.core.CaptureEasy;
import io.github.sanyarnd.applocker.AppLocker;
import io.github.sanyarnd.applocker.exceptions.LockingBusyException;
import io.github.sanyarnd.applocker.exceptions.LockingCommunicationException;
import io.github.sanyarnd.applocker.exceptions.LockingFailedException;
import io.github.sanyarnd.applocker.exceptions.LockingMessageServerException;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose
 */
public class Boot {

	/**
	 * @purpose
	 * @date 03-Jun-2022
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		try {
			PropertyService propertyService = PropertyService.getInstance();
			AppLocker.create(propertyService.getAppLockKey()).setPath(Paths.get(SystemUtil.getRootFolder())).build()
					.lock();
			
			App app = new CaptureEasy(args);
			app.handleError();
			app.init();
			app.launch();
			
		} catch (LockingBusyException | LockingCommunicationException | LockingMessageServerException
				| LockingFailedException ex) {
			ex.printStackTrace();
	 		//AlertPopup.init().type(AlertPopup.WARNING).message("Sorry !! Cannot start Application. \nAn instance of this Application is already running.").button2("Close");
			SystemNativeDialog.showError(null, "Sorry !! Cannot start Application. \nAn instance of this Application is already running.", "Warning");
		}

	}

}
