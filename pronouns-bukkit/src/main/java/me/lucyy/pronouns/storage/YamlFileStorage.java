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

package me.lucyy.pronouns.storage;

import me.lucyy.common.util.UuidUtils;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class YamlFileStorage implements Storage {

    private final ProNouns pl;
    private File configFile;
    private FileConfiguration config;

    private void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public YamlFileStorage(ProNouns plugin) {
        this.pl = plugin;

        try {
            if (!pl.getDataFolder().exists()) pl.getDataFolder().mkdirs();
            configFile = new File(pl.getDataFolder(), "datastore.yml");
            if (configFile.createNewFile()) {
                InputStream defaultCfg = pl.getResource("datastore.yml");
                byte[] buffer = new byte[defaultCfg.available()];
                defaultCfg.read(buffer);
                FileOutputStream out = new FileOutputStream(configFile);
                out.write(buffer);
                out.close();
                defaultCfg.close();
            }
            config = YamlConfiguration.loadConfiguration(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getPronouns(UUID uuid) {
        List<String> list = config.getStringList("players." + uuid.toString());
		return new HashSet<>(list);
    }

    @Override
    public void setPronouns(UUID uuid, Set<PronounSet> sets) {
        ArrayList<String> setStrings = new ArrayList<>();
        for (PronounSet set : sets)  {
            try {
                PronounSet parsed = pl.getPronounHandler().fromString(set.getSubjective());
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
	public Map<UUID, Set<String>> getAllPronouns() {
    	HashMap<UUID, Set<String>> out = new HashMap<>();

    	// if this is null then something is seriously wrong
    	for (String uuid : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
    		out.put(UuidUtils.fromString(uuid), new HashSet<>(config.getStringList("players." + uuid)));
		}
		return out;
	}
}
