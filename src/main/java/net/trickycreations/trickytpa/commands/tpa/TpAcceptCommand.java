package net.trickycreations.trickytpa.commands.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.gui.AcceptGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpaccept")
@CommandPermission("trickylifesteal.command.tpaccept")
@RequiredArgsConstructor
public class TpAcceptCommand extends BaseCommand {
    private final TrickyTPA instance;

    @Default
    public void command(Player player, String[] args) {
        if (args.length == 0) {
            Messages.ENTER_PLAYER.send(player);
            return;
        }
        Player player1 = Bukkit.getPlayer(args[0]);
        if (player1 == null) {
            Messages.PLAYER_NOT_FOUND.send(player);
            return;
        }

        new AcceptGui(player, player1).open(player);
    }
}