package me.lucyy.pronouns.bungee;

import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;
import me.lucyy.squirtgun.bungee.BungeeNodeExecutor;
import me.lucyy.squirtgun.bungee.BungeePlatform;
import me.lucyy.squirtgun.command.node.CommandNode;
import me.lucyy.squirtgun.platform.audience.PermissionHolder;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class ProNounsBungeePlatform extends BungeePlatform implements ProNounsPlatform {
    private final ProNounsBungee plugin;
    public ProNounsBungeePlatform(ProNounsBungee plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public BungeeConfigHandler getConfigHandler() {
        return plugin.getConfigHandler();
    }

    @Override
    public Storage getStorage() {
        return plugin.getStorage();
    }

    @Override
    public void reloadConfig() {
        try {
            plugin.getConfigHandler().reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // unused
    @Override
    public void onPronounsSet(UUID uuid, Set<PronounSet> sets) { }

    @Override
    public void registerCommand(CommandNode<PermissionHolder> node) {
        plugin.getProxy().getPluginManager().registerCommand(plugin,
                new BungeeNodeExecutor(node, getConfigHandler(), this));
    }

    @Override
    public String name() {
        return "BungeeCord (ProNouns)";
    }
}
