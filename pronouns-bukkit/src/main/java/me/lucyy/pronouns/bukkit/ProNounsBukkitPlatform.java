package me.lucyy.pronouns.bukkit;

import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.bukkit.event.PronounsSetEvent;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.storage.Storage;
import me.lucyy.squirtgun.bukkit.BukkitNodeExecutor;
import me.lucyy.squirtgun.bukkit.BukkitPlatform;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;
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
    public void registerCommand(CommandNode<PermissionHolder> node) {
        PluginCommand command = plugin.getCommand(node.getName());
        Objects.requireNonNull(command);
        TabExecutor executor = new BukkitNodeExecutor(node, plugin.getConfigHandler(), this);
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }

    @Override
    public String name() {
        return "Bukkit (ProNouns)";
    }
}
