package me.lucyy.pronouns;

import org.bukkit.ChatColor;

public class ConfigHandler {
    private static ProNouns pl;

    public static void SetPlugin(ProNouns plugin) {
        pl = plugin;
        pl.saveDefaultConfig();
    }

    public static String GetPrefix() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("prefix"));
    }
    public static String GetAccentColour() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("accent"));
    }

    public static String GetMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("main"));
    }
}
