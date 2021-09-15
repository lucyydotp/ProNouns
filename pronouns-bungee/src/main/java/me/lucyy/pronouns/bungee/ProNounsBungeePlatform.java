package me.lucyy.pronouns.bungee;

import me.lucyy.pronouns.ProNounsPlatform;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.bungee.BungeeNodeExecutor;
import net.lucypoulton.squirtgun.bungee.BungeePlatform;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;

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
    public String name() {
        return "BungeeCord (ProNouns)";
    }
}
