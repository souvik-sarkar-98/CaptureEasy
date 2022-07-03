package app.techy10souvik.captureeasy.installer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipFile;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.techy10souvik.captureeasy.common.dto.GithubReleaseResponse;
import app.techy10souvik.captureeasy.common.dto.GithubReleaseResponse.Asset;
import app.techy10souvik.captureeasy.common.util.GithubUtil;
import app.techy10souvik.captureeasy.common.util.SystemUtil;

/**
 * @author Souvik Sarkar
 * @createdOn 07-Jun-2022
 * @purpose 
 */
public class CaptureEasyInstaller {
	
	
	public void install() throws ClientProtocolException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(GithubUtil.getGithubLatestReleaseUrl());
		GithubReleaseResponse response = client.execute(request,
				httpResponse -> mapper.readValue(httpResponse.getEntity().getContent(), GithubReleaseResponse.class));
		
		for (Asset asset:response.getAssets()) {
			Path downloadFolderPath=SystemUtil.getDownloadPath(response.getTag_name());
			Path filePath=Paths.get(downloadFolderPath.toString(),asset.getName());
			
			downloadFile(asset.getBrowser_download_url(),filePath.toString());
			if(asset.getName().endsWith(".jar") && asset.getName().toLowerCase().contains(response.getTag_name())) {
				SystemUtil.createDesktopShortcut(filePath.toString(), "CaptureEasy");
			}
			else if(asset.getName().endsWith(".zip") && asset.getName().toLowerCase().contains(response.getTag_name())) {
//				 new ZipFile(filePath.toFile()).
//	                .extractAll(downloadFolderPath.toString());
				SystemUtil.unzip(filePath.toString(), downloadFolderPath.toString());
				SystemUtil.createDesktopShortcut("C:/Users/Souvik Sarkar/AppData/Local/CaptureEasy/app-V_0.8.3/assets/CaptureEasy-V_0.8.3/CaptureEasy-V_0.8.3.jar", "CaptureEasy");
			}
		}
	}

	
	private String downloadFile(String url,String filePath) throws IOException {
		Files.copy(new URL(url).openStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		return filePath;
	}
}
