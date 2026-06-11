package app.captureeasy.common.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppEventTest {

    @Test
    void constructor_withData_storesTypeAndData() {
        AppEvent<Integer> event = new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, 7);
        assertEquals(EventType.UPDATE_SCREENSHOT_COUNT, event.getType());
        assertEquals(7, event.getData());
    }

    @Test
    void constructor_withoutData_dataIsNull() {
        AppEvent<Void> event = new AppEvent<>(EventType.APP_LOADED);
        assertEquals(EventType.APP_LOADED, event.getType());
        assertNull(event.getData());
    }

    @Test
    void constructor_withNullData_storesNull() {
        AppEvent<String> event = new AppEvent<>(EventType.CLOSE_APP, null);
        assertNull(event.getData());
    }
}
