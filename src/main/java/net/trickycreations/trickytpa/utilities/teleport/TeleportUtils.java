package net.trickycreations.trickytpa.utilities.teleport;

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.enums.Settings;
import net.trickycreations.trickytpa.utilities.strings.CC;
import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;

@UtilityClass
public final class TeleportUtils {
    private static final Map<UUID, BukkitTask> teleports = Maps.newHashMap();

    public void startCountdownTeleport(Player player, Location destination) {
        int seconds = Settings.TPA_COUNTDOWN.get(Integer.class);
        cancelExistingTeleport(player);
        if (seconds <= 0) {
            handleTeleportSuccess(player, destination);
            return;
        }

        Location initialLocation = player.getLocation();
        double initialX = initialLocation.getX();
        double initialY = initialLocation.getY();
        double initialZ = initialLocation.getZ();

        MutableInt cooldown = new MutableInt(seconds);
        teleports.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimerAsynchronously(TrickyTPA.getInstance(), () -> {
            if (!player.isOnline() || hasPlayerMoved(player, initialX, initialY, initialZ)) {
                cancelTeleport(player);
                if (player.isOnline())
                    handleTeleportFailure(player);
                return;
            }

            if (cooldown.intValue() <= 0) {
                handleTeleportSuccess(player, destination);
                cancelTeleport(player);
                return;
            }

            handleTeleportProgress(player, cooldown.intValue());
            cooldown.decrement();
        }, 1L, 20L));
    }

    private void cancelExistingTeleport(Player player) {
        BukkitTask existingTask = teleports.remove(player.getUniqueId());
        if (existingTask != null)
            existingTask.cancel();
    }

    private boolean hasPlayerMoved(Player player, double initialX, double initialY, double initialZ) {
        Location playerLocation = player.getLocation();
        return (playerLocation.getX() != initialX || playerLocation.getY() != initialY || playerLocation.getZ() != initialZ);
    }

    private void handleTeleportFailure(Player player) {
        Messages.TELEPORT_CANCELLED.send(player);
        if(Settings.TELEPORT_ACTION_BAR.get(Boolean.class))
            Messages.TELEPORT_ACTION_BAR_CANCEL.sendActionBar(player);
        if(Settings.TELEPORT_TITLE.get(Boolean.class))
            CC.sendTitle(player, Messages.TELEPORT_TITLE_CANCELLED_TITLE.get(), Messages.TELEPORT_TITLE_CANCELLED_SUB_TITLE.get(), 2);
    }

    private void handleTeleportSuccess(Player player, Location toLocation) {
        player.teleportAsync(toLocation);
        Messages.TELEPORT_SUCCESS.send(player);
        if(Settings.TELEPORT_ACTION_BAR.get(Boolean.class))
            Messages.TELEPORT_ACTION_BAR_SUCCESS.sendActionBar(player);
        if(Settings.TELEPORT_TITLE.get(Boolean.class))
            CC.sendTitle(player, Messages.TELEPORT_TITLE_SUCCESS_TITLE.get(), Messages.TELEPORT_TITLE_SUCCESS_SUB_TITLE.get(), 2);
    }

    private void handleTeleportProgress(Player player, int cooldown) {
        Messages.TELEPORT_PROGRESS.send(player, "{time}", String.valueOf(cooldown));
        if(Settings.TELEPORT_ACTION_BAR.get(Boolean.class))
            Messages.TELEPORT_ACTION_BAR_PROGRESS.sendActionBar(player, "{time}", String.valueOf(cooldown));
        if(Settings.TELEPORT_TITLE.get(Boolean.class))
            CC.sendTitle(player, Messages.TELEPORT_TITLE_PROGRESS_TITLE.get().replace("{time}", String.valueOf(cooldown)), Messages.TELEPORT_TITLE_PROGRESS_SUB_TITLE.get().replace("{time}", String.valueOf(cooldown)), 2);
    }

    private void cancelTeleport(Player player) {
        BukkitTask existingTask = teleports.remove(player.getUniqueId());
        if (existingTask != null)
            existingTask.cancel();
    }
}