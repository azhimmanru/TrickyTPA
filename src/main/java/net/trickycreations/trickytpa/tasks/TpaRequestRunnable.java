package net.trickycreations.trickytpa.tasks;

import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.tpa.struct.TpaManager;
import net.trickycreations.trickytpa.tpa.model.TpaRequest;
import net.trickycreations.trickytpa.utilities.strings.CC;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class TpaRequestRunnable extends BukkitRunnable {

    private final TpaManager tpaManager;
    private final TpaRequest request;

    @Override
    public void run() {
        if (!tpaManager.getRequests().containsKey(request.getSender().getUniqueId()) || tpaManager.getRequests().get(request.getSender().getUniqueId()) == null) {
            tpaManager.getRequests().remove(request.getSender().getUniqueId());
            this.cancel();
            return;
        }

        CC.send(request.getSender(), "&cTpa request has expired.");
        CC.send(request.getReceiver(), "&cTpa request has expired.");

        tpaManager.getRequests().remove(request.getSender().getUniqueId());
    }
}