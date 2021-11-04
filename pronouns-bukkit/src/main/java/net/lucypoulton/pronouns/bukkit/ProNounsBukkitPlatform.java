package net.lucypoulton.pronouns.bukkit;

import net.lucypoulton.pronouns.ProNounsPlatform;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.bukkit.BukkitPlatform;

public class ProNounsBukkitPlatform extends BukkitPlatform implements ProNounsPlatform {
    private final ProNounsBukkit plugin;
    public ProNounsBukkitPlatform(ProNounsBukkit plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public ConfigHandler getConfigHandler() {
        return plugin.getConfigHandler();
    }

    @Override
    public Storage getStorage() {
        return plugin.getStorage();
    }

    @Override
    public void reloadConfig() {
        plugin.reloadConfig();
    }

    @Override
    public String name() {
        return "Bukkit (ProNouns)";
    }
}
