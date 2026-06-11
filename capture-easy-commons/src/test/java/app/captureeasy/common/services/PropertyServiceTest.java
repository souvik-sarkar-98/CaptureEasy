package app.captureeasy.common.services;

import app.captureeasy.common.util.SystemUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

/**
 * Integration-style tests for PropertyService using a real temporary filesystem.
 *
 * The singleton path is bypassed via the package-private
 * {@code PropertyService(String propFilePath)} constructor so tests remain
 * hermetic and do not touch LOCALAPPDATA.
 */
@ExtendWith(MockitoExtension.class)
class PropertyServiceTest {

    @TempDir
    Path tempDir;

    private Path propFile;
    private PropertyService service;

    @BeforeEach
    void setUp() throws IOException {
        propFile = tempDir.resolve("app.properties");
        // File does not exist yet — service starts with empty in-memory state.
        service = new PropertyService(propFile.toString());
    }

    // ── getString ─────────────────────────────────────────────────────────────

    @Test
    void getString_missingKey_returnsNull() {
        assertNull(service.getString("MISSING_KEY"));
    }

    @Test
    void getString_afterSet_returnsValue() {
        service.set("MY_KEY", "hello");
        assertEquals("hello", service.getString("MY_KEY"));
    }

    @Test
    void getString_withDefault_returnsFallbackWhenAbsent() {
        assertEquals("default_val", service.getString("ABSENT", "default_val"));
    }

    @Test
    void getString_withDefault_returnsStoredValueWhenPresent() {
        service.set("K", "stored");
        assertEquals("stored", service.getString("K", "ignored_default"));
    }

    // ── getInt / getBoolean ───────────────────────────────────────────────────

    @Test
    void getInt_validNumericValue_parsesCorrectly() {
        service.set("COUNT", "42");
        assertEquals(42, service.getInt("COUNT", 0));
    }

    @Test
    void getInt_nonNumericValue_returnsDefault() {
        service.set("COUNT", "not-a-number");
        assertEquals(99, service.getInt("COUNT", 99));
    }

    @Test
    void getInt_missingKey_returnsDefault() {
        assertEquals(-1, service.getInt("MISSING", -1));
    }

    @Test
    void getBoolean_trueValue_returnsTrue() {
        service.set("FLAG", "true");
        assertTrue(service.getBoolean("FLAG", false));
    }

    @Test
    void getBoolean_falseValue_returnsFalse() {
        service.set("FLAG", "false");
        assertFalse(service.getBoolean("FLAG", true));
    }

    @Test
    void getBoolean_missingKey_returnsDefault() {
        assertTrue(service.getBoolean("MISSING", true));
    }

    // ── set() — disk persistence ──────────────────────────────────────────────

    @Test
    void set_persistsValueToDisk() throws IOException {
        service.set("PERSISTED", "value123");

        // Load a fresh Properties directly from the file to verify disk write.
        Properties reloaded = new Properties();
        try (FileInputStream in = new FileInputStream(propFile.toFile())) {
            reloaded.load(in);
        }
        assertEquals("value123", reloaded.getProperty("PERSISTED"));
    }

    @Test
    void set_canBeReadByNewInstance() throws IOException {
        service.set("SESSION_KEY", "abc");

        PropertyService second = new PropertyService(propFile.toString());
        assertEquals("abc", second.getString("SESSION_KEY"));
    }

    // ── remove() — disk persistence ───────────────────────────────────────────

    @Test
    void remove_deletesKeyFromMemory() {
        service.set("TO_REMOVE", "x");
        service.remove("TO_REMOVE");
        assertNull(service.getString("TO_REMOVE"));
    }

    @Test
    void remove_persistsDeletionToDisk() throws IOException {
        service.set("TO_REMOVE", "x");
        service.remove("TO_REMOVE");

        PropertyService second = new PropertyService(propFile.toString());
        assertNull(second.getString("TO_REMOVE"));
    }

    // ── getAppVersion ─────────────────────────────────────────────────────────

    @Test
    void getAppVersion_defaultsToOnePointZero_onFirstCall() {
        assertEquals("1.0", service.getAppVersion());
    }

    @Test
    void getAppVersion_stableOnSubsequentCalls() {
        String first  = service.getAppVersion();
        String second = service.getAppVersion();
        assertEquals(first, second);
    }

    @Test
    void getAppVersion_respectsExistingValue() {
        service.set("APP_VERSION", "2.5");
        assertEquals("2.5", service.getAppVersion());
    }

    // ── getAppLockKey ─────────────────────────────────────────────────────────

    @Test
    void getAppLockKey_generatesUuidOnFirstCall() {
        String key = service.getAppLockKey();
        assertNotNull(key);
        assertFalse(key.isBlank());
        // UUID format: 8-4-4-4-12 hex characters
        assertTrue(key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }

    @Test
    void getAppLockKey_stableAcrossInstances() throws IOException {
        String first = service.getAppLockKey();

        PropertyService second = new PropertyService(propFile.toString());
        assertEquals(first, second.getAppLockKey());
    }

    // ── getGUILocation ────────────────────────────────────────────────────────

    @Test
    void getGUILocation_defaultsToRightEdge() {
        try (MockedStatic<SystemUtil> su = mockStatic(SystemUtil.class)) {
            su.when(SystemUtil::getScreenSize).thenReturn(new Dimension(1920, 1080));

            var location = service.getGUILocation();

            // X = screenWidth - 400 = 1520, Y = 20
            assertEquals(1520, location.x);
            assertEquals(20,   location.y);
        }
    }

    @Test
    void getGUILocation_respectsStoredCoordinates() {
        service.set("X_LOCATION", "100");
        service.set("Y_LOCATION", "200");

        var location = service.getGUILocation();
        assertEquals(100, location.x);
        assertEquals(200, location.y);
    }
}
