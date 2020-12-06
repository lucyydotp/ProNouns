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
                pl.getConfig().getString("prefix") + GetMainColour());
    }
    public static String GetAccentColour() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("accent"));
    }

    public static String GetMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("main"));
    }

    public static String GetUpdateUrl() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("updateUrl"));
    }

    public static Boolean CheckForUpdates() {
        return pl.getConfig().getBoolean("checkForUpdates");
    }
}
