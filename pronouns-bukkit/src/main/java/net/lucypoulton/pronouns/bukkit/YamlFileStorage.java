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

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import net.lucypoulton.pronouns.api.set.PronounSet;
import net.lucypoulton.pronouns.api.set.old.OldPronounSet;
import net.lucypoulton.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.util.UuidUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class YamlFileStorage implements Storage {

    private final ProNounsBukkit pl;
    private File configFile;
    private FileConfiguration config;

    private void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Failed to save data - " + e);
        }
    }

    public YamlFileStorage(ProNounsBukkit plugin) {
        this.pl = plugin;

        try {
            if (!pl.getDataFolder().exists()) pl.getDataFolder().mkdirs();
            configFile = new File(pl.getDataFolder(), "datastore.yml");
            if (configFile.createNewFile()) {
                InputStream defaultCfg = pl.getResource("datastore.yml");
                // the resource should always be provided by the plugin,
                // if people are screwing with it then that's their problem
                Objects.requireNonNull(defaultCfg);
                byte[] buffer = new byte[defaultCfg.available()];
                defaultCfg.read(buffer);
                FileOutputStream out = new FileOutputStream(configFile);
                out.write(buffer);
                out.close();
                defaultCfg.close();
            }
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Error while loading data store file - " + e);
            pl.getPluginLoader().disablePlugin(pl);
        }
    }

    @Override
    public Set<String> getPronouns(UUID uuid) {
        List<String> list = config.getStringList("players." + uuid.toString());
		return new LinkedHashSet<>(list);
    }

    @Override
    public void setPronouns(UUID uuid, Set<String> sets) {
        config.set("players." + uuid.toString(), new ArrayList<>(sets));
        save();
    }

    @Override
    public void clearPronouns(UUID uuid) {
        config.set("players." + uuid.toString(), new String[0]);
        save();
    }

	@Override
	public SetMultimap<UUID, String> getAllPronouns() {
        SetMultimap<UUID, String> out = MultimapBuilder.hashKeys().linkedHashSetValues().build();

    	// if this is null then something is seriously wrong
    	for (String uuid : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
    		out.putAll(UuidUtils.fromString(uuid), config.getStringList("players." + uuid));
		}
		return out;
	}
}
