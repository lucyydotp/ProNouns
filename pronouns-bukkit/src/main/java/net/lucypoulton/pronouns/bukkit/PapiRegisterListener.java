package net.lucypoulton.pronouns.bukkit;

import net.lucypoulton.pronouns.ProNouns;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PapiRegisterListener implements Listener {
    private final ProNouns platform;

    public PapiRegisterListener(ProNouns platform) {
        this.platform = platform;
    }

    @EventHandler
    public void onRegister(PluginEnableEvent e) {
        if (!e.getPlugin().getName().equals("PlaceholderAPI")) return;
        new ProNounsPapi(platform).register();
    }
}
