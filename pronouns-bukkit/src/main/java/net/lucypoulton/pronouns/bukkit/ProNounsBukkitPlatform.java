package net.lucypoulton.pronouns.bukkit;

import net.lucypoulton.pronouns.ProNounsPlatform;
import net.lucypoulton.pronouns.bukkit.event.PronounsSetEvent;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.bukkit.BukkitPlatform;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;

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
    public void onPronounsSet(UUID uuid, Set<OldPronounSet> sets) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().getPluginManager().callEvent(new PronounsSetEvent(uuid, sets));
            }
        }.runTask(plugin);
    }

    @Override
    public String name() {
        return "Bukkit (ProNouns)";
    }
}
