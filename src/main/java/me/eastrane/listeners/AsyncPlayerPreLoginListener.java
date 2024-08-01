package me.eastrane.listeners;

import me.eastrane.EastWhitelist;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.DataManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener extends BaseListener implements Listener {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private final DataManager dataManager;

    public AsyncPlayerPreLoginListener(EastWhitelist plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        configManager = plugin.getConfigManager();
        languageManager = plugin.getLanguageManager();
        dataManager = plugin.getDataManager();
    }

    @Override
    protected boolean shouldRegister() {
        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        plugin.getDebugManager().sendWarning("async!");
        if (!configManager.isEnabled()) {
            return;
        }
        if (dataManager.isPlayerWhitelisted(event.getPlayerProfile().getName())) {
            return;
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, languageManager.Colorize(languageManager.getTranslation("messages.not_whitelisted")));
    }
}
