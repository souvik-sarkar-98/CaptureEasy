package app.captureeasy.installer;

import app.captureeasy.installer.GithubReleaseResponse.Asset;
import app.captureeasy.common.util.SystemUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.SystemUtils.*;

/**
 * Orchestrates the full auto-update flow.
 *
 * <p>Delegates all GitHub API concerns to {@link GithubClient}. This class
 * is responsible only for: version comparison, user consent, download,
 * process management, and update application (JAR swap or native installer).</p>
 *
 * <p>No class in {@code capture-easy-core} imports this class. Communication
 * with core is purely via the OS (process spawn + kill by PID).</p>
 */
public class AutoUpdater {

    private static final Logger log = LogManager.getLogger(AutoUpdater.class);

    private AutoUpdater() {}

    // ── Entry point ───────────────────────────────────────────────────────────

    /**
     * Runs the full update flow synchronously on the calling thread.
     * Swing dialogs are shown on the EDT and block this thread via a latch.
     *
     * @param currentVersion version string of the running app (e.g. "0.0.1")
     * @param corePid        PID of the core process to terminate before applying;
     *                       {@code -1} means no process to kill
     * @param currentJarPath absolute path of the running core JAR; used for
     *                       in-place replacement in JAR update mode
     */
    public static void run(String currentVersion, long corePid, String currentJarPath) {
        log.info("──────────────────────────────────────────────");

        try {
            GithubClient github = new GithubClient();

            // ── [1/6] Fetch latest release ────────────────────────────────────
            log.info("[1/6] Fetching latest release from GitHub…");
            GithubReleaseResponse release = github.fetchLatestRelease();
            if (release == null) {
                log.info("[1/6] No release data returned — nothing to do.");
                return;
            }
            String latestTag   = release.getTagName();
            String latestClean = stripV(latestTag);
            int    assetCount  = release.getAssets() != null ? release.getAssets().length : 0;
            log.info("[1/6] Latest release: {}  ({} asset(s))", latestTag, assetCount);

            // ── [2/6] Version comparison ──────────────────────────────────────
            log.info("[2/6] Comparing versions: current={} latest={}", currentVersion, latestClean);
            if (!isNewer(latestClean, currentVersion)) {
                log.info("[2/6] Already up to date — exiting updater.");
                return;
            }
            log.info("[2/6] Update available: {} → {}", currentVersion, latestTag);

            // ── [3/6] User consent ────────────────────────────────────────────
            log.info("[3/6] Prompting user for consent…");
            boolean consented = askConsent(currentVersion, latestTag);
            log.info("[3/6] User choice: {}", consented ? "UPDATE NOW" : "REMIND LATER");
            if (!consented) {
                log.info("[3/6] User declined — exiting updater.");
                return;
            }

            // ── [4/6] Asset selection ─────────────────────────────────────────
            log.info("[4/6] Selecting best asset for this OS…");
            Asset chosen = github.selectBestAsset(release.getAssets(), latestTag);
            log.info("[4/6] Selected: {}  ({} bytes, {})",
                    chosen.getName(), chosen.getSize(), chosen.getContentType());

            // ── [5/6] Download ────────────────────────────────────────────────
            Path downloadDir = SystemUtil.getDownloadPath(latestTag);
            Path localFile   = downloadDir.resolve(chosen.getName());
            log.info("[5/6] Downloading to: {}", localFile);
            log.debug("      From: {}", chosen.getBrowserDownloadUrl());

            showInfo("Downloading CaptureEasy " + latestTag + "…\nThis may take a moment.");
            long startMs = System.currentTimeMillis();
            downloadFile(chosen.getBrowserDownloadUrl(), localFile);
            long elapsedMs = System.currentTimeMillis() - startMs;
            log.info("[5/6] Download complete: {} bytes in {}ms",
                    Files.size(localFile), elapsedMs);

            // ── [6/6] Apply update ────────────────────────────────────────────
            log.info("[6/6] Applying update…");
            killProcess(corePid);

            if (chosen.getName().toLowerCase().endsWith(".jar")) {
                log.info("[6/6] Strategy: JAR in-place replacement + restart script");
                applyJarUpdate(localFile, currentJarPath);
            } else {
                log.info("[6/6] Strategy: native installer handoff ({})", chosen.getName());
                applyNativeInstaller(localFile);
            }

        } catch (Exception e) {
            log.error("Auto-update failed", e);
            showError("Update failed: " + e.getMessage()
                    + "\n\nPlease download the latest version manually from GitHub.");
        }
    }

    // ── Apply strategies ──────────────────────────────────────────────────────

    /**
     * JAR update: writes a platform restart script that waits for this JVM to exit,
     * replaces the old JAR with the downloaded one, then relaunches.
     */
    private static void applyJarUpdate(Path newJar, String currentJarPath) throws IOException {
        Path currentJar = (currentJarPath != null && !currentJarPath.isBlank())
                ? Path.of(currentJarPath) : newJar;

        if (currentJarPath == null || currentJarPath.isBlank()) {
            log.warn("No --jar-path provided — new JAR stays at {}", newJar);
        }

        String javaExe = Path.of(System.getProperty("java.home"), "bin",
                IS_OS_WINDOWS ? "javaw.exe" : "java").toString();

        log.debug("JAR update params — new: {}, current: {}, java: {}",
                newJar, currentJar, javaExe);

        Path script = writeRestartScript(newJar, currentJar, javaExe);
        log.info("Restart script: {}", script);
        log.info("Launching restart script and exiting…");

        if (IS_OS_WINDOWS) {
            new ProcessBuilder("cmd.exe", "/c", script.toString()).start();
        } else {
            new ProcessBuilder("/bin/sh", script.toString()).start();
        }
        System.exit(0);
    }

    /**
     * Native installer update: opens the downloaded file with the OS default
     * handler and exits so installation files are not locked.
     */
    private static void applyNativeInstaller(Path installerFile) throws IOException {
        log.info("Opening native installer: {}", installerFile);
        showInfo("The installer is opening.\nPlease complete the installation,"
                + " then relaunch CaptureEasy.");
        openWithOs(installerFile);
        log.info("Native installer handed off — exiting updater.");
        System.exit(0);
    }

    // ── Restart script ────────────────────────────────────────────────────────

    private static Path writeRestartScript(Path newJar, Path currentJar, String javaExe)
            throws IOException {
        Path tempDir = Files.createTempDirectory("captureeasy-update");
        log.debug("Restart script temp dir: {}", tempDir);

        if (IS_OS_WINDOWS) {
            Path bat = tempDir.resolve("restart.bat");
            String content = "@echo off\r\n"
                    + "timeout /t 3 /nobreak > nul\r\n"
                    + "move /Y \"" + newJar + "\" \"" + currentJar + "\"\r\n"
                    + "start \"\" \"" + javaExe + "\" -jar \"" + currentJar + "\"\r\n"
                    + "del \"%~f0\"\r\n";
            Files.writeString(bat, content);
            log.debug("Restart script (bat):\n{}", content);
            return bat;
        } else {
            Path sh = tempDir.resolve("restart.sh");
            String content = "#!/bin/sh\n"
                    + "sleep 3\n"
                    + "mv -f '" + newJar + "' '" + currentJar + "'\n"
                    + "'" + javaExe + "' -jar '" + currentJar + "' &\n"
                    + "rm -f \"$0\"\n";
            Files.writeString(sh, content);
            sh.toFile().setExecutable(true);
            log.debug("Restart script (sh):\n{}", content);
            return sh;
        }
    }

    // ── Process management ────────────────────────────────────────────────────

    private static void killProcess(long pid) {
        if (pid <= 0) {
            log.debug("No core PID — skipping process termination.");
            return;
        }
        log.info("Terminating core process (pid={})…", pid);
        Optional<ProcessHandle> handle = ProcessHandle.of(pid);
        if (handle.isPresent()) {
            ProcessHandle ph = handle.get();
            log.debug("Process info: {}", ph.info());
            ph.destroy();
            log.info("Destroy signal sent — waiting 1500ms for graceful exit…");
            try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
            if (ph.isAlive()) {
                log.warn("pid={} still alive — sending destroyForcibly()", pid);
                ph.destroyForcibly();
            } else {
                log.info("pid={} has exited.", pid);
            }
        } else {
            log.warn("pid={} not found — may have already exited.", pid);
        }
    }

    // ── Version comparison ────────────────────────────────────────────────────

    /**
     * Returns {@code true} when {@code candidate} is strictly newer than {@code base}.
     * Compares dot-separated integer segments; non-numeric suffixes are ignored.
     */
    static boolean isNewer(String candidate, String base) {
        int[] c = parseVersion(candidate);
        int[] b = parseVersion(base);
        int len = Math.max(c.length, b.length);
        for (int i = 0; i < len; i++) {
            int cv = i < c.length ? c[i] : 0;
            int bv = i < b.length ? b[i] : 0;
            if (cv != bv) return cv > bv;
        }
        return false;
    }

    private static int[] parseVersion(String version) {
        String[] parts = stripV(version).split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            try {
                result[i] = Integer.parseInt(parts[i].replaceAll("[^0-9].*", ""));
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }
        return result;
    }

    private static String stripV(String v) {
        if (v == null)         return "0";
        if (v.startsWith("v")) return v.substring(1);
        return v;
    }

    // ── Swing helpers ─────────────────────────────────────────────────────────

    /**
     * Blocks the calling thread until the user dismisses the consent dialog on
     * the EDT.
     *
     * @return {@code true} if the user chose "Update Now"
     */
    private static boolean askConsent(String current, String latest) {
        log.debug("Showing consent dialog (current={}, latest={})", current, latest);
        CountDownLatch latch   = new CountDownLatch(1);
        AtomicBoolean  consent = new AtomicBoolean(false);

        SwingUtilities.invokeLater(() -> {
            try {
                String message = "<html>"
                        + "<b>CaptureEasy " + latest + " is available!</b><br><br>"
                        + "You are running version <b>" + current + "</b>.<br>"
                        + "The update will be downloaded and applied automatically.<br>"
                        + "CaptureEasy will restart when done."
                        + "</html>";

                Object[] options = {"Update Now", "Remind Later"};
                int choice = JOptionPane.showOptionDialog(
                        null, message, "Update Available",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                boolean agreed = choice == 0;
                log.info("User chose: {}",
                        agreed ? "Update Now"
                               : choice == JOptionPane.CLOSED_OPTION ? "closed" : "Remind Later");
                consent.set(agreed);
            } finally {
                latch.countDown();
            }
        });

        try { latch.await(); } catch (InterruptedException ignored) {}
        return consent.get();
    }

    private static void showInfo(String message) {
        log.debug("Info dialog: {}", message.replace("\n", " "));
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, message,
                        "CaptureEasy Update", JOptionPane.INFORMATION_MESSAGE));
    }

    private static void showError(String message) {
        log.error("Error dialog: {}", message.replace("\n", " "));
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(null, message,
                        "Update Failed", JOptionPane.ERROR_MESSAGE));
    }

    // ── I/O helpers ───────────────────────────────────────────────────────────

    private static void downloadFile(String url, Path destination) throws IOException {
        log.debug("Downloading: {}", url);
        try (InputStream in = URI.create(url).toURL().openStream()) {
            long bytes = Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Written {} bytes → {}", bytes, destination);
        }
    }

    private static void openWithOs(Path file) throws IOException {
        if (IS_OS_WINDOWS) {
            log.debug("openWithOs: cmd /c start {}", file);
            new ProcessBuilder("cmd.exe", "/c", "start", "", file.toString()).start();
        } else if (IS_OS_MAC) {
            log.debug("openWithOs: open {}", file);
            new ProcessBuilder("open", file.toString()).start();
        } else {
            log.debug("openWithOs: xdg-open {}", file);
            new ProcessBuilder("xdg-open", file.toString()).start();
        }
    }
}
