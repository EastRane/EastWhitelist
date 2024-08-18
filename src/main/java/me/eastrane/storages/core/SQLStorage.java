package me.eastrane.storages.core;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.DebugProvider;

import java.sql.*;

public abstract class SQLStorage extends BaseStorage {
    protected final ConfigProvider configProvider;
    protected final DebugProvider debugProvider;
    protected final String table;

    public SQLStorage(EastWhitelist plugin) {
        super(plugin);
        this.configProvider = plugin.getConfigProvider();
        this.debugProvider = plugin.getDebugProvider();
        this.table = configProvider.getMysqlTable();
        initializeDatabase();
        loadStorage();
    }

    protected abstract Connection openConnection() throws SQLException;

    private void initializeDatabase() {
        try (Connection connection = openConnection();
             Statement statement = connection.createStatement()) {
            debugProvider.sendInfo("Successfully connected to the database.");
            String createTable = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                    "nickname VARCHAR(16) NOT NULL PRIMARY KEY," +
                    "added_by VARCHAR(16) NOT NULL," +
                    "added_at BIGINT NOT NULL" +
                    ");";
            statement.executeUpdate(createTable);
            debugProvider.sendInfo("Database table initialized.");
        } catch (SQLException e) {
            debugProvider.sendException(e);
        }
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
                debugProvider.sendInfo(players.size() + " players were loaded from the database.", true);
            }
        } catch (SQLException e) {
            debugProvider.sendException(e);
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
                    debugProvider.sendException(e);
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
                    debugProvider.sendException(e);
                }
            });
            return true;
        }
        return false;
    }
}
