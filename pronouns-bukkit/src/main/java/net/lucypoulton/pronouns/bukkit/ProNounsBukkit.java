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

package net.lucypoulton.pronouns.bukkit;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import net.lucypoulton.pronouns.ProNouns;
import net.lucypoulton.pronouns.ProNounsPlatform;
import net.lucypoulton.pronouns.api.PronounHandler;
import net.lucypoulton.pronouns.config.ConfigHandler;
import net.lucypoulton.pronouns.storage.MysqlConnectionException;
import net.lucypoulton.pronouns.storage.MysqlFileStorage;
import net.lucypoulton.pronouns.storage.Storage;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ProNounsBukkit extends JavaPlugin {

    private ProNouns plugin;
    private BukkitConfigHandler configHandler;
    private Storage storage;

    public ProNouns getPlugin() {
        return plugin;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public Storage getStorage() {
        return storage;
    }

    @Override
    public void onEnable() {
        Metrics metrics = new Metrics(this, 9519);
        configHandler = new BukkitConfigHandler(this);

        metrics.addCustomChart(new SimplePie("storage_backend", () -> configHandler.getConnectionType().name()));

        ProNounsPlatform platform = new ProNounsBukkitPlatform(this);
        plugin = new ProNouns(platform);


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Path oldPapi = Path.of(getDataFolder().getParent(), "PlaceholderAPI/expansions/Expansion-pronouns.jar");
            if (Files.exists(oldPapi)) {
                try {
                    Files.delete(oldPapi);
                    getLogger().warning("Deleted the old PlaceholderAPI expansion. ProNouns doesn't use the eCloud anymore.");
                } catch (IOException e) {
                    getLogger().warning("Encountered an error trying to remove the old PlaceholderAPI expansion");
                    e.printStackTrace();
                }
                PlaceholderAPIPlugin.getInstance().reloadConf(Bukkit.getConsoleSender());
            }
            new ProNounsPapi(plugin).register();
        }
        switch (configHandler.getConnectionType()) {
            case YML:
                storage = new YamlFileStorage(this);
                break;
            case MYSQL:
                try {
                    storage = new MysqlFileStorage(plugin);
                    break;
                } catch (MysqlConnectionException e) {
                    getPluginLoader().disablePlugin(this);
                    return;
                }
        }

        plugin.onEnable();

        this.getServer().getServicesManager().register(PronounHandler.class, plugin.getPronounHandler(),
                this, ServicePriority.Normal);
    }
}