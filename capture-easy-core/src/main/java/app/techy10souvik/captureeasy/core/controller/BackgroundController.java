package app.techy10souvik.captureeasy.core.controller;

import app.techy10souvik.captureeasy.common.events.AppEvent;
import app.techy10souvik.captureeasy.common.events.EventBus;
import app.techy10souvik.captureeasy.common.events.EventType;
import app.techy10souvik.captureeasy.core.eventdto.CaptureData;
import app.techy10souvik.captureeasy.core.services.CaptureService;

public class BackgroundController {
    private final CaptureService captureService;

    protected BackgroundController() throws Exception {
        registerAppInitializationEvent();
        captureService = new CaptureService();
    }

    private void registerAppInitializationEvent() {
        EventBus.subscribe(EventType.APP_LOADED, event -> {
            try {
                int count = Math.toIntExact(captureService.getScreenshotCount());
                EventBus.publish(new AppEvent<>(EventType.UPDATE_SCREENSHOT_COUNT,count));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
