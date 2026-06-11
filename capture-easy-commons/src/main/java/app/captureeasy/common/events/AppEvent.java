package app.captureeasy.common.events;

import lombok.Getter;

@Getter
public class AppEvent<T> {
    private final EventType type;
    private final T data;

    public AppEvent(EventType type, T data) {
        this.type = type;
        this.data = data;
    }

    public AppEvent(EventType type) {
        this(type, null);
    }
}
