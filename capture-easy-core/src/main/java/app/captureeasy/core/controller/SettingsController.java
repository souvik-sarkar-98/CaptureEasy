package app.captureeasy.core.controller;

import app.captureeasy.common.events.AppEvent;
import app.captureeasy.common.events.EventBus;
import app.captureeasy.common.events.EventType;
import app.captureeasy.common.services.PropertyService;
import app.captureeasy.core.eventdto.SettingsDto;
import app.captureeasy.core.services.KeyboardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Persists user preferences received via {@link EventType#SAVE_SETTINGS}.
 *
 * <p>SettingsPanel publishes {@link EventType#SAVE_SETTINGS} with a
 * {@link SettingsDto}; this controller is the only class that writes to
 * {@link PropertyService} for user preferences, keeping persistence concerns
 * out of the UI layer. After persisting it publishes
 * {@link EventType#SETTINGS_SAVED} so the UI can show confirmation.</p>
 */
public class SettingsController {

    private static final Logger log = LogManager.getLogger(SettingsController.class);

    protected SettingsController() {
        registerSaveSettingsEvent();
    }

    private void registerSaveSettingsEvent() {
        EventBus.subscribe(EventType.SAVE_SETTINGS, (AppEvent<SettingsDto> event) -> {
            SettingsDto dto = event.getData();
            if (dto == null) return;

            PropertyService settings = PropertyService.getInstance();

            if (dto.getImageFormat() != null) {
                settings.set("IMAGE_FORMAT", dto.getImageFormat());
                log.info("Image format updated to {}", dto.getImageFormat());
            }
            if (dto.getHotkeyCode() != null) {
                settings.set(KeyboardService.PROP_KEY_CODE, String.valueOf(dto.getHotkeyCode()));
                // Always persist the modifier mask alongside the key code so they stay in sync.
                int modMask = dto.getHotkeyModifierMask() != null ? dto.getHotkeyModifierMask() : 0;
                settings.set(KeyboardService.PROP_MODIFIER_MASK, String.valueOf(modMask));
                log.info("Screenshot hotkey updated to keyCode={} modifierMask={}",
                        dto.getHotkeyCode(), modMask);
            }

            EventBus.publish(new AppEvent<>(EventType.SETTINGS_SAVED));
        });
    }
}
