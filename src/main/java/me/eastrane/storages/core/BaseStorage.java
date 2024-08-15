package me.eastrane.storages.core;

import me.eastrane.EastWhitelist;

import java.util.*;

public abstract class BaseStorage {
    protected EastWhitelist plugin;
    protected final Map<String, PlayerData> players = new LinkedHashMap<>();

    public BaseStorage(EastWhitelist plugin) {
        this.plugin = plugin;
    }

    public abstract void loadStorage();

    public abstract boolean addPlayer(String player, String addedBy);

    public abstract boolean removePlayer(String player);

    public boolean isPlayerWhitelisted(String player) {
        return players.containsKey(player);
    }

    public Map<String, PlayerData> getPlayers() {
        return players;
    }
}
