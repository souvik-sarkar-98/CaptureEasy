package app.techy10souvik.captureeasy.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubReleaseResponse {
	
	private long id;
	private String tag_name;
	private boolean draft;
	private boolean prerelease;
	private String body;
	private Asset[] assets;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Asset{
		private long id;
		private String name;
		private String label;
		private String content_type;
		private String state;
		private long size;
		private int download_count;
		private String browser_download_url;
		public long getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public String getLabel() {
			return label;
		}
		public String getContent_type() {
			return content_type;
		}
		public String getState() {
			return state;
		}
		public long getSize() {
			return size;
		}
		public int getDownload_count() {
			return download_count;
		}
		public String getBrowser_download_url() {
			return browser_download_url;
		}
		public void setId(long id) {
			this.id = id;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public void setContent_type(String content_type) {
			this.content_type = content_type;
		}
		public void setState(String state) {
			this.state = state;
		}
		public void setSize(long size) {
			this.size = size;
		}
		public void setDownload_count(int download_count) {
			this.download_count = download_count;
		}
		public void setBrowser_download_url(String browser_download_url) {
			this.browser_download_url = browser_download_url;
		}
		
		
	}

	public long getId() {
		return id;
	}

	public String getTag_name() {
		return tag_name;
	}

	public boolean isDraft() {
		return draft;
	}

	public boolean isPrerelease() {
		return prerelease;
	}

	public String getBody() {
		return body;
	}

	public Asset[] getAssets() {
		return assets;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public void setPrerelease(boolean prerelease) {
		this.prerelease = prerelease;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setAssets(Asset[] assets) {
		this.assets = assets;
	}

}
