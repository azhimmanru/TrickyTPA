package net.trickycreations.trickytpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.utilities.strings.CC;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public enum Messages {
    PLAYER_NOT_FOUND("player_not_found"),
    ENTER_PLAYER("enter_player"),
    USAGE_TPAHERE("usage_tpahere"),
    USAGE_TPA("usage_tpa"),
    CANT_TPA_YOURSELF("cant_tpa_yourself"),
    NO_REQUESTS("no_requests"),
    REQUEST_SENT("request_sent"),
    REQUEST_RECEIVED("request_received"),
    RECEIVER_ACCEPT("receiver_accept"),
    SENDER_NOTIFY_ACCEPT("sender_notify_accept"),
    RECEIVER_REFUSE("receiver_refuse"),
    SENDER_NOTIFY_REFUSE("sender_notify_refuse");

    private final String path;

    public void send(Player player) {
        CC.send(player, TrickyTPA.getInstance().getConfig().getString("messages." + path));
    }
    public void send(Player player, Object... params) {
        CC.send(player, TrickyTPA.getInstance().getConfig().getString("messages." + path), params);
    }
}