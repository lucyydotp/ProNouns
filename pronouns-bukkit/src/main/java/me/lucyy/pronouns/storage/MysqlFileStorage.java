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

import com.zaxxer.hikari.HikariDataSource;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.SqlInfoContainer;
import me.lucyy.pronouns.api.set.PronounSet;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.ConnectException;
import java.sql.*;
import java.util.*;

public class MysqlFileStorage implements Storage {

    private final HikariDataSource ds = new HikariDataSource();
    private final ProNouns plugin;
    private final HashMap<UUID, List<String>> cache = new HashMap<>();

    public MysqlFileStorage(ProNouns plugin) throws MysqlConnectionException {
        this.plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL driver not found! Unable to continue!");
            throw new MysqlConnectionException();
        }
        SqlInfoContainer sqlData = plugin.getConfigHandler().getSqlConnectionData();

        ds.setJdbcUrl("jdbc:mysql://" + sqlData.host + ":" + sqlData.port + "/"
                + sqlData.database + "?useSSL=false");
        ds.setUsername(sqlData.username);
        ds.setPassword(sqlData.password);
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts ", "true");

        try (Connection connection = ds.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS pronouns_players ( playerUuid VARCHAR(36), pronouns TEXT )");
            plugin.getLogger().info("Connected to MySQL.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL!");
            throw new MysqlConnectionException();
        }

    }

    public void onPlayerDisconnect(UUID uuid) {
        cache.remove(uuid);
    }

    @Override
    public List<String> getPronouns(UUID uuid) {
        return getPronouns(uuid, true);
    }

    public List<String> getPronouns(UUID uuid, boolean useCache) {
        if (useCache && cache.containsKey(uuid)) return cache.get(uuid);
        try (Connection connection = ds.getConnection()) {
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
                try (Connection connection = ds.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();

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
                try (Connection connection = ds.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                    stmt.setString(1, uuid.toString());
                    stmt.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}