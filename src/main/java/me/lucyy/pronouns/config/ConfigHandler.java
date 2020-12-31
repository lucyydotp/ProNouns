package me.lucyy.pronouns.config;

import me.lucyy.pronouns.ProNouns;
import org.bukkit.ChatColor;

public class ConfigHandler {
    private static ProNouns pl;

    public static void SetPlugin(ProNouns plugin) {
        pl = plugin;
        pl.saveDefaultConfig();
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
        return ConnectionType.valueOf(getString("connection"));
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
