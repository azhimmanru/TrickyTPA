package net.trickycreations.trickytpa;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import net.trickycreations.trickytpa.commands.tpa.TpAcceptCommand;
import net.trickycreations.trickytpa.commands.tpa.TpaCommand;
import net.trickycreations.trickytpa.commands.tpa.TpaDenyCommand;
import net.trickycreations.trickytpa.commands.tpa.TpaHereCommand;
import net.trickycreations.trickytpa.tpa.struct.TpaManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public final class TrickyTPA extends JavaPlugin {

    @Getter
    private static TrickyTPA instance;

    private TpaManager tpaManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        tpaManager = new TpaManager(this);
        loadCommandsAndListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommandsAndListeners() {
        PaperCommandManager commandManager = new PaperCommandManager(this);

        List.of(
                new TpaCommand(),
                new TpAcceptCommand(this),
                new TpaDenyCommand(this),
                new TpaHereCommand()
        ).forEach(commandManager::registerCommand);
    }
}