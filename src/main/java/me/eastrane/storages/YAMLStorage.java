package me.eastrane.storages;

import me.eastrane.EastWhitelist;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.storages.core.PlayerData;
import me.eastrane.utilities.DebugProvider;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class YAMLStorage extends BaseStorage {
    private final DebugProvider debugProvider;
    private final File storageFile;
    private FileConfiguration storageConfig;

    public YAMLStorage(EastWhitelist plugin) {
        super(plugin);
        this.debugProvider = plugin.getDebugProvider();
        storageFile = new File(plugin.getDataFolder(), "whitelist.yml");
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                debugProvider.sendException(e);
            }
        }
        loadStorage();
    }

    @Override
    public void loadStorage() {
        storageConfig = YamlConfiguration.loadConfiguration(storageFile);
        players.clear();
        if (storageConfig.isList("players")) {
            List<String> playerNames = storageConfig.getStringList("players");
            for (String playerName : playerNames) {
                players.put(playerName, new PlayerData("unknown", 0));
            }
            saveStorage();
            debugProvider.sendInfo("Converted player list to detailed player data structure.");
        } else if (storageConfig.isConfigurationSection("players")) {
            for (String nickname : storageConfig.getConfigurationSection("players").getKeys(false)) {
                String addedBy = storageConfig.getString("players." + nickname + ".added_by", "unknown");
                long addedAt = storageConfig.getLong("players." + nickname + ".added_at", System.currentTimeMillis());
                players.put(nickname, new PlayerData(addedBy, addedAt));
            }
            if (!players.isEmpty()) {
                debugProvider.sendInfo(players.size() + " players were loaded from YAML storage.", true);
            }
        }
    }

    private void saveStorage() {
        for (Map.Entry<String, PlayerData> entry : players.entrySet()) {
            String nickname = entry.getKey();
            PlayerData data = entry.getValue();
            storageConfig.set("players." + nickname + ".added_by", data.getAddedBy());
            storageConfig.set("players." + nickname + ".added_at", data.getAddedAt());
        }
        try {
            storageConfig.save(storageFile);
            debugProvider.sendInfo("YAML file was saved successfully.");
        } catch (IOException e) {
            debugProvider.sendException(e);
        }
    }

    @Override
    public boolean addPlayer(String player, String addedBy) {
        if (!players.containsKey(player)) {
            long currentEpochTime = System.currentTimeMillis();
            players.put(player, new PlayerData(addedBy, currentEpochTime));
            saveStorage();
            return true;
        }
        return false;
    }

    @Override
    public boolean removePlayer(String player) {
        if (players.remove(player) != null) {
            saveStorage();
            return true;
        }
        return false;
    }
}
