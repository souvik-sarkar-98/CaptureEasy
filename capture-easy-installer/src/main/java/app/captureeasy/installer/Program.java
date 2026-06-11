package app.captureeasy.installer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point for the updater companion process.
 *
 * <p>Invoked by the core application after {@code APP_LOADED} fires, via:</p>
 * <pre>
 *   java -jar CaptureEasy.jar \
 *     --mode=updater \
 *     --current-version=0.0.1 \
 *     --pid=12345 \
 *     --jar-path=/absolute/path/to/CaptureEasy.jar
 * </pre>
 *
 * <p>Also serves as the standalone first-install entry point when
 * {@code --current-version} is absent (treats current version as "0.0.0"
 * so any published release will be offered).</p>
 */
public class Program {

    private static final Logger log = LogManager.getLogger(Program.class);

    public static void main(String[] args) {
        log.info("═══════════════════════════════════════════════");
        log.info("  CaptureEasy Updater — process starting");
        log.info("═══════════════════════════════════════════════");

        // Log raw args at DEBUG so troubleshooting is possible without changing log level
        log.debug("Raw args received: {}", (Object) args);

        String currentVersion = parseArg(args, "--current-version", "0.0.0");
        String pidStr         = parseArg(args, "--pid",             "-1");
        String jarPath        = parseArg(args, "--jar-path",        "");

        long pid;
        try {
            pid = Long.parseLong(pidStr);
        } catch (NumberFormatException e) {
            log.warn("Invalid --pid value '{}' — defaulting to -1 (no process will be killed)", pidStr);
            pid = -1;
        }

        log.info("Parsed args:");
        log.info("  current-version : {}", currentVersion);
        log.info("  core pid        : {}", pid > 0 ? pid : "n/a");
        log.info("  jar-path        : {}", jarPath.isBlank() ? "(not provided)" : jarPath);

        log.info("Handing off to AutoUpdater…");
        AutoUpdater.run(currentVersion, pid, jarPath);

        log.info("Updater process finished.");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * Parses a {@code --key=value} argument from the args array.
     *
     * @param args         command-line arguments
     * @param key          argument name including leading {@code --}
     * @param defaultValue value to return when the key is absent
     * @return the value part, or {@code defaultValue}
     */
    static String parseArg(String[] args, String key, String defaultValue) {
        String prefix = key + "=";
        for (String arg : args) {
            if (arg.startsWith(prefix)) {
                String value = arg.substring(prefix.length());
                log.debug("Arg parsed: {} = '{}'", key, value);
                return value;
            }
        }
        log.debug("Arg '{}' not found — using default: '{}'", key, defaultValue);
        return defaultValue;
    }
}
