package me.lucyy.pronouns;

import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProNouns extends JavaPlugin {

    private PronounHandler handler;

    public PronounHandler getPronounHandler() {
        return handler;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        handler = new PronounHandler(new YamlFileStorage(this));

        ConfigHandler.SetPlugin(this);

        getCommand("pronouns").setExecutor(new PronounsCommand(this));

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PronounsPapiExpansion(this).register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}