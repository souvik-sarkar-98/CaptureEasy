package app.captureeasy.common.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.OptionalLong;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author Souvik Sarkar
 * @createdOn 03-Jun-2022
 * @purpose OS-level utilities: paths, filesystem helpers, screen info.
 */
public class SystemUtil extends SystemUtils {

    public static String getRootFolder() {
        Path rootFolder;
        if (IS_OS_WINDOWS) {
            rootFolder = Paths.get(System.getenv("LOCALAPPDATA"), "CaptureEasy");
        } else if (IS_OS_MAC) {
            rootFolder = Paths.get(System.getProperty("user.home"), "Library", "Application Support", "CaptureEasy");
        } else {
            // Linux / other Unix
            rootFolder = Paths.get(System.getProperty("user.home"), ".captureeasy");
        }

        if (!rootFolder.toFile().exists()) {
            createFolder(rootFolder.toString());
        }
        return rootFolder.toString();
    }

    public static String getPropFile() throws IOException {
        File propfile = new File(getRootFolder(), "app.properties");
        if (!propfile.exists()) {
            propfile.createNewFile();
        }
        return propfile.getAbsolutePath();
    }

    public static String createFolder(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    public static Path getTempPath() throws IOException {
        Path path = Paths.get(getRootFolder(), "temp", String.valueOf(System.currentTimeMillis()));
        if (!path.toFile().exists()) {
            createFolder(path.toString());
        }
        return path;
    }

    public static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public static Path getDownloadPath(String version) throws IOException {
        Path path = Paths.get(getRootFolder(), "app-" + version, "assets");
        if (!path.toFile().exists()) {
            createFolder(path.toString());
        }
        return path;
    }

    /**
     * Extracts a ZIP archive to {@code destDir}.
     *
     * <p>Zip-slip protection: every entry is resolved against the canonical
     * destination directory. Any entry that escapes the destination throws
     * {@link SecurityException}.</p>
     *
     * @param zipFilePath path to the ZIP file
     * @param destDir     target extraction directory (created if absent)
     * @throws IOException       on I/O failure
     * @throws SecurityException if a zip-slip entry is detected
     */
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();

        // Canonical destination — used to prevent zip-slip
        Path canonicalDest = dir.toPath().toRealPath();

        byte[] buffer = new byte[8192];
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                Path resolved = canonicalDest.resolve(ze.getName()).normalize();
                if (!resolved.startsWith(canonicalDest)) {
                    throw new SecurityException("Zip-slip attempt blocked: " + ze.getName());
                }

                File target = resolved.toFile();
                if (ze.isDirectory()) {
                    target.mkdirs();
                } else {
                    target.getParentFile().mkdirs();
                    try (OutputStream fos = new FileOutputStream(target)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

    public static String getLogFolder() {
        Path logFolder = Paths.get(getRootFolder(), "logs");
        if (!logFolder.toFile().exists()) {
            createFolder(logFolder.toString());
        }
        return logFolder.toString();
    }

    public static String getDocumentFolder() {
        Path docFolder = Paths.get(
                FileSystemView.getFileSystemView().getDefaultDirectory().getAbsolutePath(),
                "Screenshots");
        return docFolder.toString();
    }

    /**
     * Creates a desktop shortcut that launches the given JAR.
     *
     * <ul>
     *   <li>Windows: uses PowerShell / WScript.Shell COM object to write a .lnk file.</li>
     *   <li>macOS: writes an executable shell-script launcher on the Desktop.</li>
     *   <li>Linux: writes a standards-compliant .desktop entry on the Desktop.</li>
     * </ul>
     *
     * Failures are logged but never propagate to callers; a missing shortcut is
     * not a fatal error during installation.
     */
    public static void createDesktopShortcut(String targetJarPath, String shortcutName) {
        File desktop = FileSystemView.getFileSystemView().getHomeDirectory();
        try {
            if (IS_OS_WINDOWS) {
                createWindowsShortcut(desktop, targetJarPath, shortcutName);
            } else if (IS_OS_MAC) {
                createMacLauncher(desktop, targetJarPath, shortcutName);
            } else {
                createLinuxDesktopEntry(desktop, targetJarPath, shortcutName);
            }
        } catch (Exception e) {
            // Non-fatal — the app still works without a shortcut
            System.err.println("[WARN] Could not create desktop shortcut: " + e.getMessage());
        }
    }

    // ── Shortcut helpers ──────────────────────────────────────────────────────

    private static void createWindowsShortcut(File desktop, String targetJar, String name)
            throws IOException, InterruptedException {
        String lnkPath = Paths.get(desktop.getAbsolutePath(), name + ".lnk").toString();
        // PowerShell one-liner: create a WScript.Shell shortcut pointing to the JAR via javaw
        String javaExe  = Paths.get(System.getProperty("java.home"), "bin", "javaw.exe").toString();
        String psScript = String.format(
                "$s = (New-Object -COM WScript.Shell).CreateShortcut('%s');" +
                "$s.TargetPath = '%s';" +
                "$s.Arguments = '-jar \"%s\"';" +
                "$s.WorkingDirectory = '%s';" +
                "$s.Save()",
                lnkPath.replace("'", "''"),
                javaExe.replace("'", "''"),
                targetJar.replace("'", "''"),
                new File(targetJar).getParent().replace("'", "''"));
        new ProcessBuilder("powershell.exe", "-NonInteractive", "-Command", psScript)
                .inheritIO()
                .start()
                .waitFor();
    }

    private static void createMacLauncher(File desktop, String targetJar, String name)
            throws IOException {
        File script = new File(desktop, name + ".command");
        String content = "#!/bin/bash\n"
                + "java -jar \"" + targetJar + "\"\n";
        Files.writeString(script.toPath(), content);
        script.setExecutable(true);
    }

    private static void createLinuxDesktopEntry(File desktop, String targetJar, String name)
            throws IOException {
        File entry = new File(desktop, name + ".desktop");
        String content = "[Desktop Entry]\n"
                + "Version=1.0\n"
                + "Type=Application\n"
                + "Name=" + name + "\n"
                + "Exec=java -jar \"" + targetJar + "\"\n"
                + "Terminal=false\n"
                + "Categories=Utility;\n";
        Files.writeString(entry.toPath(), content);
        entry.setExecutable(true);
    }

    /**
     * Counts the regular files in a directory.
     *
     * @param directoryPath the path to the directory
     * @return an {@link OptionalLong} containing the file count, or empty if the path is
     *         not a valid directory or an I/O error occurs
     */
    public static OptionalLong countFilesInDirectory(String directoryPath) {
        Path dir = Paths.get(directoryPath);
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            return OptionalLong.empty();
        }
        try (Stream<Path> entries = Files.list(dir)) {
            return OptionalLong.of(entries.filter(Files::isRegularFile).count());
        } catch (IOException e) {
            return OptionalLong.empty();
        }
    }
}
