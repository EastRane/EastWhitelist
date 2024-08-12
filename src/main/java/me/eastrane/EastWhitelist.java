package me.eastrane;

import me.eastrane.commands.MainCommand;
//import me.eastrane.handlers.APIHandler;
import me.eastrane.handlers.core.HandlerManager;
import me.eastrane.listeners.core.ListenerManager;
import me.eastrane.storages.MySQLStorage;
import me.eastrane.storages.YamlStorage;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DebugManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EastWhitelist extends JavaPlugin {
    private ConfigManager configManager;
    private LanguageManager languageManager;
    private DebugManager debugManager;
    private BaseStorage baseStorage;
    private ListenerManager listenerManager;
    private HandlerManager handlerManager;

    //private APIHandler apiHandler;

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

    public ConfigManager getConfigManager() {
        if (configManager == null) {
            configManager = new ConfigManager(this);
        }
        return configManager;
    }
    public LanguageManager getLanguageManager() {
        if (languageManager == null) {
            languageManager = new LanguageManager(this);
        }
        return languageManager;
    }
    public DebugManager getDebugManager() {
        if (debugManager == null) {
            debugManager = new DebugManager(this);
        }
        return debugManager;
    }
    public BaseStorage getBaseStorage() {
        if (baseStorage == null) {
            switch (getConfigManager().getStorage()) {
                case "mysql":
                    baseStorage = new MySQLStorage(this);
                    break;
                case "yaml":
                default:
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
    public HandlerManager getHandlerManager() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }

//    public APIHandler getAPIHandler() {
//        if (apiHandler == null) {
//            apiHandler = (APIHandler) getHandlerManager().getHandler("APIHandler");
//        }
//        return apiHandler;
//    }
}
