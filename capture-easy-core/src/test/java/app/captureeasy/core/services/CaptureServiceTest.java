package app.captureeasy.core.services;

import app.captureeasy.common.services.PropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CaptureService.
 *
 * Note: captureScreenshot() requires a real display (Robot) so it is covered only in
 * integration/manual testing. These tests focus on the file-management methods.
 */
@ExtendWith(MockitoExtension.class)
class CaptureServiceTest {

    @TempDir
    Path tempDir;

    private PropertyService mockProperties;

    @BeforeEach
    void setUp() {
        mockProperties = mock(PropertyService.class);
    }

    @Test
    void getScreenshotCount_emptyFolder_returnsZero() throws Exception {
        when(mockProperties.getTempFolder()).thenReturn(tempDir.toString());

        CaptureService service = new CaptureService(mockProperties);

        assertEquals(0L, service.getScreenshotCount());
    }

    @Test
    void getScreenshotCount_withFiles_returnsCorrectCount() throws Exception {
        Files.createFile(tempDir.resolve("shot1.png"));
        Files.createFile(tempDir.resolve("shot2.png"));
        when(mockProperties.getTempFolder()).thenReturn(tempDir.toString());

        CaptureService service = new CaptureService(mockProperties);

        assertEquals(2L, service.getScreenshotCount());
    }

    @Test
    void deleteScreenshots_removesAllFiles_andRegeneratesFolder() throws Exception {
        Files.createFile(tempDir.resolve("shot1.png"));
        Files.createFile(tempDir.resolve("shot2.png"));
        Path newTemp = tempDir.getParent().resolve("new_session_"+System.currentTimeMillis());
        Files.createDirectories(newTemp);

        when(mockProperties.getTempFolder()).thenReturn(tempDir.toString());
        when(mockProperties.generateTempFolder()).thenReturn(newTemp.toString());

        CaptureService service = new CaptureService(mockProperties);
        service.deleteScreenshots();

        // Original folder deleted; generateTempFolder called once
        verify(mockProperties).generateTempFolder();
        assertFalse(Files.exists(tempDir,LinkOption.NOFOLLOW_LINKS), "Original temp folder should be deleted");
    }

    @Test
    void getScreenshotCount_nonExistentFolder_returnsZero() throws Exception {
        when(mockProperties.getTempFolder()).thenReturn(tempDir.resolve("missing").toString());

        CaptureService service = new CaptureService(mockProperties);

        assertEquals(0L, service.getScreenshotCount());
    }
}
