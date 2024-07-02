package net.trickycreations.trickytpa.commands.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.utilities.strings.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpadeny|tparefuse")
@CommandPermission("trickylifesteal.command.tpadeny")
@RequiredArgsConstructor
public class TpaDenyCommand extends BaseCommand {
    private final TrickyTPA instance;

    @Default
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            Messages.ENTER_PLAYER.send(player);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Messages.PLAYER_NOT_FOUND.send(player);
            return;
        }
        instance.getTpaManager().refuseRequest(player, target);
    }
}