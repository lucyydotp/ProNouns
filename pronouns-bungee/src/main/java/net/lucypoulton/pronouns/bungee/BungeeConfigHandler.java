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

package net.lucypoulton.pronouns.bungee;

import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.config.ConnectionType;
import net.lucypoulton.pronouns.config.SqlInfoContainer;
import net.lucypoulton.squirtgun.format.TextFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class BungeeConfigHandler implements ConfigHandler {
    private final ProNounsBungee plugin;
    private final HashMap<TextDecoration, Character> decoStrings = new HashMap<>();
    private Configuration cfg;

    public BungeeConfigHandler(ProNounsBungee plugin) throws IOException {
        this.plugin = plugin;
        reload();

        decoStrings.put(TextDecoration.OBFUSCATED, 'k');
        decoStrings.put(TextDecoration.BOLD, 'l');
        decoStrings.put(TextDecoration.STRIKETHROUGH, 'm');
        decoStrings.put(TextDecoration.UNDERLINED, 'n');
        decoStrings.put(TextDecoration.ITALIC, 'o');
    }

    public void reload() throws IOException {
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            Files.copy(plugin.getResourceAsStream("config.yml"), configFile.toPath());
        }
        cfg = ConfigurationProvider.getProvider(YamlConfiguration.class)
                .load(configFile);
    }

    private String getString(String key) {
        return getString(key, null);
    }

    private String getString(String key, String defaultVal) {
        String value = cfg.getString(key);
        if (value == null) {
            if (defaultVal == null) {
                plugin.getLogger().severe("Your config file is broken! Unable to read key '" + key + "'");
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

    public String getAccentColour() {
        return getString("accent", "&3");
    }

    @Override
    public Component formatAccent(@NotNull String s, TextDecoration... formatters) {
        return applyFormatter(getAccentColour(), s, serialiseFormatters(formatters));
    }

    public String getMainColour() {
        return getString("main", "&f");
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
        return cfg.getStringList("predefinedSets");
    }

    public List<String> getFilterPatterns() {
        return cfg.getStringList("filter.patterns");
    }

    public ConnectionType getConnectionType() {
        return ConnectionType.valueOf(getString("connection").toUpperCase());
    }

    private boolean getBoolValue(String key) {
        return !"false".equals(cfg.getString(key, "true"));
    }

    public boolean filterEnabled() {
        return getBoolValue("filter.enabled");
    }

    public boolean checkForUpdates() {
        return getBoolValue("checkForUpdates");
    }

    @Override
    public boolean shouldSyncWithCloud() {
        return getBoolValue("cloud.sync");
    }

    @Override
    public boolean shouldUploadToCloud() {
        return getBoolValue("cloud.upload");
    }

    public SqlInfoContainer getSqlConnectionData() {
        return new SqlInfoContainer(
                getString("mysql.host"),
                cfg.getInt("mysql.port", 3306),
                getString("mysql.database"),
                getString("mysql.username"),
                getString("mysql.password")
        );
    }
}
