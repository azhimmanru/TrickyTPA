package net.trickycreations.trickytpa.tpa.struct;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.enums.Settings;
import net.trickycreations.trickytpa.tasks.TpaRequestRunnable;
import net.trickycreations.trickytpa.tpa.model.TpaRequest;
import net.trickycreations.trickytpa.tpa.model.TpaType;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class TpaManager {
    private final TrickyTPA instance;

    @Getter
    private final Map<UUID, TpaRequest> requests = Maps.newConcurrentMap();

    public void sendRequest(TpaRequest request) {
        requests.put(request.getSender().getUniqueId(), request);

        Messages.REQUEST_SENT.send(request.getSender(), "{receiver}", request.getReceiver().getName(), "{type}", getType(request.getType()));
        Messages.REQUEST_RECEIVED.send(request.getReceiver(), "{sender}", request.getSender().getName(), "{type}", getType(request.getType()));

        startTask(request);
    }

    private void startTask(TpaRequest request) {
        if (request == null)
            return;
        new TpaRequestRunnable(this, request).runTaskLaterAsynchronously(instance, Settings.EXPIRED_TIME.get(Integer.class) * 20L);
    }

    public void acceptRequest(Player receiver, Player sender) {
        TpaRequest request = getRequest(sender);
        if (request != null) {
            if (!hasPendingRequest(receiver, sender) || receiver == sender) {
                Messages.NO_REQUESTS.send(receiver);
                return;
            }

            Messages.RECEIVER_ACCEPT.send(receiver, "{type}", getType(request.getType()));
            Messages.SENDER_NOTIFY_ACCEPT.send(sender, "{type}", getType(request.getType()));
            request.startTeleport();

            requests.remove(sender.getUniqueId());
            return;
        }
        Messages.NO_REQUESTS.send(receiver);
    }

    public void refuseRequest(Player receiver, Player sender) {
        TpaRequest request = getRequest(sender);
        if (request != null) {
            if (!hasPendingRequest(receiver, sender) || receiver == sender) {
                Messages.NO_REQUESTS.send(receiver);
                return;
            }
            Messages.RECEIVER_REFUSE.send(receiver, "{type}", getType(request.getType()));
            Messages.SENDER_NOTIFY_REFUSE.send(sender, "{type}", getType(request.getType()));
            requests.remove(sender.getUniqueId());
            return;
        }
        Messages.NO_REQUESTS.send(receiver);
    }

    public TpaRequest getRequest(Player sender) {
        return requests.get(sender.getUniqueId());
    }

    public boolean hasPendingRequest(Player receiver, Player sender) {
        TpaRequest request = getRequest(sender);
        if (request == null)
            return false;
        return request.getReceiver().getUniqueId().equals(receiver.getUniqueId()) && sender.getUniqueId().equals(sender.getUniqueId());
    }

    private String getType(TpaType type) {
        return (type == TpaType.TPA_HERE) ? instance.getConfig().getString("settings.format.tpa_here") : instance.getConfig().getString("settings.format.tpa");
    }
}
