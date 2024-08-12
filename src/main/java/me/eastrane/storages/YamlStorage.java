package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.DebugManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YamlStorage extends BaseStorage {
    private final DebugManager debugManager;
    private final EastWhitelist plugin;
    private final File storageFile;
    private FileConfiguration storageConfig;

    public YamlStorage(EastWhitelist plugin) {
        super(plugin);
        this.plugin = plugin;
        this.debugManager = plugin.getDebugManager();
        storageFile = new File(plugin.getDataFolder(), "whitelist.yml");
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                plugin.getDebugManager().sendException(e);
            }
        }
        loadStorage();
    }

    @Override
    public void loadStorage() {
        storageConfig = YamlConfiguration.loadConfiguration(storageFile);
        if (storageConfig.contains("players")) {
            List<String> playerNames = storageConfig.getStringList("players");
            players.clear();
            players.addAll(playerNames);
            if (!players.isEmpty()) {
                debugManager.sendInfo(players.size() + " players were loaded from data file.", true);
            }
        }
    }

    public void saveStorage() {
        storageConfig.set("players", new ArrayList<>(players));
        try {
            storageConfig.save(storageFile);
            debugManager.sendInfo("Data file was saved successfully.");
        } catch (IOException e) {
            debugManager.sendException(e);
        }
    }

    @Override
    public boolean addPlayer(String player) {
        boolean result = players.add(player);
        if (result) saveStorage();
        return result;
    }

    @Override
    public boolean removePlayer(String player) {
        boolean result = players.remove(player);
        if (result) saveStorage();
        return result;
    }
}
