import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

class Main {

	public static void downloadFile1(URL url, String fileName) throws IOException {
		try (InputStream in = url.openStream();
				BufferedInputStream bis = new BufferedInputStream(in);
				FileOutputStream fos = new FileOutputStream(fileName)) {

			byte data[] = new byte[1024];
			int count;
			while ((count = bis.read(data, 0, 1024)) != -1) {
				fos.write(data, 0, count);
				System.out.println("data");
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// call to downloadFile() method
		downloadFile(new URL("https://github.com/souvik-sarkar-98/CaptureEasy/releases/download/V_0.4/CaptureEasy-V_0.4.jar")," bc.jar");
	}
	public static void downloadFile(URL url, String fileName) throws IOException {
		FileUtils.copyURLToFile(url, new File(fileName));
	}
}