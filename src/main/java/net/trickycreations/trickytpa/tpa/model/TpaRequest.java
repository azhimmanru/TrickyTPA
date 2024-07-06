package net.trickycreations.trickytpa.tpa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.trickycreations.trickytpa.utilities.teleport.TeleportUtils;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public class TpaRequest {
    private Player sender, receiver;
    private TpaType type;

    public void startTeleport() {
        if (type == TpaType.TPA_HERE)
            TeleportUtils.startCountdownTeleport(receiver, sender.getLocation());
        else
            TeleportUtils.startCountdownTeleport(sender, receiver.getLocation());
    }
}