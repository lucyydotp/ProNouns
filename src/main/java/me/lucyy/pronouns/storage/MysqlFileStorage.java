package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.SqlInfoContainer;
import me.lucyy.pronouns.set.PronounSet;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.*;

public class MysqlFileStorage implements Storage {

    private Connection connection;
    private final ProNouns plugin;
    private final HashMap<UUID, List<String>> cache = new HashMap<>();

    public MysqlFileStorage(ProNouns plugin) {
        this.plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL driver not found! Unable to continue - this plugin will not work!");
            return;
        }
        SqlInfoContainer sqlData = plugin.getConfigHandler().getSqlConnectionData();
        plugin.getLogger().info("Attempting to connect to MySQL database...");


        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + sqlData.Host + ":" + sqlData.Port + "/"
                    + sqlData.Database + "?useSSL=false", sqlData.Username, sqlData.Password);
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL! Expect things to break!");
        }

        try {
            this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS pronouns_players ( playerUuid VARCHAR(36), pronouns TEXT )").execute();
            plugin.getLogger().info("Connected to MySQL.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onPlayerDisconnect(UUID uuid) {
        cache.remove(uuid);
    }

    @Override
    public List<String> getPronouns(UUID uuid) {
        return GetPronouns(uuid, true);
    }

    public List<String> GetPronouns(UUID uuid, boolean useCache) {
        if (useCache && cache.containsKey(uuid)) return cache.get(uuid);
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT pronouns FROM pronouns_players WHERE playerUUID=?");
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
            return null;
        }
    }

    @Override
    public void setPronouns(UUID uuid, List<PronounSet> sets) {
        List<String> stringEquivalent = new ArrayList<>();
        for (PronounSet set : sets) stringEquivalent.add(set.toString());
        cache.put(uuid, stringEquivalent);
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();
                    stmt.closeOnCompletion();

                    PreparedStatement insStmt = connection.prepareStatement("INSERT INTO pronouns_players VALUES (?,?)");
                    for (PronounSet set : sets) {
                        insStmt.setString(1, uuid.toString());

                        try {
                            PronounSet parsed = plugin.getPronounHandler().fromString(set.subjective);
                            if (parsed.equals(set)) insStmt.setString(2, set.subjective);
                            else insStmt.setString(2, set.toString());
                        } catch (IllegalArgumentException e) {
                            insStmt.setString(2, set.toString());
                        }
                        insStmt.addBatch();
                    }
                    insStmt.executeBatch();
                    insStmt.closeOnCompletion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public void clearPronouns(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();
                    stmt.closeOnCompletion();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}