package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DebugManager;
import org.bukkit.Bukkit;

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
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "nickname VARCHAR(16) NOT NULL UNIQUE" +
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
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            players.clear();
            try (Connection connection = openConnection();
                 Statement statement = connection.createStatement();
                 ResultSet playerNames = statement.executeQuery("SELECT nickname FROM " + table)) {
                while (playerNames.next()) {
                    players.add(playerNames.getString("nickname"));
                }
                if (!players.isEmpty()) {
                    debugManager.sendInfo(players.size() + " players were loaded from the database.", true);
                }
            } catch (SQLException e) {
                debugManager.sendException(e);
            }
        });
    }

    @Override
    public boolean addPlayer(String player) {
        boolean result = players.add(player);
        if (result) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + " (nickname) VALUES (?)")) {
                    statement.setString(1, player);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    debugManager.sendException(e);
                }
            });
        }
        return result;
    }

    @Override
    public boolean removePlayer(String player) {
        boolean result = players.remove(player);
        if (result) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try (Connection connection = openConnection();
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE nickname = ?")) {
                    statement.setString(1, player);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    debugManager.sendException(e);
                }
            });
        }
        return result;
    }
}