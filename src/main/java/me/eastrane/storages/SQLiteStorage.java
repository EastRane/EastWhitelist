package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.SQLStorage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteStorage extends SQLStorage {

    public SQLiteStorage(EastWhitelist plugin) {
        super(plugin);
    }

    @Override
    protected Connection openConnection() throws SQLException {
        String url = "jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + File.separator + table + ".db";
        return DriverManager.getConnection(url);
    }
}