package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.SQLStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLStorage extends SQLStorage {

    public MySQLStorage(EastWhitelist plugin) {
        super(plugin);
    }

    @Override
    protected Connection openConnection() throws SQLException {
        String url = "jdbc:mysql://" + configProvider.getMysqlHost() + ":" + configProvider.getMysqlPort() + "/" + configProvider.getMysqlDatabase();
        return DriverManager.getConnection(url, configProvider.getMysqlUsername(), configProvider.getMysqlPassword());
    }
}