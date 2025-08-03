package app.techy10souvik.captureeasy.core.eventdto;

import lombok.Getter;

public class CaptureData {
    @Getter
    private final String filePath;
    @Getter
    private int count;


    public CaptureData(String filePath, int count) {
        this.filePath = filePath;
        this.count = count;
    }
}
