package net.lucypoulton.pronouns.bungee;

import net.lucypoulton.pronouns.ProNounsPlatform;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.bungee.BungeePlatform;

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

    @Override
    public String name() {
        return "BungeeCord (ProNouns)";
    }
}
