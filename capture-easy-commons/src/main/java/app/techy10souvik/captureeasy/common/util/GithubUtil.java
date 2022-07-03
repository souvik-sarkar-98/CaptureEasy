package app.techy10souvik.captureeasy.common.util;

/**
 * @author Souvik Sarkar
 * @createdOn 07-Jun-2022
 * @purpose 
 */
public class GithubUtil {

	private static final String GITHUB_LATEST_RELEASE_URL = "https://api.github.com/repos/souvik-sarkar-98/CaptureEasy/releases/latest";

	public static String getGithubLatestReleaseUrl() {
		return GITHUB_LATEST_RELEASE_URL;
	}


}
