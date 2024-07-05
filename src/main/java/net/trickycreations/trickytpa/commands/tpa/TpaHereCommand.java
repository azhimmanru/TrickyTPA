package net.trickycreations.trickytpa.commands.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.gui.RequestGui;
import net.trickycreations.trickytpa.tpa.model.TpaType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpahere")
@CommandPermission("trickylifesteal.command.tpahere")
public class TpaHereCommand extends BaseCommand {

    @Default
    public void command(Player player, String[] args) {
        if (args.length == 0) {
            Messages.USAGE_TPAHERE.send(player);
            return;
        }

        Player player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            Messages.PLAYER_NOT_FOUND.send(player);
            return;
        }

        if (player == player1) {
            Messages.CANT_TPA_YOURSELF.send(player);
            return;
        }

        new RequestGui(player, player1, TpaType.TPA_HERE).open(player);
    }
}