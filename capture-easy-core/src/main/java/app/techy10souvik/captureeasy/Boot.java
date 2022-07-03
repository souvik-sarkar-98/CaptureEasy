package app.techy10souvik.captureeasy;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;

import app.techy10souvik.captureeasy.common.util.SystemUtil;
import app.techy10souvik.captureeasy.core.App;
import app.techy10souvik.captureeasy.core.CaptureEasy;

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
			File file = Paths.get(SystemUtil.getRootFolder(), new String[] { "lock" }).toFile();
			RandomAccessFile randomAccessFile;
			FileChannel fileChannel;
			FileLock fileLock;
			if ((fileLock = (fileChannel = (randomAccessFile = new RandomAccessFile(file, "rw")).getChannel())
					.tryLock()) != null) {
				App app = new CaptureEasy(args);
				app.handleError();
				app.init();
				app.launch();
				
				

			} else {
				System.out.println("Failed to start - another instance is already running!");
				// logger.warn("Failed to start - another instance is already running!");
			}
			// logger.info("Releasing lock...");
			if (fileLock != null)
				fileLock.release();
			fileChannel.close();
			randomAccessFile.close();
		} catch (IOException iOException) {
			// logger.error("Failed to verify that this process is the only running
			// instance", iOException);
		}

	}

}
