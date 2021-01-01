package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.SqlInfoContainer;
import me.lucyy.pronouns.set.PronounSet;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;

public class MysqlFileStorage implements Storage {

    private Connection connection;
    private ProNouns plugin;
    private HashMap<UUID, List<String>> cache;

    public MysqlFileStorage(ProNouns plugin) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL driver not found! Unable to continue - this plugin will not work!");
            return;
        }
        SqlInfoContainer sqlData = ConfigHandler.GetSqlConnectionData();
        plugin.getLogger().info("Attempting to connect to MySQL database...");


        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + sqlData.Host + ":" + sqlData.Port + "/"
                    + sqlData.Database + "?useSSL=false", sqlData.Username, sqlData.Password);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL! Expect things to break!");
        }

        try {
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS pronouns_players ( playerUuid VARCHAR(36), pronouns TEXT )").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> GetPronouns(UUID uuid) {
        if (cache.containsKey(uuid)) return cache.get(uuid);
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT pronouns FROM pronouns_players WHERE playerUUID=?");
            stmt.setString(1, uuid.toString());
            ResultSet set = stmt.executeQuery();
            List<String> results = new ArrayList<>();
            while (set.next()) {
                results.add(set.getString("pronouns"));
            }
            cache.put(uuid, results);
            stmt.close();
            set.close();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void SetPronouns(UUID uuid, List<PronounSet> sets) {
        List<String> stringEquivalent = new ArrayList<>();
        for (PronounSet set : sets) stringEquivalent.add(set.toString());
        cache.put(uuid, stringEquivalent);
        new BukkitRunnable() {
            @Override
            public void run() {

                connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void ClearPronouns(UUID uuid) {

    }
}