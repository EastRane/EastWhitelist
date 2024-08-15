package me.eastrane.storages.core;

import java.sql.Timestamp;

public class PlayerData {
    private final String addedBy;
    private final long addedAt;

    public PlayerData(String addedBy, long addedAt) {
        this.addedBy = addedBy;
        this.addedAt = addedAt;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public long getAddedAt() {
        return addedAt;
    }
}
