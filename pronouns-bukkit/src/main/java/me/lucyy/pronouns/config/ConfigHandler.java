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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler implements FormatProvider {
    private final ProNouns pl;
    private final HashMap<TextDecoration, Character> decoStrings = new HashMap<>();

    public ConfigHandler(ProNouns plugin) {
        pl = plugin;
        pl.getConfig().options().copyDefaults(true);

        pl.getConfig().addDefault("checkForUpdates", "true");
        pl.getConfig().addDefault("accent", "&d");
        pl.getConfig().addDefault("main", "&f");

        pl.getConfig().addDefault("connection", "yml");
        pl.getConfig().addDefault("mysql.host", "127.0.0.1");
        pl.getConfig().addDefault("mysql.port", 3306);
        pl.getConfig().addDefault("mysql.database", "pronouns");
        pl.getConfig().addDefault("mysql.username", "pronouns");
        pl.getConfig().addDefault("mysql.password", "password");

        pl.getConfig().addDefault("predefinedSets", new ArrayList<String>());
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
    public Component formatAccent(@NotNull String s, TextDecoration[] formatters) {
        return applyFormatter(getAccentColour(), s, serialiseFormatters(formatters));
    }

    @SuppressWarnings("ConstantConditions")
    public String getMainColour() {
        return ChatColor.translateAlternateColorCodes('&',
                getString("main", "&f"));
    }

    @Override
    public Component formatMain(@NotNull String s, TextDecoration[] formatters) {
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

    public Boolean checkForUpdates() {
        return !"false".equals(pl.getConfig().getString("checkForUpdates"));
    }
}
