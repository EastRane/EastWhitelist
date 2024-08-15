package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.storages.core.PlayerData;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DebugManager;

import java.sql.*;

public class MySQLStorage extends BaseStorage {
    private final ConfigManager configManager;
    private final DebugManager debugManager;
    private final String table;

    public MySQLStorage(EastWhitelist plugin) {
        super(plugin);
        configManager = plugin.getConfigManager();
        debugManager = plugin.getDebugManager();
        table = configManager.getMysqlTable();
        initializeDatabase();
        loadStorage();
    }

    private void initializeDatabase() {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {
            debugManager.sendInfo("Successfully connected to MySQL.");
            String createTable = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "nickname VARCHAR(16) NOT NULL PRIMARY KEY," +
                    "added_by VARCHAR(16) NOT NULL," +
                    "added_at BIGINT NOT NULL" +
                    ");";
            statement.executeUpdate(createTable);
            debugManager.sendInfo("MySQL table initialized.");
        } catch (SQLException e) {
            debugManager.sendException(e);
        }
    }


    private Connection openConnection() throws SQLException {
        String url = "jdbc:mysql://" + configManager.getMysqlHost() + ":" + configManager.getMysqlPort() + "/" + configManager.getMysqlDatabase();
        return DriverManager.getConnection(url, configManager.getMysqlUsername(), configManager.getMysqlPassword());
    }

    @Override
    public void loadStorage() {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT nickname, added_by, added_at FROM " + table)) {
            while (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                String addedBy = resultSet.getString("added_by");
                long addedAt = resultSet.getLong("added_at");
                players.put(nickname, new PlayerData(addedBy, addedAt));
            }
            if (!players.isEmpty()) {
                debugManager.sendInfo(players.size() + " players were loaded from the MySQL database.", true);
            }
        } catch (SQLException e) {
            debugManager.sendException(e);
        }
    }

    @Override
    public boolean addPlayer(String player, String addedBy) {
        if (!players.containsKey(player)) {
            long currentEpochTime = System.currentTimeMillis();
            players.put(player, new PlayerData(addedBy, currentEpochTime));
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + " (nickname, added_by, added_at) VALUES (?, ?, ?)")) {
                    statement.setString(1, player);
                    statement.setString(2, addedBy);
                    statement.setLong(3, currentEpochTime);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    debugManager.sendException(e);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean removePlayer(String player) {
        if (players.remove(player) != null) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE nickname = ?")) {
                    statement.setString(1, player);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    debugManager.sendException(e);
                }
            });
            return true;
        }
        return false;
    }
}