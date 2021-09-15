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

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.zaxxer.hikari.HikariDataSource;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.SqlInfoContainer;
import me.lucyy.pronouns.api.set.PronounSet;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.scheduler.Task;
import net.lucypoulton.squirtgun.util.UuidUtils;

import java.sql.*;
import java.util.*;

public class MysqlFileStorage implements Storage {

    private final HikariDataSource ds = new HikariDataSource();
    private final ProNouns plugin;
    private final Multimap<UUID, String> cache = MultimapBuilder.hashKeys().linkedHashSetValues().build();

    public MysqlFileStorage(ProNouns plugin) throws MysqlConnectionException {
        this.plugin = plugin;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getPlatform().getLogger().severe("MySQL driver not found! Unable to continue!");
            throw new MysqlConnectionException();
        }
        SqlInfoContainer sqlData = plugin.getConfigHandler().getSqlConnectionData();

        ds.setJdbcUrl("jdbc:mysql://" + sqlData.getHost() + ":" + sqlData.getPort() + "/"
                + sqlData.getDatabase() + "?useSSL=false");
        ds.setUsername(sqlData.getUsername());
        ds.setPassword(sqlData.getPassword());
        ds.addDataSourceProperty("cachePrepStmts", "true");
        ds.addDataSourceProperty("prepStmtCacheSize", "250");
        ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds.addDataSourceProperty("useServerPrepStmts ", "true");

        try (Connection connection = ds.getConnection()) {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS pronouns_players ( playerUuid VARCHAR(36), pronouns TEXT )");
            plugin.getPlatform().getLogger().info("Connected to MySQL.");
        } catch (SQLException e) {
            plugin.getPlatform().getLogger().severe("Failed to connect to MySQL! - " + e);
            throw new MysqlConnectionException();
        }

    }

    public void onPlayerDisconnect(UUID uuid) {
        cache.removeAll(uuid);
    }

    @Override
    public Set<String> getPronouns(UUID uuid) {
        return getPronouns(uuid, true);
    }

    public Set<String> getPronouns(UUID uuid, boolean useCache) {
        if (useCache && cache.containsKey(uuid)) return new HashSet<>(cache.get(uuid));
        try (Connection connection = ds.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT pronouns FROM pronouns_players WHERE playerUUID=?");
            stmt.setString(1, uuid.toString());
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                cache.put(uuid, set.getString("pronouns"));
            }
            stmt.close();
            set.close();
            return new HashSet<>(cache.get(uuid));
        } catch (SQLException e) {
            plugin.getPlatform().getLogger().severe("Error getting player pronouns from MySQL - " + e);
            return null;
        }
    }

    @Override
    public void setPronouns(UUID uuid, Set<PronounSet> sets) {
        for (PronounSet set : sets) {
            cache.put(uuid, set.toString());
        }
        Task.builder()
                .action((Platform ignored) -> {
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                        stmt.setString(1, uuid.toString());
                        stmt.execute();
                        stmt.close();

                        PreparedStatement insStmt = connection.prepareStatement("INSERT INTO pronouns_players VALUES (?,?)");
                        for (PronounSet set : sets) {
                            insStmt.setString(1, uuid.toString());

                            try {
                                PronounSet parsed = plugin.getPronounHandler().fromString(set.getSubjective());
                                if (parsed.equals(set)) insStmt.setString(2, set.getSubjective());
                                else insStmt.setString(2, set.toString());
                            } catch (IllegalArgumentException e) {
                                insStmt.setString(2, set.toString());
                            }
                            insStmt.addBatch();
                        }
                        insStmt.executeBatch();
                        insStmt.close();
                    } catch (SQLException e) {
                        plugin.getPlatform().getLogger().severe("Error settings player pronouns through MySQL - " + e);
                    }
                })
                .async()
                .build().execute(plugin.getPlatform());
    }

    @Override
    public void clearPronouns(UUID uuid) {
        Task.builder()
                .action((Platform ignored) -> {
                    try (Connection connection = ds.getConnection()) {
                        PreparedStatement stmt = connection.prepareStatement("DELETE FROM pronouns_players WHERE playerUuid=?");
                        stmt.setString(1, uuid.toString());
                        stmt.execute();
                        stmt.close();
                    } catch (SQLException e) {
                        plugin.getPlatform().getLogger().severe("Error clearing player pronouns through MySQL - " + e);
                    }
                }).async().build().execute(plugin.getPlatform());
    }

    @Override
    public Multimap<UUID, String> getAllPronouns() {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("SELECT pronouns FROM pronouns_players");
            ResultSet set = stmt.executeQuery();
            Multimap<UUID, String> results = MultimapBuilder.hashKeys().hashSetValues().build();
            while (set.next()) {
                UUID uuid = UuidUtils.fromString(set.getString("playerUuid"));
                results.put(uuid, set.getString("pronouns"));
            }
            stmt.close();
            set.close();
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getPlatform().getLogger().severe("Error getting pronouns from MySQL - " + e);
        }
        return MultimapBuilder.hashKeys().hashSetValues().build();
    }
}