package net.trickycreations.trickytpa.commands.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.enums.Messages;
import net.trickycreations.trickytpa.tpa.model.TpaRequest;
import net.trickycreations.trickytpa.tpa.model.TpaType;
import net.trickycreations.trickytpa.utilities.strings.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

@CommandAlias("tpa")
@CommandPermission("trickylifesteal.command.tpa")
@RequiredArgsConstructor
public class TpaCommand extends BaseCommand {
    private final TrickyTPA instance;

    @Default
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            Messages.USAGE_TPA.send(player);
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

        FileConfiguration config = instance.getConfig();

        Gui gui = Gui.gui()
                .title(CC.component(config.getString("guis.sent_confirm.title"), "{sender}", player.getName(), "{receiver}", player1.getName()))
                .rows(config.getInt("guis.sent_confirm.rows"))
                .disableAllInteractions()
                .create();

        GuiItem infoItem = ItemBuilder.from(Material.valueOf(config.getString("guis.sent_confirm.info.type")))
                .lore(CC.component(config.getStringList("guis.sent_confirm.info.lore")))
                .name(CC.component(config.getString("guis.sent_confirm.info.display_name"), "{sender}", player.getName(), "{receiver}", player1.getName()))
                .flags(ItemFlag.values())
                .glow(config.getBoolean("guis.sent_confirm.info.glow")).asGuiItem();

        GuiItem acceptItem = ItemBuilder.from(Material.valueOf(config.getString("guis.sent_confirm.accept.type")))
                .lore(CC.component(config.getStringList("guis.sent_confirm.accept.lore")))
                .name(CC.component(config.getString("guis.sent_confirm.accept.display_name"), "{sender}", player.getName(), "{receiver}", player1.getName()))
                .flags(ItemFlag.values())
                .glow(config.getBoolean("guis.sent_confirm.accept.glow")).asGuiItem(event -> {
                    if(!event.isLeftClick())
                        return;
                    instance.getTpaManager().sendRequest(new TpaRequest(player, player1, TpaType.TPA));
                    player.closeInventory();
                });

        GuiItem refuseItem = ItemBuilder.from(Material.valueOf(config.getString("guis.sent_confirm.refuse.type")))
                .lore(CC.component(config.getStringList("guis.sent_confirm.refuse.lore")))
                .name(CC.component(config.getString("guis.sent_confirm.refuse.display_name"), "{sender}", player.getName(), "{receiver}", player1.getName()))
                .flags(ItemFlag.values())
                .glow(config.getBoolean("guis.sent_confirm.refuse.glow")).asGuiItem(event -> {
                    if(!event.isLeftClick())
                        return;
                    CC.send(player, "&cTpa request cancelled.");
                    player.closeInventory();
                });

        gui.setItem(config.getInt("guis.sent_confirm.info.slot"), infoItem);
        gui.setItem(config.getInt("guis.sent_confirm.accept.slot"), acceptItem);
        gui.setItem(config.getInt("guis.sent_confirm.refuse.slot"), refuseItem);

        gui.open(player);
    }
}