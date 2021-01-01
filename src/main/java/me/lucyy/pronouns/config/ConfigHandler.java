package me.lucyy.pronouns.config;

import me.lucyy.pronouns.ProNouns;
import org.bukkit.ChatColor;

import java.util.Locale;

public class ConfigHandler {
    private static ProNouns pl;

    public static void SetPlugin(ProNouns plugin) {
        pl = plugin;
        pl.getConfig().options().copyDefaults(true);

        pl.getConfig().addDefault("checkForUpdates", "true");
        pl.getConfig().addDefault("updateUrl", "https://lucyy.me/pronouns/version.json");
        pl.getConfig().addDefault("prefix", "&f[&dPronouns&f] ");
        pl.getConfig().addDefault("accent", "&d");
        pl.getConfig().addDefault("main", "&f");

        pl.getConfig().addDefault("connection", "yml");
        pl.getConfig().addDefault("mysql.host", "127.0.0.1");
        pl.getConfig().addDefault("mysql.port", 3306);
        pl.getConfig().addDefault("mysql.database", "pronouns");
        pl.getConfig().addDefault("mysql.username", "pronouns");
        pl.getConfig().addDefault("mysql.password", "password");
        pl.saveConfig();
    }

    private static String getString(String key) {
        return getString(key, null);
    }

    private static String getString(String key, String defaultVal) {
        String value = pl.getConfig().getString(key);
        if (value == null) {
            if (defaultVal == null) {
                pl.getLogger().severe("Your config file is broken! Unable to read key '" + key);
                return null;
            }
            return defaultVal;
        }
        return value;
    }

    public static String GetPrefix() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("prefix") + GetMainColour());
    }

    public static String GetAccentColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("accent", "&d"));
    }

    public static String GetMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("main", "&f"));
    }

    public static String GetUpdateUrl() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("updateUrl"));
    }

    public static ConnectionType GetConnectionType() {
        return ConnectionType.valueOf(getString("connection").toUpperCase());
    }

    public static SqlInfoContainer GetSqlConnectionData() {
        SqlInfoContainer info = new SqlInfoContainer();
        info.Host = getString("mysql.host");
        info.Port = pl.getConfig().getInt("mysql.port", 3306);
        info.Database = getString("mysql.database");
        info.Username = getString("mysql.username");
        info.Password = getString("mysql.password");
        return info;
    }

    public static Boolean CheckForUpdates() {
        return pl.getConfig().getBoolean("checkForUpdates");
    }
}
