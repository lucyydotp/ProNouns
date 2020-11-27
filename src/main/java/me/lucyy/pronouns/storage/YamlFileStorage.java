package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.PronounSet;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class YamlFileStorage implements Storage {

    private ProNouns pl;
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
    public void AddPronounSet(PronounSet pronounSet) {
        List<String> list = GetAllPronouns();
        list.add(pronounSet.toString());
        config.set("pronouns", list);
        save();
    }

    @Override
    public List<String> GetPronouns(UUID uuid) {
        Object list = config.get("players." + uuid.toString());
        if (list == null) return new ArrayList<>();
        if (list instanceof List) return (List<String>)list;
        return Arrays.asList((String[])list);
    }

    @Override
    public List<String> GetAllPronouns() {
        return config.getStringList("pronouns");
    }

    @Override
    public void SetPronouns(UUID uuid, List<PronounSet> sets) {
        ArrayList<String> setString = new ArrayList<>();
        for (PronounSet set : sets) setString.add(set.Subjective);
        config.set("players." + uuid.toString(), setString.toArray(new String[0]));
        save();
    }

    @Override
    public void ClearPronouns(UUID uuid) {
        config.set("players." + uuid.toString(), new String[0]);
    }
}
