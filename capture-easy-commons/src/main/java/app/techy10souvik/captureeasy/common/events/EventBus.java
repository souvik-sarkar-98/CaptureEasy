package app.techy10souvik.captureeasy.common.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventBus {
    private static final Map<EventType, List<Consumer<AppEvent<?>>>> listeners = new ConcurrentHashMap<>();
    private static final Logger log = LogManager.getLogger(EventBus.class);

    public static <T> void subscribe(EventType eventType, Consumer<AppEvent<T>> listener) {
        log.info("Subscribing listener for event: {}", eventType.name());
        Consumer<AppEvent<?>> wrapper = event -> listener.accept((AppEvent<T>) event);
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(wrapper);
    }

    public static <T> void publish(AppEvent<T> event) {
        List<Consumer<AppEvent<?>>> eventListeners = listeners.get(event.getType());
        log.info("Publishing event: {}", event.getType().name());
        if (eventListeners != null) {
            for (Consumer<AppEvent<?>> listener : eventListeners) {
                listener.accept(event);
            }
        }
    }
}