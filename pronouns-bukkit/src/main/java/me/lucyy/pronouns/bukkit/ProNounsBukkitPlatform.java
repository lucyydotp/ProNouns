package me.lucyy.pronouns.bukkit;

import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.bukkit.event.PronounsSetEvent;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.bukkit.BukkitNodeExecutor;
import net.lucypoulton.squirtgun.bukkit.BukkitPlatform;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
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
    public void onPronounsSet(UUID uuid, Set<PronounSet> sets) {
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
