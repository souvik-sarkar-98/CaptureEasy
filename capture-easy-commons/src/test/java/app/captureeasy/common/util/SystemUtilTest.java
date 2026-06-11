package app.captureeasy.common.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.*;

class SystemUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void countFilesInDirectory_emptyDir_returnsZero() {
        OptionalLong result = SystemUtil.countFilesInDirectory(tempDir.toString());
        assertTrue(result.isPresent());
        assertEquals(0L, result.getAsLong());
    }

    @Test
    void countFilesInDirectory_withFiles_returnsCorrectCount() throws IOException {
        Files.createFile(tempDir.resolve("a.png"));
        Files.createFile(tempDir.resolve("b.png"));
        Files.createFile(tempDir.resolve("c.png"));

        OptionalLong result = SystemUtil.countFilesInDirectory(tempDir.toString());

        assertTrue(result.isPresent());
        assertEquals(3L, result.getAsLong());
    }

    @Test
    void countFilesInDirectory_subdirectoriesNotCounted() throws IOException {
        Files.createFile(tempDir.resolve("file.png"));
        Files.createDirectory(tempDir.resolve("subdir"));

        OptionalLong result = SystemUtil.countFilesInDirectory(tempDir.toString());

        assertTrue(result.isPresent());
        assertEquals(1L, result.getAsLong(), "Sub-directories must not be counted as files");
    }

    @Test
    void countFilesInDirectory_invalidPath_returnsEmpty() {
        OptionalLong result = SystemUtil.countFilesInDirectory("/does/not/exist/anywhere");
        assertTrue(result.isEmpty());
    }

    @Test
    void countFilesInDirectory_filePathInsteadOfDir_returnsEmpty() throws IOException {
        Path file = Files.createFile(tempDir.resolve("notadir.txt"));
        OptionalLong result = SystemUtil.countFilesInDirectory(file.toString());
        assertTrue(result.isEmpty());
    }

    @Test
    void createFolder_createsDirectoryIfNotExists(@TempDir Path base) {
        String newPath = base.resolve("newSubDir").toString();
        String created = SystemUtil.createFolder(newPath);
        assertTrue(Files.isDirectory(Path.of(created)));
    }

    @Test
    void unzip_extractsFilesCorrectly(@TempDir Path zipDir, @TempDir Path destDir) throws IOException {
        // Create a simple zip with one entry using standard Java
        Path zipFile = zipDir.resolve("test.zip");
        try (var zos = new java.util.zip.ZipOutputStream(Files.newOutputStream(zipFile))) {
            zos.putNextEntry(new java.util.zip.ZipEntry("hello.txt"));
            zos.write("hello world".getBytes());
            zos.closeEntry();
        }

        SystemUtil.unzip(zipFile.toString(), destDir.toString());

        Path extracted = destDir.resolve("hello.txt");
        assertTrue(Files.exists(extracted));
        assertEquals("hello world", Files.readString(extracted));
    }
}
