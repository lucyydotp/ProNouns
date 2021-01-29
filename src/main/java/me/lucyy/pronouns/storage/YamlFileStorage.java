package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.set.PronounSet;
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
    public List<String> getPronouns(UUID uuid) {
        Object list = config.get("players." + uuid.toString());
        if (list == null) return new ArrayList<>();
        if (list instanceof List) return (List<String>)list;
        return Arrays.asList((String[])list);
    }

    @Override
    public void setPronouns(UUID uuid, List<PronounSet> sets) {
        ArrayList<String> setString = new ArrayList<>();
        for (PronounSet set : sets)  {
            try {
                PronounSet parsed = pl.getPronounHandler().fromString(set.subjective);
                if (parsed.equals(set)) setString.add(set.subjective);
                else setString.add(set.toString());
            } catch (IllegalArgumentException e) {
                setString.add(set.toString());
            }
        }
        config.set("players." + uuid.toString(), setString.toArray(new String[0]));
        save();
    }

    @Override
    public void clearPronouns(UUID uuid) {
        config.set("players." + uuid.toString(), new String[0]);
        save();
    }
}
