package me.eastrane;

import me.eastrane.commands.MainCommand;
import me.eastrane.listeners.core.ListenerManager;
import me.eastrane.storages.MySQLStorage;
import me.eastrane.storages.SQLiteStorage;
import me.eastrane.storages.YamlStorage;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigProvider;
import me.eastrane.utilities.DebugProvider;
import me.eastrane.utilities.LanguageProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class EastWhitelist extends JavaPlugin {
    private ConfigProvider configProvider;
    private LanguageProvider languageProvider;
    private DebugProvider debugProvider;
    private BaseStorage baseStorage;
    private ListenerManager listenerManager;

    @Override
    public void onEnable() {
        registerManagers();
        registerHandlers();

        MainCommand mainCommand = new MainCommand(this);
        this.getCommand("eastwhitelist").setExecutor(mainCommand);
        this.getCommand("eastwhitelist").setTabCompleter(mainCommand);
    }

    private void registerManagers() {
        getConfigManager();
        getLanguageManager();
        getDebugManager();
        getBaseStorage();
        getListenerManager();
    }

    private void registerHandlers() {
        //getAPIHandler();

    }

    public ConfigProvider getConfigManager() {
        if (configProvider == null) {
            configProvider = new ConfigProvider(this);
        }
        return configProvider;
    }
    public LanguageProvider getLanguageManager() {
        if (languageProvider == null) {
            languageProvider = new LanguageProvider(this);
        }
        return languageProvider;
    }
    public DebugProvider getDebugManager() {
        if (debugProvider == null) {
            debugProvider = new DebugProvider(this);
        }
        return debugProvider;
    }
    public BaseStorage getBaseStorage() {
        if (baseStorage == null) {
            switch (getConfigManager().getStorage()) {
                case "mysql":
                    debugProvider.sendInfo("Using MySQL for data storage.", true);
                    baseStorage = new MySQLStorage(this);
                    break;
                case "sqlite":
                    debugProvider.sendInfo("Using SQLite for data storage.", true);
                    baseStorage = new SQLiteStorage(this);
                    break;
                case "yaml":
                default:
                    debugProvider.sendInfo("Using YAML for data storage.", true);
                    baseStorage = new YamlStorage(this);
                    break;
            }
        }
        return baseStorage;
    }
    public ListenerManager getListenerManager() {
        if (listenerManager == null) {
            listenerManager = new ListenerManager(this);
        }
        return listenerManager;
    }

}
