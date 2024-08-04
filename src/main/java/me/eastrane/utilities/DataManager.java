package me.eastrane.utilities;

import me.eastrane.EastWhitelist;
//import me.eastrane.api.WhitelistData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

//public class DataManager implements WhitelistData {
public class DataManager {
    private final DebugManager debugManager;
    private final EastWhitelist plugin;
    private final File dataFile;
    private FileConfiguration dataConfig;
    private final Set<String> players = new LinkedHashSet<>();

    public DataManager(EastWhitelist plugin) {
        this.plugin = plugin;
        debugManager = plugin.getDebugManager();
        dataFile = new File(plugin.getDataFolder(), "whitelist.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                plugin.getDebugManager().sendException(e);
            }
        }
        loadData();
    }

    /**
     * Loads the whitelist data from the data file.
     */
    public void loadData() {
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        if (dataConfig.contains("players")) {
            List<String> playerNames = dataConfig.getStringList("players");
            players.clear();
            players.addAll(playerNames);
            if (!players.isEmpty()) {
                debugManager.sendInfo(players.size() + " players was loaded from data file.", true);
            }
        }
    }

    /**
     * Saves the whitelist data to the data file.
     */
    private void saveData() {
        List<String> playerNames = new ArrayList<>(players);
        dataConfig.set("players", playerNames);
        try {
            dataConfig.save(dataFile);
            debugManager.sendInfo("Data file was saved successfully.");
        } catch (IOException e) {
            debugManager.sendException(e);
        }
    }

    /**
     * Adds a player to the whitelist.
     *
     * @param player The name of the player to add.
     */
    //@Override
    public boolean addPlayer(String player) {
        boolean result = players.add(player);
        if (result) saveData();
        return result;
    }

    /**
     * Removes a player from the whitelist.
     *
     * @param player The name of the player to remove.
     */
    //@Override
    public boolean removePlayer(String player) {
        boolean result = players.remove(player);
        if (result) saveData();
        return result;
    }

    /**
     * Returns a set of all players currently whitelisted.
     *
     * @return A set of player names.
     */
    public Set<String> getPlayers() {
        return players;
    }

    /**
     * Checks if a player is currently whitelisted.
     *
     * @param player The name of the player to check.
     * @return True if the player is whitelisted, false otherwise.
     */
    //@Override
    public boolean isPlayerWhitelisted(String player) {
        return players.contains(player);
    }

}
