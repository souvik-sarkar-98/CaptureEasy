package app.captureeasy.common.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Simple synchronous/asynchronous event bus.
 *
 * <ul>
 *   <li>{@link #publish} dispatches on the calling thread (e.g. capture thread → controller).</li>
 *   <li>{@link #publishAsync} dispatches on a shared background thread-pool — use this for
 *       heavy listeners that must not block the EDT or a capture thread.</li>
 * </ul>
 */
public class EventBus {
    private static final Map<EventType, List<Consumer<AppEvent<?>>>> listeners = new ConcurrentHashMap<>();
    private static final Logger log = LogManager.getLogger(EventBus.class);

    private static final ExecutorService ASYNC_EXECUTOR =
            Executors.newCachedThreadPool(r -> {
                Thread t = new Thread(r, "EventBus-async");
                t.setDaemon(true);
                return t;
            });

    @SuppressWarnings("unchecked")
	public static <T> void subscribe(EventType eventType, Consumer<AppEvent<T>> listener) {
        log.debug("Subscribing listener for event: {}", eventType.name());
        Consumer<AppEvent<?>> wrapper = event -> listener.accept((AppEvent<T>) event);
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(wrapper);
    }

    /** Dispatches the event synchronously on the calling thread. */
    public static <T> void publish(AppEvent<T> event) {
        List<Consumer<AppEvent<?>>> eventListeners = listeners.get(event.getType());
        log.debug("Publishing event: {}", event.getType().name());
        if (eventListeners != null) {
            for (Consumer<AppEvent<?>> listener : eventListeners) {
                listener.accept(event);
            }
        }
    }

    /**
     * Dispatches the event asynchronously on the shared background thread-pool.
     * Listeners must be thread-safe; Swing components should use
     * {@code SwingUtilities.invokeLater} inside the listener if they update the UI.
     */
    public static <T> void publishAsync(AppEvent<T> event) {
        ASYNC_EXECUTOR.submit(() -> publish(event));
    }
}