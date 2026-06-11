package app.captureeasy.installer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents the JSON response from the GitHub Releases API
 * ({@code GET /repos/:owner/:repo/releases/latest}).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubReleaseResponse {

    private long id;

    @JsonProperty("tag_name")
    private String tagName;

    @JsonProperty("html_url")
    private String htmlUrl;

    private boolean draft;
    private boolean prerelease;
    private String body;
    private Asset[] assets;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Asset {
        private long id;
        private String name;
        private String label;

        @JsonProperty("content_type")
        private String contentType;

        private String state;
        private long size;

        @JsonProperty("download_count")
        private int downloadCount;

        @JsonProperty("browser_download_url")
        private String browserDownloadUrl;
    }
}
