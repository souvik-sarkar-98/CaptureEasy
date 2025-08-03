package app.techy10souvik.captureeasy.common.events;

public class AppEvent<T> {
    private final EventType type;
    private T data;

    public AppEvent(EventType type, T data) {
        this.type = type;
        this.data = data;
    }

    public AppEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public T getData() {
        return data;
    }
}
