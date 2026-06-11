package app.captureeasy.installer;

import app.captureeasy.installer.GithubReleaseResponse.Asset;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.commons.lang3.SystemUtils.*;

/**
 * Thin client for the GitHub Releases API.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Load repository coordinates from {@code github.properties}.</li>
 *   <li>Fetch the latest published release via the GitHub REST API.</li>
 *   <li>Select the best release asset for the current OS and install mode.</li>
 * </ul>
 *
 * <p>All other update concerns (version comparison, downloading, applying,
 * UI dialogs) live in {@link AutoUpdater}.</p>
 */
public class GithubClient {

    private static final Logger log = LogManager.getLogger(GithubClient.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final String GITHUB_API_BASE = "https://api.github.com";

    private final String latestReleaseUrl;

    // ── Construction ──────────────────────────────────────────────────────────

    /**
     * Creates a client configured from {@code github.properties} on the classpath.
     *
     * @throws IllegalStateException if the properties file is missing or
     *                               {@code github.owner} / {@code github.repo} are absent
     */
    public GithubClient() {
        this.latestReleaseUrl = buildLatestReleaseUrl();
        log.debug("GithubClient initialised — releases URL: {}", latestReleaseUrl);
    }

    private static String buildLatestReleaseUrl() {
        try (InputStream in = GithubClient.class.getResourceAsStream("/github.properties")) {
            if (in == null) {
                throw new IllegalStateException("github.properties not found on classpath");
            }
            Properties p = new Properties();
            p.load(in);

            String owner = p.getProperty("github.owner");
            String repo  = p.getProperty("github.repo");

            if (owner == null || owner.isBlank()) {
                throw new IllegalStateException("github.owner is missing in github.properties");
            }
            if (repo == null || repo.isBlank()) {
                throw new IllegalStateException("github.repo is missing in github.properties");
            }
            return GITHUB_API_BASE + "/repos/" + owner + "/" + repo + "/releases/latest";
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read github.properties", e);
        }
    }

    // ── API calls ─────────────────────────────────────────────────────────────

    /**
     * Fetches the latest published (non-draft, non-prerelease) GitHub release.
     *
     * @return the parsed release, or {@code null} if no releases exist yet (HTTP 404)
     * @throws IOException if the network request fails or the API returns an
     *                     unexpected status code
     */
    public GithubReleaseResponse fetchLatestRelease() throws IOException {
        try (CloseableHttpClient http = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(latestReleaseUrl);
            request.setHeader("Accept", "application/vnd.github+json");

            log.debug("GET {}", latestReleaseUrl);
            try (CloseableHttpResponse response = http.execute(request)) {
                int status = response.getStatusLine().getStatusCode();
                log.debug("GitHub API → HTTP {}", status);

                if (status == 404) {
                    log.info("GitHub returned 404 — no releases published yet for this repository.");
                    return null;
                }
                if (status != 200) {
                    throw new IOException(
                            "GitHub API returned unexpected status HTTP " + status
                                    + " for URL: " + latestReleaseUrl);
                }

                try (InputStream body = response.getEntity().getContent()) {
                    GithubReleaseResponse release = MAPPER.readValue(body, GithubReleaseResponse.class);
                    log.debug("Parsed release — id={} tag={} draft={} prerelease={} assets={}",
                            release.getId(), release.getTagName(),
                            release.isDraft(), release.isPrerelease(),
                            release.getAssets() != null ? release.getAssets().length : 0);
                    return release;
                }
            }
        }
    }

    // ── Asset selection ───────────────────────────────────────────────────────

    /**
     * Selects the most appropriate release asset for the current OS and
     * installation mode.
     *
     * <p>Selection priority:</p>
     * <ol>
     *   <li>Native installer ({@code .msi} / {@code .dmg} / {@code .deb}) — only
     *       preferred when running as a jpackage install
     *       ({@code jpackage.app-version} system property is set).</li>
     *   <li>Fat JAR ({@code .jar}).</li>
     *   <li>Native installer as fallback (even for non-jpackage installs).</li>
     *   <li>ZIP archive.</li>
     * </ol>
     *
     * @param assets array of assets from {@link GithubReleaseResponse}
     * @param tag    release tag — used only in error messages
     * @return the chosen asset, never {@code null}
     * @throws IOException if no suitable asset is found
     */
    public Asset selectBestAsset(Asset[] assets, String tag) throws IOException {
        if (assets == null || assets.length == 0) {
            throw new IOException("Release " + tag + " has no downloadable assets.");
        }

        String  nativeExt    = nativeInstallerExtension();
        boolean preferNative = System.getProperty("jpackage.app-version") != null;

        log.debug("Asset selection — OS: {}, nativeExt: {}, preferNative(jpackage): {}",
                System.getProperty("os.name"), nativeExt, preferNative);

        Asset nativeAsset = null;
        Asset jar         = null;
        Asset zip         = null;

        for (Asset a : assets) {
            if (a.getName() == null) continue;
            String lower = a.getName().toLowerCase();
            log.debug("  Candidate: {} ({} bytes, {})", a.getName(), a.getSize(), a.getContentType());
            if (nativeExt != null && lower.endsWith(nativeExt)) nativeAsset = a;
            else if (lower.endsWith(".jar"))                      jar = a;
            else if (lower.endsWith(".zip"))                      zip = a;
        }

        log.debug("Candidates — native={}, jar={}, zip={}",
                name(nativeAsset), name(jar), name(zip));

        if (preferNative && nativeAsset != null) { log.debug("→ native installer (jpackage mode)"); return nativeAsset; }
        if (jar         != null)                  { log.debug("→ fat JAR");                          return jar;         }
        if (nativeAsset != null)                  { log.debug("→ native installer (fallback)");      return nativeAsset; }
        if (zip         != null)                  { log.debug("→ ZIP (last resort)");                return zip;         }

        throw new IOException("No suitable asset (.jar / " + nativeExt + " / .zip) "
                + "found in release " + tag + ".");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Returns the preferred native installer file extension for the current OS,
     * or {@code null} on unsupported platforms.
     */
    private static String nativeInstallerExtension() {
        if (IS_OS_WINDOWS) return ".msi";
        if (IS_OS_MAC)     return ".dmg";
        if (IS_OS_LINUX)   return ".deb";
        return null;
    }

    private static String name(Asset a) {
        return a != null ? a.getName() : "none";
    }
}
