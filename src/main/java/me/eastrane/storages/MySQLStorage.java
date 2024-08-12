package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DebugManager;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQLStorage extends BaseStorage {
    private final EastWhitelist plugin;
    private final ConfigManager configManager;
    private final DebugManager debugManager;
    private String table;

    public MySQLStorage(EastWhitelist plugin) {
        super(plugin);
        this.plugin = plugin;
        configManager = plugin.getConfigManager();
        debugManager = plugin.getDebugManager();
        table = configManager.getMysqlTable();
        initializeDatabase();
        loadStorage();
    }

    private void initializeDatabase() {
        try (Connection connection = openConnection()) {
            if (connection != null) {
                debugManager.sendInfo("Successfully connected to MySQL.");
                String createTable = "CREATE TABLE IF NOT EXISTS " + table + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "nickname VARCHAR(16) NOT NULL UNIQUE" +
                        ");";
                Statement statement = connection.createStatement();
                statement.executeUpdate(createTable);
                debugManager.sendInfo("MySQL table initialized.");
            }
        } catch (SQLException e) {
            debugManager.sendException(e);
        }
    }

    private Connection openConnection() {
        try {
            String url = "jdbc:mysql://" + configManager.getMysqlHost() + ":" + configManager.getMysqlPort() + "/" + configManager.getMysqlDatabase();
            Connection connection = DriverManager.getConnection(url, configManager.getMysqlUsername(), configManager.getMysqlPassword());
            return connection;
        } catch (SQLException e) {
            debugManager.sendException(e);
            return null;
        }
    }

    @Override
    public void loadStorage() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            players.clear();
            try (Connection connection = openConnection()) {
                if (connection != null) {
                    Statement statement = connection.createStatement();
                    ResultSet playerNames = statement.executeQuery("SELECT nickname FROM " + table);
                    while (playerNames.next()) {
                        players.add(playerNames.getString("nickname"));
                    }
                    if (!players.isEmpty()) {
                        debugManager.sendInfo(players.size() + " players were loaded from the database.", true);
                    }
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
                try (Connection connection = openConnection()) {
                    if (connection != null) {
                        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + " (nickname) VALUES (?)");
                        statement.setString(1, player);
                        statement.executeUpdate();
                    }
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
                try (Connection connection = openConnection()) {
                    if (connection != null) {
                        PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE nickname = ?");
                        statement.setString(1, player);
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    debugManager.sendException(e);
                }
            });
        }
        return result;
    }
}
