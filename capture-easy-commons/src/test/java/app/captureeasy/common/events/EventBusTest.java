package app.captureeasy.common.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    @BeforeEach
    void clearListeners() throws Exception {
        // Reset the static listener map between tests via reflection
        var field = EventBus.class.getDeclaredField("listeners");
        field.setAccessible(true);
        ((java.util.Map<?, ?>) field.get(null)).clear();
    }

    @Test
    void publish_deliversEventToSubscriber() {
        AtomicReference<String> received = new AtomicReference<>();
        EventBus.subscribe(EventType.APP_LOADED, event -> received.set("loaded"));

        EventBus.publish(new AppEvent<>(EventType.APP_LOADED));

        assertEquals("loaded", received.get());
    }

    @Test
    void publish_deliversTypedDataToSubscriber() {
        AtomicInteger receivedCount = new AtomicInteger();
        EventBus.subscribe(EventType.UPDATE_SCREENSHOT_COUNT,
                (AppEvent<Integer> e) -> receivedCount.set(e.getData()));

        EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT, 42));

        assertEquals(42, receivedCount.get());
    }

    @Test
    void publish_noSubscribers_doesNotThrow() {
        assertDoesNotThrow(() -> EventBus.publish(new AppEvent<>(EventType.CLOSE_APP)));
    }

    @Test
    void publish_multipleSubscribers_allReceiveEvent() {
        AtomicInteger counter = new AtomicInteger(0);
        EventBus.subscribe(EventType.TAKE_SCREENSHOT, e -> counter.incrementAndGet());
        EventBus.subscribe(EventType.TAKE_SCREENSHOT, e -> counter.incrementAndGet());

        EventBus.publish(new AppEvent<>(EventType.TAKE_SCREENSHOT));

        assertEquals(2, counter.get());
    }

    @Test
    void publishAsync_deliversEventOnBackgroundThread() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> threadName = new AtomicReference<>();

        EventBus.subscribe(EventType.APP_LOADED, event -> {
            threadName.set(Thread.currentThread().getName());
            latch.countDown();
        });

        EventBus.publishAsync(new AppEvent<>(EventType.APP_LOADED));

        assertTrue(latch.await(2, TimeUnit.SECONDS), "Event not received within timeout");
        assertTrue(threadName.get().startsWith("EventBus-async"),
                "Expected EventBus-async thread, got: " + threadName.get());
    }

    @Test
    void appEvent_withNoData_returnsNullData() {
        AppEvent<String> event = new AppEvent<>(EventType.CLOSE_APP);
        assertEquals(EventType.CLOSE_APP, event.getType());
        assertNull(event.getData());
    }
}
