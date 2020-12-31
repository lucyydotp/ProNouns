package me.lucyy.pronouns.storage;

import me.lucyy.pronouns.config.ConfigHandler;
import me.lucyy.pronouns.ProNouns;
import me.lucyy.pronouns.config.SqlInfoContainer;
import me.lucyy.pronouns.set.PronounSet;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class MysqlFileStorage implements Storage {

    private Connection connection;
    private ProNouns plugin;

    public void init(ProNouns plugin) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL driver not found! Unable to continue - this plugin will not work!");
            return;
        }
        SqlInfoContainer sqlData = ConfigHandler.GetSqlConnectionData();
        this.connection = DriverManager.getConnection("jdbc:mysql://" + sqlData.Host + ":" + sqlData.Port + "/"
                + sqlData.Database + "?useSSL=false", sqlData.Username, sqlData.Password);
        // TODO continue here

        // this.connection.prepareStatement(this.dbInitString).execute();
    }
    @Override
    public List<String> GetPronouns(UUID uuid) {
        return null;
    }

    @Override
    public List<String> GetAllPronouns() {
        return null;
    }

    @Override
    public void SetPronouns(UUID uuid, List<PronounSet> set) {

    }

    @Override
    public void ClearPronouns(UUID uuid) {

    }
}