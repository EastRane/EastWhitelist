package me.eastrane.listeners;

import me.eastrane.EastWhitelist;
import me.eastrane.listeners.core.BaseListener;
import me.eastrane.storages.core.BaseStorage;
import me.eastrane.utilities.ConfigManager;
import me.eastrane.utilities.LanguageManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class AsyncPlayerPreLoginListener extends BaseListener implements Listener {
    private final ConfigManager configManager;
    private final LanguageManager languageManager;
    private final BaseStorage baseStorage;

    public AsyncPlayerPreLoginListener(EastWhitelist plugin, boolean isReloadable) {
        super(plugin, isReloadable);
        configManager = plugin.getConfigManager();
        languageManager = plugin.getLanguageManager();
        baseStorage = plugin.getBaseStorage();
    }

    @Override
    protected boolean shouldRegister() {
        return true;
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!configManager.isEnabled()) {
            return;
        }
        if (baseStorage.isPlayerWhitelisted(event.getPlayerProfile().getName())) {
            return;
        }
        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, languageManager.Colorize(languageManager.getTranslation("messages.not_whitelisted")));
    }
}
