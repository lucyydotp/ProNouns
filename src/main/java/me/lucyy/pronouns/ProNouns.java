package me.lucyy.pronouns;

import me.lucyy.pronouns.command.PronounsCommand;
import me.lucyy.pronouns.storage.YamlFileStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ProNouns extends JavaPlugin {

    private PronounHandler handler;
    private Metrics metrics;
    public PronounHandler getPronounHandler() {
        return handler;
    }

    @Override
    public void onEnable() {
        int pluginId = 9519;
        metrics = new Metrics(this, pluginId);

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