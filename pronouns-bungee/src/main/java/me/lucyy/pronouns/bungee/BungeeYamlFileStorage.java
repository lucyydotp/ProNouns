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

package me.lucyy.pronouns.bungee;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import me.lucyy.pronouns.api.set.PronounSet;
import me.lucyy.pronouns.storage.Storage;
import net.lucypoulton.squirtgun.util.UuidUtils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class BungeeYamlFileStorage implements Storage {

    private final ProNounsBungee pl;
    private File configFile;
    private Configuration config;

    private void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Failed to save data - " + e);
        }
    }

    public BungeeYamlFileStorage(ProNounsBungee plugin) {
        this.pl = plugin;

        try {
            if (!pl.getDataFolder().exists()) pl.getDataFolder().mkdirs();
            configFile = new File(pl.getDataFolder(), "datastore.yml");

            if (configFile.createNewFile()) {
                Files.copy(plugin.getResourceAsStream("datastore.yml"), configFile.toPath());
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            pl.getLogger().severe("Error while loading data store file - " + e);
        }
    }

    @Override
    public Set<String> getPronouns(UUID uuid) {
        List<String> list = config.getStringList("players." + uuid.toString());
		return new LinkedHashSet<>(list);
    }

    @Override
    public void setPronouns(UUID uuid, Set<PronounSet> sets) {
        List<String> setStrings = new ArrayList<>();
        for (PronounSet set : sets)  {
            try {
                PronounSet parsed = pl.getPlugin().getPronounHandler().fromString(set.getSubjective());
                if (parsed.equals(set)) setStrings.add(set.getSubjective());
                else setStrings.add(set.toString());
            } catch (IllegalArgumentException e) {
                setStrings.add(set.toString());
            }
        }
        config.set("players." + uuid.toString(), setStrings);
        save();
    }

    @Override
    public void clearPronouns(UUID uuid) {
        config.set("players." + uuid.toString(), new String[0]);
        save();
    }

	@Override
	public Multimap<UUID, String> getAllPronouns() {
        Multimap<UUID, String> out = MultimapBuilder.hashKeys().linkedHashSetValues().build();

    	// if this is null then something is seriously wrong
    	for (String uuid : Objects.requireNonNull(config.getSection("players")).getKeys()) {
    		out.putAll(UuidUtils.fromString(uuid), config.getStringList("players." + uuid));
		}
		return out;
	}
}
