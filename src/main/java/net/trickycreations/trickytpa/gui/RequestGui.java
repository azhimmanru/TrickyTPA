package net.trickycreations.trickytpa.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.trickycreations.trickytpa.TrickyTPA;
import net.trickycreations.trickytpa.tpa.model.TpaRequest;
import net.trickycreations.trickytpa.tpa.model.TpaType;
import net.trickycreations.trickytpa.utilities.skull.SkullCreator;
import net.trickycreations.trickytpa.utilities.strings.CC;
import net.trickycreations.trickytpa.utilities.universal.XMaterial;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class RequestGui {
    private final Player sender, receiver;
    private final TpaType type;
    private Gui gui;

    public RequestGui(Player sender, Player receiver, TpaType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        loadGui();
    }

    public void open(Player player) {
        if(gui == null) {
            CC.send(player, "&cGui not found.");
            return;
        }
        gui.open(player);
    }

    public void loadGui() {
        FileConfiguration config = TrickyTPA.getInstance().getConfig();

        gui = Gui.gui()
                .title(CC.component(config.getString("guis.sent_confirm.title"), "{sender}", sender.getName(), "{receiver}", receiver.getName()))
                .rows(config.getInt("guis.sent_confirm.rows"))
                .disableAllInteractions()
                .create();

        GuiItem infoItem = createItem(
                SkullCreator.itemFromUuid(receiver.getUniqueId()),
                CC.replace(config.getString("guis.sent_confirm.info.display_name"),  "{sender}", sender.getName(), "{receiver}", receiver.getName()),
                config.getStringList("guis.sent_confirm.info.lore"),
                true,
                config.getBoolean("guis.sent_confirm.info.glow")
        );

        GuiItem acceptItem = createItem(
                config.getString("guis.sent_confirm.accept.type").toUpperCase(),
                CC.replace(config.getString("guis.sent_confirm.accept.display_name"),  "{sender}", sender.getName(), "{receiver}", receiver.getName()),
                config.getStringList("guis.sent_confirm.accept.lore"),
                true,
                config.getBoolean("guis.sent_confirm.accept.glow")
        );

        acceptItem.setAction(event -> {
            if(!event.isLeftClick())
                return;
            TrickyTPA.getInstance().getTpaManager().sendRequest(new TpaRequest(sender, receiver, type));
            sender.closeInventory();
        });

        GuiItem refuseItem = createItem(
                config.getString("guis.sent_confirm.refuse.type").toUpperCase(),
                CC.replace(config.getString("guis.sent_confirm.refuse.display_name"),  "{sender}", sender.getName(), "{receiver}", receiver.getName()),
                config.getStringList("guis.sent_confirm.refuse.lore"),
                true,
                config.getBoolean("guis.sent_confirm.refuse.glow")
        );

        refuseItem.setAction(event -> {
            if(!event.isLeftClick())
                return;
            CC.send(sender, "&cTpa request cancelled.");
            sender.closeInventory();
        });


        gui.setItem(config.getInt("guis.sent_confirm.info.slot"), infoItem);
        gui.setItem(config.getInt("guis.sent_confirm.accept.slot"), acceptItem);
        gui.setItem(config.getInt("guis.sent_confirm.refuse.slot"), refuseItem);

        gui.open(sender);
    }

    public GuiItem createItem(String material, String displayName, List<String> lore, boolean hideFlags, boolean glow) {
        return ItemBuilder.from(XMaterial.matchXMaterial(material).get().parseMaterial())
                .name(CC.component(displayName))
                .lore(CC.component(lore))
                .flags(hideFlags ? ItemFlag.values() : null)
                .glow(glow)
                .asGuiItem();
    }
    public GuiItem createItem(ItemStack itemStack, String displayName, List<String> lore, boolean hideFlags, boolean glow) {
        return ItemBuilder.from(itemStack)
                .name(CC.component(displayName))
                .lore(CC.component(lore))
                .flags(hideFlags ? ItemFlag.values() : null)
                .glow(glow)
                .asGuiItem();
    }
}
