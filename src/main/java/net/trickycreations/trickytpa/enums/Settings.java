package net.trickycreations.trickytpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.trickycreations.trickytpa.TrickyTPA;

@AllArgsConstructor
@Getter
public enum Settings {
    EXPIRED_TIME("settings.expired_time"),
    TPA_COUNTDOWN("settings.tpa_countdown"),

    TELEPORT_TITLE("settings.teleport.title"),
    TELEPORT_ACTION_BAR("settings.teleport.action_bar"),
    ;

    private final String path;

    public <T> T get(Class<T> clazz) {
        return clazz.cast(TrickyTPA.getInstance().getConfig().get(path));
    }
}