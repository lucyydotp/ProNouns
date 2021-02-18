package me.lucyy.pronouns.config;

import me.lucyy.pronouns.ProNouns;
import org.bukkit.ChatColor;

public class ConfigHandler {
    private ProNouns pl;

    public ConfigHandler(ProNouns plugin) {
        pl = plugin;
        pl.getConfig().options().copyDefaults(true);

        pl.getConfig().addDefault("checkForUpdates", "true");
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

    private String getString(String key) {
        return getString(key, null);
    }

    private String getString(String key, String defaultVal) {
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

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&',
                pl.getConfig().getString("prefix") + getMainColour());
    }

    @SuppressWarnings("ConstantConditions")
    public String getAccentColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("accent", "&d"));
    }

    @SuppressWarnings("ConstantConditions")
    public String getMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("main", "&f"));
    }

    public ConnectionType getConnectionType() {
        return ConnectionType.valueOf(getString("connection").toUpperCase());
    }

    public SqlInfoContainer getSqlConnectionData() {
        SqlInfoContainer info = new SqlInfoContainer();
        info.Host = getString("mysql.host");
        info.Port = pl.getConfig().getInt("mysql.port", 3306);
        info.Database = getString("mysql.database");
        info.Username = getString("mysql.username");
        info.Password = getString("mysql.password");
        return info;
    }

    public Boolean checkForUpdates() {
        return pl.getConfig().getBoolean("checkForUpdates");
    }
}
