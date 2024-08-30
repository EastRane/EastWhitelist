package me.eastrane.utilities;

import me.eastrane.EastWhitelist;
import me.eastrane.utilities.core.BaseConfig;

public class ConfigProvider extends BaseConfig {
    private String language;
    private boolean debugConsole, debugFile;
    private boolean enabled, mysqlShutdownOnException, notifyOP;
    private String storage, mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword, mysqlTable;

    public ConfigProvider(EastWhitelist plugin) {
        super(plugin);
    }

    @Override
    public void loadCustomConfig() {
        language = config.getString("language");
        debugConsole = config.getBoolean("debug.console");
        debugFile = config.getBoolean("debug.file");
        enabled = config.getBoolean("enabled");
        storage = config.getString("storage");
        mysqlShutdownOnException = config.getBoolean("mysql.shutdown_on_exception");
        mysqlHost = config.getString("mysql.host");
        mysqlPort = config.getString("mysql.port");
        mysqlDatabase = config.getString("mysql.database");
        mysqlUsername = config.getString("mysql.username");
        mysqlPassword = config.getString("mysql.password");
        mysqlTable = config.getString("mysql.table");
        notifyOP = config.getBoolean("notify_op");
    }

    @Override
    protected void reloadCustomConfig() {

    }

    public String getLanguage() {
        return language;
    }
    public boolean isDebugConsole() {
        return debugConsole;
    }
    public boolean isDebugFile() {
        return debugFile;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean value) {
        enabled = value;
        config.set("enabled", value);
        saveConfig();
    }
    public String getStorage() {
        return storage;
    }
    public boolean isMysqlShutdownOnException() {
        return mysqlShutdownOnException;
    }
    public String getMysqlHost() {
        return mysqlHost;
    }
    public String getMysqlPort() {
        return mysqlPort;
    }
    public String getMysqlDatabase() {
        return mysqlDatabase;
    }
    public String getMysqlUsername() {
        return mysqlUsername;
    }
    public String getMysqlPassword() {
        return mysqlPassword;
    }
    public String getMysqlTable() {
        return mysqlTable;
    }
    public boolean isNotifyOP() {
        return notifyOP;
    }
}
