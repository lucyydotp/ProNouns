/*
 * Copyright (C) 2021 Lucy Poulton https://lucyy.me
 * This file is part of ProNouns.
 *
 * ProNouns is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProNouns is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProNouns.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.lucyy.pronouns.config;

import me.lucyy.common.command.FormatProvider;
import me.lucyy.common.format.TextFormatter;
import me.lucyy.pronouns.ProNouns;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler implements FormatProvider {
    private final ProNouns pl;
    private final HashMap<TextDecoration, Character> decoStrings = new HashMap<>();

    public ConfigHandler(ProNouns plugin) {
        pl = plugin;
        FileConfiguration cfg = pl.getConfig();
        cfg.options().header("ProNouns Config File\n" +
                "Make changes here and update them by either using /pronouns reload\n" +
                "or by restarting the server\n" +
                "NOTE: if you're using predefined sets with MySQL, make sure they match on\n" +
                "all servers!\n" +
                "Documentation at https://docs.lucyy.me/pronouns\n" +
                "Support discord at https://support.lucyy.me");

        cfg.addDefault("checkForUpdates", "true");
        cfg.addDefault("accent", "&d");
        cfg.addDefault("main", "&f");

        cfg.addDefault("connection", "yml");
        cfg.addDefault("mysql.host", "127.0.0.1");
        cfg.addDefault("mysql.port", 3306);
        cfg.addDefault("mysql.database", "pronouns");
        cfg.addDefault("mysql.username", "pronouns");
        cfg.addDefault("mysql.password", "password");

        cfg.addDefault("predefinedSets", new ArrayList<String>());

        cfg.addDefault("filter.enabled", "true");
        cfg.addDefault("filter.patterns", new String[]{"apache+", "hel+icop+ter"});

        cfg.addDefault("discord.enabled", "false");
        cfg.addDefault("discord.cmdPrefix", "?pronouns");

        pl.saveConfig();

        decoStrings.put(TextDecoration.OBFUSCATED, 'k');
        decoStrings.put(TextDecoration.BOLD, 'l');
        decoStrings.put(TextDecoration.STRIKETHROUGH, 'm');
        decoStrings.put(TextDecoration.UNDERLINED, 'n');
        decoStrings.put(TextDecoration.ITALIC, 'o');
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

    private String serialiseFormatters(TextDecoration... formatters) {
        if (formatters == null) return null;
        StringBuilder out = new StringBuilder();
        for (TextDecoration deco : formatters) out.append(decoStrings.get(deco));
        return out.toString();
    }

    private Component applyFormatter(String formatter, String content, String formatters) {
        return formatter.contains("%s") ?
                TextFormatter.format(String.format(formatter, content), formatters, true) :
                TextFormatter.format(formatter + content, formatters, true);
    }

    @SuppressWarnings("ConstantConditions")
    public String getAccentColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("accent", "&3"));
    }

    @Override
    public Component formatAccent(@NotNull String s, TextDecoration... formatters) {
        return applyFormatter(getAccentColour(), s, serialiseFormatters(formatters));
    }

    @SuppressWarnings("ConstantConditions")
    public String getMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("main", "&f"));
    }

    @Override
    public Component formatMain(@NotNull String s, TextDecoration... formatters) {
        return applyFormatter(getMainColour(), s, serialiseFormatters(formatters));
    }

    @SuppressWarnings("ConstantConditions")
    public Component getPrefix() {
        String prefix = getString("format.prefix", "");
        if (prefix.equals("")) return formatAccent("Pronouns")
                .append(Component.text(" >> ").color(NamedTextColor.GRAY));
        return TextFormatter.format(getString("format.prefix"));
    }

    public List<String> getPredefinedSets() {
        return pl.getConfig().getStringList("predefinedSets");
    }

    public List<String> getFilterPatterns() {
        return pl.getConfig().getStringList("filter.patterns");
    }

    public boolean filterEnabled() {
        return !"false".equals(pl.getConfig().getString("filter.enabled"));
    }

    public ConnectionType getConnectionType() {
        return ConnectionType.valueOf(getString("connection").toUpperCase());
    }

    public SqlInfoContainer getSqlConnectionData() {
        SqlInfoContainer info = new SqlInfoContainer();
        info.host = getString("mysql.host");
        info.port = pl.getConfig().getInt("mysql.port", 3306);
        info.database = getString("mysql.database");
        info.username = getString("mysql.username");
        info.password = getString("mysql.password");
        return info;
    }

    public boolean checkForUpdates() {
        return !"false".equals(pl.getConfig().getString("checkForUpdates"));
    }

	public boolean discordEnabled() {
		return !"false".equals(pl.getConfig().getString("discord.enabled"));
	}

	public String getDiscordCmd() {
    	 return pl.getConfig().getString("discord.cmdPrefix");
	}
}
