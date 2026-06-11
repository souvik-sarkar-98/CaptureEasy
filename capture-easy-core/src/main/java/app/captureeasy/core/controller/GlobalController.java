package app.captureeasy.core.controller;

import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Bootstrap entry point for all controllers.
 * Constructors register EventBus subscriptions; no further wiring is needed.
 *
 * <p>Also registers the updater companion: when {@link EventType#APP_LOADED}
 * fires, a separate {@code CaptureEasy-Updater-*.jar} process is spawned if
 * one is found next to the running JAR. The updater process is completely
 * independent — this class has no imports from the installer module.</p>
 *
 * <p>When running from an IDE (no JAR, just compiled classes) the updater JAR
 * will not be present and the check is skipped silently. This is expected
 * behaviour during development.</p>
 */
public class GlobalController {

    private static final Logger log = LogManager.getLogger(GlobalController.class);

    /** Filename prefix used to locate the updater JAR next to the core JAR. */
    private static final String UPDATER_JAR_PREFIX = "CaptureEasy-Updater";

    public static void registerControllers() throws Exception {
        new ControlWindowController();
        new BackgroundController();
        new NotificationController();
        new SettingsController();
        registerUpdaterCompanion();
    }

    // ── Updater companion ─────────────────────────────────────────────────────

    /**
     * Subscribes to {@link EventType#APP_LOADED} and spawns
     * {@code CaptureEasy-Updater-*.jar} as a separate process.
     *
     * <p>The updater receives three arguments:</p>
     * <ul>
     *   <li>{@code --current-version} — this app's version (from version.properties)</li>
     *   <li>{@code --pid} — this JVM's process ID so the updater can terminate it</li>
     *   <li>{@code --jar-path} — absolute path to the running core JAR for in-place
     *       replacement on JAR-mode updates</li>
     * </ul>
     */
    private static void registerUpdaterCompanion() {
        EventBus.subscribe(EventType.APP_LOADED, event -> {
            try {
                Path updaterJar = findUpdaterJar();
                if (updaterJar == null) {
                    // Normal during IDE development — no updater JAR in the output directory.
                    log.info("Updater JAR not found next to core JAR — skipping update check. " +
                            "(Expected when running from IDE or if updater was not distributed.)");
                    return;
                }

                String version = readCurrentVersion();
                String jarPath = getCoreJarPath();
                long   pid     = ProcessHandle.current().pid();
                String javaExe = resolveJavaExecutable();

                if (!Path.of(javaExe).toFile().exists()) {
                    log.warn("Java executable not found at '{}' — cannot spawn updater.", javaExe);
                    return;
                }

                log.info("Spawning updater companion: {} (version={}, core-pid={})",
                        updaterJar.getFileName(), version, pid);
                log.debug("  java exe : {}", javaExe);
                log.debug("  core jar : {}", jarPath);

                // Pass log.home explicitly — System.setProperty() in Boot.java only
                // affects this JVM; child JVMs do not inherit Java system properties.
                String logHome = System.getProperty("log.home", "");

                Process child = new ProcessBuilder(
                        javaExe,
                        "-Dlog.home=" + logHome,
                        "-jar", updaterJar.toString(),
                        "--current-version=" + version,
                        "--pid=" + pid,
                        "--jar-path=" + (jarPath != null ? jarPath : ""))
                        // Discard child stdout/stderr — updater writes its own log file
                        // (updater-process-info.html). Not discarding would leave an unread
                        // pipe that blocks the child once its buffer (~4 KB) fills up.
                        .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                        .redirectError(ProcessBuilder.Redirect.DISCARD)
                        .start();

                long childPid = child.pid();
                log.info("Updater companion started — child pid: {}", childPid);

                // Watch the child on a daemon thread to detect early failures.
                Thread watcher = new Thread(() -> {
                    try {
                        boolean exited = child.waitFor(3, TimeUnit.SECONDS);
                        if (exited) {
                            int code = child.exitValue();
                            if (code == 0) {
                                log.debug("Updater (pid={}) exited normally with code 0 " +
                                        "(already up to date or user declined).", childPid);
                            } else {
                                log.warn("Updater (pid={}) exited with code {} within 3s — " +
                                        "check updater-process-info.html for details.", childPid, code);
                            }
                        } else {
                            log.debug("Updater (pid={}) still running after 3s — " +
                                    "consent dialog is open or download is in progress.", childPid);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, "updater-watcher");
                watcher.setDaemon(true);
                watcher.start();

            } catch (Exception e) {
                log.warn("Could not spawn updater companion — continuing without update check.", e);
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Looks for {@code CaptureEasy-Updater*.jar} in the same directory as the
     * running core JAR.
     *
     * <p>Returns {@code null} when:</p>
     * <ul>
     *   <li>The app is running from an IDE (classpath is a directory, not a JAR).</li>
     *   <li>The updater JAR was not distributed alongside the core JAR.</li>
     * </ul>
     */
    private static Path findUpdaterJar() throws URISyntaxException, IOException {
        Path codeSource = Path.of(GlobalController.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
                .toAbsolutePath();

        // Running from a directory (IDE) — no JAR to search next to
        if (Files.isDirectory(codeSource)) {
            log.debug("Code source is a directory ({}), not a JAR — IDE mode.", codeSource);
            return null;
        }

        // Running from a JAR — search sibling files for the updater JAR
        Path dir = codeSource.getParent();
        log.debug("Searching for updater JAR in: {}", dir);

        try (var entries = Files.list(dir)) {
            return entries
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        return name.startsWith(UPDATER_JAR_PREFIX) && name.endsWith(".jar");
                    })
                    .findFirst()
                    .orElse(null);
        }
    }

    /**
     * Returns the absolute path of the running core JAR, or {@code null} when
     * running from an IDE (code source is a directory).
     */
    private static String getCoreJarPath() throws URISyntaxException {
        Path codeSource = Path.of(GlobalController.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI())
                .toAbsolutePath();
        return Files.isDirectory(codeSource) ? null : codeSource.toString();
    }

    private static String readCurrentVersion() {
        try (InputStream in = GlobalController.class.getResourceAsStream("/version.properties")) {
            if (in == null) return "0.0.0";
            Properties p = new Properties();
            p.load(in);
            return p.getProperty("app.version", "0.0.0");
        } catch (IOException e) {
            return "0.0.0";
        }
    }

    /**
     * Resolves the Java executable from {@code java.home} — always the same JRE
     * that is running this process (bundled JRE for jpackage installs, system JRE
     * for fat-JAR users). Never relies on {@code PATH}.
     */
    private static String resolveJavaExecutable() {
        String os      = System.getProperty("os.name", "").toLowerCase();
        String exeName = os.contains("win") ? "javaw.exe" : "java";
        return Path.of(System.getProperty("java.home"), "bin", exeName).toString();
    }
}
