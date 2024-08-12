package me.eastrane.storages.core;

import me.eastrane.EastWhitelist;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BaseStorage {
    protected EastWhitelist plugin;
    protected final Set<String> players = new LinkedHashSet<>();

    public BaseStorage(EastWhitelist plugin) {
        this.plugin = plugin;
    }

    public abstract void loadStorage();
    public abstract boolean addPlayer(String player);
    public abstract boolean removePlayer(String player);
    public boolean isPlayerWhitelisted(String player) {
        return players.contains(player);
    }
    public Set<String> getPlayers() {
        return players;
    }
}
